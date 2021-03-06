package uk.co.idv.common.adapter.json.error.badrequest;

import org.junit.jupiter.api.Test;
import uk.co.idv.common.adapter.json.error.ApiError;

import static org.assertj.core.api.Assertions.assertThat;

class BadRequestErrorTest {

    private static final String MESSAGE = "my-message";

    private final ApiError error = new BadRequestError(MESSAGE);

    @Test
    void shouldReturnStatus() {
        assertThat(error.getStatus()).isEqualTo(400);
    }

    @Test
    void shouldReturnTitle() {
        assertThat(error.getTitle()).isEqualTo("Bad request");
    }

    @Test
    void shouldReturnMessage() {
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void shouldReturnMeta() {
        assertThat(error.getMeta()).isEmpty();
    }

}
