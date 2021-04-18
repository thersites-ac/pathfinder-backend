package cogbog;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.Map;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static Map<String, String> defaultHeaders;
    static {
        defaultHeaders = new HashMap<>();
        defaultHeaders.put("Content-Type", "application/json");
    }
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private ProfileDao profileDao = new ProfileDao();

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        logger.debug("INIT");
        String method = requestEvent.getHttpMethod();
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setHeaders(defaultHeaders);
        switch (method) {
            case "GET":
                doGet(requestEvent, responseEvent, context);
                break;
            case "PUT":
                doPut(requestEvent, responseEvent);
                break;
            case "POST":
                doPost(requestEvent, responseEvent);
                break;
            default:
                badMethod(requestEvent, responseEvent);
                break;
        }
        return responseEvent;
    }

    private void doGet(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent, Context context) {
        logger.info("GET");
        // need to handle case where profileId isn't given on the path
        String profileIdParam = requestEvent.getPathParameters().get("id");
        int profileId = Integer.parseInt(profileIdParam);
        logger.info("profile id: {}", profileId);
        try {
            Profile profile = profileDao.findProfile(profileId);
            ObjectMapper objectMapper = new ObjectMapper();
            responseEvent.setBody(objectMapper.writeValueAsString(profile));
            responseEvent.setStatusCode(200);
        } catch (NoResultException e) {
            logger.info(e.toString());
            responseEvent.setBody("Not found: " + profileId);
            responseEvent.setStatusCode(404);
        } catch (Exception e) {
            logger.error(e.toString());
            responseEvent.setStatusCode(500);
        }
    }

    private void doPut(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent) {
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
        } catch (Exception e) {
            logger.error(e.toString());
            responseEvent.setStatusCode(500);
        }
    }

    private void doPost(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent) {
        logger.info("POST");
        String body = requestEvent.getBody();
        logger.info("body: {}", body);
        // need to handle various exceptiosn here
        String profileIdParam = requestEvent.getPathParameters().get("id");
        int profileId = Integer.parseInt(profileIdParam);
        logger.info("profile id: {}", profileId);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Profile profile = objectMapper.readValue(body, Profile.class);
            profile = profileDao.updateProfile(profileId, profile);
            responseEvent.setBody(objectMapper.writeValueAsString(profile));
            responseEvent.setStatusCode(200);
            // JSON-related boilerplate should be factored out
        } catch (NoResultException e) {
            logger.info(e.toString());
            responseEvent.setBody("Not found: " + profileId);
            responseEvent.setStatusCode(404);
        } catch (JsonMappingException e) {
            logger.error(e.toString());
            responseEvent.setStatusCode(400);
        } catch (Exception e) {
            logger.error(e.toString());
            responseEvent.setStatusCode(500);
        }

    }

    private void badMethod(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent) {
        logger.info("Unsupported method: {}", requestEvent.getHttpMethod());
        responseEvent.setStatusCode(405);
    }

}
