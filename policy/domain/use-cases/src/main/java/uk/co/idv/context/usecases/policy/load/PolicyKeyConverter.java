package uk.co.idv.context.usecases.policy.load;

import lombok.extern.slf4j.Slf4j;
import uk.co.idv.context.entities.policy.PolicyKey;
import uk.co.idv.context.entities.policy.PolicyRequest;
import uk.co.idv.context.entities.policy.PolicyRequest.PolicyRequestBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class PolicyKeyConverter {

    public Collection<PolicyRequest> toPolicyRequests(PolicyKey key) {
        Collection<PolicyRequest> requests = new ArrayList<>();
        PolicyRequestBuilder builder = PolicyRequest.builder().channelId(key.getChannelId());
        for (String activityName : key.getActivityNames()) {
            builder.activityName(activityName);
            for (String aliasType : key.getAliasTypes()) {
                builder.aliasType(aliasType);
                requests.add(builder.build());
            }
        }
        log.info("converted key {} into policy requests {}", key, requests);
        return requests;
    }

}