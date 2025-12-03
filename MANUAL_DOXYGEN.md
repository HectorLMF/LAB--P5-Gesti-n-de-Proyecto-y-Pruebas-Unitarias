# Manual de Generación de Documentación Doxygen - BiCIAM

## 📚 Descripción

Este manual explica cómo generar la documentación completa del proyecto BiCIAM utilizando Doxygen. El proyecto cuenta con **106 archivos Java completamente documentados** con comentarios Doxygen en español.

## 🎯 Requisitos Previos

### 1. Instalar Doxygen

**Windows:**
```powershell
# Opción 1: Usando Chocolatey
choco install doxygen.install

# Opción 2: Descargar desde la web oficial
# Visita: https://www.doxygen.nl/download.html
# Descarga el instalador para Windows y ejecuta
```

**Linux:**
```bash
sudo apt-get install doxygen doxygen-gui
```

**macOS:**
```bash
brew install doxygen
```

### 2. Instalar Graphviz (Opcional pero Recomendado)

Para generar diagramas UML y gráficos de clases:

**Windows:**
```powershell
choco install graphviz
```

**Linux:**
```bash
sudo apt-get install graphviz
```

**macOS:**
```bash
brew install graphviz
```

### 3. Instalar LaTeX (Opcional)

Solo necesario si deseas generar documentación en PDF:

**Windows:**
```powershell
choco install miktex
```

**Linux:**
```bash
sudo apt-get install texlive-full
```

**macOS:**
```bash
brew install --cask mactex
```

## 🚀 Generación de Documentación

### Paso 1: Verificar Instalación

```powershell
# Verificar Doxygen
doxygen --version

# Verificar Graphviz (opcional)
dot -V

# Verificar LaTeX (opcional)
pdflatex --version
```

### Paso 2: Generar Documentación HTML

```powershell
# Navegar al directorio del proyecto
cd "C:\Users\Hecto\Desktop\Nueva carpeta\BiCIAM\BiCIAM"

# Generar documentación
doxygen Doxyfile
```

### Paso 3: Visualizar Documentación

```powershell
# Abrir la documentación HTML generada
start .\docs\html\index.html
```

### Paso 4: Generar PDF (Opcional)

```powershell
# Navegar al directorio LaTeX generado
cd docs\latex

# Compilar el PDF
make

# Si 'make' no funciona, usar directamente pdflatex
pdflatex refman.tex
makeindex refman.idx
pdflatex refman.tex
pdflatex refman.tex

# Abrir el PDF generado
start refman.pdf

# Volver al directorio raíz
cd ..\..
```

## 📂 Estructura de Documentación Generada

```
BiCIAM/
├── docs/
│   ├── html/
│   │   ├── index.html          # Página principal
│   │   ├── annotated.html      # Lista de clases
│   │   ├── classes.html        # Índice de clases
│   │   ├── hierarchy.html      # Jerarquía de clases
│   │   ├── files.html          # Lista de archivos
│   │   ├── namespaces.html     # Lista de paquetes
│   │   └── ...
│   └── latex/
│       ├── refman.tex          # Documento LaTeX principal
│       ├── Makefile            # Para compilar PDF
│       └── ...
└── doxygen_warnings.log        # Log de advertencias
```

## 🎨 Personalización

### Modificar el Logo del Proyecto

1. Coloca tu logo en formato PNG o JPG en el directorio raíz
2. Edita el `Doxyfile`:
   ```
   PROJECT_LOGO = ./logo.png
   ```

### Cambiar Colores del HTML

Edita en `Doxyfile`:
```
HTML_COLORSTYLE_HUE    = 220    # Tono (0-359)
HTML_COLORSTYLE_SAT    = 100    # Saturación (0-255)
HTML_COLORSTYLE_GAMMA  = 80     # Brillo (40-240)
```

### Añadir CSS Personalizado

1. Crea un archivo CSS personalizado:
   ```css
   /* custom.css */
   body {
       font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
   }
   ```

2. Edita `Doxyfile`:
   ```
   HTML_EXTRA_STYLESHEET = custom.css
   ```

## 📊 Contenido Documentado

### Paquetes Principales

| Paquete | Archivos | Descripción |
|---------|----------|-------------|
| `config` | 2 | Configuración y utilidades |
| `factory_interface` | 9 | Interfaces de patrones Factory |
| `factory_method` | 10 | Implementaciones Factory |
| `problem.definition` | 5 | Definición de problemas |
| `problem.extension` | 5 | Extensiones multi-objetivo |
| `problem.operators` | 2 | Operadores de problema |
| `evolutionary_algorithms.complement` | 25 | Componentes evolutivos |
| `local_search.acceptation_type` | 11 | Criterios de aceptación |
| `local_search.candidate_type` | 7 | Estrategias de candidatos |
| `local_search.complement` | 4 | Complementos búsqueda local |
| `metaheuristics.generators` | 24 | Algoritmos metaheurísticos |
| `metaheurictics.strategy` | 1 | Estrategia central Singleton |

**Total: 106 archivos documentados**

## 🔍 Navegación en la Documentación

### Página Principal
- **Classes**: Lista completa de todas las clases
- **Packages**: Organización por paquetes Java
- **Files**: Navegación por archivos fuente
- **Class Hierarchy**: Jerarquía visual de herencia

### Búsqueda Rápida
- Usa la barra de búsqueda en la esquina superior derecha
- Soporta búsqueda incremental
- Filtra por clases, métodos, atributos

### Diagramas UML
- **Class Diagrams**: Relaciones entre clases
- **Collaboration Diagrams**: Colaboración entre objetos
- **Call Graphs**: Grafos de llamadas de métodos
- **Include Graphs**: Dependencias entre archivos

## ⚠️ Solución de Problemas

### Error: "doxygen: command not found"

**Solución:**
```powershell
# Asegúrate de que Doxygen está en el PATH
$env:Path += ";C:\Program Files\doxygen\bin"

# O reinicia PowerShell después de instalar
```

### Advertencias de Documentación

Revisa el archivo `doxygen_warnings.log` para ver:
- Métodos sin documentar
- Parámetros sin descripción
- Referencias rotas

### Diagramas No Se Generan

**Solución:**
```powershell
# Verifica que Graphviz está instalado
dot -V

# Añade Graphviz al PATH
$env:Path += ";C:\Program Files\Graphviz\bin"

# Edita Doxyfile:
# HAVE_DOT = YES
# DOT_PATH = C:/Program Files/Graphviz/bin
```

### PDF No Se Genera

**Solución:**
```powershell
# Instala MiKTeX o TeX Live
choco install miktex

# Navega a docs/latex
cd docs\latex

# Instala paquetes faltantes automáticamente
pdflatex --interaction=nonstopmode refman.tex
```

## 📝 Comandos Útiles

```powershell
# Generar solo HTML (más rápido)
doxygen Doxyfile | Select-String "Generating"

# Ver advertencias
Get-Content doxygen_warnings.log

# Limpiar documentación anterior
Remove-Item -Recurse -Force .\docs

# Regenerar todo
doxygen Doxyfile

# Abrir documentación automáticamente después de generar
doxygen Doxyfile; start .\docs\html\index.html

# Generar y compilar PDF en un solo comando
doxygen Doxyfile; cd docs\latex; make; start refman.pdf; cd ..\..
```

## 🎓 Recursos Adicionales

- **Documentación Oficial Doxygen**: https://www.doxygen.nl/manual/
- **Guía de Comandos Doxygen**: https://www.doxygen.nl/manual/commands.html
- **Ejemplos de Documentación**: https://www.doxygen.nl/manual/examples.html
- **Graphviz**: https://graphviz.org/
- **LaTeX**: https://www.latex-project.org/

## 📧 Soporte

Para problemas específicos del proyecto BiCIAM:
- Revisa el archivo `README.md` del proyecto
- Consulta los comentarios en el código fuente
- Verifica los ejemplos en `docs/html/examples.html`

---

**Generado para BiCIAM Framework v1.0**  
**Fecha: Diciembre 2025**  
**Autor: BiCIAM Team**
