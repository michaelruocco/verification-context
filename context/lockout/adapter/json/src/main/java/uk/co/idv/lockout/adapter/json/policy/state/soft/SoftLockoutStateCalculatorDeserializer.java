package uk.co.idv.lockout.adapter.json.policy.state.soft;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uk.co.idv.lockout.entities.policy.includeattempt.IncludeAttemptsPolicy;
import uk.co.idv.lockout.entities.policy.soft.SoftLockIntervals;
import uk.co.idv.lockout.entities.policy.soft.SoftLockoutStateCalculator;
import uk.co.mruoc.json.jackson.JsonNodeConverter;
import uk.co.mruoc.json.jackson.JsonParserConverter;

public class SoftLockoutStateCalculatorDeserializer extends StdDeserializer<SoftLockoutStateCalculator> {

    public SoftLockoutStateCalculatorDeserializer() {
        super(SoftLockoutStateCalculator.class);
    }

    @Override
    public SoftLockoutStateCalculator deserialize(JsonParser parser, DeserializationContext context) {
        JsonNode node = JsonParserConverter.toNode(parser);
        return SoftLockoutStateCalculator.builder()
                .intervals(JsonNodeConverter.toObject(node.get("intervals"), parser, SoftLockIntervals.class))
                .includeAttemptsPolicy(JsonNodeConverter.toObject(node.get("includeAttemptsPolicy"), parser, IncludeAttemptsPolicy.class))
                .build();
    }

}
