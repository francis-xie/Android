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
 * @author <a href="mailto:simon_park_mail AT yahoo DOT co DOT uk">Simon
 *         Park</a>
 * @version 1.0
 */
public class DefaultThreadFactory implements ThreadFactory {

  private static int poolNumber = 1;

  private final ThreadGroup group;

  private final String namePrefix;

  private volatile int threadNumber = 1;

  DefaultThreadFactory() {
    final SecurityManager s = System.getSecurityManager();
    this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
        .getThreadGroup();
    synchronized (DefaultThreadFactory.class) {
      this.namePrefix = "pool-" + poolNumber++ + "-thread-";
    }
  }

  public Thread newThread(final Runnable r, final String threadName) {
    final int thisThreadNumber;
    synchronized (this) {
      thisThreadNumber = this.threadNumber++;
    }
    final Thread t = new Thread(this.group, r, this.namePrefix
        + thisThreadNumber + '-' + threadName, 0);
    if (!t.isDaemon()) {
      t.setDaemon(true);
    }
    if (t.getPriority() != Thread.NORM_PRIORITY) {
      t.setPriority(Thread.NORM_PRIORITY);
    }
    return t;
  }
}
