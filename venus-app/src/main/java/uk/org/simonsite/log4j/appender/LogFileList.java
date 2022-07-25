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
import java.io.FilenameFilter;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import uk.org.simonsite.log4j.helpers.FileHelper;

/**
 * Responsible for listing, sorting, and filtering log file lists.
 * <p>
 * This class is not thread-safe.
 * 
 * @author <a href="mailto:simon_park_mail AT yahoo DOT co DOT uk">Simon
 *         Park</a>
 * @version 3.6
 */
final class LogFileList extends AbstractList implements List {

  private final BackupSuffixHelper backupSuffixHelper;

  private final File baseFile;

  private final FileHelper fileHelper;

  private File[] fileArray;

  private int size;

  private final FilenameFilter filenameFilter;

  /**
   * 
   */
  LogFileList(final File baseFile, final FilenameFilter filenameFilter,
      final AppenderRollingProperties properties) {
    super();
    this.backupSuffixHelper = new BackupSuffixHelper(properties);
    this.baseFile = baseFile;
    this.fileArray = new File[0];
    this.fileHelper = FileHelper.getInstance();
    this.filenameFilter = filenameFilter;
    this.init();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.AbstractList#get(int)
   */
  public final Object get(final int index) {
    this.rangeCheck(index);
    return this.fileArray[index];
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.AbstractList#remove(int)
   */
  public final Object remove(final int index) {
    // Implementation lifted from java.util.ArrayList
    this.rangeCheck(index);
    super.modCount++;
    final Object oldValue = this.fileArray[index];
    final int numMoved = this.size - index - 1;
    if (numMoved > 0) {
      System.arraycopy(this.fileArray, index + 1, this.fileArray, index,
          numMoved);
    }
    this.fileArray[--this.size] = null; // Let gc do its work
    return oldValue;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.AbstractCollection#size()
   */
  public final int size() {
    return this.size;
  }

  /**
   * @return The first {@link File} in the list, or null if the list is empty.
   */
  final File firstFile() {
    return (this.size > 0) ? ((File) this.fileArray[0]) : null;
  }

  /**
   * @return The last {@link File} in the list, or null if the list is empty.
   */
  final File lastFile() {
    return (this.size > 0) ? ((File) this.fileArray[this.size - 1]) : null;
  }

  private void init() {
    File dir = null;
    if (this.baseFile.isDirectory()) {
      dir = this.baseFile;
    } else {
      dir = this.fileHelper.parentDirOf(this.baseFile);
    }
    if (dir != null) {
      this.readFileListFrom(dir);
      this.sortFileList();
    }
  }

  private void rangeCheck(final int index) {
    if (index >= this.size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
          + this.size);
    }
  }

  private void readFileListFrom(final File dir) {
    if (this.fileHelper.isReadable(dir)) {
      final File[] files = (this.filenameFilter != null) ? dir
          .listFiles(this.filenameFilter) : dir.listFiles();
      if (files != null) {
        this.fileArray = files;
        this.size = files.length;
      }
    }
  }

  /**
   * Sort by time bucket, then backup count, and by compression state.
   */
  private void sortFileList() {
    if (this.size > 1) {
      Arrays.sort(this.fileArray, new Comparator() {

        public final int compare(final Object o1, final Object o2) {
          final File f1 = (File) o1;
          final File f2 = (File) o2;
          final long[] f1TimeAndCount = backupSuffixHelper
              .backupTimeAndCount(f1.getName(), baseFile);
          final long[] f2TimeAndCount = backupSuffixHelper
              .backupTimeAndCount(f2.getName(), baseFile);
          final long f1TimeSuffix = f1TimeAndCount[0];
          final long f2TimeSuffix = f2TimeAndCount[0];
          if ((0L == f1TimeSuffix) && (0L == f2TimeSuffix)) {
            final long f1Time = f1.lastModified();
            final long f2Time = f2.lastModified();
            if (f1Time < f2Time) {
              return -1;
            }
            if (f1Time > f2Time) {
              return 1;
            }
            return 0;
          }
          if (f1TimeSuffix < f2TimeSuffix) {
            return -1;
          }
          if (f1TimeSuffix > f2TimeSuffix) {
            return 1;
          }
          final int f1Count = (int) f1TimeAndCount[1];
          final int f2Count = (int) f2TimeAndCount[1];
          if (f1Count < f2Count) {
            return -1;
          }
          if (f1Count > f2Count) {
            return 1;
          }
          if (f1Count == f2Count) {
            if (fileHelper.isCompressed(f1)) {
              return -1;
            }
            if (fileHelper.isCompressed(f2)) {
              return 1;
            }
          }
          return 0;
        }
      });
    }
  }
}
