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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This event is fired after a log backup file has been compressed.
 */
public final class FileCompressionEvent extends LoggingEvent {

  private static final long serialVersionUID = 470783057303942640L;

  private final File backupFile;

  /**
   * @param backupFile
   *          The newly-compressed backup file.
   */
  FileCompressionEvent(final File backupFile) {
    super(Logger.class.getName(), Logger.getRootLogger(), System
        .currentTimeMillis(), Level.ALL, "Log4J file compression event"
        + System.getProperty("line.separator"), null);
    this.backupFile = backupFile;
  }

  /**
   * @return The backup {@link File}.
   */
  public final File getBackupFile() {
    return this.backupFile;
  }
}
