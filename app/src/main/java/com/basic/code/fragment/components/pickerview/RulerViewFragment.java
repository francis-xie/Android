package com.basic.code.fragment.components.pickerview;

import android.view.View;
import android.widget.EditText;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.picker.RulerView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.common.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "RulerView\n支持选择身高、体重、视力的尺子")
public class RulerViewFragment extends BaseFragment {

    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.rulerView)
    RulerView rulerView;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ruler_view;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {


    }


    @SingleClick
    @OnClick({R.id.btn_set, R.id.btn_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set:
                if (!StringUtils.isEmpty(etWeight.getText().toString())) {
                    rulerView.setCurrentValue(StringUtils.toFloat(etWeight.getText().toString(), 0));
                } else {
                    XToastUtils.toast("请输入体重值！");
                }
                break;
            case R.id.btn_get:
                XToastUtils.toast("体重：" + rulerView.getCurrentValue());
                break;
            default:
                break;
        }
    }
}
