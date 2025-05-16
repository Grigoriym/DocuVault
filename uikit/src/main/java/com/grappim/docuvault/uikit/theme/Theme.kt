package com.grappim.docuvault.uikit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
//    primary = Dom_Aero,
//    primaryVariant = Dom_PrussianBlue,
//    secondary = Dom_ArcticLime,
//    background = Dom_White,
//    surface = Dom_White,
//    error = Dom_Bittersweet
)

private val LightColorPalette = lightColorScheme(
//    primary = Dom_Aero,
//    primaryVariant = Dom_PrussianBlue,
//    secondary = Dom_ArcticLime,
//    background = Dom_White,
//    surface = Dom_White,
//    error = Dom_Bittersweet
)

@Composable
fun DocuVaultTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = when {
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
