
package com.basic.code.fragment.expands.materialdesign.constraintlayout;

import android.view.View;
import android.widget.CompoundButton;

import androidx.constraintlayout.widget.Group;

import com.basic.page.annotation.Page;
import com.basic.face.widget.button.switchbutton.SwitchButton;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

@Page(name = "分组Group使用")
public class ConstraintLayoutGroupFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.sb_group)
    SwitchButton sbGroup;
    @BindView(R.id.sb_group2)
    SwitchButton sbGroup2;
    @BindView(R.id.group)
    Group group;
    @BindView(R.id.group2)
    Group group2;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_constraint_group;
    }

    @Override
    protected void initViews() {
        sbGroup.setChecked(true);
        sbGroup2.setChecked(true);
    }

    @Override
    protected void initListeners() {
        sbGroup.setOnCheckedChangeListener(this);
        sbGroup2.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sb_group:
                group.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.sb_group2:
                group2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            default:
                break;
        }
    }
}
