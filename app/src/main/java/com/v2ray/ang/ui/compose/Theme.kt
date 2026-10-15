package com.v2ray.ang.ui.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.v2ray.ang.AppConfig
import com.v2ray.ang.handler.MmkvManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.compose.ui.graphics.Brush

private val LightColor = lightColorScheme(
    primary = Color(0xFFFF6600), // Fiery Neon Orange
    onPrimary = Color(0xFFFFFFFF), // White
    primaryContainer = Color(0xFFF0F2F7), // Light Silver Gray
    onPrimaryContainer = Color(0xFF16181F), // Dark Charcoal
    secondary = Color(0xFFFF7A00), // Electric Orange
    onSecondary = Color(0xFFFFFFFF), // White
    secondaryContainer = Color(0xFFE6E9F0), // Cool Silver Slate
    onSecondaryContainer = Color(0xFF14161D), // Dark Charcoal
    tertiary = Color(0xFF00C853), // Neon Emerald Green
    onTertiary = Color(0xFFFFFFFF), // White
    tertiaryContainer = Color(0xFFD7F9E4), // Light Green
    onTertiaryContainer = Color(0xFF003816), // Dark Green
    error = Color(0xFFBA1A1A), // Red
    errorContainer = Color(0xFFFFDAD6), // Light Red
    onError = Color(0xFFFFFFFF), // White
    onErrorContainer = Color(0xFF410002), // Dark Red
    background = Color(0xFFF6F7FA), // Soft Silver White
    onBackground = Color(0xFF121419), // Near Black
    surface = Color(0xFFFFFFFF), // White
    onSurface = Color(0xFF121419), // Near Black
    surfaceVariant = Color(0xFFE8EAF2), // Light Silver Slate
    onSurfaceVariant = Color(0xFF464956), // Dark Slate Gray
    outline = Color(0x55FF7A00), // Delicate Neon Orange
    outlineVariant = Color(0x28FF7A00), // Soft Neon Orange
    inverseSurface = Color(0xFF1E2129), // Dark Charcoal
    inverseOnSurface = Color(0xFFF4EFF4), // Very Light Gray
    inversePrimary = Color(0xFFFF9433), // Light Neon Orange
    scrim = Color(0xFF000000), // Black
    surfaceTint = Color(0xFFFF6600), // Orange
    surfaceContainerLowest = Color(0xFFFFFFFF), // White
    surfaceContainerLow = Color(0xFFF6F7FA), // Very Light Gray
    surfaceContainer = Color(0xFFECEEF5), // Light Gray
    surfaceContainerHigh = Color(0xFFE3E6EE), // Silver Gray
    surfaceContainerHighest = Color(0xFFD9DDE6), // Light Gray
)

private val DarkColor = darkColorScheme(
    primary = Color(0xFFFF7A00), // Fiery Neon Orange
    onPrimary = Color(0xFF000000), // Crisp Contrast
    primaryContainer = Color(0xFF261A10), // Charcoal with Fiery Ember Glow
    onPrimaryContainer = Color(0xFFFFB677), // Soft Neon Peach
    secondary = Color(0xFFFF8C1A), // Electric Flame Orange
    onSecondary = Color(0xFFFFFFFF), // White
    secondaryContainer = Color(0xFF1E212A), // Cool Charcoal Slate
    onSecondaryContainer = Color(0xFFE2E6F0), // Silver Metallic Accent
    tertiary = Color(0xFF00E676), // Bright Emerald Neon
    onTertiary = Color(0xFF003818), // Dark Green
    tertiaryContainer = Color(0xFF004D22), // Deep Green Slate
    onTertiaryContainer = Color(0xFFA0F8CA), // Light Emerald
    error = Color(0xFFFF4855), // Bright Neon Red
    errorContainer = Color(0xFF5C0E14), // Dark Burgundy
    onError = Color(0xFFFFFFFF), // White
    onErrorContainer = Color(0xFFFFDAD6), // Light Red
    background = Color(0xFF0D0E12), // Deep Matte Charcoal
    onBackground = Color(0xFFF2F4F8), // Silver Crisp White
    surface = Color(0xFF13151A), // Charcoal Slate
    onSurface = Color(0xFFECEFF5), // Silver White
    surfaceVariant = Color(0xFF1C1E26), // Elevated Cool Graphite
    onSurfaceVariant = Color(0xFFA2A8BA), // Refined Silver-Slate Muted Text
    outline = Color(0x66FF7A00), // Delicate Fiery Neon Orange Border
    outlineVariant = Color(0x28FF7A00), // Subtle Neon Ember Line
    inverseSurface = Color(0xFFE4E7EE), // Light Silver
    inverseOnSurface = Color(0xFF13151A), // Charcoal Slate
    inversePrimary = Color(0xFFFF7A00), // Neon Orange
    scrim = Color(0xFF000000), // Black
    surfaceTint = Color(0xFFFF7A00), // Neon Orange
    surfaceContainerLowest = Color(0xFF08090C), // Pure Deep Charcoal
    surfaceContainerLow = Color(0xFF101216), // Very Dark Slate
    surfaceContainer = Color(0xFF16181F), // Dark Charcoal Surface
    surfaceContainerHigh = Color(0xFF1F222B), // Silver-Tinted Charcoal
    surfaceContainerHighest = Color(0xFF282C37), // Charcoal Slate Border
)

// Gradients & Shimmer Brushes
val NeonOrangeBrush = Brush.linearGradient(
    listOf(Color(0xFFFFA040), Color(0xFFFF6600), Color(0xFFFF4500))
)
val SilverMetallicBrush = Brush.linearGradient(
    listOf(Color(0xFFFFFFFF), Color(0xFFD6DAE5), Color(0xFFA0A6B8))
)
val FieryGlowBrush = Brush.horizontalGradient(
    listOf(Color(0xFFFF8C1A), Color(0xFFFF5500), Color(0xFFFFA533))
)
val CharcoalCardGradient = Brush.verticalGradient(
    listOf(Color(0xFF1B1D25), Color(0xFF121419))
)

// Semantic Colors
val colorPing = Color(0xFF00E676) // Bright Emerald Neon
val colorPingAmber = Color(0xFFFFD600) // Neon Amber
val colorPingRed = Color(0xFFFF334B) // Neon Red
val colorConfigType = Color(0xFFFF851B) // Sleek Neon Orange
val colorFabActive = Color(0xFFFF7A00) // Neon Orange
val colorFabInactiveLight = Color(0xFF8E93A4) // Silver Slate
val colorFabInactiveDark = Color(0xFF383C48) // Charcoal Dark Slate
val dividerColorLight = Color(0x24FF7A00) // Subtle Neon Orange
val dividerColorDark = Color(0x38FF7A00) // Delicate Neon Orange Border

// Toast Colors 70%
val toastNormalBgLight = Color(0xB32C2F38) // Charcoal Gray
val toastNormalBgDark = Color(0xB31C1E26) // Deep Charcoal
val toastSuccessBg = Color(0xB300A84D) // Emerald
val toastErrorBg = Color(0xB3D50028) // Red
val toastInfoBg = Color(0xB3FF6600) // Fiery Orange
val toastIconCircleBg = Color(0x33FFFFFF) // Semi-transparent White
val toastTextColor = Color.White // White

object ThemeManager {
    private val _themeMode = MutableStateFlow(
        MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
    )
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _dynamicColorEnabled = MutableStateFlow(
        MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    )
    val dynamicColorEnabled: StateFlow<Boolean> = _dynamicColorEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        MmkvManager.encodeSettings(AppConfig.PREF_UI_MODE_NIGHT, mode)
        _themeMode.value = mode
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        MmkvManager.encodeSettings(AppConfig.PREF_DYNAMIC_COLOR, enabled)
        _dynamicColorEnabled.value = enabled
    }

    fun refresh() {
        _themeMode.value =
            MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
        _dynamicColorEnabled.value =
            MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    }
}

@Composable
fun resolveDarkTheme(): Boolean {
    val mode by ThemeManager.themeMode.collectAsState()
    return when (mode) {
        "1" -> false
        "2" -> true
        else -> isSystemInDarkTheme()
    }
}

val LocalDarkTheme = compositionLocalOf { false }

@Composable
fun AppTheme(
    darkTheme: Boolean = resolveDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicColor by ThemeManager.dynamicColorEnabled.collectAsState()
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColor
        else -> LightColor
    }
    val snackbarController = rememberAppSnackbarController()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppSnackbarBridge(controller = snackbarController)
                content()
                AppSnackbarHost(hostState = snackbarController.hostState)
            }
        }
    }
}
