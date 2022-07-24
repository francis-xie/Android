
package com.basic.code.fragment.components.pickerview;

import android.widget.TextView;

import com.basic.page.annotation.Page;
import com.basic.face.widget.picker.XRangeSlider;
import com.basic.face.widget.picker.XSeekBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

/**

 * @since 2020-01-04 23:51
 */
@Page(name = "SeekBar\n滑块选择器，支持双向范围选择")
public class SeekBarFragment extends BaseFragment {

    @BindView(R.id.xrs_normal)
    XRangeSlider xrsNormal;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.xrs_bubble)
    XRangeSlider xrsBubble;
    @BindView(R.id.tv_bubble)
    TextView tvBubble;
    @BindView(R.id.xsb)
    XSeekBar xsb;
    @BindView(R.id.tv_xsb)
    TextView tvXsb;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_seekbar;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        xrsNormal.setOnRangeSliderListener(new XRangeSlider.OnRangeSliderListener() {
            @Override
            public void onMaxChanged(XRangeSlider slider, int maxValue) {
                tvNumber.setText(String.format("%d     %d", slider.getSelectedMin(), slider.getSelectedMax()));
            }

            @Override
            public void onMinChanged(XRangeSlider slider, int minValue) {
                tvNumber.setText(String.format("%d     %d", slider.getSelectedMin(), slider.getSelectedMax()));
            }
        });


        xrsBubble.setStartingMinMax(60, 100);
        xrsBubble.setOnRangeSliderListener(new XRangeSlider.OnRangeSliderListener() {
            @Override
            public void onMaxChanged(XRangeSlider slider, int maxValue) {
                tvBubble.setText(String.format("%d     %d", slider.getSelectedMin(), slider.getSelectedMax()));
            }

            @Override
            public void onMinChanged(XRangeSlider slider, int minValue) {
                tvBubble.setText(String.format("%d     %d", slider.getSelectedMin(), slider.getSelectedMax()));
            }
        });


        xsb.setDefaultValue(50);
        xsb.setOnSeekBarListener((seekBar, newValue) -> tvXsb.setText(String.valueOf(newValue)));
    }
}
