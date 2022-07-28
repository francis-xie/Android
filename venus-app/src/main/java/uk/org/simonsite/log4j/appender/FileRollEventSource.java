package uk.org.simonsite.log4j.appender;

interface FileRollEventSource {

  void addFileRollEventListener(FileRollEventListener fileRollEventListener);

  void removeFileRollEventListener(FileRollEventListener fileRollEventListener);

  void fireFileRollEvent(FileRollEvent fileRollEvent);
}
