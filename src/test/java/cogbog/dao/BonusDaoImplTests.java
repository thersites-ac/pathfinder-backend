package cogbog.dao;

import cogbog.dao.impl.GenericDaoImpl;
import cogbog.model.Bonus;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BonusDaoImplTests {

    private static GenericDao<Integer, Bonus> bonusDao;

    @BeforeClass
    public static void init() {
        bonusDao = new GenericDaoImpl<Integer, Bonus>(new Bonus()); //BonusDaoImpl();
    }

    @Test
    public void canCreate() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
    }

    @Test
    public void canFindAfterCreate() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        bonusDao.find(id);
    }

    @Test
    public void canUpdateAfterCreate() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        bonus.setName("bless");
        bonusDao.update(id, bonus);
        Bonus updatedBonus = bonusDao.find(id);
        Assert.assertEquals(bonus, updatedBonus);
    }

    @Test
    public void canDeleteAfterCreate() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        bonusDao.delete(id);
    }

    @Test
    public void findIdempotent() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        Bonus find1 = bonusDao.find(id);
        Bonus find2 = bonusDao.find(id);
        Assert.assertEquals(find1, find2);
    }

    @Test(expected = Exception.class)
    public void findFailsAfterDelete() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        bonusDao.delete(id);
        bonusDao.find(id);
        Assert.fail();
    }

    @Test(expected = Exception.class)
    public void updateFailsAfterDelete() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        bonusDao.delete(id);
        bonusDao.update(id, bonus);
    }

    @Test
    public void canFindAfterUpdate() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        bonus.setName("Bless");
        bonusDao.update(id, bonus);
        bonusDao.find(id);
    }

    @Test
    public void updateIdempotent() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        bonus.setName("bless");
        bonusDao.update(id, bonus);
        Bonus updatedOnce = bonusDao.find(id);
        bonusDao.update(id, bonus);
        Bonus updatedTwice = bonusDao.find(id);
        Assert.assertEquals(updatedOnce, updatedTwice);
    }

    @Test(expected = Exception.class)
    public void cannotDeleteTwice() throws Exception {
        Bonus bonus = new Bonus();
        int id = bonusDao.create(bonus);
        bonusDao.delete(id);
        bonusDao.delete(id);
    }

}
