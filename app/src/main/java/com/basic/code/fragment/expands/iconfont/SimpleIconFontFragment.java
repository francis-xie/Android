
package com.basic.code.fragment.expands.iconfont;

import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.basic.page.annotation.Page;
import com.basic.face.utils.ThemeUtils;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.widget.iconfont.FACEIconFont;
import com.basic.code.widget.iconfont.FACEIconImageView;

import butterknife.BindView;

@Page(name = "字体图标库的用法展示")
public class SimpleIconFontFragment extends BaseFragment {


    @BindView(R.id.iv_font)
    AppCompatImageView ivFont;
    @BindView(R.id.xiiv_font)
    FACEIconImageView xiivFont;
    @BindView(R.id.tv_font)
    TextView tvFont;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_iconfont;
    }

    @Override
    protected void initViews() {
        IconicsDrawable drawable = new IconicsDrawable(getContext())
                .icon(FACEIconFont.Icon.face_emoj)
                .color(IconicsColor.colorInt(ThemeUtils.getMainThemeColor(getContext())))
                .size(IconicsSize.dp(24));
        ivFont.setImageDrawable(drawable);

        xiivFont.setIconText("emoj");


        //TextView一定要注入字体，否则无法生效，字体注入方法详见 com.basic.code.widget.iconfont.IconFontActivity类
        tvFont.setText("{face_emoj}");
    }
}
