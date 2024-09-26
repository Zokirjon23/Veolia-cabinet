package uz.veolia.cabinet.ui.screen.phone
//
//import android.content.ActivityNotFoundException
//import android.content.ClipData
//import android.content.ClipboardManager
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.IconButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import uz.veolia.cabinet.R
//import uz.veolia.cabinet.ui.companent.IconApp
//import uz.veolia.cabinet.ui.companent.TextApp
//import uz.veolia.cabinet.ui.theme.appDark
//import uz.veolia.cabinet.ui.theme.backgroundColor
//import uz.veolia.cabinet.ui.theme.gray50Color
//import uz.veolia.cabinet.ui.theme.gray15Color
//import uz.veolia.cabinet.ui.theme.gray70Color
//import uz.veolia.cabinet.ui.theme.lato_bold
//import uz.veolia.cabinet.ui.theme.whiteColor
//
//
//@Composable
//fun PersonalDataPhoneScreen(
//    login: String,
//    password: String,
//    passport: String,
//    pinfl: String,
//    inn: String,
//    cadastre: String,
//    name : String,
//    onBack: () -> Unit
//) {
//    val context = LocalContext.current
//    Column(
//        Modifier
//            .systemBarsPadding()
//            .fillMaxSize()
//            .clip(RoundedCornerShape(10.dp))
//            .background(backgroundColor)
//    ) {
//        Row(
//            Modifier
//                .padding(top = 6.dp)
//                .fillMaxWidth()
//                .height(64.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(
//                onClick = onBack,
//                Modifier.padding(horizontal = 4.dp)
//            ) {
//                IconApp(id = R.drawable.ic_back, tint = appDark)
//            }
//            Column {
//                TextApp(
//                    text = "Данные от ЛК",
//                    fontSize = 22.sp,
//                    fontFamily = lato_bold,
//                    lineHeight = 26.4.sp
//                )
//                TextApp(
//                    text = name,
//                    color = gray50Color,
//                    fontSize = 13.sp,
//                    lineHeight = 15.6.sp,
//                    maxLines = 1, overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier.padding(end = 16.dp)
//                )
//            }
//        }
//
//
//        Column(
//            Modifier
//                .fillMaxWidth()
//                .padding(vertical = 20.dp, horizontal = 16.dp)
//                .background(
//                    whiteColor,
//                    RoundedCornerShape(10.dp)
//                )
//                .padding(horizontal = 16.dp)
//                .padding(vertical = 20.dp),
//        ) {
//            TextApp(text = "Логин", color = gray70Color)
//            TextApp(text = login, color = appDark)
//            TextApp(
//                text = "Пароль",
//                color = gray70Color,
//                modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
//            )
//            TextApp(text = password.ifEmpty { "--------" }, color = appDark)
//
//            TextApp(text = "Паспорт", color = gray70Color, modifier = Modifier.padding(top = 14.dp))
//            TextApp(text = passport.ifEmpty { "--------" }, color = appDark)
//            TextApp(
//                text = "ПИНФЛ",
//                color = gray70Color,
//                modifier = Modifier.padding(top = 14.dp)
//            )
//            TextApp(text = pinfl.ifEmpty { "--------" }, color = appDark)
//
//            TextApp(
//                text = "ИНН",
//                color = gray70Color,
//                modifier = Modifier.padding(top = 14.dp)
//            )
//            TextApp(text = inn.ifEmpty { "--------" }, color = appDark)
//
//            TextApp(
//                text = "Кадастр",
//                color = gray70Color,
//                modifier = Modifier.padding(top = 14.dp)
//            )
//            TextApp(text = cadastre.ifEmpty { "--------" }, color = appDark)
//
////            TextFieldApp(title = "Логин", value = login, readOnly = true)
////            TextFieldApp(
////                title = "Паспорт",
////                value = passport,
////                readOnly = true,
////                modifier = Modifier.padding(vertical = 22.dp)
////            )
////
////            TextFieldApp(
////                title = "ИНН",
////                value = inn,
////                readOnly = true,
////                modifier = Modifier.padding(bottom = 22.dp)
////            )
////            TextFieldApp(title = "Пароль", value = password, readOnly = true)
////
////            TextFieldApp(
////                title = "ПИНФЛ",
////                value = pinfl,
////                readOnly = true,
////                modifier = Modifier.padding(vertical = 22.dp)
////            )
////
////            TextFieldApp(title = "Кадастр", value = cadastre, readOnly = true)
//        }
//        Spacer(modifier = Modifier.weight(1f))
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.White)
//        ) {
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Button(
//                    onClick = {
//                        val text =
//                            "Ссылка в кабинет: https://cabinet.veoliaenergy.uz/ \n Логин: $login \n Пароль: $passport \n Паспорт: $passport \n ПИНФЛ: $pinfl \n ИНН: $inn \n Кадастр: $cadastre"
//                        val clipboardManager =
//                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                        val clipData = ClipData.newPlainText("key", text)
//                        clipboardManager.setPrimaryClip(clipData)
//                        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
//                    },
//                    modifier = Modifier
//                        .weight(0.5f)
//                        .height(50.dp),
//                    shape = RoundedCornerShape(10.dp),
//                    colors = ButtonDefaults.buttonColors(gray15Color)
//                ) {
//                    IconApp(id = R.drawable.ic_copy, tint = appDark)
//                    TextApp(
//                        text = "Копировать",
//                        Modifier.padding(start = 11.dp),
//                        color = appDark
//                    )
//
//                }
//                Button(
//                    onClick = {
//                        val shareText =
//                            "Ссылка в кабинет: https://cabinet.veoliaenergy.uz/ \n Логин: $login \n Пароль: $password \n Паспорт: $passport \n ПИНФЛ: $pinfl \n ИНН: $inn \n Кадастр: $cadastre"
//                        val sendIntent = Intent().apply {
//                            action = Intent.ACTION_SEND
//                            putExtra(Intent.EXTRA_TEXT, shareText)
//                            type = "text/plain"
//                        }
//                        try {
//                            context.startActivity(sendIntent)
//                        } catch (e: ActivityNotFoundException) {
//                            Log.d("DDDDD", "PersonalDataContent: ${e.message}")
//                        }
//                    },
//                    modifier = Modifier
//                        .weight(0.5f)
//                        .height(50.dp),
//                    shape = RoundedCornerShape(10.dp),
//                    colors = ButtonDefaults.buttonColors(gray15Color)
//                ) {
//                    IconApp(id = R.drawable.ic_share, tint = appDark)
//                    TextApp(
//                        text = "Поделиться",
//                        Modifier.padding(start = 11.dp),
//                        color = appDark
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun PersonalDataPreview() {
//    PersonalDataPhoneScreen("2423", "2342", "23", "324234", "423", "423424232",""){}
//}
//
//
