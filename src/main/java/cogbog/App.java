package cogbog;

import cogbog.dao.ProfileDao;
import cogbog.model.Profile;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private ProfileDao profileDao = new ProfileDao();

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        logger.debug("INIT");
        String method = requestEvent.getHttpMethod();
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        switch (method) {
            case "GET":
                doGet(requestEvent, responseEvent, context);
                break;
            case "POST":
                doPost(requestEvent, responseEvent, context);
                break;
            default:
                badMethod(requestEvent, responseEvent, context);
                break;
        }
        return responseEvent;
    }

    private void doGet(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent, Context context) {
        logger.info("GET");
        int profileId = Integer.parseInt(requestEvent.getPathParameters().get("id"));
        logger.info("Requested id: {}", profileId);
        try {
            Profile profile = profileDao.getProfile(profileId);
            ObjectMapper objectMapper = new ObjectMapper();
            requestEvent.setBody(objectMapper.writeValueAsString(profile));
            responseEvent.setStatusCode(200);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
            responseEvent.setStatusCode(500);
        } catch (NoResultException e) {
            logger.info(e.toString());
            responseEvent.setBody("Not found: " + profileId);
            responseEvent.setStatusCode(404);
        }
    }

    private void doPost(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent, Context context) {
        logger.info("POST");
        String body = requestEvent.getBody();
        logger.info("body: {}", body);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Profile profile = objectMapper.readValue(body, Profile.class);
            profileDao.createProfile(profile);
            responseEvent.setBody(objectMapper.writeValueAsString(profile));
            responseEvent.setStatusCode(201);
        } catch (UnrecognizedPropertyException e) {
            logger.info(e.toString());
            responseEvent.setStatusCode(400);
        } catch (JsonMappingException e) {
            logger.error(e.toString());
            responseEvent.setStatusCode(400);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
            responseEvent.setStatusCode(500);
        }
    }

        private void badMethod(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent, Context context) {
        logger.info("Unsupported method: {}", requestEvent.getHttpMethod());
        responseEvent.setStatusCode(405);
    }

}
