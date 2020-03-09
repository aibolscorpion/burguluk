package kz.shymkent.relaxhouse.mainActivity.addClientFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import kz.shymkent.relaxhouse.R;
import kz.shymkent.relaxhouse.databinding.FragmentAddClientBinding;
import kz.shymkent.relaxhouse.mainActivity.MainActivityViewModel;
import kz.shymkent.relaxhouse.models.Client;

public class AddClientDialogFragment extends DialogFragment {
    MainActivityViewModel viewModel;
    FragmentAddClientBinding fragmentAddClientBinding;
    public AddClientDialogFragment(MainActivityViewModel viewModel){
        this.viewModel = viewModel;
    }
    public AlertDialog alertDialog;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        fragmentAddClientBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_client,null,false);
        View view = fragmentAddClientBinding.getRoot();
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Новый клиент");
        alertDialog = alertDialogBuilder.create();
        Handler handler = new Handler(getContext(), fragmentAddClientBinding,viewModel, alertDialog);
        fragmentAddClientBinding.setHandler(handler);
        return alertDialog;
    }
}

