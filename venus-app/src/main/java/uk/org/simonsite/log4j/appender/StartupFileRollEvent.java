
package uk.org.simonsite.log4j.appender;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

final class StartupFileRollEvent extends LoggingEvent {

  private static final long serialVersionUID = -2423899714190505565L;

  StartupFileRollEvent() {
    super(Logger.class.getName(), Logger.getRootLogger(), Level.ALL,
        "Log4J start-up file roll", null);
  }
}
