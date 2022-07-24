
package com.basic.code.fragment.components.layout;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.widget.layout.FACELayoutHelper;
import com.basic.face.widget.layout.FACELinearLayout;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**

 * @since 2019-06-02 16:50
 */
@Page(name = "FACELayout\n通用布局，支持快速阴影设置")
public class FACELayoutFragment extends BaseFragment {

    @BindView(R.id.radius_tv)
    TextView mRadiusTv;
    @BindView(R.id.alpha_tv)
    TextView mAlphaTv;
    @BindView(R.id.elevation_tv)
    TextView mElevationTv;
    @BindView(R.id.layout_for_test)
    FACELinearLayout mTestLayout;
    @BindView(R.id.test_seekbar_radius)
    SeekBar mRadiusSeekBar;
    @BindView(R.id.test_seekbar_alpha)
    SeekBar mAlphaSeekBar;
    @BindView(R.id.test_seekbar_elevation)
    SeekBar mElevationSeekBar;
    @BindView(R.id.hide_radius_group)
    RadioGroup mHideRadiusGroup;

    private float mShadowAlpha = 0.25f;
    private int mShadowElevationDp = 14;
    private int mRadius;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_face_layout;
    }

    @Override
    protected void initArgs() {
        mRadius = DensityUtils.dp2px(getContext(), 15);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        //设置圆角大小（主要）、阴影大小和阴影透明度
        mRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRadius = DensityUtils.dp2px(getContext(), progress);
                mRadiusTv.setText(String.format("radius: %ddp", progress));
                mTestLayout.setRadiusAndShadow(mRadius,
                        DensityUtils.dp2px(getContext(), mShadowElevationDp),
                        mShadowAlpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置圆角大小、阴影大小和阴影透明度（主要）
        mAlphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShadowAlpha = progress * 1f / 100;
                mAlphaTv.setText(String.format("alpha: %s", mShadowAlpha));
                mTestLayout.setRadiusAndShadow(mRadius,
                        DensityUtils.dp2px(getContext(), mShadowElevationDp),
                        mShadowAlpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置圆角大小、阴影大小（主要）和阴影透明度
        mElevationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShadowElevationDp = progress;
                mElevationTv.setText(String.format("elevation: %ddp", progress));
                mTestLayout.setRadiusAndShadow(mRadius,
                        DensityUtils.dp2px(getActivity(), mShadowElevationDp),
                        mShadowAlpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mRadiusSeekBar.setProgress(15);
        mAlphaSeekBar.setProgress((int) (mShadowAlpha * 100));
        mElevationSeekBar.setProgress(mShadowElevationDp);

        mHideRadiusGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.hide_radius_none:
                    mTestLayout.setRadius(mRadius, FACELayoutHelper.HIDE_RADIUS_SIDE_NONE);
                    break;
                case R.id.hide_radius_left:
                    mTestLayout.setRadius(mRadius, FACELayoutHelper.HIDE_RADIUS_SIDE_LEFT);
                    break;
                case R.id.hide_radius_top:
                    mTestLayout.setRadius(mRadius, FACELayoutHelper.HIDE_RADIUS_SIDE_TOP);
                    break;
                case R.id.hide_radius_bottom:
                    mTestLayout.setRadius(mRadius, FACELayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
                    break;
                case R.id.hide_radius_right:
                    mTestLayout.setRadius(mRadius, FACELayoutHelper.HIDE_RADIUS_SIDE_RIGHT);
                    break;
                default:
                    break;
            }
        });
    }

    @SingleClick
    @OnClick({R.id.shadow_color_red, R.id.shadow_color_blue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shadow_color_red:
                mTestLayout.setShadowColor(0xffff0000);
                break;
            case R.id.shadow_color_blue:
                mTestLayout.setShadowColor(0xff0000ff);
                break;
            default:
                break;
        }
    }
}
