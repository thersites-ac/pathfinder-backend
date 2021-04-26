package cogbog.constant;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.Serializable;

import static cogbog.constant.ModType.*;

@RunWith(JUnit4.class)
public class ModTypeTests {

    @Test
    public void toStringWorks() {
        STAT.toString();
        SAVE.toString();
        SKILL.toString();
    }

    @Test
    public void canSerialize() {
        Gson gson = new Gson();
        gson.toJson(STAT);
        gson.toJson(SAVE);
        gson.toJson(SKILL);
    }

    private static class JsonData implements Serializable {
        private static final long serialVersionUID = 5342469627083297681L;
        private ModType modType;
        public void setModType(ModType modType) {
            this.modType = modType;
        }
    }

    @Test
    public void canDeserialize() {
        Gson gson = new Gson();
        gson.fromJson("{\"modType\": \"STAT\"}", JsonData.class);
        gson.fromJson("{\"modType\": \"SAVE\"}", JsonData.class);
        gson.fromJson("{\"modType\": \"SKILL\"}", JsonData.class);
    }

    @Test
    public void rejectsInvalidInstances() {
        Gson gson = new Gson();
        JsonData data = gson.fromJson("{\"modType\": \"DOGE\"}", JsonData.class);
        data = null;
    }

}
