package cogbog.service;

import cogbog.exception.BadHttpMethodException;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public interface ProfileService {

    public default void doGet(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        throw new BadHttpMethodException(request.getHttpMethod());
    }

    public default void doPut(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        throw new BadHttpMethodException(request.getHttpMethod());
    };

    public default void doPost(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        throw new BadHttpMethodException(request.getHttpMethod());
    }

    public default void doDelete(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) throws Exception {
        throw new BadHttpMethodException(request.getHttpMethod());
    }

    public default void healthcheck(APIGatewayProxyRequestEvent request, APIGatewayProxyResponseEvent response) {
        response.setStatusCode(200);
    }
}
