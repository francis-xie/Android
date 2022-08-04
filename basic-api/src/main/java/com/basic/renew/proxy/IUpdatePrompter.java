
package com.basic.renew.proxy;

import androidx.annotation.NonNull;

import com.basic.renew.entity.PromptEntity;
import com.basic.renew.entity.UpdateEntity;

/**
 * 版本更新提示器
 */
public interface IUpdatePrompter {

    /**
     * 显示版本更新提示
     *
     * @param updateEntity 更新信息
     * @param updateProxy  更新代理
     * @param promptEntity 提示界面参数
     */
    void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity);
}
