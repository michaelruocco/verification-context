package uk.co.idv.policy.adapter.json.error.policynotfound;

import org.junit.jupiter.api.Test;
import uk.co.idv.common.adapter.json.error.ApiError;
import uk.co.idv.common.adapter.json.error.handler.ErrorHandler;
import uk.co.idv.policy.entities.policy.PolicyNotFoundException;
import uk.co.idv.policy.entities.policy.PolicyNotFoundExceptionMother;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PolicyNotFoundHandlerTest {

    private final ErrorHandler handler = new PolicyNotFoundHandler();

    @Test
    void shouldConvertPolicyNotFoundExceptionToError() {
        PolicyNotFoundException exception = PolicyNotFoundExceptionMother.build();

        Optional<ApiError> error = handler.apply(exception);

        assertThat(error).containsInstanceOf(PolicyNotFoundError.class);
    }

    @Test
    void shouldPopulateErrorMessageWithExceptionMessage() {
        PolicyNotFoundException exception = PolicyNotFoundExceptionMother.build();

        Optional<ApiError> error = handler.apply(exception);

        assertThat(error).get().hasFieldOrPropertyWithValue("message", exception.getMessage());
    }

    @Test
    void shouldNotSupportAnyOtherException() {
        Throwable other = new Throwable();

        Optional<ApiError> error = handler.apply(other);

        assertThat(error).isEmpty();
    }

}
