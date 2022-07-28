/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.org.simonsite.log4j.appender;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InterruptedIOException;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;

abstract class AbstractLogFileScavenger implements LogFileScavenger,
    Runnable {

  private TimeAndSizeRollingAppender appender = null;

  private File file = null;

  private AppenderRollingProperties properties = null;

  private LoggingTaskExecutorService executorService = null;

  private volatile Cancellable cancellable = null;

  AbstractLogFileScavenger() {
    super();
  }

  public final void init(final TimeAndSizeRollingAppender appender,
      final AppenderRollingProperties properties) {
    this.appender = appender;
    this.properties = properties;
    this.executorService = this.appender.getExecutorService();
  }

  /**
   * Starts the scavenger.
   */
  public final synchronized void begin() {
    if (this.isScheduled()) {
      return;
    }
    final long scavengeInterval = this.getProperties().getScavengeInterval();
    if (scavengeInterval < 0) {
      LogLog.debug("Scavenger not started: scavenge interval "
          + scavengeInterval);
      return;
    }
    if (this.executorService == null) {
      this.getAppender().getErrorHandler()
          .error("Scavenger not started: missing executor service");
      return;
    }
    this.file = this.getAppender().getIoFile();
    if (this.file == null) {
      this.getAppender().getErrorHandler()
          .error("Scavenger not started: missing log file name");
      return;
    }
    this.cancellable = (Cancellable) this.executorService.scheduleAtFixedRate(
        this, scavengeInterval, scavengeInterval);
    LogLog.debug(this.getClass().getName() + " started");
  }

  /**
   * Stops the scavenger.
   */
  public final synchronized void end() {
    if (!this.isScheduled()) {
      return;
    }
    final Cancellable c = this.cancellable;
    this.cancellable = null;
    if (c != null) {
      c.cancel(true);
      LogLog.debug("Log file scavenger stopped");
    }
  }

  public final void run() {
    try {
      if (this.isScheduled()) {
        this.scavenge();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (InterruptedIOException e) {
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      this.getAppender().getErrorHandler()
          .error("Log file scavenger failed", e, ErrorCode.GENERIC_FAILURE);
    }
  }

  final TimeAndSizeRollingAppender getAppender() {
    return this.appender;
  }

  final AppenderRollingProperties getProperties() {
    return this.properties;
  }

  final LogFileList logFileList() {
    final String filename = this.file.getName();
    return new LogFileList(this.file, new FilenameFilter() {

      public final boolean accept(final File logDir, final String name) {
        // select all but the base log filename, i.e. those that have
        // temporal/backup extensions
        return (!(name.equals(filename))) && name.startsWith(filename);
      }
    }, this.getProperties());
  }

  private boolean isScheduled() {
    return (this.cancellable != null);
  }
}
