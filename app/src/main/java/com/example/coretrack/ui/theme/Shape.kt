import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape

val SingleWaveShape = GenericShape { size, _ ->
    val waveHeight = 40f
    val waveWidth = size.width


    lineTo(0f, size.height - waveHeight)


    quadraticTo(
        waveWidth / 2, size.height + waveHeight,
        waveWidth, size.height - waveHeight
    )


    lineTo(size.width, 0f)
    close()
}

