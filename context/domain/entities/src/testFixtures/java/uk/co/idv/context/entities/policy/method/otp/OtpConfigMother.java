package uk.co.idv.context.entities.policy.method.otp;


import java.time.Duration;

public interface OtpConfigMother {

    static OtpConfig build() {
        return builder().build();
    }

    static OtpConfig.OtpConfigBuilder builder() {
        return OtpConfig.builder()
                .maxNumberOfAttempts(3)
                .duration(Duration.ofMinutes(5))
                .passcodeConfig(PasscodeConfigMother.build());
    }

}
