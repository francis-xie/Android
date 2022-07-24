
package com.basic.job.thread.pool.cancel;

import com.basic.job.thread.utils.CancelUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务取消者池
 *
 
 * @since 2/7/22 12:43 PM
 */
public final class TaskCancellerPool implements ICancellerPool {

    private Map<String, ICancelable> mPool = new ConcurrentHashMap<>();

    @Override
    public boolean add(String name, ICancelable cancelable) {
        if (name == null || cancelable == null) {
            return false;
        }
        return mPool.put(name, cancelable) != null;
    }

    @Override
    public boolean remove(String name) {
        if (name == null) {
            return false;
        }
        return mPool.remove(name) != null;
    }

    @Override
    public boolean cancel(String name) {
        if (name == null) {
            return false;
        }
        ICancelable cancelable = mPool.get(name);
        boolean result = CancelUtils.cancel(cancelable);
        mPool.remove(name);
        return result;
    }

    @Override
    public void cancel(String... names) {
        for (String name : names) {
            cancel(name);
        }
    }

    @Override
    public void cancel(Collection<String> names) {
        for (String name : names) {
            cancel(name);
        }
    }

    @Override
    public void cancelAll() {
        if (mPool.isEmpty()) {
            return;
        }
        CancelUtils.cancel(mPool.values());
        mPool.clear();
    }

    @Override
    public void clear(boolean ifNeedCancel) {
        if (ifNeedCancel) {
            cancelAll();
        } else {
            mPool.clear();
        }
    }

}
