package com.grappim.docuvault.uikit.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColors(
    primary = Dom_Aero,
    primaryVariant = Dom_PrussianBlue,
    secondary = Dom_ArcticLime,
    background = Dom_White,
    surface = Dom_White,
    error = Dom_Bittersweet
)

private val LightColorPalette = lightColors(
    primary = Dom_Aero,
    primaryVariant = Dom_PrussianBlue,
    secondary = Dom_ArcticLime,
    background = Dom_White,
    surface = Dom_White,
    error = Dom_Bittersweet
)

@Composable
fun DocuVaultTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = when {
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colors = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
