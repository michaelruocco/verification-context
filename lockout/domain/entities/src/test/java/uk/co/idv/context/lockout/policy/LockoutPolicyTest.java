package uk.co.idv.context.lockout.policy;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.policy.PolicyKey;
import uk.co.idv.context.entities.policy.PolicyRequest;
import uk.co.idv.context.lockout.attempt.VerificationAttempts;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LockoutPolicyTest {

    private final PolicyKey key = mock(PolicyKey.class);
    private final AttemptsFilter attemptsFilter = mock(AttemptsFilter.class);
    private final LockoutStateCalculator stateCalculator = mock(LockoutStateCalculator.class);
    private final RecordAttemptPolicy recordAttemptPolicy = mock(RecordAttemptPolicy.class);

    private final LockoutPolicy policy = new LockoutPolicy(key, attemptsFilter, stateCalculator, recordAttemptPolicy);

    @Test
    void shouldReturnKey() {
        assertThat(policy.getKey()).isEqualTo(key);
    }

    @Test
    void shouldReturnAttemptFilter() {
        assertThat(policy.getAttemptsFilter()).isEqualTo(attemptsFilter);
    }

    @Test
    void shouldReturnStateCalculator() {
        assertThat(policy.getStateCalculator()).isEqualTo(stateCalculator);
    }

    @Test
    void shouldReturnRecordAttemptPolicy() {
        assertThat(policy.getRecordAttemptPolicy()).isEqualTo(recordAttemptPolicy);
    }

    @Test
    void shouldReturnIdFromKey() {
        UUID expectedId = UUID.randomUUID();
        given(key.getId()).willReturn(expectedId);

        UUID id = policy.getId();

        assertThat(id).isEqualTo(expectedId);
    }

    @Test
    void shouldReturnPriorityFromKey() {
        int expectedPriority = 99;
        given(key.getPriority()).willReturn(expectedPriority);

        int priority = policy.getPriority();

        assertThat(priority).isEqualTo(expectedPriority);
    }

    @Test
    void shouldReturnAppliesToFromKey() {
        PolicyRequest request = mock(PolicyRequest.class);
        given(key.appliesTo(request)).willReturn(true);

        boolean applies = policy.appliesTo(request);

        assertThat(applies).isTrue();
    }

    @Test
    void shouldReturnRecordAttemptFromRecordAttemptPolicy() {
        RecordAttemptRequest request = mock(RecordAttemptRequest.class);
        given(recordAttemptPolicy.shouldRecordAttempt(request)).willReturn(true);

        boolean shouldRecord = policy.shouldRecordAttempt(request);

        assertThat(shouldRecord).isTrue();
    }

    @Test
    void shouldReturnLockoutStateFromStateCalculator() {
        LockoutStateRequest request = mock(LockoutStateRequest.class);
        LockoutState expectedState = mock(LockoutState.class);
        given(stateCalculator.calculate(request)).willReturn(expectedState);

        LockoutState state = policy.calculateState(request);

        assertThat(state).isEqualTo(expectedState);
    }

    @Test
    void shouldReturnAttemptsFromResetFilter() {
        LockoutStateRequest request = mock(LockoutStateRequest.class);
        VerificationAttempts expectedAttempts = mock(VerificationAttempts.class);
        given(attemptsFilter.filter(request)).willReturn(expectedAttempts);

        VerificationAttempts attempts = policy.reset(request);

        assertThat(attempts).isEqualTo(expectedAttempts);
    }

}
