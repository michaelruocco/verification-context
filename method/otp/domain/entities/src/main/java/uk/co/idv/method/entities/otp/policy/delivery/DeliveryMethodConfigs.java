package uk.co.idv.method.entities.otp.policy.delivery;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.co.idv.identity.entities.identity.RequestedData;
import uk.co.idv.method.entities.otp.policy.delivery.phone.PhoneDeliveryMethodConfig;
import uk.co.idv.method.entities.otp.policy.delivery.phone.SimSwapConfig;
import uk.co.idv.method.entities.policy.RequestedDataMerger;
import uk.co.idv.method.entities.policy.RequestedDataProvider;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Data
public class DeliveryMethodConfigs implements Iterable<DeliveryMethodConfig>, RequestedDataProvider {

    @Getter(AccessLevel.NONE)
    private final Collection<DeliveryMethodConfig> values;

    public DeliveryMethodConfigs(DeliveryMethodConfig... values) {
        this(Arrays.asList(values));
    }

    @Override
    public RequestedData getRequestedData() {
        return RequestedDataMerger.mergeRequestedData(values);
    }

    @Override
    public Iterator<DeliveryMethodConfig> iterator() {
        return values.iterator();
    }

    public Stream<DeliveryMethodConfig> stream() {
        return values.stream();
    }

    public Optional<Duration> getLongestSimSwapConfigTimeout() {
        return getPhoneConfigs()
                .map(PhoneDeliveryMethodConfig::getSimSwapTimeout)
                .flatMap(Optional::stream)
                .max(Comparator.comparingLong(Duration::toMillis));
    }

    public boolean hasAsyncSimSwap() {
        return getPhoneConfigs()
                .map(PhoneDeliveryMethodConfig::getSimSwapConfig)
                .flatMap(Optional::stream)
                .anyMatch(SimSwapConfig::isAsync);
    }

    private Stream<PhoneDeliveryMethodConfig> getPhoneConfigs() {
        return values.stream()
                .filter(PhoneDeliveryMethodConfig.class::isInstance)
                .map(PhoneDeliveryMethodConfig.class::cast);
    }

}
