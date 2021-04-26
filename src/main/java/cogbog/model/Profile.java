package cogbog.model;

import cogbog.dao.DaoData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "PROFILE")
public class Profile implements Serializable, DaoData<Integer, Profile> {

    private static final long serialVersionUID = 3989051774004729369L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(name = "character_name")
    private String characterName;

    @Column(name = "character_class")
    private String characterClass;

    @Column
    private Integer strength;
    @Column
    private Integer dexterity;
    @Column
    private Integer constitution;
    @Column
    private Integer wisdom;
    @Column
    private Integer intelligence;
    @Column
    private Integer charisma;

    @Column
    private Integer fortitude;
    @Column
    private Integer reflex;
    @Column
    private Integer will;

    @Setter(AccessLevel.NONE)
    @OneToMany(targetEntity = Bonus.class, mappedBy = "owner")
    private List<Bonus> bonuses;

    @Setter(AccessLevel.NONE)
    @OneToMany(targetEntity = Skill.class, mappedBy = "owner")
    private List<Skill> skills;

    public void superimpose(Profile profile) {
        characterClass = profile.characterClass == null ? characterClass : profile.characterClass;
        characterName = profile.characterName == null ? characterName : profile.characterName;
        strength = profile.strength == null ? strength : profile.strength;
        dexterity = profile.dexterity == null ? dexterity : profile.dexterity;
        constitution = profile.constitution == null ? constitution : profile.constitution;
        wisdom = profile.wisdom == null ? wisdom : profile.wisdom;
        intelligence = profile.intelligence == null ? intelligence : profile.intelligence;
        charisma = profile.charisma == null ? charisma : profile.charisma;
        fortitude = profile.fortitude == null ? fortitude : profile.fortitude;
        reflex = profile.reflex == null ? reflex : profile.reflex;
        will = profile.will == null ? will : profile.will;
        bonuses = profile.bonuses == null ? bonuses : profile.bonuses;
    }

    @Override
    public Integer getId() {
        return id;
    }

}