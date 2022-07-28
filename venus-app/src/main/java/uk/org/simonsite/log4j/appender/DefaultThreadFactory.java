package uk.org.simonsite.log4j.appender;

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
