package kz.shymkent.relaxhouse.addClientFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

import kz.shymkent.relaxhouse.databinding.FragmentAddClientBinding;
import kz.shymkent.relaxhouse.mainActivity.MainActivityViewModel;
import kz.shymkent.relaxhouse.models.Client;

public class Handler {
    Context context;
    AlertDialog alertDialog;
    MainActivityViewModel viewModel;
    Calendar calendar = Calendar.getInstance();
    FragmentAddClientBinding fragmentAddClientBinding;

    Client client = new Client();
    public Handler(Context context, FragmentAddClientBinding fragmentAddClientBinding,MainActivityViewModel viewModel,AlertDialog alertDialog){
        this.fragmentAddClientBinding = fragmentAddClientBinding;
        this.context = context;
        this.viewModel = viewModel;
        this.alertDialog = alertDialog;
        fragmentAddClientBinding.setClient(client);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showCheckInDatePickerDialog( ){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                  client.checkInDate = dayOfMonth + "." + (month + 1) + "." + year;
                showCheckInTimePickerDialog();
            }
        };
        datePickerDialog.setOnDateSetListener(listener);
        datePickerDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showCheckOutDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                client.checkOutDate = dayOfMonth + "." + (month + 1) + "." + year;
                showCheckOutTimePickerDialog();
            }
        });
        datePickerDialog.show();
    }
    private void showCheckInTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                client.checkInTime = (hourOfDay + ":" + String.format("%02d", minute));
                fragmentAddClientBinding.invalidateAll();
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    private void showCheckOutTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                client.checkOutTime = (hourOfDay + ":" + String.format("%02d", minute));
                fragmentAddClientBinding.invalidateAll();
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    public void addClient(Client client){
        if(!client.getCheckInDate().isEmpty() && !client.getCheckInTime().isEmpty() && ! client.getCheckOutDate().isEmpty() && !client.getCheckOutTime().isEmpty()
            && !client.getQuantity().isEmpty() && !client.getAvans().isEmpty() && !client.getDebt().isEmpty() && !client.getPhoneNumber().isEmpty()){
            viewModel.addClientToFireBaseDB(client);
            Toast.makeText(context, "Новый клиент добавлен", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }else{
            Toast.makeText(context, "Заполните пустые поля", Toast.LENGTH_SHORT).show();
        }

    }
    public void cancel(){
        alertDialog.dismiss();
    }
}
