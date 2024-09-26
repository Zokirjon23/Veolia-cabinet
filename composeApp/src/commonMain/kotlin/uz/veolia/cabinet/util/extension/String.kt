package uz.veolia.cabinet.util.extension

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import okio.ByteString.Companion.decodeBase64


fun String.toCurrencyFormat(): String {
    /**
     *  this is for Int and Float
     * */
    if (this.toIntOrNull() != null) {
        val str = StringBuilder()
        val size = this.length
        val array = this.toCharArray()
        for (i in size - 1 downTo 0) {
            str.append(array[size - i - 1])
            if (i != 0 && (size - 1 - (size - 1 - i)) % 3 == 0) {
                str.append(" ")
            }
        }
        return str.toString().trim()
    } else {
        if (this.toFloatOrNull() != null) {
            val form = this.toFloat().toInt().toString()
            val str = StringBuilder()
            val size = form.length
            val array = form.toCharArray()
            for (i in size - 1 downTo 0) {
                str.append(array[size - i - 1])
                if (i != 0 && (size - 1 - (size - 1 - i)) % 3 == 0) {
                    str.append(" ")
                }
            }
            str.append(
                (this.toFloat() - this.toFloat().toInt()).toString()
                    .filterIndexed { index, _ -> index != 0 })
            return str.toString().trim()
        } else {
            return ""
        }
    }
}

fun String?.toMonth(): String {
    if (this.isNullOrEmpty()) return ""
    val list = listOf(
        "Янв",
        "Фев",
        "Март",
        "Апр",
        "Май",
        "Июнь",
        "Июль",
        "Авг",
        "Сент",
        "Октб",
        "Нояб",
        "Дек"
    )
    val date = this.split("-")
    if (date.isEmpty()) return ""
    val month = date.last().toIntOrNull() ?: return ""
    return list[month - 1] + " ${date.first()}"
}

fun String?.toDividedFormat(): String {
    return this?.let {
        if (it.length > 4) {
            it.substring(0, it.length - 3)
                .toCurrencyFormat() + it.substring(
                it.length - 3,
                it.length
            )
        } else {
            it
        }
    } ?: ""
}

fun String?.toDate() : String {
    val instant = Instant.parse(this.toString())
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.year.toString()
    return "$day.$month.$year"
}


fun String.decodeJwtPayload(): String {
    val parts = this.split(".")
    if (parts.size < 2) throw IllegalArgumentException("Invalid JWT format")
    val payloadBase64 = parts[1]
    val decodedPayload = payloadBase64.decodeBase64() ?: throw IllegalArgumentException("Invalid Base64 string")
    return decodedPayload.utf8()
}