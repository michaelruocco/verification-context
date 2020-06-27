package uk.co.idv.context.entities.channel;

import java.util.Optional;
import java.util.UUID;

public interface Rsa extends Channel {

    Optional<UUID> getIssuerSessionId();

    Optional<UUID> getDsSessionId();

}