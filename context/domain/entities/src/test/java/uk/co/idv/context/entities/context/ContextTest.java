package uk.co.idv.context.entities.context;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.context.create.ServiceCreateContextRequest;
import uk.co.idv.context.entities.context.create.DefaultCreateContextRequestMother;
import uk.co.idv.context.entities.context.method.Method;
import uk.co.idv.context.entities.context.method.otp.delivery.DeliveryMethods;
import uk.co.idv.context.entities.context.method.query.MethodQuery;
import uk.co.idv.context.entities.context.sequence.Sequences;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ContextTest {

    @Test
    void shouldReturnId() {
        UUID id = UUID.randomUUID();

        Context context = Context.builder()
                .id(id)
                .build();

        assertThat(context.getId()).isEqualTo(id);
    }

    @Test
    void shouldReturnCreated() {
        Instant created = Instant.now();

        Context context = Context.builder()
                .created(created)
                .build();

        assertThat(context.getCreated()).isEqualTo(created);
    }

    @Test
    void shouldReturnExpiry() {
        Instant expiry = Instant.now();

        Context context = Context.builder()
                .expiry(expiry)
                .build();

        assertThat(context.getExpiry()).isEqualTo(expiry);
    }

    @Test
    void shouldReturnIdentityCreateContextRequest() {
        ServiceCreateContextRequest request = DefaultCreateContextRequestMother.build();

        Context context = Context.builder()
                .request(request)
                .build();

        assertThat(context.getRequest()).isEqualTo(request);
    }

    @Test
    void shouldReturnChannelFromCreateContextRequest() {
        ServiceCreateContextRequest request = DefaultCreateContextRequestMother.build();

        Context context = Context.builder()
                .request(request)
                .build();

        assertThat(context.getChannel()).isEqualTo(request.getChannel());
    }

    @Test
    void shouldReturnActivityFromCreateContextRequest() {
        ServiceCreateContextRequest request = DefaultCreateContextRequestMother.build();

        Context context = Context.builder()
                .request(request)
                .build();

        assertThat(context.getActivity()).isEqualTo(request.getActivity());
    }

    @Test
    void shouldReturnIdentityFromCreateContextRequest() {
        ServiceCreateContextRequest request = DefaultCreateContextRequestMother.build();

        Context context = Context.builder()
                .request(request)
                .build();

        assertThat(context.getIdentity()).isEqualTo(request.getIdentity());
    }

    @Test
    void shouldReturnSequences() {
        Sequences sequences = mock(Sequences.class);

        Context context = Context.builder()
                .sequences(sequences)
                .build();

        assertThat(context.getSequences()).isEqualTo(sequences);
    }

    @Test
    void shouldReturnResultOfQueryFromSequences() {
        Method expectedMethod = mock(Method.class);
        MethodQuery<Method> query = mock(MethodQuery.class);
        Sequences sequences = givenSequencesWillReturnMethodForQuery(query, expectedMethod);
        given(sequences.find(query)).willReturn(Optional.of(expectedMethod));
        Context context = Context.builder()
                .sequences(sequences)
                .build();

        Optional<Method> method = context.find(query);

        assertThat(method).contains(expectedMethod);
    }

    @Test
    void shouldReturnEligibleFromSequences() {
        Sequences sequences = mock(Sequences.class);
        given(sequences.isEligible()).willReturn(true);

        Context context = Context.builder()
                .sequences(sequences)
                .build();

        assertThat(context.isEligible()).isTrue();
    }

    @Test
    void shouldReturnCompleteFromSequences() {
        Sequences sequences = mock(Sequences.class);
        given(sequences.isComplete()).willReturn(true);

        Context context = Context.builder()
                .sequences(sequences)
                .build();

        assertThat(context.isComplete()).isTrue();
    }

    @Test
    void shouldReturnSuccessfulFromSequences() {
        Sequences sequences = mock(Sequences.class);
        given(sequences.isSuccessful()).willReturn(true);

        Context context = Context.builder()
                .sequences(sequences)
                .build();

        assertThat(context.isSuccessful()).isTrue();
    }

    @Test
    void shouldReturnDurationFromSequences() {
        Sequences sequences = mock(Sequences.class);
        Duration duration = Duration.ofMinutes(5);
        given(sequences.getDuration()).willReturn(duration);

        Context context = Context.builder()
                .sequences(sequences)
                .build();

        assertThat(context.getDuration()).isEqualTo(duration);
    }

    @Test
    void shouldReplaceDeliveryMethodsOnSequences() {
        Sequences sequences = mock(Sequences.class);
        DeliveryMethods deliveryMethods = mock(DeliveryMethods.class);
        Sequences expectedSequences = givenReplacedDeliveryMethodsSequences(sequences, deliveryMethods);
        Context context = Context.builder()
                .sequences(sequences)
                .build();

        Context updated = context.replaceDeliveryMethods(deliveryMethods);

        assertThat(updated).usingRecursiveComparison()
                .ignoringFields("sequences")
                .isEqualTo(context);
        assertThat(updated.getSequences()).isEqualTo(expectedSequences);
    }

    private Sequences givenReplacedDeliveryMethodsSequences(Sequences sequences, DeliveryMethods deliveryMethods) {
        Sequences replaced = mock(Sequences.class);
        given(sequences.replaceDeliveryMethods(deliveryMethods)).willReturn(replaced);
        return replaced;
    }

    private Sequences givenSequencesWillReturnMethodForQuery(MethodQuery<Method> query, Method method) {
        Sequences sequences = mock(Sequences.class);
        given(sequences.find(query)).willReturn(Optional.of(method));
        return sequences;
    }

}
