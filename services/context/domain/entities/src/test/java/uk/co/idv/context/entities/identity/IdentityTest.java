package uk.co.idv.context.entities.identity;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.alias.Alias;
import uk.co.idv.context.entities.alias.Aliases;
import uk.co.idv.context.entities.alias.AliasesMother;
import uk.co.idv.context.entities.alias.CreditCardNumberMother;
import uk.co.idv.context.entities.alias.IdvId;
import uk.co.idv.context.entities.alias.IdvIdMother;
import uk.co.idv.context.entities.emailaddress.EmailAddresses;
import uk.co.idv.context.entities.emailaddress.EmailAddressesMother;
import uk.co.idv.context.entities.phonenumber.MobilePhoneNumberMother;
import uk.co.idv.context.entities.phonenumber.OtherPhoneNumberMother;
import uk.co.idv.context.entities.phonenumber.PhoneNumber;
import uk.co.idv.context.entities.phonenumber.PhoneNumbers;
import uk.co.idv.context.entities.phonenumber.PhoneNumbersMother;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityTest {

    @Test
    void shouldReturnAliases() {
        Aliases aliases = AliasesMother.idvIdAndCreditCardNumber();

        Identity identity = IdentityMother.withAliases(aliases);

        assertThat(identity.getAliases()).isEqualTo(aliases);
    }

    @Test
    void shouldReturnIdvIdAliasValue() {
        IdvId idvId = IdvIdMother.idvId();

        Identity identity = IdentityMother.withAliases(idvId);

        assertThat(identity.getIdvIdValue()).isEqualTo(idvId.getValueAsUuid());
    }

    @Test
    void shouldReturnIdvIdAlias() {
        IdvId idvId = IdvIdMother.idvId();

        Identity identity = IdentityMother.withAliases(idvId);

        assertThat(identity.getIdvId()).isEqualTo(idvId);
    }

    @Test
    void shouldReturnTrueIfHasAlias() {
        IdvId idvId = IdvIdMother.idvId();

        Identity identity = IdentityMother.withAliases(idvId);

        assertThat(identity.hasAlias(idvId)).isTrue();
    }

    @Test
    void shouldReturnFalseIfDoesNotHaveAlias() {
        IdvId idvId = IdvIdMother.idvId();

        Identity identity = IdentityMother.withAliases(idvId);

        Alias alias = CreditCardNumberMother.creditCardNumber();
        assertThat(identity.hasAlias(alias)).isFalse();
    }

    @Test
    void shouldReturnPhoneNumbers() {
        PhoneNumbers numbers = PhoneNumbersMother.mobileAndOther();

        Identity identity = IdentityMother.withPhoneNumbers(numbers);

        assertThat(identity.getPhoneNumbers()).isEqualTo(numbers);
    }

    @Test
    void shouldReturnMobilePhoneNumbers() {
        PhoneNumber mobile = MobilePhoneNumberMother.mobile();
        PhoneNumber other = OtherPhoneNumberMother.other();

        Identity identity = IdentityMother.withPhoneNumbers(mobile, other);

        assertThat(identity.getMobilePhoneNumbers()).containsExactly(mobile);
    }

    @Test
    void shouldReturnEmailAddresses() {
        EmailAddresses emailAddresses = EmailAddressesMother.two();

        Identity identity = IdentityMother.withEmailAddresses(emailAddresses);

        assertThat(identity.getEmailAddresses()).isEqualTo(emailAddresses);
    }

}
