package com.applandeo.materialcalendarview.utils;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;
public class EventDayUtils {

    /**
     * This method is used to check whether this day is an event day with provided custom label color.
     *
     * @param day                A calendar instance representing day date
     * @param calendarProperties A calendar properties
     */
    public static boolean isEventDayWithLabelColor(Calendar day, CalendarProperties calendarProperties) {
        if (calendarProperties.getEventDays() != null || calendarProperties.getEventsEnabled()) {
            return Stream.of(calendarProperties.getEventDays()).anyMatch(eventDate -> {
                Calendar eventDateCalendar = eventDate.getCalendar();
                return eventDateCalendar.get(Calendar.YEAR) == day.get(Calendar.YEAR) && eventDateCalendar.get(Calendar.MONTH) == day.get(Calendar.MONTH) && eventDateCalendar.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH) && eventDate.getLabelColor() != 0;
            });
        }

        return false;
    }

    /**
     * This method is used to get event day which contains custom label color.
     *
     * @param day                A calendar instance representing day date
     * @param calendarProperties A calendar properties
     */
    public static Optional<EventDay> getEventDayWithLabelColor(Calendar day, CalendarProperties calendarProperties) {
        return Stream.of(calendarProperties.getEventDays())
                .filter(eventDate -> {
                    Calendar eventDateCalendar = eventDate.getCalendar();
                    return eventDateCalendar.get(Calendar.YEAR) == day.get(Calendar.YEAR) && eventDateCalendar.get(Calendar.MONTH) == day.get(Calendar.MONTH) && eventDateCalendar.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH) && eventDate.getLabelColor() != 0;
                })
                .findFirst();
    }
}
