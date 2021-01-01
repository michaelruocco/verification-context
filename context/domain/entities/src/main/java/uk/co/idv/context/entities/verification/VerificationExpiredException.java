package uk.co.idv.context.entities.verification;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

//TODO test
@Getter
public class VerificationExpiredException extends RuntimeException {

    private final UUID id;
    private final Instant expiry;

    public VerificationExpiredException(UUID id, Instant expiry) {
        super(toMessage(id, expiry));
        this.id = id;
        this.expiry = expiry;
    }

    private static String toMessage(UUID id, Instant expiry) {
        return String.format("verification %s expired at %s", id, expiry);
    }

}
