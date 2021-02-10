package com.applandeo.materialcalendarview.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.R;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.applandeo.materialcalendarview.utils.DayColorsUtils;
import com.applandeo.materialcalendarview.utils.EventDayUtils;
import com.applandeo.materialcalendarview.utils.SelectedDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;

/**
 * This class is responsible for loading a one day cell.
 * <p>
 * Created by Mateusz Kornakiewicz on 24.05.2017.
 */

class CalendarDayAdapter extends ArrayAdapter<Date> {
    private CalendarPageAdapter mCalendarPageAdapter;
    private LayoutInflater mLayoutInflater;
    private int mPageMonth;
    private Calendar mToday = DateUtils.getCalendar();

    private CalendarProperties mCalendarProperties;

    CalendarDayAdapter(CalendarPageAdapter calendarPageAdapter, Context context, CalendarProperties calendarProperties,
                       ArrayList<Date> dates, int pageMonth) {
        super(context, calendarProperties.getItemLayoutResource(), dates);
        mCalendarPageAdapter = calendarPageAdapter;
        mCalendarProperties = calendarProperties;
        mPageMonth = pageMonth < 0 ? 11 : pageMonth;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = mLayoutInflater.inflate(mCalendarProperties.getItemLayoutResource(), parent, false);
        }

        LinearLayout linearLayout = view.findViewById(R.id.container);
        TextView dayLabel = view.findViewById(R.id.dayLabel);
        TextView dayDescription = view.findViewById(R.id.dayDescription);
        View cornerView = view.findViewById(R.id.cornerView);

        Calendar day = new GregorianCalendar();
        day.setTime(getItem(position));

        dayLabel.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));

        if (mToday.equals(day)){
            dayLabel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            dayLabel.setTextColor(mCalendarProperties.getTodayLabelColor());
        }

        Stream.of(mCalendarProperties.getEventDays()).filter(eventDate -> {
            Calendar eventDateCalendar = eventDate.getCalendar();
            return eventDateCalendar.get(Calendar.YEAR) == day.get(Calendar.YEAR) && eventDateCalendar.get(Calendar.MONTH) == day.get(Calendar.MONTH) && eventDateCalendar.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH);
        }).findFirst().executeIfPresent(eventDay -> {
            dayDescription.setText(eventDay.getDescription());
            cornerView.setVisibility(View.VISIBLE);
            dayLabel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            dayLabel.setTextColor(eventDay.getLabelColor());
            dayDescription.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            dayDescription.setTextColor(eventDay.getLabelColor());
        }).executeIfAbsent(() -> {
            dayDescription.setText("");
            cornerView.setVisibility(View.INVISIBLE);
        });

        if (isSelectedDay(day)) {
            dayLabel.setTextColor(mCalendarProperties.getSelectionLabelColor());
            dayDescription.setTextColor(mCalendarProperties.getSelectionLabelColor());
            linearLayout.setBackgroundColor(mCalendarProperties.getSelectionColor());
        } else {
            linearLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        if (!isCurrentMonthDay(day)) {
            dayLabel.setTextColor(mCalendarProperties.getAnotherMonthsDaysLabelsColor());
            dayLabel.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            dayLabel.setAlpha(0.12f);
            dayDescription.setAlpha(0.12f);
            cornerView.setAlpha(0.12f);
        }

        return view;
    }



    private boolean isSelectedDay(Calendar day) {
        return mCalendarProperties.getCalendarType() != CalendarView.CLASSIC && day.get(Calendar.MONTH) == mPageMonth
                && mCalendarPageAdapter.getSelectedDays().contains(new SelectedDay(day));
    }

    private boolean isCurrentMonthDay(Calendar day) {
        return day.get(Calendar.MONTH) == mPageMonth &&
                !((mCalendarProperties.getMinimumDate() != null && day.before(mCalendarProperties.getMinimumDate()))
                        || (mCalendarProperties.getMaximumDate() != null && day.after(mCalendarProperties.getMaximumDate())));
    }

}
