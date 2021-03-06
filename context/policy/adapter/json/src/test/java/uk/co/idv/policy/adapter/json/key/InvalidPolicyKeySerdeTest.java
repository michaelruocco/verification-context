package uk.co.idv.policy.adapter.json.key;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.co.idv.policy.adapter.json.key.invalid.InvalidPolicyKeyJsonMother;
import uk.co.idv.policy.entities.policy.key.PolicyKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class InvalidPolicyKeySerdeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new PolicyKeyModule());
    private static final String JSON = InvalidPolicyKeyJsonMother.invalidKey();

    @Test
    void shouldDeserialize() {
        Throwable error = catchThrowable(() -> MAPPER.readValue(JSON, PolicyKey.class));

        assertThat(error)
                .isInstanceOf(InvalidPolicyKeyTypeException.class)
                .hasMessage("invalid");
    }

}
