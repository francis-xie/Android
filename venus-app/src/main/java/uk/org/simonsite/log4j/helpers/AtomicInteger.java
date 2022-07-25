package uk.org.simonsite.log4j.helpers;

public final class AtomicInteger {

  private volatile int value = 0;

  public final int get() {
    return this.value;
  }

  public final synchronized int incrementAndGet() {
    return ++this.value;
  }

  public final synchronized int decrementAndGet() {
    return --this.value;
  }
}