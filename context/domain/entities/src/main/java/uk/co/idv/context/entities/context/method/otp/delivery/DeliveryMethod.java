package uk.co.idv.context.entities.context.method.otp.delivery;

import lombok.Builder;
import lombok.Data;
import uk.co.idv.context.entities.context.method.otp.delivery.eligibility.AsyncFutureSimSwapEligibility;
import uk.co.idv.method.entities.eligibility.AsyncEligibility;
import uk.co.idv.method.entities.eligibility.Eligibility;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
@Builder
public class DeliveryMethod {

    private final UUID id;
    private final String type;
    private final String value;
    private final Instant lastUpdated;
    private final Eligibility eligibility;

    public Optional<CompletableFuture<Eligibility>> getAsyncSimSwapEligibilityFuture() {
        return getAsyncSimSwapEligibility().map(AsyncFutureSimSwapEligibility::getFuture);
    }

    public boolean isEligible() {
        return eligibility.isEligible();
    }

    public Optional<Instant> getLastUpdated() {
        return Optional.ofNullable(lastUpdated);
    }

    public boolean isEligibilityComplete() {
        if (eligibility instanceof AsyncEligibility) {
            AsyncEligibility asyncEligibility = (AsyncEligibility) eligibility;
            return asyncEligibility.isComplete();
        }
        return true;
    }

    private Optional<AsyncFutureSimSwapEligibility> getAsyncSimSwapEligibility() {
        if (eligibility instanceof AsyncFutureSimSwapEligibility) {
            return Optional.of((AsyncFutureSimSwapEligibility) eligibility);
        }
        return Optional.empty();
    }

}
