package cogbog.service.impl;

import cogbog.dao.DaoData;
import cogbog.dao.GenericDao;
import cogbog.dao.impl.GenericDaoImpl;
import cogbog.service.RestService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Type;

public class GenericRestService<T extends Serializable & DaoData<Integer, T>> implements RestService {

    private static final Logger logger = LoggerFactory.getLogger(GenericRestService.class);
    private T witness;
    private GenericDao<Integer, T> dao;

    public GenericRestService(T witness) {
        this.witness = witness;
        dao = new GenericDaoImpl<>(witness);
    }

    @Override
    public String create(String entity) throws Exception {
        logger.debug("Creating " + witness.getClass().getSimpleName() + " {}", entity);
        Gson gson = new Gson();
//      return "" + dao.create(gson.fromJson(entity, (Class<T>) witness.getClass()));
        return "" + dao.create(gson.fromJson(entity, (Type) witness.getClass()));
    }

    @Override
    public String find(String key) {
        Gson gson = new Gson();
        // FIXME: 4/25/21 parseInt returns 0 if the string is bad
        return gson.toJson(dao.find(Integer.parseInt(key)));
    }

    @Override
    public String update(String key, String updates) throws Exception {
        Gson gson = new Gson();
//      T update = gson.fromJson(updates, (Class<T>) witness.getClass());
        T update = gson.fromJson(updates, (Type) witness.getClass());
        // FIXME: 4/25/21 parseInt returns 0 if the string is bad
        return gson.toJson(dao.update(Integer.parseInt(key), update));
    }

    @Override
    public void delete(String key) throws Exception {
        // FIXME: 4/25/21 parseInt returns 0 if the string is bad
        dao.delete(Integer.parseInt(key));
    }
}
