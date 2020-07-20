package uk.co.idv.context.policy;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.policy.PolicyRequest.PolicyRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class PolicyRequestTest {

    private final PolicyRequestBuilder builder = PolicyRequest.builder();

    @Test
    void shouldReturnChannelId() {
        String channelId = "channel-id";

        PolicyRequest request = builder.channelId(channelId).build();

        assertThat(request.getChannelId()).isEqualTo(channelId);
    }

    @Test
    void shouldReturnActivityName() {
        String activityName = "activity-name";

        PolicyRequest request = builder.activityName(activityName).build();

        assertThat(request.getActivityName()).isEqualTo(activityName);
    }

    @Test
    void shouldReturnAliasType() {
        String aliasType = "alias-type";

        PolicyRequest request = builder.aliasType(aliasType).build();

        assertThat(request.getAliasType()).isEqualTo(aliasType);
    }

}
