package uk.co.idv.context.adapter.json.error.internalserver;

import lombok.extern.slf4j.Slf4j;
import uk.co.idv.context.adapter.json.error.ApiError;
import uk.co.idv.context.adapter.json.error.handler.ErrorHandler;

import java.util.Optional;

@Slf4j
public class InternalServerHandler implements ErrorHandler {

    @Override
    public Optional<ApiError> apply(Throwable cause) {
        return Optional.of(toError(cause));
    }

    private static ApiError toError(Throwable cause) {
        log.error(cause.getMessage(), cause);
        return new InternalServerError(cause.getMessage());
    }

}
