
package com.basic.router.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.basic.router.core.RegisterTransform
import com.basic.router.util.ScanSetting
import com.basic.router.util.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 自动注册路由表的插件
 */
public class RegisterPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        def isApp = project.plugins.hasPlugin(AppPlugin)
        //only application module needs this plugin to generate register code
        if (isApp) {
            Logger.make(project)

            Logger.i('Project enable router-register plugin')

            def android = project.extensions.getByType(AppExtension)
            def transformImpl = new RegisterTransform(project)

            //初始化 router-plugin 扫描设置
            ArrayList<ScanSetting> list = new ArrayList<>(3)
            list.add(new ScanSetting('IRouteRoot'))   //扫描根路由
            list.add(new ScanSetting('IInterceptorGroup')) //扫描拦截器组
            list.add(new ScanSetting('IProviderGroup')) //扫描Provider组
            RegisterTransform.registerList = list
            //register this plugin
            android.registerTransform(transformImpl)
        }
    }

}
