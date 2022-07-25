package com.emis.venus.config;

import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogCatAppender;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import org.apache.log4j.*;
import org.apache.log4j.helpers.LogLog;
import uk.org.simonsite.log4j.appender.TimeAndSizeRollingAppender;

import java.io.File;

public class LogConfig extends LogConfigurator {
  @Override
  public void configure() {
    final Logger root = Logger.getRootLogger();

    if (isResetConfiguration()) {
      LogManager.getLoggerRepository().resetConfiguration();
    }

    LogLog.setInternalDebugging(isInternalDebugging());

    String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
      + File.separator + "logs"
      + File.separator + "emis.log";
    fileName = "/data/data/com.emis.venus/logs/emis.log";
    //A4
    final DailyRollingFileAppender dailyRollingFileAppender = new DailyRollingFileAppender();
    dailyRollingFileAppender.setEncoding("UTF-8");
    dailyRollingFileAppender.setFile(fileName);
    dailyRollingFileAppender.setDatePattern("'_'yyyyMMdd'.log'");
    dailyRollingFileAppender.setAppend(true);
    dailyRollingFileAppender.setLayout(new PatternLayout("%d %-5p [%t] %C{2}.%M (%F:%L) - %m%n"));
    root.addAppender(dailyRollingFileAppender);

    //A5
    final TimeAndSizeRollingAppender rollingAppender = new TimeAndSizeRollingAppender();
    rollingAppender.setEncoding("UTF-8");
    rollingAppender.setFile(fileName);
    rollingAppender.setDatePattern(".yyyyMMdd");
    rollingAppender.setMaxFileSize("300MB");
    rollingAppender.setMaxRollFileCount(30);
    rollingAppender.setDateRollEnforced(true);
    rollingAppender.setFileRollEventMessage("First line of each file following a roll");
    rollingAppender.setBufferedIO(false);
    rollingAppender.setCompressionAlgorithm("ZIP");
    rollingAppender.setCompressionMinQueueSize(1);
    rollingAppender.setAppend(true);
    rollingAppender.setLayout(new PatternLayout("%d %-5p [%t] %C{2}.%M (%F:%L) - %m%n"));
    root.addAppender(rollingAppender);

    // A1
    final Layout logCatLayout = new PatternLayout("%d %-5p [%t] %C{2}.%M (%F:%L) - %m%n");
    final LogCatAppender logCatAppender = new LogCatAppender(logCatLayout);
    root.addAppender(logCatAppender);

    root.setLevel(getRootLevel());
  }
}
