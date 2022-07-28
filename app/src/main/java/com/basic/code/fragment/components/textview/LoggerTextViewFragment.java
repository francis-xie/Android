
package com.basic.code.fragment.components.textview;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.textview.LoggerTextView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.tools.data.DateUtils;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "LoggerTextView\n日志打印工具")
public class LoggerTextViewFragment extends BaseFragment {

    @BindView(R.id.logger)
    LoggerTextView logger;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_logger_textview;
    }

    @Override
    protected void initViews() {
        // 这里设置自定义的日志格式
        logger.setLogFormatter((logContent, logType) -> DateUtils.getNowString(new SimpleDateFormat("[HH:mm]")) + logContent);

    }

    @OnClick({R.id.btn_normal, R.id.btn_success, R.id.btn_error, R.id.btn_warning, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_normal:
                logger.logNormal("这是一条普通日志！");
                break;
            case R.id.btn_success:
                logger.logSuccess("这是一条成功日志！");
                break;
            case R.id.btn_error:
                logger.logError("这是一条出错日志！");
                break;
            case R.id.btn_warning:
                logger.logWarning("这是一条警告日志！");
                break;
            case R.id.btn_clear:
                logger.clearLog();
                break;
            default:
                break;
        }
    }
}
