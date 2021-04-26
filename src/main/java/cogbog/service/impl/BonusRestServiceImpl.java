package cogbog.service.impl;

import cogbog.dao.GenericDao;
import cogbog.dao.impl.GenericDaoImpl;
import cogbog.exception.BadPathParametersException;
import cogbog.model.Bonus;
import cogbog.service.RestService;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BonusRestServiceImpl implements RestService<Bonus> {

    private static final Logger logger = LoggerFactory.getLogger(RestService.class);

    private GenericDao<Integer, Bonus> bonusDao = new GenericDaoImpl<>(new Bonus());

    @Override
    public void doGet(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        logger.debug("GET");
        int id = getParam(request, "id");
        logger.info("bonus id: {}", id);

        Bonus bonus = bonusDao.find(id);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setBody(objectMapper.writeValueAsString(bonus));
        response.setStatusCode(200);
    }

    @Override
    public void doPut(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        logger.debug("PUT");
        String body = request.getBody();
        logger.info("body: {}", body);
        ObjectMapper objectMapper = new ObjectMapper();
        Bonus bonus = objectMapper.readValue(body, Bonus.class);

        bonusDao.create(bonus);
        response.setBody(objectMapper.writeValueAsString(bonus));
        response.setStatusCode(201);
    }

    @Override
    public void doPost(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        logger.debug("POST");
        String body = request.getBody();
        logger.info("body: {}", body);
        int id = getParam(request, "id");
        logger.info("bonus id: {}", id);
        ObjectMapper objectMapper = new ObjectMapper();
        Bonus bonus = objectMapper.readValue(body, Bonus.class);

        bonus = bonusDao.update(id, bonus);
        response.setBody(objectMapper.writeValueAsString(bonus));
        response.setStatusCode(200);
    }

    @Override
    public void doDelete(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        logger.debug("DELETE");
        int id = getParam(request, "id");
        logger.info("bonus id: {}", id);

        bonusDao.delete(id);
        response.setStatusCode(200);
    }

    private int getParam(APIGatewayProxyRequestEvent request, String id) throws BadPathParametersException {
        Map<String, String> params = request.getPathParameters();
        if (params == null) {
            throw new BadPathParametersException(String.format("Expected parameter %s but found no path parameters", id));
        } else {
            String value = params.get(id);
            if (value == null) {
                throw new BadPathParametersException(String.format("Parameter %s missing", id));
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                throw new BadPathParametersException(ex);
            }
        }
    }

}
