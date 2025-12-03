package local_search.candidate_type;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import problem.definition.State;

public class SearchCandidateTest {

    private State stateWithEval(double value) {
        State s = new State();
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(value);
        s.setEvaluation(eval);
        return s;
    }

    @Test
    public void greaterCandidatePicksMax() throws Exception {
        GreaterCandidate gc = new GreaterCandidate();
        List<State> neighborhood = new ArrayList<>();
        neighborhood.add(stateWithEval(1.0));
        neighborhood.add(stateWithEval(2.5));
        neighborhood.add(stateWithEval(0.3));

        State picked = gc.stateSearch(neighborhood);
        assertNotNull(picked);
        assertEquals(2.5, picked.getEvaluation().get(0));
    }

    @Test
    public void greaterCandidateSingleReturnsSame() throws Exception {
        GreaterCandidate gc = new GreaterCandidate();
        List<State> neighborhood = new ArrayList<>();
        State only = stateWithEval(7.7);
        neighborhood.add(only);
        State picked = gc.stateSearch(neighborhood);
        assertSame(only, picked);
    }

    @Test
    public void smallerCandidatePicksMin() throws Exception {
        SmallerCandidate sc = new SmallerCandidate();
        List<State> neighborhood = new ArrayList<>();
        neighborhood.add(stateWithEval(5.0));
        neighborhood.add(stateWithEval(2.0));
        neighborhood.add(stateWithEval(3.0));

        State picked = sc.stateSearch(neighborhood);
        assertNotNull(picked);
        assertEquals(2.0, picked.getEvaluation().get(0));
    }

    @Test
    public void smallerCandidateSingleReturnsSame() throws Exception {
        SmallerCandidate sc = new SmallerCandidate();
        List<State> neighborhood = new ArrayList<>();
        State only = stateWithEval(4.2);
        neighborhood.add(only);
        State picked = sc.stateSearch(neighborhood);
        assertSame(only, picked);
    }

    @Test
    public void randomCandidateReturnsMemberOfNeighborhood() {
        RandomCandidate rc = new RandomCandidate();
        List<State> neighborhood = new ArrayList<>();
        neighborhood.add(stateWithEval(10));
        neighborhood.add(stateWithEval(20));
        neighborhood.add(stateWithEval(30));

        State picked = rc.stateSearch(neighborhood);
        assertNotNull(picked);
        assertTrue(neighborhood.contains(picked));
    }
}
