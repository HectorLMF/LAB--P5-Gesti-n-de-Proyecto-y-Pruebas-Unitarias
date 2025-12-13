package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Codification;
import problem.definition.ObjetiveFunction;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;

class MultiobjectiveStochasticHillClimbingCompleteTest {

    private MultiobjectiveStochasticHillClimbing generator;

    @BeforeEach
    void setUp() {
        Strategy.destroyExecute();

        Problem problem = new Problem();
        problem.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        problem.setPossibleValue(10);

        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                // Deterministic and cheap objective: minimize sum of integer code values.
                double sum = 0.0;
                if (state != null && state.getCode() != null) {
                    for (Object v : state.getCode()) {
                        if (v instanceof Number) {
                            sum += ((Number) v).doubleValue();
                        }
                    }
                }
                return sum;
            }
        });
        problem.setFunction(functions);

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
                // Two neighbors to exercise CandidateValue selection.
                return Arrays.asList(createStateWithCode(1, 2, 3), createStateWithCode(9, 8, 7));
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                return Arrays.asList(createStateWithCode(4, 5, 6));
            }
        });

        Strategy.setProblem(problem);
        generator = new MultiobjectiveStochasticHillClimbing();
        Strategy.getStrategy().generator = generator;

        State initialRef = createStateWithCode(0, 0, 0);
        initialRef.setEvaluation(createEvaluation(1.0, 1.0));
        generator.setInitialReference(initialRef);
    }

    @Test
    @DisplayName("generate devuelve un candidato del vecindario")
    void testGenerateReturnsCandidateFromNeighborhood() throws Exception {
        State candidate = generator.generate(1);
        assertNotNull(candidate);
        assertNotNull(candidate.getCode());
        assertEquals(3, candidate.getCode().size());
    }

    @Test
    @DisplayName("updateReference cuando acepta: referencia se copia del candidato y se añade a referenceList")
    void testUpdateReferenceAcceptUpdatesReferenceAndAddsToList() throws Exception {
        State candidate = createStateWithCode(1, 2, 3);
        candidate.setEvaluation(createEvaluation(0.0, 0.0));

        generator.updateReference(candidate, 1);

        State ref = generator.getReference();
        assertNotNull(ref);
        assertNotSame(candidate, ref, "Debe usar getCopy() al actualizar la referencia");
        assertEquals(candidate.getCode(), ref.getCode());

        List<State> refs = generator.getReferenceList();
        assertNotNull(refs);
        assertFalse(refs.isEmpty());
        State last = refs.get(refs.size() - 1);
        assertNotNull(last);
        assertEquals(ref.getCode(), last.getCode());
    }

    @Test
    @DisplayName("getReferenceList añade copias (no referencias directas)")
    void testGetReferenceListAddsCopy() {
        List<State> refs = generator.getReferenceList();
        assertNotNull(refs);
        assertEquals(1, refs.size());
        assertNotSame(generator.getReference(), refs.get(0));
    }

    @Test
    @DisplayName("getReference devuelve la referencia actual")
    void testGetReferenceReturnsCurrentRef() {
        State ref = generator.getReference();
        assertNotNull(ref);
        assertEquals(3, ref.getCode().size());
    }

    @Test
    @DisplayName("setStateRef establece la referencia")
    void testSetStateRefSetsReference() {
        State newRef = createStateWithCode(7, 7, 7);
        generator.setStateRef(newRef);
        assertSame(newRef, generator.getReference());
    }

    @Test
    @DisplayName("setInitialReference establece la referencia")
    void testSetInitialReferenceSetsReference() {
        State newRef = createStateWithCode(8, 8, 8);
        generator.setInitialReference(newRef);
        assertSame(newRef, generator.getReference());
    }

    @Test
    @DisplayName("getGeneratorType y getType retornan el tipo")
    void testGetTypeAndGetGeneratorType() {
        assertEquals(GeneratorType.MULTIOBJECTIVE_STOCHASTIC_HILL_CLIMBING, generator.getGeneratorType());
        assertEquals(GeneratorType.MULTIOBJECTIVE_STOCHASTIC_HILL_CLIMBING, generator.getType());
    }

    @Test
    @DisplayName("setGeneratorType cambia el tipo")
    void testSetGeneratorType() {
        generator.setGeneratorType(GeneratorType.RANDOM_SEARCH);
        assertEquals(GeneratorType.RANDOM_SEARCH, generator.getType());
        assertEquals(GeneratorType.RANDOM_SEARCH, generator.getGeneratorType());
    }

    @Test
    @DisplayName("metodos no implementados: getSonList null, awardUpdateREF false, getWeight 0, setWeight no rompe, getTrace null, contadores null")
    void testNotImplementedMethodsContract() {
        assertNull(generator.getSonList());
        assertFalse(generator.awardUpdateREF(createStateWithCode(1, 2, 3)));
        assertEquals(0.0f, generator.getWeight());
        generator.setWeight(123.0f);
        assertNull(generator.getTrace());
        assertNull(generator.getListCountBetterGender());
        assertNull(generator.getListCountGender());
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
