package cogbog.model;

import cogbog.constant.ModType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "BONUS")
public class Bonus implements Serializable {

    private static final long serialVersionUID = -7874071456729262021L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column
    private ModType type;
    @Column
    private String name;
    @Column(name = "is_tmp")
    private Boolean isTemporary;

    @Column
    private Integer owner;

    public void superimpose(Bonus bonus) {
        assert id == bonus.id;
        type = bonus.type == null? type: bonus.type;
        name = bonus.name == null? name: bonus.name;
        isTemporary = bonus.isTemporary == null? isTemporary: bonus.isTemporary;
    }
}
