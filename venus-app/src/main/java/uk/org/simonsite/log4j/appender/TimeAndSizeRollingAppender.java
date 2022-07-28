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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.xml.UnrecognizedElementHandler;
import org.w3c.dom.Element;

import uk.org.simonsite.log4j.helpers.FlushAfterWriteFileOutputStream;
import uk.org.simonsite.log4j.helpers.FileHelper;
import uk.org.simonsite.log4j.helpers.SynchronizedCountingQuietWriter;

/**
 * This appender is responsible for writing log events to file, and rolling
 * files when they reach configurable limits of time and/or size. It is
 * configured and behaves in much the same way as the
 * {@link org.apache.log4j.DailyRollingFileAppender}, with some useful
 * differences. <h2>Maximum file size</h2>
 * <p>
 * Maximum log file size can be configured (default 10MB) using the
 * <tt>MaxFileSize</tt> property. This means that log files can be rolled within
 * the configured time period if the maximum file size is exceeded. Backups are
 * indicated by a count suffix at the end of log filenames, e.g. for the time
 * suffix specified by the default pattern &quot;<tt>'.'yyyy-MM-dd</tt>&quot;
 * (which would ordinarily result in a log file named
 * e.g.&nbsp;&quot;foo.log.2007-01-01&quot;) will result in backups named
 * &quot;foo.log.2007-01-01<b>.1</b>&quot;,
 * &quot;foo.log.2007-01-01<b>.2</b>&quot;, etc.
 * <h2>Maximum number of backup files</h2>
 * <p>
 * Configuring the maximum number of allowable backup log files (default 10)
 * using the <tt>MaxRollFileCount</tt> property helps prevent this appender from
 * consuming disk space without limit. Excess backup log files are scavenged by
 * a background thread.
 * <p>
 * Configuring the scavenging interval (default 30 seconds) using the
 * <tt>ScavengeInterval</tt> property specifies the duration for which the
 * scavenger thread should sleep between operations. The log file scavenger only
 * operates upon files that start with the path specified by the <tt>File</tt>
 * configuration parameter. Older files will be deleted first.
 * <p>
 * Setting a scavenge interval of <tt>-1</tt> prevents the scavenger from
 * running.
 * <h2>Backup file compression</h2>
 * <p>
 * Backup log file compression may be configured using the allowed compression
 * algorithms specified by the <tt>CompressionAlgorithm</tt> property:
 * <ul>
 * <li>&quot;ZIP&quot; for zip compression</li>
 * <li>&quot;GZ&quot; for gzip compression</li>
 * </ul>
 * <p>
 * Default behaviour is not to compress backup log files unless the
 * <tt>CompressionAlgorithm</tt> property is configured. Backup files are
 * compressed by a background thread. At roll time the name of the backup log
 * {@link File} object is put into the compressor thread's FIFO queue.
 * By default the compressor works on a best-effort basis: if the queue fills
 * up, then older backup filenames are discarded and will therefore not be
 * compressed.
 * <p>
 * The appender can be configured to force compression of all backup files by
 * setting the <tt>CompressionUseBlockingQueue</tt> property to &quot;
 * <tt>true</tt>&quot; (default is &quot;<tt>false</tt>&quot;). Forcing
 * compression comes at the cost of blocking the compressor's queue and
 * therefore the shared daemon thread used for compression and scavenging tasks;
 * the application thread that invoked the {@link org.apache.log4j.Logger} will
 * not be blocked. The queue will cease to block once the current compression
 * operation has completed and the compressor has removed the next file from its
 * queue.
 * <p>
 * The appender's <tt>CompressionMinQueueSize</tt> property (default 0) controls
 * the minimum number of backup files that must be in the queue awaiting
 * compression before any compression will take actually take place. For
 * example, if this property is set to 5, then at least 5 backup file rolls must
 * take place before the oldest file currently in the compressor's queue will be
 * compressed. Keeping the most recent files uncompressed can be helpful at
 * support time.
 * <p>
 * You may choose to register a {@link FileCompressionEventListener} with the
 * appender. To do so you must provide your own implementation of the
 * {@link FileCompressionEventListener} interface on the classpath, and
 * configure it via XML. For example:
 * <p>
 * &lt;appender name=&quot;LOG-DAILYROLL&quot;
 * class=&quot;uk.org.simonsite.log4j
 * .appender.TimeAndSizeRollingAppender&quot;&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;File&quot;
 * value=&quot;/logs/app.log&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;CompressionAlgorithm&quot;
 * value=&quot;ZIP&quot;/&gt;<br/>
 * <b>&nbsp;&nbsp;&lt;fileCompressionEventListener
 * class=&quot;com.acme.MyFileCompressionEventListener&quot;&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param name=&quot;MyParam&quot; value=&quot;Sample
 * Value&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;/fileCompressionEventListener&gt;</b><br/>
 * &nbsp;&nbsp;&lt;layout class=&quot;org.apache.log4j.SimpleLayout&quot;/&gt;<br/>
 * &lt;/appender&gt;
 * <h2>Time boundaries</h2>
 * <p>
 * If you have chosen a daily, weekly, or longer rolling strategy (via the
 * <tt>DatePattern</tt> property), then you may wish to take advantage of the
 * <tt>TimeZoneId</tt> property. In these cases setting the <tt>TimeZoneId</tt>
 * property will ensure that the appender rolls at midnight in the configured
 * time zone. For legal time zone IDs accepted by the appender, see core Java
 * API doc for the {@link TimeZone} class.
 * <p>
 * Setting the appender's <tt>DateRollEnforced</tt> property to <tt>true</tt>
 * (default <tt>false</tt>) activates pro-active log rolling at time boundaries.
 * Time boundaries are enforced by a background thread. The standard
 * {@link org.apache.log4j.DailyRollingFileAppender} only rolls log files
 * reactively upon the dispatch of a logging event. This appender allows
 * proactive control over log rolling by enforcing a schedule implied by the
 * <tt>DatePattern</tt> property. For example, &lt;param
 * name=&quot;DatePattern&quot; value=&quot;.yyyy-MM-dd&quot;/&gt; will see the
 * log file roll at the end of the day, even if the application is otherwise
 * inactive. Similarly &lt;param name=&quot;DatePattern&quot;
 * value=&quot;.yyyy-MM-dd-HH&quot;/&gt; will result in log files being rolled
 * every hour.
 * <h2>File roll events</h2>
 * <p>
 * A custom message of your choosing may be written into the first line of each
 * new log file created after a file roll has completed. This is achieved by
 * setting the <tt>FileRollEventMessage</tt> property to a message string. If
 * this property is configured with a blank value (e.g. &lt;param
 * name=&quot;FileRollEventMessage&quot;/&gt;), the appender will ensure that a
 * default message is written at the top of the new log file instead. If this
 * property is not set, no message will be written to the top of new log files.
 * Messages are appended at {@link org.apache.log4j.Level#ALL} using the root
 * logger.
 * <p>
 * As an alternative to a message being written on file roll you may choose the
 * greater flexibility afforded by registering a {@link FileRollEventListener}
 * with the appender. To do so you must provide your own implementation of the
 * {@link FileRollEventListener} interface on the classpath, and configure it
 * via XML. For example:
 * <p>
 * &lt;appender name=&quot;LOG-DAILYROLL&quot;
 * class=&quot;uk.org.simonsite.log4j
 * .appender.TimeAndSizeRollingAppender&quot;&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;File&quot;
 * value=&quot;/logs/app.log&quot;/&gt;<br/>
 * <b>&nbsp;&nbsp;&lt;fileRollEventListener
 * class=&quot;com.acme.MyFileRollEventListener&quot; &gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param name=&quot;MyParam&quot; value=&quot;Sample
 * Value&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;/fileRollEventListener&gt;</b><br/>
 * &nbsp;&nbsp;&lt;layout class=&quot;org.apache.log4j.SimpleLayout&quot;/&gt;<br/>
 * &lt;/appender&gt;
 * <h2>Roll on start-up</h2>
 * <p>
 * The appender can be configured to roll the most recent backup file,
 * regardless of the file's last modification time or size, immediately after
 * receiving the first logging event after the appender's options are activated.
 * <p>
 * For example, say the appender is configured to roll every hour, or for files
 * exceeding 10MB in size, and that a 1 MB log file exists that was last written
 * 10 minutes before the hour. The application is restarted 5 minutes before the
 * hour and logs an event. When the <tt>RollOnStartup</tt> property is set to
 * <tt>true</tt>, the log file described in this example scenario will be rolled
 * into a backup, and a new log file will be created.
 * <p>
 * <em>NB</em> If you use a {@link Layout} that writes headers
 * and footers and require a single header and footer per log file, then you
 * should set the <tt>RollOnStartup</tt> property to <tt>true</tt>.
 * <h2>Flush to storage</h2>
 * <p>
 * Out-of-the-box Log4J file appenders can be configured to flush after each
 * append (via {@link WriterAppender#setImmediateFlush()}), though this offers
 * no guarantees that what is appended will actually be written to disk. What is
 * appended may be stored, for a short time, in memory buffers by the underlying
 * OS. Therefore, in the event of machine failure, some logging events may be
 * lost. Setting the <tt>FlushToStorageOnAppend</tt> property to <tt>true</tt>
 * causes this appender to invoke {@link FileDescriptor#sync()} after each
 * append. <tt>FlushToStorageOnAppend</tt> is <tt>false</tt> by default.
 * <em><strong>NB</strong> This option comes with a performance overhead and may have
 * a material impact upon application throughput: applications should be profiled.</em>
 * <h2>Sample configurations</h2>
 * <h3>Sample 1</h3>
 * <p>
 * An example configuration snippet taken from an actual Log4J XML configuration
 * file is given here (generate the Javadoc to see the correct formatting). This
 * configuration provides an appender that rolls each day and creates log files
 * no larger than 10MB. The number of backup files is checked every 30 seconds,
 * whereupon if the number of backup files exceeds 100 the extra backups will be
 * deleted. The appender will make best efforts to compress backup files using
 * the GZ algorithm.
 * <p>
 * &lt;appender name=&quot;LOG-DAILYROLL&quot;
 * class=&quot;uk.org.simonsite.log4j
 * .appender.TimeAndSizeRollingAppender&quot;&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;File&quot;
 * value=&quot;/logs/app.log&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;Threshold&quot; value=&quot;DEBUG&quot;/&gt;
 * <br/>
 * &nbsp;&nbsp;&lt;param name=&quot;DatePattern&quot;
 * value=&quot;.yyyy-MM-dd&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;MaxFileSize&quot;
 * value=&quot;10MB&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;MaxRollFileCount&quot;
 * value=&quot;100&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;ScavengeInterval&quot;
 * value=&quot;30000&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;BufferedIO&quot;
 * value=&quot;false&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;CompressionAlgorithm&quot;
 * value=&quot;GZ&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;layout class=&quot;org.apache.log4j.PatternLayout&quot;&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param name=&quot;ConversionPattern&quot;
 * value=&quot;%-5p %-23d{ISO8601} [%t] %x: %c{1} - %m%n&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;/layout&gt;<br/>
 * &lt;/appender&gt;
 * <h3>Sample 2</h3>
 * <p>
 * This next configuration provides an appender that rolls each day, creates log
 * files no larger than 100MB, and limits the number of backup files to no more
 * than 10. The number of backup files is checked every 30 seconds, whereupon if
 * the number of backup files exceeds 10 the extra backups will be deleted. The
 * appender will make best efforts to compress backup files using the ZIP
 * algorithm, but it will only compress backup files after more than 5 rolls
 * have taken place during the lifetime of the application instance. Finally,
 * this configuration causes the appender to honour time boundaries by rolling
 * logs pro-actively at the end of each day, rather that reactively in response
 * to a logging event. After file roll is complete, the new log file will have
 * the message &quot;First line of each file following a roll&quot; printed on
 * the first line.
 * <p>
 * &lt;appender name=&quot;LOG-DAILYROLL&quot;
 * class=&quot;uk.org.simonsite.log4j
 * .appender.TimeAndSizeRollingAppender&quot;&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;File&quot;
 * value=&quot;/logs/app.log&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;Threshold&quot; value=&quot;DEBUG&quot;/&gt;
 * <br/>
 * &nbsp;&nbsp;&lt;param name=&quot;DatePattern&quot;
 * value=&quot;.yyyy-MM-dd&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;MaxFileSize&quot;
 * value=&quot;100MB&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;DateRollEnforced&quot;
 * value=&quot;true&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;FileRollEventMessage&quot; value=&quot;First
 * line of each file following a roll&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;BufferedIO&quot;
 * value=&quot;false&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;CompressionAlgorithm&quot;
 * value=&quot;ZIP&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;CompressionMinQueueSize&quot;
 * value=&quot;5&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;layout class=&quot;org.apache.log4j.PatternLayout&quot;&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param name=&quot;ConversionPattern&quot;
 * value=&quot;%-5p %-23d{ISO8601} [%t] %x: %c{1} - %m%n&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;/layout&gt;<br/>
 * &lt;/appender&gt;
 * <h3>Sample 3</h3>
 * <p>
 * A final example of the simplest configuration provides an appender that rolls
 * each day and creates log files no larger than 10MB. The number of backup
 * files is checked every 30 seconds, whereupon if the number of backup files
 * exceeds 10 the extra backups will be deleted. Backup files are not
 * compressed, log files are rolled reactively, and no roll event messages are
 * written at the top of each new log file.
 * <p>
 * &lt;appender name=&quot;LOG-DAILYROLL&quot;
 * class=&quot;uk.org.simonsite.log4j
 * .appender.TimeAndSizeRollingAppender&quot;&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;File&quot;
 * value=&quot;/logs/app.log&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;param name=&quot;Threshold&quot; value=&quot;DEBUG&quot;/&gt;
 * <br/>
 * &nbsp;&nbsp;&lt;param name=&quot;BufferedIO&quot;
 * value=&quot;false&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;layout class=&quot;org.apache.log4j.PatternLayout&quot;&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param name=&quot;ConversionPattern&quot;
 * value=&quot;%-5p %-23d{ISO8601} [%t] %x: %c{1} - %m%n&quot;/&gt;<br/>
 * &nbsp;&nbsp;&lt;/layout&gt;<br/>
 * &lt;/appender&gt;
 * @see org.apache.log4j.DailyRollingFileAppender
 */
public final class TimeAndSizeRollingAppender extends FileAppender implements
    UnrecognizedElementHandler {

  /**
   * Properties bean to facilitate property sharing between objects.
   */
  private final AppenderRollingProperties properties = new AppenderRollingProperties();

  private FileRollable fileRollable = null;
  
  private FileRoller fileRoller = null;
  
  private LoggingTaskExecutorService executorService = null;

  private LogFileScavenger logFileScavenger = null;

  private LogFileCompressorTask logFileCompressor = null;

  private TimeBasedRollEnforcer logRollEnforcer = null;

  private FileRollEventListener guestFileRollEventListener = null;

  private FileCompressionEventListener guestFileCompressionEventListener = null;

  public TimeAndSizeRollingAppender() {
    super();
    this.setFileRoller(new FileRoller(this));
  }

  /**
   * @param layout
   * @param filename
   * @throws IOException
   */
  public TimeAndSizeRollingAppender(final Layout layout, final String filename)
      throws IOException {
    this();
    this.initSuper(layout, filename, super.getAppend(), super.getBufferedIO(),
        super.getBufferSize());
  }

  /**
   * @param layout
   * @param filename
   * @param append
   * @throws IOException
   */
  public TimeAndSizeRollingAppender(final Layout layout, final String filename,
      final boolean append) throws IOException {
    this();
    this.initSuper(layout, filename, append, super.getBufferedIO(),
        super.getBufferSize());
  }

  /**
   * @param layout
   * @param filename
   * @param append
   * @param bufferedIO
   * @param bufferSize
   * @throws IOException
   */
  public TimeAndSizeRollingAppender(final Layout layout, final String filename,
      final boolean append, final boolean bufferedIO, final int bufferSize)
      throws IOException {
    this();
    this.initSuper(layout, filename, append, bufferedIO, bufferSize);
  }

  private void initSuper(final Layout layout, final String filename,
      final boolean append, final boolean bufferedIO, final int bufferSize)
      throws IOException {
    super.setLayout(layout);
    super.setFile(filename);
    super.setAppend(append);
    super.setBufferedIO(bufferedIO);
    super.setBufferSize(bufferSize);
  }
  
  public boolean equals(final Object other) {
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!(other instanceof TimeAndSizeRollingAppender)) {
      return false;
    }
    final TimeAndSizeRollingAppender otherAppender = (TimeAndSizeRollingAppender) other;
    final String thisFile = this.getFile();
    final String otherFile = otherAppender.getFile();
    return (thisFile != null) ? thisFile.equals(otherFile) : (otherFile == null);
  }
  
  public int hashCode() {
    final String file = this.getFile();
    return (file != null) ? file.hashCode() : 17;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.log4j.FileAppender#activateOptions()
   */
  // TODO Improve consistency of object initialisation
  public synchronized final void activateOptions() {
    this.deactivateOptions();
    this.getProperties().setActivatingAppender(true);
    /*
     * Thread-safety issue: if multiple Appenders are being created at the same
     * time, programatically, and the files are all in the same directory,
     * there is a case wherein, if the directory does not yet exist, each thread
     * will attempt to create the parent directory; one thread will win,
     * therefore the other thread(s) will fail to initialise the file for
     * writing. This is essentially a bug in the Log4J 1.2.x
     * FileAppender.setFile(4-param) method. The TASRA needs to defend against
     * this. TODO Time to take it out of the Log4J class hierarchy? For now,
     * just pre-emptively create the parent directory structure via
     * File.mkdirs().
     */
    FileHelper.getInstance().makeParentDirsOf(this.getIoFile());
    /*
     * Configure buffering options before activating the parent configuration.
     */
    if (this.getFlushToStorageOnAppend()) {
      super.setBufferedIO(false);
      super.setImmediateFlush(true);
    }
    super.activateOptions();
    // rollables
    this.setFileRollable(new CompositeRoller(this));
    // rollable guest listener
    this.registerGuestFileRollEventListener();
    // shared executor service
    LoggingTaskExecutorService executorService = this.getExecutorService();
    if (executorService == null) {
      executorService = new LoggingTaskExecutorServiceFactoryBuilder().named(this)
          .create();
    }
    executorService = this.init(executorService);
    executorService.activateOptions();
    // scavenger
    LogFileScavenger fileScavenger = this.getLogFileScavenger();
    if (fileScavenger == null) {
      fileScavenger = new DefaultLogFileScavenger();
    }
    fileScavenger = this.init(fileScavenger);
    fileScavenger.begin();
    // compressor
    final LogFileCompressorTask logFileCompressor = new LogFileCompressorTask(
        this);
    this.setLogFileCompressor(logFileCompressor);
    this.getFileRoller().addFileRollEventListener(logFileCompressor);
    // compressor guest listener
    this.registerGuestFileCompressionEventListener();
    // roll enforcer
    final TimeBasedRollEnforcer logRollEnforcer = new TimeBasedRollEnforcer(
        this);
    this.setLogRollEnforcer(logRollEnforcer);
    logRollEnforcer.begin();
    // do roll on start-up
    this.rollOnStartup();
    this.getProperties().setActivatingAppender(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.log4j.WriterAppender#close()
   */
  public synchronized final void close() {
    if (super.closed) {
      return;
    }
    this.closeFile();
    this.deactivateOptions();
    super.fileName = null;
    super.closed = true;
  }

  /**
   * Parses the following elements and configures instances of the classes
   * attributed to each element discovered:
   * <ul>
   * <li>&lt;fileRollEventListener class=&quot;...&quot; /&gt; for implementors
   * of {@link FileRollEventListener}</li>
   * <li>&lt;fileCompressionEventListener class=&quot;...&quot; /&gt; for
   * implementors of {@link FileCompressionEventListener}</li>
   * <li>&lt;logFileScavenger class=&quot;...&quot; /&gt; for
   * implementors of {@link LogFileScavenger}</li>
   * <li>&lt;logTaskExecutorService class=&quot;...&quot; /&gt; for
   * implementors of {@link LoggingTaskExecutorService}</li>
   * </ul>
   * For each of these elements, child &lt;param/&gt; elements are treated as
   * expected by the {@link DOMConfigurator}.
   * <p>
   * <em>NB</em> Only one of each element is permitted.
   * 
   * @see UnrecognizedElementHandler#parseUnrecognizedElement
   *      (org.w3c.dom.Element, java.util.Properties)
   */
  public final boolean parseUnrecognizedElement(final Element element,
      final Properties props) throws Exception {
    final String elementNodeName = element.getNodeName();
    if ("fileRollEventListener".equalsIgnoreCase(elementNodeName)) {
      final Object listener = DOMConfigurator.parseElement(element, props,
          FileRollEventListener.class);
      if (listener != null) {
        this.init((FileRollEventListener) listener);
        return true;
      }
    }
    if ("fileCompressionEventListener".equalsIgnoreCase(elementNodeName)) {
      final Object listener = DOMConfigurator.parseElement(element, props,
          FileCompressionEventListener.class);
      if (listener != null) {
        this.init((FileCompressionEventListener) listener);
        return true;
      }
    }
    if ("logFileScavenger".equalsIgnoreCase(elementNodeName)) {
      final Object scavenger = DOMConfigurator.parseElement(element, props,
          LogFileScavenger.class);
      if (scavenger != null) {
        this.init((LogFileScavenger) scavenger);
        return true;
      }
    }
    if ("logTaskExecutorService".equalsIgnoreCase(elementNodeName)) {
      final Object executorService = DOMConfigurator.parseElement(element,
          props, LoggingTaskExecutorService.class);
      if (executorService != null) {
        this.init((LoggingTaskExecutorService) executorService);
        return true;
      }
    }
    return false;
  }

  public String getCompressionAlgorithm() {
    return this.getProperties().getCompressionAlgorithm();
  }

  public boolean getCompressionUseBlockingQueue() {
    return this.getProperties().isCompressionBlocking();
  }

  public long getCompressionMaxBlockingInterval() {
    return this.getProperties().getCompressionMaxWait();
  }

  public int getCompressionMinQueueSize() {
    return this.getProperties().getCompressionMinQueueSize();
  }

  public int getCompressionLevel() {
    return this.getProperties().getCompressionLevel();
  }

  public String getDatePattern() {
    return this.getProperties().getDatePattern();
  }

  public String getDatePatternLocale() {
    return this.getProperties().getDatePatternLocale().toString();
  }

  public boolean getDateRollEnforced() {
    return this.getProperties().isDateRollEnforced();
  }

  /**
   * @return The fully qualified class name of the guest listener.
   * @deprecated
   */
  public String getFileRollEventListener() {
    return (this.getGuestFileRollEventListener() != null) ? this
        .getGuestFileRollEventListener().getClass().getName() : String
        .valueOf(this.getGuestFileRollEventListener());
  }
  
  public boolean getFlushToStorageOnAppend() {
    return this.getProperties().isFlushAfterWrite();
  }

  public String getMaxFileSize() {
    return Long.toString(this.getProperties().getMaxFileSize());
  }

  public String getMinFreeDiskSpace() {
    return Long.toString(this.getProperties().getMinFreeDiscSpace());
  }

  public int getMaxRollFileCount() {
    return this.getProperties().getMaxRollFileCount();
  }

  public boolean getRollOnStartup() {
    return this.getProperties().shouldRollOnStartup();
  }

  public long getScavengeInterval() {
    return this.getProperties().getScavengeInterval();
  }

  public String getTimeZoneId() {
    return this.getProperties().getTimeZone().getID();
  }

  /**
   * @param compressionAlgorithm
   *          &quot;ZIP&quot; or &quot;GZ&quot;
   */
  public void setCompressionAlgorithm(String compressionAlgorithm) {
    if (compressionAlgorithm == null) {
      LogLog.warn("Null name supplied for compression algorithm ["
          + this.getName() + "], defaulting to '"
          + this.getProperties().getCompressionAlgorithm() + '\'');
      return;
    }
    compressionAlgorithm = compressionAlgorithm.trim();
    if ("".equals(compressionAlgorithm)) {
      LogLog.warn("Empty name supplied for compression algorithm ["
          + this.getName() + "], defaulting to '"
          + this.getProperties().getCompressionAlgorithm() + '\'');
      return;
    }
    this.getProperties().setCompressionAlgorithm(compressionAlgorithm);
  }

  public void setCompressionUseBlockingQueue(
      final boolean compressionBlockingQueue) {
    this.getProperties().setCompressionBlocking(compressionBlockingQueue);
  }

  public void setCompressionMaxBlockingInterval(final long compressionInterval) {
    this.getProperties().setCompressionMaxWait(compressionInterval);
  }

  /**
   * @param compressionLevel
   *          {@link java.util.zip.Deflater#DEFAULT_COMPRESSION},
   *          {@link java.util.zip.Deflater#NO_COMPRESSION}, or in the range
   *          {@link java.util.zip.Deflater#BEST_SPEED} to {@link java.util.zip.Deflater#BEST_COMPRESSION}.
   * @see java.util.zip.Deflater
   */
  public void setCompressionLevel(final int compressionLevel) {
    this.getProperties().setCompressionLevel(compressionLevel);
  }

  /**
   * The minimum number of backup files that must be in the queue awaiting
   * compression before any compression will take place.
   * 
   * @param compressionMinQueueSize
   *          &gt;= 0.
   */
  public void setCompressionMinQueueSize(final int compressionMinQueueSize) {
    this.getProperties().setCompressionMinQueueSize(compressionMinQueueSize);
  }

  /**
   * @param datePattern
   *          in compliance with <em>localized</em> patterns similar to those
   *          specified by {@link java.text.SimpleDateFormat}. Note that the pattern
   *          characters in the main Javadoc of {@link java.text.SimpleDateFormat} are
   *          defaults for {@link Locale#ENGLISH}, if I understand correctly.
   * @see java.text.SimpleDateFormat
   */
  public void setDatePattern(String datePattern) {
    if (datePattern == null) {
      LogLog.warn("Null date pattern supplied for appender [" + this.getName()
          + "], defaulting to " + this.getProperties().getDatePattern());
      return;
    }
    datePattern = datePattern.trim();
    if ("".equals(datePattern)) {
      LogLog.warn("Empty date pattern supplied for appender [" + this.getName()
          + "], defaulting to " + this.getProperties().getDatePattern());
      return;
    }
    this.getProperties().setDatePattern(datePattern);
  }

  /**
   * Sets the {@link Locale} to be used when processing date patterns. Variants
   * are not supported; only language and (optionally) country may be used,
   * e.g.&nbsp;&quot;en&quot;, &nbsp;&quot;en_GB&quot; or &quot;fr_CA&quot; are
   * all valid. If no locale is supplied, {@link Locale#ENGLISH} will be used.
   * 
   * @param datePatternLocale
   * @see Locale
   * @see #setDatePattern(String)
   */
  // TODO Add support for Java 1.7 Locales
  public void setDatePatternLocale(String datePatternLocale) {
    if (datePatternLocale == null) {
      LogLog.warn("Null date pattern locale supplied for appender ["
          + this.getName() + "], defaulting to "
          + this.getProperties().getDatePatternLocale());
      return;
    }
    datePatternLocale = datePatternLocale.trim();
    if ("".equals(datePatternLocale)) {
      LogLog.warn("Empty date pattern locale supplied for appender ["
          + this.getName() + "], defaulting to "
          + this.getProperties().getDatePatternLocale());
      return;
    }
    final String[] parts = datePatternLocale.split("_");
    switch (parts.length) {
    case 1:
      this.getProperties().setDatePatternLocale(new Locale(parts[0]));
      break;
    case 2:
      this.getProperties().setDatePatternLocale(new Locale(parts[0], parts[1]));
      break;
    default:
      LogLog.warn("Unable to parse date pattern locale supplied for appender ["
          + this.getName() + "], defaulting to "
          + this.getProperties().getDatePatternLocale());
    }
  }

  /**
   * @param dateRollEnforced
   *          When <tt>true</tt> file rolls will occur pro-actively when the
   *          time boundary is reached, rather than reactively in response to a
   *          logging event.
   */
  public void setDateRollEnforced(final boolean dateRollEnforced) {
    this.getProperties().setDateRollEnforced(dateRollEnforced);
  }

  /**
   * @param className
   *          The fully qualified name of the class that implements the
   *          {@link FileRollEventListener} interface; implementors must be
   *          declared public and have a default constructor.
   * @deprecated Configure {@link FileRollEventListener}s via XML, using the
   *             &quot;fileRollEventListener&quot; element.
   */
  public void setFileRollEventListener(String className) {
    if (className != null) {
      className = className.trim();
      final FileRollEventListener fileRollEventListener = (FileRollEventListener) OptionConverter
          .instantiateByClassName(className, FileRollEventListener.class, null);
      if (fileRollEventListener != null) {
        this.init(fileRollEventListener);
      }
    }
  }
  
  /**
   * When this property is <tt>true</tt>, configured values for the properties
   * {@link FileAppender#setBufferedIO(boolean)}and
   * {@link WriterAppender#setImmediateFlush(boolean)} are overridden to
   * <tt>false</tt> and <tt>true</tt>, respectively, upon activation of an
   * instance of this object.
   * 
   * @param flushToStorageOnAppend
   *          <tt>false</tt> by default.
   */
  public void setFlushToStorageOnAppend(final boolean flushToStorageOnAppend) {
    this.getProperties().setFlushAfterWrite(flushToStorageOnAppend);
  }

  /**
   * @param className
   *          The fully qualified name of the class that implements the
   *          {@link LogFileScavenger} interface; implementors must be declared
   *          public and have a default constructor.
   * @deprecated Configure {@link LogFileScavenger}s via XML, using the
   *             &quot;logFileScavenger&quot; element.
   */
  public void setLogFileScavenger(String className) {
    if (className != null) {
      className = className.trim();
      final LogFileScavenger logFileScavenger = (LogFileScavenger) OptionConverter
          .instantiateByClassName(className, LogFileScavenger.class, null);
      if (logFileScavenger != null) {
        this.init(logFileScavenger);
      }
    }
  }

  /**
   * @param message
   *          The message to be appended at the top of each new file created
   *          following a file roll. If the message is empty, a default message
   *          will be appended instead.
   */
  public void setFileRollEventMessage(final String message) {
    if (message != null) {
      if (!"".equals(message.trim())) {
        this.init(new FileRollEventListener() {
          public final void onFileRoll(final FileRollEvent fileRollEvent) {
            fileRollEvent.dispatchToAppender(message);
          }
        });
      } else {
        this.init(new FileRollEventListener() {
          public final void onFileRoll(final FileRollEvent fileRollEvent) {
            fileRollEvent.dispatchToAppender();
          }
        });
      }
    }
  }

  public void setMaxFileSize(String value) {
    if (value == null) {
      LogLog.warn("Null max file size supplied for appender [" + this.getName()
          + "], defaulting to " + this.getProperties().getMaxFileSize());
      return;
    }
    value = value.trim();
    if ("".equals(value)) {
      LogLog.warn("Empty max file size supplied for appender ["
          + this.getName() + "], defaulting to "
          + this.getProperties().getMaxFileSize());
      return;
    }
    final long defaultMaxFileSize = this.getProperties().getMaxFileSize();
    final long maxFileSize = OptionConverter.toFileSize(value,
        defaultMaxFileSize);
    this.getProperties().setMaxFileSize(maxFileSize);
  }

  /**
   * <b>Warning</b> Use of this property requires Java 6.
   * 
   * @param value
   */
  public void setMinFreeDiskSpace(String value) {
    if (value == null) {
      LogLog.warn("Null min free disk space supplied for appender ["
          + this.getName() + "], defaulting to "
          + this.getProperties().getMinFreeDiscSpace());
      return;
    }
    value = value.trim();
    if ("".equals(value)) {
      LogLog.warn("Empty min free disk space supplied for appender ["
          + this.getName() + "], defaulting to "
          + this.getProperties().getMinFreeDiscSpace());
      return;
    }
    final long defaultMinFreeDiskSpace = this.getProperties()
        .getMinFreeDiscSpace();
    final long minFreeDiskSpace = OptionConverter.toFileSize(value,
        defaultMinFreeDiskSpace);
    this.getProperties().setMinFreeDiscSpace(minFreeDiskSpace);
  }

  public void setMaxRollFileCount(final int maxRollFileCount) {
    this.getProperties().setMaxRollFileCount(maxRollFileCount);
  }

  /**
   * @param rollOnStartup
   *          <tt>true</tt> if the appender should roll, and create a new log
   *          file, immediately upon receiving the first logging event after
   *          activation.
   */
  public void setRollOnStartup(final boolean rollOnStartup) {
    this.getProperties().setRollOnStartup(rollOnStartup);
  }

  public void setScavengeInterval(final long intervalMillis) {
    this.getProperties().setScavengeInterval(intervalMillis);
  }

  public void setMinFreeDiskSpace(final long minFreeDiskSpace) {
    this.getProperties().setMinFreeDiscSpace(minFreeDiskSpace);
  }

  public void setTimeZoneId(String timeZoneId) {
    if (timeZoneId == null) {
      LogLog.warn("Null time zone ID supplied for appender [" + this.getName()
          + "], defaulting to " + this.getProperties().getTimeZone());
      return;
    }
    timeZoneId = timeZoneId.trim();
    if ("".equals(timeZoneId)) {
      LogLog.warn("Empty time zone ID supplied for appender [" + this.getName()
          + "], defaulting to " + this.getProperties().getTimeZone());
      return;
    }
    TimeZone timeZone = null;
    final String[] timeZoneIdArray = TimeZone.getAvailableIDs();
    for (int i = 0; i < timeZoneIdArray.length; i++) {
      if (timeZoneIdArray[i].equals(timeZoneId)) {
        timeZone = TimeZone.getTimeZone(timeZoneId);
        break;
      }
    }
    if (timeZone == null) {
      LogLog.warn("Supplied time zone ID not known for appender ["
          + this.getName() + "], defaulting to "
          + this.getProperties().getTimeZone());
      return;
    }
    this.getProperties().setTimeZone(timeZone);
  }

  final FileRoller getFileRoller() {
    return this.fileRoller;
  }
  
  // for internal and test use only
  final FileRollEventListener getGuestFileRollEventListener() {
    return this.guestFileRollEventListener;
  }

  // for internal and test use only
  final FileCompressionEventListener getGuestFileCompressionEventListener() {
    return this.guestFileCompressionEventListener;
  }

  // for internal and test use only
  final LogFileCompressorTask getLogFileCompressor() {
    return this.logFileCompressor;
  }

  // for internal and test use only
  final LogFileScavenger getLogFileScavenger() {
    return this.logFileScavenger;
  }
  
  // for internal and test use only
  final LoggingTaskExecutorService getExecutorService() {
    return this.executorService;
  }

  final AppenderRollingProperties getProperties() {
    return this.properties;
  }

  final File getIoFile() {
    final String fileName = super.getFile();
    if (fileName == null) {
      super.getErrorHandler().error("Filename has not been set",
          new IllegalStateException(), ErrorCode.FILE_OPEN_FAILURE);
      return null;
    }
    return new File(fileName);
  }

  /**
   * Opens the log file and prepares this appender to write to it.
   */
  final void openFile() {
    /*
     * FileAppender::activateOptions() calls setFile(:String, :boolean,
     * :boolean, :int) with all the options and error handling we're interested
     * in. NB - this is a deliberate super call since we don't want to stop this
     * appender's scavenger, etc.
     */
    super.activateOptions();
  }
  
  /**
   * Test use only.
   * 
   * @param loggingTaskExecutorService
   */
  final void setLogTaskExecutorService(
      LoggingTaskExecutorService loggingTaskExecutorService) {
    LogLog.warn("Using test-only method to set "
        + LoggingTaskExecutorService.class + " on " + this.getClass()
        + " instance");
    if (loggingTaskExecutorService == null) {
      LogLog.warn("Setting null " + LoggingTaskExecutorService.class + " on "
          + this.getClass() + " instance");
    }
    this.setExecutorService(loggingTaskExecutorService);
  }

  /**
   * Makes file closing behaviour visible to classes in this package.
   * 
   * @see FileAppender#closeFile()
   */
  protected final void closeFile() {
    /*
     * The Log4J 1.2.15 WriterAppender doesn't write footers on file roll, so do
     * it here.
     */
    this.writeFooter();
    /*
     * closeWriter() duplicates closeFile(), so we can take our pick
     */
    super.closeWriter();
    super.qw = null;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.apache.log4j.WriterAppender#createWriter(java.io.OutputStream)
   */
  protected final OutputStreamWriter createWriter(final OutputStream os) {
    if (this.getProperties().isFlushAfterWrite()) {
      if (os instanceof FileOutputStream) {
        try {
          return super.createWriter(new FlushAfterWriteFileOutputStream(
              (FileOutputStream) os));
        } catch (IOException e) {
          if (e instanceof InterruptedIOException) {
            Thread.currentThread().interrupt();
          }
          super.getErrorHandler().error(
              "Failed to open stream used to flush to storage on append.", e,
              ErrorCode.FILE_OPEN_FAILURE);
        }
      } else {
        LogLog.warn("Expected " + FileOutputStream.class.getName()
            + " but was " + os.getClass().getName()
            + "; flush to storage on append is disabled");
      }
    }
    return super.createWriter(os);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.log4j.FileAppender#setQWForFiles(java.io.Writer)
   */
  protected final void setQWForFiles(final Writer writer) {
    final SynchronizedCountingQuietWriter countingQuietWriter = new SynchronizedCountingQuietWriter(
        writer, super.getErrorHandler());
    this.getProperties().setCountingQuietWriter(countingQuietWriter);
    super.qw = countingQuietWriter;
  }

  /**
   * Responsible for executing file rolls as and when required, in addition to
   * delegating to the super class to perform the actual append operation.
   * Synchronized for safety during enforced file roll.
   * 
   * @see WriterAppender#subAppend(LoggingEvent)
   */
  protected final void subAppend(final LoggingEvent event) {
    if (event instanceof ScheduledFileRollEvent) {
      // the scheduled append() call has been made by a different thread
      synchronized (this) {
        if (this.closed) {
          // just consume the event
          return;
        }
        this.rollFile(event);
      }
    } else if (event instanceof FileRollEvent) {
      // definitely want to avoid rolling here whilst a file roll event is still
      // being handled
      super.subAppend(event);
    } else {
      this.rollFile(event);
      super.subAppend(event);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.log4j.WriterAppender#writeFooter()
   */
  protected final void writeFooter() {
    if (this.getProperties().isActivatingAppender()) {
      return;
    }
    super.writeFooter();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.log4j.WriterAppender#writeHeader()
   */
  protected final void writeHeader() {
    if (this.getProperties().shouldRollOnActivation()) {
      return;
    }
    super.writeHeader();
  }

  private synchronized void deactivateOptions() {
    // scavenger
    final LogFileScavenger logFileScavenger = this.getLogFileScavenger();
    if (logFileScavenger != null) {
      logFileScavenger.end();
    }
    // roll enforcer
    final TimeBasedRollEnforcer logRollEnforcer = this.getLogRollEnforcer();
    if (logRollEnforcer != null) {
      logRollEnforcer.end();
    }
    // compressor
    final LogFileCompressorTask logFileCompressor = this.getLogFileCompressor();
    final FileRollEventSource fileRollEventSource = this
        .getFileRollEventSource();
    if (fileRollEventSource != null) {
      fileRollEventSource.removeFileRollEventListener(logFileCompressor);
    }
    // executor
    final LoggingTaskExecutorService executorService = this
        .getExecutorService();
    if (executorService != null) {
      executorService.shutdownNow();
    }
    // guest listeners
    this.deregisterGuestFileRollEventListener();
    this.deregisterGuestFileCompressionEventListener();
  }

  private void deregisterGuestFileRollEventListener() {
    final FileRollEventListener listener = this.getGuestFileRollEventListener();
    if (listener != null) {
      // there may be only one guest listener, so remove any
      // previously-registered listeners
      final FileRollEventSource fileRollEventSource = this
          .getFileRollEventSource();
      if (fileRollEventSource != null) {
        fileRollEventSource.removeFileRollEventListener(listener);
      }
    }
  }

  private void deregisterGuestFileCompressionEventListener() {
    final FileCompressionEventListener listener = this
        .getGuestFileCompressionEventListener();
    if (listener != null) {
      // there may be only one guest listener, so remove any
      // previously-registered listeners
      final LogFileCompressorTask logFileCompressor = this.getLogFileCompressor();
      if (logFileCompressor != null) {
        logFileCompressor.removeFileCompressionEventListener(listener);
      }
    }
  }

  private void registerGuestFileRollEventListener() {
    final FileRollEventListener listener = this.getGuestFileRollEventListener();
    if (listener != null) {
      final FileRollEventSource fileRollEventSource = this
          .getFileRollEventSource();
      if (fileRollEventSource != null) {
        fileRollEventSource.addFileRollEventListener(listener);
      }
    }
  }

  private void registerGuestFileCompressionEventListener() {
    final FileCompressionEventListener listener = this
        .getGuestFileCompressionEventListener();
    if (listener != null) {
      final LogFileCompressorTask logFileCompressor = this.getLogFileCompressor();
      if (logFileCompressor != null) {
        logFileCompressor.addFileCompressionEventListener(listener);
      }
    }
  }

  private void rollFile(final LoggingEvent event) {
    this.getFileRollable().roll(event);
  }

  private void rollOnStartup() {
    if (this.getProperties().shouldRollOnActivation()) {
      this.rollFile(new StartupFileRollEvent());
    }
  }

  private FileRollable getFileRollable() {
    return this.fileRollable;
  }

  private void setFileRollable(final FileRollable fileRollable) {
    this.fileRollable = fileRollable;
  }
  
  private FileRollEventSource getFileRollEventSource() {
    return this.getFileRoller();
  }
  
  private void setFileRoller(final FileRoller fileRoller) {
    this.fileRoller = fileRoller;
  }

  private void init(final FileRollEventListener guestListener) {
    this.deregisterGuestFileRollEventListener();
    this.guestFileRollEventListener = guestListener;
  }

  private void init(final FileCompressionEventListener guestListener) {
    this.deregisterGuestFileCompressionEventListener();
    this.guestFileCompressionEventListener = guestListener;
  }

  private LogFileScavenger init(final LogFileScavenger logFileScavenger) {
    if (this.logFileScavenger != null) {
      this.logFileScavenger.end();
    }
    logFileScavenger.init(this, this.getProperties());
    this.logFileScavenger = logFileScavenger;
    return logFileScavenger;
  }

  private LoggingTaskExecutorService init(
      final LoggingTaskExecutorService executorService) {
    if (this.executorService != null) {
      this.executorService.shutdownNow();
      this.executorService.setAppender(null);
    }
    this.setExecutorService(executorService);
    executorService.setAppender(this);
    return executorService;
  }

  private void setExecutorService(final LoggingTaskExecutorService executorService) {
    this.executorService = executorService;
  }

  private void setLogFileCompressor(final LogFileCompressorTask logFileCompressor) {
    this.logFileCompressor = logFileCompressor;
  }

  private TimeBasedRollEnforcer getLogRollEnforcer() {
    return logRollEnforcer;
  }

  private void setLogRollEnforcer(final TimeBasedRollEnforcer logRollEnforcer) {
    if (this.logRollEnforcer != null) {
      this.logRollEnforcer.end();
    }
    this.logRollEnforcer = logRollEnforcer;
  }

}
