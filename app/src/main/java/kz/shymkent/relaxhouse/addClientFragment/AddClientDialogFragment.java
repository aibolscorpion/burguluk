package kz.shymkent.relaxhouse.addClientFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import kz.shymkent.relaxhouse.R;
import kz.shymkent.relaxhouse.databinding.FragmentAddClientBinding;
import kz.shymkent.relaxhouse.mainActivity.MainActivityViewModel;

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

