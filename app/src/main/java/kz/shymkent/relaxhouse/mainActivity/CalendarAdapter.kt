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
import org.threeten.bp.LocalDate

class CalendarAdapter(var context: MainActivity, var viewModel : MainActivityViewModel) : DayBinder<CalendarAdapter.DayViewContainer> {
    var selectedDate : LocalDate? = null
    var oldDate: LocalDate? = null

    override fun create(view: View): DayViewContainer {
        return DayViewContainer(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun bind(container: DayViewContainer, day: CalendarDay) {
        container.day = day
        container.calendar_day_text_view.text = day.date.dayOfMonth.toString()
        if(day.date == selectedDate){
            container.calendar_day_frame_layout.setBackgroundResource(R.drawable.selected_day_bg)
        }else if(day.date == LocalDate.now()){
            container.calendar_day_frame_layout.setBackgroundResource(R.drawable.current_day_bg)
        }else{
            container.calendar_day_frame_layout.background = null
        }
        addClientsToCalendar(container, day)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val clientLine : View = view.findViewById(R.id.client_line)
        lateinit var day : CalendarDay
        val calendar_day_text_view : TextView = view.findViewById(R.id.calendar_day_text_view)
        val calendar_day_frame_layout : FrameLayout = view.findViewById(R.id.calendar_day_frame_layout)

        init {
            calendar_day_frame_layout.setOnClickListener{
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
            calendar_day_frame_layout.setOnLongClickListener { v : View? ->
                if(day.owner == DayOwner.THIS_MONTH){
                    val client = viewModel.getCurrentDateClient(day.date)
                    if(client != null && client.checkInDate.isNotEmpty()){
                        viewModel.removeClientFromFireBaseDB(client)
                        Toast.makeText(context, context.resources.getString(R.string.client_was_removed), Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
            dayViewContainer.calendar_day_text_view.setTextColor(context.getColor(R.color.example_5_text_grey_light))
        }
    }
}