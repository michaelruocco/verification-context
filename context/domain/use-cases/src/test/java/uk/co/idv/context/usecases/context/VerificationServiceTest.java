package uk.co.idv.context.usecases.context;

import org.junit.jupiter.api.Test;
import uk.co.idv.common.usecases.id.IdGenerator;
import uk.co.idv.context.entities.context.Context;
import uk.co.idv.context.entities.context.ContextMother;
import uk.co.idv.context.entities.verification.Verification;
import uk.co.idv.context.entities.verification.CreateVerificationRequest;
import uk.co.idv.context.entities.verification.CreateVerificationRequestMother;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class VerificationServiceTest {

    private static final Instant NOW = Instant.now();

    private final ContextService contextService = mock(ContextService.class);
    private final IdGenerator idGenerator = mock(IdGenerator.class);
    private final Clock clock = Clock.fixed(NOW, ZoneId.systemDefault());

    private final VerificationService verificationService = VerificationService.builder()
            .contextService(contextService)
            .idGenerator(idGenerator)
            .clock(clock)
            .build();

    @Test
    void shouldGenerateIdOnCreatedVerification() {
        CreateVerificationRequest request = CreateVerificationRequestMother.build();
        UUID id = givenRandomIdGenerated();
        givenContextFoundForId(request.getContextId());

        Verification verification = verificationService.create(request);

        assertThat(verification.getId()).isEqualTo(id);
    }

    @Test
    void shouldSetCreatedOnVerification() {
        CreateVerificationRequest request = CreateVerificationRequestMother.build();
        givenContextFoundForId(request.getContextId());

        Verification verification = verificationService.create(request);

        assertThat(verification.getCreated()).isEqualTo(NOW);
    }

    @Test
    void shouldPopulateContextIdOnVerification() {
        CreateVerificationRequest request = CreateVerificationRequestMother.build();
        Context context = givenContextFoundForId(request.getContextId());

        Verification verification = verificationService.create(request);

        assertThat(verification.getContextId()).isEqualTo(context.getId());
    }

    @Test
    void shouldPopulateActivityOnVerification() {
        CreateVerificationRequest request = CreateVerificationRequestMother.build();
        Context context = givenContextFoundForId(request.getContextId());

        Verification verification = verificationService.create(request);

        assertThat(verification.getActivity()).isEqualTo(context.getActivity());
    }

    @Test
    void shouldPopulateProtectSensitiveDataOnVerification() {
        CreateVerificationRequest request = CreateVerificationRequestMother.build();
        Context context = givenContextFoundForId(request.getContextId());

        Verification verification = verificationService.create(request);

        assertThat(verification.isProtectSensitiveData()).isEqualTo(context.isProtectSensitiveData());
    }

    @Test
    void shouldPopulateMethodsOnVerification() {
        CreateVerificationRequest request = CreateVerificationRequestMother.build();
        Context context = givenContextFoundForId(request.getContextId());

        Verification verification = verificationService.create(request);

        assertThat(verification.getMethods()).isEqualTo(context.getNextMethods(request.getMethodName()));
    }

    @Test
    void shouldNotPopulateCompletedTimeOnVerification() {
        CreateVerificationRequest request = CreateVerificationRequestMother.build();
        givenContextFoundForId(request.getContextId());

        Verification verification = verificationService.create(request);

        assertThat(verification.getCompleted()).isEmpty();
    }

    private UUID givenRandomIdGenerated() {
        UUID id = UUID.randomUUID();
        given(idGenerator.generate()).willReturn(id);
        return id;
    }

    private Context givenContextFoundForId(UUID contextId) {
        Context context = ContextMother.build();
        given(contextService.findWithEligibleIncompleteSequences(contextId)).willReturn(context);
        return context;
    }

}
