package uz.veolia.cabinet.ui.companent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.ui.theme.primaryColor

@Composable
fun DatePickerDialogApp(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
//    initialDisplayMode : DisplayMode = DisplayMode.Picker
) {
//    val datePickerState = rememberDatePickerState(initialDisplayMode = initialDisplayMode)
//
//    val selectedDate = datePickerState.selectedDateMillis?.let {
//        convertMillisToDate(it)
//    } ?: ""
//
//
//    DatePickerDialog(
//        colors = DatePickerDefaults.colors(containerColor = Color.White),
//        onDismissRequest = onDismiss,
//        confirmButton = {
//            TextApp(
//                text = "Ок",
//                modifier = Modifier
//                    .padding(end = 20.dp, bottom = 20.dp)
//                    .clickable {
//                        onDateSelected(selectedDate)
//                        onDismiss()
//                    },
//                fontWeight = FontWeight.Bold
//            )
//        },
//        dismissButton = {
//            TextApp(
//                text = "Отмена",
//                modifier = Modifier
//                    .padding(end = 20.dp, bottom = 20.dp)
//                    .clickable {
//                        onDismiss()
//                    },
//                fontWeight = FontWeight.Bold,
//            )
//        },
//    ) {
//        DatePicker(
//            state = datePickerState, colors = DatePickerDefaults.colors(
//                selectedDayContainerColor = primaryColor,
//                selectedDayContentColor = Color.White,
//                containerColor = Color.White,
//                todayDateBorderColor = primaryColor,
//                todayContentColor = primaryColor
//            )
//        )
//    }
}

@Preview
@Composable
fun DatePickerDialogPreview() {
    Box(Modifier.fillMaxSize()) {
        DatePickerDialogApp(onDateSelected = { }, onDismiss = { })
    }
}

//
//private fun convertMillisToDate(millis: Long): String {
//    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale("uz"))
//    return formatter.format(java.util.Date(millis))
//}