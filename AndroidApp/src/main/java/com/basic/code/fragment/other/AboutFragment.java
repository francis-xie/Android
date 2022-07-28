package com.basic.code.fragment.other;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.basic.code.R;
import com.basic.code.core.BaseFragment;
import com.basic.code.core.web.WebActivity;
import com.basic.code.databinding.FragmentAboutBinding;
import com.basic.code.utils.Utils;
import com.basic.page.annotation.Page;
import com.basic.face.widget.grouplist.FACEGroupListView;
import com.basic.tools.app.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Page(name = "关于")
public class AboutFragment extends BaseFragment<FragmentAboutBinding> {

    @Override
    protected void initViews() {
        binding.tvVersion.setText(String.format("版本号：%s", AppUtils.getAppVersionName()));

        FACEGroupListView.newSection(getContext())
                .addItemView(binding.aboutList.createItemView(getResources().getString(R.string.about_item_homepage)), v -> WebActivity.goWeb(getContext(), getString(R.string.url_project_github)))
                .addItemView(binding.aboutList.createItemView(getResources().getString(R.string.about_item_author_github)), v -> WebActivity.goWeb(getContext(), getString(R.string.url_author_github)))
                .addItemView(binding.aboutList.createItemView(getResources().getString(R.string.about_item_donation_link)), v -> WebActivity.goWeb(getContext(), getString(R.string.url_donation_link)))
                .addItemView(binding.aboutList.createItemView(getResources().getString(R.string.about_item_add_qq_group)), v -> WebActivity.goWeb(getContext(), getString(R.string.url_add_qq_group)))
                .addItemView(binding.aboutList.createItemView(getResources().getString(R.string.title_user_protocol)), v -> Utils.gotoProtocol(this, false, false))
                .addItemView(binding.aboutList.createItemView(getResources().getString(R.string.title_privacy_protocol)), v -> Utils.gotoProtocol(this, true, false))
                .addTo(binding.aboutList);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String currentYear = dateFormat.format(new Date());
        binding.tvCopyright.setText(String.format(getResources().getString(R.string.about_copyright), currentYear));
    }

    @NonNull
    @Override
    protected FragmentAboutBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentAboutBinding.inflate(inflater, container, false);
    }
}
