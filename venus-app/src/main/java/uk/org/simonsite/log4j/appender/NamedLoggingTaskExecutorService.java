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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorHandler;

public final class NamedLoggingTaskExecutorService implements
    LoggingTaskExecutorService {

  private static final Map EXECUTOR_MAP = new HashMap();

  private static final Map APPENDER_MAP = new HashMap();

  private static final Object MAPLOCK = new Object();

  private static final String DEFAULT = "defaultGroup";

  private volatile String name;

  private volatile ErrorHandler errorHandler;

  private volatile ThreadFactory threadFactory;

  private volatile Appender appender;

  private final LoggingTaskExecutorServiceFactory executorServiceFactory;

  public NamedLoggingTaskExecutorService() {
    this(new LoggingTaskExecutorServiceFactoryBuilder().pooled());
  }

  public NamedLoggingTaskExecutorService(
      final LoggingTaskExecutorServiceFactory executorServiceFactory) {
    super();
    if (executorServiceFactory == null) {
      throw new IllegalArgumentException("executor service factory expected");
    }
    this.executorServiceFactory = executorServiceFactory;
    this.setName(DEFAULT);
  }

  public NamedLoggingTaskExecutorService(final Appender appender,
      final LoggingTaskExecutorServiceFactory executorServiceFactory) {
    this(executorServiceFactory);
    if (appender == null) {
      throw new IllegalArgumentException("appender expected");
    }
    this.appender = appender;
  }

  public final void setName(String name) {
    if (name == null) {
      return;
    }
    name = name.trim();
    synchronized (this) {
      if ("".equals(name)) {
        this.name = DEFAULT;
      } else {
        this.name = name;
      }
    }
  }

  public final void setAppender(final Appender appender) {
    synchronized (this) {
      this.appender = appender;
    }
  }

  public final void setErrorHandler(final ErrorHandler eh) {
    synchronized (this) {
      this.errorHandler = eh;
    }
  }

  public final void setThreadFactory(final ThreadFactory tf) {
    synchronized (this) {
      this.threadFactory = tf;
    }
  }

  public final void activateOptions() {
    LoggingTaskExecutorService executorService = null;
    synchronized (MAPLOCK) {
      executorService = (LoggingTaskExecutorService) EXECUTOR_MAP
          .get(this.name);
      if (executorService == null) {
        executorService = this.executorServiceFactory.create();
        executorService.setErrorHandler(this.errorHandler);
        executorService.setThreadFactory(this.threadFactory);
        executorService.activateOptions();
        EXECUTOR_MAP.put(this.name, executorService);
        final Set appenderSet = (Set) APPENDER_MAP.get(this.name);
        if (appenderSet == null) {
          APPENDER_MAP.put(this.name, new HashSet());
        }
      }
      final Set appenderSet = (Set) APPENDER_MAP.get(this.name);
      if (appenderSet != null) {
        appenderSet.add(this.appender);
      } else {
        LogLog.error("Appender Map not initialised in "
            + this.getClass().getName() + " instance named " + this.name);
      }
    }
  }

  public final List shutdownNow() {
    LoggingTaskExecutorService executorService = null;
    synchronized (MAPLOCK) {
      final Set appenderSet = (Set) APPENDER_MAP.get(this.name);
      final boolean shutdown = (appenderSet != null)
          && appenderSet.remove(this.appender) && appenderSet.isEmpty();
      if (shutdown) {
        APPENDER_MAP.remove(this.name);
        executorService = (LoggingTaskExecutorService) EXECUTOR_MAP
            .remove(this.name);
      }
    }
    if (executorService != null) {
      return executorService.shutdownNow();
    }
    return Collections.EMPTY_LIST;
  }

  public final Object scheduleAtFixedRate(final Runnable task,
      final long initialDelay, final long delay) {
    if (task == null) {
      return null;
    }
    final LoggingTaskExecutorService executorService = this
        .lookupExecutorService();
    if (executorService != null) {
      return executorService.scheduleAtFixedRate(task, initialDelay, delay);
    }
    return null;
  }

  public final Object submit(final Runnable task) {
    if (task == null) {
      return null;
    }
    final LoggingTaskExecutorService executorService = this
        .lookupExecutorService();
    if (executorService != null) {
      return executorService.submit(task);
    }
    return null;
  }

  /**
   * For test purposes only.
   * 
   * @return this object's name.
   */
  final String getName() {
    return this.name;
  }

  /**
   * For test purposes only.
   * 
   * @return the internal map.
   */
  final Map getNamedLoggingTaskExecutorServices() {
    synchronized (MAPLOCK) {
      return new HashMap(EXECUTOR_MAP);
    }
  }

  private LoggingTaskExecutorService lookupExecutorService() {
    synchronized (MAPLOCK) {
      return (LoggingTaskExecutorService) EXECUTOR_MAP.get(this.name);
    }
  }
}
