package quickcoverage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TowPointsMutationQuickTest {
    @Test
    void classLoads() {
        assertDoesNotThrow(() -> Class.forName("evolutionary_algorithms.complement.TowPointsMutation"));
    }
}
