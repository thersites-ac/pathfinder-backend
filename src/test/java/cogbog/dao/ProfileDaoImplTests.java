package cogbog.dao;

import cogbog.dao.impl.BonusDaoImpl;
import cogbog.dao.impl.ProfileDaoImpl;
import cogbog.dao.impl.SkillDaoImpl;
import cogbog.model.Bonus;
import cogbog.model.Profile;
import cogbog.model.Skill;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProfileDaoImplTests {

    private static ProfileDao profileDao;
    private static BonusDao bonusDao;
    private static SkillDao skillDao;

    @BeforeClass
    public static void init() {
        profileDao = new ProfileDaoImpl();
        bonusDao = new BonusDaoImpl();
        skillDao = new SkillDaoImpl();
    }

    @Test
    public void boots() {
        new ProfileDaoImpl();
    }

    @Test
    public void canCreate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
    }

    @Test
    public void canFindAfterCreate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profileDao.findProfile(id);
    }

    @Test
    public void canUpdateAfterCreate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profile.setCharacterClass("bard");
        profileDao.updateProfile(id, profile);
    }

    @Test
    public void canDeleteAfterCreate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profileDao.deleteProfile(id);
    }

    @Test
    public void findIdempotent() throws Exception {
        Profile profile = new Profile();
        profile.setCharacterClass("Investigator");
        int id = profileDao.createProfile(profile);
        Profile find1 = profileDao.findProfile(id);
        Profile find2 = profileDao.findProfile(id);
        Assert.assertEquals(find1.toString(), find2.toString());
    }

    @Test(expected = Exception.class)
    public void findFailsAfterDelete() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profileDao.deleteProfile(id);
        profileDao.findProfile(id);
        Assert.fail();
    }

    @Test(expected = Exception.class)
    public void updateFailsAfterDelete() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profileDao.deleteProfile(id);
        profileDao.updateProfile(id, profile);
    }

    @Test
    public void canFindAfterUpdate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profile.setCharacterName("Toby");
        profileDao.updateProfile(id, profile);
        profileDao.findProfile(id);
    }

    @Test
    public void updateIdempotent() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profile.setCharacterClass("bard");
        profileDao.updateProfile(id, profile);
        Profile updatedOnce = profileDao.findProfile(id);
        profileDao.updateProfile(id, profile);
        Profile updatedTwice = profileDao.findProfile(id);
        Assert.assertEquals(updatedOnce.toString(), updatedTwice.toString());
    }

    @Test(expected = Exception.class)
    public void cannotDeleteTwice() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        profileDao.deleteProfile(id);
        profileDao.deleteProfile(id);
    }

    @Test
    public void findProfileTurnsUpLinkedSkills() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        Skill skill = new Skill();
        skill.setOwner(id);
        skillDao.createSkill(skill);
        Assert.assertNull(profile.getSkills());
        profile = profileDao.findProfile(id);
        Assert.assertNotNull(profile.getSkills());
        Assert.assertTrue(profile.getSkills().contains(skill));
    }

    @Test
    public void findProfileTurnsUpLinkedBonuses() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.createProfile(profile);
        Bonus bonus = new Bonus();
        bonus.setOwner(id);
        bonusDao.createBonus(bonus);
        Assert.assertNull(profile.getBonuses());
        profile = profileDao.findProfile(id);
        Assert.assertNotNull(profile.getBonuses());
        Assert.assertTrue(profile.getBonuses().contains(bonus));
    }
}