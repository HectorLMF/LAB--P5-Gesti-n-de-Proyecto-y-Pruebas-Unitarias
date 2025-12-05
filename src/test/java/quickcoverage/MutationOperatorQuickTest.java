package quickcoverage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MutationOperatorQuickTest {
    @Test
    public void classLoads() {
        assertDoesNotThrow(() -> Class.forName("problem_operators.MutationOperator"));
    }
}
