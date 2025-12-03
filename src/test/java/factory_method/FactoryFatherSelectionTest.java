package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFactoryFatherSelection;

@DisplayName("Tests para FactoryFatherSelection")
class FactoryFatherSelectionTest {

    private FactoryFatherSelection factoryFatherSelection;

    @BeforeEach
    void setUp() {
        factoryFatherSelection = new FactoryFatherSelection();
    }

    @Test
    @DisplayName("FactoryFatherSelection debe ser instanciable")
    void testFactoryFatherSelectionInstantiation() {
        assertNotNull(factoryFatherSelection);
    }

    @Test
    @DisplayName("FactoryFatherSelection debe implementar IFFactoryFatherSelection")
    void testImplementsInterface() {
        assertInstanceOf(IFFactoryFatherSelection.class, factoryFatherSelection);
    }

    @Test
    @DisplayName("createSelectFather() con tipo null debe lanzar excepción")
    void testCreateSelectFatherWithNullType() {
        assertThrows(Exception.class, () -> {
            factoryFatherSelection.createSelectFather(null);
        });
    }
}
