# 🌐 Configuración de GitHub Pages para Documentación Doxygen

## 📋 Guía Paso a Paso

### 1️⃣ Preparar y Subir los Archivos

```powershell
# Añadir todos los archivos de documentación
git add docs/ Doxyfile MANUAL_DOXYGEN.md DOCS_README.md generar_docs.bat generar_docs.sh .nojekyll

# Crear commit
git commit -m "📚 Añadir documentación Doxygen completa y configuración GitHub Pages"

# Subir al repositorio
git push origin main
```

### 2️⃣ Configurar GitHub Pages

1. **Ir a tu repositorio en GitHub:**
   ```
   https://github.com/HectorLMF/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias
   ```

2. **Ir a Settings (Configuración):**
   - Haz clic en la pestaña **"Settings"** en la parte superior

3. **Activar GitHub Pages:**
   - En el menú lateral izquierdo, busca **"Pages"** (bajo "Code and automation")
   - En **"Source"**, selecciona: **Deploy from a branch**
   - En **"Branch"**, selecciona:
     * Branch: **main**
     * Folder: **/docs**
   - Haz clic en **"Save"**

4. **Esperar el despliegue:**
   - GitHub Pages tardará 1-3 minutos en construir tu sitio
   - Verás un mensaje azul que dice "Your site is ready to be published at..."
   - Cuando cambie a verde, ¡estará listo!

### 3️⃣ Acceder a tu Documentación

Tu documentación estará disponible en:
```
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/
```

Se redirigirá automáticamente a:
```
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/html/index.html
```

## 🔄 Actualizar la Documentación

Cada vez que hagas cambios en el código y quieras actualizar la documentación:

```powershell
# 1. Regenerar la documentación
doxygen Doxyfile
# O usar el script
.\generar_docs.bat

# 2. Añadir cambios
git add docs/

# 3. Commit
git commit -m "📝 Actualizar documentación"

# 4. Push
git push origin main
```

GitHub Pages se actualizará automáticamente en 1-3 minutos.

## 🎨 Personalización Adicional (Opcional)

### Añadir un README en la Página Principal

Puedes crear un `docs/README.md` que se mostrará en GitHub:

```markdown
# 📚 Documentación BiCIAM Framework

Documentación completa generada con Doxygen.

[Ver Documentación HTML](html/index.html)

## 📖 Contenido

- **106 clases Java** completamente documentadas
- **Algoritmos Metaheurísticos** implementados
- **Diagramas UML** de clases
- **Guías de uso** y ejemplos

## 🔗 Enlaces

- [Manual de Doxygen](../MANUAL_DOXYGEN.md)
- [Repositorio](https://github.com/HectorLMF/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias)
```

### Personalizar el Dominio (Opcional)

Si tienes un dominio personalizado:

1. Crea un archivo `docs/CNAME` con tu dominio:
   ```
   docs.tudominio.com
   ```

2. Configura los DNS de tu dominio apuntando a GitHub Pages

## ✅ Verificación

### Comprobar el Estado del Despliegue

1. Ve a la pestaña **"Actions"** en GitHub
2. Verás el workflow **"pages build and deployment"**
3. Si tiene un ✅ verde, está listo
4. Si tiene ❌ rojo, revisa los logs

### Probar Localmente Antes de Subir

```powershell
# Iniciar un servidor HTTP local
cd docs
python -m http.server 8000
# O con PowerShell
dotnet tool install --global dotnet-serve
dotnet serve -p 8000

# Abrir en navegador
start http://localhost:8000
```

## 🐛 Solución de Problemas

### Error 404

**Problema:** La página muestra "404 - File not found"

**Solución:**
1. Verifica que la carpeta `docs/` esté en la rama `main`
2. Verifica que exista `docs/index.html`
3. Espera 5 minutos después del push
4. Limpia caché del navegador (Ctrl + F5)

### Los Estilos No Se Cargan

**Problema:** La página se ve sin estilos CSS

**Solución:**
1. Verifica que `.nojekyll` exista en `docs/`
2. Limpia el caché de GitHub Pages:
   - Settings → Pages → "Unpublish site"
   - Espera 1 minuto
   - Vuelve a publicar

### Enlaces Rotos

**Problema:** Los enlaces internos no funcionan

**Solución:**
1. Verifica que las rutas en `Doxyfile` sean relativas
2. No uses `file://` en las URLs
3. Usa rutas relativas: `html/index.html` en vez de `/html/index.html`

## 📊 Estadísticas

Una vez publicado, puedes ver las estadísticas de visitas:
- Ve a **Insights** → **Traffic** en GitHub
- Verás vistas únicas, clones, y visitantes

## 🔐 Privacidad

### Repositorio Público
- La documentación será accesible por cualquiera

### Repositorio Privado
- Solo colaboradores pueden ver la documentación
- GitHub Pages en repos privados requiere GitHub Pro/Team/Enterprise

## 🎉 ¡Listo!

Una vez configurado, tu documentación estará disponible en:

**🌐 URL Principal:**
```
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/
```

**📚 Documentación Doxygen:**
```
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/html/index.html
```

Comparte este enlace con tu equipo, profesores, o en el README del proyecto! 🚀

---

**Última actualización:** Diciembre 2025  
**Proyecto:** BiCIAM Framework v1.0
