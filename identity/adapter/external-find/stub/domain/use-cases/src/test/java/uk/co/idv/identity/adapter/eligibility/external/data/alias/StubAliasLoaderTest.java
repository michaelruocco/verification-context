package uk.co.idv.identity.adapter.eligibility.external.data.alias;

import org.junit.jupiter.api.Test;
import uk.co.idv.identity.entities.alias.Aliases;
import uk.co.idv.identity.entities.alias.AliasesMother;
import uk.co.idv.identity.entities.alias.CardNumberMother;
import uk.co.idv.identity.entities.alias.DefaultAliases;
import uk.co.idv.identity.entities.eligibility.CreateEligibilityRequestMother;
import uk.co.idv.identity.entities.identity.FindIdentityRequest;
import uk.co.idv.identity.usecases.eligibility.external.data.AliasLoader;

import static org.assertj.core.api.Assertions.assertThat;

class StubAliasLoaderTest {

    private final AliasLoader loader = new StubAliasLoader();

    @Test
    void shouldReturnInputAliasesIfAllAliasValuesDoNotEndIn9AndDoesNotContainCreditCardNumber() {
        DefaultAliases inputAliases = AliasesMother.with(CardNumberMother.debit());
        FindIdentityRequest request = CreateEligibilityRequestMother.withAliases(inputAliases);

        Aliases loadedAliases = loader.load(request);

        assertThat(loadedAliases).isEqualTo(inputAliases);
    }

    @Test
    void shouldReturnAdditionalDebitCardAliasIfAllAliasValuesDoNotEndIn9AndContainsCreditCardNumber() {
        DefaultAliases inputAliases = AliasesMother.with(CardNumberMother.creditWithValue("4929111111111111"));
        FindIdentityRequest request = CreateEligibilityRequestMother.withAliases(inputAliases);

        Aliases loadedAliases = loader.load(request);

        assertThat(loadedAliases)
                .hasSize(inputAliases.size() + 1)
                .containsAll(inputAliases)
                .contains(CardNumberMother.debitWithValue("5929111111111111"));
    }

    @Test
    void shouldReturnEmptyAliasesIfAnyAliasValuesEndIn9() {
        FindIdentityRequest request = CreateEligibilityRequestMother.withAliases(
                AliasesMother.with(CardNumberMother.creditWithValueEndingIn9())
        );

        Aliases aliases = loader.load(request);

        assertThat(aliases).isEqualTo(AliasesMother.empty());
    }

}
