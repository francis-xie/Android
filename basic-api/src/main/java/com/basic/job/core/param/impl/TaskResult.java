
package com.basic.job.core.param.impl;

import androidx.annotation.NonNull;

import com.basic.job.core.param.ITaskParam;
import com.basic.job.core.param.ITaskResult;
import com.basic.job.logger.TaskLogger;

/**
 * 任务执行结果
 */
public class TaskResult extends TaskParam implements ITaskResult {

    private static final String TAG = TaskLogger.getLogTag("TaskResult");

    /**
     * 获取成功的结果
     *
     * @return 任务执行结果
     */
    public static TaskResult succeed() {
        return new TaskResult(SUCCESS, "");
    }

    /**
     * 获取失败的结果
     *
     * @return 任务执行结果
     */
    public static TaskResult failed() {
        return new TaskResult(ERROR, "");
    }

    /**
     * 获取失败的结果
     *
     * @param code 失败的错误码
     * @return 任务执行结果
     */
    public static TaskResult failed(int code) {
        return new TaskResult(code, "");
    }

    /**
     * 获取失败的结果
     *
     * @param code    失败的错误码
     * @param message 错误信息
     * @return 任务执行结果
     */
    public static TaskResult failed(int code, String message) {
        return new TaskResult(code, message);
    }

    /**
     * 任务执行结果码
     */
    private int mCode;

    /**
     * 任务执行信息
     */
    private String mMessage;

    /**
     * 空构造方法
     */
    public TaskResult() {

    }

    /**
     * 构造方法
     *
     * @param taskParam 任务参数
     */
    public TaskResult(@NonNull ITaskParam taskParam) {
        updateParam(taskParam.getPath(), taskParam.getDataStore());
    }

    /**
     * 构造方法
     *
     * @param taskResult 任务结果
     */
    public TaskResult(@NonNull ITaskResult taskResult) {
        saveResult(taskResult);
    }


    /**
     * 构造方法
     *
     * @param code    结果码
     * @param message 消息
     */
    public TaskResult(int code, String message) {
        setResult(code, message);
    }

    @Override
    public int getCode() {
        return mCode;
    }

    public TaskResult setCode(int code) {
        mCode = code;
        return this;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }

    public TaskResult setMessage(String message) {
        mMessage = message;
        return this;
    }

    /**
     * 设置结果
     *
     * @param code    结果码
     * @param message 消息
     */
    @Override
    public void setResult(int code, String message) {
        mCode = code;
        mMessage = message;
    }

    @Override
    public void saveResult(ITaskResult taskResult) {
        if (taskResult == null) {
            TaskLogger.eTag(TAG, "saveResult error, taskResult is null!");
            return;
        }
        updateParam(taskResult.getPath(), taskResult.getDataStore());
        setResult(taskResult.getCode(), taskResult.getMessage());
    }

    @Override
    public void saveResultNotPath(ITaskResult taskResult) {
        if (taskResult == null) {
            TaskLogger.eTag(TAG, "saveResultNotPath error, taskResult is null!");
            return;
        }
        updateData(taskResult.getDataStore());
        setResult(taskResult.getCode(), taskResult.getMessage());
    }

    @Override
    public String getDetailMessage() {
        return "[code]:" + mCode + ", [msg]:" + mMessage;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "mCode=" + mCode +
                ", mMessage='" + mMessage + '\'' +
                '}';
    }
}
