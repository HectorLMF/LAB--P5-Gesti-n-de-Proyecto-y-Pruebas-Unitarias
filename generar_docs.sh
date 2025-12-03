#!/bin/bash
########################################
# Script de Generación de Documentación
# Proyecto: BiCIAM Framework
########################################

echo ""
echo "========================================"
echo " Generación de Documentación BiCIAM"
echo "========================================"
echo ""

# Verificar si Doxygen está instalado
if ! command -v doxygen &> /dev/null; then
    echo "[ERROR] Doxygen no está instalado."
    echo ""
    echo "Por favor, instala Doxygen:"
    echo "  Ubuntu/Debian: sudo apt-get install doxygen"
    echo "  macOS: brew install doxygen"
    echo ""
    exit 1
fi

echo "[OK] Doxygen encontrado: $(doxygen --version)"
echo ""

# Limpiar documentación anterior
if [ -d "docs" ]; then
    echo "[INFO] Limpiando documentación anterior..."
    rm -rf docs
    echo "[OK] Limpieza completada."
    echo ""
fi

# Generar documentación
echo "[INFO] Generando documentación..."
echo ""
doxygen Doxyfile

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo " Documentación generada exitosamente!"
    echo "========================================"
    echo ""
    echo "Ubicación: ./docs/html/index.html"
    echo ""
    
    # Verificar advertencias
    if [ -f "doxygen_warnings.log" ] && [ -s "doxygen_warnings.log" ]; then
        echo "[AVISO] Se encontraron advertencias. Ver: doxygen_warnings.log"
        echo ""
    fi
    
    # Preguntar si abrir HTML
    read -p "¿Deseas abrir la documentación HTML? (s/n): " open
    if [[ $open =~ ^[Ss]$ ]]; then
        # Detectar sistema operativo
        if [[ "$OSTYPE" == "darwin"* ]]; then
            open docs/html/index.html
        elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
            xdg-open docs/html/index.html 2>/dev/null || echo "Abre manualmente: docs/html/index.html"
        fi
    fi
    
    echo ""
    # Preguntar si generar PDF
    read -p "¿Deseas generar el PDF? (Requiere LaTeX) (s/n): " genpdf
    if [[ $genpdf =~ ^[Ss]$ ]]; then
        echo ""
        echo "[INFO] Generando PDF..."
        cd docs/latex
        
        if command -v make &> /dev/null; then
            make
        else
            echo "[INFO] Usando pdflatex directamente..."
            pdflatex refman.tex
            makeindex refman.idx
            pdflatex refman.tex
            pdflatex refman.tex
        fi
        
        if [ -f "refman.pdf" ]; then
            echo "[OK] PDF generado: docs/latex/refman.pdf"
            read -p "¿Deseas abrir el PDF? (s/n): " openpdf
            if [[ $openpdf =~ ^[Ss]$ ]]; then
                if [[ "$OSTYPE" == "darwin"* ]]; then
                    open refman.pdf
                elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
                    xdg-open refman.pdf 2>/dev/null || echo "Abre manualmente: docs/latex/refman.pdf"
                fi
            fi
        else
            echo "[ERROR] No se pudo generar el PDF."
            echo "Verifica que LaTeX esté instalado correctamente."
        fi
        
        cd ../..
    fi
    
else
    echo ""
    echo "[ERROR] Falló la generación de documentación."
    echo "Revisa el archivo doxygen_warnings.log para más detalles."
    echo ""
    exit 1
fi

echo ""
echo "========================================"
echo " Proceso completado!"
echo "========================================"
echo ""
