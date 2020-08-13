package uk.co.idv.app.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.co.idv.context.adapter.json.error.ApiError;
import uk.co.idv.context.adapter.json.error.handler.ErrorHandler;
import uk.co.idv.context.adapter.json.error.internalserver.InternalServerError;

import java.util.Optional;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class ApplicationErrorHandler {

    private final ErrorHandler errorHandler;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleException(Throwable throwable) {
        Optional<ApiError> error = errorHandler.apply(throwable);
        return toResponseEntity(error.orElse(toInternalServerError(throwable)));
    }

    private static ApiError toInternalServerError(Throwable throwable) {
        return new InternalServerError(throwable.getMessage());
    }

    private static ResponseEntity<ApiError> toResponseEntity(ApiError error) {
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

}
