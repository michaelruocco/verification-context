package uk.co.idv.context.entities.lockout.policy;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.alias.Alias;
import uk.co.idv.context.entities.alias.DefaultAliasMother;
import uk.co.idv.context.entities.lockout.attempt.AttemptMother;
import uk.co.idv.context.entities.lockout.attempt.Attempts;
import uk.co.idv.context.entities.lockout.attempt.AttemptsMother;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LockoutStateRequestTest {

    @Test
    void shouldReturnAlias() {
        Alias alias = DefaultAliasMother.build();

        LockoutStateRequest request = LockoutStateRequest.builder()
                .alias(alias)
                .build();

        assertThat(request.getAlias()).isEqualTo(alias);
    }

    @Test
    void shouldReturnTimestamp() {
        Instant timestamp = Instant.now();

        LockoutStateRequest request = LockoutStateRequest.builder()
                .timestamp(timestamp)
                .build();

        assertThat(request.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldReturnAttempts() {
        Attempts attempts = AttemptsMother.build();

        LockoutStateRequest request = LockoutStateRequest.builder()
                .attempts(attempts)
                .build();

        assertThat(request.getAttempts()).isEqualTo(attempts);
    }

    @Test
    void shouldReturnNumberOfAttempts() {
        Attempts attempts = AttemptsMother.build();

        LockoutStateRequest request = LockoutStateRequest.builder()
                .attempts(attempts)
                .build();

        assertThat(request.getNumberOfAttempts()).isEqualTo(attempts.size());
    }

    @Test
    void shouldReturnRequestWithUpdatedAttemptsAndAllOtherValuesUnchanged() {
        Attempts attempts = AttemptsMother.withAttempts(AttemptMother.build());
        LockoutStateRequest request = LockoutStateRequest.builder()
                .attempts(attempts)
                .build();
        Attempts empty = AttemptsMother.empty();

        LockoutStateRequest updated = request.withAttempts(empty);

        assertThat(updated)
                .isEqualToIgnoringGivenFields(request, "attempts")
                .hasFieldOrPropertyWithValue("attempts", empty);
    }

    @Test
    void shouldReturnTrueIfTimestampIsBeforeNow() {
        Instant timestamp = Instant.now();
        LockoutStateRequest request = LockoutStateRequest.builder()
                .timestamp(timestamp)
                .build();
        Instant now = timestamp.plusMillis(1);

        boolean isBefore = request.isBefore(now);

        assertThat(isBefore).isTrue();
    }

    @Test
    void shouldReturnFalseIfTimestampIsAfterNow() {
        Instant timestamp = Instant.now();
        LockoutStateRequest request = LockoutStateRequest.builder()
                .timestamp(timestamp)
                .build();
        Instant now = timestamp.minusMillis(1);

        boolean isBefore = request.isBefore(now);

        assertThat(isBefore).isFalse();
    }

    @Test
    void shouldAddDurationToMostRecentTimestampFromAttempts() {
        Instant mostRecentTimestamp = Instant.now();
        LockoutStateRequest request = LockoutStateRequest.builder()
                .attempts(verificationAttemptsWithMostRecentTimestamp(mostRecentTimestamp))
                .build();
        Duration duration = Duration.ofMinutes(5);

        Instant result = request.addToMostRecentAttemptTimestamp(duration);

        assertThat(result).isEqualTo(mostRecentTimestamp.plus(duration));
    }

    private Attempts verificationAttemptsWithMostRecentTimestamp(Instant timestamp) {
        Attempts attempts = mock(Attempts.class);
        given(attempts.getMostRecentTimestamp()).willReturn(timestamp);
        return attempts;
    }

}
