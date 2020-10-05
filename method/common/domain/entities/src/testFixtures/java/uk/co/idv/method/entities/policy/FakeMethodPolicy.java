package uk.co.idv.method.entities.policy;

import lombok.Builder;
import lombok.Data;
import uk.co.idv.identity.entities.identity.RequestedData;
import uk.co.idv.method.entities.method.MethodConfig;

@Builder
@Data
public class FakeMethodPolicy implements MethodPolicy {

    private final String name;
    private final MethodConfig config;
    private final RequestedData requestedData;

}
