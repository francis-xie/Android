
package com.basic.router.util

import org.gradle.api.Project

/**
 * 日志记录
 */
class Logger {
    static org.gradle.api.logging.Logger logger

    static void make(Project project) {
        logger = project.getLogger()
    }

    static void i(String info) {
        if (null != info && null != logger) {
            logger.info("Router::Register >>> " + info)
        }
    }

    static void e(String error) {
        if (null != error && null != logger) {
            logger.error("Router::Register >>> " + error)
        }
    }

    static void w(String warning) {
        if (null != warning && null != logger) {
            logger.warn("Router::Register >>> " + warning)
        }
    }
}
