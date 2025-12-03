package factory_method;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import problem.extension.SolutionMethod;
import problem.extension.TypeSolutionMethod;

class FactorySolutionMethodTest {

    @Test
    void createValidSolutionMethodMultiObjetivoPuro() throws Exception {
        FactorySolutionMethod factory = new FactorySolutionMethod();
        // Pick an enum value that exists as a class in problem.extension
        // e.g., MultiObjetivoPuro or FactoresPonderados depending on enum names
        TypeSolutionMethod type = TypeSolutionMethod.MULTI_OBJETIVO_PURO;
        SolutionMethod sm = factory.createdSolutionMethod(type);
        assertNotNull(sm);
        assertEquals("problem.extension." + type.toClassName(), sm.getClass().getName());
    }

    @Test
    void createValidSolutionMethodFactoresPonderados() throws Exception {
        FactorySolutionMethod factory = new FactorySolutionMethod();
        TypeSolutionMethod type = TypeSolutionMethod.FACTORES_PONDERADOS;
        SolutionMethod sm = factory.createdSolutionMethod(type);
        assertNotNull(sm);
        assertEquals("problem.extension." + type.toClassName(), sm.getClass().getName());
    }

    @Test
    void createNullThrows() {
        FactorySolutionMethod factory = new FactorySolutionMethod();
        assertThrows(Exception.class, () -> factory.createdSolutionMethod(null));
    }
}
