package com.basic.date;

public interface CalendarTopView {

    int[] getCurrentSelectRect();

    int getCurrentSelectPosition();

    CalendarDate getSelectCalendarDate();

    int getItemHeight();

    void setCaledarTopViewChangeListener(CaledarTopViewChangeListener listener);

}
