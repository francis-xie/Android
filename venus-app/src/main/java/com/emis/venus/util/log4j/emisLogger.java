package com.emis.venus.util.log4j;

import com.emis.venus.config.LogConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.OptionConverter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 包裝Log4J的Logger, 以簡化Log檔的使用.
 */
public class emisLogger {
  private static boolean bInit = false;
  private static Properties oProps_;

  private static void initLog4j() throws Exception {
    if (!bInit) {
      //oProps_ = getLog4jProperties();
      //PropertyConfigurator.configure(oProps_);
      LogConfig config = new LogConfig();
      config.setRootLevel(Level.DEBUG);
      config.setLevel("log", OptionConverter.toLevel("INFO", (Level) Level.DEBUG));
      config.configure(); // 设置提交

      bInit = true;
    }
  }

  /**
   * 记录发票历程的log
   *
   * @return
   */
  public static Log getInvoiceLog() {
    Log log = null;
    try {
      initLog4j();
      log = LogFactory.getLog("invoiceLogger");
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return log;
  }

  public static Logger getLog(Class clazz) {
    return getLog(clazz.getName());
  }

  public static Logger getLog(String sClassName) {
    Logger oLogger = null;
    try {
      initLog4j();
      oLogger = Logger.getLogger(sClassName);
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return oLogger;
  }

  private static Properties getLog4jProperties() throws Exception {
    try {
      Properties _oProps = new Properties();
      _oProps.put("log4j.rootLogger", "INFO,A1,A5");

      _oProps.put("log4j.appender.A1.Threshold", "INFO");
      _oProps.put("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
      _oProps.put("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
      _oProps.put("log4j.appender.A1.layout.ConversionPattern", "%d %-5p [%t] %C{2}.%M (%F:%L) - %m%n");

      _oProps.put("log4j.appender.A4", "org.apache.log4j.DailyRollingFileAppender");
      _oProps.put("log4j.appender.A4.Encoding", "UTF-8");
      _oProps.put("log4j.appender.A4.file", "logs/emis.log");
      _oProps.put("log4j.appender.A4.DatePattern", "'_'yyyyMMdd'.log'");
      _oProps.put("log4j.appender.A4.Append", "true");
      _oProps.put("log4j.appender.A4.layout", "org.apache.log4j.PatternLayout");
      _oProps.put("log4j.appender.A4.layout.ConversionPattern", "%d %-5p [%t] %C{2}.%M (%F:%L) - %m%n");

      _oProps.put("log4j.appender.A5", "uk.org.simonsite.log4j.appender.TimeAndSizeRollingAppender");
      _oProps.put("log4j.appender.A5.Encoding", "UTF-8");
      _oProps.put("log4j.appender.A5.file", "logs/emis.log");
      _oProps.put("log4j.appender.A5.DatePattern", ".yyyyMMdd");
      _oProps.put("log4j.appender.A5.MaxFileSize", "300MB");
      _oProps.put("log4j.appender.A5.MaxRollFileCount", "30");
      _oProps.put("log4j.appender.A5.DateRollEnforced", "true");
      _oProps.put("log4j.appender.A5.FileRollEventMessage", "First line of each file following a roll");
      _oProps.put("log4j.appender.A5.BufferedIO", "false");
      _oProps.put("log4j.appender.A5.CompressionAlgorithm", "ZIP");
      _oProps.put("log4j.appender.A5.CompressionMinQueueSize", "1");
      _oProps.put("log4j.appender.A5.Append", "true");
      _oProps.put("log4j.appender.A5.layout", "org.apache.log4j.PatternLayout");
      _oProps.put("log4j.appender.A5.layout.ConversionPattern", "%d %-5p [%t] %C{2}.%M (%F:%L) - %m%n");
      return _oProps;
    } catch (Exception e) {
      System.err.println("emisLogger: IO error " + e.getMessage());
    }
    return null;
  }

  private static Properties getLog4jProperties(String sFileName) throws Exception {
    FileInputStream in = null;
    try {
      in = new FileInputStream(sFileName);
      Properties _oProps = new Properties();
      _oProps.load(in);
      return _oProps;
    } catch (FileNotFoundException e) {
      System.err.println("emisLogger: File not exists " + e.getMessage());
    } catch (IOException e) {
      System.err.println("emisLogger: IO error " + e.getMessage());
    } finally {
      if (in != null)
        in.close();
    }
    return null;
  }

  public static void main(String[] argv) {
    try {
      Logger log1 = emisLogger.getLog(emisLogger.class.getName());
      log1.info("log1 1");

      Logger log2 = emisLogger.getLog(emisLogger.class);
      log1.info("log1 2");
      log2.info("log2 1");
      log2.info("log2 2");
      log1.info("log1 3");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}