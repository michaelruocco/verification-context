package uk.co.idv.context.entities.policy.method.otp.delivery;

import uk.co.idv.method.entities.policy.RequestedDataProvider;

public interface DeliveryMethodConfig extends RequestedDataProvider {

    String getType();

}
