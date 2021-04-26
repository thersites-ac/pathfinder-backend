package cogbog.model;

import cogbog.dao.DaoData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "SKILL")
public class Skill implements Serializable, DaoData<Integer, Skill> {

    private static final long serialVersionUID = 1037748963692030350L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column
    private String name;

    @Column
    private Integer points;

    @Column
    private Integer owner;

    @Override
    public void superimpose(Skill skill) {
        assert skill.getId() == id;
        name = skill.name == null? name: skill.name;
        points = skill.points == null? points: skill.points;
        owner = skill.owner == null? owner: skill.owner;
    }

    @Override
    public Integer getId() {
        return  id;
    }

}
