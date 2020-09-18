package uk.co.idv.context.adapter.json.policy.method.otp.delivery;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import uk.co.idv.context.adapter.json.policy.method.otp.delivery.email.EmailDeliveryMethodConfigJsonMother;
import uk.co.idv.context.adapter.json.policy.method.otp.delivery.phone.SmsDeliveryMethodConfigJsonMother;
import uk.co.idv.context.adapter.json.policy.method.otp.delivery.phone.VoiceDeliveryMethodConfigJsonMother;
import uk.co.idv.context.entities.policy.method.otp.delivery.email.EmailDeliveryMethodConfigMother;
import uk.co.idv.context.entities.policy.method.otp.delivery.phone.sms.SmsDeliveryMethodConfigMother;
import uk.co.idv.context.entities.policy.method.otp.delivery.phone.voice.VoiceDeliveryMethodConfigMother;

import java.util.stream.Stream;

public class DeliveryMethodConfigArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(SmsDeliveryMethodConfigJsonMother.sms(), SmsDeliveryMethodConfigMother.sms()),
                Arguments.of(VoiceDeliveryMethodConfigJsonMother.voice(), VoiceDeliveryMethodConfigMother.voice()),
                Arguments.of(EmailDeliveryMethodConfigJsonMother.email(), EmailDeliveryMethodConfigMother.email())
        );
    }

}
