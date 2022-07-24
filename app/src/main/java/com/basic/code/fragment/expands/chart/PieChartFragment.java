package com.basic.code.fragment.expands.chart;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.expands.chart.pie.BasicPieChartFragment;
import com.basic.code.fragment.expands.chart.pie.HalfPieChartFragment;

/**

 * @since 2019/4/10 上午12:03
 */
@Page(name = "PieChart\n饼图")
public class PieChartFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                BasicPieChartFragment.class,
                HalfPieChartFragment.class
        };
    }
}
