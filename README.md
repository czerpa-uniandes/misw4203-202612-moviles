# VinilosApp

Aplicación móvil Android para la gestión y exploración de álbumes, artistas y coleccionistas de vinilos. Desarrollada en Kotlin con Jetpack Compose.

## Requisitos previos

| Herramienta | Versión mínima |
|-------------|---------------|
| Android Studio | Hedgehog (2023.1.1) o superior |
| JDK | 11 |
| Gradle | 9.3.1 (incluido vía wrapper) |
| Android SDK | API 24 (Android 7.0) |
| Emulador Android | API 24 o superior |

## Backend

La aplicación consume una API REST que debe estar corriendo en `localhost:3000` antes de lanzar la app, sin embargo, en caso de no tener el back en funcionamiento esto no es un stopper para que la aplicacion se ejecute.

La URL base configurada en `RetrofitProvider.kt` usa `http://10.0.2.2:3000/`, que es la dirección especial con la que el **emulador de Android** accede al `localhost` de la máquina host. No se debe modificar para uso con emulador.

## Construir el proyecto

### Desde Android Studio

1. Abrir Android Studio
2. Seleccionar **File → Open** y elegir la carpeta raíz del proyecto
3. Esperar a que Gradle sincronice las dependencias
4. Seleccionar **Build → Make Project** (`Cmd+F9` en macOS / `Ctrl+F9` en Windows)

### Desde la terminal

```bash
# macOS — usar el JDK de Android Studio
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"

# Construir APK de debug
./gradlew assembleDebug

# El APK generado queda en:
# app/build/outputs/apk/debug/app-debug.apk
```

## Ejecutar la aplicación

1. Iniciar un emulador desde **Device Manager** en Android Studio (API 24 o superior)
2. Presionar **Run** (`Shift+F10`) o ejecutar desde terminal:

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew installDebug
```

## Ejecutar las pruebas

### Unit tests

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew testDebugUnitTest
```

## Arquitectura

El proyecto implementa el patrón **MVVM + Repository + Service Adapter**:

```
UI (Compose)
  └── ViewModel (StateFlow)
        └── Repository (Result<T>)
              └── ServiceAdapter (interfaz)
                    └── ServiceAdapterImpl
                          └── Retrofit → API REST
```

| Capa | Ubicación | Responsabilidad |
|------|-----------|----------------|
| UI | `ui/screens/` | Composables, observa StateFlow |
| ViewModel | `ui/viewmodels/` | Estado, lógica de búsqueda |
| Repository | `data/repository/` | Combina fuentes, retorna `Result<T>` |
| Service Adapter | `data/adapters/` | Abstrae Retrofit del resto |
| Network | `data/network/` | Interfaz Retrofit + proveedor |
| Models | `data/models/` | Data classes del dominio |
