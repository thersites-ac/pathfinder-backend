package cogbog.dao;

import cogbog.model.Profile;

public interface ProfileDao {
    public int createProfile(Profile profile) throws Exception;

    public Profile findProfile(int profileId);

    public Profile updateProfile(int profileId, Profile profile) throws Exception;

    void deleteProfile(int id) throws Exception;
}
