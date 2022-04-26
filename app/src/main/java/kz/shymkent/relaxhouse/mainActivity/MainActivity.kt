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
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    lateinit var month_text_view: TextView
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    lateinit var calendarView: CalendarView
    lateinit var call_FAB: FloatingActionButton
    lateinit var nextMonth: ImageView
    lateinit var previous_month: ImageView
    lateinit var toolbar: Toolbar
    var currentMonth = YearMonth.now()
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
        call_FAB = findViewById(R.id.call_floating_action_button)
        previous_month = findViewById(R.id.previousMonthImage)
        month_text_view = findViewById(R.id.month_text_view)
        previous_month.setOnClickListener {
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
        viewModel.getAllClientsListLiveData().observe(this) {
            viewModel.getClientByMonth(YearMonth.now())
            viewModel.getCurrentDateClient(LocalDate.now())
            calendarView.notifyCalendarChanged()
        }
        viewModel.getCurrentClientLiveData().observe(this) { client ->
            activityMainBinding.client = client
            phoneNumber = client.phoneNumber
            setFadeAnimation(activityMainBinding.clientInformationView.root)
        }
        setupCalendar()
    }

    fun addClient() {
        val addClientDialogFragment = AddClientDialogFragment()
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
            val call_intent = Intent(Intent.ACTION_CALL)
            call_intent.data = Uri.parse("tel:$phoneNumber")
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            startActivity(call_intent)
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

    fun setupCalendar() {
        val calendarAdapter = CalendarAdapter(this, viewModel!!)
        calendarView.dayBinder = calendarAdapter
        val listener = { cm: CalendarMonth ->
            viewModel.getClientByMonth(cm.yearMonth)
            month_text_view.text = monthTitleFormatter.format(cm.yearMonth.month) + " " + cm.year
            Unit
        }
        calendarView.monthScrollListener = listener
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
    }

    fun send_message_to_client() {
        if (!TextUtils.isEmpty(phoneNumber)) {
            val i = Intent(Intent.ACTION_VIEW)
                try {
                    val url ="https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode("","UTF-8")
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