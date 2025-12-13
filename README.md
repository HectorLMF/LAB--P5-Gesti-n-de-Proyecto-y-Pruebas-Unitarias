# BiCIAM - Algoritmos Metaheurísticos y Evolutivos

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias)

Proyecto de algoritmos metaheurísticos y evolutivos para la resolución de problemas de optimización.

## Despliegue con Docker

 - Imagen multi-stage incluida: `Dockerfile` en la raíz del repo.
 - Construcción (local): `docker build -t biciam:latest .`
 - Ejecutar (local): `docker run --rm biciam:latest`
 - Nota: la imagen ejecuta `java -jar /app/app.jar`. Si el JAR no contiene un `Main-Class` en el manifest la ejecución fallará con "no main manifest attribute". En ese caso hay dos opciones:
	 - Ejecutar una clase concreta dentro del contenedor usando `docker run --rm biciam:latest java -cp /app/app.jar <MainClass>`
	 - Añadir una clase `Main` y configurar el `pom.xml` para incluir `Main-Class` en el manifest antes de construir la imagen.


### Documentacion doxygen en Github Pages:
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/html/index.html

## Estructura del Proyecto

El proyecto ahora sigue la estructura estándar de Maven:

```
BiCIAM/
├── src/
│   ├── main/
│   │   ├── java/          # Código fuente
│   │   └── resources/     # Recursos de la aplicación
│   └── test/
│       ├── java/          # Código de pruebas
│       └── resources/     # Recursos de pruebas
├── pom.xml                # Configuración de Maven
└── README.md
```

## Requisitos Previos

- Java JDK 11 o superior
- Maven 3.6 o superior

## Compilación del Proyecto

Para compilar el proyecto, ejecute:

```bash
mvn clean compile
```

## Ejecutar Pruebas

Para ejecutar las pruebas unitarias:

```bash
mvn test
```

## Generar Reporte de Cobertura

Para generar un reporte de cobertura de código con JaCoCo:

```bash
mvn test jacoco:report
```

El reporte se generará en `target/site/jacoco/index.html`

## Estado de Cobertura (actual)

	**Pruebas (archivos de test y clases objetivo)**

	- `src/test/java/evolutionary_algorithms/complement/UniformCrossoverTest.java` : `evolutionary_algorithms.complement.UniformCrossover`
	- `src/test/java/evolutionary_algorithms/complement/TruncationSelectionTest.java` : `evolutionary_algorithms.complement.TruncationSelection`
	- `src/test/java/evolutionary_algorithms/complement/SteadyStateReplaceTest.java` : `evolutionary_algorithms.complement.SteadyStateReplace`
	- `src/test/java/evolutionary_algorithms/complement/RangeTest.java` : `evolutionary_algorithms.complement.Range`
	- `src/test/java/evolutionary_algorithms/complement/ProbabilityTest.java` : `evolutionary_algorithms.complement.Probability`
	- `src/test/java/evolutionary_algorithms/complement/OnePointMutationTest.java` : `evolutionary_algorithms.complement.OnePointMutation`
	- `src/test/java/evolutionary_algorithms/complement/OnePointCrossoverTest.java` : `evolutionary_algorithms.complement.OnePointCrossover`
	- `src/test/java/evolutionary_algorithms/complement/GenerationalReplaceTest.java` : `evolutionary_algorithms.complement.GenerationalReplace`
	- `src/test/java/metaheurictics/strategy/StrategyTest.java` : `metaheurictics.strategy.Strategy`
	- `src/test/java/problem/definition/ProblemSettersExtraTest.java` : `problem.definition.Problem`
	- `src/test/java/problem/definition/StateComparatorEqualTest.java` : `problem.definition.State`
	- `src/test/java/problem/definition/StateConstructorsTest.java` : `problem.definition.State`
	- `src/test/java/problem/definition/ProblemEvaluateWithMethodTest.java` : `problem.definition.Problem`
	- `src/test/java/problem/definition/ProblemDefinitionExtraTest.java` : `problem.definition.Problem`
	- `src/test/java/problem/definition/ProblemEvaluateCoverageTest.java` : `problem.definition.Problem`
	- `src/test/java/problem/definition/ProblemCoverageTest.java` : `problem.definition.Problem`
	- `src/test/java/problem/definition/ObjetiveFunctionCoverageTest.java` : `problem.definition.ObjetiveFunction`
	- `src/test/java/problem/definition/OperatorCoverageTest.java` : `problem.definition.Operator`
	- `src/test/java/problem/definition/StateCoverageTest.java` : `problem.definition.State`
	- `src/test/java/problem/definition/ProblemNewSolutionMethodTest.java` : `problem.definition.Problem`
	- `src/test/java/metaheuristics/generators/MultiGeneratorTest.java` : `metaheuristics.generators.MultiGenerator`
	- `src/test/java/metaheuristics/generators/HillClimbingTest.java` : `metaheuristics.generators.HillClimbing`
	- `src/test/java/metaheuristics/generators/GeneticAlgorithmTest.java` : `metaheuristics.generators.GeneticAlgorithm`
	- `src/test/java/problem/definition/StateTest.java` : `problem.definition.State`
	- `src/test/java/problem/definition/StateNullHandlingTest.java` : `problem.definition.State`
	- `src/test/java/problem/definition/StateGetCopyTest.java` : `problem.definition.State`
	- `src/test/java/metaheuristics/generators/ParticleSwarmOptimizationTest.java` : `metaheuristics.generators.ParticleSwarmOptimization`
	- `src/test/java/metaheuristics/generators/TabuSearchTest.java` : `metaheuristics.generators.TabuSearch`
	- `src/test/java/metaheuristics/generators/SimulatedAnnealingTest.java` : `metaheuristics.generators.SimulatedAnnealing`
	- `src/test/java/config/SecureRandomGeneratorTest.java` : `config.SecureRandomGenerator`
	- `src/test/java/problem/extension/MetricasMultiobjetivoTest.java` : `problem.extension.MetricasMultiobjetivo`
	- `src/test/java/problem/extension/SolutionMethodsTest.java` : `problem.extension.SolutionMethod`
	- `src/test/java/problem/extension/MultiObjetivoPuroMaxTest.java` : `problem.extension.MultiObjetivoPuro`
	- `src/test/java/problem/extension/FactoresPonderadosTest.java` : `problem.extension.FactoresPonderados`
	- `src/test/java/factory_method/FactoryAcceptCandidateTest.java` : `factory_method.FactoryAcceptCandidate`
	- `src/test/java/factory_method/FactoryCandidateTest.java` : `factory_method.FactoryCandidate`
	- `src/test/java/factory_method/FactorySolutionMethodTest.java` : `factory_method.FactorySolutionMethod`
	- `src/test/java/factory_method/FactorySamplingTest.java` : `factory_method.FactorySampling`
	- `src/test/java/factory_method/FactoryReplaceTest.java` : `factory_method.FactoryReplace`
	- `src/test/java/factory_method/FactoryReplaceExtraTest.java` : `factory_method.FactoryReplace`
	- `src/test/java/factory_method/FactoryMutationTest.java` : `factory_method.FactoryMutation`
	- `src/test/java/factory_method/FactoryLoaderTest.java` : `factory_method.FactoryLoader`
	- `src/test/java/factory_method/FactoryGeneratorTest.java` : `factory_method.FactoryGenerator`
	- `src/test/java/factory_method/FactoryGeneratorExtraTest.java` : `factory_method.FactoryGenerator`
	- `src/test/java/factory_method/FactoryFatherSelectionTest.java` : `factory_method.FactoryFatherSelection`
	- `src/test/java/factory_method/FactoryDistributionTest.java` : `factory_method.FactoryDistribution`
	- `src/test/java/factory_method/FactoryCrossoverTest.java` : `factory_method.FactoryCrossover`
	- `src/test/java/local_search/acceptation_type/AcceptNotDominatedTest.java` : `local_search.acceptation_type.AcceptNotDominated`
	- `src/test/java/local_search/acceptation_type/DominanceMaxTest.java` : `local_search.acceptation_type.DominanceMax`
	- `src/test/java/local_search/complement/StopExecuteTest.java` : `local_search.complement.StopExecute`
	- `src/test/java/local_search/acceptation_type/AcceptNotBadUTest.java` : `local_search.acceptation_type.AcceptNotBadU`
	- `src/test/java/local_search/acceptation_type/AcceptNotBadTTest.java` : `local_search.acceptation_type.AcceptNotBadT`
	- `src/test/java/local_search/acceptation_type/AcceptNotBadTest.java` : `local_search.acceptation_type.AcceptNotBad`
	- `src/test/java/local_search/acceptation_type/AcceptBestTest.java` : `local_search.acceptation_type.AcceptBest`
	- `src/test/java/local_search/acceptation_type/AcceptAnyoneTest.java` : `local_search.acceptation_type.AcceptAnyone`
	- `src/test/java/local_search/candidate_type/SmallerCandidateTest.java` : `local_search.candidate_type.SmallerCandidate`
	- `src/test/java/local_search/candidate_type/SearchCandidateTest.java` : `local_search.candidate_type.SearchCandidate`
	- `src/test/java/local_search/candidate_type/RandomCandidateTest.java` : `local_search.candidate_type.RandomCandidate`
	- `src/test/java/local_search/candidate_type/NotDominatedCandidateTest.java` : `local_search.candidate_type.NotDominatedCandidate`
	- `src/test/java/local_search/acceptation_type/DominanceMinTest.java` : `local_search.acceptation_type.DominanceMin`
	- `src/test/java/local_search/candidate_type/GreaterCandidateTest.java` : `local_search.candidate_type.GreaterCandidate`
	- `src/test/java/local_search/acceptation_type/DominanceTest.java` : `local_search.acceptation_type.Dominance`

	Total de archivos de prueba detectados: **62**.

	Si quieres que incluya porcentajes de cobertura por clase (líneas cubiertas / totales), puedo parsear `target/site/jacoco/jacoco.xml` y añadirlos aquí.

	Para regenerar el informe y verificar números exactos:

```powershell
mvn clean test jacoco:report
Start-Process "target/site/jacoco/index.html"
```

Nota: Los porcentajes anteriores se obtienen del archivo `target/site/jacoco/jacoco.xml` generado por JaCoCo en el último `mvn verify`.

## Empaquetar el Proyecto

Para crear el archivo JAR:

```bash
mvn package
```

## Dependencias Incluidas

- **JUnit 5**: Framework para pruebas unitarias
- **Mockito**: Framework para crear mocks en pruebas
- **AssertJ**: Librería para assertions más expresivas
- **JaCoCo**: Plugin para cobertura de código
- **JExcel API**: Librería para leer archivos Excel (utilizada en MetricasMultiobjetivo)

## Notas Técnicas

- El proyecto utiliza codificación **ISO-8859-1** debido a caracteres especiales en español en los comentarios del código fuente
- Compilado con **Java 11** como versión mínima
- El JAR generado se encuentra en `target/biciam-1.0.0.jar` después de ejecutar `mvn package`

## Comandos Maven Útiles

### Construcción y Empaquetado
- `mvn clean`: Limpia el directorio target
- `mvn compile`: Compila el código fuente
- `mvn test`: Ejecuta las pruebas
- `mvn package`: Empaqueta el proyecto en un JAR
- `mvn install`: Instala el JAR en el repositorio local
- `mvn verify`: Ejecuta todas las verificaciones

### Herramientas de Calidad y Seguridad (Fase 1)
- `mvn jacoco:report`: Genera reporte de cobertura de código
- `mvn dependency-check:check`: Audita vulnerabilidades (CVEs) - **Requiere NVD API Key**
- `mvn pmd:check`: Análisis estático de código (Code Smells)
- `mvn checkstyle:check`: Valida estilo de código
- `mvn spotbugs:check`: Detecta bugs y vulnerabilidades de seguridad ✅ **413 issues encontrados**
- `mvn site`: Genera todos los reportes en `target/site/`

**Ver guía completa:** [CALIDAD_SEGURIDAD.md](CALIDAD_SEGURIDAD.md)

**Nota OWASP:** Para usar OWASP Dependency-Check necesitas una API key gratuita de NVD:
1. Regístrate en: https://nvd.nist.gov/developers/request-an-api-key
2. Agrega a `pom.xml`: `<nvdApiKey>${env.NVD_API_KEY}</nvdApiKey>`
3. Variable de entorno: `$env:NVD_API_KEY="tu-api-key"`

### SonarQube - Análisis Integral de Calidad
```bash
# Análisis completo con SonarQube
mvn clean verify sonar:sonar

# O con token en línea de comandos
mvn clean verify sonar:sonar -Dsonar.token=TU_TOKEN_AQUI
```

**Dashboard:** http://localhost:9000  
**Ver guía completa de instalación y configuración:** [SONARQUBE_SETUP.md](SONARQUBE_SETUP.md)

