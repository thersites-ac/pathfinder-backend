package cogbog.service.impl;

import cogbog.dao.DaoData;
import cogbog.dao.GenericDao;
import cogbog.dao.impl.GenericDaoImpl;
import cogbog.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class GenericRestService<T extends Serializable & DaoData<Integer, T>> implements RestService {

    private static final Logger logger = LoggerFactory.getLogger(GenericRestService.class);
    private final Class<T> type;
    private final GenericDao<Integer, T> dao;

    public GenericRestService(Class<T> type) {
        this.type = type;
        dao = new GenericDaoImpl<>(type);
    }

    @Override
    public String create(String entity) throws Exception {
        logger.debug("Creating " + type.getSimpleName() + " {}", entity);
        ObjectMapper objectMapper = new ObjectMapper();
        return "" + dao.create(objectMapper.readValue(entity, type));
    }

    @Override
    public String find(String key) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(dao.find(Integer.parseInt(key)));
    }

    @Override
    public String update(String key, String updates) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        T update = objectMapper.readValue(updates, type);
        return objectMapper.writeValueAsString(dao.update(Integer.parseInt(key), update));
    }

    @Override
    public void delete(String key) throws Exception {
        dao.delete(Integer.parseInt(key));
    }
}
