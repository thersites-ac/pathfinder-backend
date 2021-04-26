package cogbog.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RestService {

    public String create(String entity) throws Exception;
    public String find(String key);
    public String update(String key, String updates) throws Exception;
    public void delete(String key) throws Exception;

}
