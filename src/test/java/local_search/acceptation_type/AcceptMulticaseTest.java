package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.MultiCaseSimulatedAnnealing;
import metaheuristics.generators.RandomSearch;
import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

/**
 * Tests unitarios deterministas para `AcceptMulticase`.
 *
 * Estos tests siguen el mismo patrón que los tests de `Particle`: configuran
 * el `Strategy` singleton, inicializan la temperatura estática usada por
 * `MultiCaseSimulatedAnnealing` y ejercitan caminos deterministas donde
 * `pAccept` se fija a 1 para evitar aleatoriedad (por ejemplo, ramas donde
 * la solución candidata domina o el contador de dominancias > 0).
 */
public class AcceptMulticaseTest {

    @BeforeEach
    public void setup() {
        // configurar un problema sencillo (maximizar) en la estrategia singleton
        Problem problem = new Problem();
        problem.setTypeProblem(Problem.ProblemType.MAXIMIZAR);
        Strategy.setProblem(problem);
        // limpiar la lista del frente de Pareto antes de cada test
        Strategy.getStrategy().listRefPoblacFinal = new ArrayList<State>();
        // asegurar que Strategy tenga un generador no nulo (evita NPE en Dominance)
        Strategy.getStrategy().generator = new RandomSearch();
        // Temperatura inicial determinista para las fórmulas de probabilidad
        MultiCaseSimulatedAnnealing.tinitial = 1.0;
    }

    private State makeState(double[] evals, String id) {
        State s = new State();
        ArrayList<Double> ev = new ArrayList<Double>();
        for (double d : evals)
            ev.add(d);
        s.setEvaluation(ev);
        ArrayList<Object> code = new ArrayList<Object>();
        code.add(id);
        s.setCode(code);
        return s;
    }

    @Test
    public void candidateDominatesCurrent_isAccepted() {
        State current = makeState(new double[] {1.0, 1.0}, "cur");
        State candidate = makeState(new double[] {2.0, 2.0}, "cand");

        AcceptMulticase acceptor = new AcceptMulticase();

        // Cuando el candidato domina al actual, pAccept se fuerza a 1 y debe aceptarse
        boolean accepted = acceptor.acceptCandidate(current, candidate);
        assertTrue(accepted, "Candidate that dominates current should be accepted");
    }

    @Test
    public void candidateDominatesSomeInList_isAccepted() {
        // Preparar lista con una solución que el candidato dominará
        State listElem = makeState(new double[] {0.5, 0.5}, "listElem");
        Strategy.getStrategy().listRefPoblacFinal.add(listElem.getCopy());

        State current = makeState(new double[] {1.0, 1.0}, "cur2");
        State candidate = makeState(new double[] {1.5, 1.5}, "cand2");

        AcceptMulticase acceptor = new AcceptMulticase();
        boolean accepted = acceptor.acceptCandidate(current, candidate);
        assertTrue(accepted, "Candidate that dominates an element of the list should be accepted");
    }

    @Test
    public void candidateRankZero_notDominated_isAccepted() {
        // lista vacía -> acceptor añadirá current; candidate no domina y no es dominado => rank 0
        State current = makeState(new double[] {1.0, 2.0}, "cur3");
        State candidate = makeState(new double[] {1.1, 1.9}, "cand3");

        AcceptMulticase acceptor = new AcceptMulticase();
        boolean accepted = acceptor.acceptCandidate(current, candidate);
        assertTrue(accepted, "Candidate with rank 0 (no one dominates it) should be accepted in this deterministic path");
    }

}
