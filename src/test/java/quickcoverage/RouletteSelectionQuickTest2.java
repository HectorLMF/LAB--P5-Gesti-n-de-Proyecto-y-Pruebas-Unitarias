package quickcoverage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RouletteSelectionQuickTest2 {
    @Test
    public void classLoads() {
        assertDoesNotThrow(() -> Class.forName("evolutionary_algorithms.complement.RouletteSelection"));
    }
}
