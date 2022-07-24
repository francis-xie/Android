package com.basic.code.fragment.expands.chart;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.expands.chart.bar.BasicBarChartFragment;
import com.basic.code.fragment.expands.chart.bar.SimpleBarChartFragment;
import com.basic.code.fragment.expands.chart.bar.HorizontalBarChartFragment;

/**

 * @since 2019/4/10 上午12:00
 */
@Page(name = "BarChart\n柱状图")
public class BarChartFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                BasicBarChartFragment.class,
                SimpleBarChartFragment.class,
                HorizontalBarChartFragment.class
        };
    }
}
