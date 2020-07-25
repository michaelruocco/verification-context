package uk.co.idv.context.lockout.policy.recordattempt;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.lockout.policy.RecordAttemptPolicy;
import uk.co.idv.context.lockout.policy.RecordAttemptRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class NeverRecordAttemptsTest {

    private final RecordAttemptPolicy policy = new NeverRecordAttempts();

    @Test
    void shouldReturnType() {
        assertThat(policy.getType()).isEqualTo("never-record");
    }

    @Test
    void shouldNeverRecordAttempt() {
        RecordAttemptRequest anyRequest = mock(RecordAttemptRequest.class);

        boolean shouldRecord = policy.shouldRecordAttempt(anyRequest);

        assertThat(shouldRecord).isFalse();
    }

}
