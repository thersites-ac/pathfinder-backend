package cogbog;

import cogbog.exception.BadHttpMethodException;
import cogbog.exception.BadPathParametersException;
import cogbog.exception.NotFoundException;
import cogbog.service.RestService;
import cogbog.service.impl.ProfileRestServiceImpl;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.Map;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static Map<String, String> defaultHeaders;
    private static ProfileRestServiceImpl profileService;
    static {
        defaultHeaders = new HashMap<>();
        defaultHeaders.put("Content-Type", "application/json");
        profileService = new ProfileRestServiceImpl();
    }

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        logger.info("Invoked with request {}", request.toString());
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setHeaders(defaultHeaders);
        try {
            branch(request, response);
        } catch (BadPathParametersException | JsonProcessingException ex) {
            logger.error(ex.toString());
            response.setStatusCode(400);
        } catch (NoResultException ex) {
            logger.error(ex.toString());
            response.setStatusCode(404);
        } catch (BadHttpMethodException ex) {
            response.setBody(ex.getMessage());
            response.setStatusCode(405);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response.setStatusCode(500);
        }
        return response;
    }

    private void branch(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        String[] route = request.getPath().split("/");
        // validate route here
        String resource = route[0];
        switch (resource) {
            case "profile":
                break;
            default:
                throw new NotFoundException(resource);
        }
    }

    private void restfulRouting(RestService service, String[] route, String httpMethod, String body, APIGatewayProxyResponseEvent response) throws Exception {
        switch (httpMethod) {
            case "GET":
                // validate route here
                response.setBody(service.find(route[1]));
                response.setStatusCode(200);
                break;
            case "PUT":
                String id = service.create(body);
                response.setBody("{\"id\": \"" + id + "\"}");
                response.setStatusCode(201);
            case "POST":
                // validate route
                service.update(route[1], body);
                response.setStatusCode(200);
            case "DELETE":
                // validate route
                service.delete(route[1]);
            default:
                throw new BadHttpMethodException(httpMethod);
        }
    }

}
