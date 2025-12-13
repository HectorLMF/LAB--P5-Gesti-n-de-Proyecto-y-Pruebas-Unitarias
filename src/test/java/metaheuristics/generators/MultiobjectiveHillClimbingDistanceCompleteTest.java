package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;

public class MultiobjectiveHillClimbingDistanceCompleteTest {

	private static State newStateWithCodeAndEval(List<Object> code, double eval) {
		State s = new State();
		s.setCode(new ArrayList<>(code));
		ArrayList<Double> e = new ArrayList<>();
		e.add(eval);
		s.setEvaluation(e);
		return s;
	}

	@BeforeEach
	void setUp() {
		Strategy.destroyExecute();

		Problem p = new Problem();
		p.setTypeProblem(Problem.ProblemType.MINIMIZAR);

		ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
		funcs.add(new ObjetiveFunction() {
			@Override
			public Double Evaluation(State state) {
				ArrayList<Object> code = state.getCode();
				double sum = 0.0;
				for (Object o : code) {
					if (o instanceof Number) {
						sum += ((Number) o).doubleValue();
					}
				}
				return sum;
			}
		});
		p.setFunction(funcs);

		Strategy.setProblem(p);
		Strategy.getStrategy().listRefPoblacFinal = new ArrayList<>();
		MultiobjectiveHillClimbingDistance.distanceSolution.clear();
	}

	@Test
	@DisplayName("generate() returns a candidate from neighborhood")
	void generate_returnsCandidate() throws Exception {
		State ref = newStateWithCodeAndEval(List.of(0), 0.0);
		State n1 = newStateWithCodeAndEval(List.of(9), 9.0);
		State n2 = newStateWithCodeAndEval(List.of(1), 1.0);

		Strategy.getStrategy().getProblem().setOperator(new Operator() {
			@Override
			public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
				return new ArrayList<>(List.of(n1, n2));
			}

			@Override
			public List<State> generateRandomState(Integer operatornumber) {
				return new ArrayList<>(List.of(new State()));
			}
		});

		MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();
		gen.setInitialReference(ref);

		State candidate = gen.generate(0);
		assertNotNull(candidate);
		assertTrue(candidate.Comparator(n1) || candidate.Comparator(n2));
	}

	@Test
	@DisplayName("updateReference(): accept==true branch resets visited and updates reference")
	void updateReference_acceptTrue() throws Exception {
		MultiobjectiveHillClimbingDistance.sizeNeighbors = 1;
		MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();

		State ref = newStateWithCodeAndEval(List.of(10), 10.0);
		gen.setInitialReference(ref);
		Strategy.getStrategy().generator = gen;

		Strategy.getStrategy().getProblem().setOperator(new Operator() {
			@Override
			public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
				return new ArrayList<>(List.of(newStateWithCodeAndEval(List.of(99), 99.0)));
			}

			@Override
			public List<State> generateRandomState(Integer operatornumber) {
				return new ArrayList<>(List.of(newStateWithCodeAndEval(List.of(7), 7.0)));
			}
		});

		State cand = newStateWithCodeAndEval(List.of(1), 1.0);
		gen.updateReference(cand, 0);
		assertEquals(1.0, gen.getReference().getEvaluation().get(0));
		assertTrue(Strategy.getStrategy().listRefPoblacFinal.size() >= 1);
		assertTrue(gen.getReferenceList().size() >= 2);
	}

	@Test
	@DisplayName("updateReference(): accept==false then neighborhood chooses most distant state")
	void updateReference_acceptFalse_neighborhoodPath() throws Exception {
		MultiobjectiveHillClimbingDistance.sizeNeighbors = 2;
		MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();

		State s0 = newStateWithCodeAndEval(List.of(0), 0.0);
		State sA = newStateWithCodeAndEval(List.of(1), 1.0);
		State sB = newStateWithCodeAndEval(List.of(2), 2.0);
		gen.setInitialReference(s0);
		Strategy.getStrategy().generator = gen;

		Strategy.getStrategy().listRefPoblacFinal.add(sA.getCopy());
		Strategy.getStrategy().listRefPoblacFinal.add(sB.getCopy());
		MultiobjectiveHillClimbingDistance.distanceSolution.clear();
		MultiobjectiveHillClimbingDistance.distanceSolution.add(1.0);
		MultiobjectiveHillClimbingDistance.distanceSolution.add(10.0);

		State neighbor = newStateWithCodeAndEval(List.of(123), 123.0);
		Strategy.getStrategy().getProblem().setOperator(new Operator() {
			@Override
			public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
				return new ArrayList<>(List.of(neighbor));
			}

			@Override
			public List<State> generateRandomState(Integer operatornumber) {
				return new ArrayList<>(List.of(newStateWithCodeAndEval(List.of(5), 5.0)));
			}
		});

		State worse = newStateWithCodeAndEval(List.of(100), 100.0);
		gen.updateReference(worse, 0);
		assertTrue(gen.getReference().Comparator(sB));
	}

	@Test
	@DisplayName("updateReference(): accept==false then restart loop evaluates random state")
	void updateReference_acceptFalse_restartPath() throws Exception {
		MultiobjectiveHillClimbingDistance.sizeNeighbors = 2;
		MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();
		State ref = newStateWithCodeAndEval(List.of(10), 10.0);
		gen.setInitialReference(ref);
		Strategy.getStrategy().generator = gen;

		Strategy.getStrategy().getProblem().setOperator(new Operator() {
			@Override
			public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
				return new ArrayList<>();
			}

			@Override
			public List<State> generateRandomState(Integer operatornumber) {
				return new ArrayList<>(List.of(newStateWithCodeAndEval(List.of(0), 0.0)));
			}
		});

		State cand = newStateWithCodeAndEval(List.of(100), 100.0);
		gen.updateReference(cand, 0);
		assertEquals(0.0, gen.getReference().getEvaluation().get(0));
	}

	@Test
	@DisplayName("private helpers: Contain() and SolutionMoreDistance() branches")
	void privateHelpers_branches() throws Exception {
		MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();
		gen.setInitialReference(newStateWithCodeAndEval(List.of(0), 0.0));
		Strategy.getStrategy().generator = gen;
		MultiobjectiveHillClimbingDistance.sizeNeighbors = 1;
		Strategy.getStrategy().getProblem().setOperator(new Operator() {
			@Override
			public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
				return new ArrayList<>(List.of(newStateWithCodeAndEval(List.of(42), 42.0)));
			}
			@Override
			public List<State> generateRandomState(Integer operatornumber) {
				return new ArrayList<>(List.of(newStateWithCodeAndEval(List.of(1), 1.0)));
			}
		});

		Method contain = MultiobjectiveHillClimbingDistance.class.getDeclaredMethod("Contain", State.class);
		contain.setAccessible(true);
		Method solMore = MultiobjectiveHillClimbingDistance.class.getDeclaredMethod("SolutionMoreDistance", List.class, List.class);
		solMore.setAccessible(true);

		State a = newStateWithCodeAndEval(List.of(1), 1.0);
		assertEquals(false, (boolean) contain.invoke(gen, a));
		gen.updateReference(newStateWithCodeAndEval(List.of(2), 2.0), 0);
		assertEquals(true, (boolean) contain.invoke(gen, gen.getReference()));

		State s1 = newStateWithCodeAndEval(List.of(1, 0), 1.0);
		State s2 = newStateWithCodeAndEval(List.of(0, 1), 1.0);
		State picked = (State) solMore.invoke(gen, List.of(s1, s2), List.of(0.1, 0.9));
		assertTrue(picked.Comparator(s2));

		assertNull(solMore.invoke(gen, List.of(), List.of()));
	}

	@Test
	@DisplayName("stubs: methods keep returning null/false/0")
	void stubs_contract() {
		MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();
		assertNull(gen.getSonList());
		assertFalse(gen.awardUpdateREF(new State()));
		assertEquals(0.0f, gen.getWeight());
		gen.setWeight(123.0f);
		assertNull(gen.getListCountBetterGender());
		assertNull(gen.getListCountGender());
		assertNull(gen.getTrace());
	}
}
