package my_group.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public abstract class ResponseEntityUtil {
    public static final ResponseEntity<Void> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    public static <T> ResponseEntity<ResponseData<T>> okResponse(T data) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ResponseData<>(data));
    }

    public static ResponseEntity<ResponseData<Void>> errorResponse(String errorCode) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new ResponseData<>(new ErrorData(errorCode)));
    }

    public static ResponseEntity<ResponseData<Void>> errorResponse(String errorCode, String message) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new ResponseData<>(new ErrorData(errorCode, message)));
    }

    public static ResponseEntity<ResponseData<Void>> serverErrorResponse(String errorCode) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData<>(new ErrorData(errorCode)));
    }

    public static ResponseEntity<ResponseData<Void>> serverErrorResponse(String errorCode, String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData<>(new ErrorData(errorCode, message)));
    }
}
