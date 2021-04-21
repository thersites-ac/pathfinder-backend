package cogbog.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "SKILL")
public class Skill implements Serializable {

    private static final long serialVersionUID = 1037748963692030350L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column
    private String name;

    @Column
    private Integer points;

    @Column
    private Integer owner;
}
