package uk.co.idv.identity.entities.eligibility;

import uk.co.idv.identity.entities.alias.AliasesMother;
import uk.co.idv.identity.entities.alias.DefaultAliases;
import uk.co.idv.identity.entities.channel.Channel;
import uk.co.idv.identity.entities.channel.DefaultChannelMother;
import uk.co.idv.identity.entities.eligibility.CreateEligibilityRequest.CreateEligibilityRequestBuilder;
import uk.co.idv.identity.entities.identity.RequestedData;
import uk.co.idv.identity.entities.identity.RequestedDataMother;

public interface CreateEligibilityRequestMother {

    static CreateEligibilityRequest withChannel(Channel channel) {
        return builder().channel(channel).build();
    }

    static CreateEligibilityRequest withAliases(DefaultAliases aliases) {
        return builder().aliases(aliases).build();
    }

    static CreateEligibilityRequest withRequestedData(RequestedData requestedData) {
        return builder().requestedData(requestedData).build();
    }

    static CreateEligibilityRequest build() {
        return builder().build();
    }

    static CreateEligibilityRequestBuilder builder() {
        return CreateEligibilityRequest.builder()
                .aliases(AliasesMother.creditCardNumberOnly())
                .channel(DefaultChannelMother.build())
                .requestedData(RequestedDataMother.allRequested());
    }

}
