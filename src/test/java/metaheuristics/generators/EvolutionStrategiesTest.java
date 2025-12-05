package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class EvolutionStrategiesTest {

    @BeforeEach
    public void setUp() {
        // ensure fresh strategy & a minimal Problem so methods using Strategy won't NPE
        Strategy.destroyExecute();
        Strategy.setProblem(new Problem());
        Strategy.getStrategy().setCountMax(10);

        // install a minimal stub generator to avoid code paths that reference Strategy.generator
        Strategy.getStrategy().generator = new Generator() {
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) { }
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) { }
            @Override public GeneratorType getType() { return GeneratorType.EVOLUTION_STRATEGIES; }
            @Override public java.util.List<State> getReferenceList() { return new ArrayList<>(); }
            @Override public java.util.List<State> getSonList() { return new ArrayList<>(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) { }
            @Override public float getWeight() { return 0; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[0]; }
            @Override public int[] getListCountGender() { return new int[0]; }
        };
    }

    @Test
    public void testConstructorAndType() {
        EvolutionStrategies es = new EvolutionStrategies();
        assertEquals(GeneratorType.EVOLUTION_STRATEGIES, es.getType(), "Type should be EVOLUTION_STRATEGIES");
        assertNotNull(es.getListStateReference(), "List of references should not be null after construction");
    }

    @Test
    public void testSetAndGetListStateReference() {
        EvolutionStrategies es = new EvolutionStrategies();
        State s = new State();
        s.setNumber(42);
        List<State> list = new ArrayList<>();
        list.add(s);
        es.setListStateReference(list);
        assertEquals(1, es.getListStateReference().size());
        assertSame(list, es.getListStateReference());
        assertEquals(42, es.getListStateReference().get(0).getNumber());
    }

    @Test
    public void testGenerateReturnsCandidateWhenReferenceAvailable() throws Exception {
        EvolutionStrategies es = new EvolutionStrategies();
        State father = new State();
        father.setNumber(7);
        ArrayList<Object> code = new ArrayList<>(); code.add("A");
        father.setCode(code);
        ArrayList<Double> eval = new ArrayList<>(); eval.add(1.23);
        father.setEvaluation(eval);
        father.setTypeGenerator(GeneratorType.EVOLUTION_STRATEGIES);

        List<State> list = new ArrayList<>();
        list.add(father);
        es.setListStateReference(list);

        // With a single father, generate should return a (possibly mutated) candidate, not null
        State candidate = es.generate(0);
        assertNotNull(candidate, "generate should return a candidate when references exist");
        assertNotNull(candidate.getCode(), "candidate code should not be null");
        assertEquals(father.getNumber(), candidate.getNumber());
    }

}
