package cogbog.dao;

import cogbog.dao.impl.GenericDaoImpl;
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

    private static GenericDao<Integer, Profile> profileDao;
    private static GenericDao<Integer, Bonus> bonusDao;
    private static GenericDao<Integer, Skill> skillDao;

    @BeforeClass
    public static void init() {
//        profileDao = new ProfileDaoImpl();
//        bonusDao = new BonusDaoImpl();
//        skillDao = new SkillDaoImpl();
        profileDao = new GenericDaoImpl<>(new Profile());
        bonusDao = new GenericDaoImpl<>(new Bonus());
        skillDao = new GenericDaoImpl<>(new Skill());
    }

    @Test
    public void canCreate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
    }

    @Test
    public void canFindAfterCreate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        profileDao.find(id);
    }

    @Test
    public void canUpdateAfterCreate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        profile.setCharacterClass("bard");
        profileDao.update(id, profile);
    }

    @Test
    public void canDeleteAfterCreate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        profileDao.delete(id);
    }

    @Test
    public void findIdempotent() throws Exception {
        Profile profile = new Profile();
        profile.setCharacterClass("Investigator");
        int id = profileDao.create(profile);
        Profile find1 = profileDao.find(id);
        Profile find2 = profileDao.find(id);
        Assert.assertEquals(find1.toString(), find2.toString());
    }

    @Test(expected = Exception.class)
    public void findFailsAfterDelete() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        profileDao.delete(id);
        profileDao.find(id);
        Assert.fail();
    }

    @Test(expected = Exception.class)
    public void updateFailsAfterDelete() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        profileDao.delete(id);
        profileDao.update(id, profile);
    }

    @Test
    public void canFindAfterUpdate() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        profile.setCharacterName("Toby");
        profileDao.update(id, profile);
        profileDao.find(id);
    }

    @Test
    public void updateIdempotent() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        profile.setCharacterClass("bard");
        profileDao.update(id, profile);
        Profile updatedOnce = profileDao.find(id);
        profileDao.update(id, profile);
        Profile updatedTwice = profileDao.find(id);
        Assert.assertEquals(updatedOnce.toString(), updatedTwice.toString());
    }

    @Test(expected = Exception.class)
    public void cannotDeleteTwice() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        profileDao.delete(id);
        profileDao.delete(id);
    }

    @Test
    public void findTurnsUpLinkedSkills() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        Skill skill = new Skill();
        skill.setOwner(id);
        skillDao.create(skill);
        Assert.assertNull(profile.getSkills());
        profile = profileDao.find(id);
        Assert.assertNotNull(profile.getSkills());
        Assert.assertTrue(profile.getSkills().contains(skill));
    }

    @Test
    public void findTurnsUpLinkedBonuses() throws Exception {
        Profile profile = new Profile();
        int id = profileDao.create(profile);
        Bonus bonus = new Bonus();
        bonus.setOwner(id);
        bonusDao.create(bonus);
        Assert.assertNull(profile.getBonuses());
        profile = profileDao.find(id);
        Assert.assertNotNull(profile.getBonuses());
        Assert.assertTrue(profile.getBonuses().contains(bonus));
    }
}