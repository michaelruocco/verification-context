package uk.co.idv.context.entities.emailaddress;

import java.util.Arrays;

public interface EmailAddressesMother {

    static EmailAddresses empty() {
        return with();
    }

    static EmailAddresses two() {
        return with("joe.bloggs@hotmail.co.uk", "joebloggs@yahoo.co.uk");
    }

    static EmailAddresses with(String... values) {
        return new EmailAddresses(Arrays.asList(values));
    }

}