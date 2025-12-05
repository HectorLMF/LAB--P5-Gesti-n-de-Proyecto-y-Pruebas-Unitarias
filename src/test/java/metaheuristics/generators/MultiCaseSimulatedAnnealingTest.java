package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class MultiCaseSimulatedAnnealingTest {

    @BeforeEach
    public void setUp() {
        Strategy.destroyExecute();
        Problem problem = new Problem();
        problem.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        Strategy.setProblem(problem);
        // Stub generator to satisfy Dominance/accept flows inside AcceptMulticase
        Strategy.getStrategy().generator = new Generator() {
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) { }
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) { }
            @Override public GeneratorType getType() { return GeneratorType.MULTI_CASE_SIMULATED_ANNEALING; }
            @Override public List<State> getReferenceList() { return new ArrayList<>(); }
            @Override public List<State> getSonList() { return new ArrayList<>(); }
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
        MultiCaseSimulatedAnnealing sa = new MultiCaseSimulatedAnnealing();
        assertEquals(GeneratorType.MULTI_CASE_SIMULATED_ANNEALING, sa.getType());
    }

    @Test
    public void testSetStateRefAndReferenceListCopy() {
        MultiCaseSimulatedAnnealing sa = new MultiCaseSimulatedAnnealing();
        State ref = new State();
        ArrayList<Double> eval = new ArrayList<>(); eval.add(2.0); ref.setEvaluation(eval);
        ref.setCode(new ArrayList<>());
        sa.setInitialReference(ref);
        assertSame(ref, sa.getReference());
        List<State> refs = sa.getReferenceList();
        assertTrue(refs.size() >= 1);
        State last = refs.get(refs.size() - 1);
        assertNotSame(ref, last, "Reference list should store a copy");
        assertEquals(ref.getEvaluation().get(0), last.getEvaluation().get(0));
    }

    @Test
    public void testUpdateReferenceAdjustsTemperatureOnSchedule() throws Exception {
        MultiCaseSimulatedAnnealing.alpha = 0.5;
        MultiCaseSimulatedAnnealing.tinitial = 10.0;
        MultiCaseSimulatedAnnealing.countIterationsT = 3;

        MultiCaseSimulatedAnnealing sa = new MultiCaseSimulatedAnnealing();
        State ref = new State();
        ref.setCode(new ArrayList<>());
        ArrayList<Double> eval = new ArrayList<>(); eval.add(1.0); ref.setEvaluation(eval);
        sa.setInitialReference(ref);

        State candidate = ref.getCopy();
        candidate.setNumber(1);
        double before = MultiCaseSimulatedAnnealing.tinitial;
        sa.updateReference(candidate, 3);
        double after = MultiCaseSimulatedAnnealing.tinitial;
        assertEquals(before * 0.5, after, 1e-9);
    }

    @Test
    void testClassExists() {
        assertEquals("MultiCaseSimulatedAnnealing", 
            MultiCaseSimulatedAnnealing.class.getSimpleName());
    }

    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(
            MultiCaseSimulatedAnnealing.class.getModifiers()));
    }

    @Test
    void testAlphaExists() {
        try {
            MultiCaseSimulatedAnnealing.class.getDeclaredField("alpha");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("Campo 'alpha' no encontrado");
        }
    }

    @Test
    void testTypeGeneratorMethods() {
        try {
            MultiCaseSimulatedAnnealing.class.getMethod("getTypeGenerator");
            MultiCaseSimulatedAnnealing.class.getMethod("setTypeGenerator", GeneratorType.class);
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Métodos getTypeGenerator/setTypeGenerator no encontrados");
        }
    }

    @Test
    void testConstructorExists() {
        try {
            MultiCaseSimulatedAnnealing.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Constructor público no encontrado");
        }
    }
}
