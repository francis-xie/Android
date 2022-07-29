package com.basic.log.crash.ui;

import android.content.Context;
import android.content.Intent;

import com.basic.log.crash.ICrashHandler;
import com.basic.log.crash.OnCrashListener;
import com.basic.log.crash.R;
import com.basic.log.crash.Crash;

public class ShowActivityCrashListener implements OnCrashListener {

    /**
     * 主题颜色
     */
    private int mThemeId;

    public ShowActivityCrashListener() {
        this(R.style.CrashTheme_Default);
    }

    public ShowActivityCrashListener(int themeId) {
        mThemeId = themeId;
    }

    /**
     * 发生崩溃
     *
     * @param context
     * @param crashHandler
     * @param throwable
     */
    @Override
    public void onCrash(Context context, ICrashHandler crashHandler, Throwable throwable) {
        CrashInfo crashInfo = CrashUtils.parseCrash(context, throwable)
                .setCrashLogFilePath(crashHandler.getCrashLogFile().getPath());

        Intent intent = new Intent(context, CrashActivity.class);
        intent.putExtra(CrashActivity.KEY_CRASH_INFO, crashInfo);
        intent.putExtra(CrashActivity.KEY_MAIL_INFO, Crash.getPrepareMailInfo());
        intent.putExtra(CrashActivity.KEY_THEME_ID, mThemeId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        //禁止重启
        crashHandler.setIsHandledCrash(true);
        crashHandler.setIsNeedReopen(false);
    }


}
