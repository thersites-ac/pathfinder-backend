package cogbog.constant;

import java.io.Serializable;

public enum ModType implements Serializable {

    STAT, SAVE, SKILL;

    @Override
    public String toString() {
        if (this == STAT)
            return "STAT";
        if (this == SAVE)
            return "SAVE";
        if (this == SKILL)
            return "SKILL";
        else throw new RuntimeException("Unimplemented toString case");
    }
}
