package uk.org.simonsite.log4j.appender;

final class SimpleLoggingTaskExecutorServiceFactory implements
    LoggingTaskExecutorServiceFactory {

  SimpleLoggingTaskExecutorServiceFactory() {
    super();
  }

  public final LoggingTaskExecutorService create() {
    return new SimpleLoggingTaskExecutorService();
  }

}
