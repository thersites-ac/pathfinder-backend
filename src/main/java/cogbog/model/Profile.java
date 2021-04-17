package cogbog.model;

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
    private int strength;

    @Column
    private int dexterity;

    @Column
    private int constitution;

    @Column
    private int wisdom;

    @Column
    private int intelligence;

    @Column
    private int charisma;

}
