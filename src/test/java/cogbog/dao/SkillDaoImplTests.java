package cogbog.dao;

import cogbog.dao.impl.GenericDaoImpl;
import cogbog.model.Skill;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SkillDaoImplTests {

    private static GenericDao<Integer, Skill> skillDao;

    @BeforeClass
    public static void init() {
        skillDao = new GenericDaoImpl<>(new Skill());
    }

    @Test
    public void canCreate() throws Exception {
        skillDao.create(new Skill());
    }

    @Test
    public void canFindAfterCreate() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        skillDao.find(id);
    }

    @Test
    public void canUpdateAfterCreate() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        skill.setName("bless");
        skillDao.update(id, skill);
        Skill updatedSkill = skillDao.find(id);
        Assert.assertEquals(skill, updatedSkill);
    }

    @Test
    public void canDeleteAfterCreate() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        skillDao.delete(id);
    }

    @Test
    public void findIdempotent() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        Skill find1 = skillDao.find(id);
        Skill find2 = skillDao.find(id);
        Assert.assertEquals(find1, find2);
    }

    @Test(expected = Exception.class)
    public void findFailsAfterDelete() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        skillDao.delete(id);
        skillDao.find(id);
        Assert.fail();
    }

    @Test(expected = Exception.class)
    public void updateFailsAfterDelete() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        skillDao.delete(id);
        skillDao.update(id, skill);
    }

    @Test
    public void canFindAfterUpdate() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        skill.setName("Bless");
        skillDao.update(id, skill);
        skillDao.find(id);
    }

    @Test
    public void updateIdempotent() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        skill.setName("bless");
        skillDao.update(id, skill);
        Skill updatedOnce = skillDao.find(id);
        skillDao.update(id, skill);
        Skill updatedTwice = skillDao.find(id);
        Assert.assertEquals(updatedOnce, updatedTwice);
    }

    @Test(expected = Exception.class)
    public void cannotDeleteTwice() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.create(skill);
        skillDao.delete(id);
        skillDao.delete(id);
    }

}
