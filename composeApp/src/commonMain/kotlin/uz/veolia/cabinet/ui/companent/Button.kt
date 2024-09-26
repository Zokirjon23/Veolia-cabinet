package uz.veolia.cabinet.ui.companent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_add_fab
import kotlinproject.composeapp.generated.resources.ic_cancel
import kotlinproject.composeapp.generated.resources.ic_setting
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.primaryColor
import uz.veolia.cabinet.ui.theme.whiteColor

@Composable
fun ButtonIcon(idRes: DrawableResource, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(40.dp)
            .background(backgroundColor)
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        IconApp(idRes)
    }
}

@Composable
fun ButtonIcon(idRes: DrawableResource, modifier: Modifier = Modifier, onClick: () -> Unit, count: Int = 0) {
    Box(modifier = modifier.size(42.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(CircleShape)
                .size(40.dp)
                .background(backgroundColor)
                .clickable { onClick() }
        ) {
            IconApp(
                idRes,
                tint = if (count > 0) primaryColor else LocalContentColor.current,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (count > 0)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(16.dp)
                    .background(primaryColor, CircleShape)
            ) {
                TextApp(
                    lineHeight = 12.sp,
                    text = count.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 12.sp,
                    color = whiteColor
                )
            }
    }
}

@Preview
@Composable
private fun ButtonIconAppPreview() {
    ButtonIcon(
        idRes = Res.drawable.ic_setting,
        onClick = { }, count = 2
    )
}

@Composable
fun FabApp(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        backgroundColor = primaryColor
    ) {
        IconApp(resource = Res.drawable.ic_add_fab, tint = Color.White)
    }
}

@Composable
fun IconButtonApp(
    modifier: Modifier = Modifier,
    resId: DrawableResource,
    tint: Color = LocalContentColor.current,
    description: String? = null,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier, enabled = enable) {
        IconApp(resource = resId, tint = tint, contentDescription = description)
    }
}

@Composable
fun ButtonApp(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor,
        disabledContainerColor = primaryColor.copy(0.5f)
    ),
    elevation: ButtonElevation? = ButtonDefaults.elevatedButtonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textColor: Color = whiteColor,
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = androidx.compose.material3.ButtonDefaults.elevatedButtonElevation(),
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        TextApp(text = text, color = textColor, fontSize = 13.sp)
    }
}


@Composable
fun CancelIcon(modifier: Modifier = Modifier, size: Dp = 24.dp, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .size(size)
            .background(appDark),
        contentAlignment = Alignment.Center
    ) {
        IconApp(
            resource = Res.drawable.ic_cancel,
            tint = whiteColor
        )
    }
}