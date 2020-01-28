package kz.shymkent.relaxhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {
    DatabaseReference databaseClients;
    CalendarView calendarView;
    RecyclerView recyclerView;
    ClientAdapter clientAdapter;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    FloatingActionButton call_FAB;
    List<Client> clients = new ArrayList<>();
    List<Client> newList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        call_FAB = findViewById(R.id.call_floating_action_button);
        recyclerView = findViewById(R.id.client_info_recycler_view);
        clientAdapter = new ClientAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(clientAdapter);
        databaseClients = FirebaseDatabase.getInstance().getReference("clients");

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseClients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clients.clear();
                for (DataSnapshot clientSnapshot : dataSnapshot.getChildren()) {
                    Client client = clientSnapshot.getValue(Client.class);
                    clients.add(client);
                }

                LocalDate currentDay = LocalDate.now();
                int year =currentDay.getYear();
                int month = currentDay.getMonthValue()-1;
                int dayOfMonth = currentDay.getDayOfMonth();
                onSelectedDayChange(calendarView,year,month,dayOfMonth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

    }

    public void call_client(View view) {
        if (!TextUtils.isEmpty(newList.get(0).getPhoneNumber())) {
            Intent call_intent = new Intent(Intent.ACTION_CALL);
            call_intent.setData(Uri.parse("tel:" + newList.get(0).getPhoneNumber()));
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.i("aibol","permission not granted");
               return;
            }
            startActivity(call_intent);
        }
    }
    public void add_client(View view){
        AddClientDialogFragment addClientDialogFragment = new AddClientDialogFragment();
        addClientDialogFragment.show(getSupportFragmentManager(),"add_client_dialog_fragment");


    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Log.i("aibol","/onSelectedDayCh4. ange "+year+" "+month+" "+dayOfMonth+" clients size = "+clients.size());
                newList.clear();
                for (Client client : clients) {
                    LocalDate date = LocalDate.parse(client.getDate(), dateTimeFormatter);
                    Log.i("aibol","client date = "+date.getYear()+" "+date.getMonthValue()+" "+date.getDayOfMonth());
                    if (date.getYear() == year && date.getMonthValue() == (month + 1) && date.getDayOfMonth() == dayOfMonth) {
                        newList.add(client);
                    }
                }
                clientAdapter.setClientList(newList);
    }
}
