package com.artofelectronic.nexchat.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * Formats a Firebase Timestamp for display in a chat.
 */
fun Timestamp.toVerboseDate(): String {

    val date = this.toDate()

    // Get today's date and yesterday's date using Calendar
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_YEAR)
    calendar.add(Calendar.DAY_OF_YEAR, -1)  // Move to yesterday
    val yesterday = calendar.get(Calendar.DAY_OF_YEAR)

    // Create a Calendar instance for the timestamp's date
    val timestampCalendar = Calendar.getInstance().apply {
        time = date
    }

    // Format the time in AM/PM format
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val timeString = timeFormat.format(date)

    return when {
        timestampCalendar.get(Calendar.DAY_OF_YEAR) == today -> timeString // Today: return time with AM/PM
        timestampCalendar.get(Calendar.DAY_OF_YEAR) == yesterday -> "Yesterday" // Yesterday: return "Yesterday"
        else -> {
            // Otherwise: return date in d/M/yyyy format
            val dateFormatter = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            dateFormatter.format(date)
        }
    }
}


/**
 * Converts a Firebase Timestamp to a LocalDate.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Timestamp.toLocalDate(): LocalDate = this.toDate()
    .toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDate()


/**
 * Converts a LocalDate to a verbose label (e.g., "Today", "Yesterday", or "dd/MM/yyyy").
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toVerboseLabel(): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (this) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> this.format(DateTimeFormatter.ofPattern("d/M/yyyy"))
    }
}



/**
 * Checks if a Firebase Timestamp is from today.
 */
fun Timestamp.isToday(): Boolean {
    val date = this.toDate()

    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_YEAR)

    val timestampCalendar = Calendar.getInstance().apply {
        time = date
    }

    return timestampCalendar.get(Calendar.DAY_OF_YEAR) == today
}


/**
 * Checks if a Firebase Timestamp is from yesterday.
 */
fun Timestamp.isYesterday(): Boolean {
    val date = this.toDate()

    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1)  // Move to yesterday
    val yesterday = calendar.get(Calendar.DAY_OF_YEAR)

    val timestampCalendar = Calendar.getInstance().apply {
        time = date
    }

    return timestampCalendar.get(Calendar.DAY_OF_YEAR) == yesterday
}

/**
 * Converts a Firebase Timestamp to a string in the format "dd MMM yyyy".
 */
fun Timestamp.toLocalDateString(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(this.toDate())
}

/**
 * Converts a Firebase Timestamp to a string in the format "hh:mm a".
 */
fun Timestamp.toTimeOnly(): String {
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(this.toDate())
}