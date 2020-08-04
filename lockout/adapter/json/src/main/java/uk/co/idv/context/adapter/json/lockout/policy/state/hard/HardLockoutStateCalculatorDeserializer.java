package uk.co.idv.context.adapter.json.lockout.policy.state.hard;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uk.co.idv.context.entities.lockout.policy.hard.HardLockoutStateCalculator;
import uk.co.mruoc.json.jackson.JsonParserConverter;

public class HardLockoutStateCalculatorDeserializer extends StdDeserializer<HardLockoutStateCalculator> {

    public HardLockoutStateCalculatorDeserializer() {
        super(HardLockoutStateCalculator.class);
    }

    @Override
    public HardLockoutStateCalculator deserialize(JsonParser parser, DeserializationContext context) {
        JsonNode node = JsonParserConverter.toNode(parser);
        return new HardLockoutStateCalculator(node.get("maxNumberOfAttempts").asInt());
    }

}