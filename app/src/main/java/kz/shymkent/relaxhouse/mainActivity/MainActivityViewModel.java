package kz.shymkent.relaxhouse.mainActivity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
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

import kz.shymkent.relaxhouse.Constants;
import kz.shymkent.relaxhouse.PushNotification;
import kz.shymkent.relaxhouse.SharedPreferencesTools;
import kz.shymkent.relaxhouse.models.Client;
import kz.shymkent.relaxhouse.models.Cottage;

public class MainActivityViewModel extends AndroidViewModel {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    DatabaseReference cottagesReference = FirebaseDatabase.getInstance().getReference("cottages");
    List<Client> clients = new ArrayList<>();
    MutableLiveData<List<Client>> currentListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Client>> allClientsMutableLiveData = new MutableLiveData<>();
    public ObservableBoolean noClient = new ObservableBoolean();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void getCottageNameAfterLogin(){
        String firebasePhoneNumber = SharedPreferencesTools.getPhoneNumber();
        cottagesReference.addValueEventListener(new ValueEventListener() {
            boolean cottageFound = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot cottageSnapshot : dataSnapshot.getChildren()){
                    Cottage cottage = cottageSnapshot.getValue(Cottage.class);
                    for(String dataBasePhoneNumber : cottage.getPhoneNumbers()){
                        if(dataBasePhoneNumber.equals(firebasePhoneNumber)){
                            SharedPreferencesTools.saveCottageName(cottage.getName());
                            getAllClientsFromFirebase();
                            cottageFound = true;
                            return;
                        }
                    }
                }
                if(!cottageFound){
                    Toast.makeText(getApplication(),"Нужно добавить коттедж в приложение",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getAllClientsFromFirebase(){
        cottagesReference.child(SharedPreferencesTools.getCottagename()).child("clients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clients.clear();
                for (DataSnapshot clientSnapshot : dataSnapshot.getChildren()) {
                    Client client = clientSnapshot.getValue(Client.class);
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
    public void removeClientFromFireBaseDB(Client client){
        cottagesReference.child(SharedPreferencesTools.getCottagename()).child(Constants.CLIENTS).child(client.checkInDate.replace(".","")).removeValue();
        PushNotification.createPushNotification("Клиент "+client.getCheckInDate()+" был удален !");
    }

    public LiveData<List<Client>> getCurrentListLiveData(){
        return currentListMutableLiveData;
    }
    public LiveData<List<Client>> getAllClientsLiveData(){
        return allClientsMutableLiveData;
    }

    public void addNewCottage(String name){
        Cottage cottage = new Cottage();
        cottage.setName(name);

        ArrayList<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+77755105107");

        cottage.setPhoneNumbers(phoneNumbers);
        cottagesReference.child(name).setValue(cottage);

    }
}
