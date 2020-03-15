package kz.shymkent.relaxhouse.loginActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import kz.shymkent.relaxhouse.Constants;
import kz.shymkent.relaxhouse.mainActivity.MainActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AuthViewModel extends AndroidViewModel {
    String verificationId,code;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public ObservableField<String> phoneNumber = new ObservableField<>("");
    public ObservableField<String> verificationCode = new ObservableField<>("");

    public AuthViewModel(@NonNull Application application) {
        super(application);
    }

    public void sendVerificationCode(String phoneNumber) {
            OnVerificationStateChangedCallbacks mCallbacks = new OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                    Log.i("aibol","onCodeSend: "+s);
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    code = phoneAuthCredential.getSmsCode();
                    if(code !=null){
                        constractCredential(code);
                    }
                    Log.i("aibol","onCompleted()");
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Log.i("aibol","onFailed(): "+e.getMessage());
                }
            };

            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60,TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallbacks);
        }
        private void constractCredential(String code){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
            signInWithCredential(credential);
        }
        private void signInWithCredential(PhoneAuthCredential credential){
            mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(getApplication(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getApplication().startActivity(i);
                        FirebaseUser user = task.getResult().getUser();
                        Toast.makeText(getApplication(),user.getPhoneNumber(), Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = getApplication().getSharedPreferences(Constants.PHONE_NUMBER,Context.MODE_PRIVATE).edit();
                        editor.putString(Constants.PHONE_NUMBER,user.getPhoneNumber());
                        editor.commit();
                    }else{
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getApplication(),"Введенный вами код неверный !",Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplication(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
}
