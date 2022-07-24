package com.basic.date;

/**
 *
 *

 * @since 2019/5/28 14:29
 */
public interface CalendarTopView {

    int[] getCurrentSelectRect();

    int getCurrentSelectPosition();

    CalendarDate getSelectCalendarDate();

    int getItemHeight();

    void setCaledarTopViewChangeListener(CaledarTopViewChangeListener listener);

}
