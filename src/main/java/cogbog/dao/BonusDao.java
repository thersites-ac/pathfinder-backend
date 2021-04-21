package cogbog.dao;

import cogbog.model.Bonus;

public interface BonusDao {
    int createBonus(Bonus bonus) throws Exception;

    Bonus findBonus(int id);

    Bonus updateBonus(int id, Bonus bonus) throws Exception;

    void deleteBonus(int id) throws Exception;
}
