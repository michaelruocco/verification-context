package uk.co.idv.context.adapter.json.emailaddress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.emailaddress.EmailAddresses;
import uk.co.idv.context.entities.emailaddress.EmailAddressesMother;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class EmailAddressesSerdeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new EmailAddressModule());
    private static final String JSON = EmailAddressesJsonMother.two();
    private static final EmailAddresses EMAIL_ADDRESSES = EmailAddressesMother.two();

    @Test
    void shouldSerialize() throws JsonProcessingException {
        String json = MAPPER.writeValueAsString(EMAIL_ADDRESSES);

        assertThatJson(json).isEqualTo(JSON);
    }

    @Test
    void shouldDeserialize() throws JsonProcessingException {
        EmailAddresses emailAddresses = MAPPER.readValue(JSON, EmailAddresses.class);

        assertThat(emailAddresses).isEqualTo(EMAIL_ADDRESSES);
    }

}
