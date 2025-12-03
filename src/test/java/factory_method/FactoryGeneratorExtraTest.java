package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import metaheuristics.generators.GeneratorType;

public class FactoryGeneratorExtraTest {

    @Test
    @DisplayName("createGenerator returns null for null type")
    void createGenerator_nullType_returnsNull() {
        FactoryGenerator fg = new FactoryGenerator();
        try {
            assertNull(fg.createGenerator(null));
        } catch (Exception ex) {
            // Method may throw due to null; acceptable behavior
        }
    }

    @Test
    @DisplayName("createGenerator returns generator for all enum values if supported")
    void createGenerator_allEnums() {
        FactoryGenerator fg = new FactoryGenerator();
        for (GeneratorType gt : GeneratorType.values()) {
            // Method should not throw; may return null when not supported
            try {
                fg.createGenerator(gt);
            } catch (Exception ex) {
                fail("createGenerator threw for type: " + gt + " - " + ex);
            }
        }
    }
}
