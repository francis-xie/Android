package com.basic.code.fragment.components.textview;

import com.basic.page.annotation.Page;
import com.basic.face.widget.textview.ExpandableTextView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;

/**

 * @date 2017/10/27 下午3:33
 */
@Page(name = "可伸缩折叠的TextView")
public class ExpandableTextViewFragment extends BaseFragment {
    @BindView(R.id.expand_text_view)
    ExpandableTextView mExpandableTextView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_expandabletextview;
    }

    @Override
    protected void initViews() {
        mExpandableTextView.setText(getString(R.string.etv_content_demo1));
        mExpandableTextView.setOnExpandStateChangeListener((textView, isExpanded) -> XToastUtils.toast(isExpanded ? "Expanded" : "Collapsed"));

    }

    @Override
    protected void initListeners() {

    }
}
