package cogbog.dao.impl;

import cogbog.dao.DaoData;
import cogbog.dao.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenericDaoImpl<K, V extends DaoData<K, V>> implements GenericDao<K, V> {

    private static final String SELECT_QUERY_TEMPLATE = "Select x from %s x where x.%s = :id";
    private static final Logger logger = LoggerFactory.getLogger(GenericDao.class);
    private final String SELECT_QUERY;
    private final EntityManagerFactory ENTITY_MANAGER_FACTORY;

    private final Class<V> type;

    public GenericDaoImpl(Class<V> type) {
        logger.info("Initializing a new GenericDaoImpl");
        this.type = type;
        Optional<Field> maybeId = Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst();
        assert maybeId.isPresent();
        Field id = maybeId.get();
        SELECT_QUERY = String.format(SELECT_QUERY_TEMPLATE,
                type.getSimpleName(),
                id.getName());
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("cogbog.pathfinder", getConfigOpinions());
    }

    private Map<String, String> getConfigOpinions() {
        Map<String, String> envTransform = new HashMap<>();
        envTransform.put("DB_URL", "javax.persistence.jdbc.url");
        envTransform.put("DB_USER", "javax.persistence.jdbc.user");
        envTransform.put("DB_PASSWORD", "javax.persistence.jdbc.password");

        return System.getenv()
                .entrySet()
                .stream()
                .filter(entry -> envTransform.containsKey(entry.getKey()))
                .collect(Collectors.toMap(
                        entry -> envTransform.get(entry.getKey()),
                        Map.Entry::getValue
                ));
    }

    @Override
    public K create(V value) throws Exception {
        logger.info("Creating {}", value.toString());
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(value);
            transaction.commit();
            logger.info("Created {}", value.toString());
        } catch (Exception ex) {
            rollback(transaction, ex);
        } finally {
            manager.close();
        }
        return value.getId();
    }

    @Override
    public V find(K key) {
        logger.info("Finding entity {}", key.toString());
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        V entity;
        try {
            TypedQuery<V> query = manager.createQuery(SELECT_QUERY, type);
            query.setParameter("id", key);
            entity = query.getSingleResult();
            logger.info("Found entity {}", entity.toString());
        } finally {
            manager.close();
        }
        return entity;
    }

    @Override
    public V update(K key, V updates) throws Exception {
        logger.info("Updating entity " + key.toString() + " with properties {}", updates.toString());
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        V original = null;
        try {
            TypedQuery<V> query = manager.createQuery(SELECT_QUERY, type);
            query.setParameter("id", key);
            transaction = manager.getTransaction();
            transaction.begin();
            original = query.getSingleResult();
            original.superimpose(updates);
            manager.persist(original);
            transaction.commit();
            logger.info("Updated entity {}", original.toString());
        } catch (Exception ex) {
            rollback(transaction, ex);
        } finally {
            manager.close();
        }
        return original;
    }

    @Override
    public void delete(K key) throws Exception {
        logger.info("Deleting entity {}", key.toString());
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            TypedQuery<V> query = manager.createQuery(SELECT_QUERY, type);
            query.setParameter("id", key);
            transaction = manager.getTransaction();
            transaction.begin();
            V value = query.getSingleResult();
            manager.remove(value);
            transaction.commit();
            logger.info("Deleted entity {}", key.toString());
        } catch (Exception ex) {
            rollback(transaction, ex);
        } finally {
            manager.close();
        }
    }

    private void rollback(EntityTransaction transaction, Exception ex) throws Exception {
        if (transaction != null) {
            logger.error("Rolling back a transaction");
            transaction.rollback();
        }
        logger.error(ex.toString());
        throw ex;
    }

}
