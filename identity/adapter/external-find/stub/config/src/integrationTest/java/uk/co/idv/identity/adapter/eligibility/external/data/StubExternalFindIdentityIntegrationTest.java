package uk.co.idv.identity.adapter.eligibility.external.data;

import org.junit.jupiter.api.Test;
import uk.co.idv.identity.adapter.eligibility.external.StubExternalFindIdentityConfig;
import uk.co.idv.identity.entities.alias.Aliases;
import uk.co.idv.identity.entities.alias.AliasesMother;
import uk.co.idv.identity.entities.alias.CardNumberMother;
import uk.co.idv.identity.entities.emailaddress.EmailAddressesMother;
import uk.co.idv.identity.entities.identity.Identity;
import uk.co.idv.identity.entities.phonenumber.PhoneNumbersMother;
import uk.co.idv.identity.entities.eligibility.CreateEligibilityRequestMother;
import uk.co.idv.identity.usecases.eligibility.external.ExternalFindIdentity;
import uk.co.idv.identity.entities.identity.FindIdentityRequest;

import static org.assertj.core.api.Assertions.assertThat;

class StubExternalFindIdentityIntegrationTest {

    private final StubExternalFindIdentityConfig config = StubExternalFindIdentityConfig.withNoDelays();
    private final ExternalFindIdentity find = config.externalFindIdentity();

    @Test
    void shouldFindStubbedIdentityWithCountryCode() {
        FindIdentityRequest request = CreateEligibilityRequestMother.build();

        Identity identity = find.find(request);

        assertThat(identity.getCountry()).isEqualTo(request.getCountry());
    }

    @Test
    void shouldFindStubbedIdentityWithAliases() {
        FindIdentityRequest request = CreateEligibilityRequestMother.build();

        Identity identity = find.find(request);

        Aliases expectedAliases = AliasesMother.with(CardNumberMother.debit1());
        assertThat(identity.getAliases()).isEqualTo(expectedAliases.add(request.getAliases()));
    }

    @Test
    void shouldFindStubbedIdentityWithPhoneNumbers() {
        FindIdentityRequest request = CreateEligibilityRequestMother.build();

        Identity identity = find.find(request);

        assertThat(identity.getPhoneNumbers()).isEqualTo(PhoneNumbersMother.two());
    }

    @Test
    void shouldFindStubbedIdentityWithEmailAddresses() {
        FindIdentityRequest request = CreateEligibilityRequestMother.build();

        Identity identity = find.find(request);

        assertThat(identity.getEmailAddresses()).containsExactlyElementsOf(EmailAddressesMother.two());
    }

}
