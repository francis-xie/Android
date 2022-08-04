package com.basic.http2.model;

/**
 * 请求失效信息
 */
public class ExpiredInfo {

    /**
     * 请求是否失效
     */
    private boolean mIsExpired;

    /**
     * 失效的类型
     */
    private int mExpiredType;

    /**
     * 服务器返回的错误码（失效码）
     */
    private int mCode;

    /**
     * 请求的body内容
     */
    private String mBodyString;

    public ExpiredInfo(int code) {
        mCode = code;
    }

    public boolean isExpired() {
        return mIsExpired;
    }

    public ExpiredInfo setExpired(boolean expired) {
        mIsExpired = expired;
        return this;
    }

    public int getExpiredType() {
        return mExpiredType;
    }

    public ExpiredInfo setExpiredType(int expiredType) {
        mExpiredType = expiredType;
        mIsExpired = true;
        return this;
    }

    public int getCode() {
        return mCode;
    }

    public ExpiredInfo setCode(int code) {
        mCode = code;
        return this;
    }

    public String getBodyString() {
        return mBodyString;
    }

    public ExpiredInfo setBodyString(String bodyString) {
        mBodyString = bodyString;
        return this;
    }

}
