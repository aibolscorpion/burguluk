package kz.shymkent.relaxhouse.addClientFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.FirebaseDatabase
import kz.shymkent.relaxhouse.Constants
import kz.shymkent.relaxhouse.PushNotification
import kz.shymkent.relaxhouse.R
import kz.shymkent.relaxhouse.SharedPreferencesTools
import kz.shymkent.relaxhouse.models.Client

class AddClientDialogFragmentViewModel(application : Application) : AndroidViewModel(application) {
    private val cottagesReference = FirebaseDatabase.getInstance().getReference("cottages")

    fun addClientToFirebase(client : Client){
        cottagesReference.child(SharedPreferencesTools.cottagename!!).child(Constants.CLIENTS).child(client.checkInDate.replace(".","")).setValue(client)
        PushNotification.createPushNotification( getApplication<Application>().getString(R.string.added_new_client,client.formatDateForOneSignal(client.checkInDate, client.checkInTime)))
    }
}