package problem.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class StateGetCopyTest {
    @Test
    void getCopyReturnsClone() {
        ArrayList<Object> code = new ArrayList<>();
        code.add(1);
        code.add(2);
        State s = new State(code);
        s.setNumber(10);
        
        Object copy = s.getCopy();
        assertNotNull(copy);
        assertEquals(State.class, copy.getClass());
        
        State sCopy = (State) copy;
        assertEquals(2, sCopy.getCode().size());
        assertEquals(10, sCopy.getNumber());
    }
}
