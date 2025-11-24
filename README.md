# 💰 MiFinanzas - App de Control Financiero Personal

Una aplicación Android nativa moderna e intuitiva para llevar el control de tus finanzas personales y del hogar de manera fácil y proactiva.

## ✨ Características Principales

### 📊 Dashboard Inteligente
- **Balance Total**: Visualiza tu situación financiera de un vistazo
- **Resumen Mensual**: Ingresos y gastos del mes actual
- **Movimientos Recientes**: Acceso rápido a las últimas transacciones
- **Saludo Personalizado**: Buenos días/tardes/noches según la hora

### 💸 Gestión de Transacciones
- **Añadir Ingresos y Gastos**: Interfaz intuitiva con teclado numérico
- **12 Categorías Predefinidas**: Alimentación, Transporte, Entretenimiento, Salud, Educación, Compras, Servicios, Hogar, Salario, Inversiones, Regalos y Otros
- **Filtros Avanzados**: Por tipo (ingreso/gasto), categoría y búsqueda de texto
- **Navegación por Meses**: Revisa transacciones de cualquier período
- **Edición y Eliminación**: Modifica o elimina transacciones existentes

### 📈 Estadísticas Visuales
- **Gráfico de Dona**: Distribución de gastos por categoría
- **Comparativa Ingresos vs Gastos**: Balance mensual claro
- **Barras de Progreso**: Porcentaje por categoría con animaciones
- **Navegación Temporal**: Explora estadísticas de meses anteriores

### ⚙️ Configuración
- **Tema Oscuro/Claro**: Adapta la app a tu preferencia
- **Selección de Moneda**: ARS, USD, EUR, MXN, CLP, COP
- **Notificaciones**: Recordatorios diarios configurables
- **Exportar/Importar**: Respaldo de tus datos (próximamente)

## 🎨 Diseño UI/UX

### Paleta de Colores
- **Primary**: Emerald Green (#10B981) - Transmite crecimiento financiero
- **Ingresos**: Verde (#10B981)
- **Gastos**: Rojo (#EF4444)
- **Acentos**: Coral, Púrpura, Cyan, Rosa, Amarillo

### Componentes Visuales
- **Cards con Sombras Suaves**: Elevación sutil para profundidad
- **Bordes Redondeados**: 16-24dp para un look moderno
- **Animaciones Fluidas**: Transiciones de 300ms para feedback visual
- **Iconografía Material**: Icons consistentes de Material Design 3

### Navegación
- **Bottom Navigation Flotante**: Barra de navegación moderna con animación
- **Transiciones Suaves**: Fade y slide animations entre pantallas
- **Gestos Intuitivos**: Swipe y tap naturales

## 🛠️ Tecnologías Utilizadas

### Arquitectura
- **MVVM**: Model-View-ViewModel para separación de responsabilidades
- **Repository Pattern**: Abstracción de la capa de datos
- **StateFlow**: Manejo reactivo del estado de la UI

### Stack Técnico
- **Kotlin**: Lenguaje principal
- **Jetpack Compose**: UI declarativa moderna
- **Material Design 3**: Sistema de diseño actualizado
- **Room Database**: Persistencia local SQLite
- **Navigation Compose**: Navegación declarativa
- **Coroutines + Flow**: Programación asíncrona reactiva

### Dependencias Principales
```kotlin
// Compose BOM 2023.10.01
// Room 2.6.1
// Navigation Compose 2.7.5
// DataStore Preferences 1.0.0
// Splash Screen API 1.0.1
```

## 📱 Requisitos del Sistema

- **Android mínimo**: API 26 (Android 8.0 Oreo)
- **Android objetivo**: API 34 (Android 14)
- **Espacio**: ~15MB

## 🚀 Instalación

### Opción 1: Compilar desde código fuente

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd FinanzasApp
```

2. **Abrir en Android Studio**
   - Android Studio Hedgehog (2023.1.1) o superior
   - Gradle 8.2
   - JDK 17

3. **Sincronizar Gradle**
```bash
./gradlew build
```

4. **Ejecutar en dispositivo/emulador**
   - Conectar dispositivo con USB debugging
   - Click en "Run" o `Shift + F10`

### Opción 2: Instalar APK

1. Descargar el archivo `app-release.apk`
2. Habilitar "Instalar apps de fuentes desconocidas"
3. Abrir el APK e instalar

## 📂 Estructura del Proyecto

```
app/src/main/
├── java/com/finanzas/app/
│   ├── data/
│   │   ├── model/
│   │   │   ├── Transaction.kt      # Modelo de transacción
│   │   │   └── Budget.kt           # Modelo de presupuesto
│   │   ├── repository/
│   │   │   └── TransactionRepository.kt
│   │   ├── TransactionDao.kt       # Data Access Object
│   │   ├── BudgetDao.kt
│   │   ├── FinanzasDatabase.kt     # Base de datos Room
│   │   └── Converters.kt           # Type converters
│   ├── ui/
│   │   ├── theme/
│   │   │   ├── Color.kt            # Paleta de colores
│   │   │   ├── Type.kt             # Tipografía
│   │   │   └── Theme.kt            # Tema Material 3
│   │   ├── components/
│   │   │   └── CommonComponents.kt # Componentes reutilizables
│   │   ├── screens/
│   │   │   ├── HomeScreen.kt       # Pantalla principal
│   │   │   ├── HomeViewModel.kt
│   │   │   ├── TransactionsScreen.kt
│   │   │   ├── TransactionsViewModel.kt
│   │   │   ├── AddTransactionScreen.kt
│   │   │   ├── AddTransactionViewModel.kt
│   │   │   ├── StatsScreen.kt      # Estadísticas
│   │   │   └── SettingsScreen.kt   # Configuración
│   │   └── Navigation.kt           # Navegación principal
│   ├── utils/
│   │   └── FormatUtils.kt          # Utilidades de formato
│   ├── FinanzasApplication.kt      # Application class
│   └── MainActivity.kt             # Activity principal
└── res/
    ├── values/
    │   ├── strings.xml             # Textos en español
    │   ├── colors.xml              # Colores
    │   └── themes.xml              # Tema claro
    ├── values-night/
    │   └── themes.xml              # Tema oscuro
    └── drawable/
        ├── ic_launcher_*.xml       # Iconos de app
        └── ic_splash.xml           # Splash screen
```

## 🔮 Próximas Características

- [ ] **Presupuestos por Categoría**: Establece límites mensuales
- [ ] **Alertas Inteligentes**: Notificaciones cuando superes el presupuesto
- [ ] **Gráficos de Tendencia**: Evolución de gastos en el tiempo
- [ ] **Transacciones Recurrentes**: Pagos automáticos mensuales
- [ ] **Múltiples Cuentas**: Separa finanzas personales y del hogar
- [ ] **Exportación a Excel/CSV**: Backup de datos
- [ ] **Sincronización en la Nube**: Backup en Google Drive
- [ ] **Widgets**: Acceso rápido desde el home screen
- [ ] **Modo Familia**: Compartir finanzas del hogar

## 📄 Licencia

Este proyecto es de código abierto bajo la licencia MIT.

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Por favor:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

**Desarrollado con ❤️ para ayudarte a tomar control de tus finanzas**
