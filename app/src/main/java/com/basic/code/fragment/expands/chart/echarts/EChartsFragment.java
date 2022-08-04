
package com.basic.code.fragment.expands.chart.echarts;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.base.web.PageWebViewFragment;
import com.basic.code.utils.Utils;

import java.util.List;

@Page(name = "ECharts\n非常丰富的web图表组件")
public class EChartsFragment extends BaseSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("简单的ECharts使用（直接使用Js）");
        lists.add("使用Android原生封装调用ECharts");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:
                PageWebViewFragment.openUrl(this, "file:///android_asset/chart/src/index.html");
                break;
            case 1:
                openPage(EChartsAndroidFragment.class);
                break;
            default:
                break;
        }
    }


    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("官网") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://echarts.baidu.com/index.html");
            }
        });
        return titleBar;
    }
}
