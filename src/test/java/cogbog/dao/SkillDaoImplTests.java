package cogbog.dao;

import cogbog.dao.impl.SkillDaoImpl;
import cogbog.model.Skill;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SkillDaoImplTests {

    private static SkillDao skillDao;

    @BeforeClass
    public static void init() {
        skillDao = new SkillDaoImpl();
    }

    @Test
    public void boots() {
        new SkillDaoImpl();
    }

    @Test
    public void canCreate() throws Exception {
        skillDao.createSkill(new Skill());
    }

    @Test
    public void canFindAfterCreate() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        skillDao.findSkill(id);
    }

    @Test
    public void canUpdateAfterCreate() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        skill.setName("bless");
        skillDao.updateSkill(id, skill);
        Skill updatedSkill = skillDao.findSkill(id);
        Assert.assertEquals(skill, updatedSkill);
    }

    @Test
    public void canDeleteAfterCreate() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        skillDao.deleteSkill(id);
    }

    @Test
    public void findIdempotent() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        Skill find1 = skillDao.findSkill(id);
        Skill find2 = skillDao.findSkill(id);
        Assert.assertEquals(find1, find2);
    }

    @Test(expected = Exception.class)
    public void findFailsAfterDelete() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        skillDao.deleteSkill(id);
        skillDao.findSkill(id);
        Assert.fail();
    }

    @Test(expected = Exception.class)
    public void updateFailsAfterDelete() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        skillDao.deleteSkill(id);
        skillDao.updateSkill(id, skill);
    }

    @Test
    public void canFindAfterUpdate() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        skill.setName("Bless");
        skillDao.updateSkill(id, skill);
        skillDao.findSkill(id);
    }

    @Test
    public void updateIdempotent() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        skill.setName("bless");
        skillDao.updateSkill(id, skill);
        Skill updatedOnce = skillDao.findSkill(id);
        skillDao.updateSkill(id, skill);
        Skill updatedTwice = skillDao.findSkill(id);
        Assert.assertEquals(updatedOnce, updatedTwice);
    }

    @Test(expected = Exception.class)
    public void cannotDeleteTwice() throws Exception {
        Skill skill = new Skill();
        int id = skillDao.createSkill(skill);
        skillDao.deleteSkill(id);
        skillDao.deleteSkill(id);
    }

}
