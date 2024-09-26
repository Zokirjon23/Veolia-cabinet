package uz.veolia.cabinet.ui.companent
//
//import android.Manifest
//import android.content.Context
//import android.icu.text.SimpleDateFormat
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.platform.LocalContext
//import androidx.core.content.FileProvider
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.rememberPermissionState
//import uz.veolia.cabinet.BuildConfig
//import java.io.File
//import java.util.Date
//import java.util.Locale
//import java.util.Objects
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun ImagePicker() {
//    val context = LocalContext.current
//    var currentPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
//    val file = context.createImageFile()
//    val uri = FileProvider.getUriForFile(
//        Objects.requireNonNull(context),
//        BuildConfig.APPLICATION_ID + ".provider", file
//    )
//
//    val cameraLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicture(),
//        onResult = { success ->
//            if (success) currentPhotoUri = uri
//        }
//    )
//
//    val cameraPermissionState = rememberPermissionState(
//        permission = Manifest.permission.CAMERA,
//        onPermissionResult = { granted ->
//            if (granted) {
//                cameraLauncher.launch(uri)
//            } else print("camera permission is denied")
//        }
//    )
//}
//
//fun Context.createImageFile(): File {
//    // Create an image file name
//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("uz")).format(Date())
//    val imageFileName = "JPEG_" + timeStamp + "_"
//    return File.createTempFile(
//        imageFileName, /* prefix */
//        ".jpg", /* suffix */
//        externalCacheDir /* directory */
//    )
//}