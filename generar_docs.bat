@echo off
REM ========================================
REM Script de Generación de Documentación
REM Proyecto: BiCIAM Framework
REM ========================================

echo.
echo ========================================
echo  Generacion de Documentacion BiCIAM
echo ========================================
echo.

REM Verificar si Doxygen está instalado
where doxygen >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Doxygen no esta instalado.
    echo.
    echo Por favor, instala Doxygen desde:
    echo https://www.doxygen.nl/download.html
    echo.
    echo O usa Chocolatey:
    echo   choco install doxygen.install
    echo.
    pause
    exit /b 1
)

echo [OK] Doxygen encontrado: 
doxygen --version
echo.

REM Limpiar documentación anterior
if exist "docs" (
    echo [INFO] Limpiando documentacion anterior...
    rmdir /s /q docs
    echo [OK] Limpieza completada.
    echo.
)

REM Generar documentación
echo [INFO] Generando documentacion...
echo.
doxygen Doxyfile

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo  Documentacion generada exitosamente!
    echo ========================================
    echo.
    echo Ubicacion: .\docs\html\index.html
    echo.
    
    REM Verificar advertencias
    if exist "doxygen_warnings.log" (
        for %%A in (doxygen_warnings.log) do set size=%%~zA
        if !size! GTR 0 (
            echo [AVISO] Se encontraron advertencias. Ver: doxygen_warnings.log
            echo.
        )
    )
    
    REM Preguntar si abrir HTML
    set /p open="Deseas abrir la documentacion HTML? (S/N): "
    if /i "%open%"=="S" (
        start "" "docs\html\index.html"
    )
    
    echo.
    REM Preguntar si generar PDF
    set /p genpdf="Deseas generar el PDF? (Requiere LaTeX) (S/N): "
    if /i "%genpdf%"=="S" (
        echo.
        echo [INFO] Generando PDF...
        cd docs\latex
        
        REM Verificar si existe make
        where make >nul 2>nul
        if %ERRORLEVEL% EQU 0 (
            make
        ) else (
            echo [INFO] Usando pdflatex directamente...
            pdflatex refman.tex
            makeindex refman.idx
            pdflatex refman.tex
            pdflatex refman.tex
        )
        
        if exist "refman.pdf" (
            echo [OK] PDF generado: docs\latex\refman.pdf
            set /p openpdf="Deseas abrir el PDF? (S/N): "
            if /i "!openpdf!"=="S" (
                start "" "refman.pdf"
            )
        ) else (
            echo [ERROR] No se pudo generar el PDF.
            echo Verifica que LaTeX este instalado correctamente.
        )
        
        cd ..\..
    )
    
) else (
    echo.
    echo [ERROR] Fallo la generacion de documentacion.
    echo Revisa el archivo doxygen_warnings.log para mas detalles.
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo  Proceso completado!
echo ========================================
echo.
pause
