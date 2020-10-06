package uk.co.idv.method.usecases.otp.simswap.async;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import uk.co.idv.context.entities.context.Context;
import uk.co.idv.context.usecases.context.ContextRepository;
import uk.co.idv.method.entities.otp.simswap.SimSwapRequest;
import uk.co.idv.method.usecases.otp.delivery.OtpDeliveryMethodReplacer;
import uk.co.idv.method.usecases.otp.simswap.sync.SyncSimSwap;

import java.util.Optional;

@Builder
@Data
@Slf4j
public class AsyncSimSwapUpdateContextTask implements Runnable {

    private final ContextRepository repository;
    private final SimSwapRequest request;
    private final SyncSimSwap syncStrategy;

    @Override
    public void run() {
        syncStrategy.waitForSimSwapsToComplete(request);
        Optional<Context> context = repository.load(request.getContextId());
        if (context.isEmpty()) {
            throw new AsyncSimSwapContextNotFoundException(request.getContextId());
        }
        updateContextDeliveryMethods(context.get());
    }

    private void updateContextDeliveryMethods(Context context) {
        Context updated = context.apply(new OtpDeliveryMethodReplacer(request.getDeliveryMethods()));
        log.info("updating context {} with updated delivery methods with sim swap eligibility", updated.getId());
        repository.save(updated);
    }

}
