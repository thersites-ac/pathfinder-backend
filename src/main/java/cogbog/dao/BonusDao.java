package cogbog.dao;

import cogbog.model.Bonus;

public interface BonusDao {
    public int createBonus(Bonus bonus) throws Exception;

    public Bonus findBonus(int id);

    public Bonus updateBonus(int id, Bonus bonus) throws Exception;

    public void deleteBonus(int id) throws Exception;
}
