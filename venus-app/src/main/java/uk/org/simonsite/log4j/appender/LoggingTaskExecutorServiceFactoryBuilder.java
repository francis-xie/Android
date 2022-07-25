package uk.org.simonsite.log4j.appender;

import org.apache.log4j.Appender;

final class LoggingTaskExecutorServiceFactoryBuilder {

  LoggingTaskExecutorServiceFactoryBuilder() {
    super();
  }

  final LoggingTaskExecutorServiceFactory named(final Appender appender) {
    return new NamedLoggingTaskExecutorServiceFactory(appender, this.pooled());
  }

  final LoggingTaskExecutorServiceFactory simple() {
    return new SimpleLoggingTaskExecutorServiceFactory();
  }

  final LoggingTaskExecutorServiceFactory pooled() {
    return new PooledLoggingTaskExecutorServiceFactory(this.simple());
  }

}
