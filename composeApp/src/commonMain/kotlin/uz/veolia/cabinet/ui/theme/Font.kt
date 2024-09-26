package uz.veolia.cabinet.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.lato_bold
import kotlinproject.composeapp.generated.resources.lato_regular
import org.jetbrains.compose.resources.Font


val lato_regular : FontFamily @Composable get(){
    return FontFamily(Font(resource = Res.font.lato_regular))
}
val lato_bold : FontFamily @Composable get(){
    return FontFamily(Font(Res.font.lato_bold))
}