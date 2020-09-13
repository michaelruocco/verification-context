package uk.co.idv.context.entities.context.method.otp.delivery.eligibility;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.context.eligibility.Eligibility;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class EligibilityFuturesTest {

    @Test
    void shouldReturnTrueIfEmpty() {
        EligibilityFutures futures = new EligibilityFutures(Collections.emptyList());

        boolean empty = futures.isEmpty();

        assertThat(empty).isTrue();
    }

    @Test
    void shouldReturnFalseIfNotEmpty() {
        CompletableFuture<Eligibility> future = CompletableFuture.completedFuture(mock(Eligibility.class));
        EligibilityFutures futures = new EligibilityFutures(Collections.singleton(future));

        boolean empty = futures.isEmpty();

        assertThat(empty).isFalse();
    }

    @Test
    void shouldBeIterable() {
        CompletableFuture<Eligibility> future1 = CompletableFuture.completedFuture(mock(Eligibility.class));
        CompletableFuture<Eligibility> future2 = CompletableFuture.completedFuture(mock(Eligibility.class));

        EligibilityFutures futures = new EligibilityFutures(Arrays.asList(future1, future2));

        assertThat(futures).containsExactly(future1, future2);
    }

    @Test
    void shouldReturnAllFuturesCombined() {
        CompletableFuture<Eligibility> future1 = CompletableFuture.completedFuture(mock(Eligibility.class));
        CompletableFuture<Eligibility> future2 = CompletableFuture.failedFuture(new Exception("test-error"));
        EligibilityFutures futures = new EligibilityFutures(Arrays.asList(future1, future2));

        CompletableFuture<Void> all = futures.all();

        assertThat(all).isCompletedExceptionally();
    }

}