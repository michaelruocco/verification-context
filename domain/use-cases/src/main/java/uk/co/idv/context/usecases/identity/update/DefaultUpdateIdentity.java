package uk.co.idv.context.usecases.identity.update;

import lombok.Builder;
import uk.co.idv.context.entities.identity.Identity;
import uk.co.idv.context.usecases.identity.IdentityRepository;
import uk.co.idv.context.usecases.identity.create.CreateIdentity;
import uk.co.idv.context.usecases.identity.merge.MergeIdentities;

import java.util.Collection;

@Builder
public class DefaultUpdateIdentity implements UpdateIdentity {

    private final CreateIdentity create;
    private final MergeIdentities merge;
    private final IdentityRepository repository;

    public static UpdateIdentity build(IdentityRepository repository) {
        return DefaultUpdateIdentity.builder()
                .create(CreateIdentity.build(repository))
                .merge(new MergeIdentities())
                .repository(repository)
                .build();
    }

    @Override
    public Identity update(UpdateIdentityRequest request) {
        return checkAgainstExistingIdentities(request.getIdentity());
    }

    private Identity checkAgainstExistingIdentities(Identity identity) {
        Collection<Identity> existingIdentities = repository.load(identity.getAliases());
        return handle(identity, existingIdentities);
    }

    private Identity handle(Identity identity, Collection<Identity> existingIdentities) {
        switch (existingIdentities.size()) {
            case 0:
                return create.create(identity);
            case 1:
                return save(identity);
            default:
                return merge.merge(identity, existingIdentities);
        }
    }

    private Identity save(Identity identity) {
        repository.save(identity);
        return identity;
    }
}