# 🚀 Configurar GitHub Pages para Documentación Doxygen

Tu documentación ya está en el repositorio. Ahora solo necesitas **activar GitHub Pages** desde la web de GitHub.

## 📋 Pasos a Seguir

### 1️⃣ Ve a la configuración del repositorio

Abre tu navegador y ve a:
```
https://github.com/HectorLMF/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/settings/pages
```

O manualmente:
1. Ve a tu repositorio: https://github.com/HectorLMF/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias
2. Click en **Settings** (⚙️)
3. Click en **Pages** (en el menú lateral izquierdo)

### 2️⃣ Configura GitHub Pages

En la página de GitHub Pages, configura lo siguiente:

#### 🔹 Source (Origen)
- **Source**: Selecciona `Deploy from a branch`

#### 🔹 Branch (Rama)
- **Branch**: Selecciona `main`
- **Folder**: Selecciona `/docs` 
- Click en **Save** (Guardar)

![Configuración GitHub Pages](https://docs.github.com/assets/cb-47267/mw-1440/images/help/pages/publishing-source-folder-drop-down.webp)

### 3️⃣ Espera el despliegue

GitHub comenzará a construir tu sitio automáticamente. Esto toma **1-3 minutos**.

Verás un mensaje como:
```
✅ Your site is published at https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/
```

### 4️⃣ Accede a tu documentación

Una vez desplegado, tu documentación estará disponible en:

**🌐 URL Principal:**
```
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/
```

Esta URL te redirigirá automáticamente a:
```
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/html/index.html
```

## ✅ Verificación

Para verificar que todo funciona:

1. **Espera 3 minutos** después de guardar la configuración
2. Abre la URL en tu navegador
3. Deberías ver la página principal de la documentación Doxygen
4. Navega por las clases, archivos y jerarquías

## 🔄 Actualizaciones Futuras

Cada vez que hagas cambios en el código y quieras actualizar la documentación:

```bash
# 1. Regenera la documentación
doxygen Doxyfile

# 2. Haz commit y push
git add docs/
git commit -m "📚 Actualizar documentación"
git push origin main
```

GitHub Pages se actualizará automáticamente en 1-3 minutos.

## 📱 Compartir la Documentación

Puedes compartir tu documentación con:
- **Profesores**: Para evaluación del proyecto
- **Compañeros**: Para colaboración
- **Portfolio**: En tu CV o LinkedIn

Simplemente comparte este enlace:
```
https://hectorlmf.github.io/LAB--P5-Gesti-n-de-Proyecto-y-Pruebas-Unitarias/
```

## 🆘 Solución de Problemas

### ❌ Error 404
Si ves error 404:
- Espera 5 minutos más (el primer despliegue puede tardar)
- Verifica que la rama sea `main` y la carpeta `/docs`
- Refresca la página con Ctrl+F5

### ❌ No aparece la documentación
Si la página carga pero no se ve bien:
- Verifica que exista el archivo `docs/index.html`
- Verifica que exista el archivo `docs/.nojekyll`
- Limpia la caché del navegador

### ❌ Los estilos no cargan
Si ves solo texto sin formato:
- Verifica que todos los archivos estén en `docs/html/`
- Usa el enlace completo con `/html/index.html`

## 📚 Archivos Importantes

Tu repositorio ahora incluye:

```
📁 docs/                        # Documentación HTML generada
   ├── index.html              # Página de redirección
   ├── .nojekyll               # Desactiva Jekyll
   └── html/                   # Documentación Doxygen
       └── index.html          # Página principal

📁 src/main/java/              # Código fuente documentado
📄 Doxyfile                    # Configuración Doxygen
📄 MANUAL_DOXYGEN.md          # Manual de usuario
📄 DOCS_README.md             # Guía rápida
📄 GITHUB_PAGES_SETUP.md      # Guía detallada de setup
📄 generar_docs.bat           # Script Windows
📄 generar_docs.sh            # Script Linux/Mac
```

## 🎯 Resumen

1. ✅ Código documentado (106 archivos)
2. ✅ Documentación generada (Doxygen)
3. ✅ Archivos subidos a GitHub
4. 🔄 **PENDIENTE**: Activar GitHub Pages (tú debes hacerlo manualmente)
5. ⏳ Esperar despliegue (1-3 minutos)
6. 🎉 ¡Documentación online!

---

**Última actualización**: 2025
**Autor**: BiCIAM Team
**Generado con**: Doxygen 1.15.0
