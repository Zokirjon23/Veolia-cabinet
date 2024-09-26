package uz.veolia.cabinet.ui.companent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import uz.veolia.cabinet.ui.theme.primaryColor

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun BoxScope.CircularProgressIndicatorApp(
    modifier: Modifier = Modifier,
    color: Color = primaryColor,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    trackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
) {

//    CircularProgressIndicator(
//        modifier.align(Alignment.Center), color, strokeWidth, trackColor, strokeCap
//    )
    AdaptiveCircularProgressIndicator(modifier = modifier.align(Alignment.Center)) {
        material {
            this.strokeWidth = strokeWidth
            this.color = color
            this.trackColor = trackColor
            this.strokeCap = strokeCap
        }

        cupertino {
            this.color = primaryColor
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxWithSwipeRefresh(
    onSwipe: () -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val refresh = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onSwipe)
    Box(modifier = modifier.pullRefresh(refresh)) {
        content()
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refresh,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
//    val state = rememberPullToRefreshState()
//
//    if (state.isRefreshing) {
//        LaunchedEffect(true) {
//            onSwipe()
//        }
//    }
//
//    if (!isRefreshing) {
//        LaunchedEffect(true) {
//            state.endRefresh()
//        }
//    }
//
//    Box(modifier = modifier.nestedScroll(state.nestedScrollConnection)) {
//        content()
//        PullToRefreshContainer(
//            modifier = Modifier.align(Alignment.TopCenter),
//            state = state,
//        )
//    }
}