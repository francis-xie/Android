package com.basic.code.fragment.components.progress;

import android.view.View;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.progress.ratingbar.RatingBar;
import com.basic.face.widget.progress.ratingbar.RotationRatingBar;
import com.basic.face.widget.progress.ratingbar.ScaleRatingBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "RatingBar\n星级评分控件")
public class RatingBarFragment extends BaseFragment {
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.scale_rating_bar)
    ScaleRatingBar scaleRatingBar;
    @BindView(R.id.rrb_custom)
    RotationRatingBar rrbCustom;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ratingbar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        ratingBar.setOnRatingChangeListener((ratingBar, rating) -> XToastUtils.toast("当前星级：" + rating));
        scaleRatingBar.setOnRatingChangeListener((ratingBar, rating) -> {

        });
    }

    @SingleClick
    @OnClick(R.id.btn_add_rating)
    public void onViewClicked(View view) {

        float currentRating = ratingBar.getRating();
        ratingBar.setRating(currentRating + 0.25f);

        currentRating = scaleRatingBar.getRating();
        scaleRatingBar.setRating(currentRating + 0.25f);

        currentRating = rrbCustom.getRating();
        rrbCustom.setRating(currentRating + 0.25f);

    }
}
