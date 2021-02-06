package com.applandeo.materialcalendarview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.RestrictTo;

import com.applandeo.materialcalendarview.utils.DateUtils;

import java.util.Calendar;

/**
 * This class represents an event of a day. An instance of this class is returned when user click
 * a day cell. This class can be overridden to make calendar more functional. A list of instances of
 * this class can be passed to CalendarView object using setEvents() method.
 * <p>
 * Created by Mateusz Kornakiewicz on 23.05.2017.
 */

public class EventDay {
    private Calendar mDay;
    private int mLabelColor;
    private boolean mIsDisabled;
    private String mDescription;

    /**
     * @param day Calendar object which represents a date of the event
     */
    public EventDay(Calendar day) {
        mDay = day;
    }

    public EventDay(Calendar day, String description, int labelColor){
        mDay = day;
        mDescription = description;
        this.mLabelColor = labelColor;
    }

    /**
     * @return Color which will be displayed as row label text color
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public int getLabelColor() {
        return mLabelColor;
    }


    /**
     * @return Calendar object which represents a date of current event
     */
    public Calendar getCalendar() {
        return mDay;
    }


    /**
     * @return Boolean value if day is not disabled
     */
    public boolean isEnabled() {
        return !mIsDisabled;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void setEnabled(boolean enabled) {
        mIsDisabled = enabled;
    }

    public String getDescription() {
        return mDescription;
    }
}
