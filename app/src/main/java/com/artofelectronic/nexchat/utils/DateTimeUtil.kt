package com.artofelectronic.nexchat.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Formats a Firebase Timestamp for display in a chat.
 */
fun Long.toVerboseDate(): String {

    val date = Date(this)

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