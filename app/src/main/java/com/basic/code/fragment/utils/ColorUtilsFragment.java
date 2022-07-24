
package com.basic.code.fragment.utils;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.basic.page.annotation.Page;
import com.basic.face.utils.ColorUtils;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

/**

 * @since 2018/12/27 下午3:11
 */
@Page(name = "ColorUtils", extra = R.drawable.ic_util_color)
public class ColorUtilsFragment extends BaseFragment {

    @BindView(R.id.square_alpha)
    View mAlphaView;
    @BindView(R.id.square_desc_alpha)
    TextView mAlphaTextView;
    @BindView(R.id.ratioSeekBar)
    SeekBar mRatioSeekBar;
    @BindView(R.id.transformTextView)
    TextView mTransformTextView;
    @BindView(R.id.ratioSeekBarWrap)
    LinearLayout mRatioSeekBarWrap;
    @BindView(R.id.v_random)
    View mVRandom;
    @BindView(R.id.tv_is_dark)
    TextView mTvIsDark;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_util_color;
    }

    @Override
    protected void initViews() {
        // 设置颜色的 alpha 值
        float alpha = 0.5f;
        int alphaColor = ColorUtils.setColorAlpha(ContextCompat.getColor(getContext(), R.color.face_config_color_red), alpha);
        mAlphaView.setBackgroundColor(alphaColor);
        mAlphaTextView.setText(String.format(getResources().getString(R.string.color_utils_square_alpha), alpha));

        // 根据比例，在两个 color 值之间计算出一个 color 值
        final int fromColor = ContextCompat.getColor(getContext(), R.color.face_config_color_pure_blue);
        final int toColor = ContextCompat.getColor(getContext(), R.color.face_config_color_pure_yellow);

        mRatioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int ratioColor = ColorUtils.computeColor(fromColor, toColor, (float) progress / 100);
                mRatioSeekBarWrap.setBackgroundColor(ratioColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mRatioSeekBar.setProgress(50);

        // 将 color 颜色值转换为字符串
        String transformColor = ColorUtils.colorToString(mTransformTextView.getCurrentTextColor());
        mTransformTextView.setText(String.format("这个 TextView 的字体颜色是：%1$s", transformColor));

        // 获取随机颜色
        getRandomColor();
        mVRandom.setOnClickListener(v -> getRandomColor());
    }

    private void getRandomColor() {
        int color = ColorUtils.getRandomColor();
        mVRandom.setBackgroundColor(color);
        mTvIsDark.setText(String.format("颜色是否为深色:%s", ColorUtils.isColorDark(color)));
    }

}
