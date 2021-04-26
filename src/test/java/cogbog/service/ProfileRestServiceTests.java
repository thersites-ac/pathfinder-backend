package cogbog.service;

import cogbog.model.Profile;
import cogbog.service.impl.GenericRestService;
import com.google.gson.Gson;
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
        service = new GenericRestService<Profile>(new Profile());
    }

    @Test
    public void boots() {}

    @Test
    public void createAfterFind() throws Exception {
        Gson gson = new Gson();
        Profile profile = new Profile();
        profile.setCharacterClass("Investigator");
        profile.setCharacterName("Toby");
        String id = service.create(gson.toJson(profile));
        Profile found = gson.fromJson(service.find(id), Profile.class);
        Assert.assertEquals(profile.getCharacterName(), found.getCharacterName());
        Assert.assertEquals(profile.getCharacterClass(), found.getCharacterClass());
    }

}
