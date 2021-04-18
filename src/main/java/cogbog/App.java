package cogbog;

import cogbog.exception.BadHttpMethodException;
import cogbog.exception.BadPathParametersException;
import cogbog.service.ProfileService;
import cogbog.service.ProfileServiceImpl;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    private ProfileService profileService = new ProfileServiceImpl();

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        logger.debug("INIT");
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setHeaders(defaultHeaders);
        try {
            branchOnMethod(request, response);
        } catch (BadPathParametersException ex) {
            logger.error(ex.toString());
            response.setStatusCode(400);
        } catch (JsonMappingException ex) {
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

    private void branchOnMethod(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        String method = request.getHttpMethod();
        switch (method) {
            case "GET":
                profileService.doGet(request, response);
                break;
            case "PUT":
                profileService.doPut(request, response);
                break;
            case "POST":
                profileService.doPost(request, response);
                break;
            case "DELETE":
                profileService.doDelete(request, response);
            default:
                throw new BadHttpMethodException("Unsupported: " + request.getHttpMethod());
        }
    }

}
