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

import java.util.EventListener;

/**
 * Implementors are notified of a file roll after the roll is complete. A
 * typical use may be, for example, to call
 * {@link FileRollEvent#dispatchToAppender(org.apache.log4j.spi.LoggingEvent)}
 * to append a custom logging event directly to the new log file. Alternatively,
 * the {@link FileRollEvent} makes available the source
 * {@link org.apache.log4j.Appender}, making possible, for example,
 * {@link org.apache.log4j.Appender#doAppend(org.apache.log4j.spi.LoggingEvent)}
 * in order to trigger a {@link org.apache.log4j.spi.Filter}, or perhaps to log
 * more highly customised messages.
 * <p>
 * Implementors must be declared public and have a default constructor.
 * <p>
 * It is not advisable to attempt to move or rename the backup file unless you
 * are fully aware of and accept the fact that doing so may, in the context of
 * the normal operation of the {@link TimeAndSizeRollingAppender}, break
 * behavioural contracts and cause log data loss.
 * <p>
 * {@link FileRollEvent}s are processed on either an application thread or, if
 * {@link TimeAndSizeRollingAppender#getDateRollEnforced()} is <tt>true</tt>, on
 * the {@link TimeBasedRollEnforcer} thread.
 */
public interface FileRollEventListener extends EventListener {

  void onFileRoll(FileRollEvent fileRollEvent);
}
