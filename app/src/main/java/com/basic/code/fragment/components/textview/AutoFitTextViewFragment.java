
package com.basic.code.fragment.components.textview;

import android.widget.TextView;

import com.basic.page.annotation.Page;
import com.basic.face.widget.textview.autofit.AutoFitTextView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.tools.common.RandomUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "AutoFitTextView\n自适应字体大小的文字")
public class AutoFitTextViewFragment extends BaseFragment {
    @BindView(R.id.tv_in_auto_fit)
    TextView tvInAutoFit;
    @BindView(R.id.aftv)
    AutoFitTextView aftv;
    @BindView(R.id.tv)
    TextView tv;

    StringBuilder sb = new StringBuilder();

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_autofit_textview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        setText(sb);
    }


    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        sb.append(RandomUtils.getRandomNumbersAndLetters(2));
        setText(sb);
    }


    private void setText(StringBuilder sb) {
        tvInAutoFit.setText(sb);
        aftv.setText(sb);
        tv.setText(sb);
    }
}
