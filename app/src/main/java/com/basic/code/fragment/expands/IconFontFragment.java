
package com.basic.code.fragment.expands;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.page.core.PageOption;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.fragment.expands.iconfont.SimpleIconFontFragment;
import com.basic.code.fragment.expands.iconfont.FACEIconFontDisplayFragment;
import com.basic.code.utils.Utils;
import com.basic.code.widget.iconfont.IconFontActivity;

import java.util.List;

/**

 * @since 2019-10-13 16:59
 */
@Page(name = "字体图标库", extra = R.drawable.ic_expand_iconfont)
public class IconFontFragment extends BaseSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("字体图标库的用法展示");
        lists.add("自定义字体图标库FACEIconFont展示");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:
                PageOption.to(SimpleIconFontFragment.class)
                        .setNewActivity(true, IconFontActivity.class)
                        .open(this);
                break;
            case 1:
                openPage(FACEIconFontDisplayFragment.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("图标库") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://www.iconfont.cn/");
            }
        });
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/mikepenz/Android-Iconics");
            }
        });
        return titleBar;
    }
}
