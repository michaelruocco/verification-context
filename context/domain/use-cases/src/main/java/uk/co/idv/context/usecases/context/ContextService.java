package uk.co.idv.context.usecases.context;

import lombok.Builder;
import uk.co.idv.context.entities.context.Context;
import uk.co.idv.context.entities.context.create.ServiceCreateContextRequest;

import java.util.UUID;

@Builder
public class ContextService implements ContextFinder {

    private final CreateContext createContext;
    private final FindContext findContext;

    public Context create(ServiceCreateContextRequest request) {
        return createContext.create(request);
    }

    @Override
    public Context find(UUID id) {
        return findContext.find(id);
    }

}
