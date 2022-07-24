
package com.basic.job.core.step.impl;

import androidx.annotation.NonNull;

import com.basic.job.core.ITaskChainCallback;
import com.basic.job.core.ITaskChainEngine;
import com.basic.job.core.param.ITaskResult;

/**
 * 任务链执行回调适配器【默认回主线程的回调】
 *

 * @since 1/31/22 9:11 PM
 */
public abstract class TaskChainCallbackAdapter implements ITaskChainCallback {

    @Override
    public boolean isCallBackOnMainThread() {
        return true;
    }

    @Override
    public void onTaskChainStart(@NonNull ITaskChainEngine engine) {

    }

    @Override
    public void onTaskChainError(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {

    }

    @Override
    public void onTaskChainCancelled(@NonNull ITaskChainEngine engine) {

    }
}
