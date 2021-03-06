package uk.co.idv.lockout.adapter.json.policy.state;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uk.co.idv.identity.entities.alias.Alias;
import uk.co.idv.lockout.entities.policy.LockoutStateCalculator;
import uk.co.idv.lockout.entities.policy.hard.HardLockoutStateCalculator;
import uk.co.idv.lockout.entities.policy.nonlocking.NonLockingStateCalculator;
import uk.co.idv.lockout.entities.policy.soft.RecurringSoftLockoutStateCalculator;
import uk.co.idv.lockout.entities.policy.soft.SoftLockoutStateCalculator;
import uk.co.mruoc.json.jackson.JsonNodeConverter;
import uk.co.mruoc.json.jackson.JsonParserConverter;

import java.util.Map;
import java.util.Optional;

public class LockoutStateCalculatorDeserializer extends StdDeserializer<LockoutStateCalculator> {

    private final Map<String, Class<? extends LockoutStateCalculator>> mappings;

    protected LockoutStateCalculatorDeserializer() {
        this(buildMappings());
    }

    public LockoutStateCalculatorDeserializer(Map<String, Class<? extends LockoutStateCalculator>> mappings) {
        super(Alias.class);
        this.mappings = mappings;
    }

    @Override
    public LockoutStateCalculator deserialize(JsonParser parser, DeserializationContext context) {
        JsonNode node = JsonParserConverter.toNode(parser);
        String type = extractType(node);
        return JsonNodeConverter.toObject(node, parser, toMappingType(type));
    }

    private static String extractType(JsonNode node) {
        return node.get("type").asText();
    }

    private Class<? extends LockoutStateCalculator> toMappingType(String name) {
        return Optional.ofNullable(mappings.get(name))
                .orElseThrow(() -> new InvalidLockoutStateCalculatorTypeException(name));
    }

    private static Map<String, Class<? extends LockoutStateCalculator>> buildMappings() {
        return Map.of(
                HardLockoutStateCalculator.TYPE, HardLockoutStateCalculator.class,
                NonLockingStateCalculator.TYPE, NonLockingStateCalculator.class,
                SoftLockoutStateCalculator.TYPE, SoftLockoutStateCalculator.class,
                RecurringSoftLockoutStateCalculator.TYPE, RecurringSoftLockoutStateCalculator.class
        );
    }

}
