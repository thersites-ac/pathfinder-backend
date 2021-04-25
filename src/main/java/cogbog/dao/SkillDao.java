package cogbog.dao;

import cogbog.model.Skill;

public interface SkillDao {
    public int createSkill(Skill skill) throws Exception;

    public Skill findSkill(int id);

    public Skill updateSkill(int id, Skill skill) throws Exception;

    void deleteSkill(int id) throws Exception;
}
