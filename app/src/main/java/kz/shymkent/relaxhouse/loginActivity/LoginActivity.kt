package kz.shymkent.relaxhouse.loginActivity

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kz.shymkent.relaxhouse.R
import kz.shymkent.relaxhouse.SharedPreferencesTools
import kz.shymkent.relaxhouse.SharedPreferencesTools.authorized
import kz.shymkent.relaxhouse.databinding.ActivityLoginBinding
import kz.shymkent.relaxhouse.mainActivity.MainActivity
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    lateinit var activityLoginBinding: ActivityLoginBinding
    var verificationId = ""
    private val mAuth = FirebaseAuth.getInstance()
    @JvmField
    var phoneNumber = ObservableField("")
    @JvmField
    var verificationCode = ObservableField("")
    @JvmField
    var phoneEditTextEnabled = ObservableBoolean(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        activityLoginBinding.loginActivity = this
        if(authorized){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    fun sendVerificationCode(phoneNumber: String) {
        phoneEditTextEnabled.set(false)
        val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
                Toast.makeText(application,
                    application.getString(R.string.verification_code_sent_to_your_number),
                    Toast.LENGTH_LONG).show()
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                verificationCode.set(phoneAuthCredential.smsCode)
                if (!verificationCode.get()!!.isEmpty()) {
                    constractCredential(verificationCode.get())
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(application, e.message, Toast.LENGTH_LONG).show()
            }
        }


        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setCallbacks(mCallbacks)
            .setActivity(this)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setPhoneNumber(phoneNumber)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    fun constractCredential(code: String?) {
        if (!verificationId.isEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationId, code!!)
            signInWithCredential(credential)
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val i = Intent(application, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                val user = task.result.user
                SharedPreferencesTools.saveAuthorized(true)
                SharedPreferencesTools.savePhoneNumber(user!!.phoneNumber)
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(application,
                        application.getString(R.string.invalid_verification_code),
                        Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}