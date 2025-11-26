# Guía de Herramientas de Calidad y Seguridad - Fase 1

Este documento describe los plugins de calidad y seguridad configurados en el proyecto BiCIAM.

## 📋 Plugins Configurados

### 1. JaCoCo Maven Plugin - Cobertura de Código
**Propósito:** Medir la cobertura de las pruebas unitarias.

**Comandos:**
```bash
# Generar reporte de cobertura
mvn clean test jacoco:report

# Ver reporte en: target/site/jacoco/index.html
```

**Configuración:**
- Umbral mínimo: 0% (configurable en pom.xml)
- Reportes generados automáticamente después de `mvn test`

---

### 2. OWASP Dependency-Check - Auditoría de Vulnerabilidades
**Propósito:** Buscar vulnerabilidades conocidas (CVEs) en las dependencias del proyecto.

**Comandos:**
```bash
# Ejecutar análisis de vulnerabilidades
mvn dependency-check:check

# Ver reporte en: target/dependency-check-report.html
```

**Configuración:**
- CVSS Score mínimo para fallar: 7.0 (Alto)
- Formatos: HTML y JSON
- Archivo de supresión: `owasp-suppressions.xml`

**Nota:** La primera ejecución puede tardar varios minutos mientras descarga la base de datos de CVEs.

---

### 3. PMD Maven Plugin - Análisis Estático
**Propósito:** Detectar Code Smells, problemas de diseño y código duplicado (CPD).

**Comandos:**
```bash
# Ejecutar análisis PMD
mvn pmd:check

# Generar reporte HTML
mvn pmd:pmd

# Detectar código duplicado
mvn pmd:cpd

# Ver reportes en:
# - target/pmd.xml
# - target/cpd.xml
# - target/site/pmd.html
```

**Reglas aplicadas:**
- Ruleset: Java Quickstart (conjunto básico de reglas)
- Detección de código duplicado: mínimo 100 tokens

**Problemas comunes detectados:**
- Variables no utilizadas
- Imports innecesarios
- Métodos demasiado largos
- Complejidad ciclomática alta
- Código duplicado

---

### 4. Checkstyle Maven Plugin - Estilo de Código
**Propósito:** Imponer un estilo de código estándar y consistente.

**Comandos:**
```bash
# Validar estilo de código
mvn checkstyle:check

# Generar reporte
mvn checkstyle:checkstyle

# Ver reporte en: target/site/checkstyle.html
```

**Configuración:**
- Estándar: Google Java Style Guide
- Codificación: ISO-8859-1
- El build no falla por violaciones (configurable)

**Reglas principales:**
- Indentación correcta
- Nombres de variables según convención
- Espacios en blanco consistentes
- Organización de imports
- Longitud máxima de líneas

---

### 5. SpotBugs + FindSecBugs - Detección de Bugs y Vulnerabilidades
**Propósito:** Encontrar bugs potenciales y vulnerabilidades de seguridad (SAST).

**Comandos:**
```bash
# Ejecutar análisis de SpotBugs
mvn spotbugs:check

# Generar reporte GUI (requiere interfaz gráfica)
mvn spotbugs:gui

# Ver reporte en: target/spotbugsXml.xml
```

**Configuración:**
- Esfuerzo: Máximo
- Umbral: Low (detecta todos los niveles)
- Incluye FindSecBugs para análisis de seguridad

**FindSecBugs detecta:**
- Inyecciones SQL
- Cross-Site Scripting (XSS)
- Uso inseguro de criptografía
- Manejo inadecuado de contraseñas
- Deserialización insegura
- Path Traversal
- Configuraciones inseguras

---

## 🚀 Comandos Útiles Combinados

### Ejecutar todos los análisis de calidad
```bash
mvn clean verify site
```

### Solo análisis de seguridad
```bash
mvn dependency-check:check spotbugs:check
```

### Solo análisis de código
```bash
mvn pmd:check checkstyle:check
```

### Generar todos los reportes (sin fallar el build)
```bash
mvn clean test site -Dcheckstyle.failOnViolation=false -Dpmd.failOnViolation=false
```

### Ver reportes en un servidor local
```bash
mvn site:run
# Acceder a: http://localhost:8080
```

---

## 📊 Ubicación de Reportes

Después de ejecutar los análisis, los reportes se generan en:

```
target/
├── site/
│   ├── jacoco/
│   │   └── index.html          # Cobertura de código
│   ├── checkstyle.html          # Reporte de estilo
│   ├── pmd.html                 # Reporte PMD
│   ├── cpd.html                 # Código duplicado
│   └── spotbugs.html            # Bugs y vulnerabilidades
├── dependency-check-report.html  # Vulnerabilidades CVE
├── spotbugsXml.xml              # XML de SpotBugs
├── pmd.xml                      # XML de PMD
└── checkstyle-result.xml        # XML de Checkstyle
```

---

## ⚙️ Configuración Personalizada

### Ajustar umbrales de JaCoCo
Editar en `pom.xml`:
```xml
<limit>
    <counter>LINE</counter>
    <value>COVEREDRATIO</value>
    <minimum>0.80</minimum>  <!-- 80% de cobertura -->
</limit>
```

### Cambiar CVSS mínimo de OWASP
```xml
<failBuildOnCVSS>9</failBuildOnCVSS>  <!-- Solo críticas -->
```

### Usar reglas personalizadas de PMD
1. Crear archivo `pmd-ruleset.xml`
2. Actualizar en `pom.xml`:
```xml
<rulesets>
    <ruleset>pmd-ruleset.xml</ruleset>
</rulesets>
```

### Usar configuración personalizada de Checkstyle
1. Crear archivo `checkstyle.xml`
2. Actualizar en `pom.xml`:
```xml
<configLocation>checkstyle.xml</configLocation>
```

---

## 🔧 Integración Continua (CI/CD)

Para integrar en pipelines de CI/CD (GitHub Actions, Jenkins, etc.):

```bash
# Comando completo para CI
mvn clean verify \
  -Dcheckstyle.failOnViolation=true \
  -Dpmd.failOnViolation=true \
  -DfailBuildOnCVSS=7
```

---

## 📚 Referencias

- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [OWASP Dependency-Check](https://jeremylong.github.io/DependencyCheck/)
- [PMD Rules](https://pmd.github.io/pmd/pmd_rules_java.html)
- [Checkstyle Checks](https://checkstyle.sourceforge.io/checks.html)
- [SpotBugs Bug Descriptions](https://spotbugs.readthedocs.io/)
- [FindSecBugs Rules](https://find-sec-bugs.github.io/bugs.htm)

---

## 🐛 Solución de Problemas

### Error: "Unable to find main class"
- SpotBugs requiere que el código esté compilado: `mvn clean compile spotbugs:check`

### Error: "CVE database download failed"
- OWASP necesita descargar ~500MB la primera vez
- Verificar conexión a internet
- Aumentar timeout: `mvn dependency-check:check -DconnectionTimeout=300000`

### Checkstyle reporta muchos errores
- Deshabilitar temporalmente: `-Dcheckstyle.skip=true`
- O cambiar a modo warning: `<failsOnError>false</failsOnError>`

### PMD/SpotBugs muy lento
- Reducir esfuerzo en SpotBugs: `<effort>Default</effort>`
- Excluir directorios: usar `<excludes>` en configuración

---

## ✅ Checklist de Calidad

Antes de hacer commit/push, verificar:

- [ ] `mvn clean test` - Todas las pruebas pasan
- [ ] `mvn jacoco:report` - Cobertura aceptable
- [ ] `mvn pmd:check` - Sin violaciones críticas
- [ ] `mvn checkstyle:check` - Estilo consistente
- [ ] `mvn spotbugs:check` - Sin bugs de seguridad
- [ ] `mvn dependency-check:check` - Sin CVEs críticas

---

## 📝 Notas Adicionales

- Los análisis estáticos pueden producir **falsos positivos**
- Revisar manualmente los reportes antes de aplicar cambios
- Usar archivos de supresión para excepciones justificadas
- Actualizar plugins regularmente para nuevas reglas de seguridad
- La configuración actual está en modo "warning" para facilitar adopción gradual
