package uz.veolia.cabinet.ui.companent

import androidx.compose.foundation.Image
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconApp(
    resource: DrawableResource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current
) =
    Icon(painter = painterResource(resource), contentDescription, modifier, tint)

@Composable
fun ImageApp(
    id : DrawableResource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
){
    Image(painter = painterResource(id), contentDescription = contentDescription,modifier, alignment, contentScale, alpha, colorFilter)
}

//@Composable
//fun LoadImageAsync(
//    modifier: Modifier = Modifier,
//    url: String = "",
//    contentScale: ContentScale = ContentScale.Crop,
//    placeholderId: Int = R.drawable.place_holder,
//) {
//    AsyncImage(
//        model = ImageRequest.Builder(LocalContext.current)
//            .dispatcher(Dispatchers.IO)
//            .data(url)
//            .crossfade(true)
//            .diskCachePolicy(CachePolicy.ENABLED)
//            .memoryCachePolicy(CachePolicy.ENABLED)
//            .build(),
//        onError = {
//            Log.d("TTTT", "LoadImageAsync: ${it.result.throwable.message}")
//        },
//        placeholder = if (placeholderId != -1) painterResource(placeholderId) else null,
//        error = if (placeholderId != -1) painterResource(placeholderId) else null,
//        contentDescription = null,
//        contentScale = contentScale,
//        modifier = modifier
//    )
//}