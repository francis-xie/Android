
package com.basic.code.fragment.expands;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.expands.calendar.ChineseCalendarFragment;
import com.basic.code.fragment.expands.calendar.DingDingCalendarFragment;
import com.basic.code.fragment.expands.calendar.MaterialDesignCalendarFragment;
import com.basic.code.fragment.expands.calendar.SimpleCalendarFragment;

/**
 
 * @since 2019-05-29 19:48
 */
@Page(name = "日历", extra = R.drawable.ic_expand_calendar)
public class CalendarFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                SimpleCalendarFragment.class,
                DingDingCalendarFragment.class,
                MaterialDesignCalendarFragment.class,
                ChineseCalendarFragment.class

        };
    }
}
