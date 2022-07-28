package uk.org.simonsite.log4j.appender;

public interface ThreadFactory {
  
  Thread newThread(Runnable r, String threadName);
}
