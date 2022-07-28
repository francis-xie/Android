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
 * Implementors are notified of backup file compression after the compression is
 * complete. A typical use may be, for example, to compute a checksum for the
 * compressed backup file.
 * <p>
 * Implementors must be declared public and have a default constructor.
 * <p>
 * It is not advisable to attempt to move or rename the compressed backup file
 * unless you are fully aware of and accept the fact that doing so may, in the
 * context of the normal operation of the {@link TimeAndSizeRollingAppender} ,
 * break behavioural contracts and cause log data loss.
 * <p>
 * {@link FileCompressionEvent}s are processed on the {@link LogFileCompressorTask}
 * thread.
 */
public interface FileCompressionEventListener extends EventListener {

  void onFileCompression(FileCompressionEvent fileCompressionEvent);
}
