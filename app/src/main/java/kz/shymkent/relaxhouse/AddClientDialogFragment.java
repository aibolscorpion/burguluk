package kz.shymkent.relaxhouse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddClientDialogFragment extends DialogFragment {
    DatabaseReference databaseClients;
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
        final EditText add_quantity_edit_text = view.findViewById(R.id.add_quantity_edit_text);
        final EditText add_check_in_time_edit_text = view.findViewById(R.id.add_check_in_time_edit_text);
        final EditText add_check_out_time_edit_text = view.findViewById(R.id.add_check_out_time_edit_text);
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

