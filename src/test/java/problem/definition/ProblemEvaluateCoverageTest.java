package problem.definition;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ProblemEvaluateCoverageTest {
    @Test
    void evaluateSetsStateEvaluationWhenNoSolutionMethod() throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Problem p = new Problem();
        ArrayList<ObjetiveFunction> fun = new ArrayList<>();
        fun.add(new EvalObjetiveFunctionStub());
        p.setFunction(fun);

        State s = new State(new ArrayList<>());
        p.setState(s);

        // No typeSolutionMethod set -> direct evaluation path
        p.Evaluate(s);

        assertNotNull(s.getEvaluation());
        assertEquals(1, s.getEvaluation().size());
        assertEquals(10.0, s.getEvaluation().get(0));
    }
}

class EvalObjetiveFunctionStub extends ObjetiveFunction {
    @Override
    public Double Evaluation(State state) {
        return 10.0; // deterministic value to assert
    }
}
