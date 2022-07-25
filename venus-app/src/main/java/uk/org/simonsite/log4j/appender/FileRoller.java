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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;

import uk.org.simonsite.log4j.helpers.FileHelper;

/**
 * Has responsibility only for performing the actual file roll.
 * 
 * @author <a href="mailto:simon_park_mail AT yahoo DOT co DOT uk">Simon
 *         Park</a>
 * @version 1.1
 */
final class FileRoller implements FileRollEventSource {

  private static final class BackupFile {

    private final File baseFile;

    private final File timeSuffixedFile;

    private String backupCountSuffix = "";

    BackupFile(final File logFile, final String timeSuffix) {
      super();
      this.baseFile = logFile;
      // work out the full path
      this.timeSuffixedFile = new File(logFile.getAbsolutePath() + timeSuffix);
    }

    final void setBackupCountSuffix(final String backupCountSuffix) {
      this.backupCountSuffix = backupCountSuffix;
    }

    final File getBaseFile() {
      return this.baseFile;
    }

    final File getTimeSuffixedFile() {
      return this.timeSuffixedFile;
    }

    final File getBackupFile() {
      // we already have the absolute path, so just used use the cached path
      return new File(this.timeSuffixedFile.getPath() + '.'
          + this.backupCountSuffix);
    }
  }

  private final TimeAndSizeRollingAppender appender;

  private final AppenderRollingProperties properties;

  private final BackupSuffixHelper backupSuffixHelper;

  private final List fileRollEventListeners;

  FileRoller(final TimeAndSizeRollingAppender rollingAppender) {
    super();
    this.fileRollEventListeners = Collections.synchronizedList(new ArrayList());
    this.appender = rollingAppender;
    this.properties = rollingAppender.getProperties();
    this.backupSuffixHelper = new BackupSuffixHelper(properties);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.org.simonsite.log4j.appender.FileRollEventSource#addFileRollEventListener
   * ( uk.org.simonsite.log4j.appender.FileRollEventListener)
   */
  public final void addFileRollEventListener(
      final FileRollEventListener fileRollEventListener) {
    this.fileRollEventListeners.add(fileRollEventListener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.org.simonsite.log4j.appender.FileRollEventSource#removeFileRollEventListener
   * (uk.org.simonsite.log4j.appender.FileRollEventListener)
   */
  public final void removeFileRollEventListener(
      final FileRollEventListener fileRollEventListener) {
    this.fileRollEventListeners.remove(fileRollEventListener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.org.simonsite.log4j.appender.FileRollEventSource#fireFileRollEvent(org
   * .apache .log4j.appender.FileRollEvent)
   */
  public final void fireFileRollEvent(final FileRollEvent fileRollEvent) {
    final Object[] listeners = this.fileRollEventListeners.toArray();
    for (int i = 0; i < listeners.length; i++) {
      final FileRollEventListener listener = (FileRollEventListener) listeners[i];
      try {
        listener.onFileRoll(fileRollEvent);
      } catch (RuntimeException e) {
        this.getAppender()
            .getErrorHandler()
            .error("Failed to dispatch file roll event", e,
                ErrorCode.GENERIC_FAILURE);
      }
    }
  }

  final TimeAndSizeRollingAppender getAppender() {
    return this.appender;
  }

  final AppenderRollingProperties getProperties() {
    return this.properties;
  }

  /**
   * Performs actual file roll. Tests to see whether roll is necessary have
   * already been performed, so just do it.
   */
  final void roll(final long timeForSuffix) {

    final BackupFile backupFile = this.prepareBackupFile(timeForSuffix);

    // close filename
    this.getAppender().closeFile();

    // rename filename on disk to filename+suffix(+number)
    this.doFileRoll(backupFile);

    // setup new file 'filename'
    this.getAppender().openFile();

    this.fireFileRollEvent(new FileRollEvent(this, backupFile.getBackupFile()));
  }

  /**
   * Builds the file that the current log file will be renamed to, including the
   * base log file name, plus time part, plus backup counter suffix,
   * e.g.&nbsp;&quotbase.log.2007-01-01.1&quot;.
   * 
   * @param timeForSuffix
   * @return The file to be used when performing the actual file roll.
   */
  private BackupFile prepareBackupFile(final long timeForSuffix) {
    final String timeSuffix = this.backupSuffixHelper
        .backupTimeAsString(timeForSuffix);
    final BackupFile backupFile = new BackupFile(
        this.getAppender().getIoFile(), timeSuffix);
    // read in a sorted list of log files (most recent first), filtered on
    // the suffix pattern
    final LogFileList logFileList = this.logFileListFilteredOn(backupFile);
    String backupCountSuffix = this.backupSuffixHelper
        .defaultBackupCountAsString();
    if (!logFileList.isEmpty()) {
      // file list not empty: numbering to be added to suffix.
      final File lastLogFile = logFileList.lastFile();
      // extract number appended to filename of last file in list
      backupCountSuffix = this.backupSuffixHelper.nextBackupCountAsString(
          lastLogFile.getName(), backupFile.getBaseFile());
    }
    backupFile.setBackupCountSuffix(backupCountSuffix);
    return backupFile;
  }

  /**
   * @param backupFile
   * @return A list of log files that contain the base log file name plus the
   *         current time-based filename extension.
   */
  private LogFileList logFileListFilteredOn(final BackupFile backupFile) {
    LogLog.debug("Reading in list of existing log files");
    final String timeSuffixedFilename = backupFile.getTimeSuffixedFile()
        .getName();
    return new LogFileList(backupFile.getBaseFile(), new FilenameFilter() {

      public final boolean accept(final File logDir, final String name) {
        // select all files matching the current filename plus temporal suffix
        return name.startsWith(timeSuffixedFilename);
      }
    }, this.getProperties());
  }

  /**
   * Renames the current base log file to the roll file name.
   * 
   * @param backupFile
   *          The value object containing both current base log file and backup
   *          log file names.
   */
  private void doFileRoll(final BackupFile backupFile) {
    final File from = backupFile.getBaseFile();
    final File to = backupFile.getBackupFile();
    final FileHelper fileHelper = FileHelper.getInstance();
    if (!fileHelper.deleteExisting(to)) {
      this.getAppender().getErrorHandler()
          .error("Unable to delete existing " + to + " for rename");
    }
    final String original = from.toString();
    if (fileHelper.rename(from, to)) {
      LogLog.debug("Renamed " + original + " to " + to);
    } else {
      this.getAppender().getErrorHandler()
          .error("Unable to rename " + original + " to " + to);
    }
  }
}