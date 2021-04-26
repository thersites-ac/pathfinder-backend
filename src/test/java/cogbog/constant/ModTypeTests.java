package cogbog.constant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void canSerialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(STAT);
        objectMapper.writeValueAsString(SAVE);
        objectMapper.writeValueAsString(SKILL);
    }

    private static class JsonData implements Serializable {
        private static final long serialVersionUID = 5342469627083297681L;
        private ModType modType;
        public void setModType(ModType modType) {
            this.modType = modType;
        }
    }

    @Test
    public void canDeserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue("{\"modType\": \"STAT\"}", JsonData.class);
        objectMapper.readValue("{\"modType\": \"SAVE\"}", JsonData.class);
        objectMapper.readValue("{\"modType\": \"SKILL\"}", JsonData.class);
    }

    @Test(expected = JsonProcessingException.class)
    public void rejectsInvalidInstances() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonData data = objectMapper.readValue("{\"modType\": \"DOGE\"}", JsonData.class);
    }

}
