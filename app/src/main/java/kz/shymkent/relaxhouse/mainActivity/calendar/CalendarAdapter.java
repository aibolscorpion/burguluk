package kz.shymkent.relaxhouse.mainActivity.calendar;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.threeten.bp.LocalDate;

import kz.shymkent.relaxhouse.mainActivity.MainActivity;
import kz.shymkent.relaxhouse.mainActivity.MainActivityViewModel;
import kz.shymkent.relaxhouse.R;
import kz.shymkent.relaxhouse.models.Client;

public class CalendarAdapter implements com.kizitonwose.calendarview.ui.DayBinder<CalendarAdapter.DayViewContainer> {
    org.threeten.bp.LocalDate selectedDate,oldDate;
    Context context;
    MainActivityViewModel viewModel;

    public CalendarAdapter(Context context, MainActivityViewModel viewModel){
        this.context = context;
        this.viewModel = viewModel;
    }
    @Override
    public DayViewContainer create(View view) {
        return new DayViewContainer(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bind(DayViewContainer dayViewContainer, CalendarDay calendarDay) {
        dayViewContainer.day = calendarDay;
        dayViewContainer.calendar_day_text_view.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
        if(calendarDay.getDate().equals(selectedDate)){
            dayViewContainer.calendar_day_frame_layout.setBackgroundResource(R.drawable.selected_day_bg);
        }else if(calendarDay.getDate().equals(LocalDate.now())){
            dayViewContainer.calendar_day_frame_layout.setBackgroundResource(R.drawable.current_day_bg);
        }else{
            dayViewContainer.calendar_day_frame_layout.setBackground(null);
        }
            addClientsToCalendar(dayViewContainer,calendarDay);
    }

    class DayViewContainer extends ViewContainer {
        View clientTop,clientBottom;
        CalendarDay day;
        public TextView calendar_day_text_view;
        public FrameLayout calendar_day_frame_layout;
        public DayViewContainer(View view) {
            super(view);
            clientTop = view.findViewById(R.id.clientTop);
            clientBottom = view.findViewById(R.id.clientBottom);
            calendar_day_frame_layout = view.findViewById(R.id.calendar_day_frame_layout);
            calendar_day_text_view = view.findViewById(R.id.calendar_day_text_view);

                calendar_day_frame_layout.setOnClickListener(view1 -> {
                if(day.getOwner() == DayOwner.THIS_MONTH){
                    oldDate = selectedDate;
                    selectedDate = day.getDate();
                    if(oldDate!=null){
                        ((MainActivity)context).calendarView.notifyDateChanged(oldDate);
                    }
                    ((MainActivity)context).calendarView.notifyDateChanged(selectedDate);
                    viewModel.getCurrentDateClientList(selectedDate,true);
                }
                });

                calendar_day_frame_layout.setOnLongClickListener(v -> {
                    if(day.getOwner() == DayOwner.THIS_MONTH){
                        Client client = viewModel.getClientByDate(day.getDate());
                        if(!client.getCheckInTime().isEmpty()){
                            viewModel.removeClientFromFireBaseDB(client);
                            Toast.makeText(context, context.getResources().getString(R.string.client_was_removed), Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                });
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addClientsToCalendar(DayViewContainer dayViewContainer, CalendarDay calendarDay){
        if(calendarDay.getOwner() == DayOwner.THIS_MONTH){
            int size = viewModel.getCurrentDateClientList(calendarDay.getDate(),false);
            if(size == 1){
                dayViewContainer.clientTop.setBackgroundColor(context.getColor(R.color.calendar_text_view_color));
                dayViewContainer.clientBottom.setBackground(null);
            }else if(size == 2){
                dayViewContainer.clientTop.setBackgroundColor(context.getColor(R.color.calendar_text_view_color));
                dayViewContainer.clientBottom.setBackgroundColor(context.getColor(R.color.calendar_text_view_color));
            }else{
                dayViewContainer.clientTop.setBackground(null);
                dayViewContainer.clientBottom.setBackground(null);
            }
        }else{
            dayViewContainer.clientTop.setBackground(null);
            dayViewContainer.clientBottom.setBackground(null);
                dayViewContainer.calendar_day_text_view.setTextColor(context.getColor(R.color.example_5_text_grey_light));
        }
        }
}
