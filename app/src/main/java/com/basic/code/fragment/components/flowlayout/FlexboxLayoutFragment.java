
package com.basic.code.fragment.components.flowlayout;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.adapter.FlexboxLayoutAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.common.StringUtils;

import butterknife.BindView;

/**

 * @since 2019-11-23 01:23
 */
@Page(name = "FlexboxLayoutManager + RecyclerView\n流标签")
public class FlexboxLayoutFragment extends BaseFragment {

    @BindView(R.id.recycler_view_1)
    RecyclerView recyclerView1;
    @BindView(R.id.recycler_view_2)
    RecyclerView recyclerView2;
    @BindView(R.id.recycler_view_3)
    RecyclerView recyclerView3;
    @BindView(R.id.recycler_view_4)
    RecyclerView recyclerView4;

    private FlexboxLayoutAdapter mAdapter1;
    private FlexboxLayoutAdapter mAdapter2;
    private FlexboxLayoutAdapter mAdapter3;
    private FlexboxLayoutAdapter mAdapter4;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_flexbox_layout;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("清除") {
            @SingleClick
            @Override
            public void performAction(View view) {
                mAdapter3.clearSelection();
                mAdapter4.clearSelection();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        String[] array = ResUtils.getStringArray(R.array.tags_values);

        recyclerView1.setLayoutManager(Utils.getFlexboxLayoutManager(getContext()));
        recyclerView1.setAdapter(mAdapter1 = new FlexboxLayoutAdapter(array));

        recyclerView2.setLayoutManager(Utils.getFlexboxLayoutManager(getContext()));
        recyclerView2.setAdapter(mAdapter2 = new FlexboxLayoutAdapter(array));

        recyclerView3.setLayoutManager(Utils.getFlexboxLayoutManager(getContext()));
        recyclerView3.setAdapter(mAdapter3 = new FlexboxLayoutAdapter(array).setCancelable(true));

        recyclerView4.setLayoutManager(Utils.getFlexboxLayoutManager(getContext()));
        recyclerView4.setItemAnimator(null);
        recyclerView4.setAdapter(mAdapter4 = new FlexboxLayoutAdapter(array).setIsMultiSelectMode(true));

        mAdapter2.select(2);
        mAdapter3.select(3);
        mAdapter4.multiSelect(1, 2, 3);
    }

    @Override
    protected void initListeners() {
        mAdapter1.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast("点击了：" + item));

        mAdapter2.setOnItemClickListener((itemView, item, position) -> {
            if (mAdapter2.select(position)) {
                XToastUtils.toast("选中的内容：" + mAdapter2.getSelectContent());
            }
        });

        mAdapter3.setOnItemClickListener((itemView, item, position) -> {
            if (mAdapter3.select(position)) {
                XToastUtils.toast("选中的内容：" + mAdapter3.getSelectContent());
            }
        });

        mAdapter4.setOnItemClickListener((itemView, item, position) -> {
            mAdapter4.select(position);
            XToastUtils.toast("选中的内容：" + StringUtils.listToString(mAdapter4.getMultiContent(), ","));
        });
    }
}
