package kz.shymkent.relaxhouse.models

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

data class Client (var checkInDate:String = "",
              var checkOutDate:String = "",
              var quantity:String = "",
              var checkInTime:String = "",
              var checkOutTime:String = "",
              var avans:String = "",
              var debt:String = "",
              var phoneNumber:String = "",
              var comment:String = ""){


    companion object {
        @JvmStatic
        fun copy(client: Client): Client {
            val newClient = Client()
            newClient.checkInDate = client.checkInDate
            newClient.checkOutDate = client.checkOutDate
            newClient.checkInTime = client.checkInTime
            newClient.checkOutTime = client.checkOutTime
            newClient.avans = client.avans
            newClient.debt = client.debt
            newClient.quantity = client.quantity
            newClient.comment = client.comment
            newClient.phoneNumber = client.phoneNumber
            return newClient
        }

        @JvmField
        var compareCheckInTime = java.util.Comparator { s1: Client, s2: Client ->
            val timeArray1 = s1.checkInTime.split(":").toTypedArray()
            val timeArray2 = s2.checkInTime.split(":").toTypedArray()
            val Id1 = timeArray1[0].toInt()
            val Id2 = timeArray2[0].toInt()
            Id1 - Id2
        }
    }


    fun formatCurrentDateAndTime(date: String, time: String) : String {
        var dateTimeFormatter1 = DateTimeFormatter.ofPattern("d.M.yyyy")
        var dateTimeFormatter2 = DateTimeFormatter.ofPattern("EE''d MMM")

        val checkInDate = LocalDate.parse(date, dateTimeFormatter1)
        return """
            ${dateTimeFormatter2.format(checkInDate)}
            $time
            """.trimIndent()

    }

    fun formatCurrentDate(date: String) : String {
        var dateTimeFormatter1 = DateTimeFormatter.ofPattern("d.M.yyyy")
        var dateTimeFormatter2 = DateTimeFormatter.ofPattern("d MMMM")

        val checkInDate = LocalDate.parse(date, dateTimeFormatter1)
        return """${dateTimeFormatter2.format(checkInDate)}
            """.trimIndent()

    }

    fun formatDateForOneSignal(date : String, time : String): String {
        var dateTimeFormatter1 = DateTimeFormatter.ofPattern("d.M.yyyy")
        var dateTimeFormatter2 = DateTimeFormatter.ofPattern("d MMMM")
        val formattedCheckInDate = LocalDate.parse(date, dateTimeFormatter1)

        return " ${dateTimeFormatter2.format(formattedCheckInDate)} $time"
    }

}