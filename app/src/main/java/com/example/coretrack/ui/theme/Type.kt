package com.example.coretrack.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.coretrack.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val Parkinsans = FontFamily(
    Font(R.font.parkinsans_extrabold, FontWeight.Black, FontStyle.Normal),


    Font(R.font.parkinsas_bold, FontWeight.Bold, FontStyle.Normal),

    Font(R.font.parkinsans_medium, FontWeight.Medium, FontStyle.Normal),


    Font(R.font.parkinsans_regular, FontWeight.Normal, FontStyle.Normal),


    Font(R.font.parkinsans_light, FontWeight.Normal, FontStyle.Normal),


    Font(R.font.parkinsas_semibold, FontWeight.Thin, FontStyle.Normal),

)