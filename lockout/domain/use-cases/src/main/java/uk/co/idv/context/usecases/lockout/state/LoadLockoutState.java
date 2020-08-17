package uk.co.idv.context.usecases.lockout.state;

import lombok.Builder;
import uk.co.idv.context.entities.lockout.attempt.Attempts;
import uk.co.idv.context.entities.lockout.policy.LockoutPolicy;
import uk.co.idv.context.entities.lockout.policy.LockoutState;
import uk.co.idv.context.entities.lockout.LockoutRequest;
import uk.co.idv.context.usecases.lockout.attempt.LoadAttempts;
import uk.co.idv.context.usecases.lockout.policy.LockoutPolicyService;

@Builder
public class LoadLockoutState {

    private final LockoutPolicyService policyService;
    private final LoadAttempts loadAttempts;

    public LockoutState load(LockoutRequest request) {
        LockoutPolicy policy = policyService.loadHighestPriority(request);
        return load(request, policy);
    }

    public LockoutState load(LockoutRequest request, LockoutPolicy policy) {
        Attempts attempts = loadAttempts.load(request.getIdvId());
        return policy.calculateState(request, attempts);
    }

}
