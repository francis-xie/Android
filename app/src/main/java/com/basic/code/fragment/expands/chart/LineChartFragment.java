package com.basic.code.fragment.expands.chart;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.expands.chart.line.BasicLineChartFragment;
import com.basic.code.fragment.expands.chart.line.MultiLineChartFragment;

/**
 
 * @since 2019/4/9 下午11:52
 */
@Page(name = "LineChart\n折线图")
public class LineChartFragment extends ComponentContainerFragment {

    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                BasicLineChartFragment.class,
                MultiLineChartFragment.class
        };
    }
}
