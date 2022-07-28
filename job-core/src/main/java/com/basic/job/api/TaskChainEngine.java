
package com.basic.job.api;

import androidx.annotation.NonNull;

import com.basic.job.core.ITaskChainCallback;
import com.basic.job.core.ITaskChainEngine;
import com.basic.job.core.param.ITaskParam;
import com.basic.job.core.param.ITaskResult;
import com.basic.job.core.param.impl.TaskResult;
import com.basic.job.core.step.ITaskStep;
import com.basic.job.core.step.impl.AutoDestroyTaskChainCallback;
import com.basic.job.logger.TaskLogger;
import com.basic.job.thread.pool.cancel.ICancelable;
import com.basic.job.thread.pool.cancel.ICanceller;
import com.basic.job.utils.CancellerPoolUtils;
import com.basic.job.utils.CommonUtils;
import com.basic.job.utils.TaskUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认实现的任务链执行引擎
 */
public class TaskChainEngine implements ITaskChainEngine {

    private static final String TAG = TaskLogger.getLogTag("TaskChainEngine");

    /**
     * 任务链名前缀
     */
    private static final String TASK_CHAIN_NAME_PREFIX = "TaskChain-";

    /**
     * 是否取消
     */
    private AtomicBoolean mIsCancelled = new AtomicBoolean(false);

    /**
     * 任务链名称
     */
    private String mName;

    /**
     * 任务结果
     */
    private TaskResult mResult = new TaskResult();

    /**
     * 执行任务集合
     */
    private List<ITaskStep> mTasks = new CopyOnWriteArrayList<>();

    /**
     * 任务链执行回调, 默认回调处理是自动销毁任务链
     */
    private ITaskChainCallback mTaskChainCallback = new AutoDestroyTaskChainCallback();

    /**
     * 执行的时候，是否把取消者加入到缓存池中去
     */
    private boolean mIsAddCancellerPool;

    /**
     * 获取任务链执行引擎
     *
     * @return 任务链执行引擎
     */
    public static TaskChainEngine get() {
        return new TaskChainEngine();
    }

    /**
     * 获取任务链执行引擎
     *
     * @param name 任务链名称
     * @return 任务链执行引擎
     */
    public static TaskChainEngine get(String name) {
        return new TaskChainEngine(name);
    }

    /**
     * 构造方法
     */
    public TaskChainEngine() {
        this(TASK_CHAIN_NAME_PREFIX + UUID.randomUUID().toString());
    }

    /**
     * 构造方法
     *
     * @param name 任务链名称
     */
    public TaskChainEngine(String name) {
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public TaskChainEngine setTaskParam(@NonNull ITaskParam taskParam) {
        mResult.updateParam(taskParam);
        return this;
    }

    @Override
    public TaskChainEngine setTaskChainCallback(ITaskChainCallback iTaskChainCallback) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, getTaskChainName() + " setTaskChainCallback failed, task chain has destroyed!");
            return this;
        }
        mTaskChainCallback = iTaskChainCallback;
        return this;
    }

    @Override
    public TaskChainEngine addTask(ITaskStep taskStep) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, getTaskChainName() + " addTask failed, task chain has destroyed!");
            return this;
        }
        if (taskStep != null) {
            taskStep.setTaskStepLifecycle(this);
            mTasks.add(taskStep);
        }
        return this;
    }

    @Override
    public TaskChainEngine addTasks(List<ITaskStep> taskStepList) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, getTaskChainName() + " addTasks failed, task chain has destroyed!");
            return this;
        }
        if (!CommonUtils.isEmpty(taskStepList)) {
            for (ITaskStep taskStep : taskStepList) {
                addTask(taskStep);
            }
        }
        return this;
    }

    @Override
    public void clearTask() {
        if (isDestroy()) {
            return;
        }
        if (CommonUtils.isEmpty(mTasks)) {
            return;
        }
        for (ITaskStep taskStep : mTasks) {
            taskStep.recycle();
        }
        mTasks.clear();
    }

    @Override
    public ICanceller start() {
        return start(true);
    }

    @Override
    public ICanceller start(boolean isAddPool) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, getTaskChainName() + " start failed, task chain has destroyed!");
            return null;
        }
        mIsAddCancellerPool = isAddPool;
        onTaskChainStart();
        ITaskStep firstTaskStep = TaskUtils.findNextTaskStep(mTasks, null);
        if (firstTaskStep != null) {
            firstTaskStep.prepareTask(mResult);
            ICancelable cancelable = TaskUtils.executeTask(firstTaskStep);
            firstTaskStep.setCancelable(cancelable);
        } else {
            onTaskChainCompleted(mResult);
        }
        if (isAddPool) {
            CancellerPoolUtils.add(getName(), this);
        }
        return this;
    }

    @Override
    public void reset() {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, getTaskChainName() + " reset failed, task chain has destroyed!");
            return;
        }
        mIsCancelled.set(false);
        mResult.clear();
        clearTask();
        CancellerPoolUtils.remove(getName());
    }

    @Override
    public void destroy() {
        if (isDestroy()) {
            return;
        }
        TaskLogger.dTag(TAG, getTaskChainName() + " destroy...");
        reset();
        mTaskChainCallback = null;
        mResult = null;
        mTasks = null;
    }

    /**
     * 是否已经销毁
     *
     * @return 是否已经销毁
     */
    private boolean isDestroy() {
        return mResult == null || mTasks == null;
    }

    @Override
    public void cancel() {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, getTaskChainName() + " cancel failed, task chain has destroyed!");
            return;
        }
        if (isCancelled()) {
            return;
        }
        for (ITaskStep taskStep : mTasks) {
            taskStep.cancel();
        }
        mIsCancelled.set(true);
        onTaskChainCancelled();
    }

    @Override
    public boolean isCancelled() {
        return mIsCancelled.get();
    }

    @Override
    public void onTaskStepCompleted(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, getTaskChainName() + " onTaskStepCompleted failed, task chain has destroyed!");
            return;
        }
        mResult.saveResult(result);
        ITaskStep nextTaskStep = TaskUtils.findNextTaskStep(mTasks, step);
        if (nextTaskStep != null) {
            // 更新数据，将上一个task的结果更新到下一个task
            nextTaskStep.prepareTask(mResult);
            ICancelable cancelable = TaskUtils.executeTask(nextTaskStep);
            nextTaskStep.setCancelable(cancelable);
        } else {
            onTaskChainCompleted(result);
        }
    }

    @Override
    public void onTaskStepError(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, getTaskChainName() + " onTaskStepError failed, task chain has destroyed!");
            return;
        }
        onTaskChainError(result);
    }

    private boolean isNeedChangeToMainThread() {
        return mTaskChainCallback.isCallBackOnMainThread() && !TaskUtils.isMainThread();
    }

    private void onTaskChainStart() {
        TaskLogger.dTag(TAG, getTaskChainName() + "(size=" + CommonUtils.getSize(mTasks) + ") start...");
        if (mTaskChainCallback == null) {
            return;
        }
        if (isNeedChangeToMainThread()) {
            TaskUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mTaskChainCallback.onTaskChainStart(TaskChainEngine.this);
                }
            });
        } else {
            mTaskChainCallback.onTaskChainStart(this);
        }
    }


    private void onTaskChainCancelled() {
        TaskLogger.dTag(TAG, getTaskChainName() + " cancelled!");
        if (mIsAddCancellerPool) {
            CancellerPoolUtils.remove(getName());
        }
        if (mTaskChainCallback == null) {
            return;
        }
        if (isNeedChangeToMainThread()) {
            TaskUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mTaskChainCallback.onTaskChainCancelled(TaskChainEngine.this);
                }
            });
        } else {
            mTaskChainCallback.onTaskChainCancelled(this);
        }
    }

    private void onTaskChainCompleted(final ITaskResult result) {
        TaskLogger.dTag(TAG, getTaskChainName() + " completed!");
        if (mIsAddCancellerPool) {
            CancellerPoolUtils.remove(getName());
        }
        if (mTaskChainCallback == null) {
            return;
        }
        if (isNeedChangeToMainThread()) {
            TaskUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mTaskChainCallback.onTaskChainCompleted(TaskChainEngine.this, result);
                }
            });
        } else {
            mTaskChainCallback.onTaskChainCompleted(this, result);
        }
    }

    private void onTaskChainError(final ITaskResult result) {
        TaskLogger.dTag(TAG, getTaskChainName() + " error!");
        if (mIsAddCancellerPool) {
            CancellerPoolUtils.remove(getName());
        }
        if (mTaskChainCallback == null) {
            return;
        }
        if (isNeedChangeToMainThread()) {
            TaskUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mTaskChainCallback.onTaskChainError(TaskChainEngine.this, result);
                }
            });
        } else {
            mTaskChainCallback.onTaskChainError(this, result);
        }
    }

    /**
     * 获取任务链的名称
     *
     * @return 任务链的名称
     */
    protected String getTaskChainName() {
        return "Task chain [" + getName() + "]";
    }
}
