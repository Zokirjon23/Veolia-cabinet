package uz.veolia.cabinet.util.extension

import kotlinx.datetime.*

fun String.time(): String {
    val instant = Instant.parse(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.year.toString()
    return "$day.$month.$year"

}

fun Long.toFormattedDateString(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val monthName = when (localDateTime.month) {
        Month.JANUARY -> "Янв"
        Month.FEBRUARY -> "Фев"
        Month.MARCH -> "Мар"
        Month.APRIL -> "Апр"
        Month.MAY -> "Май"
        Month.JUNE -> "Июн"
        Month.JULY -> "Июл"
        Month.AUGUST -> "Авг"
        Month.SEPTEMBER -> "Сен"
        Month.OCTOBER -> "Окт"
        Month.NOVEMBER -> "Ноя"
        Month.DECEMBER -> "Дек"
        else -> ""
    }
    return "$day $monthName"
}

fun Long.isToday(): Boolean {
    val instant = Instant.fromEpochMilliseconds(this)
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return date == today
}

fun Long.isYesterday(): Boolean {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val startOfToday = today.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    val startOfYesterday = today.minus(1, DateTimeUnit.DAY)
        .atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    return this in startOfYesterday until startOfToday
}

fun Long.isFirstDayOfCurrentMonth(): Boolean {
    val date = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstDayOfCurrentMonth = LocalDate(now.year, now.month, 1)
    return date == firstDayOfCurrentMonth
}

fun getFirstDayOfCurrentMonthInMillis(): Long {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstDayOfMonth = LocalDate(currentDate.year, currentDate.monthNumber, 1)
    val firstDayStartOfDay = firstDayOfMonth.atStartOfDayIn(TimeZone.currentSystemDefault())
    return firstDayStartOfDay.toEpochMilliseconds()
}

fun getCurrentDayInMillis(): Long {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val startOfDay = currentDate.atStartOfDayIn(TimeZone.currentSystemDefault())
    return startOfDay.toEpochMilliseconds()
}

fun getYesterdayInMillis(): Long {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    return yesterday.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun Long.toFormattedDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.year.toString()

    return "$day.$month.$year"
}

fun getMiddleOfDayInMillis(epochMillis: Long): Long {
    // Convert the provided epochMillis to a LocalDateTime in the system's default timezone
    val localDateTime = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.currentSystemDefault())

    // Get the middle of the day (12:00 PM) in the system's default timezone
    val middleOfDay = localDateTime.date.atTime(12, 0).toInstant(TimeZone.currentSystemDefault())

    // Convert to epoch milliseconds
    return middleOfDay.toEpochMilliseconds()
}

fun getCurrentYear(): Int {
    val currentInstant = Clock.System.now()
    val currentDateTime = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    return currentDateTime.year
}