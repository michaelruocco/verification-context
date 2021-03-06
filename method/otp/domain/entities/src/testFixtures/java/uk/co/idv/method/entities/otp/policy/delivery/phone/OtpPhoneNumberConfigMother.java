package uk.co.idv.method.entities.otp.policy.delivery.phone;

import com.neovisionaries.i18n.CountryCode;
import uk.co.idv.method.entities.otp.policy.delivery.LastUpdatedConfigMother;
import uk.co.idv.method.entities.otp.policy.delivery.phone.simswap.SimSwapConfigMother;

import java.time.Duration;

public interface OtpPhoneNumberConfigMother {

    static OtpPhoneNumberConfig build() {
        return builder().build();
    }

    static OtpPhoneNumberConfig withAsyncSimSwap() {
        return withSimSwapConfig(SimSwapConfigMother.async());
    }

    static OtpPhoneNumberConfig withSimSwapTimeout(Duration timeout) {
        return withSimSwapConfig(SimSwapConfigMother.withTimeout(timeout));
    }

    static OtpPhoneNumberConfig withSimSwapConfig(SimSwapConfig simSwapConfig) {
        return builder().simSwapConfig(simSwapConfig).build();
    }

    static OtpPhoneNumberConfig withoutSimSwapConfig() {
        return builder().simSwapConfig(null).build();
    }

    static OtpPhoneNumberConfig.OtpPhoneNumberConfigBuilder builder() {
        return OtpPhoneNumberConfig.builder()
                .country(CountryCode.GB)
                .allowInternational(false)
                .lastUpdatedConfig(LastUpdatedConfigMother.unknownAllowed())
                .simSwapConfig(SimSwapConfigMother.build());
    }

}
