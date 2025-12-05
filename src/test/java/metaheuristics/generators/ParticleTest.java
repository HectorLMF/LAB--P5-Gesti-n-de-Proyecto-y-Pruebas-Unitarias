package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class ParticleTest {

    @BeforeEach
    public void setUp() {
        // ensure a fresh strategy and deterministic PSO static settings
        Strategy.setProblem(new Problem());
        Strategy.getStrategy().setCountMax(10);
        ParticleSwarmOptimization.wmax = 0.9;
        ParticleSwarmOptimization.wmin = 0.2;
        ParticleSwarmOptimization.countCurrentIterPSO = 0;
        ParticleSwarmOptimization.learning1 = 0;
        ParticleSwarmOptimization.learning2 = 0;
        ParticleSwarmOptimization.binary = false;
        ParticleSwarmOptimization.countParticle = 0;
        ParticleSwarmOptimization.countParticleBySwarm = 1;
        ParticleSwarmOptimization.coutSwarm = 1;
    }

    @Test
    public void testConstructorsAndAccessors() {
        Particle p = new Particle();
        assertNotNull(p.getStateActual());
        assertNotNull(p.getStatePBest());
        assertNotNull(p.getVelocity());
        assertEquals(0, p.getVelocity().size());

        ArrayList<Object> vel = new ArrayList<>();
        vel.add(1.0);
        State s1 = new State();
        State s2 = new State();
        p.setVelocity(vel);
        p.setStateActual(s1);
        p.setStatePBest(s2);
        assertSame(vel, p.getVelocity());
        assertSame(s1, p.getStateActual());
        assertSame(s2, p.getStatePBest());
    }

    @Test
    public void testUpdateCodeContinuousViaReflection() throws Exception {
        Particle p = new Particle();
        State st = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(1.0);
        code.add(2.0);
        st.setCode(code);
        p.setStateActual(st);

        ArrayList<Object> actualVelocity = new ArrayList<>();
        actualVelocity.add(0.5);
        actualVelocity.add(-1.0);

        Method m = Particle.class.getDeclaredMethod("UpdateCode", ArrayList.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<Object> result = (ArrayList<Object>) m.invoke(p, actualVelocity);

        assertEquals(2, result.size());
        assertEquals(1.5, (Double) result.get(0), 1e-9);
        assertEquals(1.0, (Double) result.get(1), 1e-9);
    }

    @Test
    public void testUpdateVelocityPredictable() throws Exception {
        Particle p = new Particle();
        State st = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(0.0);
        code.add(0.0);
        st.setCode(code);
        p.setStateActual(st);

        ArrayList<Object> vel = new ArrayList<>();
        vel.add(1.0);
        vel.add(1.0);
        p.setVelocity(vel);

        // ensure Strategy countMax used by inertia calculation
        Strategy.getStrategy().setCountMax(10);
        ParticleSwarmOptimization.countCurrentIterPSO = 0;
        ParticleSwarmOptimization.learning1 = 0; // force cognitive/social to 0
        ParticleSwarmOptimization.learning2 = 0;

        Method m = Particle.class.getDeclaredMethod("UpdateVelocity");
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<Object> actual = (ArrayList<Object>) m.invoke(p);

        assertEquals(2, actual.size());
        double expected = ParticleSwarmOptimization.wmax; // 0.9 in our setup
        assertEquals(expected, (Double) actual.get(0), 1e-9);
        assertEquals(expected, (Double) actual.get(1), 1e-9);
    }

    @Test
    public void testUpdateReferenceMaxAndMin() throws Exception {
        Particle p = new Particle();

        // prepare stateActual and statePBest for maximization
        State actual = new State();
        ArrayList<Object> codeA = new ArrayList<>(); codeA.add(1.0);
        actual.setCode(codeA);
        ArrayList<Double> evalA = new ArrayList<>(); evalA.add(5.0);
        actual.setEvaluation(evalA);

        State pbest = new State();
        ArrayList<Object> codeB = new ArrayList<>(); codeB.add(0.0);
        pbest.setCode(codeB);
        ArrayList<Double> evalB = new ArrayList<>(); evalB.add(1.0);
        pbest.setEvaluation(evalB);

        p.setStateActual(actual);
        p.setStatePBest(pbest);

        Problem prob = new Problem();
        prob.setTypeProblem(ProblemType.MAXIMIZAR);
        Strategy.setProblem(prob);

        // when maximizing, updateReference compares stateActual vs pBest
        p.updateReference(null, 0);
        // pBest should be replaced by actual
        assertEquals(5.0, p.getStatePBest().getEvaluation().get(0), 1e-9);

        // now test minimization branch: candidate should replace pBest if lower
        State candidate = new State();
        ArrayList<Object> codeC = new ArrayList<>(); codeC.add(2.0);
        candidate.setCode(codeC);
        ArrayList<Double> evalC = new ArrayList<>(); evalC.add(0.1);
        candidate.setEvaluation(evalC);

        // set pBest to something higher to be replaced by candidate
        ArrayList<Double> evalHigh = new ArrayList<>(); evalHigh.add(10.0);
        p.setStatePBest(new State());
        p.getStatePBest().setCode(new ArrayList<Object>());
        p.getStatePBest().setEvaluation(evalHigh);

        prob.setTypeProblem(ProblemType.MINIMIZAR);
        Strategy.setProblem(prob);

        p.updateReference(candidate, 0);
        assertEquals(0.1, p.getStatePBest().getEvaluation().get(0), 1e-9);
    }
}
