package problem.definition;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class OperatorCoverageTest {

    @Test
    void constructorExists() {
        TestOperatorStub op = new TestOperatorStub();
        assertNotNull(op);
    }
}

class TestOperatorStub extends Operator {
    @Override
    public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
        return new ArrayList<>();
    }

    @Override
    public List<State> generateRandomState(Integer operatornumber) {
        return new ArrayList<>();
    }
}
