package uz.veolia.cabinet.ui.companent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.error_connection_server
import kotlinproject.composeapp.generated.resources.ic_done
import kotlinproject.composeapp.generated.resources.ic_warning
import kotlinproject.composeapp.generated.resources.success
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.errorColor
import uz.veolia.cabinet.ui.theme.successColor
import uz.veolia.cabinet.ui.theme.success_background_color
import kotlin.math.roundToInt

@Composable
fun ToastApp(
    modifier: Modifier = Modifier,
    text: String = stringResource(Res.string.success),
    iconId: DrawableResource = Res.drawable.ic_done,
    color: Color = successColor,
    containerColor : Color = success_background_color,
    enter: EnterTransition = slideInVertically(
        initialOffsetY = { -40 }
    ) + expandVertically(
        expandFrom = Alignment.Top
    ) + scaleIn(
        transformOrigin = TransformOrigin(0.5f, 0f)
    ) + fadeIn(initialAlpha = 0.3f),
    exit: ExitTransition = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(
        targetScale = 1.2f
    ),
    onDismiss: () -> Unit = {}
) {
    val isVisible = remember { mutableStateOf(false) }
    val offsetX = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(key1 = Unit) {
        delay(10)
        isVisible.value = true
        delay(3000)
        isVisible.value = false
        onDismiss()
    }

    AnimatedVisibility(
        modifier = modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .offset { IntOffset(offsetX.floatValue.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectDragGestures { _, _ ->
                    isVisible.value = false
                }
            },
        visible = isVisible.value,
        exit = exit,
        enter = enter
    ) {
        Box(modifier = Modifier.background(backgroundColor, RoundedCornerShape(10.dp))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(containerColor, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 12.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconApp(resource = iconId, tint = color)
                TextApp(
                    text = text,
                    modifier = Modifier.padding(start = 5.dp),
                    color = color,
                    lineHeight = 18.62.sp,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Composable
fun ToastError(
    modifier: Modifier = Modifier,
    text: String = stringResource(Res.string.error_connection_server),
    enter: EnterTransition = slideInVertically(
        initialOffsetY = { -40 }
    ) + expandVertically(
        expandFrom = Alignment.Top
    ) + scaleIn(
        transformOrigin = TransformOrigin(0.5f, 0f)
    ) + fadeIn(initialAlpha = 0.3f),
    exit: ExitTransition = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(
        targetScale = 1.2f
    ),
    isInfinitive: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    val isVisible = remember { mutableStateOf(false) }
    val offsetX = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(key1 = Unit) {
        delay(10)
        isVisible.value = true
        if (!isInfinitive) {
            delay(if (text.length < 15) 3000L else (text.length * 100).toLong())
            isVisible.value = false
            onDismiss()
        }
    }

    AnimatedVisibility(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .offset { IntOffset(offsetX.floatValue.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectDragGestures { _, _ ->
                    isVisible.value = false
                }
            },
        visible = isVisible.value,
        exit = exit,
        enter = enter
    ) {
        Box(modifier = Modifier.background(backgroundColor, RoundedCornerShape(10.dp))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(errorColor.copy(0.15f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 12.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconApp(resource = Res.drawable.ic_warning, tint = errorColor)
                TextApp(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp),
                    color = errorColor,
                    fontSize = 15.sp
                )
            }
        }
    }
}

