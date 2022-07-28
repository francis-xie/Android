
package com.basic.job.thread.executor.impl;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.basic.job.thread.executor.IExecutorCore;
import com.basic.job.thread.executor.IPriorityExecutorCore;
import com.basic.job.thread.pool.cancel.ICancelable;
import com.basic.job.thread.pool.PriorityThreadPoolExecutor;
import com.basic.job.thread.pool.TaskThreadFactory;
import com.basic.job.thread.utils.ExecutorUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拥有优先级控制，使用PriorityThreadPoolExecutor实现的线程执行内核，通过阻塞队列(PriorityBlockingQueue)来实现优先级的控制。
 */
public class PriorityExecutorCore implements IPriorityExecutorCore, IExecutorCore {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int GROUP_CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final String GROUP_FACTORY_NAME_PREFIX = "PriorityGroup-";

    private PriorityThreadPoolExecutor mExecutor;

    private Map<String, PriorityThreadPoolExecutor> mGroupExecutorMap = new ConcurrentHashMap<>();

    @Override
    public ICancelable submit(Runnable task, int priority) {
        return submitTask(getThreadPoolExecutor(null), task, priority);
    }

    @Override
    public ICancelable submit(String groupName, Runnable task, int priority) {
        return submitTask(getThreadPoolExecutor(groupName), task, priority);
    }

    @Override
    public void shutdown() {
        ExecutorUtils.shutdown(mExecutor);
        mExecutor = null;
        ExecutorUtils.shutdown(mGroupExecutorMap.values());
        mGroupExecutorMap.clear();
    }

    /**
     * 提交任务
     *
     * @param executor 线程池
     * @param task     任务
     * @param priority 优先级
     * @return 取消执行的接口
     */
    private ICancelable submitTask(@NonNull PriorityThreadPoolExecutor executor, Runnable task, int priority) {
        return executor.submit(task, priority);
    }

    /**
     * 获取线程池
     *
     * @param groupName 组名称
     * @return 线程池
     */
    @NonNull
    private PriorityThreadPoolExecutor getThreadPoolExecutor(String groupName) {
        if (TextUtils.isEmpty(groupName)) {
            if (mExecutor == null) {
                mExecutor = PriorityThreadPoolExecutor.getDefault();
            }
            return mExecutor;
        } else {
            PriorityThreadPoolExecutor executor = mGroupExecutorMap.get(groupName);
            if (executor == null) {
                executor = PriorityThreadPoolExecutor.newBuilder(GROUP_CORE_POOL_SIZE)
                        .setThreadFactory(TaskThreadFactory.getFactory(GROUP_FACTORY_NAME_PREFIX + groupName))
                        .build();
                mGroupExecutorMap.put(groupName, executor);
            }
            return executor;
        }
    }

}
