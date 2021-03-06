package uk.co.idv.method.usecases.otp.delivery.phone;

import org.junit.jupiter.api.Test;
import uk.co.idv.identity.entities.identity.Identity;
import uk.co.idv.identity.entities.identity.IdentityMother;
import uk.co.idv.identity.entities.phonenumber.PhoneNumbers;
import uk.co.idv.method.entities.otp.delivery.DeliveryMethods;
import uk.co.idv.method.entities.otp.delivery.phone.OtpPhoneNumbers;
import uk.co.idv.method.entities.otp.policy.delivery.DeliveryMethodConfig;
import uk.co.idv.method.entities.otp.policy.delivery.phone.PhoneDeliveryMethodConfig;
import uk.co.idv.method.entities.otp.policy.delivery.phone.sms.SmsDeliveryMethodConfigMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PhoneDeliveryMethodConfigConverterTest {

    private final PhoneNumbersConverter numbersConverter = mock(PhoneNumbersConverter.class);
    private final OtpPhoneNumbersConverter otpNumbersConverter = mock(OtpPhoneNumbersConverter.class);

    private final PhoneDeliveryMethodConfigConverter converter = PhoneDeliveryMethodConfigConverter.builder()
            .numbersConverter(numbersConverter)
            .otpNumbersConverter(otpNumbersConverter)
            .build();

    @Test
    void shouldSupportPhoneDeliveryMethodConfig() {
        DeliveryMethodConfig config = mock(PhoneDeliveryMethodConfig.class);

        boolean supported = converter.supports(config);

        assertThat(supported).isTrue();
    }

    @Test
    void shouldNotSupportAnyOtherDeliveryMethodConfig() {
        DeliveryMethodConfig config = mock(DeliveryMethodConfig.class);

        boolean supported = converter.supports(config);

        assertThat(supported).isFalse();
    }

    @Test
    void shouldConvertConfigToDeliveryMethods() {
        PhoneDeliveryMethodConfig config = SmsDeliveryMethodConfigMother.sms();
        Identity identity = IdentityMother.example();
        OtpPhoneNumbers otpPhoneNumbers = givenConvertToOtpPhoneNumbers(identity.getPhoneNumbers(), config);
        DeliveryMethods expectedMethods = givenConvertToDeliveryMethods(otpPhoneNumbers, config);

        DeliveryMethods methods = converter.toDeliveryMethods(identity, config);

        assertThat(methods).isEqualTo(expectedMethods);
    }

    @Test
    void shouldThrowExceptionIfAttemptToConvertAnyOtherDeliveryMethodConfig() {
        DeliveryMethodConfig config = mock(DeliveryMethodConfig.class);
        Identity identity = mock(Identity.class);

        Throwable error = catchThrowable(() -> converter.toDeliveryMethods(identity, config));

        assertThat(error).isInstanceOf(ClassCastException.class);
    }

    private OtpPhoneNumbers givenConvertToOtpPhoneNumbers(PhoneNumbers numbers, PhoneDeliveryMethodConfig config) {
        OtpPhoneNumbers otpNumbers = mock(OtpPhoneNumbers.class);
        given(numbersConverter.toOtpPhoneNumbers(numbers, config.getCountry())).willReturn(otpNumbers);
        return otpNumbers;
    }

    private DeliveryMethods givenConvertToDeliveryMethods(OtpPhoneNumbers otpNumbers, PhoneDeliveryMethodConfig config) {
        DeliveryMethods methods = mock(DeliveryMethods.class);
        given(otpNumbersConverter.toDeliveryMethods(otpNumbers, config)).willReturn(methods);
        return methods;
    }

}
