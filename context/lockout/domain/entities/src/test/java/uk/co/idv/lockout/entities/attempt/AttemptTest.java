package uk.co.idv.lockout.entities.attempt;

import org.junit.jupiter.api.Test;
import uk.co.idv.identity.entities.alias.AliasesMother;
import uk.co.idv.identity.entities.alias.DefaultAliases;
import uk.co.idv.identity.entities.alias.IdvId;
import uk.co.idv.identity.entities.alias.IdvIdMother;
import uk.co.idv.lockout.entities.attempt.Attempt.AttemptBuilder;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AttemptTest {

    private final AttemptBuilder builder = Attempt.builder();

    @Test
    void shouldReturnSuccessful() {
        boolean successful = true;

        Attempt attempt = builder.successful(successful).build();

        assertThat(attempt.isSuccessful()).isTrue();
    }

    @Test
    void shouldReturnContextId() {
        UUID contextId = UUID.randomUUID();

        Attempt attempt = builder.contextId(contextId).build();

        assertThat(attempt.getContextId()).isEqualTo(contextId);
    }

    @Test
    void shouldReturnChannelId() {
        String channelId = "channel-id";

        Attempt attempt = builder.channelId(channelId).build();

        assertThat(attempt.getChannelId()).isEqualTo(channelId);
    }

    @Test
    void shouldReturnActivityName() {
        String activityName = "activity-name";

        Attempt attempt = builder.activityName(activityName).build();

        assertThat(attempt.getActivityName()).isEqualTo(activityName);
    }

    @Test
    void shouldReturnAliases() {
        DefaultAliases aliases = AliasesMother.defaultAliasOnly();

        Attempt attempt = builder.aliases(aliases).build();

        assertThat(attempt.getAliases()).isEqualTo(aliases);
    }

    @Test
    void shouldReturnAliasTypes() {
        DefaultAliases aliases = AliasesMother.defaultAliasOnly();

        Attempt attempt = builder.aliases(aliases).build();

        assertThat(attempt.getAliasTypes()).isEqualTo(aliases.getTypes());
    }

    @Test
    void shouldReturnHasAliasTrueIfHasAtLeastOneAlias() {
        DefaultAliases aliases = AliasesMother.defaultAliasOnly();
        Attempt attempt = builder.aliases(aliases).build();

        boolean hasAlias = attempt.hasAtLeastOneAlias(aliases);

        assertThat(hasAlias).isTrue();
    }

    @Test
    void shouldReturnHasAliasFalseIfAliasDoesNotMatch() {
        DefaultAliases aliases = AliasesMother.defaultAliasOnly();
        Attempt attempt = builder.aliases(aliases).build();

        boolean hasAlias = attempt.hasAtLeastOneAlias(AliasesMother.idvIdOnly());

        assertThat(hasAlias).isFalse();
    }

    @Test
    void shouldReturnIdvId() {
        IdvId idvId = IdvIdMother.idvId();

        Attempt attempt = builder.idvId(idvId).build();

        assertThat(attempt.getIdvId()).isEqualTo(idvId);
    }

    @Test
    void shouldReturnMethodName() {
        String methodName = "method-name";

        Attempt attempt = builder.methodName(methodName).build();

        assertThat(attempt.getMethodName()).isEqualTo(methodName);
    }

    @Test
    void shouldReturnVerificationId() {
        UUID verificationId = UUID.randomUUID();

        Attempt attempt = builder.verificationId(verificationId).build();

        assertThat(attempt.getVerificationId()).isEqualTo(verificationId);
    }

    @Test
    void shouldReturnTimestamp() {
        Instant timestamp = Instant.now();

        Attempt attempt = builder.timestamp(timestamp).build();

        assertThat(attempt.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldReturnTrueIfOccurredAfterStartAndBeforeEnd() {
        Instant timestamp = Instant.now();
        Instant start = timestamp.minusMillis(1);
        Instant end = timestamp.plusMillis(1);
        Attempt attempt = builder.timestamp(timestamp).build();

        boolean between = attempt.occurredBetweenInclusive(start, end);

        assertThat(between).isTrue();
    }

    @Test
    void shouldReturnTrueIfOccurredAtStart() {
        Instant timestamp = Instant.now();
        Instant end = timestamp.plusMillis(1);
        Attempt attempt = builder.timestamp(timestamp).build();

        boolean between = attempt.occurredBetweenInclusive(timestamp, end);

        assertThat(between).isTrue();
    }

    @Test
    void shouldReturnTrueIfOccurredAtEnd() {
        Instant timestamp = Instant.now();
        Instant start = timestamp.minusMillis(1);
        Attempt attempt = builder.timestamp(timestamp).build();

        boolean between = attempt.occurredBetweenInclusive(start, timestamp);

        assertThat(between).isTrue();
    }

    @Test
    void shouldReturnFalseIfOccurredBeforeStart() {
        Instant timestamp = Instant.now();
        Instant start = timestamp.plusMillis(1);
        Instant end = timestamp.plusMillis(2);
        Attempt attempt = builder.timestamp(timestamp).build();

        boolean between = attempt.occurredBetweenInclusive(start, end);

        assertThat(between).isFalse();
    }

    @Test
    void shouldReturnFalseIfOccurredAfterEnd() {
        Instant timestamp = Instant.now();
        Instant start = timestamp.minusMillis(2);
        Instant end = timestamp.minusMillis(1);
        Attempt attempt = builder.timestamp(timestamp).build();

        boolean between = attempt.occurredBetweenInclusive(start, end);

        assertThat(between).isFalse();
    }

}
