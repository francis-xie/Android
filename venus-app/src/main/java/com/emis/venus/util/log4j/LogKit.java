package com.emis.venus.util.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * LogKit.
 */
public class LogKit {

  private final static Logger log = emisLogger.getLog(LogKit.class);
  private final static String callerFQCN = LogKit.class.getName();

  /**
   * Do nothing.
   */
  public static void logNothing(Throwable t) {

  }

  public static void debug(Object o) {
    log(Level.DEBUG, o, null);
  }

  public static void debug(Object o, Throwable t) {
    log(Level.DEBUG, o, t);
  }

  public static void info(Object o) {
    log(Level.INFO, o, null);
  }

  public static void info(Object o, Throwable t) {
    log(Level.INFO, o, t);
  }

  public static void warn(Object o) {
    log(Level.WARN, o, null);
  }

  public static void warn(Object o, Throwable t) {
    log(Level.WARN, o, t);
  }

  public static void error(Object o) {
    log(Level.ERROR, o, null);
  }

  public static void error(Object o, Throwable t) {
    log(Level.ERROR, o, t);
  }

  public static void fatal(Object o) {
    log(Level.FATAL, o, null);
  }

  public static void fatal(Object o, Throwable t) {
    log(Level.FATAL, o, t);
  }

  private static void log(Priority level, Object o, Throwable t) {
    if (o instanceof Throwable)
      log.log(callerFQCN, level, ((Throwable) o).getMessage(), t);
    else
      log.log(callerFQCN, level, o.toString(), t);
  }

  public static boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  public static boolean isInfoEnabled() {
    return log.isInfoEnabled();
  }

  public static boolean isWarnEnabled() {
    return log.isEnabledFor(Level.WARN);
  }

  public static boolean isErrorEnabled() {
    return log.isEnabledFor(Level.ERROR);
  }

  public static boolean isFatalEnabled() {
    return log.isEnabledFor(Level.FATAL);
  }
}