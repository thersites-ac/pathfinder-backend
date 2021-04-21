package cogbog.dao.impl;

import cogbog.dao.ProfileDao;
import cogbog.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

public class ProfileDaoImpl implements ProfileDao {

    private static final String SELECT_QUERY = "Select p from Profile p where p.id = :id";
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY
            = Persistence.createEntityManagerFactory("cogbog.model.Profile");

    private static final Logger logger = LoggerFactory.getLogger(ProfileDao.class);

    @Override
    public int createProfile(Profile profile) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(profile);
            entityTransaction.commit();
            logger.info("Created profile: {}", profile.toString());
        } catch (Exception ex) {
            rollback(entityTransaction, ex);
        } finally {
            entityManager.close();
        }
        return profile.getId();
    }

    @Override
    public Profile findProfile(int profileId) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        try {
            TypedQuery<Profile> typedQuery = entityManager.createQuery(SELECT_QUERY, Profile.class);
            typedQuery.setParameter("id", profileId);
            Profile profile = typedQuery.getSingleResult();
            logger.info("Found profile {}", profile.toString());
            return profile;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Profile updateProfile(int profileId, Profile profile) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        logger.info("Updating profile with {}", profile);
        Profile originalProfile = null;
        try {
            TypedQuery<Profile> typedQuery = entityManager.createQuery(SELECT_QUERY, Profile.class);
            typedQuery.setParameter("id", profileId);
            originalProfile = typedQuery.getSingleResult();
            logger.info("Profile originally was {}", originalProfile.toString());
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            originalProfile.superimpose(profile);
            entityManager.persist(originalProfile);
            entityTransaction.commit();
            logger.info("Updated profile: {}", originalProfile.toString());
        } catch (Exception ex) {
            rollback(entityTransaction, ex);
        } finally {
            entityManager.close();
        }
        return originalProfile;
    }

    @Override
    public void deleteProfile(int id) throws Exception {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            TypedQuery<Profile> typedQuery = entityManager.createQuery(SELECT_QUERY, Profile.class);
            typedQuery.setParameter("id", id);
            Profile profile = typedQuery.getSingleResult();
            logger.info("Preparing to delete {}", profile.toString());
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.remove(profile);
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
