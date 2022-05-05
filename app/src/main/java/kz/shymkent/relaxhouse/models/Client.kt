package kz.shymkent.relaxhouse.models

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class Client {
    var checkInDate = ""
    var checkOutDate = ""
    var quantity = ""
    var checkInTime = ""
    var checkOutTime = ""
    var avans = ""
    var debt = ""
    var phoneNumber = ""
    var comment = ""

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

    override fun toString(): String {
        return "Client(checkInDate='$checkInDate', checkOutDate='$checkOutDate', quantity='$quantity', checkInTime='$checkInTime', checkOutTime='$checkOutTime', avans='$avans', debt='$debt', phoneNumber='$phoneNumber', comment='$comment')"
    }
}