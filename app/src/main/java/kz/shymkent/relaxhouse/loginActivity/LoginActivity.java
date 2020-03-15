package kz.shymkent.relaxhouse.loginActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import kz.shymkent.relaxhouse.R;
import kz.shymkent.relaxhouse.databinding.ActivityLoginBinding;
import kz.shymkent.relaxhouse.databinding.ActivityLoginBindingImpl;

public class LoginActivity extends AppCompatActivity {
    AuthViewModel viewModel;
    ActivityLoginBinding activityLoginBinding;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        activityLoginBinding.setViewModel(viewModel);
    }
}
