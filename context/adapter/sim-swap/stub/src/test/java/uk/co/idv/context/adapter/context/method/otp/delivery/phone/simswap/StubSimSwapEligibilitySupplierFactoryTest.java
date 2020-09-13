package uk.co.idv.context.adapter.context.method.otp.delivery.phone.simswap;

import org.junit.jupiter.api.Test;
import uk.co.idv.common.usecases.async.Delay;
import uk.co.idv.context.entities.policy.method.otp.delivery.phone.OtpPhoneNumber;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class StubSimSwapEligibilitySupplierFactoryTest {

    private final Clock clock = mock(Clock.class);
    private final Delay delay = mock(Delay.class);
    private final StubSimSwapResultFactory resultFactory = mock(StubSimSwapResultFactory.class);

    private final StubSimSwapEligibilitySupplierFactory supplierFactory = StubSimSwapEligibilitySupplierFactory.builder()
            .clock(clock)
            .delay(delay)
            .resultFactory(resultFactory)
            .build();

    @Test
    void shouldPopulateClockOnSupplier() {
        OtpPhoneNumber number = mock(OtpPhoneNumber.class);

        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number);

        assertThat(supplier.getClock()).isEqualTo(clock);
    }

    @Test
    void shouldPopulateDelayOnSupplier() {
        OtpPhoneNumber number = mock(OtpPhoneNumber.class);

        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number);

        assertThat(supplier.getDelay()).isEqualTo(delay);
    }

    @Test
    void shouldPopulateResultFactoryOnSupplier() {
        OtpPhoneNumber number = mock(OtpPhoneNumber.class);

        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number);

        assertThat(supplier.getResultFactory()).isEqualTo(resultFactory);
    }

    @Test
    void shouldPopulateOtpPhoneNumberOnSupplier() {
        OtpPhoneNumber number = mock(OtpPhoneNumber.class);

        StubSimSwapEligibilitySupplier supplier = supplierFactory.toSupplier(number);

        assertThat(supplier.getNumber()).isEqualTo(number);
    }

}