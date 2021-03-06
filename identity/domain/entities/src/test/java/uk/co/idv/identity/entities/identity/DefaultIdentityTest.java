package uk.co.idv.identity.entities.identity;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import uk.co.idv.identity.entities.alias.Alias;
import uk.co.idv.identity.entities.alias.Aliases;
import uk.co.idv.identity.entities.alias.AliasesMother;
import uk.co.idv.identity.entities.alias.CardNumberMother;
import uk.co.idv.identity.entities.alias.DefaultAliases;
import uk.co.idv.identity.entities.alias.IdvId;
import uk.co.idv.identity.entities.alias.IdvIdAlreadyPresentException;
import uk.co.idv.identity.entities.alias.IdvIdMother;
import uk.co.idv.identity.entities.emailaddress.EmailAddresses;
import uk.co.idv.identity.entities.emailaddress.EmailAddressesMother;
import uk.co.idv.identity.entities.phonenumber.PhoneNumbers;
import uk.co.idv.identity.entities.phonenumber.PhoneNumbersMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DefaultIdentityTest {

    @Test
    void shouldReturnCountry() {
        CountryCode country = CountryCode.GB;

        Identity identity = IdentityMother.withCountry(country);

        assertThat(identity.getCountry()).isEqualTo(country);
    }

    @Test
    void shouldReturnAliases() {
        DefaultAliases aliases = AliasesMother.idvIdAndCreditCardNumber();

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
        Alias alias = CardNumberMother.credit();

        Identity identity = IdentityMother.withAliases(alias);

        assertThat(identity.hasAlias(alias)).isTrue();
    }

    @Test
    void shouldReturnFalseIfDoesNotHaveAlias() {
        Alias creditCardNumber = CardNumberMother.credit();

        Identity identity = IdentityMother.withAliases(creditCardNumber);

        Alias debitCardNumber = CardNumberMother.debit();
        assertThat(identity.hasAlias(debitCardNumber)).isFalse();
    }

    @Test
    void shouldReturnTrueIfHasIdvId() {
        Identity identity = IdentityMother.withAliases(IdvIdMother.idvId());

        assertThat(identity.hasIdvId()).isTrue();
    }

    @Test
    void shouldReturnFalseIfDoesHaveIdvId() {
        Identity identity = IdentityMother.withAliases(AliasesMother.empty());

        assertThat(identity.hasIdvId()).isFalse();
    }

    @Test
    void shouldReturnPhoneNumbers() {
        PhoneNumbers numbers = PhoneNumbersMother.two();

        Identity identity = IdentityMother.withPhoneNumbers(numbers);

        assertThat(identity.getPhoneNumbers()).isEqualTo(numbers);
    }

    @Test
    void shouldReturnEmailAddresses() {
        EmailAddresses emailAddresses = EmailAddressesMother.two();

        Identity identity = IdentityMother.withEmailAddresses(emailAddresses);

        assertThat(identity.getEmailAddresses()).isEqualTo(emailAddresses);
    }

    @Test
    void shouldReturnAliasesNotPresentInOtherIdentity() {
        DefaultAliases aliases = mock(DefaultAliases.class);
        DefaultAliases otherAliases = mock(DefaultAliases.class);
        Aliases expectedAliases = mock(DefaultAliases.class);
        given(aliases.notPresent(otherAliases)).willReturn(expectedAliases);
        Identity identity = IdentityMother.withAliases(aliases);
        Identity other = IdentityMother.withAliases(otherAliases);

        Aliases notPresent = identity.getAliasesNotPresent(other);

        assertThat(notPresent).isEqualTo(expectedAliases);
    }

    @Test
    void shouldReturnHasIdvAliasFromAliases() {
        Aliases aliases = mock(DefaultAliases.class);
        given(aliases.hasIdvId()).willReturn(true);

        boolean hasIdvId = aliases.hasIdvId();

        assertThat(hasIdvId).isTrue();
    }

    @Test
    void shouldCopyAllOtherAttributesWhenSettingIdvIdAlias() {
        DefaultAliases aliases = mock(DefaultAliases.class);
        Identity identity = IdentityMother.withAliases(aliases);
        IdvId idvId = IdvIdMother.idvId();
        Aliases updatedAliases = mock(DefaultAliases.class);
        given(aliases.add(idvId)).willReturn(updatedAliases);

        Identity updatedIdentity = identity.setIdvId(idvId);

        assertThat(updatedIdentity).usingRecursiveComparison().ignoringFields("aliases").isEqualTo(identity);
        assertThat(updatedIdentity.getAliases()).isEqualTo(updatedAliases);
    }

    @Test
    void shouldThrowExceptionIfAttemptToAddAllDataAndBothIdentitiesHaveDifferentIdvIds() {
        Identity identity = IdentityMother.example();
        Identity other = IdentityMother.example1();

        IdvIdAlreadyPresentException error = catchThrowableOfType(
                () -> identity.addData(other),
                IdvIdAlreadyPresentException.class
        );

        assertThat(error.getExisting()).isEqualTo(identity.getIdvId());
        assertThat(error.getUpdated()).isEqualTo(other.getIdvId());
    }

    @Test
    void shouldThrowExceptionIfAttemptToAddAllDataAndBothIdentitiesHaveDifferentCountries() {
        Identity identity = IdentityMother.example();
        Identity other = IdentityMother.withCountry(CountryCode.DE);

        CountryMismatchException error = catchThrowableOfType(
                () -> identity.addData(other),
                CountryMismatchException.class
        );

        assertThat(error.getExisting()).isEqualTo(identity.getCountry());
        assertThat(error.getUpdated()).isEqualTo(other.getCountry());
    }

    @Test
    void shouldAddAllAliasesFromOtherIdentity() {
        Identity identity = IdentityMother.withoutIdvId();
        Identity other = IdentityMother.example1();

        Identity added = identity.addData(other);

        Aliases expected = identity.getAliases().add(other.getAliases());
        assertThat(added.getAliases()).containsExactlyElementsOf(expected);
    }

    @Test
    void shouldAddAllPhoneNumbersFromOtherIdentity() {
        Identity identity = IdentityMother.withoutIdvId();
        Identity other = IdentityMother.example1();

        Identity added = identity.addData(other);

        PhoneNumbers expected = identity.getPhoneNumbers().add(other.getPhoneNumbers());
        assertThat(added.getPhoneNumbers()).containsExactlyElementsOf(expected);
    }

    @Test
    void shouldAddAllEmailAddressesFromOtherIdentity() {
        Identity identity = IdentityMother.withoutIdvId();
        Identity other = IdentityMother.example1();

        Identity added = identity.addData(other);

        EmailAddresses expected = identity.getEmailAddresses().add(other.getEmailAddresses());
        assertThat(added.getEmailAddresses()).containsExactlyElementsOf(expected);
    }

    @Test
    void shouldAddAllAliasesFromMoreThanOneOtherIdentity() {
        Identity identity = IdentityMother.withAliases(AliasesMother.empty());
        Identity other1 = IdentityMother.withAliases(AliasesMother.idvIdAndDebitCardNumber());
        Identity other2 = IdentityMother.withAliases(AliasesMother.idvIdAndCreditCardNumber());

        Identity added = identity.addData(IdentitiesMother.with(other1, other2));

        assertThat(added.getAliases()).containsExactly(
                CardNumberMother.debit(),
                CardNumberMother.credit()
        );
    }

    @Test
    void shouldRemoveIdvId() {
        Identity identity = IdentityMother.example();

        Identity updated = identity.removeIdvId();

        assertThat(updated).usingRecursiveComparison().ignoringFields("aliases").isEqualTo(identity);
        assertThat(updated.getAliases()).containsExactlyElementsOf(identity.getAliases().remove(identity.getIdvId()));
    }

    @Test
    void shouldReturnTrueIfHasCountry() {
        CountryCode country = CountryCode.GB;

        Identity identity = IdentityMother.withCountry(country);

        assertThat(identity.hasCountry()).isTrue();
    }

    @Test
    void shouldReturnFalseIfDoesNotHaveCountry() {
        Identity identity = IdentityMother.withoutCountry();

        assertThat(identity.hasCountry()).isFalse();
    }

    @Test
    void shouldReturnCopyWithUpdatedPhoneNumbers() {
        PhoneNumbers phoneNumbers = PhoneNumbersMother.empty();
        Identity identity = IdentityMother.example();

        Identity updated = identity.withPhoneNumbers(phoneNumbers);

        assertThat(updated).usingRecursiveComparison().ignoringFields("phoneNumbers").isEqualTo(identity);
        assertThat(updated.getPhoneNumbers()).isEqualTo(phoneNumbers);
    }

    @Test
    void shouldReturnCopyWithUpdatedEmailAddresses() {
        EmailAddresses emailAddresses = EmailAddressesMother.with("joe.bloggs@hotmail.co.uk");
        Identity identity = IdentityMother.example();

        Identity updated = identity.withEmailAddresses(emailAddresses);

        assertThat(updated).usingRecursiveComparison().ignoringFields("emailAddresses").isEqualTo(identity);
        assertThat(updated.getEmailAddresses()).isEqualTo(emailAddresses);
    }

}
