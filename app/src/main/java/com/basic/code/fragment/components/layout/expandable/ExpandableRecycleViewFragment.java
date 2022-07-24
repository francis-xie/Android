
package com.basic.code.fragment.components.layout.expandable;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.page.annotation.Page;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.ExpandableListAdapter;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

/**

 * @since 2019-11-22 15:33
 */
@Page(name = "在RecycleView中使用")
public class ExpandableRecycleViewFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.layout_common_recycleview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(recyclerView);

        recyclerView.setAdapter(new ExpandableListAdapter(recyclerView, DemoDataProvider.getDemoData1()));
    }
}
