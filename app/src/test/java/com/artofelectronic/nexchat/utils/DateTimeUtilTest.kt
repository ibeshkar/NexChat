package com.artofelectronic.nexchat.utils

import com.google.firebase.Timestamp
import org.junit.Test
import java.util.Calendar
import java.util.Date


class DateTimeUtilTest {

    @Test
    fun `toVerboseDate should return time 'hh mm a' for today's date`() {
        // Create a timestamp for today at 10:30 AM
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 30)
        }
        val timestamp = Timestamp(Date(calendar.timeInMillis))
        val expected = "10:30 AM"
        val actual = timestamp.toVerboseDate()
        assert(actual == expected)
    }

    @Test
    fun `toVerboseDate should return 'Yesterday' for yesterday's date`() {
        // Create a timestamp for yesterday at 10:30 AM
        val date = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 30)
        }
        val timestamp = Timestamp(Date(date.timeInMillis))
        val expected = "Yesterday"
        val actual = timestamp.toVerboseDate()
        assert(actual == expected)
    }

    @Test
    fun `toVerboseDate should return 'dd MM yyyy' for older dates`() {
        // Create a timestamp for a date in the past, e.g. Jan 10, 2022
        val date = Calendar.getInstance().apply {
            set(2022, Calendar.JANUARY, 10)
        }
        val timestamp = Timestamp(Date(date.timeInMillis))
        val expected = "10/1/2022"
        val actual = timestamp.toVerboseDate()
        assert(actual == expected)
    }
}