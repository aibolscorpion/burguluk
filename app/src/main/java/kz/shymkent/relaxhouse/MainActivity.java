package kz.shymkent.relaxhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.YearMonth;
import org.threeten.bp.temporal.WeekFields;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseClients;
    com.kizitonwose.calendarview.CalendarView calendarView;
    RecyclerView recyclerView;
    ClientAdapter clientAdapter;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    FloatingActionButton call_FAB;
    List<Client> clients = new ArrayList<>();
    List<Client> newList = new ArrayList<>();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);
        call_FAB = findViewById(R.id.call_floating_action_button);
        recyclerView = findViewById(R.id.client_info_recycler_view);
        clientAdapter = new ClientAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(clientAdapter);
        databaseClients = FirebaseDatabase.getInstance().getReference("clients");

        calendarView = findViewById(R.id.calendarView);

        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(DayViewContainer dayViewContainer, CalendarDay calendarDay) {
                dayViewContainer.day = calendarDay;
                dayViewContainer.calendar_day_text_view.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
            }

        });
        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(10);
        YearMonth lastMonth = currentMonth.plusMonths(10);

        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);
    }



    class DayViewContainer extends ViewContainer{

        CalendarDay day;
        public TextView calendar_day_text_view;
        public FrameLayout calendar_day_frame_layout;
        public DayViewContainer(View view) {
            super(view);
            calendar_day_frame_layout = view.findViewById(R.id.calendar_day_frame_layout);
            calendar_day_frame_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCurrentClientList(day.getDate().getYear(),day.getDate().getMonthValue(),day.getDate().getDayOfMonth());
                }
            });
            calendar_day_text_view = view.findViewById(R.id.calendar_day_text_view);
        }
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
                int month = currentDay.getMonthValue();
                int dayOfMonth = currentDay.getDayOfMonth();

                setCurrentClientList(year,month,dayOfMonth);
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


    void setCurrentClientList(int year, int month, int dayOfMonth) {
        Log.i("aibol","/onSelectedDayCh4. ange "+year+" "+month+" "+dayOfMonth+" clients size = "+clients.size());
                newList.clear();
                for (Client client : clients) {
                    LocalDate date = LocalDate.parse(client.getDate(), dateTimeFormatter);
                    Log.i("aibol","client date = "+date.getYear()+" "+date.getMonthValue()+" "+date.getDayOfMonth());
                    if (date.getYear() == year && date.getMonthValue() == month && date.getDayOfMonth() == dayOfMonth) {
                        newList.add(client);
                    }
                }
                clientAdapter.setClientList(newList);
    }
}
