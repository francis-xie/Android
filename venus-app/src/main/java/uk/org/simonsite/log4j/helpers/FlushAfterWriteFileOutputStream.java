
package uk.org.simonsite.log4j.helpers;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * After each write is complete this class is responsible for actively flushing
 * OS buffers, such that the bytes written are committed to permanent storage,
 * on a local storage device (subject to support in the underlying OS).
 */
public final class FlushAfterWriteFileOutputStream extends OutputStream {

  private final FileOutputStream stream;

  private final FileDescriptor descriptor;

  public FlushAfterWriteFileOutputStream(final FileOutputStream fileOutputStream)
      throws IOException {
    super();
    this.stream = fileOutputStream;
    this.descriptor = fileOutputStream.getFD();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#close()
   */
  public final void close() throws IOException {
    this.flush();
    this.stream.close();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#flush()
   */
  public final void flush() throws IOException {
    this.descriptor.sync();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#write(byte[])
   */
  public final void write(final byte[] b) throws IOException {
    this.write(b, 0, b.length);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#write(byte[], int, int)
   */
  public final void write(final byte[] b, final int off, final int len)
      throws IOException {
    this.stream.write(b, off, len);
    this.flush();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#write(int)
   */
  public final void write(final int b) throws IOException {
    throw new UnsupportedOperationException(
        "Flush on each byte is not a supported use case");
  }
}
