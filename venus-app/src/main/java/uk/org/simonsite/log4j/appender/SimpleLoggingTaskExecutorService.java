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
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OnlyOnceErrorHandler;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.ErrorHandler;

public final class SimpleLoggingTaskExecutorService implements
    LoggingTaskExecutorService {

  private interface ScheduledTask extends Cancellable, Comparable, Runnable {
    long nextTime();

    void reset();
  }

  private abstract class AbstractScheduledTask implements ScheduledTask {

    private final TaskScheduler taskScheduler;

    private volatile long nextTime;

    private final Runnable command;

    private volatile boolean cancelled;

    AbstractScheduledTask(final TaskScheduler scheduledTasks,
        final long nextTime, final Runnable command) {
      super();
      this.taskScheduler = scheduledTasks;
      this.nextTime = nextTime;
      this.command = command;
      this.cancelled = false;
    }

    public final int compareTo(final Object o) {
      final ScheduledTask that = (ScheduledTask) o;
      final long thisNextTime = this.nextTime();
      final long thatNextTime = that.nextTime();
      return (thisNextTime < thatNextTime ? -1
          : (thisNextTime == thatNextTime ? 0 : 1));
    }

    public final boolean cancel(final boolean mayInterruptIfRunning/* ignored */) {
      synchronized (this) {
        this.cancelled = true;
      }
      this.taskScheduler.unschedule(this);
      return true;
    }

    public final boolean isCancelled() {
      return this.cancelled;
    }

    public final long nextTime() {
      return this.nextTime;
    }

    public final void run() {
      this.command.run();
    }

    final void updateNextTime(final long delay) {
      synchronized (this) {
        this.nextTime += delay;
      }
    }

    final TaskScheduler getTaskScheduler() {
      return this.taskScheduler;
    }
  }

  private final class FixedRateTask extends AbstractScheduledTask {
    private final long delay;

    FixedRateTask(final TaskScheduler scheduledTasks, final long initialDelay,
        final long delay, final Runnable command) {
      super(scheduledTasks, System.currentTimeMillis() + initialDelay, command);
      this.delay = delay;
    }

    public final void reset() {
      super.updateNextTime(this.delay);
    }
  }

  private final class OneShotTask extends AbstractScheduledTask {

    OneShotTask(final TaskScheduler scheduledTasks, final Runnable command) {
      super(scheduledTasks, System.currentTimeMillis(), command);
    }

    public final void reset() {
      super.getTaskScheduler().unschedule(this);
    }
  }

  private final class TaskScheduler implements Runnable {
    private final List scheduledTasks;

    private final ErrorHandler errorHandler;

    private final ThreadFactory threadFactory;

    private final Object scheduleLock;

    private volatile boolean run;

    private volatile Thread thread;

    TaskScheduler(final ThreadFactory threadFactory,
        final ErrorHandler errorHandler) {
      super();
      this.scheduledTasks = new LinkedList();
      this.threadFactory = threadFactory;
      this.errorHandler = errorHandler;
      this.run = true;
      this.scheduleLock = new Object();
    }

    public final void run() {
      while (this.run) {
        ScheduledTask task = null;
        synchronized (this.scheduledTasks) {
          Collections.sort(this.scheduledTasks);
          if (this.scheduledTasks.size() > 0) {
            task = (ScheduledTask) this.scheduledTasks.get(0);
            final long now = System.currentTimeMillis();
            if (now >= task.nextTime()) {
              // reset the task time to repeat, or remove non-repeatable task
              task.reset();
              this.scheduledTasks.notifyAll();
            } else {
              final long nextTime = task.nextTime() - now;
              task = null;
              try {
                this.scheduledTasks.wait(nextTime);
              } catch (InterruptedException e) {
                // a new task may have been added, or the task stopped
                // so clear the interrupt flag and check the run flag
              }
            }
          } else {
            try {
              this.scheduledTasks.wait();
            } catch (InterruptedException e) {
              // a new task may have been added, or the task stopped
              // so clear the interrupt flag and check the run flag
            }
          }
        }
        if (task != null) {
          if (task.isCancelled()) {
            this.unschedule(task);
          } else {
            try {
              task.run();
            } catch (RuntimeException e) {
              final ErrorHandler eh = this.errorHandler;
              if (eh != null) {
                eh.error("Logging scheduled task failed", e,
                    ErrorCode.GENERIC_FAILURE);
              }
            }
          }
        }
      }
    }

    final void schedule(final ScheduledTask task) {
      if (this.run) {
        synchronized (this.scheduledTasks) {
          this.scheduledTasks.add(task);
          this.scheduledTasks.notifyAll();
        }
      }
    }

    final void unschedule(final ScheduledTask task) {
      synchronized (this.scheduledTasks) {
        this.scheduledTasks.remove(task);
        this.scheduledTasks.notifyAll();
      }
    }

    final void begin() {
      synchronized (this.scheduleLock) {
        if (this.thread != null) {
          return;
        }
        LogLog.debug("Starting Logging TaskScheduler");
        this.run = true;
        this.thread = this.threadFactory.newThread(this,
            "Logging Task Scheduler");
        this.thread.start();
        LogLog.debug("Started Logging TaskScheduler");
      }
    }

    final void end() {
      Thread t = null;
      synchronized (this.scheduleLock) {
        this.run = false;
        t = this.thread;
        this.thread = null;
      }
      if (t != null) {
        t.interrupt();
        try {
          t.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        LogLog.debug("Stopped Logging TaskScheduler");
      }
    }
  }

  private volatile ErrorHandler errorHandler;

  private volatile ThreadFactory threadFactory;

  private volatile TaskScheduler taskScheduler;

  private final Object lock;

  public SimpleLoggingTaskExecutorService() {
    super();
    this.lock = new Object();
  }

  public final void setErrorHandler(final ErrorHandler eh) {
    if (eh == null) {
      LogLog
          .debug("You have tried to set a null error-handler on the Logging TaskScheduler;"
              + " a default will be used instead.");
    } else {
      synchronized (this.lock) {
        this.errorHandler = eh;
      }
    }
  }

  // For test use only
  final ErrorHandler getErrorHandler() {
    return this.errorHandler;
  }

  public final void setThreadFactory(final ThreadFactory tf) {
    if (tf == null) {
      LogLog
          .debug("You have tried to set a null thread factory on the Logging TaskScheduler;"
              + " a default will be used instead.");
    } else {
      synchronized (this.lock) {
        this.threadFactory = tf;
      }
    }
  }

  // For test use only
  final ThreadFactory getThreadFactory() {
    return this.threadFactory;
  }

  public final void activateOptions() {
    synchronized (this.lock) {
      this.shutdownNow();
      if (this.threadFactory == null) {
        LogLog.debug("Logging TaskScheduler using default thread factory");
        this.threadFactory = new DefaultThreadFactory();
      }
      if (this.errorHandler == null) {
        LogLog.debug("Logging TaskScheduler using default error handler");
        this.errorHandler = new OnlyOnceErrorHandler();
      }
      this.taskScheduler = new TaskScheduler(this.threadFactory,
          this.errorHandler);
      this.taskScheduler.begin();
    }
  }

  public final List shutdownNow() {
    TaskScheduler scheduler = null;
    synchronized (this.lock) {
      scheduler = this.taskScheduler;
      this.taskScheduler = null;
    }
    if (scheduler != null) {
      scheduler.end();
    }
    return Collections.EMPTY_LIST;
  }

  public final void setAppender(final Appender appender) {
    // no-op
  }

  public final Object scheduleAtFixedRate(final Runnable command,
      final long initialDelay, final long delay) {
    if (command == null) {
      final ErrorHandler eh = this.errorHandler;
      if (eh != null) {
        eh.error("Logging TaskScheduler received a null task for fixed rate execution");
      }
      return null;
    }
    final TaskScheduler scheduler = this.taskScheduler;
    if (scheduler == null) {
      this.errorInactiveScheduler();
      return null;
    }
    return this.schedule(new FixedRateTask(scheduler, initialDelay, delay,
        command));
  }

  public final Object submit(final Runnable task) {
    if (task == null) {
      final ErrorHandler eh = this.errorHandler;
      if (eh != null) {
        eh.error("Logging TaskScheduler received a null task for one shot execution");
      }
      return null;
    }
    final TaskScheduler scheduler = this.taskScheduler;
    if (scheduler == null) {
      this.errorInactiveScheduler();
      return null;
    }
    return this.schedule(new OneShotTask(scheduler, task));
  }

  private Object schedule(final ScheduledTask task) {
    final TaskScheduler ts = this.taskScheduler;
    if (ts != null) {
      ts.schedule(task);
    } else {
      this.errorInactiveScheduler();
    }
    return task;
  }

  private void errorInactiveScheduler() {
    final ErrorHandler eh = this.errorHandler;
    if (eh != null) {
      eh.error("Logging TaskScheduler is not running; "
          + this.getClass().getName() + " may not have been activated.");
    }
  }
}
