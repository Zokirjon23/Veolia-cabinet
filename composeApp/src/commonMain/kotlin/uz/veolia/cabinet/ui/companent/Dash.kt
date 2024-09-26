package uz.veolia.cabinet.ui.companent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.veolia.cabinet.ui.theme.gray70Color

@Composable
fun DashedDivider(
    thickness: Dp,
    color: Color = gray70Color.copy(0.25f),
    phase: Float = 1f,
    intervals: FloatArray = floatArrayOf(4f, 4f),
    modifier: Modifier
) {
    Canvas(
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        val dividerHeight = thickness.toPx()
        drawRoundRect(
            color = color,
            style = Stroke(
                width = dividerHeight,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = intervals,
                    phase = phase
                )
            )
        )
    }
}