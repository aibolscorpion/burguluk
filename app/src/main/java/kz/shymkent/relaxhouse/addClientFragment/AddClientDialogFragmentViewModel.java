package kz.shymkent.relaxhouse.addClientFragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kz.shymkent.relaxhouse.Constants;
import kz.shymkent.relaxhouse.PushNotification;
import kz.shymkent.relaxhouse.SharedPreferencesTools;
import kz.shymkent.relaxhouse.models.Client;


public class AddClientDialogFragmentViewModel extends AndroidViewModel {
    DatabaseReference cottagesReference = FirebaseDatabase.getInstance().getReference("cottages");
    String cottageName="";
    SharedPreferences sharedPreferences = getApplication().getSharedPreferences(Constants.APPLICATION_NAME, Context.MODE_PRIVATE);
    public AddClientDialogFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void addClientToFireBaseDB(Client client ){
        cottagesReference.child(SharedPreferencesTools.getCottagename()).child(Constants.CLIENTS).child(client.checkInDate.replace(".","")).setValue(client);
        PushNotification.createPushNotification("Добавлен новый клиент на : "+client.getCheckInDate());
    }

}
