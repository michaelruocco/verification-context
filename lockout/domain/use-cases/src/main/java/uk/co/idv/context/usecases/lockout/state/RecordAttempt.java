package uk.co.idv.context.usecases.lockout.state;

import lombok.Builder;
import uk.co.idv.context.entities.lockout.attempt.Attempt;
import uk.co.idv.context.entities.lockout.attempt.Attempts;
import uk.co.idv.context.entities.lockout.policy.LockoutPolicy;
import uk.co.idv.context.entities.lockout.policy.LockoutState;
import uk.co.idv.context.entities.lockout.policy.RecordAttemptRequest;
import uk.co.idv.context.usecases.lockout.attempt.SaveAttempt;
import uk.co.idv.context.usecases.lockout.policy.LockoutPolicyService;

@Builder
public class RecordAttempt {

    private final LockoutPolicyService policyService;
    private final ResetLockoutState reset;
    private final SaveAttempt save;
    private final LoadLockoutState load;

    public LockoutState record(RecordAttemptRequest request) {
        LockoutPolicy policy = policyService.loadHighestPriority(request);
        if (policy.shouldRecordAttempt(request)) {
            return record(request, policy);
        }
        return load.load(request, policy);
    }

    private LockoutState record(RecordAttemptRequest request, LockoutPolicy policy) {
        Attempt attempt = request.getAttempt();
        if (attempt.isSuccessful()) {
            return reset.reset(request, policy);
        }
        return recordFailed(request, policy);
    }

    private LockoutState recordFailed(RecordAttemptRequest request, LockoutPolicy policy) {
        Attempts attempts = save.save(request.getAttempt());
        return policy.calculateState(request, attempts);
    }

}
