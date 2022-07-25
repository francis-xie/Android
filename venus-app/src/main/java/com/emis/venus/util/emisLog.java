package com.emis.venus.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class emisLog {

  public static FileWriter logWriter;
  public static File _logTxtfile;

  public static void d(Class c, String info) {
    Log.d(c.getName(), info);
  }

  public static void i(Class c, String info) {
    Log.i(c.getName(), info);
  }

  public static void e(Class c, String info) {
    Log.e(c.getName(), info);
  }

  public static FileWriter getLogWriter() {
    return logWriter;
  }

  public static void setLogWriter(FileWriter logWriter) {
    emisLog.logWriter = logWriter;
  }

  public static void checkLogFile() {
    try {
      String _rootPath = Environment.getExternalStorageDirectory() + "";//app目錄
      String _logFilePath = _rootPath + "/log";//Log目錄
      SimpleDateFormat _formatter = new SimpleDateFormat("yyyyMMdd");
      Date _nowDate = new Date();
      String _ymfileName = _formatter.format(_nowDate);//like 20160112

      //如果沒有Log資料夾則新增
      File _logFile = new File(_logFilePath);
      if (!_logFile.exists()) {
        _logFile.mkdirs();
      }
      // 檢查Log年月資料夾
      File _logYMFile = new File(_logFilePath + "/" + _ymfileName.substring(0, 6));
      if (!_logYMFile.exists()) {
        _logYMFile.mkdirs();
      }
      // 刪除超過七天的LOG
//            String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
//            Log.d("Files", "Path: " + path);
//            File f = new File(path);
//            File file[] = f.listFiles();
//            Log.d("Files", "Size: "+ file.length);
//            for (int i=0; i < file.length; i++)
//            {
//                Log.d("Files", "FileName:" + file[i].getName());
//            }

      _logTxtfile = new File(_logYMFile, _ymfileName + ".txt");

      logWriter = new FileWriter(_logTxtfile, true);
    } catch (Exception e) {
      Log.d("emisLog.checkLogFile()", e.getMessage());
    }
  }

  public static void delLogFile(String date) {
    String _rootPath = Environment.getExternalStorageDirectory() + "";//app目錄
    String _logFilePath = _rootPath + "/log";//Log目錄

    File _logFile = new File(_logFilePath);
    if (!_logFile.exists()) {
      _logFile.mkdirs();
    }
    File _logYMFile = new File(_logFilePath + "/" + date.substring(0, 6));
    if (!_logYMFile.exists()) {
      _logYMFile.mkdirs();
    }

    File _logDelte = new File(_logYMFile, date + ".txt");
    _logDelte.delete();
  }

  public static void addLog(String msg) {
    try {
      if (logWriter == null) checkLogFile();

      logWriter.append("[" + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()) + "]:" + msg + "\n");
      logWriter.flush();
    } catch (Exception e) {
      Log.d("emisLog.addLog()", e.getMessage());
    }
  }

  public static void addLog(String msg, Exception e) {
    try {
      if (logWriter == null) checkLogFile();

      logWriter.append("[" + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()) + "]:" + msg + "\n");

      StackTraceElement[] temp = e.getStackTrace();
      for (int i = 0; i < temp.length; i++) {
        StackTraceElement tempEle = temp[i];
        logWriter.append(tempEle.toString() + "\n");
      }
      logWriter.flush();
    } catch (Exception e1) {
      Log.d("emisLog.addLog()", e1.getMessage());
    }
  }

  public static void addLog(String msg, Throwable e) {
    try {
      if (logWriter == null) checkLogFile();

      logWriter.append("[" + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()) + "]:" + msg + "\n");

      StackTraceElement[] temp = e.getStackTrace();
      for (int i = 0; i < temp.length; i++) {
        StackTraceElement tempEle = temp[i];
        logWriter.append(tempEle.toString() + "\n");
      }
      logWriter.flush();
    } catch (Exception e1) {
      Log.d("emisLog.addLog()", e1.getMessage());
    }
  }

  public static void closeLog() {
    try {
      if (logWriter != null) {
        addLog("===== Close Log =====");
        logWriter.close();
      }
    } catch (Exception e) {
      Log.d("emisLog.closeLog()", e.getMessage());
    }
  }

  public static File getLogFile() {
    try {
      checkLogFile();
    } catch (Exception e) {
      Log.d("emisLog.getLogFile()", e.getMessage());
    }
    return _logTxtfile;
  }

}
