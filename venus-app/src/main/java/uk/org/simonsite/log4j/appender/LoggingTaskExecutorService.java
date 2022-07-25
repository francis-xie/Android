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

import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.spi.ErrorHandler;

/**
 * An object that executes submitted logging tasks that provides methods to
 * manage termination and methods that can produce a {@link Cancellable} for
 * cancellation of one asynchronous task.
 * <p>
 * A <tt>LoggingTaskExecutorService</tt> can be shut down, which will cause it
 * to reject new tasks. The {@link #shutdownNow} method prevents waiting tasks
 * from starting and attempts to stop currently executing tasks. Upon
 * termination, a service has no tasks actively executing, no tasks awaiting
 * execution, and no new tasks can be submitted.
 * <p>
 * This documentation has been shamelessly plagiarised from Doug Lea's excellent
 * concurrency library.
 * 
 * @author <a href="mailto:simon_park_mail AT yahoo DOT co DOT uk">Simon
 *         Park</a>
 * @version 1.1
 */
public interface LoggingTaskExecutorService {
  
  void setAppender(Appender appender);

  void setErrorHandler(ErrorHandler eh);

  void setThreadFactory(ThreadFactory tf);

  /**
   * Initialise the implementation with a {@link ErrorHandler} and a
   * {@link ThreadFactory}, and start the scheduler thread.
   */
  void activateOptions();

  /**
   * Signal the scheduler thread to shutdown in an orderly manner and then
   * clears any waiting tasks.
   * 
   * @return an empty list.
   */
  List shutdownNow();

  /**
   * @param command
   * @param initialDelay
   * @param delay
   * @return a {@link Cancellable} object.
   */
  Object scheduleAtFixedRate(Runnable command, long initialDelay, long delay);

  /**
   * @param task
   * @return a {@link Cancellable} object.
   */
  Object submit(Runnable task);
}
