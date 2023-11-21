package my_group.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.ServerResponse;

public abstract class ServerResponseUtil {
    public static final ServerResponse UNAUTHORIZED = ServerResponse.status(HttpStatus.UNAUTHORIZED).build();

    public static <T> ServerResponse okResponse(T data) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(new ResponseData<>(data));
    }

    public static ServerResponse errorResponse(String errorCode) {
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(new ResponseData<>(new ErrorData(errorCode)));
    }

    public static ServerResponse errorResponse(String errorCode, String message) {
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(new ResponseData<>(new ErrorData(errorCode, message)));
    }

    public static ServerResponse serverErrorResponse(String errorCode) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData<>(new ErrorData(errorCode)));
    }

    public static ServerResponse serverErrorResponse(String errorCode, String message) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData<>(new ErrorData(errorCode, message)));
    }
}
