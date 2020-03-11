package kz.shymkent.relaxhouse.mainActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kizitonwose.calendarview.model.CalendarMonth;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.WeekFields;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kz.shymkent.relaxhouse.databinding.ActivityMainBinding;
import kz.shymkent.relaxhouse.addClientFragment.AddClientDialogFragment;
import kz.shymkent.relaxhouse.mainActivity.calendar.CalendarAdapter;
import kz.shymkent.relaxhouse.mainActivity.recyclerView.RecyclerViewAdapter;
import kz.shymkent.relaxhouse.R;
import kz.shymkent.relaxhouse.models.Client;


public class MainActivity extends AppCompatActivity {
    TextView month_text_view;
    ActivityMainBinding activityMainBinding;
    Context context= this;
     MainActivityViewModel viewModel;
    public com.kizitonwose.calendarview.CalendarView calendarView;
    RecyclerView recyclerView;
    RecyclerViewAdapter clientAdapter;
    public FloatingActionButton call_FAB;
    ImageView nextMonth,previous_month;
    Toolbar toolbar;
    YearMonth currentMonth = YearMonth.now();
    String phoneNumber;
    private DateTimeFormatter monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        activityMainBinding.setViewModel(viewModel);
        toolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);
        calendarView = findViewById(R.id.calendarView);
        call_FAB = findViewById(R.id.call_floating_action_button);
        recyclerView = findViewById(R.id.client_info_recycler_view);
        previous_month = findViewById(R.id.previousMonthImage);
        month_text_view = findViewById(R.id.month_text_view);
        previous_month.setOnClickListener(view -> {
            currentMonth=currentMonth.minusMonths(1);
            calendarView.smoothScrollToMonth(currentMonth);
        });
        nextMonth = findViewById(R.id.nextMonthImage);
        nextMonth.setOnClickListener(view -> {
            currentMonth=currentMonth.plusMonths(1);
            calendarView.smoothScrollToMonth(currentMonth);

        });

        clientAdapter = new RecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(clientAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalDate today = LocalDate.now();
        viewModel.getAllClientsFromFirebase();
        viewModel.getAllClientsLiveData().observe(this, new Observer<List<Client>>() {
            @Override
            public void onChanged(List<Client> clients) {
                viewModel.getCurrentDateClientList(today,true);
                setupCalendar();

            }
        });

        viewModel.getCurrentListLiveData().observe(this, new Observer<List<Client>>() {
                    @Override
                    public void onChanged(List<Client> clients) {
                        clientAdapter.setClientList(clients);
                        if(!clients.isEmpty()){
                            phoneNumber = clients.get(0).getPhoneNumber();
                        }
                    }
                }
        );
    }

    public void addClient(View view){
        AddClientDialogFragment addClientDialogFragment = new AddClientDialogFragment(viewModel);
        addClientDialogFragment.show(getSupportFragmentManager(),"add_client_dialog_fragment");
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void callClient(View view) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            call();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void call(){
        if (!TextUtils.isEmpty(phoneNumber)) {
            Intent call_intent = new Intent(Intent.ACTION_CALL);
            call_intent.setData(Uri.parse("tel:" + phoneNumber));
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(call_intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
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
    public void setupCalendar(){
        CalendarAdapter calendarDayBinder = new CalendarAdapter(context,viewModel);
        calendarView.setDayBinder(calendarDayBinder);
        Function1<CalendarMonth, Unit> listener = new Function1<CalendarMonth, Unit>() {
            @Override
            public Unit invoke(CalendarMonth cm) {
                month_text_view.setText(monthTitleFormatter.format(cm.getYearMonth().getMonth())+" "+cm.getYear());
                return kotlin.Unit.INSTANCE;
            }
        };
        calendarView.setMonthScrollListener(listener);
        YearMonth firstMonth = currentMonth.minusMonths(10);
        YearMonth lastMonth = currentMonth.plusMonths(10);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);
    }
    public void send_message_to_client(View view) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            try {
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode("", "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
