package uk.co.idv.context.adapter.json.policy.method.otp.delivery.email;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import uk.co.idv.method.entities.otp.policy.delivery.email.EmailDeliveryMethodConfig;

public class EmailDeliveryMethodConfigModule extends SimpleModule {

    public EmailDeliveryMethodConfigModule() {
        super("email-delivery-method-config-module", Version.unknownVersion());

        addDeserializer(EmailDeliveryMethodConfig.class, new EmailDeliveryMethodConfigDeserializer());
    }

}
