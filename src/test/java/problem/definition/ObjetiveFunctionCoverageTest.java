package problem.definition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ObjetiveFunctionCoverageTest {

    @Test
    void gettersAndSettersWork() {
            TestObjetiveFunctionStub of = new TestObjetiveFunctionStub();
        // weight
            of.setWeight(2.5f);
            assertEquals(2.5f, of.getWeight());

        // type problem
            of.setTypeProblem(Problem.ProblemType.Minimizar);
            assertEquals(Problem.ProblemType.Minimizar, of.getTypeProblem());
    }
}

class TestObjetiveFunctionStub extends ObjetiveFunction {
    @Override
    public Double Evaluation(State state) {
        return 0.0;
    }
}

