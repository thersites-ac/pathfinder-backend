package cogbog.service;

import cogbog.model.Profile;
import cogbog.dao.ProfileDao;
import cogbog.dao.ProfileDaoImpl;
import cogbog.exception.BadPathParametersException;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    private ProfileDao profileDao = new ProfileDaoImpl();

    @Override
    public void doGet(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        logger.debug("GET");
        int profileId = getParam(request, "id");
        logger.info("profile id: {}", profileId);

        Profile profile = profileDao.findProfile(profileId);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setBody(objectMapper.writeValueAsString(profile));
        response.setStatusCode(200);
    }

    @Override
    public void doPut(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        logger.debug("PUT");
        String body = request.getBody();
        logger.info("body: {}", body);
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = objectMapper.readValue(body, Profile.class);

        profileDao.createProfile(profile);
        response.setBody(objectMapper.writeValueAsString(profile));
        response.setStatusCode(201);
    }

    @Override
    public void doPost(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        logger.debug("POST");
        String body = request.getBody();
        logger.info("body: {}", body);
        int profileId = getParam(request, "id");
        logger.info("profile id: {}", profileId);
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profile = objectMapper.readValue(body, Profile.class);

        profile = profileDao.updateProfile(profileId, profile);
        response.setBody(objectMapper.writeValueAsString(profile));
        response.setStatusCode(200);
    }

    @Override
    public void doDelete(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        logger.debug("DELETE");
        int id = getParam(request, "id");
        logger.info("profile id: {}", id);

        profileDao.deleteProfile(id);
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