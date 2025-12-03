package problem.extension;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import problem.definition.State;

public class MetricasMultiobjetivoTest {

    private State stateEval(double... vals) {
        State s = new State();
        ArrayList<Double> eval = new ArrayList<>();
        for (double v : vals) eval.add(v);
        s.setEvaluation(eval);
        return s;
    }

    @Test
    public void tasaErrorCountsNonMembers() throws Exception {
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        List<State> current = Arrays.asList(stateEval(1, 2), stateEval(3, 4));
        List<State> truth = Arrays.asList(stateEval(1, 2));
        double result = m.TasaError(current, truth);
        assertEquals(0.5, result);
    }

    @Test
    public void distanciaGeneracionalNonNegative() throws Exception {
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        List<State> current = Arrays.asList(stateEval(1, 2), stateEval(3, 4));
        List<State> truth = Arrays.asList(stateEval(1, 2), stateEval(3, 4));
        double result = m.DistanciaGeneracional(current, truth);
        assertTrue(result >= 0.0);
    }

    @Test
    public void dispersionSingleIsZero() throws Exception {
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        ArrayList<State> solutions = new ArrayList<>();
        solutions.add(stateEval(1, 2));
        double result = m.Dispersion(solutions);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void calcularMinMaxMediaWorks() {
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        ArrayList<Double> metrics = new ArrayList<>(Arrays.asList(1.0, 4.0, 2.0));
        assertEquals(1.0, m.CalcularMin(metrics));
        assertEquals(4.0, m.CalcularMax(metrics));
        assertEquals(7.0/3.0, m.CalcularMedia(metrics));
    }
}
