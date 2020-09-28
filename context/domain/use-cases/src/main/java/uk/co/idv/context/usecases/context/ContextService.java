package uk.co.idv.context.usecases.context;

import lombok.Builder;
import uk.co.idv.context.entities.context.Context;
import uk.co.idv.context.entities.context.create.DefaultCreateContextRequest;
import uk.co.idv.context.entities.context.sequence.Sequences;
import uk.co.idv.context.usecases.context.expiry.ExpiryCalculator;
import uk.co.idv.context.entities.context.sequence.SequencesRequest;
import uk.co.idv.context.usecases.context.sequence.SequencesBuilder;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Builder
public class ContextService {

    @Builder.Default
    private final ExpiryCalculator expiryCalculator = new ExpiryCalculator();

    private final CreateContextRequestConverter requestConverter;
    private final Clock clock;
    private final SequencesBuilder sequencesBuilder;
    private final ContextRepository repository;

    public Context create(DefaultCreateContextRequest request) {
        Instant created = clock.instant();
        SequencesRequest sequencesRequest = requestConverter.toSequencesRequest(request);
        Sequences sequences = sequencesBuilder.build(sequencesRequest);
        Context context = Context.builder()
                .id(sequencesRequest.getContextId())
                .created(created)
                .expiry(expiryCalculator.calculate(created, sequences))
                .request(request)
                .sequences(sequences)
                .build();
        repository.save(context);
        return context;
    }

    public Context find(UUID id) {
        return repository.load(id).orElseThrow(() -> new ContextNotFoundException(id));
    }

}
