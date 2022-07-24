package com.basic.code.fragment.components.flowlayout;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.flowlayout.FlowTagLayout;
import com.basic.code.R;
import com.basic.code.adapter.FlowTagAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 
 * @date 2017/11/21 上午10:09
 */
@Page(name = "FlowTagLayout\n流标签")
public class FlowTagLayoutFragment extends BaseFragment {
    @BindView(R.id.flowlayout_normal_select)
    FlowTagLayout mNormalFlowTagLayout;

    @BindView(R.id.flowlayout_single_select)
    FlowTagLayout mSingleFlowTagLayout;

    @BindView(R.id.flowlayout_single_select_cancelable)
    FlowTagLayout mSingleCancelableFlowTagLayout;

    @BindView(R.id.flowlayout_multi_select)
    FlowTagLayout mMultiFlowTagLayout;

    @BindView(R.id.flowlayout_display)
    FlowTagLayout mDisplayFlowTagLayout;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("清除") {
            @Override
            public void performAction(View view) {
                mSingleCancelableFlowTagLayout.clearSelection();
                mMultiFlowTagLayout.clearSelection();
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_flowtaglayout;
    }

    @Override
    protected void initViews() {
        initNormalFlowTagLayout();
        initSingleFlowTagLayout();
        initSingleCancelableFlowTagLayout();
        initMultiFlowTagLayout();
    }

    @Override
    protected void initListeners() {

    }

    private void initNormalFlowTagLayout() {
        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        mNormalFlowTagLayout.setAdapter(tagAdapter);
        mNormalFlowTagLayout.setOnTagClickListener((parent, view, position) -> XToastUtils.toast("点击了：" + parent.getAdapter().getItem(position)));
        tagAdapter.addTags(ResUtils.getStringArray(R.array.tags_values));
    }

    private void initSingleFlowTagLayout() {
        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        mSingleFlowTagLayout.setAdapter(tagAdapter);
        mSingleFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mSingleFlowTagLayout.setOnTagSelectListener((parent, position, selectedList) -> XToastUtils.toast(getSelectedText(parent, selectedList)));
        tagAdapter.addTags(ResUtils.getStringArray(R.array.tags_values));
        tagAdapter.setSelectedPositions(2, 3, 4);

    }

    private void initSingleCancelableFlowTagLayout() {
        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        mSingleCancelableFlowTagLayout.setAdapter(tagAdapter);
        mSingleCancelableFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mSingleCancelableFlowTagLayout.setOnTagSelectListener((parent, position, selectedList) -> XToastUtils.toast(getSelectedText(parent, selectedList)));
        tagAdapter.addTags(ResUtils.getStringArray(R.array.tags_values));
        tagAdapter.setSelectedPositions(2, 3, 4);

    }

    private void initMultiFlowTagLayout() {
        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        mMultiFlowTagLayout.setAdapter(tagAdapter);
        mMultiFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mMultiFlowTagLayout.setOnTagSelectListener((parent, position, selectedList) -> XToastUtils.toast(getSelectedText(parent, selectedList)));
        tagAdapter.addTags(ResUtils.getStringArray(R.array.tags_values));
        tagAdapter.setSelectedPositions(2, 3, 4);

//        mMultiFlowTagLayout.setItems("1111", "2222", "3333", "4444");
//        mMultiFlowTagLayout.setSelectedItems("3333", "4444");

    }

    private String getSelectedText(FlowTagLayout parent, List<Integer> selectedList) {
        StringBuilder sb = new StringBuilder("选中的内容：\n");
        for (int index : selectedList) {
            sb.append(parent.getAdapter().getItem(index));
            sb.append(";");
        }
        return sb.toString();
    }

    @OnClick({R.id.btn_add_tag, R.id.btn_clear_tag})
    void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_add_tag:
                mDisplayFlowTagLayout.addTag("标签" + (int)(Math.random() * 100));
                break;
            case R.id.btn_clear_tag:
                mDisplayFlowTagLayout.clearTags();
                break;
            default:
                break;
        }
    }

}
