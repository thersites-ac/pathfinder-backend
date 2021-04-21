package cogbog.dao.impl;

import cogbog.dao.BonusDao;
import cogbog.model.Bonus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

public class BonusDaoImpl implements BonusDao {

    private static final String SELECT_QUERY = "Select b from Bonus b where b.id = :id";
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY
            = Persistence.createEntityManagerFactory("cogbog.model.Bonus");

    private static final Logger logger = LoggerFactory.getLogger(BonusDao.class);

    @Override
    public int createBonus(Bonus bonus) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(bonus);
            entityTransaction.commit();
            logger.info("Created bonus: {}", bonus.toString());
        } catch (Exception ex) {
            rollback(entityTransaction, ex);
        } finally {
            entityManager.close();
        }
        return bonus.getId();
    }

    @Override
    public Bonus findBonus(int id) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        try {
            TypedQuery<Bonus> typedQuery = entityManager.createQuery(SELECT_QUERY, Bonus.class);
            typedQuery.setParameter("id", id);
            Bonus bonus = typedQuery.getSingleResult();
            logger.info("Found bonus {}", bonus.toString());
            return bonus;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Bonus updateBonus(int id, Bonus bonus) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        logger.info("Updating bonus with {}", bonus);
        Bonus original = null;
        try {
            TypedQuery<Bonus> typedQuery = entityManager.createQuery(SELECT_QUERY, Bonus.class);
            typedQuery.setParameter("id", id);
            original = typedQuery.getSingleResult();
            logger.info("Bonus originally was {}", original.toString());
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            original.superimpose(bonus);
            entityManager.persist(original);
            entityTransaction.commit();
            logger.info("Updated bonus: {}", original.toString());
        } catch (Exception ex) {
            rollback(entityTransaction, ex);
        } finally {
            entityManager.close();
        }
        return original;
    }

    @Override
    public void deleteBonus(int id) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            TypedQuery<Bonus> typedQuery = entityManager.createQuery(SELECT_QUERY, Bonus.class);
            typedQuery.setParameter("id", id);
            Bonus bonus = typedQuery.getSingleResult();
            logger.info("Preparing to delete {}", bonus.toString());
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.remove(bonus);
            entityTransaction.commit();
        } catch (Exception ex) {
            rollback(entityTransaction, ex);
        } finally {
            entityManager.close();
        }
    }

    private void rollback(EntityTransaction entityTransaction, Exception ex) throws Exception {
        if (entityTransaction != null) {
            entityTransaction.rollback();
        }
        throw ex;
    }

}
