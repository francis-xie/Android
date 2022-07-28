package com.basic.code.core.http.loader;

import android.content.Context;

import com.basic.http2.subsciber.impl.IProgressLoader;

/**
 * 迷你加载框创建工厂
 */
public class MiniProgressLoaderFactory implements IProgressLoaderFactory {

    @Override
    public IProgressLoader create(Context context) {
        return new MiniLoadingDialogLoader(context);
    }

    @Override
    public IProgressLoader create(Context context, String message) {
        return new MiniLoadingDialogLoader(context, message);
    }
}
