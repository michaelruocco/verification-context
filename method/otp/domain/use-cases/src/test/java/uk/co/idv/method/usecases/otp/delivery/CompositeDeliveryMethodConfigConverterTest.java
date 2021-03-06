package uk.co.idv.method.usecases.otp.delivery;

import org.junit.jupiter.api.Test;
import uk.co.idv.identity.entities.identity.Identity;
import uk.co.idv.method.entities.otp.delivery.DeliveryMethods;
import uk.co.idv.method.entities.otp.policy.delivery.DeliveryMethodConfig;
import uk.co.idv.method.entities.otp.policy.delivery.phone.sms.SmsDeliveryMethodConfigMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CompositeDeliveryMethodConfigConverterTest {

    private final DeliveryMethodConfigConverter converter1 = mock(DeliveryMethodConfigConverter.class);
    private final DeliveryMethodConfigConverter converter2 = mock(DeliveryMethodConfigConverter.class);

    private final CompositeDeliveryMethodConfigConverter compositeConverter = new CompositeDeliveryMethodConfigConverter(
            converter1,
            converter2
    );

    @Test
    void shouldThrowExceptionIfConvertersSupportingConfig() {
        DeliveryMethodConfig config = SmsDeliveryMethodConfigMother.sms();
        Identity identity = mock(Identity.class);

        Throwable error = catchThrowable(() -> compositeConverter.toDeliveryMethods(identity, config));

        assertThat(error)
                .isInstanceOf(DeliveryMethodConfigNotSupportedException.class)
                .hasMessage(config.getType());
    }

    @Test
    void shouldReturnDeliveryMethodsFromSupportedConverter() {
        DeliveryMethodConfig config = SmsDeliveryMethodConfigMother.sms();
        Identity identity = mock(Identity.class);
        given(converter2.supports(config)).willReturn(true);
        DeliveryMethods expectedMethods = mock(DeliveryMethods.class);
        given(converter2.toDeliveryMethods(identity, config)).willReturn(expectedMethods);

        DeliveryMethods methods = compositeConverter.toDeliveryMethods(identity, config);

        assertThat(methods).isEqualTo(expectedMethods);
    }

}
