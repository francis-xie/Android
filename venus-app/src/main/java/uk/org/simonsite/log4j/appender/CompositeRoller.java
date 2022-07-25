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

import org.apache.log4j.spi.LoggingEvent;

/**
 * Composes the behaviour of the startup roller, file-size roller, and the
 * time-based roller. The startup roller takes precedence, followed by the
 * time-based roller, with the file-size roller being evaluated last in the
 * chain.
 * 
 * @author <a href="mailto:simon_park_mail AT yahoo DOT co DOT uk">Simon
 *         Park</a>
 * @version 3.0
 */
final class CompositeRoller implements FileRollable {

  private final FileRollable[] fileRollables;

  private final TimeAndSizeRollingAppender appender;

  CompositeRoller(final TimeAndSizeRollingAppender rollingAppender) {
    super();
    this.appender = rollingAppender;
    this.fileRollables = new FileRollable[] {
        new OneShotStartupRoller(rollingAppender),
        new TimeBasedRoller(rollingAppender),
        new FileSizeRoller(rollingAppender) };
  }

  /**
   * Delegates file rolling to composed objects.
   * 
   * @see FileRollable#roll(LoggingEvent)
   */
  public final boolean roll(final LoggingEvent loggingEvent) {
    for (int i = 0; i < this.fileRollables.length; i++) {
      if (this.fileRollables[i].roll(loggingEvent)) {
        return true;
      }
    }
    return false;
  }

  public final TimeAndSizeRollingAppender getAppender() {
    return this.appender;
  }
}
