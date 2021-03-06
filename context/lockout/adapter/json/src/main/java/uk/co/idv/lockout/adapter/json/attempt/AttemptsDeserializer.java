package uk.co.idv.lockout.adapter.json.attempt;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uk.co.idv.identity.entities.alias.IdvId;
import uk.co.idv.lockout.entities.attempt.Attempt;
import uk.co.idv.lockout.entities.attempt.Attempts;
import uk.co.mruoc.json.jackson.JsonNodeConverter;
import uk.co.mruoc.json.jackson.JsonParserConverter;

import java.util.Collection;
import java.util.UUID;

public class AttemptsDeserializer extends StdDeserializer<Attempts> {

    private static final TypeReference<Collection<Attempt>> ATTEMPT_COLLECTION = new TypeReference<>() {
        // intentionally blank
    };

    public AttemptsDeserializer() {
        super(Attempts.class);
    }

    @Override
    public Attempts deserialize(JsonParser parser, DeserializationContext context) {
        JsonNode node = JsonParserConverter.toNode(parser);
        return Attempts.builder()
                .id(UUID.fromString(node.get("id").asText()))
                .idvId(new IdvId(node.get("idvId").asText()))
                .attempts(JsonNodeConverter.toCollection(node.get("attempts"), parser, ATTEMPT_COLLECTION))
                .build();
    }

}
