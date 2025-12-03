package problem.operators;

import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.Operator;
import problem.definition.State;

public class MutationOperator extends Operator {

    @Override
    public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
        List<State> listNeigborhood = new ArrayList<>();
        for (int i = 0; i < operatornumber; i++) {
            int key = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
            Object candidate = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue(key);
            State state = stateCurrent.getCopy();
            state.getCode().set(key, candidate);
            listNeigborhood.add(state);
        }
        return listNeigborhood;
    }

    @Override
    public List<State> generateRandomState(Integer operatornumber) {
        return null;
    }
}
