package problem.definition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import metaheuristics.generators.GeneratorType;

@DisplayName("Tests para State")
class StateTest {

    private State state;
    private ArrayList<Object> code;
    private ArrayList<Double> evaluation;

    @BeforeEach
    void setUp() {
        code = new ArrayList<>();
        code.add(1);
        code.add(2);
        code.add(3);
        
        evaluation = new ArrayList<>();
        evaluation.add(10.5);
        evaluation.add(20.3);
        
        state = new State(code);
        state.setTypeGenerator(GeneratorType.EvolutionStrategies);
        state.setEvaluation(evaluation);
        state.setNumber(42);
    }

    @Test
    @DisplayName("Constructor vacío debe crear un State con código vacío")
    void testEmptyConstructor() {
        State emptyState = new State();
        assertNotNull(emptyState.getCode());
        assertTrue(emptyState.getCode().isEmpty());
    }

    @Test
    @DisplayName("Constructor con código debe inicializar correctamente")
    void testConstructorWithCode() {
        State newState = new State(code);
        assertEquals(code, newState.getCode());
        assertEquals(3, newState.getCode().size());
    }

    @Test
    @DisplayName("Constructor con código null debe crear lista vacía")
    void testConstructorWithNullCode() {
        State newState = new State((ArrayList<Object>) null);
        assertNotNull(newState.getCode());
        assertTrue(newState.getCode().isEmpty());
    }

    @Test
    @DisplayName("Constructor de copia debe copiar todos los atributos")
    void testCopyConstructor() {
        // Create a new state using the copy constructor pattern
        State copiedState = state.clone();
        assertNotSame(state, copiedState);
        assertEquals(state.getTypeGenerator(), copiedState.getTypeGenerator());
        assertEquals(state.getEvaluation(), copiedState.getEvaluation());
        assertEquals(state.getNumber(), copiedState.getNumber());
        assertEquals(state.getCode(), copiedState.getCode());
    }

    @Test
    @DisplayName("getCode() debe retornar una copia del código")
    void testGetCode() {
        ArrayList<Object> retrievedCode = state.getCode();
        assertEquals(code, retrievedCode);
        // Verificar que es una copia y no la referencia original
        retrievedCode.add(4);
        assertNotEquals(retrievedCode, state.getCode());
    }

    @Test
    @DisplayName("setCode() con null debe crear lista vacía")
    void testSetCodeWithNull() {
        state.setCode(null);
        assertNotNull(state.getCode());
        assertTrue(state.getCode().isEmpty());
    }

    @Test
    @DisplayName("setCode() debe establecer el código correctamente")
    void testSetCode() {
        ArrayList<Object> newCode = new ArrayList<>();
        newCode.add("A");
        newCode.add("B");
        state.setCode(newCode);
        assertEquals(newCode, state.getCode());
    }

    @Test
    @DisplayName("getTypeGenerator() debe retornar el tipo correcto")
    void testGetTypeGenerator() {
        assertEquals(GeneratorType.EvolutionStrategies, state.getTypeGenerator());
    }

    @Test
    @DisplayName("setTypeGenerator() debe establecer el tipo correctamente")
    void testSetTypeGenerator() {
        state.setTypeGenerator(GeneratorType.GeneticAlgorithm);
        assertEquals(GeneratorType.GeneticAlgorithm, state.getTypeGenerator());
    }

    @Test
    @DisplayName("getEvaluation() debe retornar una copia de la evaluación")
    void testGetEvaluation() {
        ArrayList<Double> retrievedEval = state.getEvaluation();
        assertEquals(evaluation, retrievedEval);
        // Verificar que es una copia
        retrievedEval.add(30.0);
        assertNotEquals(retrievedEval, state.getEvaluation());
    }

    @Test
    @DisplayName("setEvaluation() con null debe establecer null")
    void testSetEvaluationWithNull() {
        state.setEvaluation(null);
        assertNull(state.getEvaluation());
    }

    @Test
    @DisplayName("setEvaluation() debe establecer la evaluación correctamente")
    void testSetEvaluation() {
        ArrayList<Double> newEval = new ArrayList<>();
        newEval.add(100.0);
        state.setEvaluation(newEval);
        assertEquals(newEval, state.getEvaluation());
    }

    @Test
    @DisplayName("getNumber() debe retornar el número correcto")
    void testGetNumber() {
        assertEquals(42, state.getNumber());
    }

    @Test
    @DisplayName("setNumber() debe establecer el número correctamente")
    void testSetNumber() {
        state.setNumber(100);
        assertEquals(100, state.getNumber());
    }

    @Test
    @DisplayName("clone() debe crear una copia exacta")
    void testClone() {
        State clonedState = state.clone();
        assertNotSame(state, clonedState);
        assertEquals(state.getTypeGenerator(), clonedState.getTypeGenerator());
        assertEquals(state.getNumber(), clonedState.getNumber());
        assertEquals(state.getCode(), clonedState.getCode());
        assertEquals(state.getEvaluation(), clonedState.getEvaluation());
    }

    @Test
    @DisplayName("clone() con código null debe crear lista vacía")
    void testCloneWithNullCode() {
        State emptyState = new State();
        State clonedState = emptyState.clone();
        assertNotNull(clonedState.getCode());
        assertTrue(clonedState.getCode().isEmpty());
    }

    @Test
    @DisplayName("clone() con evaluación null debe mantener null")
    void testCloneWithNullEvaluation() {
        State stateWithNullEval = new State();
        stateWithNullEval.setEvaluation(null);
        State clonedState = stateWithNullEval.clone();
        assertNull(clonedState.getEvaluation());
    }

    @Test
    @DisplayName("getCopy() debe retornar un nuevo State con el mismo código")
    void testGetCopy() {
        Object copy = state.getCopy();
        assertInstanceOf(State.class, copy);
        State copiedState = (State) copy;
        assertEquals(state.getCode(), copiedState.getCode());
    }

    @Test
    @DisplayName("Comparator() debe retornar true para estados con el mismo código")
    void testComparatorWithEqualCode() {
        State otherState = new State(code);
        assertTrue(state.Comparator(otherState));
    }

    @Test
    @DisplayName("Comparator() debe retornar false para estados con código diferente")
    void testComparatorWithDifferentCode() {
        ArrayList<Object> differentCode = new ArrayList<>();
        differentCode.add(5);
        differentCode.add(6);
        State otherState = new State(differentCode);
        assertFalse(state.Comparator(otherState));
    }

    @Test
    @DisplayName("Distance() debe calcular correctamente la distancia entre estados")
    void testDistance() {
        ArrayList<Object> similarCode = new ArrayList<>();
        similarCode.add(1);  // igual
        similarCode.add(5);  // diferente
        similarCode.add(3);  // igual
        State otherState = new State(similarCode);
        
        double distance = state.Distance(otherState);
        assertEquals(1.0, distance, "La distancia debe ser 1 (un elemento diferente)");
    }

    @Test
    @DisplayName("Distance() debe retornar 0 para estados idénticos")
    void testDistanceWithIdenticalStates() {
        State identicalState = new State(code);
        double distance = state.Distance(identicalState);
        assertEquals(0.0, distance, "La distancia debe ser 0 para estados idénticos");
    }

    @Test
    @DisplayName("Distance() debe contar todos los elementos diferentes")
    void testDistanceWithCompletelyDifferentStates() {
        ArrayList<Object> differentCode = new ArrayList<>();
        differentCode.add(10);
        differentCode.add(20);
        differentCode.add(30);
        State otherState = new State(differentCode);
        
        double distance = state.Distance(otherState);
        assertEquals(3.0, distance, "La distancia debe ser 3 (todos los elementos son diferentes)");
    }
}
