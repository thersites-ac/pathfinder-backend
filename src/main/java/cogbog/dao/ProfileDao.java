package cogbog.dao;

import cogbog.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

public class ProfileDao {

    private static final EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("cogbog.model.Profile");

    private static final Logger logger = LoggerFactory.getLogger(ProfileDao.class);

    public int createProfile(Profile profile) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(profile);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            logger.error("{}", e);
        } finally {
            entityManager.close();
        }
        return profile.getId();
    }

    public Profile getProfile(int profileId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String query = "Select p from Profile p where p.id = :id";
        TypedQuery<Profile> typedQuery = entityManager.createQuery(query, Profile.class);
        typedQuery.setParameter("id", profileId);
        Profile profile = typedQuery.getSingleResult();
        return profile;
    }
}
