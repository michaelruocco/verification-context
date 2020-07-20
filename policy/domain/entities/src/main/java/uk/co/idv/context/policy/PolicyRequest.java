package uk.co.idv.context.policy;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class PolicyRequest {

    private final String channelId;
    private final String activityName;
    private final String aliasType;

}
