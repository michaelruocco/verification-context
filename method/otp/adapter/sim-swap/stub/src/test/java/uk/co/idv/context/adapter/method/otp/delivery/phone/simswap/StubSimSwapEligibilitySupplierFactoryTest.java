package uk.co.idv.context.adapter.method.otp.delivery.phone.simswap;

import org.junit.jupiter.api.Test;
import uk.co.idv.common.entities.async.Delay;
import uk.co.idv.method.entities.otp.delivery.phone.OtpPhoneNumber;
import uk.co.idv.method.entities.otp.policy.delivery.phone.SimSwapConfig;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class StubSimSwapEligibilitySupplierFactoryTest {

    private final Clock clock = mock(Clock.class);
    private final Delay delay = mock(Delay.class);
    private final StubSimSwapResultFactory resultFactory = mock(StubSimSwapResultFactory.class);

    private final OtpPhoneNumber number = mock(OtpPhoneNumber.class);
    private final SimSwapConfig config = mock(SimSwapConfig.class);

    private final StubSimSwapEligibilitySupplierFactory supplierFactory = StubSimSwapEligibilitySupplierFactory.builder()
            .clock(clock)
            .delaySupplier(() -> delay)
            .resultFactory(resultFactory)
            .build();

    @Test
    void shouldPopulateClockOnSupplier() {
        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number, config);

        assertThat(supplier.getClock()).isEqualTo(clock);
    }

    @Test
    void shouldPopulateDelayOnSupplier() {
        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number, config);

        assertThat(supplier.getDelay()).isEqualTo(delay);
    }

    @Test
    void shouldPopulateConfigOnSupplier() {
        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number, config);

        assertThat(supplier.getConfig()).isEqualTo(config);
    }

    @Test
    void shouldPopulateResultFactoryOnSupplier() {
        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number, config);

        assertThat(supplier.getResultFactory()).isEqualTo(resultFactory);
    }

    @Test
    void shouldPopulateOtpPhoneNumberOnSupplier() {
        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number, config);

        assertThat(supplier.getNumber()).isEqualTo(number);
    }

}
