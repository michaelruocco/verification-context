package uk.co.idv.context.entities.context.method.otp;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.context.eligibility.Eligibility;
import uk.co.idv.context.entities.context.method.otp.delivery.DeliveryMethod;
import uk.co.idv.context.entities.context.method.otp.delivery.DeliveryMethodMother;
import uk.co.idv.context.entities.context.method.otp.delivery.DeliveryMethods;
import uk.co.idv.context.entities.context.method.otp.delivery.DeliveryMethodsMother;
import uk.co.idv.context.entities.policy.method.otp.OtpConfig;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OtpTest {

    @Test
    void shouldReturnName() {
        String name = "my-name";

        Otp otp = Otp.builder()
                .name(name)
                .build();

        assertThat(otp.getName()).isEqualTo(name);
    }

    @Test
    void shouldReturnDeliveryMethods() {
        DeliveryMethods deliveryMethods = givenDeliveryMethods();

        Otp otp = Otp.builder()
                .deliveryMethods(deliveryMethods)
                .build();

        assertThat(otp.getDeliveryMethods()).isEqualTo(deliveryMethods);
    }

    @Test
    void shouldReturnEligibilityFromDeliveryMethods() {
        Eligibility eligibility = mock(Eligibility.class);
        DeliveryMethods deliveryMethods = givenDeliveryMethodsWithEligibility(eligibility);

        Otp otp = Otp.builder()
                .deliveryMethods(deliveryMethods)
                .build();

        assertThat(otp.getEligibility()).isEqualTo(eligibility);
    }

    @Test
    void shouldNotBeComplete() {
        Otp otp = Otp.builder()
                .build();

        assertThat(otp.isComplete()).isFalse();
    }

    @Test
    void shouldNotBeSuccessful() {
        Otp otp = Otp.builder()
                .build();

        assertThat(otp.isSuccessful()).isFalse();
    }

    @Test
    void shouldReturnMethodConfig() {
        OtpConfig otpConfig = mock(OtpConfig.class);

        Otp otp = Otp.builder()
                .otpConfig(otpConfig)
                .build();

        assertThat(otp.getConfig()).isEqualTo(otpConfig);
    }

    @Test
    void shouldReplaceDeliveryMethods() {
        DeliveryMethods deliveryMethods = givenDeliveryMethods();
        DeliveryMethods newValues = givenDeliveryMethods();
        DeliveryMethods replaced = givenDeliveryMethods();
        given(deliveryMethods.replace(newValues)).willReturn(replaced);
        Otp otp = OtpMother.withDeliveryMethods(deliveryMethods);

        Otp updated = otp.replaceDeliveryMethods(newValues);

        assertThat(updated).usingRecursiveComparison()
                .ignoringFields("deliveryMethods")
                .isEqualTo(otp);
        assertThat(updated.getDeliveryMethods()).isEqualTo(replaced);
    }

    @Test
    void shouldFindDeliveryMethodFromDeliveryMethods() {
        DeliveryMethod expectedMethod = DeliveryMethodMother.build();
        Otp otp = OtpMother.withDeliveryMethods(DeliveryMethodsMother.with(expectedMethod));

        Optional<DeliveryMethod> found = otp.findDeliveryMethod(expectedMethod.getId());

        assertThat(found).contains(expectedMethod);
    }

    private DeliveryMethods givenDeliveryMethodsWithEligibility(Eligibility eligibility) {
        DeliveryMethods deliveryMethods = givenDeliveryMethods();
        given(deliveryMethods.getEligibility()).willReturn(eligibility);
        return deliveryMethods;
    }

    private DeliveryMethods givenDeliveryMethods() {
        return mock(DeliveryMethods.class);
    }

}
