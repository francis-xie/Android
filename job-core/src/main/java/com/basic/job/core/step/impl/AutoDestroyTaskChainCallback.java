
package com.basic.job.core.step.impl;

import androidx.annotation.NonNull;

import com.basic.job.core.ITaskChainCallback;
import com.basic.job.core.ITaskChainEngine;
import com.basic.job.core.param.ITaskResult;

/**
 * 自动销毁任务链的任务链执行回调
 *

 * @since 2/10/22 10:26 PM
 */
public class AutoDestroyTaskChainCallback implements ITaskChainCallback {

    @Override
    public boolean isCallBackOnMainThread() {
        return false;
    }

    @Override
    public void onTaskChainStart(@NonNull ITaskChainEngine engine) {

    }

    @Override
    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
        engine.destroy();
    }

    @Override
    public void onTaskChainError(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {

    }

    @Override
    public void onTaskChainCancelled(@NonNull ITaskChainEngine engine) {

    }
}
