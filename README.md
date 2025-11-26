# BiCIAM - Algoritmos Metaheurísticos y Evolutivos

Proyecto de algoritmos metaheurísticos y evolutivos para la resolución de problemas de optimización.

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

- `mvn clean`: Limpia el directorio target
- `mvn compile`: Compila el código fuente
- `mvn test`: Ejecuta las pruebas
- `mvn package`: Empaqueta el proyecto en un JAR
- `mvn install`: Instala el JAR en el repositorio local
- `mvn verify`: Ejecuta todas las verificaciones
