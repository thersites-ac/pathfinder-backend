package cogbog.service;

import cogbog.model.Profile;
import cogbog.service.impl.GenericRestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProfileRestServiceTests {

    private static RestService service;

    @BeforeClass
    public static void init() {
        service = new GenericRestService<>(Profile.class);
    }

    @Test
    public void boots() {}

    @Test
    public void canCreate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = new Profile();
        service.create(objectMapper.writeValueAsString(profile));
    }

    @Test
    public void canFindAfterCreate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = new Profile();
        profile.setCharacterClass("Investigator");
        profile.setCharacterName("Toby");
        String id = service.create(objectMapper.writeValueAsString(profile));
        Profile found = objectMapper.readValue(service.find(id), Profile.class);
        Assert.assertEquals(profile.getCharacterName(), found.getCharacterName());
        Assert.assertEquals(profile.getCharacterClass(), found.getCharacterClass());
    }

    @Test
    public void canUpdateAfterCreate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = new Profile();
        profile.setCharacterClass("Investigator");
        profile.setCharacterName("Toby");
        profile.setConstitution(8);
        String id = service.create(objectMapper.writeValueAsString(profile));
        Profile updates = new Profile();
        updates.setConstitution(10);
        updates.setStrength(10);
        Profile updated = objectMapper.readValue(service.update(id, objectMapper.writeValueAsString(updates)), Profile.class);
        Profile found = objectMapper.readValue(service.find(id), Profile.class);
        Assert.assertEquals(updated, found);
    }

    @Test
    public void canDeleteAfterCreate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = new Profile();
        String id = service.create(objectMapper.writeValueAsString(profile));
        service.delete(id);
    }

    @Test(expected = Exception.class)
    public void cannotDeleteTwice() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = new Profile();
        String id = service.create(objectMapper.writeValueAsString(profile));
        service.delete(id);
        service.delete(id);
    }

    @Test
    public void updateIdempotent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = new Profile();
        String id = service.create(objectMapper.writeValueAsString(profile));
        profile.setCharacterClass("Rogue");
        String updatedOnce = service.update(id, objectMapper.writeValueAsString(profile));
        String updatedTwice = service.update(id, updatedOnce);
        Assert.assertEquals(updatedOnce, updatedTwice);
    }

    @Test(expected = Exception.class)
    public void cannotFindAfterDelete() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = new Profile();
        String id = service.create(objectMapper.writeValueAsString(profile));
        service.delete(id);
        service.find(id);
    }

    @Test(expected = Exception.class)
    public void cannotUpdateAfterDelete() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = new Profile();
        String id = service.create(objectMapper.writeValueAsString(profile));
        service.delete(id);
        profile.setCharacterClass("Bard");
        service.update(id, objectMapper.writeValueAsString(profile));
    }

}
