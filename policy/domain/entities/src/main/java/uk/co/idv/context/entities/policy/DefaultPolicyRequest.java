package uk.co.idv.context.entities.policy;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class DefaultPolicyRequest implements PolicyRequest {

    private final String channelId;
    private final String activityName;
    private final String aliasType;

}
