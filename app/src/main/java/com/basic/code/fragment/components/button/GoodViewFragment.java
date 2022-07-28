
package com.basic.code.fragment.components.button;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.basic.page.annotation.Page;
import com.basic.face.widget.popupwindow.good.GoodView;
import com.basic.face.widget.popupwindow.good.IGoodView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "GoodView\n按钮点击漂浮效果")
public class GoodViewFragment extends BaseFragment {

    private IGoodView mGoodView;

    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.iv_4)
    ImageView iv4;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_good_view;
    }

    @Override
    protected void initViews() {
        mGoodView = new GoodView(getContext());
    }

    @OnClick({R.id.btn_reset, R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                reset();
                break;
            case R.id.iv_1:
                iv1.setImageResource(R.drawable.ic_good_checked);
                mGoodView.setText("+1")
                        .setTextColor(Color.parseColor("#f66467"))
                        .setTextSize(12)
                        .show(view);
                break;
            case R.id.iv_2:
                iv2.setImageResource(R.drawable.ic_good_checked);
                mGoodView.setImageResource(R.drawable.ic_good_checked)
                        .show(view);
                break;
            case R.id.iv_3:
                iv3.setImageResource(R.drawable.ic_collection_checked);
                mGoodView.setTextInfo("收藏成功", Color.parseColor("#f66467"), 12)
                        .show(view);
                break;
            case R.id.iv_4:
                iv4.setImageResource(R.drawable.ic_bookmark_checked);
                mGoodView.setTextInfo("收藏成功", Color.parseColor("#ff941A"), 12)
                        .show(view);
                break;
            default:
                break;
        }
    }

    public void reset() {
        iv1.setImageResource(R.drawable.ic_good);
        iv2.setImageResource(R.drawable.ic_good);
        iv3.setImageResource(R.drawable.ic_collection);
        iv4.setImageResource(R.drawable.ic_bookmark);
        mGoodView.reset();
    }
}
