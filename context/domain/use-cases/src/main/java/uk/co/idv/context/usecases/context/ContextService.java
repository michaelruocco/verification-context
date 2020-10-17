package uk.co.idv.context.usecases.context;

import lombok.Builder;
import uk.co.idv.context.entities.context.Context;
import uk.co.idv.context.entities.context.create.ServiceCreateContextRequest;
import uk.co.idv.context.entities.context.sequence.Sequences;
import uk.co.idv.context.usecases.context.event.create.ContextCreatedHandler;
import uk.co.idv.context.usecases.context.expiry.ExpiryCalculator;
import uk.co.idv.context.entities.context.sequence.SequencesRequest;
import uk.co.idv.context.usecases.context.lockout.ContextLockoutService;
import uk.co.idv.context.usecases.context.sequence.SequencesBuilder;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Builder
public class ContextService {

    @Builder.Default
    private final ExpiryCalculator expiryCalculator = new ExpiryCalculator();

    private final ContextLockoutService lockoutService;
    private final CreateContextRequestConverter requestConverter;
    private final Clock clock;
    private final SequencesBuilder sequencesBuilder;
    private final ContextCreatedHandler createdHandler;
    private final ContextRepository repository;

    public Context create(ServiceCreateContextRequest request) {
        lockoutService.validateLockoutState(request);
        Context context = buildContext(request);
        repository.save(context);
        createdHandler.created(context);
        return context;
    }

    public Context find(UUID id) {
        Context context = repository.load(id).orElseThrow(() -> new ContextNotFoundException(id));
        if (context.hasExpired(clock.instant())) {
            throw new ContextExpiredException(id, context.getExpiry());
        }
        lockoutService.validateLockoutState(context);
        return context;
    }

    private Instant calculateExpiry(Instant created, Sequences sequences) {
        return expiryCalculator.calculate(created, sequences.getDuration());
    }

    private Context buildContext(ServiceCreateContextRequest request) {
        Instant created = clock.instant();
        SequencesRequest sequencesRequest = requestConverter.toSequencesRequest(request);
        Sequences sequences = sequencesBuilder.build(sequencesRequest);
        return Context.builder()
                .id(sequencesRequest.getContextId())
                .created(created)
                .expiry(calculateExpiry(created, sequences))
                .request(request)
                .sequences(sequences)
                .build();
    }

}
