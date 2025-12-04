package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import problem.definition.State;

/**
 * Tests exhaustivos para la clase AcceptAnyone
 */
@DisplayName("Tests para AcceptAnyone")
class AcceptAnyoneTest {

    private AcceptAnyone acceptAnyone;
    private State currentState;
    private State candidateState;

    @BeforeEach
    void setUp() {
        acceptAnyone = new AcceptAnyone();
        currentState = createState(10.0);
        candidateState = createState(5.0);
    }

    private State createState(double evaluation) {
        State state = new State();
        ArrayList<Double> evals = new ArrayList<>();
        evals.add(evaluation);
        state.setEvaluation(evals);
        return state;
    }

    @Test
    @DisplayName("Debe aceptar cualquier candidato mejor")
    void testAcceptBetterCandidate() {
        State betterCandidate = createState(15.0);
        Boolean result = acceptAnyone.acceptCandidate(currentState, betterCandidate);
        assertTrue(result, "AcceptAnyone debe aceptar candidatos mejores");
    }

    @Test
    @DisplayName("Debe aceptar cualquier candidato peor")
    void testAcceptWorseCandidate() {
        State worseCandidate = createState(5.0);
        Boolean result = acceptAnyone.acceptCandidate(currentState, worseCandidate);
        assertTrue(result, "AcceptAnyone debe aceptar candidatos peores");
    }

    @Test
    @DisplayName("Debe aceptar candidato igual")
    void testAcceptEqualCandidate() {
        State equalCandidate = createState(10.0);
        Boolean result = acceptAnyone.acceptCandidate(currentState, equalCandidate);
        assertTrue(result, "AcceptAnyone debe aceptar candidatos iguales");
    }

    @Test
    @DisplayName("Debe aceptar candidato con evaluación cero")
    void testAcceptZeroEvaluation() {
        State zeroCandidate = createState(0.0);
        Boolean result = acceptAnyone.acceptCandidate(currentState, zeroCandidate);
        assertTrue(result, "AcceptAnyone debe aceptar candidatos con evaluación cero");
    }

    @Test
    @DisplayName("Debe aceptar candidato con evaluación negativa")
    void testAcceptNegativeEvaluation() {
        State negativeCandidate = createState(-100.0);
        Boolean result = acceptAnyone.acceptCandidate(currentState, negativeCandidate);
        assertTrue(result, "AcceptAnyone debe aceptar candidatos con evaluación negativa");
    }

    @Test
    @DisplayName("Debe aceptar candidato con evaluación muy alta")
    void testAcceptVeryHighEvaluation() {
        State highCandidate = createState(1000000.0);
        Boolean result = acceptAnyone.acceptCandidate(currentState, highCandidate);
        assertTrue(result, "AcceptAnyone debe aceptar candidatos con evaluación muy alta");
    }

    @Test
    @DisplayName("Debe aceptar candidato con estado actual null evaluation")
    void testAcceptWithNullCurrentEvaluation() {
        State nullCurrentState = new State();
        State candidate = createState(10.0);
        Boolean result = acceptAnyone.acceptCandidate(nullCurrentState, candidate);
        assertTrue(result, "AcceptAnyone debe aceptar incluso con estado actual null");
    }

    @Test
    @DisplayName("Debe aceptar candidato con estado candidato null evaluation")
    void testAcceptWithNullCandidateEvaluation() {
        State current = createState(10.0);
        State nullCandidateState = new State();
        Boolean result = acceptAnyone.acceptCandidate(current, nullCandidateState);
        assertTrue(result, "AcceptAnyone debe aceptar incluso con candidato null");
    }

    @Test
    @DisplayName("Debe siempre retornar true")
    void testAlwaysReturnsTrue() {
        for (int i = -100; i <= 100; i += 10) {
            State candidate = createState(i);
            Boolean result = acceptAnyone.acceptCandidate(currentState, candidate);
            assertTrue(result, "AcceptAnyone debe siempre retornar true para evaluación " + i);
        }
    }

    @Test
    @DisplayName("Debe ser instancia de AcceptableCandidate")
    void testInstanceOfAcceptableCandidate() {
        assertTrue(acceptAnyone instanceof AcceptableCandidate, 
            "AcceptAnyone debe ser instancia de AcceptableCandidate");
    }

    @Test
    @DisplayName("Múltiples llamadas consecutivas deben retornar true")
    void testMultipleConsecutiveCalls() {
        State candidate1 = createState(5.0);
        State candidate2 = createState(15.0);
        State candidate3 = createState(10.0);

        assertTrue(acceptAnyone.acceptCandidate(currentState, candidate1));
        assertTrue(acceptAnyone.acceptCandidate(currentState, candidate2));
        assertTrue(acceptAnyone.acceptCandidate(currentState, candidate3));
    }

    @Test
    @DisplayName("Debe aceptar con evaluaciones Double.MAX_VALUE")
    void testAcceptMaxDoubleValue() {
        State maxCandidate = createState(Double.MAX_VALUE);
        Boolean result = acceptAnyone.acceptCandidate(currentState, maxCandidate);
        assertTrue(result, "Debe aceptar Double.MAX_VALUE");
    }

    @Test
    @DisplayName("Debe aceptar con evaluaciones Double.MIN_VALUE")
    void testAcceptMinDoubleValue() {
        State minCandidate = createState(Double.MIN_VALUE);
        Boolean result = acceptAnyone.acceptCandidate(currentState, minCandidate);
        assertTrue(result, "Debe aceptar Double.MIN_VALUE");
    }

    @Test
    @DisplayName("Debe aceptar con evaluaciones infinitas positivas")
    void testAcceptPositiveInfinity() {
        State infCandidate = createState(Double.POSITIVE_INFINITY);
        Boolean result = acceptAnyone.acceptCandidate(currentState, infCandidate);
        assertTrue(result, "Debe aceptar Double.POSITIVE_INFINITY");
    }

    @Test
    @DisplayName("Debe aceptar con evaluaciones infinitas negativas")
    void testAcceptNegativeInfinity() {
        State infCandidate = createState(Double.NEGATIVE_INFINITY);
        Boolean result = acceptAnyone.acceptCandidate(currentState, infCandidate);
        assertTrue(result, "Debe aceptar Double.NEGATIVE_INFINITY");
    }
}
