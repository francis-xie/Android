package com.basic.log.crash;

import android.text.TextUtils;

import com.basic.log.crash.mail.MailInfo;

/**
 * Crash全局处理
 */
public class Crash {

    /**
     * 默认SMTP主机
     */
    private static final String DEFAULT_HOST = "smtp.163.com";
    /**
     * 默认SMTP服务端口
     */
    private static final String DEFAULT_PORT = "465";

    private static volatile Crash sInstance = null;

    /**
     * 整体的邮件信息
     */
    private MailInfo mMailInfo;
    /**
     * 发送邮箱的地址
     */
    private String mSendEmail;
    /**
     * 邮箱第三方登陆授权码
     */
    private String mAuthorizationCode;
    /**
     * SMTP服务主机
     */
    private String mServerHost;
    /**
     * SMTP服务端口
     */
    private String mServerPort;
    /**
     * 收件人的邮箱地址
     */
    private String[] mToEmails;
    /**
     * 抄送人的邮箱地址
     */
    private String[] mCcEmails;


    private Crash() {
        mServerHost = DEFAULT_HOST;
        mServerPort = DEFAULT_PORT;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static Crash getInstance() {
        if (sInstance == null) {
            synchronized (Crash.class) {
                if (sInstance == null) {
                    sInstance = new Crash();
                }
            }
        }
        return sInstance;
    }

    /**
     * 设置整体的邮件信息
     *
     * @param mailInfo
     * @return
     */
    public Crash setMailInfo(MailInfo mailInfo) {
        mMailInfo = mailInfo;
        return this;
    }


    /**
     * 设置发件人的邮箱地址
     *
     * @param sendEmail
     * @return
     */
    public Crash setSendEmail(String sendEmail) {
        mSendEmail = sendEmail;
        return this;
    }

    /**
     * 设置第三方邮件登陆授权码
     *
     * @param authorizationCode
     * @return
     */
    public Crash setAuthorizationCode(String authorizationCode) {
        mAuthorizationCode = authorizationCode;
        return this;
    }

    /**
     * 设置邮件服务地址
     *
     * @param serverHost
     * @return
     */
    public Crash setServerHost(String serverHost) {
        mServerHost = serverHost;
        return this;
    }

    /**
     * 设置邮件服务端口
     *
     * @param serverPort
     * @return
     */
    public Crash setServerPort(String serverPort) {
        mServerPort = serverPort;
        return this;
    }

    /**
     * 设置收件人邮件地址
     *
     * @param toEmails
     * @return
     */
    public Crash setToEmails(String... toEmails) {
        mToEmails = toEmails;
        return this;
    }

    /**
     * 设置抄送人邮件地址
     *
     * @param ccEmails
     * @return
     */
    public Crash setCcEmails(String... ccEmails) {
        mCcEmails = ccEmails;
        return this;
    }

    //============get==============//

    public static MailInfo getMailInfo() {
        return getInstance().mMailInfo;
    }


    public static MailInfo getPrepareMailInfo() {
        if (!enableSendEmail()) {
            return null;
        }

        if (getInstance().mMailInfo == null) {
            return new MailInfo()
                    .setAuthorizedUser(getSendEmail())
                    .setAuthorizationCode(getAuthorizationCode())
                    .setSendEmail(getSendEmail())
                    .setToEmails(getToEmails())
//                    .setCcEmails(getCcEmails())
                    .setHost(getServerHost())
                    .setPort(getServerPort());
        }
        return getInstance().mMailInfo;
    }

    /**
     * @return 是否配置了可以发送邮件的信息
     */
    public static boolean enableSendEmail() {
        return getInstance().mMailInfo != null
                || (!TextUtils.isEmpty(getSendEmail()) && !TextUtils.isEmpty(getAuthorizationCode()) && getInstance().mToEmails != null);
    }


    public static String getSendEmail() {
        return getInstance().mSendEmail;
    }

    public static String getAuthorizationCode() {
        return getInstance().mAuthorizationCode;
    }

    public static String getServerHost() {
        return getInstance().mServerHost;
    }

    public static String getServerPort() {
        return getInstance().mServerPort;
    }

    public static String[] getToEmails() {
        return getInstance().mToEmails;
    }

    public static String[] getCcEmails() {
        return getInstance().mCcEmails;
    }
}
