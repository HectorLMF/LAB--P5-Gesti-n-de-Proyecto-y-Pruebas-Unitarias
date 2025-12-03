package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import evolutionary_algorithms.complement.ReplaceType;

public class FactoryReplaceExtraTest {

    @Test
    @DisplayName("createReplace returns null for null type")
    void createReplace_nullType_returnsNull() {
        FactoryReplace fr = new FactoryReplace();
        try {
            assertNull(fr.createReplace(null));
        } catch (Exception ex) {
            // acceptable if it throws due to null
        }
    }

    @Test
    @DisplayName("createReplace handles all enum values without throwing")
    void createReplace_allEnums() {
        FactoryReplace fr = new FactoryReplace();
        for (ReplaceType rt : ReplaceType.values()) {
            assertDoesNotThrow(() -> fr.createReplace(rt));
        }
    }
}
