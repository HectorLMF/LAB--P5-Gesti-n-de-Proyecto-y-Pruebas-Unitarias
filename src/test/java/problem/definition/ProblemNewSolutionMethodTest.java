package problem.definition;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import problem.extension.TypeSolutionMethod;
import problem.extension.SolutionMethod;

public class ProblemNewSolutionMethodTest {
    @Test
    void newSolutionMethodReturnsInstance() throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Problem p = new Problem();
        SolutionMethod sm = p.newSolutionMethod(TypeSolutionMethod.FactoresPonderados);
        assertNotNull(sm);
    }
}
