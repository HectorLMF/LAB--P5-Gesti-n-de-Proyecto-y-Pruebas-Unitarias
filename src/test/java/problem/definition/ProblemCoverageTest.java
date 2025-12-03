package problem.definition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class ProblemCoverageTest {

    @Test
    void gettersAndSettersWork() {
        Problem p = new Problem();

        // function
        ArrayList<ObjetiveFunction> fun = new ArrayList<>();
        fun.add(new ProblemTestObjetiveFunctionStub());
        p.setFunction(fun);
        assertSame(fun, p.getFunction());

        // state
        State s = new State(new ArrayList<>());
        p.setState(s);
        assertSame(s, p.getState());

        // type problem
        Problem.ProblemType type = Problem.ProblemType.MAXIMIZAR;
        p.setTypeProblem(type);
        assertEquals(type, p.getTypeProblem());

        // codification
        Codification c = new TestCodificationStub();
        p.setCodification(c);
        assertSame(c, p.getCodification());

        // operator
        Operator op = new ProblemTestOperatorStub();
        p.setOperator(op);
        assertSame(op, p.getOperator());

        // possible value
        p.setPossibleValue(42);
        assertEquals(42, p.getPossibleValue());
    }
}

class ProblemTestObjetiveFunctionStub extends ObjetiveFunction {
    @Override
    public Double Evaluation(State state) {
        return 0.0;
    }
}

class ProblemTestOperatorStub extends Operator {
    @Override
    public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
        return new ArrayList<>();
    }

    @Override
    public List<State> generateRandomState(Integer operatornumber) {
        return new ArrayList<>();
    }
}

class TestCodificationStub extends Codification {
    @Override
    public boolean validState(State state) {
        return true;
    }

    @Override
    public Object getVariableAleatoryValue(int key) {
        return 1;
    }

    @Override
    public int getAleatoryKey() {
        return 0;
    }

    @Override
    public int getVariableCount() {
        return 1;
    }
}
