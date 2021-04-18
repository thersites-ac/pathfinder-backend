package cogbog;

import cogbog.dao.ProfileDao;
import cogbog.dao.ProfileDaoImpl;
import cogbog.model.Profile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProfileDaoImplTests {

    @Test
    public void boots() {
        new ProfileDaoImpl();
    }

    @Test
    public void canCreate() throws Exception {
        ProfileDao profileDao = new ProfileDaoImpl();
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
    }

    @Test(expected = Exception.class)
    public void cannotSpecifyKey() throws Exception {
        ProfileDao profileDao = new ProfileDaoImpl();
        Profile profile = new Profile();
        profile.setId(42);
        profileDao.createProfile(profile);
    }

    @Test
    public void canFindAfterCreate() throws Exception {
        ProfileDao profileDao = new ProfileDaoImpl();
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profileDao.findProfile(id);
    }

    @Test
    public void canUpdateAfterCreate() throws Exception {
        Profile profile = new Profile();
        ProfileDao profileDao = new ProfileDaoImpl();
        int id = profileDao.createProfile(profile);
        profile.setCharacterClass("bard");
        profileDao.updateProfile(id, profile);
        Profile updatedProfile = profileDao.findProfile(id);
        Assert.assertEquals(profile, updatedProfile);
    }

    @Test
    public void canDeleteAfterCreate() throws Exception {
        Profile profile = new Profile();
        ProfileDao profileDao = new ProfileDaoImpl();
        int id = profileDao.createProfile(profile);
        profileDao.deleteProfile(id);
    }

    @Test
    public void findIdempotent() throws Exception {
        ProfileDao profileDao = new ProfileDaoImpl();
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        Profile find1 = profileDao.findProfile(id);
        Profile find2 = profileDao.findProfile(id);
        Assert.assertEquals(find1, find2);
    }

    @Test(expected = Exception.class)
    public void findFailsAfterDelete() throws Exception {
        ProfileDao profileDao = new ProfileDaoImpl();
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profileDao.deleteProfile(id);
        profileDao.findProfile(id);
        Assert.fail();
    }

    @Test(expected = Exception.class)
    public void updateFailsAfterDelete() throws Exception {
        ProfileDao profileDao = new ProfileDaoImpl();
        Profile profile = new Profile();
        try {
            profileDao.deleteProfile(42);
        } catch (Exception e) {
        }
        profileDao.updateProfile(42, profile);
    }

    @Test
    public void canFindAfterUpdate() throws Exception {
        ProfileDao profileDao = new ProfileDaoImpl();
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profile.setCharacterName("Toby");
        profileDao.updateProfile(id, profile);
        profileDao.findProfile(id);
    }

    @Test
    public void updateIdempotent() throws Exception {
        Profile profile = new Profile();
        ProfileDao profileDao = new ProfileDaoImpl();
        int id = profileDao.createProfile(profile);
        profile.setCharacterClass("bard");
        profileDao.updateProfile(id, profile);
        Profile updatedOnce = profileDao.findProfile(id);
        profileDao.updateProfile(id, profile);
        Profile updatedTwice = profileDao.findProfile(id);
        Assert.assertEquals(updatedOnce, updatedTwice);
    }

    @Test(expected = Exception.class)
    public void cannotDeleteTwice() throws Exception {
        ProfileDao profileDao = new ProfileDaoImpl();
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profileDao.deleteProfile(id);
        profileDao.deleteProfile(id);
    }

}