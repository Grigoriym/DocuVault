package com.grappim.docuvault.uikit.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.grappim.docuvault.uikit.R

private val fonts = FontFamily(
    Font(R.font.montserrat_light_300, FontWeight.Light),
    Font(R.font.montserrat_regular_400, FontWeight.Normal),
    Font(R.font.montserrat_bold_700, FontWeight.Bold)
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    )
)
