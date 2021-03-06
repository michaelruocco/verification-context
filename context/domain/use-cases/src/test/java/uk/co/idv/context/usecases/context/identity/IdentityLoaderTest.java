package uk.co.idv.context.usecases.context.identity;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.idv.context.entities.context.create.ContextCreateEligibilityRequest;
import uk.co.idv.context.entities.context.create.CreateContextRequest;
import uk.co.idv.context.entities.context.create.FacadeCreateContextRequestMother;
import uk.co.idv.context.entities.context.create.ServiceCreateContextRequest;
import uk.co.idv.identity.entities.eligibility.IdentityEligibility;
import uk.co.idv.identity.entities.eligibility.IdentityEligibilityMother;
import uk.co.idv.identity.entities.identity.FindIdentityRequest;
import uk.co.idv.context.entities.policy.ContextPolicy;
import uk.co.idv.identity.usecases.eligibility.CreateEligibility;
import uk.co.idv.context.usecases.policy.ContextPolicyService;
import uk.co.idv.policy.entities.policy.PolicyRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class IdentityLoaderTest {

    private final ContextPolicyService policyService = mock(ContextPolicyService.class);
    private final CreateEligibility createEligibility = mock(CreateEligibility.class);
    private final PolicyRequestFactory policyRequestFactory = mock(PolicyRequestFactory.class);

    private final IdentityLoader loader = IdentityLoader.builder()
            .policyService(policyService)
            .createEligibility(createEligibility)
            .policyRequestFactory(policyRequestFactory)
            .build();

    @Test
    void shouldPassOriginalRequestWhenCreatingEligibility() {
        CreateContextRequest initialRequest = FacadeCreateContextRequestMother.build();
        givenEligibilityReturned();

        loader.addIdentity(initialRequest);

        ArgumentCaptor<ContextCreateEligibilityRequest> captor = ArgumentCaptor.forClass(ContextCreateEligibilityRequest.class);
        verify(createEligibility).create(captor.capture());
        ContextCreateEligibilityRequest eligibilityRequest = captor.getValue();
        assertThat(eligibilityRequest.getRequest()).isEqualTo(initialRequest);
    }

    @Test
    void shouldPassPolicyWhenCreatingEligibility() {
        CreateContextRequest initialRequest = FacadeCreateContextRequestMother.build();
        PolicyRequest policyRequest = givenConvertedToPolicyRequest(initialRequest);
        ContextPolicy policy = givenPolicyLoadedForRequest(policyRequest);
        givenEligibilityReturned();

        loader.addIdentity(initialRequest);

        ArgumentCaptor<ContextCreateEligibilityRequest> captor = ArgumentCaptor.forClass(ContextCreateEligibilityRequest.class);
        verify(createEligibility).create(captor.capture());
        ContextCreateEligibilityRequest eligibilityRequest = captor.getValue();
        assertThat(eligibilityRequest.getPolicy()).isEqualTo(policy);
    }

    @Test
    void shouldReturnInitialRequest() {
        CreateContextRequest initialRequest = FacadeCreateContextRequestMother.build();
        givenEligibilityReturned();

        ServiceCreateContextRequest identityRequest = loader.addIdentity(initialRequest);

        assertThat(identityRequest.getInitial()).isEqualTo(initialRequest);
    }

    @Test
    void shouldReturnPolicy() {
        CreateContextRequest request = FacadeCreateContextRequestMother.build();
        PolicyRequest policyRequest = givenConvertedToPolicyRequest(request);
        ContextPolicy policy = givenPolicyLoadedForRequest(policyRequest);
        givenEligibilityReturned();

        ServiceCreateContextRequest identityRequest = loader.addIdentity(request);

        assertThat(identityRequest.getPolicy()).isEqualTo(policy);
    }

    @Test
    void shouldReturnIdentityFromEligibility() {
        CreateContextRequest request = FacadeCreateContextRequestMother.build();
        IdentityEligibility eligibility = givenEligibilityReturned();

        ServiceCreateContextRequest identityRequest = loader.addIdentity(request);

        assertThat(identityRequest.getIdentity()).isEqualTo(eligibility.getIdentity());
    }

    private PolicyRequest givenConvertedToPolicyRequest(CreateContextRequest request) {
        PolicyRequest policyRequest = mock(PolicyRequest.class);
        given(policyRequestFactory.toPolicyRequest(request)).willReturn(policyRequest);
        return policyRequest;
    }

    private ContextPolicy givenPolicyLoadedForRequest(PolicyRequest request) {
        ContextPolicy policy = mock(ContextPolicy.class);
        given(policyService.loadHighestPriority(request)).willReturn(policy);
        return policy;
    }

    private IdentityEligibility givenEligibilityReturned() {
        IdentityEligibility eligibility = IdentityEligibilityMother.build();
        given(createEligibility.create(any(FindIdentityRequest.class))).willReturn(eligibility);
        return eligibility;
    }

}
