package com.basic.http2.cookie;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * <p>描述：cookie管理器</p>
 */
public class CookieManager implements CookieJar {

    private final PersistentCookieStore mCookieStore;

    private static CookieManager sInstance;

    private CookieManager(Context context) {
        mCookieStore = new PersistentCookieStore(context);
    }

    public static CookieManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CookieManager.class) {
                if (sInstance == null) {
                    sInstance = new CookieManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public void saveFromResponse(HttpUrl url, Cookie cookie) {
        if (cookie != null) {
            mCookieStore.add(url, cookie);
        }
    }

    public PersistentCookieStore getCookieStore() {
        return mCookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                mCookieStore.add(url, item);
            }
        }
    }


    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = mCookieStore.get(url);
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public void addCookies(List<Cookie> cookies) {
        mCookieStore.addCookies(cookies);
    }

    public void remove(HttpUrl url, Cookie cookie) {
        mCookieStore.remove(url, cookie);
    }

    public void removeAll() {
        mCookieStore.removeAll();
    }

}
