package uz.veolia.cabinet.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_splash
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.ui.theme.primaryColor


@Composable
fun SplashScreen() {
//    val systemUiController = rememberSystemUiController()
//    SideEffect {
//        systemUiController.setStatusBarColor(primaryColor)
//        systemUiController.isStatusBarVisible = false
//        systemUiController.setNavigationBarColor(primaryColor)
//    }
    SplashContent()
}

@Composable
fun SplashContent() {
    Box(
        Modifier
            .fillMaxSize()
            .background(primaryColor), contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(Res.drawable.ic_splash), contentDescription = null)
    }
}

@Preview
@Composable
private fun SplashPreview() {
    SplashContent()
}
