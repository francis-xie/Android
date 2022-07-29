package com.basic.log.crash.mail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.Toast;

import com.basic.log.crash.ICrashHandler;
import com.basic.log.crash.OnCrashListener;
import com.basic.log.crash.R;
import com.basic.log.crash.Crash;
import com.basic.log.utils.FileUtils;

import java.io.File;

/**
 * 通过系统自带的邮件进行崩溃信息发送的监听【需要申请系统悬浮窗的权限】
 */
public class SystemEmailCrashListener implements OnCrashListener {
    /**
     * 收件人的邮箱地址
     */
    private String[] mToEmails;
    /**
     * 抄送人的邮箱地址【暂时会被视为垃圾邮件】
     */
    private String[] mCcEmails;

    public SystemEmailCrashListener() {
        this(Crash.getToEmails(), Crash.getCcEmails());
    }

    /**
     * 构造方法
     *
     * @param toEmails 邮件发送地址
     * @param ccEmails 邮件抄送地址
     */
    public SystemEmailCrashListener(String[] toEmails, String[] ccEmails) {
        mToEmails = toEmails;
        mCcEmails = ccEmails;
    }

    /**
     * 设置收件人邮件地址
     *
     * @param toEmails
     * @return
     */
    public SystemEmailCrashListener setToEmails(String... toEmails) {
        mToEmails = toEmails;
        return this;
    }

    /**
     * 设置抄送人邮件地址
     *
     * @param ccEmails
     * @return
     */
    public SystemEmailCrashListener setCcEmails(String... ccEmails) {
        mCcEmails = ccEmails;
        return this;
    }

    /**
     * 发生崩溃
     *
     * @param context
     * @param crashHandler
     * @param throwable
     */
    @Override
    public void onCrash(final Context context, final ICrashHandler crashHandler, final Throwable throwable) {
        sendAppCrashReport(context, crashHandler, throwable);
    }


    /**
     * 发送应用崩溃报告
     *
     * @param context
     * @param crashHandler
     * @param throwable
     */
    private void sendAppCrashReport(final Context context, final ICrashHandler crashHandler, Throwable throwable) {
        final File crashLogFile = crashHandler.getCrashLogFile();
        final String crashReport = crashHandler.getCrashReport(throwable);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.log_title_app_crash)
                .setMessage(R.string.log_tip_crash_msg)
                .setPositiveButton(R.string.log_lab_submit_report,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendCrashReportEmail(context, crashHandler, crashLogFile, crashReport);
                            }
                        })
                .setNeutralButton(R.string.log_lab_show_detail, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showCrashDetail(context, crashReport, crashHandler);
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 退出
                                crashHandler.setIsHandledCrash(true);
                            }
                        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        //需要的窗口句柄方式，没有这句会报错的
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    /**
     * 发送崩溃日志邮件
     *
     * @param context
     * @param crashHandler
     * @param crashLogFile
     * @param crashReport
     */
    private void sendCrashReportEmail(Context context, ICrashHandler crashHandler, File crashLogFile, String crashReport) {
        try {
            //这以下的内容，只做参考，因为没有服务器
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String[] tos = mToEmails;
            String[] ccs = mCcEmails;
            //收件人
            intent.putExtra(Intent.EXTRA_EMAIL, tos);
            //抄送者
            intent.putExtra(Intent.EXTRA_CC, ccs);
            //密送者
            intent.putExtra(Intent.EXTRA_BCC, ccs);

            intent.putExtra(Intent.EXTRA_SUBJECT,
                    context.getString(R.string.log_title_crash_report_email));
            if (crashLogFile != null) {
                intent.putExtra(Intent.EXTRA_STREAM, FileUtils.getUriForFile(crashLogFile));
                intent.putExtra(Intent.EXTRA_TEXT,
                        context.getString(R.string.log_content_crash_report_email));
            } else {
                intent.putExtra(Intent.EXTRA_TEXT,
                        context.getString(R.string.log_content_crash_report_email)
                                + crashReport);
            }
            intent.setType("text/plain");
            intent.setType("message/rfc882");
            Intent.createChooser(intent, context.getString(R.string.log_title_please_choose_email_client));
            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.log_no_application_support, Toast.LENGTH_LONG).show();
        } finally {
            //禁止重启
            crashHandler.setIsNeedReopen(false);
            //处理完毕退出
            crashHandler.setIsHandledCrash(true);
        }
    }


    /**
     * 显示崩溃详情
     *
     * @param context
     * @param crashLogReport
     */
    private void showCrashDetail(Context context, String crashLogReport, final ICrashHandler crashHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.log_title_crash_detail)
                .setMessage(crashLogReport)
                .setPositiveButton(R.string.log_lab_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 退出
                        crashHandler.setIsHandledCrash(true);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }
}
