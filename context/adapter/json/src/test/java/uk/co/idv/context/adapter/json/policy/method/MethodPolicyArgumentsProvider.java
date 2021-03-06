package uk.co.idv.context.adapter.json.policy.method;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import uk.co.idv.method.adapter.json.fake.policy.FakeMethodPolicyJsonMother;
import uk.co.idv.method.entities.method.fake.policy.FakeMethodPolicyMother;

import java.util.stream.Stream;

public class MethodPolicyArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(Arguments.of(FakeMethodPolicyJsonMother.build(), FakeMethodPolicyMother.build()));
    }

}
