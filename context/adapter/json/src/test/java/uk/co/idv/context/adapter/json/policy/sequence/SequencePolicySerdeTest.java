package uk.co.idv.context.adapter.json.policy.sequence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.policy.sequence.SequencePolicy;
import uk.co.idv.context.entities.policy.sequence.SequencePolicyMother;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class SequencePolicySerdeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new SequencePolicyModule());
    private static final SequencePolicy POLICY = SequencePolicyMother.build();
    private static final String JSON = SequencePolicyJsonMother.build();

    @Test
    void shouldSerialize() throws JsonProcessingException {
        String json = MAPPER.writeValueAsString(POLICY);

        assertThatJson(json).isEqualTo(JSON);
    }

    @Test
    void shouldDeserialize() throws JsonProcessingException {
        SequencePolicy policy = MAPPER.readValue(JSON, SequencePolicy.class);

        assertThat(policy).isEqualTo(POLICY);
    }

}
