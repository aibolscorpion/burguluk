package kz.shymkent.relaxhouse.mainActivity

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kz.shymkent.relaxhouse.Constants
import kz.shymkent.relaxhouse.PushNotification
import kz.shymkent.relaxhouse.R
import kz.shymkent.relaxhouse.SharedPreferencesTools.cottageName
import kz.shymkent.relaxhouse.SharedPreferencesTools.phoneNumber
import kz.shymkent.relaxhouse.SharedPreferencesTools.saveCottageName
import kz.shymkent.relaxhouse.models.Client
import kz.shymkent.relaxhouse.models.Client.Companion.copy
import kz.shymkent.relaxhouse.models.Cottage
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

class MainActivityViewModel(var context : Application) : AndroidViewModel(context) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")
    private val cottagesReference = FirebaseDatabase.getInstance().getReference("cottages")
    private var currentClient : Client?= null
    private val allClientList = ArrayList<Client>()
    private val currentMonthClientList = ArrayList<Client>()
    val currentClientMutableLiveData = MutableLiveData<Client>()
    val allClientsListMutableLiveData = MutableLiveData<List<Client>>()
    val isDayHaveClientObservableBoolean = ObservableBoolean()

    companion object{
        val playerIdList = ArrayList<String>()
    }

    fun getCottageNameAfterLogin() {
        val firebasePhoneNumber = phoneNumber
        cottagesReference.addValueEventListener(object : ValueEventListener {
            var cottageFound = false
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (cottageSnapshot in dataSnapshot.children) {
                    val cottage = cottageSnapshot.getValue(Cottage::class.java)
                    for (dataBasePhoneNumber in cottage!!.phoneNumbers!!) {
                        if (dataBasePhoneNumber == firebasePhoneNumber) {
                            saveCottageName(cottage.name)
                            getAllClientsFromFirebase()
                            getOneSignalPlayerIds()
                            cottageFound = true
                            return
                        }
                    }
                }
                if (!cottageFound) {
                    Toast.makeText(getApplication(),
                        getApplication<Application>().getString(R.string.add_cottage_to_app),
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getAllClientsFromFirebase() {
        cottagesReference.child(cottageName!!).child("clients")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    allClientList.clear()
                    for (clientSnapshot in dataSnapshot.children) {
                        val client = clientSnapshot.getValue(Client::class.java)
                        allClientList.add(client!!)
                    }
                    allClientsListMutableLiveData.value = allClientList
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getClientByMonth(currentMonth: YearMonth) {
        currentMonthClientList.clear()
        for (client in allClientList) {
            val clientCheckInDate = LocalDate.parse(client.checkInDate, dateTimeFormatter)
            if (clientCheckInDate.year == currentMonth.year && clientCheckInDate.monthValue == currentMonth.monthValue) {
                currentMonthClientList.add(client)
            }
        }
    }

    fun getCurrentDateClient(currentDate: LocalDate): Client? {
        currentClient = null
        var isDayHaveClient = false
        for (client in currentMonthClientList) {
            val date = LocalDate.parse(client.checkInDate, dateTimeFormatter)
            if (date.year == currentDate.year && date.monthValue == currentDate.monthValue && date.dayOfMonth == currentDate.dayOfMonth) {
                currentClient = copy(client)
                isDayHaveClient = true
                currentClientMutableLiveData.postValue(currentClient)
            }
        }
        isDayHaveClientObservableBoolean.set(isDayHaveClient)
        return currentClient
    }

    fun isDayHaveClient(currentDate : LocalDate) : Boolean{
        var isDayHaveClient  = false
        for(client in allClientList){
            val date = LocalDate.parse(client.checkInDate, dateTimeFormatter)
            if(date.year == currentDate.year && date.monthValue == currentDate.monthValue && date.dayOfMonth == currentDate.dayOfMonth){
                isDayHaveClient = true
            }
        }

        return isDayHaveClient
    }

    fun addClientToFirebase(client : Client){
        cottagesReference.child(cottageName!!).child(Constants.CLIENTS).child(client.checkInDate.replace(".","")).setValue(client)
        PushNotification.createPushNotification( getApplication<Application>().getString(R.string.added_new_client,client.formatDateForOneSignal(client.checkInDate, client.checkInTime)))
    }

    fun removeClientFromFireBaseDB(client : Client){
        cottagesReference.child(cottageName!!).child(Constants.CLIENTS).child(client.checkInDate.replace(".","")).removeValue()
        PushNotification.createPushNotification(context.getString(R.string.client_with_date_was_removed,client.formatDateForOneSignal(client.checkInDate, client.checkInTime)))
    }

    fun getOneSignalPlayerIds(){
        cottagesReference.child(cottageName!!).child("oneSignalPlayerId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    playerIdList.clear()
                    for (playerId in dataSnapshot.children) {
                        playerIdList.add(playerId.value.toString())
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun addNewCottage(name: String?) {
        val cottage = Cottage()
        cottage.name = name
        val phoneNumbers = java.util.ArrayList<String>()
        phoneNumbers.add("+77755105107")
        cottage.phoneNumbers = phoneNumbers
        cottagesReference.child(name!!).setValue(cottage)
    }
}