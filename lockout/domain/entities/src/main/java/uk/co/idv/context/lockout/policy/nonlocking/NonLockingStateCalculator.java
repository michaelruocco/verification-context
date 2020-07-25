package uk.co.idv.context.lockout.policy.nonlocking;

import lombok.extern.slf4j.Slf4j;
import uk.co.idv.context.lockout.policy.LockoutState;
import uk.co.idv.context.lockout.policy.LockoutStateCalculator;
import uk.co.idv.context.lockout.policy.LockoutStateRequest;

@Slf4j
public class NonLockingStateCalculator implements LockoutStateCalculator {

    @Override
    public String getType() {
        return "non-locking";
    }

    @Override
    public LockoutState calculate(LockoutStateRequest request) {
        log.info("calculating non locking state from request {}", request);
        return new NonLockingState(request.getAttempts());
    }

}
