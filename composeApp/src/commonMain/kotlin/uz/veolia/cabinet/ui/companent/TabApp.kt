package uz.veolia.cabinet.ui.companent

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.ui.theme.gray20Color
import uz.veolia.cabinet.ui.theme.primaryColor
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.gray15Color


@Composable
fun TabApp(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    onClick: (index: Int) -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, gray20Color, RoundedCornerShape(10.dp))
            .background(if (!enable) backgroundColor else White)
            .height(50.dp)
            .padding(5.dp)
    ) {
        val indicatorOffset: Dp by animateDpAsState(
            targetValue = maxWidth / items.size * selectedItemIndex,
            animationSpec = tween(easing = LinearEasing, durationMillis = 200), label = "",
        )

        MyTabIndicator(
            indicatorWidth = maxWidth / items.size,
            indicatorOffset = indicatorOffset,
            enable
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.mapIndexed { index, text ->
                MyTabItem(
                    onClick = {
                        onClick(index)
                    },
                    tabWidth = this@BoxWithConstraints.maxWidth / items.size,
                    text = text,
                    selected = index == selectedItemIndex,
                    clickable = enable
                )
            }
        }
    }
}

@Composable
private fun MyTabIndicator(
    indicatorWidth: Dp,
    indicatorOffset: Dp,
    selected: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(width = indicatorWidth)
            .offset(x = indicatorOffset)
            .background(
                color = if (selected) primaryColor else gray15Color,
                RoundedCornerShape(10.dp)
            )
    )
}


@Composable
private fun MyTabItem(
    onClick: () -> Unit,
    tabWidth: Dp,
    text: String,
    clickable : Boolean,
    selected : Boolean,
) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .clip(RoundedCornerShape(10.dp))
        .clickable(clickable) { onClick() }
        .width(tabWidth), contentAlignment = Alignment.Center) {
        TextApp(
            text = text,
            color = if (selected && clickable) White else appDark,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
fun TabPreview() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val (selected, setSelected) = remember {
            mutableIntStateOf(0)
        }

        TabApp(
            items = listOf("ЯТТ", "Физ. лицо", "Юр. лицо"),
            selectedItemIndex = selected,
            onClick = setSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

//
//fun List<String>.sumUntil(t: Int): Dp {
//    var sum = 0.dp
//    for (i in this.indices) {
//        sum += get(i).length.dp * 16 + 6.dp
//        if (i == t) return sum
//    }
//    return sum
//}