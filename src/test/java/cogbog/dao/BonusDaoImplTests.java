package cogbog.dao;

import cogbog.dao.impl.BonusDaoImpl;
import cogbog.model.Bonus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BonusDaoImplTests {

    @Test
    public void boots() {
        new BonusDaoImpl();
    }

    @Test
    public void canCreate() throws Exception {
        BonusDao bonusDao = new BonusDaoImpl();
        Bonus bonus = new Bonus();
        int id = bonusDao.createBonus(bonus);
    }

    @Test(expected = Exception.class)
    public void cannotSpecifyKey() throws Exception {
        BonusDao bonusDao = new BonusDaoImpl();
        Bonus bonus = new Bonus();
        bonus.setId(42);
        bonusDao.createBonus(bonus);
    }

    @Test
    public void canFindAfterCreate() throws Exception {
        BonusDao bonusDao = new BonusDaoImpl();
        Bonus bonus = new Bonus();
        int id = bonusDao.createBonus(bonus);
        bonusDao.findBonus(id);
    }

    @Test
    public void canUpdateAfterCreate() throws Exception {
        Bonus bonus = new Bonus();
        BonusDao bonusDao = new BonusDaoImpl();
        int id = bonusDao.createBonus(bonus);
        bonus.setName("bless");
        bonusDao.updateBonus(id, bonus);
        Bonus updatedBonus = bonusDao.findBonus(id);
        Assert.assertEquals(bonus, updatedBonus);
    }

    @Test
    public void canDeleteAfterCreate() throws Exception {
        Bonus bonus = new Bonus();
        BonusDao bonusDao = new BonusDaoImpl();
        int id = bonusDao.createBonus(bonus);
        bonusDao.deleteBonus(id);
    }

    @Test
    public void findIdempotent() throws Exception {
        BonusDao bonusDao = new BonusDaoImpl();
        Bonus bonus = new Bonus();
        int id = bonusDao.createBonus(bonus);
        Bonus find1 = bonusDao.findBonus(id);
        Bonus find2 = bonusDao.findBonus(id);
        Assert.assertEquals(find1, find2);
    }

    @Test(expected = Exception.class)
    public void findFailsAfterDelete() throws Exception {
        BonusDao bonusDao = new BonusDaoImpl();
        Bonus bonus = new Bonus();
        int id = bonusDao.createBonus(bonus);
        bonusDao.deleteBonus(id);
        bonusDao.findBonus(id);
        Assert.fail();
    }

    @Test(expected = Exception.class)
    public void updateFailsAfterDelete() throws Exception {
        BonusDao bonusDao = new BonusDaoImpl();
        Bonus bonus = new Bonus();
        int id = bonusDao.createBonus(bonus);
        bonusDao.deleteBonus(id);
        bonusDao.updateBonus(id, bonus);
    }

    @Test
    public void canFindAfterUpdate() throws Exception {
        BonusDao bonusDao = new BonusDaoImpl();
        Bonus bonus = new Bonus();
        int id = bonusDao.createBonus(bonus);
        bonus.setName("Bless");
        bonusDao.updateBonus(id, bonus);
        bonusDao.findBonus(id);
    }

    @Test
    public void updateIdempotent() throws Exception {
        Bonus bonus = new Bonus();
        BonusDao bonusDao = new BonusDaoImpl();
        int id = bonusDao.createBonus(bonus);
        bonus.setName("bless");
        bonusDao.updateBonus(id, bonus);
        Bonus updatedOnce = bonusDao.findBonus(id);
        bonusDao.updateBonus(id, bonus);
        Bonus updatedTwice = bonusDao.findBonus(id);
        Assert.assertEquals(updatedOnce, updatedTwice);
    }

    @Test(expected = Exception.class)
    public void cannotDeleteTwice() throws Exception {
        BonusDao bonusDao = new BonusDaoImpl();
        Bonus bonus = new Bonus();
        int id = bonusDao.createBonus(bonus);
        bonusDao.deleteBonus(id);
        bonusDao.deleteBonus(id);
    }

}
