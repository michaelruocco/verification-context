package uk.co.idv.app.spring.filters.masking.emailaddress;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import uk.co.mruoc.json.mask.JsonPathFactory;

import java.util.Collection;

public class ContextEmailAddressResponseJsonMasker extends EmailAddressJsonMasker {

    public ContextEmailAddressResponseJsonMasker(ObjectMapper mapper) {
        super(mapper, paths());
    }

    private static Collection<JsonPath> paths() {
        return JsonPathFactory.toJsonPaths(
                "$.sequences[*].methods[?(@.name=='one-time-passcode')].deliveryMethods[?(@.name=='email')].value",
                "$.request.identity.emailAddresses[*]"
        );
    }

}
