package uk.org.simonsite.log4j.appender;

final class PooledLoggingTaskExecutorServiceFactory implements
    LoggingTaskExecutorServiceFactory {

  private final LoggingTaskExecutorServiceFactory delegate;

  PooledLoggingTaskExecutorServiceFactory(
      final LoggingTaskExecutorServiceFactory delegate) {
    super();
    if (delegate == null) {
      throw new IllegalArgumentException("executor service factory expected");
    }
    this.delegate = delegate;
  }

  public final LoggingTaskExecutorService create() {
    return new PooledLoggingTaskExecutorService(this.delegate);
  }

}
