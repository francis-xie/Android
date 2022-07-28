
package com.basic.code.fragment.utils.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.basic.page.annotation.Page;
import com.basic.face.utils.ViewUtils;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "控件Padding")
public class ViewPaddingFragment extends BaseFragment {
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;

    @BindView(R.id.seekBar)
    SeekBar mSeekBar;

    @BindView(R.id.seekBar1)
    SeekBar mSeekBar1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_padding;
    }

    @Override
    protected void initViews() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewUtils.setPaddingLeft(iv1, progress);
                ViewUtils.setPaddingRight(iv1, progress);

                ViewUtils.setPaddingTop(iv2, progress);
                ViewUtils.setPaddingBottom(iv2, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewUtils.expendTouchArea(iv3, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick({R.id.iv_1, R.id.iv_2, R.id.iv_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_1:
                XToastUtils.toast("点击图片1");
                break;
            case R.id.iv_2:
                XToastUtils.toast("点击图片2");
                break;
            case R.id.iv_3:
                XToastUtils.toast("点击图片3");
                break;
            default:
                break;
        }
    }
}
