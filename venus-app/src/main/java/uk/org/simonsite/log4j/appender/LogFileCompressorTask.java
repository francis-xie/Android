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
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;

import uk.org.simonsite.log4j.helpers.AtomicInteger;
import uk.org.simonsite.log4j.helpers.FileHelper;

/**
 * Responsible for compressing log files using a given compression algorithm,
 * adding checksums if specified.
 */
final class LogFileCompressorTask implements Runnable, FileRollEventListener {

  private static final int QUEUE_LIMIT = 64;

  private final TimeAndSizeRollingAppender appender;

  private final AppenderRollingProperties properties;

  private final LoggingTaskExecutorService executorService;

  private final List queue;

  private final List fileCompressionEventListeners;

  private final AtomicInteger testSize;

  LogFileCompressorTask(final TimeAndSizeRollingAppender rollingAppender) {
    super();
    this.appender = rollingAppender;
    this.properties = rollingAppender.getProperties();
    this.queue = new LinkedList();
    this.executorService = this.appender.getExecutorService();
    this.fileCompressionEventListeners = Collections
        .synchronizedList(new ArrayList());
    this.testSize = new AtomicInteger();
  }

  public final void run() {
    try {
      this.compressNext();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (InterruptedIOException e) {
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      this.appender.getErrorHandler().error("Log file compressor failed", e,
          ErrorCode.GENERIC_FAILURE);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.org.simonsite.log4j.appender.FileRollEventListener#onFileRoll(org.apache
   * .log4j .appender.FileRollEvent)
   */
  public final void onFileRoll(final FileRollEvent fileRollEvent) {
    this.compress(fileRollEvent.getBackupFile());
  }

  /**
   * For test purposes only.
   * 
   * @return max files in the backup file queue
   */
  final int getQueueLimit() {
    return QUEUE_LIMIT;
  }

  /**
   * For test purposes only.
   * 
   * @return number of files remaining in the backup file queue
   */
  final int getTestQueueSize() {
    return this.testSize.get();
  }

  /**
   * For test purposes only.
   */
  final void waitForEmptyQueue() {
    this.waitForSizeQueue(0);
  }

  /**
   * For test purposes only.
   */
  final void waitForSizeQueue(final int queueSize) {
    synchronized (this.testSize) {
      while (this.testSize.get() > queueSize) {
        try {
          this.testSize.wait(250L);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
    }
    if (Thread.currentThread().isInterrupted()) {
      return;
    }
    try {
      Thread.sleep(500L);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  final void compress(final File file) {
    if (LogFileCompressionStrategy.existsFor(this.properties)) {
      synchronized (this.queue) {
        while (this.queue.size() > QUEUE_LIMIT) {
          if (this.properties.isCompressionBlocking()) {
            try {
              this.queue.wait(this.properties.getCompressionMaxWait());
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          } else {
            this.queue.remove(0);
            this.testSize.decrementAndGet();
          }
        }
        this.queue.add(file);
        this.testSize.incrementAndGet();
        this.executorService.submit(this);
        this.queue.notifyAll();
      }
    } else {
      LogLog.debug("File '" + file
          + "' not compressed: no strategy for algorithm '"
          + this.properties.getCompressionAlgorithm() + '\'');
    }
  }

  final void addFileCompressionEventListener(
      final FileCompressionEventListener fileCompressionEventListener) {
    this.fileCompressionEventListeners.add(fileCompressionEventListener);
  }

  final void removeFileCompressionEventListener(
      final FileCompressionEventListener fileCompressionEventListener) {
    this.fileCompressionEventListeners.remove(fileCompressionEventListener);
  }

  final void fireFileCompressionEvent(
      final FileCompressionEvent fileCompressionEvent) {
    final Object[] listeners = this.fileCompressionEventListeners.toArray();
    for (int i = 0; i < listeners.length; i++) {
      final FileCompressionEventListener listener = (FileCompressionEventListener) listeners[i];
      try {
        listener.onFileCompression(fileCompressionEvent);
      } catch (RuntimeException e) {
        this.appender.getErrorHandler().error(
            "Failed to dispatch file compression event", e,
            ErrorCode.GENERIC_FAILURE);
      }
    }
  }

  private boolean queueBelowMinSize() {
    boolean belowMinSize = false;
    final int compressionMinQueueSize = this.properties
        .getCompressionMinQueueSize();
    final int queueSize = this.queue.size();
    if (compressionMinQueueSize > 0) {
      belowMinSize = (queueSize < compressionMinQueueSize);
    } else {
      belowMinSize = (queueSize == 0);
    }
    return belowMinSize;
  }

  private void compressNext() throws InterruptedException,
      InterruptedIOException {
    File file = null;
    synchronized (this.queue) {
      if (!this.queueBelowMinSize()) {
        file = (File) this.queue.remove(0);
        this.testSize.decrementAndGet();
      }
      this.queue.notifyAll();
    }
    if (file != null) {
      this.doCompression(file);
    }
  }

  private void doCompression(final File file) {
    if (FileHelper.getInstance().isWriteable(file)) {
      final LogFileCompressionStrategy compressionStrategy = LogFileCompressionStrategy
          .findCompressionStrategy(this.properties);
      if (compressionStrategy != null) {
        final File deflatedFile = compressionStrategy.compress(file,
            this.properties);
        if (deflatedFile != null) {
          this.fireFileCompressionEvent(new FileCompressionEvent(deflatedFile));
        }
      }
    }
  }
}
