package cogbog.dao.impl;

import cogbog.dao.SkillDao;
import cogbog.model.Profile;
import cogbog.model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

public class SkillDaoImpl implements SkillDao {

    private static final String SELECT_QUERY = "Select s from Skill s where s.id = :id";
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("cogbog.model.pathfinder");
    private static final Logger logger = LoggerFactory.getLogger(SkillDao.class);

    @Override
    public int createSkill(Skill skill) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(skill);
            entityTransaction.commit();
            logger.info("Created skill {}", skill.toString());
        } catch (Exception ex) {
            logger.error(ex.toString());
            rollback(entityTransaction, ex);
        } finally {
            entityManager.close();
        }
        return skill.getId();
    }

    private void rollback(EntityTransaction entityTransaction, Exception ex) throws Exception {
        if (entityTransaction != null)
            entityTransaction.rollback();
        throw ex;
    }

    @Override
    public Skill findSkill(int id) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        Skill skill = null;
        try {
            TypedQuery<Skill> query = entityManager.createQuery(SELECT_QUERY, Skill.class);
            query.setParameter("id", id);
            skill = query.getSingleResult();
            logger.info("Found skill {}", skill.toString());
        } finally {
            entityManager.close();
        }
        return skill;
    }

    @Override
    public Skill updateSkill(int id, Skill skill) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        Skill original = null;
        try {
            TypedQuery<Skill> query = entityManager.createQuery(SELECT_QUERY, Skill.class);
            query.setParameter("id", id);
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            original = query.getSingleResult();
            original.superimpose(skill);
            entityManager.persist(original);
            entityTransaction.commit();
        } catch (Exception ex) {
            logger.error(ex.toString());
            rollback(entityTransaction, ex);
        } finally {
            entityManager.close();
        }
        return original;
    }

    @Override
    public void deleteSkill(int id) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            TypedQuery<Skill> query = entityManager.createQuery(SELECT_QUERY, Skill.class);
            query.setParameter("id", id);
            Skill skill = query.getSingleResult();
            logger.info("Deleting skill {}", skill.toString());
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.remove(skill);
            entityTransaction.commit();
        } catch (Exception ex) {
            logger.error(ex.toString());
            rollback(entityTransaction, ex);
        } finally {
            entityManager.close();
        }
    }
}
