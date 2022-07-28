
package uk.org.simonsite.log4j.appender;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

final class ScheduledFileRollEvent extends LoggingEvent {

  private static final long serialVersionUID = -2423899714190505565L;

  private final TimeAndSizeRollingAppender appender;

  ScheduledFileRollEvent(final TimeAndSizeRollingAppender appender,
      final long eventTime) {
    super(Logger.class.getName(), Logger.getRootLogger(), eventTime, Level.ALL,
        "Log4J scheduled file roll event", null);
    this.appender = appender;
  }

  /**
   * Convenience method dispatches this object to the source appender, which
   * will result in a forced file roll.
   */
  final void dispatchToAppender() {
    this.appender.append(this);
  }
}
