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

/**
 * Base class for roller implementations, this has responsibility only for
 * performing the actual file roll. Subclasses have responsibility for deciding
 * whether a roll is required, and for computing the time used to perform the
 * roll.
 */
abstract class AbstractRoller implements FileRollable {

  private final TimeAndSizeRollingAppender appender;

  private final AppenderRollingProperties properties;

  private final FileRoller fileRoller;

  AbstractRoller(final TimeAndSizeRollingAppender rollingAppender) {
    super();
    this.appender = rollingAppender;
    this.properties = rollingAppender.getProperties();
    this.fileRoller = rollingAppender.getFileRoller();
  }

  final TimeAndSizeRollingAppender getAppender() {
    return this.appender;
  }

  final AppenderRollingProperties getProperties() {
    return this.properties;
  }

  /**
   * Invoked by subclasses; delegates actual file roll.
   */
  final void roll(final long timeForSuffix) {
    this.fileRoller.roll(timeForSuffix);
  }
}