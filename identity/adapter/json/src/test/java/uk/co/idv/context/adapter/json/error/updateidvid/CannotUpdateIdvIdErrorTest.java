package uk.co.idv.context.adapter.json.error.updateidvid;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.adapter.json.error.ApiError;
import uk.co.idv.context.entities.alias.IdvId;
import uk.co.idv.context.entities.alias.IdvIdMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class CannotUpdateIdvIdErrorTest {

    private final IdvId updated = IdvIdMother.idvId();
    private final IdvId existing = IdvIdMother.idvId1();

    private final ApiError error = CannotUpdateIdvIdError.builder()
            .updated(updated)
            .existing(existing)
            .build();

    @Test
    void shouldReturnStatus() {
        assertThat(error.getStatus()).isEqualTo(422);
    }

    @Test
    void shouldReturnTitle() {
        assertThat(error.getTitle()).isEqualTo("Cannot update idv id");
    }

    @Test
    void shouldReturnMessage() {
        String expectedMessage = String.format(
                "attempted to update existing value %s to %s",
                existing.getValue(),
                updated.getValue()
        );

        assertThat(error.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void shouldReturnMeta() {
        assertThat(error.getMeta()).contains(
                entry("new", updated.getValue()),
                entry("existing", existing.getValue())
        );
    }

}
