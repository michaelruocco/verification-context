package uk.co.idv.method.entities.otp.delivery.phone.eligibility;

import uk.co.idv.method.entities.eligibility.Ineligible;

public class NotAMobileNumber extends Ineligible {

    public NotAMobileNumber() {
        super("not a mobile number");
    }

}
