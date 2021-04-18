package cogbog;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "PROFILE")
public class Profile implements Serializable {

    private static final long serialVersionUID = 3989051774004729369L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
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

    public void superimpose(Profile profile) {
        assert id == profile.id;
        if (profile.characterClass != null)
            characterClass = profile.characterClass;
        if (profile.characterName != null)
            characterName = profile.characterName;
        if (profile.strength != null)
            strength = profile.strength;
        if (profile.dexterity != null)
            dexterity = profile.dexterity;
        if (profile.constitution != null)
            constitution = profile.constitution;
        if (profile.wisdom != null)
            wisdom = profile.wisdom;
        if (profile.intelligence != null)
            intelligence = profile.intelligence;
        if (profile.charisma != null)
            charisma = profile.charisma;
    }

}
