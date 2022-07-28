
package com.basic.web.core.web;


import com.basic.web.core.Web;

/**
 
 * @since 1.0.0
 */
public class HookManager {


    public static Web hookWeb(Web web, Web.AgentBuilder agentBuilder) {
        return web;
    }

    public static boolean permissionHook(String url,String[]permissions){
        return true;
    }

}
