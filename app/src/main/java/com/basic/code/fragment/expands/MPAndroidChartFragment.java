package com.basic.code.fragment.expands;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.expands.chart.BarChartFragment;
import com.basic.code.fragment.expands.chart.LineChartFragment;
import com.basic.code.fragment.expands.chart.PieChartFragment;
import com.basic.code.fragment.expands.chart.RadarChartFragment;
import com.basic.code.fragment.expands.chart.echarts.EChartsFragment;
import com.basic.code.utils.Utils;

@Page(name = "图表", extra = R.drawable.ic_expand_chart)
public class MPAndroidChartFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                LineChartFragment.class,
                BarChartFragment.class,
                PieChartFragment.class,
                RadarChartFragment.class,
                EChartsFragment.class
        };
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/PhilJay/MPAndroidChart");
            }
        });
        return titleBar;
    }
}
