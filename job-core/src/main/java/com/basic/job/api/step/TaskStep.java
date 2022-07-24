
package com.basic.job.api.step;

import androidx.annotation.NonNull;

import com.basic.job.core.ThreadType;
import com.basic.job.core.param.ITaskParam;
import com.basic.job.core.param.ITaskResult;
import com.basic.job.core.param.impl.TaskParam;
import com.basic.job.core.step.ITaskStepHandler;
import com.basic.job.core.step.impl.AbstractTaskStep;
import com.basic.job.core.step.impl.TaskCommand;
import com.basic.job.logger.TaskLogger;
import com.basic.job.utils.CommonUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简化任务步骤的使用
 *

 * @since 1/30/22 5:08 PM
 */
public class TaskStep extends AbstractTaskStep {

    private static final String TAG = TaskLogger.getLogTag("TaskStep");

    /**
     * 任务的编号【静态全局】
     */
    private static final AtomicInteger TASK_NUMBER = new AtomicInteger(1);

    /**
     * 获取简化的任务
     *
     * @param command 任务执行内容
     * @return 简化任务的构建者
     */
    public static TaskStep getTask(@NonNull TaskCommand command) {
        return new Builder(command).build();
    }

    /**
     * 获取简化的任务
     *
     * @param command      任务执行内容
     * @param isAutoNotify 是否自动通知任务成功或者失败
     * @return 简化任务的构建者
     */
    public static TaskStep getTask(@NonNull TaskCommand command, boolean isAutoNotify) {
        return new Builder(command).setIsAutoNotify(isAutoNotify).build();
    }

    /**
     * 获取简化的任务
     *
     * @param command    任务执行内容
     * @param threadType 线程类型
     * @return 简化任务的构建者
     */
    public static TaskStep getTask(@NonNull TaskCommand command, ThreadType threadType) {
        return new Builder(command)
                .setThreadType(threadType)
                .build();
    }

    /**
     * 获取简化的任务
     *
     * @param command   任务执行内容
     * @param taskParam 任务参数
     * @return 简化任务的构建者
     */
    public static TaskStep getTask(@NonNull TaskCommand command, @NonNull ITaskParam taskParam) {
        return new Builder(command)
                .setTaskParam(taskParam)
                .build();
    }

    /**
     * 获取简化的任务
     *
     * @param command    任务执行内容
     * @param threadType 线程类型
     * @param taskParam  任务参数
     * @return 简化任务的构建者
     */
    public static TaskStep getTask(@NonNull TaskCommand command, ThreadType threadType, @NonNull ITaskParam taskParam) {
        return new Builder(command)
                .setThreadType(threadType)
                .setTaskParam(taskParam)
                .build();
    }

    /**
     * 获取简化任务的构建者
     *
     * @param command 任务执行内容
     * @return 简化任务的构建者
     */
    public static Builder newBuilder(@NonNull TaskCommand command) {
        return new Builder(command);
    }

    /**
     * 任务名称
     */
    private String mName;

    /**
     * 任务执行内容
     */
    private TaskCommand mTaskCommand;

    /**
     * 是否自动通知任务成功或者失败
     */
    private boolean mIsAutoNotify;

    /**
     * 构造方法
     *
     * @param name         任务步骤名称
     * @param command      任务执行内容
     * @param threadType   线程类型
     * @param taskParam    任务参数
     * @param taskHandler  任务处理者
     * @param isAutoNotify 是否自动通知任务成功或者失败
     */
    private TaskStep(@NonNull String name, @NonNull TaskCommand command, ThreadType threadType, @NonNull ITaskParam taskParam, ITaskStepHandler taskHandler, boolean isAutoNotify) {
        super(threadType, taskParam);
        mName = name;
        mTaskCommand = command;
        mTaskCommand.setTaskStepResultController(this);
        mIsAutoNotify = isAutoNotify;
        setTaskStepHandler(taskHandler);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void doTask() throws Exception {
        if (mIsAutoNotify) {
            try {
                mTaskCommand.run();
            } catch (Exception e) {
                TaskLogger.eTag(TAG, getTaskLogName() + " has error！", e);
                notifyTaskFailed(ITaskResult.PROCESS_TASK_THROW_EXCEPTION, e.getMessage());
                return;
            }
            mTaskCommand.notifyTaskSucceed();
        } else {
            mTaskCommand.run();
        }
    }

    /**
     * 简化任务构建者
     *

     * @since 1/30/22 5:22 PM
     */
    public static final class Builder {
        /**
         * 任务步骤名称
         */
        String name;
        /**
         * 任务执行内容
         */
        TaskCommand command;
        /**
         * 线程执行类型
         */
        ThreadType threadType = ThreadType.ASYNC;
        /**
         * 任务参数
         */
        ITaskParam taskParam;
        /**
         * 任务处理者
         */
        ITaskStepHandler taskHandler;
        /**
         * 是否自动通知任务成功或者失败
         */
        boolean isAutoNotify = true;

        /**
         * 构造方法
         *
         * @param command 执行内容
         */
        private Builder(@NonNull TaskCommand command) {
            this.command = command;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCommand(TaskCommand command) {
            this.command = command;
            return this;
        }

        public Builder setThreadType(ThreadType threadType) {
            this.threadType = threadType;
            return this;
        }

        public Builder setTaskParam(ITaskParam taskParam) {
            this.taskParam = taskParam;
            return this;
        }

        public Builder setTaskHandler(ITaskStepHandler taskHandler) {
            this.taskHandler = taskHandler;
            return this;
        }

        public Builder setIsAutoNotify(boolean isAutoNotify) {
            this.isAutoNotify = isAutoNotify;
            return this;
        }

        public TaskStep build() {
            CommonUtils.requireNonNull(this.command, "TaskStep.Builder command can not be null!");

            if (CommonUtils.isEmpty(name)) {
                name = "TaskStep-" + TASK_NUMBER.getAndIncrement();
            }
            if (taskParam == null) {
                taskParam = new TaskParam();
            }
            return new TaskStep(name, command, threadType, taskParam, taskHandler, isAutoNotify);
        }
    }


}
