package uk.co.idv.context.entities.context;

import java.util.UUID;

public class ContextNotFoundException extends RuntimeException {

    public ContextNotFoundException(UUID id) {
        super(id.toString());
    }

}
