package kz.shymkent.relaxhouse.mainActivity

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kz.shymkent.relaxhouse.R
import androidx.lifecycle.ViewModelProvider
import kz.shymkent.relaxhouse.addClientFragment.AddClientDialogFragment
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.pm.PackageManager
import android.text.TextUtils
import android.content.Intent
import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarMonth
import kz.shymkent.relaxhouse.databinding.ActivityMainBinding
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.lang.Exception
import java.net.URLEncoder
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var monthTextView: TextView
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    lateinit var calendarView: CalendarView
    private lateinit var callFAB: FloatingActionButton
    private lateinit var nextMonth: ImageView
    private lateinit var previousMonth: ImageView
    private lateinit var toolbar: Toolbar
    private var currentMonth = YearMonth.now()
    private lateinit var addClientDialogFragment : AddClientDialogFragment
    lateinit var phoneNumber: String
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        activityMainBinding.viewModel = viewModel
        activityMainBinding.mainActivity = this
        toolbar = findViewById(R.id.appBarLayout)
        setSupportActionBar(toolbar)

        viewModel.getCottageNameAfterLogin()

        calendarView = findViewById(R.id.calendarView)
        callFAB = findViewById(R.id.call_floating_action_button)
        previousMonth = findViewById(R.id.previousMonthImage)
        monthTextView = findViewById(R.id.month_text_view)
        previousMonth.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            calendarView.smoothScrollToMonth(currentMonth)
            viewModel.getClientByMonth(currentMonth)
        }

        nextMonth = findViewById(R.id.nextMonthImage)
        nextMonth.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            calendarView.smoothScrollToMonth(currentMonth)
            viewModel.getClientByMonth(currentMonth)
        }
        viewModel.allClientsListMutableLiveData.observe(this) {
            viewModel.getClientByMonth(currentMonth)
            viewModel.getCurrentDateClient(LocalDate.now())
            calendarView.notifyCalendarChanged()
        }

        viewModel.currentClientMutableLiveData.observe(this) { client ->
            activityMainBinding.client = client
            phoneNumber = client.phoneNumber
            setFadeAnimation(activityMainBinding.clientInformationView.root)
        }
        setupCalendar()
    }

    fun addClient() {
        addClientDialogFragment = AddClientDialogFragment()
        addClientDialogFragment.show(supportFragmentManager, "add_client_dialog_fragment")
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun callClient() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
        } else {
            call()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun call() {
        if (!TextUtils.isEmpty(phoneNumber)) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            startActivity(callIntent)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call()
            }
        }
    }

    private fun setupCalendar() {
        val calendarAdapter = CalendarAdapter(this, viewModel)
        calendarView.dayBinder = calendarAdapter
        val listener = { cm: CalendarMonth ->
            currentMonth = cm.yearMonth
            viewModel.getClientByMonth(currentMonth)
            monthTextView.text = monthTitleFormatter.format(currentMonth.month) + " " + currentMonth.year
        }
        calendarView.monthScrollListener = listener
        val firstMonth = currentMonth.minusMonths(36)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
    }

    fun sendMessageToClient() {
        if (!TextUtils.isEmpty(phoneNumber)) {
            val i = Intent(Intent.ACTION_VIEW)
                try {
                    val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
                    i.setPackage("com.whatsapp")
                    i.data = Uri.parse(url)
                    startActivity(i)
                }catch (e: Exception) {
                    Toast.makeText(this,"Whatsapp is not installed in your phone", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
        }
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        view.startAnimation(anim)
    }

}