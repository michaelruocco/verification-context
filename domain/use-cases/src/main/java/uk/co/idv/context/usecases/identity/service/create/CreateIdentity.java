package uk.co.idv.context.usecases.identity.service.create;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.idv.context.entities.identity.Identity;
import uk.co.idv.context.usecases.identity.IdentityRepository;

@Builder
@Slf4j
public class CreateIdentity {

    @Builder.Default
    private final RandomIdvIdGenerator idGenerator = new RandomIdvIdGenerator();
    private final IdentityRepository repository;

    public static CreateIdentity build(IdentityRepository repository) {
        return CreateIdentity.builder()
                .repository(repository)
                .build();
    }

    public Identity create(Identity identity) {
        Identity identityWithId = allocateIdvIdIfRequired(identity);
        repository.save(identityWithId);
        return identityWithId;
    }

    private Identity allocateIdvIdIfRequired(Identity identity) {
        if (!identity.hasIdvId()) {
            return allocateIdvId(identity);
        }
        log.warn("create called for identity idvId {}", identity);
        return identity;
    }

    private Identity allocateIdvId(Identity identity) {
        Identity identityWithId = identity.setIdvId(idGenerator.generate());
        log.info("generated idvId for identity {}", identityWithId);
        return identityWithId;
    }

}
