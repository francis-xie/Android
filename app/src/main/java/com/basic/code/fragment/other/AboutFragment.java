
package com.basic.code.fragment.other;

import android.widget.TextView;

import com.basic.page.annotation.Page;
import com.basic.face.widget.grouplist.FACEGroupListView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.widget.GuideTipsDialog;
import com.basic.tools.app.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

/**

 * @since 2019/1/6 下午12:11
 */
@Page(name = "关于")
public class AboutFragment extends BaseFragment {

    @BindView(R.id.version)
    TextView mVersionTextView;
    @BindView(R.id.about_list)
    FACEGroupListView mAboutGroupListView;
    @BindView(R.id.copyright)
    TextView mCopyrightTextView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initViews() {
        mVersionTextView.setText(String.format("版本号：%s", AppUtils.getAppVersionName()));

        FACEGroupListView.newSection(getContext())
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_homepage)), v -> Utils.goWeb(getContext(), "https://zhiqiang.github.io/FACE/"))
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_wiki)), v -> Utils.goWeb(getContext(), "https://github.com/zhiqiang/FACE/wiki/"))
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_github)), v -> Utils.goWeb(getContext(), "https://github.com/zhiqiang/FACE/"))
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_update)), v -> Utils.checkUpdate(getContext(), true)).addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_sponsor)), v -> openPage(SponsorFragment.class))
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_tips)), v -> GuideTipsDialog.showTipsForce(getContext()))
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_add_qq_group)), v -> Utils.goWeb(getContext(), getString(R.string.url_add_qq_group)))
                .addTo(mAboutGroupListView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String currentYear = dateFormat.format(new Date());
        mCopyrightTextView.setText(String.format(getResources().getString(R.string.about_copyright), currentYear));
    }

}
