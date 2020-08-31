package uk.co.idv.context.entities.policy.method.otp.delivery.phone;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.policy.method.otp.delivery.LastUpdatedConfig;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OtpPhoneNumberConfigTest {

    @Test
    void shouldReturnRegion() {
        CountryCode region = CountryCode.GB;

        OtpPhoneNumberConfig config = OtpPhoneNumberConfig.builder()
                .region(region)
                .build();

        assertThat(config.getRegion()).isEqualTo(region);
    }

    @Test
    void shouldReturnAllowInternational() {
        OtpPhoneNumberConfig config = OtpPhoneNumberConfig.builder()
                .allowInternational(true)
                .build();

        assertThat(config.isAllowInternational()).isTrue();
    }

    @Test
    void shouldReturnLastUpdatedConfig() {
        LastUpdatedConfig lastUpdatedConfig = mock(LastUpdatedConfig.class);

        OtpPhoneNumberConfig config = OtpPhoneNumberConfig.builder()
                .lastUpdatedConfig(lastUpdatedConfig)
                .build();

        assertThat(config.getLastUpdatedConfig()).isEqualTo(lastUpdatedConfig);
    }

    @Test
    void shouldReturnLastUpdatedValidResultIfAllowInternationalIsTrue() {
        OtpPhoneNumber number = mock(OtpPhoneNumber.class);
        Instant now = Instant.now();
        LastUpdatedConfig lastUpdatedConfig = mock(LastUpdatedConfig.class);
        given(lastUpdatedConfig.isValid(number, now)).willReturn(true);
        OtpPhoneNumberConfig config = OtpPhoneNumberConfig.builder()
                .allowInternational(true)
                .lastUpdatedConfig(lastUpdatedConfig)
                .build();

        boolean valid = config.isValid(number, now);

        assertThat(valid).isTrue();
    }

    @Test
    void shouldReturnNotValidIfInternationalNotAllowedAndPhoneNumberIsInternational() {
        OtpPhoneNumber number = givenInternationalPhoneNumber();
        Instant now = Instant.now();
        OtpPhoneNumberConfig config = OtpPhoneNumberConfig.builder()
                .allowInternational(false)
                .build();

        boolean valid = config.isValid(number, now);

        assertThat(valid).isFalse();
    }

    @Test
    void shouldReturnValidIfInternationalNotAllowedAndNumberIsNotInternationalAndLastUpdateValid() {
        OtpPhoneNumber number = mock(OtpPhoneNumber.class);
        Instant now = Instant.now();
        LastUpdatedConfig lastUpdatedConfig = mock(LastUpdatedConfig.class);
        given(lastUpdatedConfig.isValid(number, now)).willReturn(true);
        OtpPhoneNumberConfig config = OtpPhoneNumberConfig.builder()
                .allowInternational(false)
                .lastUpdatedConfig(lastUpdatedConfig)
                .build();

        boolean valid = config.isValid(number, now);

        assertThat(valid).isTrue();
    }

    private OtpPhoneNumber givenInternationalPhoneNumber() {
        OtpPhoneNumber number = mock(OtpPhoneNumber.class);
        given(number.isInternational()).willReturn(true);
        return number;
    }

}
