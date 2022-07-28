
package com.basic.job.utils;

import android.os.Looper;

import com.basic.job.core.ThreadType;
import com.basic.job.core.step.ITaskStep;
import com.basic.job.logger.TaskLogger;
import com.basic.job.thread.TaskExecutor;
import com.basic.job.thread.pool.cancel.ICancelable;

import java.util.List;

/**
 * Task内部工具类
 */
public final class TaskUtils {

    private static final String TAG = TaskLogger.getLogTag("TaskUtils");

    private TaskUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 是否是主线程
     *
     * @return 是否是主线程
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 切到主线程运行
     *
     * @param runnable 命令
     */
    public static void runOnMainThread(Runnable runnable) {
        TaskExecutor.get().postToMain(runnable);
    }

    /**
     * 查找下一条需要执行的任务
     *
     * @param taskStepList 执行任务集合
     * @param taskStep     当前任务
     * @return 下一条执行任务
     */
    public static ITaskStep findNextTaskStep(List<ITaskStep> taskStepList, ITaskStep taskStep) {
        if (CommonUtils.isEmpty(taskStepList)) {
            return null;
        }
        int index = 0;
        if (taskStep != null) {
            index = taskStepList.indexOf(taskStep) + 1;
        }
        for (; index < taskStepList.size(); index++) {
            ITaskStep target = taskStepList.get(index);
            if (target != null && target.accept()) {
                return target;
            }
        }
        return null;
    }

    /**
     * 查找需要执行的任务总数
     *
     * @param taskStepList 执行任务集合
     * @return 需要执行的任务总数
     */
    public static int findTaskStepSize(List<ITaskStep> taskStepList) {
        int count = 0;
        if (!CommonUtils.isEmpty(taskStepList)) {
            for (ITaskStep taskStep: taskStepList) {
                if (taskStep != null && taskStep.accept()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 执行任务
     *
     * @param taskStep 需要执行的任务
     * @return 取消接口
     */
    public static ICancelable executeTask(ITaskStep taskStep) {
        if (taskStep == null) {
            TaskLogger.eTag(TAG, "execute task failed, taskStep is null!");
            return null;
        }
        ThreadType type = taskStep.getThreadType();
        if (type == ThreadType.MAIN) {
            TaskExecutor.get().postToMain(taskStep);
            return null;
        } else if (type == ThreadType.ASYNC_EMERGENT) {
            return TaskExecutor.get().emergentSubmit(taskStep);
        } else if (type == ThreadType.ASYNC) {
            return TaskExecutor.get().submit(taskStep);
        } else if (type == ThreadType.ASYNC_IO) {
            return TaskExecutor.get().ioSubmit(taskStep);
        } else if (type == ThreadType.ASYNC_BACKGROUND) {
            return TaskExecutor.get().backgroundSubmit(taskStep);
        } else {
            taskStep.run();
            return null;
        }
    }

}
