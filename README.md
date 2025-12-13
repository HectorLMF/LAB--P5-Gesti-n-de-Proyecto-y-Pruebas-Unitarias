# BiCIAM - Algoritmos Metaheurísticos y Evolutivos

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=coverage)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias)

 [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=HectorLMF_LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias) 



Proyecto de algoritmos metaheurísticos y evolutivos para la resolución de problemas de optimización.

## Despliegue en Integración Continua
[![CI](https://github.com/HectorLMF/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/actions/workflows/ci.yml/badge.svg)](https://github.com/HectorLMF/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/actions/workflows/ci.yml) [![pages-build-deployment](https://github.com/HectorLMF/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/actions/workflows/pages/pages-build-deployment/badge.svg)](https://github.com/HectorLMF/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/actions/workflows/pages/pages-build-deployment)
- Se ha utilizado Github actions, script YML localizado en .github/workflows
- Capturas de integracion continua en /Capturas:
  	- Fallada: Forzamos un error en Particle.java, eliminando un ciere de linea aleatorio.
  	- Pasada: Comprobamos que todos los reportes pasan. 
### Documentacion doxygen en Github Pages:
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/html/index.html


## Despliegue con Docker

 - Imagen multi-stage incluida: `Dockerfile` en la raíz del repo.
 - Construcción (local): `docker build -t biciam:latest .`
 - Ejecutar (local): `docker run --rm biciam:latest`
 - Nota: la imagen ejecuta `java -jar /app/app.jar`. Si el JAR no contiene un `Main-Class` en el manifest la ejecución fallará con "no main manifest attribute". En ese caso hay dos opciones:
	 - Ejecutar una clase concreta dentro del contenedor usando `docker run --rm biciam:latest java -cp /app/app.jar <MainClass>`
	 - Añadir una clase `Main` y configurar el `pom.xml` para incluir `Main-Class` en el manifest antes de construir la imagen.




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

