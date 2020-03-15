package kz.shymkent.relaxhouse.addClientFragment;

import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kz.shymkent.relaxhouse.Constants;
import kz.shymkent.relaxhouse.PushNotification;
import kz.shymkent.relaxhouse.models.Client;


public class AddClientDialogFragmentViewModel extends ViewModel {
    DatabaseReference cottagesReference = FirebaseDatabase.getInstance().getReference("cottages");
    public void addClientToFireBaseDB(Client client ){
        cottagesReference.child(Constants.COTTAGE_NAME).child(Constants.CLIENTS).child(client.checkInDate.replace(".","")).setValue(client);
        PushNotification.createPushNotification("Добавлен новый клиент на : "+client.getCheckInDate());
    }

}
