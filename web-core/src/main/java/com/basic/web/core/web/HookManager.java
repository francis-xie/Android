
package com.basic.web.core.web;


import com.basic.web.core.AgentWeb;

/**
 
 * @since 1.0.0
 */
public class HookManager {


    public static AgentWeb hookAgentWeb(AgentWeb agentWeb, AgentWeb.AgentBuilder agentBuilder) {
        return agentWeb;
    }

    public static boolean permissionHook(String url,String[]permissions){
        return true;
    }

}
