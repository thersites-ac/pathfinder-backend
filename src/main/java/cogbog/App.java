package cogbog;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

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
        String player = requestEvent.getPathParameters().get("player");
        responseEvent.setBody(player + " HAS BIG FLOPPY FEET");
        responseEvent.setStatusCode(200);
    }

    private void doPost(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent, Context context) {
        String body = requestEvent.getBody();
        responseEvent.setBody(body.toUpperCase());
        responseEvent.setStatusCode(200);
    }

    private void badMethod(APIGatewayProxyRequestEvent requestEvent, APIGatewayProxyResponseEvent responseEvent, Context context) {
        responseEvent.setStatusCode(405);
    }

}
