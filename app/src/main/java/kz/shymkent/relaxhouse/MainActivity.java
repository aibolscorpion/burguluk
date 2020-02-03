package kz.shymkent.relaxhouse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    org.threeten.bp.LocalDate selectedDate,oldDate;
    DatabaseReference databaseClients;
    com.kizitonwose.calendarview.CalendarView calendarView;
    RecyclerView recyclerView;
    ClientAdapter clientAdapter;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    FloatingActionButton call_FAB;
    List<Client> clients = new ArrayList<>();
    ImageView nextMonth,previous_month;
    Toolbar toolbar;
    YearMonth currentMonth = YearMonth.now();
    MutableLiveData<List<Client>> liveData = new MutableLiveData<>();
    List<Client> newList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);
        call_FAB = findViewById(R.id.call_floating_action_button);
        recyclerView = findViewById(R.id.client_info_recycler_view);
        previous_month = findViewById(R.id.previousMonthImage);
        previous_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonth=currentMonth.minusMonths(1);
                calendarView.smoothScrollToMonth(currentMonth);
            }
        });
        nextMonth = findViewById(R.id.nextMonthImage);
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonth=currentMonth.plusMonths(1);
                calendarView.smoothScrollToMonth(currentMonth);
            }
        });
        clientAdapter = new ClientAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(clientAdapter);
        databaseClients = FirebaseDatabase.getInstance().getReference("clients");

        calendarView = findViewById(R.id.calendarView);
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

                clientAdapter.setClientList(getCurrentList(currentDay));

                calendarView.setDayBinder(dayBinder);
                YearMonth firstMonth = currentMonth.minusMonths(10);
                YearMonth lastMonth = currentMonth.plusMonths(10);
                DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
                calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
                calendarView.scrollToMonth(currentMonth);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    DayBinder<DayViewContainer> dayBinder = new DayBinder<DayViewContainer>() {
        @Override
        public DayViewContainer create(View view) {
            return new DayViewContainer(view);
        }

        @Override
        public void bind(DayViewContainer dayViewContainer, CalendarDay calendarDay) {
            dayViewContainer.day = calendarDay;
            dayViewContainer.calendar_day_text_view.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
            if(calendarDay.getDate().equals(selectedDate)){
                dayViewContainer.calendar_day_frame_layout.setBackgroundResource(R.drawable.selected_day_bg);
            }else if(calendarDay.getDate().equals(LocalDate.now())){
                dayViewContainer.calendar_day_frame_layout.setBackgroundResource(R.drawable.current_day_bg);
            }else{
                dayViewContainer.calendar_day_frame_layout.setBackground(null);
            }
            dayViewContainer.clientTop.setBackground(null);
            dayViewContainer.clientBottom.setBackground(null);

//            List<Client> clients = getCurrentList(calendarDay.getDate());
//            if(clients!= null){
//                if(clients.size() == 1){
//                    dayViewContainer.clientTop.setBackgroundColor(getColor(R.color.calendar_text_view_color));
//                }else if(clients.size() == 2){
//                    dayViewContainer.clientTop.setBackgroundColor(getColor(R.color.calendar_text_view_color));
//                    dayViewContainer.clientBottom.setBackgroundColor(getColor(R.color.calendar_text_view_color));
//                }
//            }
        }


    };

    class DayViewContainer extends ViewContainer{
        View clientTop,clientBottom;
        CalendarDay day;
        public TextView calendar_day_text_view;
        public FrameLayout calendar_day_frame_layout;
        public DayViewContainer(View view) {
            super(view);
            clientTop = view.findViewById(R.id.clientTop);
            clientBottom = view.findViewById(R.id.clientBottom);
            calendar_day_frame_layout = view.findViewById(R.id.calendar_day_frame_layout);
            calendar_day_frame_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldDate = selectedDate;
                    selectedDate = day.getDate();
                    calendarView.notifyDateChanged(selectedDate);
                    if(oldDate!=null){
                        calendarView.notifyDateChanged(oldDate);
                    }
                    clientAdapter.setClientList(getCurrentList(selectedDate));
                }
            });
            calendar_day_text_view = view.findViewById(R.id.calendar_day_text_view);
        }
    }


    public void add_client(View view){
        AddClientDialogFragment addClientDialogFragment = new AddClientDialogFragment();
        addClientDialogFragment.show(getSupportFragmentManager(),"add_client_dialog_fragment");
    }
    public void call_client(View view) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            call();
        }

    }
    public void call(){
        if (!TextUtils.isEmpty(newList.get(0).getPhoneNumber())) {
            Intent call_intent = new Intent(Intent.ACTION_CALL);
            call_intent.setData(Uri.parse("tel:" + newList.get(0).getPhoneNumber()));
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(call_intent);
        }
    }

    @SuppressLint("RestrictedApi")
    public List<Client> getCurrentList(LocalDate currentDate) {
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int dayOfMonth = currentDate.getDayOfMonth();
        newList.clear();
        for (Client client : clients) {
            LocalDate date = LocalDate.parse(client.getDate(),dateTimeFormatter);
            if (date.getYear() == year && date.getMonthValue() == month && date.getDayOfMonth() == dayOfMonth) {
                newList.add(client);
            }
        }
        Collections.sort(newList, Client.compareCheckInTime);
        return newList;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                }
                break;
        }
    }
}
