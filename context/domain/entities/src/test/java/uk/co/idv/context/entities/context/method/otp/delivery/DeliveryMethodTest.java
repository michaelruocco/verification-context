package uk.co.idv.context.entities.context.method.otp.delivery;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.context.eligibility.Eligibility;
import uk.co.idv.context.entities.context.method.otp.delivery.eligibility.AsyncSimSwapEligibility;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DeliveryMethodTest {

    @Test
    void shouldReturnId() {
        UUID id = UUID.randomUUID();

        DeliveryMethod method = DeliveryMethod.builder()
                .id(id)
                .build();

        assertThat(method.getId()).isEqualTo(id);
    }

    @Test
    void shouldReturnType() {
        String type = "my-type";

        DeliveryMethod method = DeliveryMethod.builder()
                .type(type)
                .build();

        assertThat(method.getType()).isEqualTo(type);
    }

    @Test
    void shouldReturnValue() {
        String value = "my-value";

        DeliveryMethod method = DeliveryMethod.builder()
                .value(value)
                .build();

        assertThat(method.getValue()).isEqualTo(value);
    }

    @Test
    void shouldReturnEligibility() {
        Eligibility eligibility = mock(Eligibility.class);

        DeliveryMethod method = DeliveryMethod.builder()
                .eligibility(eligibility)
                .build();

        assertThat(method.getEligibility()).isEqualTo(eligibility);
    }

    @Test
    void shouldReturnEmptyOptionalIfEligibilityIsNotAsyncSimSwapEligibility() {
        Eligibility eligibility = mock(Eligibility.class);

        DeliveryMethod method = DeliveryMethod.builder()
                .eligibility(eligibility)
                .build();

        assertThat(method.getAsyncSimSwapEligibilityFuture()).isEmpty();
    }

    @Test
    void shouldReturnFutureFromAsyncSimSwapEligibilityIfEligibilityIsAsyncSimSwapEligibility() {
        AsyncSimSwapEligibility eligibility = mock(AsyncSimSwapEligibility.class);
        CompletableFuture<Eligibility> expectedFuture = givenFutureReturnedFrom(eligibility);

        DeliveryMethod method = DeliveryMethod.builder()
                .eligibility(eligibility)
                .build();

        assertThat(method.getAsyncSimSwapEligibilityFuture()).contains(expectedFuture);
    }

    private CompletableFuture<Eligibility> givenFutureReturnedFrom(AsyncSimSwapEligibility eligibility) {
        CompletableFuture<Eligibility> future = mock(CompletableFuture.class);
        given(eligibility.getFuture()).willReturn(future);
        return future;
    }

}