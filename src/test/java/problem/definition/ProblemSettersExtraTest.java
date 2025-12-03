package problem.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import problem.extension.TypeSolutionMethod;
import factory_method.FactorySolutionMethod;

public class ProblemSettersExtraTest {
    @Test
    void setFactoryAndTypeSolutionMethod() {
        Problem p = new Problem();
        p.setTypeSolutionMethod(TypeSolutionMethod.FactoresPonderados);
        assertEquals(TypeSolutionMethod.FactoresPonderados, p.getTypeSolutionMethod());

        FactorySolutionMethod fsm = new FactorySolutionMethod();
        p.setFactorySolutionMethod(fsm);
        assertSame(fsm, p.getFactorySolutionMethod());
    }
}
