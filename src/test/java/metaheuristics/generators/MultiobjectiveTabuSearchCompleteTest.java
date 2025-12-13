package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import local_search.complement.TabuSolutions;
import metaheurictics.strategy.Strategy;
import problem.definition.Codification;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;

class MultiobjectiveTabuSearchCompleteTest {

    private MultiobjectiveTabuSearch generator;

    @BeforeEach
    void setUp() {
        Strategy.destroyExecute();

        TabuSolutions.listTabu.clear();
        TabuSolutions.maxelements = 3;

        Problem problem = new Problem();
        problem.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        problem.setPossibleValue(10);
        problem.setCodification(new Codification() {
            @Override
            public boolean validState(State state) {
                return state != null && state.getCode() != null && state.getCode().size() == 3;
            }

            @Override
            public Object getVariableAleatoryValue(int key) {
                return 0;
            }

            @Override
            public int getAleatoryKey() {
                return 0;
            }

            @Override
            public int getVariableCount() {
                return 3;
            }
        });

        problem.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                return Arrays.asList(createStateWithCode(1, 2, 3), createStateWithCode(9, 8, 7));
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                return Arrays.asList(createStateWithCode(4, 5, 6));
            }
        });

        Strategy.setProblem(problem);
        generator = new MultiobjectiveTabuSearch();
        Strategy.getStrategy().generator = generator;
    }

    @Test
    @DisplayName("get/set StateReferenceTS")
    void testGetSetStateReferenceTS() {
        State ref = createStateWithCode(1, 1, 1);
        generator.setStateReferenceTS(ref);
        assertSame(ref, generator.getStateReferenceTS());
    }

    @Test
    @DisplayName("get/set TypeGenerator")
    void testGetSetTypeGenerator() {
        generator.setTypeGenerator(GeneratorType.MULTIOBJECTIVE_TABU_SEARCH);
        assertEquals(GeneratorType.MULTIOBJECTIVE_TABU_SEARCH, generator.getTypeGenerator());
    }

    @Test
    @DisplayName("generate devuelve un candidato del vecindario")
    void testGenerateReturnsCandidate() throws Exception {
        State ref = createStateWithCode(0, 0, 0);
        generator.setInitialReference(ref);

        State candidate = generator.generate(1);
        assertNotNull(candidate);
        assertNotNull(candidate.getCode());
        assertEquals(3, candidate.getCode().size());
    }

    @Test
    @DisplayName("updateReference cuando acepta: actualiza referencia y añade a lista tabu si cabe")
    void testUpdateReferenceAcceptAddsToTabuWhenNotFull() throws Exception {
        State ref = createStateWithCode(0, 0, 0);
        State cand = createStateWithCode(1, 2, 3);
        ref.setEvaluation(createEvaluation(1.0, 1.0));
        cand.setEvaluation(createEvaluation(0.0, 0.0));
        generator.setInitialReference(ref);

        // Forzar accept=true usando AcceptType ya configurado; cand sin evaluation no debería impedir comparator/equality.
        generator.updateReference(cand, 1);

        assertSame(cand, generator.getReference());
        assertTrue(TabuSolutions.listTabu.contains(cand));
        assertEquals(1, TabuSolutions.listTabu.size());
    }

    @Test
    @DisplayName("updateReference con lista tabu llena: hace FIFO y añade si no existe")
    void testUpdateReferenceAcceptWhenTabuFullRemovesFirst() throws Exception {
        State ref = createStateWithCode(0, 0, 0);
        ref.setEvaluation(createEvaluation(1.0, 1.0));
        generator.setInitialReference(ref);

        State old1 = createStateWithCode(9, 9, 9);
        State old2 = createStateWithCode(8, 8, 8);
        State old3 = createStateWithCode(7, 7, 7);
        old1.setEvaluation(createEvaluation(0.0, 0.0));
        old2.setEvaluation(createEvaluation(0.0, 0.0));
        old3.setEvaluation(createEvaluation(0.0, 0.0));
        TabuSolutions.listTabu.add(old1);
        TabuSolutions.listTabu.add(old2);
        TabuSolutions.listTabu.add(old3);
        TabuSolutions.maxelements = 3;

        State cand = createStateWithCode(1, 2, 3);
        cand.setEvaluation(createEvaluation(0.0, 0.0));
        generator.updateReference(cand, 1);

        assertEquals(3, TabuSolutions.listTabu.size());
        assertFalse(TabuSolutions.listTabu.contains(old1));
        assertTrue(TabuSolutions.listTabu.contains(cand));
    }

    @Test
    @DisplayName("updateReference no duplica candidato si ya está en lista tabu")
    void testUpdateReferenceDoesNotDuplicate() throws Exception {
        State ref = createStateWithCode(0, 0, 0);
        ref.setEvaluation(createEvaluation(1.0, 1.0));
        generator.setInitialReference(ref);

        State cand = createStateWithCode(1, 2, 3);
        cand.setEvaluation(createEvaluation(0.0, 0.0));
        TabuSolutions.listTabu.add(cand);

        int before = TabuSolutions.listTabu.size();
        generator.updateReference(cand, 1);
        int after = TabuSolutions.listTabu.size();

        assertEquals(before, after);
    }

    @Test
    @DisplayName("getSonList retorna null")
    void testGetSonListReturnsNull() {
        assertNull(generator.getSonList());
    }

    @Test
    @DisplayName("awardUpdateREF retorna false")
    void testAwardUpdateREFReturnsFalse() {
        assertFalse(generator.awardUpdateREF(createStateWithCode(1, 2, 3)));
    }

    @Test
    @DisplayName("getListCountBetterGender retorna null")
    void testGetListCountBetterGenderReturnsNull() {
        assertNull(generator.getListCountBetterGender());
    }

    @Test
    @DisplayName("getListCountGender retorna null")
    void testGetListCountGenderReturnsNull() {
        assertNull(generator.getListCountGender());
    }

    @Test
    @DisplayName("getTrace retorna null")
    void testGetTraceReturnsNull() {
        assertNull(generator.getTrace());
    }

    private static State createStateWithCode(Object a, Object b, Object c) {
        State s = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(a);
        code.add(b);
        code.add(c);
        s.setCode(code);
        return s;
    }

    private static ArrayList<Double> createEvaluation(double... values) {
        ArrayList<Double> evaluation = new ArrayList<>();
        for (double value : values) {
            evaluation.add(value);
        }
        return evaluation;
    }
}
