package uk.co.idv.context.usecases.policy;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.policy.DefaultPolicyRequest;
import uk.co.idv.context.entities.policy.MockPolicyMother;
import uk.co.idv.context.entities.policy.Policies;
import uk.co.idv.context.entities.policy.Policy;
import uk.co.idv.context.entities.policy.PolicyKey;
import uk.co.idv.context.entities.policy.PolicyRequest;
import uk.co.idv.context.usecases.policy.create.CreatePolicy;
import uk.co.idv.context.usecases.policy.load.LoadPolicy;
import uk.co.idv.context.usecases.policy.update.UpdatePolicy;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PolicyServiceTest {

    private final CreatePolicy<Policy> create = mock(CreatePolicy.class);
    private final UpdatePolicy<Policy> update = mock(UpdatePolicy.class);
    private final LoadPolicy<Policy> load = mock(LoadPolicy.class);

    private final PolicyService<Policy> facade = PolicyService.builder()
            .create(create)
            .update(update)
            .load(load)
            .build();

    @Test
    void shouldCreatePolicy() {
        Policy policy = MockPolicyMother.policy();

        facade.create(policy);

        verify(create).create(policy);
    }

    @Test
    void shouldUpdatePolicy() {
        Policy policy = MockPolicyMother.policy();

        facade.update(policy);

        verify(update).update(policy);
    }

    @Test
    void shouldLoadPolicyById() {
        UUID id = UUID.randomUUID();
        Policy expectedPolicy = MockPolicyMother.withId(id);
        given(load.load(id)).willReturn(expectedPolicy);

        Policy policy = facade.load(id);

        assertThat(policy).isEqualTo(expectedPolicy);
    }

    @Test
    void shouldLoadPoliciesByRequest() {
        PolicyRequest request = mock(DefaultPolicyRequest.class);
        Policies<Policy> expectedPolicies = mock(Policies.class);
        given(load.load(request)).willReturn(expectedPolicies);

        Policies<Policy> policies = facade.load(request);

        assertThat(policies).isEqualTo(expectedPolicies);
    }

    @Test
    void shouldLoadPoliciesByKey() {
        PolicyKey key = mock(PolicyKey.class);
        Policies<Policy> expectedPolicies = mock(Policies.class);
        given(load.load(key)).willReturn(expectedPolicies);

        Policies<Policy> policies = facade.load(key);

        assertThat(policies).isEqualTo(expectedPolicies);
    }

    @Test
    void shouldLoadAllPolicies() {
        Policies<Policy> expectedPolicies = mock(Policies.class);
        given(load.loadAll()).willReturn(expectedPolicies);

        Policies<Policy> policies = facade.loadAll();

        assertThat(policies).isEqualTo(expectedPolicies);
    }

}
