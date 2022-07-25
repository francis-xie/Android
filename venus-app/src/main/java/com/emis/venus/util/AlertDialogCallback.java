package com.emis.venus.util;

/**
 * Callback interface for AlertDialog. 需要使用的Activity要實作doAlertDialogCallback.
 */
public interface AlertDialogCallback<T> {
  public void doAlertDialogCallback(T ret);
}
