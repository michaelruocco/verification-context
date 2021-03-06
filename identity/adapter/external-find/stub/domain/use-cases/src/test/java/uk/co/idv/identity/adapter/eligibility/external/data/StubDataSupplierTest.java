package uk.co.idv.identity.adapter.eligibility.external.data;

import org.junit.jupiter.api.Test;
import uk.co.idv.common.entities.async.Delay;
import uk.co.idv.identity.entities.alias.AliasesMother;
import uk.co.idv.identity.entities.alias.CardNumberMother;
import uk.co.idv.identity.entities.alias.DefaultAliases;
import uk.co.idv.identity.entities.phonenumber.PhoneNumbers;
import uk.co.idv.identity.adapter.eligibility.external.data.phonenumber.StubPhoneNumberFactory;
import uk.co.idv.identity.usecases.eligibility.external.data.AsyncDataLoadRequest;
import uk.co.idv.identity.usecases.eligibility.external.data.AsyncDataLoadRequestMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class StubDataSupplierTest {

    private final Delay delay = mock(Delay.class);
    private final StubPhoneNumberFactory factory = new StubPhoneNumberFactory();

    @Test
    void shouldReturnEmptyDataIfAtLeastOneAliasValueEndsIn9() {
        AsyncDataLoadRequest request = toRequest(AliasesMother.with(CardNumberMother.creditWithValueEndingIn9()));
        StubDataSupplier<PhoneNumbers> supplier = buildSupplier(request);

        PhoneNumbers phoneNumbers = supplier.get();

        assertThat(phoneNumbers).isEqualTo(factory.getEmptyData());
    }

    @Test
    void shouldReturnPopulatedDataIfAllAliasValuesDoNotEndIn9() {
        AsyncDataLoadRequest request = toRequest(AliasesMother.with(CardNumberMother.credit()));
        StubDataSupplier<PhoneNumbers> supplier = buildSupplier(request);

        PhoneNumbers phoneNumbers = supplier.get();

        assertThat(phoneNumbers).isEqualTo(factory.getPopulatedData());
    }

    private AsyncDataLoadRequest toRequest(DefaultAliases aliases) {
        return AsyncDataLoadRequestMother.withAliases(aliases);
    }

    private StubDataSupplier<PhoneNumbers> buildSupplier(AsyncDataLoadRequest request) {
        return new StubDataSupplier<>(request.getAliases(), delay, factory);
    }

}
