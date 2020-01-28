package kz.shymkent.relaxhouse;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddClientDialogFragment extends DialogFragment {
    DatabaseReference databaseClients;
    Calendar calendar = Calendar.getInstance();
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        databaseClients = FirebaseDatabase.getInstance().getReference("clients");
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_client,null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Новый клиент");
        alertDialogBuilder.setPositiveButton("Добавить", null);
        alertDialogBuilder.setNegativeButton("Отмена", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final EditText add_date_edit_text = view.findViewById(R.id.add_date_edit_text);
        add_date_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        add_date_edit_text.setText(dayOfMonth+"."+(month+1)+"."+year);
                    }
                }
                );
                datePickerDialog.show();
            }
        });

        final EditText add_quantity_edit_text = view.findViewById(R.id.add_quantity_edit_text);
        final EditText add_check_in_time_edit_text = view.findViewById(R.id.add_check_in_time_edit_text);
        final EditText add_comment_edit_text = view.findViewById(R.id.add_comment_edit_text);
        add_check_in_time_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        add_check_in_time_edit_text.setText(hourOfDay+":"+String.format("%02d",minute));
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }

        });
        final EditText add_check_out_time_edit_text = view.findViewById(R.id.add_check_out_time_edit_text);
        add_check_out_time_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        add_check_out_time_edit_text.setText(hourOfDay+":"+String.format("%02d",minute));
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }

        });
        final EditText add_avans_edit_text = view.findViewById(R.id.add_avans_edit_text);
        final EditText add_debt_edit_text = view.findViewById(R.id.add_debt_edit_text);
        final EditText add_phone_number_edit_text = view.findViewById(R.id.add_phone_number_edit_text);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(add_date_edit_text.getText().toString())
                                && !TextUtils.isEmpty(add_avans_edit_text.getText().toString())
                                && !TextUtils.isEmpty(add_debt_edit_text.getText().toString())
                                && !TextUtils.isEmpty(add_phone_number_edit_text.getText().toString())
                                && !TextUtils.isEmpty(add_check_in_time_edit_text.getText().toString())
                                && !TextUtils.isEmpty(add_check_out_time_edit_text.getText().toString())
                                && !TextUtils.isEmpty(add_quantity_edit_text.getText().toString())) {
                            Client client = new Client();
                            client.setDate(add_date_edit_text.getText().toString());
                            client.setAvans(add_avans_edit_text.getText().toString());
                            client.setDebt(add_debt_edit_text.getText().toString());
                            client.setPhoneNumber(add_phone_number_edit_text.getText().toString());
                            client.setCheckInTime(add_check_in_time_edit_text.getText().toString());
                            client.setCheckOutTime(add_check_out_time_edit_text.getText().toString());
                            client.setQuantity(add_quantity_edit_text.getText().toString());
                            client.setComment(add_comment_edit_text.getText().toString());
                            String id = databaseClients.push().getKey();
                            databaseClients.child(id).setValue(client);
                            alertDialog.dismiss();
                            Toast.makeText(getContext(), "Новый клиент добавлен", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Заполните пустые поля", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return alertDialog;
    }

}

