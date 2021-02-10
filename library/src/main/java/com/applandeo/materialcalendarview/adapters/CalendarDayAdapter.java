package com.applandeo.materialcalendarview.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        View cornerView = view.findViewById(R.id.cornerView);
        TextView dayLabel = view.findViewById(R.id.dayLabel);
        TextView dayDescription = view.findViewById(R.id.dayDescription);

        Calendar day = new GregorianCalendar();
        day.setTime(getItem(position));

        setLabelColors(view, day);

        Stream.of(mCalendarProperties.getEventDays()).filter(eventDate -> {
            Calendar eventDateCalendar = eventDate.getCalendar();
            return eventDateCalendar.get(Calendar.YEAR) == day.get(Calendar.YEAR) && eventDateCalendar.get(Calendar.MONTH) == day.get(Calendar.MONTH) && eventDateCalendar.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH);
        }).findFirst().executeIfPresent(eventDay -> {
            dayDescription.setText(eventDay.getDescription());
            cornerView.setVisibility(isCurrentMonthDay(day) ? View.VISIBLE : View.INVISIBLE);
        }).executeIfAbsent(() -> {
            dayDescription.setText("");
            cornerView.setVisibility(View.INVISIBLE);
        });

        dayLabel.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
        return view;
    }

    private void setLabelColors(View view, Calendar day) {
        TextView dayLabel = view.findViewById(R.id.dayLabel);
        TextView dayDescription = view.findViewById(R.id.dayDescription);
        // Setting not current month day color
        if (!isCurrentMonthDay(day)) {
            DayColorsUtils.setDayColors(dayLabel, mCalendarProperties.getAnotherMonthsDaysLabelsColor(),
                    Typeface.NORMAL, R.drawable.background_transparent);
            DayColorsUtils.setDayColors(dayDescription, Color.TRANSPARENT,
                    Typeface.ITALIC, R.drawable.background_transparent);
            return;
        }

        // Setting view for all SelectedDays
        if (isSelectedDay(day)) {
            Stream.of(mCalendarPageAdapter.getSelectedDays())
                    .filter(selectedDay -> selectedDay.getCalendar().equals(day))
                    .findFirst().ifPresent(selectedDay -> selectedDay.setView(view));
            DayColorsUtils.setSelectedDayColors(view, dayLabel, dayDescription, mCalendarProperties);
            return;
        }

        // Setting disabled days color
        if (!isActiveDay(day)) {
            DayColorsUtils.setDayColors(dayLabel, mCalendarProperties.getDisabledDaysLabelsColor(),
                    Typeface.NORMAL, R.drawable.background_transparent);
            DayColorsUtils.setDayColors(dayLabel, Color.TRANSPARENT,
                    Typeface.ITALIC, R.drawable.background_transparent);
            return;
        }

        // Setting custom label color for event day
        if (isEventDayWithLabelColor(day)) {
            DayColorsUtils.setCurrentMonthDayColors(day, mToday, view, mCalendarProperties);
            return;
        }

        // Setting current month day color
        DayColorsUtils.setCurrentMonthDayColors(day, mToday, view, mCalendarProperties);
    }

    private boolean isSelectedDay(Calendar day) {
        return mCalendarProperties.getCalendarType() != CalendarView.CLASSIC && day.get(Calendar.MONTH) == mPageMonth
                && mCalendarPageAdapter.getSelectedDays().contains(new SelectedDay(day));
    }

    private boolean isEventDayWithLabelColor(Calendar day) {
        return EventDayUtils.isEventDayWithLabelColor(day, mCalendarProperties);
    }

    private boolean isCurrentMonthDay(Calendar day) {
        return day.get(Calendar.MONTH) == mPageMonth &&
                !((mCalendarProperties.getMinimumDate() != null && day.before(mCalendarProperties.getMinimumDate()))
                        || (mCalendarProperties.getMaximumDate() != null && day.after(mCalendarProperties.getMaximumDate())));
    }

    private boolean isActiveDay(Calendar day) {
        return !mCalendarProperties.getDisabledDays().contains(day);
    }

}
