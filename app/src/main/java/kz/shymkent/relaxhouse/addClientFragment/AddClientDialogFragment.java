package kz.shymkent.relaxhouse.addClientFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import kz.shymkent.relaxhouse.R;
import kz.shymkent.relaxhouse.databinding.FragmentAddClientBinding;
import kz.shymkent.relaxhouse.mainActivity.MainActivityViewModel;
import kz.shymkent.relaxhouse.models.Client;

public class AddClientDialogFragment extends DialogFragment {
    AddClientDialogFragmentViewModel viewModel;
    FragmentAddClientBinding fragmentAddClientBinding;
    public AlertDialog alertDialog;
    Calendar calendar = Calendar.getInstance();
    Client client = new Client();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AddClientDialogFragmentViewModel.class);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        fragmentAddClientBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_client,null,false);
        View view = fragmentAddClientBinding.getRoot();
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Новый клиент");
        alertDialog = alertDialogBuilder.create();
        fragmentAddClientBinding.setClient(client);
        fragmentAddClientBinding.setAddClientDialogFragment(this);
        return alertDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showCheckInDatePickerDialog( ){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                client.checkInTime = (hourOfDay + ":" + String.format("%02d", minute));
                fragmentAddClientBinding.invalidateAll();
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    private void showCheckOutTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
            Toast.makeText(getContext(), "Новый клиент добавлен", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }else{
            Toast.makeText(getContext(), "Заполните пустые поля", Toast.LENGTH_SHORT).show();
        }

    }
    public void cancel(){
        alertDialog.dismiss();
    }
}

