package uk.co.idv.policy.adapter.json.error;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static uk.co.idv.policy.adapter.json.error.PolicyNotFoundErrorJsonMother.policyNotFoundErrorJson;
import static uk.co.idv.policy.adapter.json.error.PolicyNotFoundErrorMother.policyNotFoundError;

public class PolicyApiErrorArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                policyNotFoundArguments()
        );
    }

    private static Arguments policyNotFoundArguments() {
        return Arguments.of(
                policyNotFoundErrorJson(),
                policyNotFoundError()
        );
    }

}
