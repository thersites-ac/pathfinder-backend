package cogbog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

public class ProfileDao {

    private static final String SELECT_QUERY = "Select p from Profile p where p.id = :id";
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY
            = Persistence.createEntityManagerFactory("cogbog.Profile");

    private static final Logger logger = LoggerFactory.getLogger(ProfileDao.class);

    public int createProfile(Profile profile) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(profile);
            entityTransaction.commit();
            logger.info("Created profile: {}", profile.toString());
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
        return profile.getId();
    }

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

    public Profile updateProfile(int profileId, Profile profile) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        logger.info("Updating profile with {}", profile);
        try {
            TypedQuery<Profile> typedQuery = entityManager.createQuery(SELECT_QUERY, Profile.class);
            typedQuery.setParameter("id", profileId);
            Profile originalProfile = typedQuery.getSingleResult();
            logger.info("Profile originally was {}", originalProfile.toString());
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            originalProfile.superimpose(profile);
            entityManager.persist(originalProfile);
            entityTransaction.commit();
            logger.info("Updated profile: {}", originalProfile.toString());
            return originalProfile;
        } catch (NoResultException e) {
            logger.info("Could not find profile with id {}", profileId);
            logger.info(e.toString());
            throw e;
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
