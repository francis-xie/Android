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

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.spi.ErrorHandler;

public final class PooledLoggingTaskExecutorService implements
    LoggingTaskExecutorService {

  private final LoggingTaskExecutorService delegate;

  private final Object lock;

  private volatile boolean active;

  public PooledLoggingTaskExecutorService() {
    this(new LoggingTaskExecutorServiceFactoryBuilder().simple());
  }

  public PooledLoggingTaskExecutorService(
      final LoggingTaskExecutorServiceFactory executorServiceFactory) {
    super();
    if (executorServiceFactory == null) {
      throw new IllegalArgumentException("executor service factory expected");
    }
    this.delegate = executorServiceFactory.create();
    this.lock = new Object();
    synchronized (this.lock) {
      this.active = false;
    }
  }

  public final void setAppender(final Appender appender) {
    // no-op
  }

  public final void setErrorHandler(final ErrorHandler eh) {
    this.delegate.setErrorHandler(eh);
  }

  public final void setThreadFactory(final ThreadFactory tf) {
    this.delegate.setThreadFactory(tf);
  }

  public final void activateOptions() {
    synchronized (this.lock) {
      if (!this.active) {
        this.delegate.activateOptions();
        this.active = true;
      }
    }
  }

  public final List shutdownNow() {
    synchronized (this.lock) {
      if (this.active) {
        this.active = false;
        return this.delegate.shutdownNow();
      }
    }
    return Collections.EMPTY_LIST;
  }

  public final Object scheduleAtFixedRate(final Runnable task,
      final long initialDelay, final long delay) {
    if (task == null) {
      return null;
    }
    return this.active ? this.delegate.scheduleAtFixedRate(task, initialDelay,
        delay) : null;
  }

  public final Object submit(final Runnable task) {
    if (task == null) {
      return null;
    }
    return this.active ? this.delegate.submit(task) : null;
  }
}
