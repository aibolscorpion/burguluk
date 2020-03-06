package kz.shymkent.relaxhouse.mainActivity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kz.shymkent.relaxhouse.models.Client;

public class MainActivityViewModel extends androidx.lifecycle.ViewModel {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    DatabaseReference databaseClients;
    List<Client> clients = new ArrayList<>();
    MutableLiveData<List<Client>> currentListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Client>> allClientsMutableLiveData = new MutableLiveData<>();
    public ObservableBoolean noClient = new ObservableBoolean();
    public void getAllClientsFromFirebase(){
        databaseClients = FirebaseDatabase.getInstance().getReference("clients");
        databaseClients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clients.clear();
                for (DataSnapshot clientSnapshot : dataSnapshot.getChildren()) {
                    Client client = clientSnapshot.getValue(Client.class);
                    client.setKey(clientSnapshot.getKey());
                    clients.add(client);
                }
                allClientsMutableLiveData.setValue(clients);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public int getCurrentDateClientList(LocalDate currentDate,boolean setMutableLiveData) {
        List<Client> newList = new ArrayList<>();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int dayOfMonth = currentDate.getDayOfMonth();
        newList.clear();
        for (Client client : clients) {
            LocalDate date = LocalDate.parse(client.getCheckInDate(),dateTimeFormatter);
            if (date.getYear() == year && date.getMonthValue() == month && date.getDayOfMonth() == dayOfMonth) {
                newList.add(client);
            }
        }

        Collections.sort(newList, Client.compareCheckInTime);
        if(setMutableLiveData){
            if ((newList.isEmpty())) {
                noClient.set(true);
            } else {
                noClient.set(false);
            }
            currentListMutableLiveData.postValue(newList);
        }
        return newList.size();
    }
    public Client getClientByDate(LocalDate currentDate) {
        List<Client> newList = new ArrayList<>();
        Client removeClient = new Client();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int dayOfMonth = currentDate.getDayOfMonth();
        newList.clear();
        for (Client client : clients) {
            LocalDate date = LocalDate.parse(client.getCheckInDate(),dateTimeFormatter);
            if (date.getYear() == year && date.getMonthValue() == month && date.getDayOfMonth() == dayOfMonth) {
                newList.add(client);
            }
        }
        if (!newList.isEmpty()){
            removeClient  = newList.get(0);
        }
        return removeClient;
    }
    public void addClientToFireBaseDB(Client client ){
        databaseClients = FirebaseDatabase.getInstance().getReference("clients");
        String id = databaseClients.push().getKey();
        databaseClients.child(id).setValue(client);
        createPushNotification("Добавлен новый клиент на : "+client.getCheckInDate());
    }
    public void removeClientFromFireBaseDB(Client client){
        databaseClients = FirebaseDatabase.getInstance().getReference("clients");
        databaseClients.child(client.getKey()).removeValue();
        createPushNotification("Клиент "+client.getCheckInDate()+" был удален !");
    }
    public void createPushNotification(String text){
        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'"+text+"'}, 'include_player_ids': ['" + "aeff2fe7-c0ce-4ccf-943e-6938b25d53e4" + "']}"),
                    new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.i("OneSignalExample", "postNotification Success: " + response.toString());
                        }

                        @Override
                        public void onFailure(JSONObject response) {
                            Log.e("OneSignalExample", "postNotification Failure: " + response.toString());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public LiveData<List<Client>> getCurrentListLiveData(){
        return currentListMutableLiveData;
    }
    public LiveData<List<Client>> getAllClientsLiveData(){
        return allClientsMutableLiveData;
    }
}
