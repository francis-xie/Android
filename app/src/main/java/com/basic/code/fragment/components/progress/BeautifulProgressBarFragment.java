
package com.basic.code.fragment.components.progress;

import android.view.View;
import android.widget.TextView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.progress.CircleProgressView;
import com.basic.face.widget.progress.HorizontalProgressView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "漂亮的进度条")
public class BeautifulProgressBarFragment extends BaseFragment implements HorizontalProgressView.HorizontalProgressUpdateListener, CircleProgressView.CircleProgressUpdateListener {
    @BindView(R.id.progressView_circle_small)
    CircleProgressView progressViewCircleSmall;
    @BindView(R.id.progressView_circle_main)
    CircleProgressView progressViewCircleMain;
    @BindView(R.id.progress_text_main)
    TextView progressTextMain;
    @BindView(R.id.hpv_language)
    HorizontalProgressView hpvLanguage;
    @BindView(R.id.progress_text_language)
    TextView progressTextLanguage;
    @BindView(R.id.hpv_history)
    HorizontalProgressView hpvHistory;
    @BindView(R.id.progress_text_history)
    TextView progressTextHistory;
    @BindView(R.id.hpv_math)
    HorizontalProgressView hpvMath;
    @BindView(R.id.progress_text_math)
    TextView progressTextMath;
    @BindView(R.id.hpv_english)
    HorizontalProgressView hpvEnglish;
    @BindView(R.id.progress_text_english)
    TextView progressTextEnglish;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_beautiful_progressbar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        hpvLanguage.setProgressViewUpdateListener(this);
        hpvMath.setProgressViewUpdateListener(this);
        hpvHistory.setProgressViewUpdateListener(this);
        hpvEnglish.setProgressViewUpdateListener(this);
        progressViewCircleSmall.setProgressViewUpdateListener(this);

        progressViewCircleMain.setGraduatedEnabled(true);
        progressViewCircleMain.setProgressViewUpdateListener(this);
    }


    @SingleClick
    @OnClick(R.id.btn_start)
    public void onViewClicked(View view) {
        hpvLanguage.startProgressAnimation();
        hpvMath.startProgressAnimation();
        hpvHistory.startProgressAnimation();
        hpvEnglish.startProgressAnimation();
    }

    /**
     * 进度条开始更新
     *
     * @param view
     */
    @Override
    public void onCircleProgressStart(View view) {

    }

    /**
     * 进度条更新中
     *
     * @param view
     * @param progress
     */
    @Override
    public void onCircleProgressUpdate(View view, float progress) {
        int progressInt = (int) progress;
        if (view.getId() == R.id.progressView_circle_main) {
            progressTextMain.setText(progressInt + "");
        }
    }

    /**
     * 进度条更新结束
     *
     * @param view
     */
    @Override
    public void onCircleProgressFinished(View view) {
        if (view.getId() == R.id.progressView_circle_small) {
            progressViewCircleMain.startProgressAnimation();
        }
    }

    /**
     * 进度条开始更新
     *
     * @param view
     */
    @Override
    public void onHorizontalProgressStart(View view) {

    }

    /**
     * 进度条更新中
     *
     * @param view
     * @param progress
     */
    @Override
    public void onHorizontalProgressUpdate(View view, float progress) {
        int progressInt = (int) progress;
        switch (view.getId()) {
            case R.id.hpv_language:
                progressTextLanguage.setText(progressInt + "%");
                break;
            case R.id.hpv_english:
                progressTextEnglish.setText(progressInt + "%");
                break;
            case R.id.hpv_history:
                progressTextHistory.setText(progressInt + "%");
                break;
            case R.id.hpv_math:
                progressTextMath.setText(progressInt + "%");
                break;
            default:
                break;
        }
    }

    /**
     * 进度条更新结束
     *
     * @param view
     */
    @Override
    public void onHorizontalProgressFinished(View view) {
        if (view.getId() == R.id.hpv_language) {
            progressViewCircleSmall.startProgressAnimation();
        }
    }

    @Override
    public void onDestroyView() {
        progressViewCircleSmall.stopProgressAnimation();
        progressViewCircleMain.stopProgressAnimation();

        hpvLanguage.stopProgressAnimation();
        hpvMath.stopProgressAnimation();
        hpvHistory.stopProgressAnimation();
        hpvEnglish.stopProgressAnimation();


        progressViewCircleSmall.setProgressViewUpdateListener(null);
        progressViewCircleMain.setProgressViewUpdateListener(null);

        hpvLanguage.setProgressViewUpdateListener(null);
        hpvMath.setProgressViewUpdateListener(null);
        hpvHistory.setProgressViewUpdateListener(null);
        hpvEnglish.setProgressViewUpdateListener(null);
        super.onDestroyView();
    }
}
