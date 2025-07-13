//package com.artofelectronic.nexchat.utils
//
//import com.google.firebase.Timestamp
//import org.junit.Test
//import java.util.Calendar
//import java.util.Date
//
//
//class DateTimeUtilTest {
//
//    @Test
//    fun `formatTimestampForChat should return time for today`() {
//        // Create a timestamp for today at 10:30 AM
//        val calendar = Calendar.getInstance().apply {
//            set(Calendar.HOUR_OF_DAY, 10)
//            set(Calendar.MINUTE, 30)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }
//        val timestamp = Timestamp(Date(calendar.timeInMillis))
//
//        val formattedTimestamp = formatTimestampForChat(timestamp)
//
//        assert(formattedTimestamp == "10:30 AM")
//    }
//
//    @Test
//    fun `formatTimestampForChat should return Yesterday for yesterday's date`() {
//        // Create a timestamp for yesterday
//        val calendar = Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_YEAR, -1)
//            set(Calendar.HOUR_OF_DAY, 15)
//            set(Calendar.MINUTE, 0)
//        }
//        val timestamp = Timestamp(Date(calendar.timeInMillis))
//
//        val formattedTimestamp = formatTimestampForChat(timestamp)
//
//        assert(formattedTimestamp == "Yesterday")
//    }
//
//    @Test
//    fun `formatTimestampForChat should return formatted date for older dates`() {
//        // Create a timestamp for a date in the past, e.g. Jan 10, 2022
//        val calendar = Calendar.getInstance().apply {
//            set(2022, Calendar.JANUARY, 10, 12, 0)
//        }
//        val timestamp = Timestamp(Date(calendar.timeInMillis))
//
//        val formattedTimestamp = formatTimestampForChat(timestamp)
//
//        assert(formattedTimestamp == "10/1/2022")
//    }
//
//}