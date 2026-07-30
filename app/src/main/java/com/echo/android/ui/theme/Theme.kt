package com.echo.android.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

// Echo color palette - sleek dark with cyan accent
private val EchoCyan = Color(0xFF00D4FF)          // Primary cyan from icon
private val EchoPurple = Color(0xFF8B5CF6)        // Secondary purple accent
private val EchoDarkBg = Color(0xFF0D0D12)        // Deep dark background
private val EchoDarkSurface = Color(0xFF16161D)  // Elevated surface
private val EchoTextLight = Color(0xFFE8E8EC)    // Soft white text
private val EchoTextMuted = Color(0xFF9CA3AF)    // Muted gray text

// Sleek dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = EchoCyan,
    onPrimary = Color.Black,
    secondary = EchoPurple,
    onSecondary = Color.White,
    tertiary = EchoPurple,
    background = EchoDarkBg,
    onBackground = EchoTextLight,
    surface = EchoDarkSurface,
    onSurface = EchoTextLight,
    surfaceVariant = Color(0xFF1E1E26),
    onSurfaceVariant = EchoTextMuted,
    outline = Color(0xFF2A2A35),
    error = Color(0xFFFF6B6B),
    onError = Color.White
)

// Minimal monochrome light scheme
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00A3CC),                  // Darker cyan for light mode
    onPrimary = Color.White,
    secondary = Color(0xFF7C3AED),                // Darker purple
    onSecondary = Color.White,
    tertiary = Color(0xFF7C3AED),
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFE5E7EB),
    error = Color(0xFFDC2626),
    onError = Color.White
)

@Composable
fun EchoTheme(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    // App-level override from ThemePreferenceManager
    val themePref by ThemePreferenceManager.themeFlow.collectAsState(initial = ThemePreference.System)
    val shouldUseDark = when (darkTheme) {
        true -> true
        false -> false
        null -> when (themePref) {
            ThemePreference.Dark -> true
            ThemePreference.Light -> false
            ThemePreference.System -> isSystemInDarkTheme()
        }
    }

    val colorScheme = if (shouldUseDark) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    SideEffect {
        (view.context as? Activity)?.window?.let { window ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    if (!shouldUseDark) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = if (!shouldUseDark) {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else 0
            }
            window.navigationBarColor = colorScheme.background.toArgb()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun BitchatTheme(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit
) = EchoTheme(darkTheme, content)

