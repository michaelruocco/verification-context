package uk.co.idv.context.entities.context.sequence;

import org.junit.jupiter.api.Test;
import uk.co.idv.context.entities.context.sequence.stage.StagesRequest;
import uk.co.idv.context.entities.policy.sequence.SequencePolicies;
import uk.co.idv.context.entities.policy.sequence.SequencePolicy;
import uk.co.idv.context.entities.policy.sequence.SequencePolicyMother;
import uk.co.idv.identity.entities.identity.Identity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SequencesRequestTest {

    @Test
    void shouldReturnContextId() {
        UUID id = UUID.randomUUID();

        SequencesRequest request = SequencesRequest.builder()
                .contextId(id)
                .build();

        assertThat(request.getContextId()).isEqualTo(id);
    }

    @Test
    void shouldReturnIdentity() {
        Identity identity = mock(Identity.class);

        SequencesRequest request = SequencesRequest.builder()
                .identity(identity)
                .build();

        assertThat(request.getIdentity()).isEqualTo(identity);
    }

    @Test
    void shouldReturnSequencePolicies() {
        SequencePolicies policies = mock(SequencePolicies.class);

        SequencesRequest request = SequencesRequest.builder()
                .policies(policies)
                .build();

        assertThat(request.getPolicies()).isEqualTo(policies);
    }

    @Test
    void shouldPopulateContextIdOnStagesRequest() {
        UUID id = UUID.randomUUID();
        SequencesRequest sequencesRequest = SequencesRequest.builder()
                .contextId(id)
                .build();
        SequencePolicy policy = SequencePolicyMother.build();

        StagesRequest stagesRequest = sequencesRequest.toStagesRequest(policy);

        assertThat(stagesRequest.getContextId()).isEqualTo(id);
    }

    @Test
    void shouldPopulateIdentityOnStagesRequest() {
        Identity identity = mock(Identity.class);
        SequencesRequest sequencesRequest = SequencesRequest.builder()
                .identity(identity)
                .build();
        SequencePolicy policy = SequencePolicyMother.build();

        StagesRequest stagesRequest = sequencesRequest.toStagesRequest(policy);

        assertThat(stagesRequest.getIdentity()).isEqualTo(identity);
    }

    @Test
    void shouldPopulateStagePoliciesOnStagesRequest() {
        Identity identity = mock(Identity.class);
        SequencesRequest sequencesRequest = SequencesRequest.builder()
                .identity(identity)
                .build();
        SequencePolicy policy = SequencePolicyMother.build();

        StagesRequest stagesRequest = sequencesRequest.toStagesRequest(policy);

        assertThat(stagesRequest.getPolicies()).isEqualTo(policy.getStagePolicies());
    }

}
