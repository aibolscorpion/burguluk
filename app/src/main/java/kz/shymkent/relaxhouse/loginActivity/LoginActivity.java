package kz.shymkent.relaxhouse.loginActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import kz.shymkent.relaxhouse.Constants;
import kz.shymkent.relaxhouse.R;
import kz.shymkent.relaxhouse.SharedPreferencesTools;
import kz.shymkent.relaxhouse.databinding.ActivityLoginBinding;
import kz.shymkent.relaxhouse.databinding.ActivityLoginBindingImpl;
import kz.shymkent.relaxhouse.mainActivity.MainActivity;

public class LoginActivity extends AppCompatActivity {
    AuthViewModel viewModel;
    ActivityLoginBinding activityLoginBinding;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        activityLoginBinding.setViewModel(viewModel);

        if(SharedPreferencesTools.getAuthorized()){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
