package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import local_search.candidate_type.CandidateType;
import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class MultiobjectiveTabuSearchTest {

    @BeforeEach
    public void setUp() {
        // Fresh strategy and a minimal problem context
        Strategy.destroyExecute();
        Strategy.setProblem(new Problem());
        // Provide a minimal stub generator to avoid NPEs in Dominance/acceptance flows
        Strategy.getStrategy().generator = new Generator() {
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) { }
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) { }
            @Override public GeneratorType getType() { return GeneratorType.MULTIOBJECTIVE_TABU_SEARCH; }
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
        MultiobjectiveTabuSearch ts = new MultiobjectiveTabuSearch();
        assertEquals(GeneratorType.MULTIOBJECTIVE_TABU_SEARCH, ts.getType());
    }

    @Test
    public void testInitialReferenceAndReferenceList() {
        MultiobjectiveTabuSearch ts = new MultiobjectiveTabuSearch();
        State ref = new State();
        ArrayList<Double> eval = new ArrayList<>(); eval.add(1.0);
        ref.setEvaluation(eval);
        ref.setCode(new ArrayList<>());

        ts.setInitialReference(ref);
        assertSame(ref, ts.getReference(), "getReference should return set ref");

        List<State> refs = ts.getReferenceList();
        assertNotNull(refs);
        assertTrue(refs.size() >= 1);
        assertSame(ref, refs.get(refs.size() - 1), "MultiobjectiveTabuSearch stores the same ref instance in list");
    }

    @Test
    public void testCandidateTypeAndWeightAccessors() {
        MultiobjectiveTabuSearch ts = new MultiobjectiveTabuSearch();
        ts.setTypeCandidate(CandidateType.RANDOM_CANDIDATE);
        ts.setWeight(73.5f);
        assertEquals(73.5f, ts.getWeight(), 1e-6);
    }
    
    @Test
    @DisplayName("La clase MultiobjectiveTabuSearch existe")
    void testClassExists() {
        assertEquals("MultiobjectiveTabuSearch", MultiobjectiveTabuSearch.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase MultiobjectiveTabuSearch es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(MultiobjectiveTabuSearch.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase MultiobjectiveTabuSearch extiende Generator")
    void testExtendsGenerator() {
        assertTrue(Generator.class.isAssignableFrom(MultiobjectiveTabuSearch.class));
    }

    @Test
    @DisplayName("La clase tiene el campo candidatevalue")
    void testCandidatevalueFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("candidatevalue");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo candidatevalue no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo typeAcceptation")
    void testTypeAcceptationFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("typeAcceptation");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo typeAcceptation no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo stateReferenceTS")
    void testStateReferenceTSFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("stateReferenceTS");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo stateReferenceTS no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo typeGenerator")
    void testTypeGeneratorFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("typeGenerator");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo typeGenerator no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo strategy")
    void testStrategyFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("strategy");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo strategy no existe");
        }
    }
}
