package com.emis.venus.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class GHelper {

  /**
   * 显示错误信息
   *
   * @param oContext
   * @param sMsg     显示的消息
   */
  public static void showErrorMsgBox(Context oContext, String sMsg) {
    showVenusMsgBox(oContext, sMsg, null);
  }

  /**
   * 显示通知信息
   *
   * @param oContext
   * @param sMsg     显示的消息
   * @return
   */
  public static void showInfoMsgBox(Context oContext, String sMsg) {
    showVenusMsgBox(oContext, sMsg, null);
  }

  /**
   * 显示确定取消询问信息
   *
   * @param oContext
   * @param sMsg     显示的消息
   * @return 确定 SWT.OK 取消 SWT.CANCEL
   */
  public static boolean showOkCancelMsgBox(Context oContext, String sMsg) {
    return 0 == showVenusMsgBox(oContext, sMsg, new String[]{"确定", "取消"});
  }

  /**
   * 显示是否询问信息
   *
   * @param oContext
   * @param sMsg     显示的消息
   * @return 是 true 否 false
   */
  public static boolean showYesNoMsgBox(Context oContext, String sMsg) {
    return 0 == showVenusMsgBox(oContext, sMsg, new String[]{"是", "否"});
  }

  /**
   * 显示是否取消询问信息
   *
   * @param oContext
   * @param sMsg     显示的消息
   * @return 是 SWT.OK 否 SWT.NO 取消 SWT.CANCEL
   */
  public static int showYesNoCancelMsgBox(Context oContext, String sMsg) {
    return showVenusMsgBox(oContext, sMsg, new String[]{"是", "否", "取消"});
  }

  /**
   * 显示信息
   *
   * @param oContext
   * @param sMsg     显示的消息
   * @param aButtons 画面按钮样式 SWT.OK ; SWT.ERROR; SWT.OK|SWT.CANCEL; SWT.YES|SWT.NO;
   *                 SWT.YES|SWT.NO|SWT.CANCEL
   * @return YesNoCancel :是 SWT.OK 否 SWT.NO 取消 SWT.CANCEL <br>
   * YesNo :是 SWT.OK 否 SWT.CANCEL <br>
   * OkCancel :确定 SWT.OK 取消 SWT.CANCEL
   */
  public static int showVenusMsgBox(Context oContext, String sMsg, String[] aButtons) {
    return showVenusMsgBox(oContext, null, sMsg, aButtons);
  }

  /**
   * 显示信息
   *
   * @param oContext
   * @param sTitle   显示的标题
   * @param sMsg     显示的消息
   * @param aButtons 画面按钮样式 SWT.OK ; SWT.ERROR; SWT.OK|SWT.CANCEL; SWT.YES|SWT.NO;
   *                 SWT.YES|SWT.NO|SWT.CANCEL
   * @return YesNoCancel :是 SWT.OK 否 SWT.NO 取消 SWT.CANCEL <br>
   * YesNo :是 SWT.OK 否 SWT.CANCEL <br>
   * OkCancel :确定 SWT.OK 取消 SWT.CANCEL
   */
  public static int showVenusMsgBox(Context oContext, String sTitle, String sMsg, String[] aButtons) {
    AlertDialog.Builder builder = new AlertDialog.Builder(oContext); //创建AlertDialog.Builder对象
    if (sTitle != null && !"".equals(sTitle)) {
      builder.setTitle(sTitle); // 设置标题
    }
    builder.setTitle("设定"); // 设置标题
    builder.setMessage(sMsg); // 设置对话框的内容
    if (aButtons == null || aButtons.length == 1) {
      builder.setPositiveButton((aButtons == null ? "确定" : aButtons[0]), doOnClickListener()); //设置确定按钮
    } else if (aButtons.length == 2) {
      builder.setPositiveButton(aButtons[0], doOnClickListener()); //设置确定按钮
      builder.setNegativeButton(aButtons[1], doOnClickListener()); //设置取消按钮
    } else if (aButtons.length >= 3) {
      builder.setPositiveButton(aButtons[0], doOnClickListener()); //设置确定按钮
      builder.setNegativeButton(aButtons[1], doOnClickListener()); //设置取消按钮
      builder.setNeutralButton(aButtons[2], doOnClickListener()); //设置中立按钮
    }
    AlertDialog dialog = builder.create(); //创建对话框对象
    dialog.setIcon(android.R.drawable.ic_dialog_alert); //设置图标
    dialog.show(); //显示对话框

    return 0;
  }

  private static DialogInterface.OnClickListener doOnClickListener() {
    return new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    };
  }
}
