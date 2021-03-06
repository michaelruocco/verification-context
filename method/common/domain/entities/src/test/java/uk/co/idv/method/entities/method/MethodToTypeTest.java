package uk.co.idv.method.entities.method;

import org.junit.jupiter.api.Test;
import uk.co.idv.method.entities.method.fake.FakeMethod;
import uk.co.idv.method.entities.method.fake.FakeMethodMother;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MethodToTypeTest {

    private final MethodToType<FakeMethod> methodToType = new MethodToType<>(FakeMethod.class);

    @Test
    void shouldReturnEmptyIfMethodIsNotAssignableFromType() {
        Method method = mock(Method.class);

        Optional<FakeMethod> fakeMethod = methodToType.apply(method);

        assertThat(fakeMethod).isEmpty();
    }

    @Test
    void shouldReturnMethodIfAssignableFromType() {
        FakeMethod method = FakeMethodMother.build();

        Optional<FakeMethod> fakeMethod = methodToType.apply(method);

        assertThat(fakeMethod).contains(method);
    }

}
