package uk.org.simonsite.log4j.appender;

import org.apache.log4j.spi.LoggingEvent;

interface FileRollable {

  boolean roll(LoggingEvent loggingEvent);
}
