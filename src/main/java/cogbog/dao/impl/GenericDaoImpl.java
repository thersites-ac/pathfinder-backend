package cogbog.dao.impl;

import cogbog.dao.DaoData;
import cogbog.dao.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class GenericDaoImpl<K, V extends DaoData<K, V>> implements GenericDao<K, V> {

    private static final String SELECT_QUERY_TEMPLATE = "Select x from %s x where x.%s = :id";
    private String SELECT_QUERY;
    private static final Logger logger = LoggerFactory.getLogger(GenericDao.class);
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("cogbog.pathfinder");
    private V witness;

    public GenericDaoImpl(V witness) {
        this.witness = witness;
        Optional<Field> idField = Arrays.stream(witness.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst();
        assert idField.isPresent();
        Field id = idField.get();
        SELECT_QUERY = String.format(SELECT_QUERY_TEMPLATE,
                witness.getClass().getSimpleName(),
                id.getName());
    }

    @Override
    public K create(V value) throws Exception {
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(value);
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction, ex);
        } finally {
            manager.close();
        }
        return value.getId();
    }

    @Override
    public V find(K key) {
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        V entity;
        try {
            TypedQuery<? extends DaoData> query = manager.createQuery(SELECT_QUERY, witness.getClass());
            query.setParameter("id", key);
            entity = (V) query.getSingleResult();
            logger.info("Found entity {}", entity.toString());
        } finally {
            manager.close();
        }
        return entity;
    }

    @Override
    public V update(K key, V updates) throws Exception {
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        V original = null;
        try {
            TypedQuery<? extends DaoData> query = manager.createQuery(SELECT_QUERY, witness.getClass());
            query.setParameter("id", key);
            transaction = manager.getTransaction();
            transaction.begin();
            original = (V) query.getSingleResult();
            original.superimpose(updates);
            manager.persist(original);
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction, ex);
        } finally {
            manager.close();
        }
        return original;
    }

    @Override
    public void delete(K key) throws Exception {
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            TypedQuery<? extends DaoData> query = manager.createQuery(SELECT_QUERY, witness.getClass());
            query.setParameter("id", key);
            transaction = manager.getTransaction();
            transaction.begin();
            V value = (V) query.getSingleResult();
            manager.remove(value);
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction, ex);
        } finally {
            manager.close();
        }
    }

    private void rollback(EntityTransaction transaction, Exception ex) throws Exception {
        if (transaction != null)
            transaction.rollback();
        logger.error(ex.toString());
        throw ex;
    }

}
