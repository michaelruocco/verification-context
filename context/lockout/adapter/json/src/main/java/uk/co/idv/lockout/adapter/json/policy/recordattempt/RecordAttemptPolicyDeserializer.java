package uk.co.idv.lockout.adapter.json.policy.recordattempt;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uk.co.idv.identity.entities.alias.Alias;
import uk.co.idv.lockout.entities.policy.RecordAttemptPolicy;
import uk.co.idv.lockout.entities.policy.recordattempt.AlwaysRecordAttemptPolicy;
import uk.co.idv.lockout.entities.policy.recordattempt.NeverRecordAttemptPolicy;
import uk.co.idv.lockout.entities.policy.recordattempt.RecordAttemptWhenMethodCompletePolicy;
import uk.co.idv.lockout.entities.policy.recordattempt.RecordAttemptWhenSequenceCompletePolicy;
import uk.co.mruoc.json.jackson.JsonNodeConverter;
import uk.co.mruoc.json.jackson.JsonParserConverter;

import java.util.Map;
import java.util.Optional;

public class RecordAttemptPolicyDeserializer extends StdDeserializer<RecordAttemptPolicy> {

    private final Map<String, Class<? extends RecordAttemptPolicy>> mappings;

    protected RecordAttemptPolicyDeserializer() {
        this(buildMappings());
    }

    public RecordAttemptPolicyDeserializer(Map<String, Class<? extends RecordAttemptPolicy>> mappings) {
        super(Alias.class);
        this.mappings = mappings;
    }

    @Override
    public RecordAttemptPolicy deserialize(JsonParser parser, DeserializationContext context) {
        JsonNode node = JsonParserConverter.toNode(parser);
        String type = extractType(node);
        return JsonNodeConverter.toObject(node, parser, toMappingType(type));
    }

    private static String extractType(JsonNode node) {
        return node.get("type").asText();
    }

    private Class<? extends RecordAttemptPolicy> toMappingType(String name) {
        return Optional.ofNullable(mappings.get(name))
                .orElseThrow(() -> new InvalidRecordAttemptPolicyTypeException(name));
    }

    private static Map<String, Class<? extends RecordAttemptPolicy>> buildMappings() {
        return Map.of(
                AlwaysRecordAttemptPolicy.TYPE, AlwaysRecordAttemptPolicy.class,
                NeverRecordAttemptPolicy.TYPE, NeverRecordAttemptPolicy.class,
                RecordAttemptWhenMethodCompletePolicy.TYPE, RecordAttemptWhenMethodCompletePolicy.class,
                RecordAttemptWhenSequenceCompletePolicy.TYPE, RecordAttemptWhenSequenceCompletePolicy.class
        );
    }

}
