package uk.co.idv.identity.usecases.eligibility;

import org.junit.jupiter.api.Test;
import uk.co.idv.identity.entities.channel.Channel;
import uk.co.idv.identity.entities.channel.DefaultChannelMother;
import uk.co.idv.identity.entities.eligibility.CreateEligibilityRequest;
import uk.co.idv.identity.entities.eligibility.CreateEligibilityRequestMother;
import uk.co.idv.identity.entities.eligibility.IdentityEligibility;
import uk.co.idv.identity.entities.eligibility.IdentityEligibilityMother;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CompositeCreateEligibilityTest {

    private final ChannelCreateEligibility channelCreate1 = buildCreateForChannels("my-channel-1");
    private final ChannelCreateEligibility channelCreate2 = buildCreateForChannels("my-channel-2");

    private final CompositeCreateEligibility compositeCreate = new CompositeCreateEligibility(
            channelCreate1,
            channelCreate2
    );

    @Test
    void shouldPerformUpdateForGivenChannelId() {
        Channel channel = DefaultChannelMother.withId("my-channel-1");
        CreateEligibilityRequest request = CreateEligibilityRequestMother.withChannel(channel);
        IdentityEligibility expectedEligibility = IdentityEligibilityMother.build();
        given(channelCreate1.create(request)).willReturn(expectedEligibility);

        IdentityEligibility eligibility = compositeCreate.create(request);

        assertThat(eligibility).isEqualTo(expectedEligibility);
    }

    @Test
    void shouldThrowExceptionIfUpdateNotConfiguredForChannelId() {
        String channelId = "not-configured-channel";
        Channel channel = DefaultChannelMother.withId(channelId);
        CreateEligibilityRequest request = CreateEligibilityRequestMother.withChannel(channel);

        Throwable error = catchThrowable(() -> compositeCreate.create(request));

        assertThat(error)
                .isInstanceOf(EligibilityNotConfiguredException.class)
                .hasMessage(channelId);
    }

    private ChannelCreateEligibility buildCreateForChannels(String... channelIds) {
        ChannelCreateEligibility update = mock(ChannelCreateEligibility.class);
        given(update.getSupportedChannelIds()).willReturn(Arrays.asList(channelIds));
        return update;
    }

}
