package uk.org.simonsite.log4j.appender;

import org.apache.log4j.Appender;

final class NamedLoggingTaskExecutorServiceFactory implements
    LoggingTaskExecutorServiceFactory {

  private final Appender appender;

  private final LoggingTaskExecutorServiceFactory delegate;

  NamedLoggingTaskExecutorServiceFactory(final Appender appender,
      final LoggingTaskExecutorServiceFactory delegate) {
    super();
    if (appender == null) {
      throw new IllegalArgumentException("appender expected");
    }
    if (delegate == null) {
      throw new IllegalArgumentException("executor service factory expected");
    }
    this.appender = appender;
    this.delegate = delegate;
  }

  public final LoggingTaskExecutorService create() {
    return new NamedLoggingTaskExecutorService(this.appender, this.delegate);
  }

}
