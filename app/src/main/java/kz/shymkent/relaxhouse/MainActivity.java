package kz.shymkent.relaxhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.CalendarView;

import kz.shymkent.relaxhouse.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setClient(addNewClient());
        activityMainBinding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                activityMainBinding.setClient(addAnotherNewClient());
            }
        });
    }

    private Client addNewClient(){
        Client client = new Client();
        client.setDate("11.01.2020");
        client.setQuantity("10");
        client.setCheckInTime("14:00");
        client.setCheckOutTime("12:00");
        client.setAvans("10.000");
        client.setDebt("30.000");
        client.setPhoneNumber("+77075544372");
        return client;
    }
    private Client addAnotherNewClient() {
        Client client = new Client();
        client.setDate("12.01.2020");
        client.setQuantity("15");
        client.setCheckInTime("14:00");
        client.setCheckOutTime("12:00");
        client.setAvans("10.000");
        client.setDebt("40.000");
        client.setPhoneNumber("+77716000313");
        return client;
    }
}
