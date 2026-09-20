# Instrucciones de compilación y ejecución

Este documento reúne los pasos para ejecutar las tres versiones Android del Catálogo UI.

## Requisitos

- Android Studio con JDK 17 y Android SDK Platform 37.
- Un emulador Android o teléfono físico con depuración USB autorizada.
- Flutter SDK compatible con Dart `^3.13.4` para la carpeta `flutter/`.

Desde PowerShell se puede confirmar la conexión del dispositivo con:

```powershell
adb devices
```

El equipo debe aparecer con el estado `device`. Si aparece `unauthorized`, se debe aceptar la huella RSA en la pantalla del teléfono y ejecutar el comando de nuevo.

## Jetpack Compose

1. Abrir `android-compose/` en Android Studio y esperar a que termine la sincronización de Gradle.
2. Elegir un dispositivo Android y ejecutar la configuración `app` desde el IDE.

Para compilar desde la terminal, dentro de `android-compose/`:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
```

El APK de depuración se genera en `android-compose/app/build/outputs/apk/debug/app-debug.apk`.

## Views y XML

1. Abrir `android-views/` en Android Studio y permitir que Gradle sincronice el proyecto.
2. Seleccionar el módulo `app`, un dispositivo Android y ejecutar la aplicación.

Para compilar y ejecutar pruebas desde la terminal, dentro de `android-views/`:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
```

El APK de depuración se genera en `android-views/app/build/outputs/apk/debug/app-debug.apk`.

## Flutter

Desde la carpeta `flutter/`, ejecutar:

```powershell
flutter doctor
flutter pub get
flutter analyze
flutter test
flutter run
```

`flutter run` muestra los dispositivos disponibles; para elegir uno concreto se puede usar `flutter run -d <id-del-dispositivo>`.

Para generar el APK de depuración:

```powershell
flutter build apk --debug
```

El resultado se encuentra en `flutter/build/app/outputs/flutter-apk/app-debug.apk`.

## Verificación mínima

En cada versión se debe comprobar lo siguiente antes de tomar capturas:

- Inicio abre las seis secciones del catálogo.
- Un texto válido agregado desde Entrada aparece en Listas o Colecciones.
- Los controles de selección responden y los deslizadores muestran un rango de 0 a 100.
- La eliminación de una colección puede deshacerse.
- Diálogo, hoja inferior, imágenes y barras de progreso se muestran en Información.
- La aplicación se adapta al tema claro u oscuro configurado en el sistema.

Las capturas de la entrega se almacenan en `docs/`.
