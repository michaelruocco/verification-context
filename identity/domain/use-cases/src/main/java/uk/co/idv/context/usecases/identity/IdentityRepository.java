package uk.co.idv.context.usecases.identity;

import uk.co.idv.context.entities.alias.Alias;
import uk.co.idv.context.entities.alias.Aliases;
import uk.co.idv.context.entities.identity.Identities;
import uk.co.idv.context.entities.identity.Identity;

import java.util.Optional;

public interface IdentityRepository {

    Optional<Identity> load(Alias alias);

    Identities load(Aliases aliases);

    void save(Identity identity);

    void delete(Aliases aliases);

}