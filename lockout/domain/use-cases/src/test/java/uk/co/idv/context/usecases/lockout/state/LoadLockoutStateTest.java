package uk.co.idv.context.usecases.lockout.state;

import org.junit.jupiter.api.Test;
import uk.co.idv.identity.entities.alias.IdvId;
import uk.co.idv.context.entities.lockout.LockoutRequest;
import uk.co.idv.context.entities.lockout.LockoutRequestMother;
import uk.co.idv.context.entities.lockout.attempt.Attempts;
import uk.co.idv.context.entities.lockout.policy.LockoutPolicy;
import uk.co.idv.context.entities.lockout.policy.LockoutState;
import uk.co.idv.context.usecases.lockout.attempt.LoadAttempts;
import uk.co.idv.context.usecases.lockout.policy.LockoutPolicyService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LoadLockoutStateTest {

    private final LockoutPolicyService policyService = mock(LockoutPolicyService.class);
    private final LoadAttempts loadAttempts = mock(LoadAttempts.class);

    private final LoadLockoutState loadState = LoadLockoutState.builder()
            .policyService(policyService)
            .loadAttempts(loadAttempts)
            .build();

    @Test
    void shouldLoadLockoutPolicyAndAttemptsToCalculateLockoutState() {
        LockoutRequest request = LockoutRequestMother.build();
        LockoutPolicy policy = givenPolicyLoadedForRequest(request);
        Attempts attempts = givenAttemptsLoadedForIdvId(request.getIdvId());
        LockoutState expectedState = mock(LockoutState.class);
        given(policy.calculateState(request, attempts)).willReturn(expectedState);

        LockoutState state = loadState.load(request);

        assertThat(state).isEqualTo(expectedState);
    }

    private LockoutPolicy givenPolicyLoadedForRequest(LockoutRequest request) {
        LockoutPolicy policy = mock(LockoutPolicy.class);
        given(policyService.loadHighestPriority(request)).willReturn(policy);
        return policy;
    }

    private Attempts givenAttemptsLoadedForIdvId(IdvId idvId) {
        Attempts attempts = mock(Attempts.class);
        given(loadAttempts.load(idvId)).willReturn(attempts);
        return attempts;
    }


}
