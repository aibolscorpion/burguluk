package kz.shymkent.relaxhouse.mainActivity

import com.kizitonwose.calendarview.ui.DayBinder
import androidx.annotation.RequiresApi
import android.os.Build
import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import kz.shymkent.relaxhouse.R
import com.kizitonwose.calendarview.ui.ViewContainer
import android.widget.TextView
import android.widget.FrameLayout
import com.kizitonwose.calendarview.model.DayOwner
import android.widget.Toast
import kz.shymkent.relaxhouse.models.Client
import kz.shymkent.relaxhouse.removeClientFragment.RemoveClientDialogFragment
import org.threeten.bp.LocalDate

class CalendarAdapter(var context: MainActivity, var viewModel : MainActivityViewModel) : DayBinder<CalendarAdapter.DayViewContainer> {
    var selectedDate : LocalDate? = null
    var oldDate: LocalDate? = null
    lateinit var dialog : RemoveClientDialogFragment
    @RequiresApi(Build.VERSION_CODES.O)
    override fun create(view: View): DayViewContainer {
        return DayViewContainer(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(container: DayViewContainer, day: CalendarDay) {
        container.day = day
        container.calendarDayTextView.text = day.date.dayOfMonth.toString()
        when (day.date) {
            selectedDate -> {
                container.calendarDayFrameLayout.setBackgroundResource(R.drawable.selected_day_bg)
            }
            LocalDate.now() -> {
                container.calendarDayFrameLayout.setBackgroundResource(R.drawable.current_day_bg)
            }
            else -> {
                container.calendarDayFrameLayout.background = null
            }
        }
        addClientsToCalendar(container, day)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    inner class DayViewContainer(view: View) : ViewContainer(view), RemoveClientDialogFragment.ClickListener {
        val clientLine : View = view.findViewById(R.id.client_line)
        lateinit var day : CalendarDay
        val calendarDayTextView : TextView = view.findViewById(R.id.calendar_day_text_view)
        val calendarDayFrameLayout : FrameLayout = view.findViewById(R.id.calendar_day_frame_layout)

        init {
            calendarDayFrameLayout.setOnClickListener{
                if(day.owner == DayOwner.THIS_MONTH){
                    oldDate = selectedDate
                    selectedDate = day.date
                    if(oldDate != null){
                        context.calendarView.notifyDateChanged(oldDate!!)
                    }
                    context.calendarView.notifyDateChanged(selectedDate!!)
                    viewModel.getCurrentDateClient(selectedDate!!)
                }
            }
            calendarDayFrameLayout.setOnLongClickListener {
                if(day.owner == DayOwner.THIS_MONTH){
                    val client = viewModel.getCurrentDateClient(day.date)
                    if(client != null && client.checkInDate.isNotEmpty()){
                        dialog = RemoveClientDialogFragment(this, client)
                        dialog.show(context.supportFragmentManager, "remove_client_dialog_fragment")
                        }
                }
                true
            }
        }


        override fun onOkClicked(client : Client) {
            viewModel.removeClientFromFireBaseDB(client)
            Toast.makeText(context, context.resources.getString(R.string.client_was_removed), Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addClientsToCalendar(dayViewContainer: DayViewContainer, day: CalendarDay){
        if (day.owner == DayOwner.THIS_MONTH) {
            val isDayHaveClient = viewModel.isDayHaveClient(day.date)
            if (isDayHaveClient) {
                dayViewContainer.clientLine.setBackgroundColor(context.getColor(R.color.calendar_text_view_color))
            } else {
                dayViewContainer.clientLine.background = null
            }
        } else {
            dayViewContainer.clientLine.background = null
            dayViewContainer.calendarDayTextView.setTextColor(context.getColor(R.color.example_5_text_grey_light))
        }
    }
}