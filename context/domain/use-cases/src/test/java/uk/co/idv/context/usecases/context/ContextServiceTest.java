package uk.co.idv.context.usecases.context;

import org.junit.jupiter.api.Test;
import uk.co.idv.common.usecases.id.IdGenerator;
import uk.co.idv.context.entities.context.Context;
import uk.co.idv.context.entities.context.create.DefaultCreateContextRequest;
import uk.co.idv.context.entities.context.create.DefaultCreateContextRequestMother;
import uk.co.idv.context.entities.context.sequence.Sequences;
import uk.co.idv.context.usecases.context.expiry.ExpiryCalculator;
import uk.co.idv.context.usecases.context.sequence.SequencesBuilder;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ContextServiceTest {

    private static final Instant NOW = Instant.parse("2020-09-25T07:14:01.050Z");

    private final IdGenerator idGenerator = mock(IdGenerator.class);
    private final Clock clock = Clock.fixed(NOW, ZoneId.systemDefault());
    private final SequencesBuilder sequencesBuilder = mock(SequencesBuilder.class);
    private final ExpiryCalculator expiryCalculator = mock(ExpiryCalculator.class);
    private final ContextRepository repository = mock(ContextRepository.class);

    private final ContextService service = ContextService.builder()
            .idGenerator(idGenerator)
            .clock(clock)
            .sequencesBuilder(sequencesBuilder)
            .expiryCalculator(expiryCalculator)
            .repository(repository)
            .build();

    @Test
    void shouldPopulateIdOnContext() {
        UUID expected = UUID.randomUUID();
        given(idGenerator.generate()).willReturn(expected);
        DefaultCreateContextRequest request = DefaultCreateContextRequestMother.build();

        Context context = service.create(request);

        assertThat(context.getId()).isEqualTo(expected);
    }

    @Test
    void shouldPopulateCreatedOnContext() {
        DefaultCreateContextRequest request = DefaultCreateContextRequestMother.build();

        Context context = service.create(request);

        assertThat(context.getCreated()).isEqualTo(NOW);
    }

    @Test
    void shouldPopulateRequestOnContext() {
        DefaultCreateContextRequest request = DefaultCreateContextRequestMother.build();

        Context context = service.create(request);

        assertThat(context.getRequest()).isEqualTo(request);
    }

    @Test
    void shouldPopulateSequencesOnContext() {
        DefaultCreateContextRequest request = DefaultCreateContextRequestMother.build();
        Sequences sequences = givenSequencesBuiltFromRequest(request);

        Context context = service.create(request);

        assertThat(context.getSequences()).isEqualTo(sequences);
    }

    @Test
    void shouldPopulateExpiryOnContext() {
        DefaultCreateContextRequest request = DefaultCreateContextRequestMother.build();
        Sequences sequences = givenSequencesBuiltFromRequest(request);
        Instant expectedExpiry = givenExpiryCalculatedFor(sequences);

        Context context = service.create(request);

        assertThat(context.getExpiry()).isEqualTo(expectedExpiry);
    }

    @Test
    void shouldFindContextIfExists() {
        UUID id = UUID.randomUUID();
        Context expectedContext = givenContextFound(id);

        Context context = service.find(id);

        assertThat(context).isEqualTo(expectedContext);
    }

    @Test
    void shouldThrowExceptionIfContextDoesNotExist() {
        UUID id = UUID.randomUUID();
        givenContextNotFound(id);

        Throwable error = catchThrowable(() -> service.find(id));

        assertThat(error)
                .isInstanceOf(ContextNotFoundException.class)
                .hasMessage(id.toString());
    }

    private Sequences givenSequencesBuiltFromRequest(DefaultCreateContextRequest request) {
        Sequences sequences = mock(Sequences.class);
        given(sequencesBuilder.build(request.getIdentity(), request.getSequencePolicies())).willReturn(sequences);
        return sequences;
    }

    private Context givenContextFound(UUID id) {
        Context context = mock(Context.class);
        given(repository.load(id)).willReturn(Optional.of(context));
        return context;
    }

    private void givenContextNotFound(UUID id) {
        given(repository.load(id)).willReturn(Optional.empty());
    }

    private Instant givenExpiryCalculatedFor(Sequences sequences) {
        Instant expiry = Instant.parse("2020-09-25T07:20:01.050Z");
        given(expiryCalculator.calculate(NOW, sequences)).willReturn(expiry);
        return expiry;
    }

}
