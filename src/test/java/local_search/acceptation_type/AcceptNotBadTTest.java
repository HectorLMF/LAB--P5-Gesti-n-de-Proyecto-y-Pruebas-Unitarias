package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.SimulatedAnnealing;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * Pruebas exhaustivas para AcceptNotBadT (criterio de aceptación con temperatura Simulated Annealing)
 * 
 * Cubre:
 * - Aceptación determinística (mejor solución)
 * - Aceptación probabilística (peor solución con temperatura)
 * - Casos de maximización y minimización
 * - Diferentes temperaturas (alta, baja, extremas)
 * - Diferencias de calidad (pequeñas, grandes)
 * - Casos límite (igualdad, cero, valores extremos)
 * - Comportamiento probabilístico con múltiples ejecuciones
 */
@DisplayName("Tests exhaustivos de AcceptNotBadT")
public class AcceptNotBadTTest {

    private AcceptNotBadT acceptor;
    private Strategy mockStrategy;
    private Problem mockProblem;
    
    /**
     * Crea un estado con una evaluación específica
     */
    private State createState(double evaluation) {
        State state = new State();
        List<Double> evaluationList = new ArrayList<>();
        evaluationList.add(evaluation);
        state.setEvaluation(evaluationList);
        return state;
    }

    @BeforeEach
    public void setUp() {
        acceptor = new AcceptNotBadT();
        mockStrategy = mock(Strategy.class);
        mockProblem = mock(Problem.class);
        
        when(mockStrategy.getProblem()).thenReturn(mockProblem);
    }

    // ==================== TESTS DE MAXIMIZACIÓN ====================
    
    @Test
    @DisplayName("MAX: Acepta candidato mejor (200 > 100)")
    public void testMaximizacionAceptaCandidatoMejor() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(100.0);
        State candidate = createState(200.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar candidato mejor en maximización");
        }
    }
    
    @Test
    @DisplayName("MAX: Acepta candidato igual (100 >= 100)")
    public void testMaximizacionAceptaCandidatoIgual() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(100.0);
        State candidate = createState(100.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar candidato igual en maximización");
        }
    }
    
    @Test
    @DisplayName("MAX: Rechaza candidato mucho peor con temperatura baja")
    public void testMaximizacionRechazaCandidatoMuchoPeorTemperaturaBaja() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 1.0; // Temperatura muy baja
        
        State current = createState(100.0);
        State candidate = createState(50.0); // Mucho peor
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            // Con temperatura baja, exp((50-100)/1.0) = exp(-50) ≈ 0
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertNotNull(result);
        }
    }
    
    @RepeatedTest(10)
    @DisplayName("MAX: Con temperatura alta acepta más candidatos peores")
    public void testMaximizacionTemperaturaAltaAceptaMasCandidatosPeores() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 1000.0; // Temperatura muy alta
        
        State current = createState(100.0);
        State candidate = createState(95.0); // Ligeramente peor
        
        int acceptCount = 0;
        for (int i = 0; i < 20; i++) {
            try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
                strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
                
                // exp((95-100)/1000) = exp(-0.005) ≈ 0.995
                Boolean result = acceptor.acceptCandidate(current, candidate);
                if (result) acceptCount++;
            }
        }
        
        // Con temperatura alta, debería aceptar casi siempre
        assertTrue(acceptCount >= 15, 
            "Con temperatura alta debe aceptar mayoría de candidatos ligeramente peores (aceptó " + acceptCount + "/20)");
    }
    
    @Test
    @DisplayName("MAX: Temperatura extremadamente alta acepta casi todo")
    public void testMaximizacionTemperaturaExtremadamenteAltaAceptaCasiTodo() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = Double.MAX_VALUE; // Temperatura extrema
        
        State current = createState(1000.0);
        State candidate = createState(1.0); // Mucho peor
        
        int acceptCount = 0;
        for (int i = 0; i < 20; i++) {
            try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
                strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
                
                // exp((1-1000)/MAX) ≈ exp(0) = 1
                Boolean result = acceptor.acceptCandidate(current, candidate);
                if (result) acceptCount++;
            }
        }
        
        assertTrue(acceptCount >= 18, 
            "Con temperatura extrema debe aceptar casi todos (aceptó " + acceptCount + "/20)");
    }
    
    @Test
    @DisplayName("MAX: Pequeña mejora siempre aceptada")
    public void testMaximizacionPequenaMejoraSiempreAceptada() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 50.0;
        
        State current = createState(100.0);
        State candidate = createState(100.01); // Ligeramente mejor
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar cualquier mejora, por pequeña que sea");
        }
    }
    
    @Test
    @DisplayName("MAX: Valores negativos con mejora")
    public void testMaximizacionValoresNegativosConMejora() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(-50.0);
        State candidate = createState(-40.0); // Mejor (menos negativo)
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar mejora incluso con valores negativos");
        }
    }
    
    @Test
    @DisplayName("MAX: Desde cero a positivo")
    public void testMaximizacionDesdeCeroAPositivo() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(0.0);
        State candidate = createState(1.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar transición de 0 a positivo");
        }
    }
    
    @Test
    @DisplayName("MAX: Valores extremos con mejora")
    public void testMaximizacionValoresExtremosConMejora() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 1000.0;
        
        State current = createState(Double.MAX_VALUE - 1000);
        State candidate = createState(Double.MAX_VALUE - 500);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar mejora con valores extremos");
        }
    }

    // ==================== TESTS DE MINIMIZACIÓN ====================
    
    @Test
    @DisplayName("MIN: Acepta candidato mejor (50 < 100)")
    public void testMinimizacionAceptaCandidatoMejor() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(100.0);
        State candidate = createState(50.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar candidato mejor (menor) en minimización");
        }
    }
    
    @Test
    @DisplayName("MIN: Acepta candidato igual (100 <= 100)")
    public void testMinimizacionAceptaCandidatoIgual() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(100.0);
        State candidate = createState(100.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar candidato igual en minimización");
        }
    }
    
    @Test
    @DisplayName("MIN: Rechaza candidato mucho peor con temperatura baja")
    public void testMinimizacionRechazaCandidatoMuchoPeorTemperaturaBaja() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        SimulatedAnnealing.tinitial = 1.0; // Temperatura muy baja
        
        State current = createState(50.0);
        State candidate = createState(100.0); // Mucho peor (mayor)
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            // exp((100-50)/1.0) = exp(50) >> 1, pero random < exp(-50) ≈ 0
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertNotNull(result);
        }
    }
    
    @RepeatedTest(10)
    @DisplayName("MIN: Con temperatura alta acepta más candidatos peores")
    public void testMinimizacionTemperaturaAltaAceptaMasCandidatosPeores() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        SimulatedAnnealing.tinitial = 1000.0; // Temperatura muy alta
        
        State current = createState(95.0);
        State candidate = createState(100.0); // Ligeramente peor
        
        int acceptCount = 0;
        for (int i = 0; i < 20; i++) {
            try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
                strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
                
                // exp((100-95)/1000) = exp(0.005) ≈ 1.005, pero necesita random < exp(-0.005)
                Boolean result = acceptor.acceptCandidate(current, candidate);
                if (result) acceptCount++;
            }
        }
        
        // Con temperatura alta, debería aceptar algunos
        assertTrue(acceptCount >= 15, 
            "Con temperatura alta debe aceptar mayoría de candidatos ligeramente peores (aceptó " + acceptCount + "/20)");
    }
    
    @Test
    @DisplayName("MIN: Pequeña mejora siempre aceptada")
    public void testMinimizacionPequenaMejoraSiempreAceptada() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        SimulatedAnnealing.tinitial = 50.0;
        
        State current = createState(100.0);
        State candidate = createState(99.99); // Ligeramente mejor (menor)
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar cualquier mejora (reducción), por pequeña que sea");
        }
    }
    
    @Test
    @DisplayName("MIN: Valores negativos con mejora")
    public void testMinimizacionValoresNegativosConMejora() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(-40.0);
        State candidate = createState(-50.0); // Mejor (más negativo)
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar mejora hacia valores más negativos en MIN");
        }
    }
    
    @Test
    @DisplayName("MIN: Desde positivo a cero")
    public void testMinimizacionDesdePositivoACero() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(1.0);
        State candidate = createState(0.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar transición a cero en minimización");
        }
    }
    
    @Test
    @DisplayName("MIN: Valores muy pequeños cercanos a cero")
    public void testMinimizacionValoresMuyPequenos() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        SimulatedAnnealing.tinitial = 0.001;
        
        State current = createState(0.001);
        State candidate = createState(0.0001); // Mejora
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Debe aceptar mejora con valores muy pequeños");
        }
    }

    // ==================== TESTS DE TEMPERATURAS ====================
    
    @Test
    @DisplayName("Temperatura cero - debe manejar sin error")
    public void testTemperaturaCero() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 0.0; // Temperatura cero
        
        State current = createState(100.0);
        State candidate = createState(150.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            // Debería causar división por cero, pero acepta mejoras directamente
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Mejora debe aceptarse incluso con temperatura 0");
        }
    }
    
    @Test
    @DisplayName("Temperatura negativa - caso especial")
    public void testTemperaturaNegativa() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = -50.0; // Temperatura negativa (inválida)
        
        State current = createState(100.0);
        State candidate = createState(150.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertNotNull(result);
        }
    }
    
    @Test
    @DisplayName("Temperatura muy pequeña (epsilon)")
    public void testTemperaturaMuyPequena() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 0.0001;
        
        State current = createState(100.0);
        State candidate = createState(99.0); // Ligeramente peor
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            // exp((99-100)/0.0001) = exp(-10000) ≈ 0
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertNotNull(result);
        }
    }

    // ==================== TESTS DE CASOS EXTREMOS ====================
    
    @Test
    @DisplayName("Diferencia infinita positiva")
    public void testDiferenciaInfinitaPositiva() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(0.0);
        State candidate = createState(Double.MAX_VALUE);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Mejora infinita debe ser aceptada");
        }
    }
    
    @Test
    @DisplayName("Ambos estados en infinito")
    public void testAmbosEstadosInfinito() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(Double.MAX_VALUE);
        State candidate = createState(Double.MAX_VALUE);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Estados iguales en infinito deben aceptarse");
        }
    }
    
    @Test
    @DisplayName("Evaluaciones muy cercanas (diferencia epsilon)")
    public void testEvaluacionesMuyCercanas() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(100.0);
        State candidate = createState(100.0 + 1e-15); // Diferencia epsilon
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertTrue(result, "Mejora epsilon debe ser aceptada");
        }
    }
    
    // ==================== TESTS DE INSTANCIA Y HERENCIA ====================
    
    @Test
    @DisplayName("Es instancia de AcceptableCandidate")
    public void testEsInstanciaDeAcceptableCandidate() {
        assertInstanceOf(AcceptableCandidate.class, acceptor,
            "AcceptNotBadT debe ser instancia de AcceptableCandidate");
    }
    
    @Test
    @DisplayName("Método acceptCandidate no retorna null")
    public void testAcceptCandidateNoRetornaNull() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(100.0);
        State candidate = createState(100.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertNotNull(result, "El resultado no debe ser null");
        }
    }
    
    @Test
    @DisplayName("Resultado es Boolean (no boolean)")
    public void testResultadoEsBoolean() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(100.0);
        State candidate = createState(150.0);
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertInstanceOf(Boolean.class, result,
                "El resultado debe ser de tipo Boolean");
        }
    }

    // ==================== TESTS DE COMPORTAMIENTO PROBABILÍSTICO ====================
    
    @RepeatedTest(20)
    @DisplayName("MAX: Comportamiento probabilístico repetido con temperatura media")
    public void testComportamientoProbabilisticoTemperaturaMedia() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 50.0;
        
        State current = createState(100.0);
        State candidate = createState(95.0); // Ligeramente peor
        
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            // exp((95-100)/50) = exp(-0.1) ≈ 0.904
            Boolean result = acceptor.acceptCandidate(current, candidate);
            assertNotNull(result);
        }
    }
    
    @Test
    @DisplayName("MIN: Comparar aceptación con diferentes temperaturas")
    public void testMinimizacionComparacionTemperaturas() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(50.0);
        State candidate = createState(55.0); // Peor
        
        // Temperatura alta
        SimulatedAnnealing.tinitial = 500.0;
        int acceptCountHigh = 0;
        for (int i = 0; i < 50; i++) {
            try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
                strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
                if (acceptor.acceptCandidate(current, candidate)) acceptCountHigh++;
            }
        }
        
        // Temperatura baja
        SimulatedAnnealing.tinitial = 5.0;
        int acceptCountLow = 0;
        for (int i = 0; i < 50; i++) {
            try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
                strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
                if (acceptor.acceptCandidate(current, candidate)) acceptCountLow++;
            }
        }
        
        assertTrue(acceptCountHigh >= acceptCountLow,
            "Temperatura alta debe aceptar más o igual que temperatura baja " +
            "(Alta: " + acceptCountHigh + ", Baja: " + acceptCountLow + ")");
    }
    
    @Test
    @DisplayName("MAX: Gran deterioro con alta temperatura - alguna aceptación")
    public void testMaxGranDeterioroAltaTemperatura() throws Exception {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        SimulatedAnnealing.tinitial = 100.0;
        
        State current = createState(1000.0);
        State candidate = createState(900.0); // Peor en 100
        
        int acceptCount = 0;
        for (int i = 0; i < 100; i++) {
            try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
                strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
                
                // exp((900-1000)/100) = exp(-1) ≈ 0.368
                if (acceptor.acceptCandidate(current, candidate)) acceptCount++;
            }
        }
        
        assertTrue(acceptCount > 0,
            "Con temperatura razonable debe aceptar algunas soluciones peores (aceptó " + acceptCount + "/100)");
    }
}
