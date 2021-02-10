package com.applandeo.materialcalendarview.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.R;

import java.util.Calendar;

/**
 * This class is used to set a style of calendar cells.
 * <p>
 * Created by Mateusz Kornakiewicz on 24.05.2017.
 */

public class DayColorsUtils {

    /**
     * This is general method which sets a color of the text, font type and a background of a TextView object.
     * It is used to set day cell (numbers) style.
     *
     * @param textView   TextView containing a day number
     * @param textColor  A resource of a color of the day number
     * @param typeface   A type of text style, can be set as NORMAL or BOLD
     * @param background A resource of a background drawable
     */
    public static void setDayColors(TextView textView, int textColor, int typeface, int background) {
        if (textView == null) {
            return;
        }

        textView.setTypeface(null, typeface);
        textView.setTextColor(textColor);
        textView.setBackgroundResource(background);
    }

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

    /**
     * This method is used to set a color of texts, font types and backgrounds of TextView objects
     * in a current visible month. Visible day labels from previous and forward months are set using
     * setDayColors() method. It also checks if a day number is a day number of today and set it
     * a different color and bold face type.
     *
     * @param day                A calendar instance representing day date
     * @param today              A calendar instance representing today date
     * @param dayLabel           TextView containing a day numberx
     * @param calendarProperties A resource of a color used to mark today day
     */
    public static void setCurrentMonthDayColors(Calendar day, Calendar today, TextView dayLabel, TextView dayDescription,
                                                CalendarProperties calendarProperties) {
        setDayBackgroundColor(dayLabel, Color.TRANSPARENT);
        ((ViewGroup) dayLabel.getParent()).findViewById(R.id.cornerView).setBackgroundColor(Color.TRANSPARENT);
        if (EventDayUtils.isEventDayWithLabelColor(day, calendarProperties)) {
            setEventDayColors(day, dayLabel, dayDescription, calendarProperties);
            ((ViewGroup) dayLabel.getParent()).findViewById(R.id.cornerView).setBackgroundResource(R.drawable.background_event);
        } else if (today.equals(day)) {
            setTodayColors(dayLabel, dayDescription, calendarProperties);
        } else if (calendarProperties.getHighlightedDays().contains(day)) {
            setHighlightedDayColors(dayLabel, dayDescription, calendarProperties);
        } else {
            setNormalDayColors(dayLabel, dayDescription, calendarProperties);
        }
    }

    private static void setTodayColors(TextView dayLabel, TextView dayDescription, CalendarProperties calendarProperties) {
        setDayColors(dayLabel, calendarProperties.getTodayLabelColor(), Typeface.BOLD,
                R.drawable.background_transparent);
        setDayColors(dayDescription, calendarProperties.getTodayLabelColor(), Typeface.ITALIC,
                R.drawable.background_transparent);

    }

    private static void setEventDayColors(Calendar day, TextView dayLabel, TextView dayDescription, CalendarProperties calendarProperties) {
        EventDayUtils.getEventDayWithLabelColor(day, calendarProperties).executeIfPresent(eventDay -> {
                DayColorsUtils.setDayColors(dayLabel, eventDay.getLabelColor(),
                        Typeface.BOLD, R.drawable.background_transparent);
            DayColorsUtils.setDayColors(dayDescription, eventDay.getLabelColor(),
                    Typeface.ITALIC, R.drawable.background_transparent);
        });

    }

    private static void setHighlightedDayColors(TextView dayLabel, TextView dayDescription, CalendarProperties calendarProperties) {
        setDayColors(dayLabel, calendarProperties.getHighlightedDaysLabelsColor(),
                Typeface.NORMAL, R.drawable.background_transparent);
        setDayColors(dayDescription, calendarProperties.getHighlightedDaysLabelsColor(),
                Typeface.ITALIC, R.drawable.background_transparent);
    }

    private static void setNormalDayColors(TextView dayLabel, TextView dayDescription, CalendarProperties calendarProperties) {
        setDayColors(dayLabel, calendarProperties.getDaysLabelsColor(), Typeface.NORMAL,
                R.drawable.background_transparent);
        setDayColors(dayDescription, calendarProperties.getDaysLabelsColor(), Typeface.ITALIC,
                R.drawable.background_transparent);
    }

    private static void setDayBackgroundColor(TextView dayLabel, int color) {
        ((ViewGroup) dayLabel.getParent().getParent().getParent()).setBackgroundColor(color);
    }
}
