package com.applandeo.materialcalendarview.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class is used to set a style of calendar cells.
 * <p>
 * Created by Mateusz Kornakiewicz on 24.05.2017.
 */

public class DayColorsUtils {



    /**
     * This method sets a color of the text, font type and a background of a TextView object.
     * It is used to set day cell (numbers) style in the case of selected day (when calendar is in
     * the picker mode). It also colors a background of the selection.
     *
     * @param dayLabel           TextView containing a day number
     * @param calendarProperties A resource of a selection background color
     */
    public static void setSelectedDayColors(LinearLayout container, TextView dayLabel, TextView dayDescription, CalendarProperties calendarProperties) {
        dayLabel.setTextColor(calendarProperties.getSelectionLabelColor());
        dayDescription.setTextColor(calendarProperties.getSelectionLabelColor());
        container.setBackgroundColor(calendarProperties.getSelectionColor());
    }

    public static void setupDay(View view, Calendar day, Calendar today, CalendarProperties calendarProperties, int pageMonths, boolean isSelectedDay){
        LinearLayout linearLayout = view.findViewById(R.id.container);
        TextView dayLabel = view.findViewById(R.id.dayLabel);
        TextView dayDescription = view.findViewById(R.id.dayDescription);
        View cornerView = view.findViewById(R.id.cornerView);



        dayLabel.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));

        if (today.equals(day)){
            dayLabel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            dayLabel.setTextColor(calendarProperties.getTodayLabelColor());
        }

        Stream.of(calendarProperties.getEventDays()).filter(eventDate -> {
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

        if (isSelectedDay) {
            dayLabel.setTextColor(calendarProperties.getSelectionLabelColor());
            dayDescription.setTextColor(calendarProperties.getSelectionLabelColor());
            linearLayout.setBackgroundColor(calendarProperties.getSelectionColor());
        } else {
            linearLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        if (!isCurrentMonthDay(day, calendarProperties, pageMonths)) {
            dayLabel.setTextColor(calendarProperties.getAnotherMonthsDaysLabelsColor());
            dayLabel.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            dayLabel.setAlpha(0.12f);
            dayDescription.setAlpha(0.12f);
            cornerView.setAlpha(0.12f);
        }
    }



    private static boolean isCurrentMonthDay(Calendar day, CalendarProperties calendarProperties, int pageMonth) {
        return day.get(Calendar.MONTH) == pageMonth &&
                !((calendarProperties.getMinimumDate() != null && day.before(calendarProperties.getMinimumDate()))
                        || (calendarProperties.getMaximumDate() != null && day.after(calendarProperties.getMaximumDate())));
    }


}
