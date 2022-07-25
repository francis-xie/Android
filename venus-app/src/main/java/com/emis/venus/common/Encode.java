package com.emis.venus.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.emis.venus.util.emisUtil;

public class Encode {
  //---------byte转化成String
  public static String byteArrayToHexStr(byte[] byteArray) {
    if (byteArray == null) {
      return null;
    }
    char[] hexArray = "0123456789ABCDEF".toCharArray();
    char[] hexChars = new char[byteArray.length * 2];
    for (int j = 0; j < byteArray.length; j++) {
      int v = byteArray[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  //-------String转化成byte[]
  public static byte[] HexStrTobyteArray(String str) {
    if (str == null) {
      return null;
    }
    if (str.length() == 0) {
      return new byte[0];
    }
    byte[] byteArray = new byte[str.length() / 2];
    for (int i = 0; i < byteArray.length; i++) {
      String subStr = str.substring(2 * i, 2 * i + 2);
      byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
    }
    return byteArray;
  }

  public static Bitmap String2Bitmap(String code) {
    Bitmap bitmap = null;
    try {
      byte[] bitmapArray;
      bitmapArray = Base64.decode(code, Base64.DEFAULT);
      bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bitmap;
  }

  public static boolean checkTax(String _sTaxId) {
    //sNo_ = emisUtil.parseString(sNo_);
    if ("".equals(_sTaxId)) //$NON-NLS-1$
      return false;
    if (!_sTaxId.matches("\\d{8}")) //$NON-NLS-1$
      return false;
    boolean isOK = false;
    long wk1, wk2, wk3, wk4, wk5, wk6, wk7, wk8, wk9, wk10, wk_total, wk_check;
    wk1 = Long.valueOf(_sTaxId.substring(0, 1))
      + Long.valueOf(_sTaxId.substring(2, 3))
      + Long.valueOf(_sTaxId.substring(4, 5))
      + Long.valueOf(_sTaxId.substring(7));

    int i2 = Integer.valueOf(_sTaxId.substring(1, 2));
    int i4 = Integer.valueOf(_sTaxId.substring(3, 4));
    int i6 = Integer.valueOf(_sTaxId.substring(5, 6));
    int i7 = Integer.valueOf(_sTaxId.substring(6, 7));

    wk2 = i2 * 2 / 10;
    wk3 = i4 * 2 / 10;
    wk4 = i6 * 2 / 10;
    wk5 = i7 * 4 / 10;

    wk6 = i2 * 2 % 10;
    wk7 = i4 * 2 % 10;
    wk8 = i6 * 2 % 10;
    wk9 = i7 * 4 % 10;

    wk10 = (wk5 + wk9) / 10;
    wk_total = wk1 + wk2 + wk3 + wk4 + wk5 + wk6 + wk7 + wk8 + wk9;
    wk_check = wk_total % 10;
    if (wk_check == 0) {
      isOK = true;
    } else if (i7 == 7) {
      wk_total = wk1 + wk2 + wk3 + wk4 + wk6 + wk7 + wk8 + wk10;
      wk_check = wk_total % 10;
      isOK = (wk_check == 0);
    }
    return isOK;
  }

  public static boolean checkCarrier(String _sCarrId) {
    return _sCarrId.matches("[A-Za-z]{2}\\d{14}") //$NON-NLS-1$
      || _sCarrId.matches("/[a-zA-Z0-9-+.]{7}");
  }

  public static String getCT_NO(String _sCarrId) {
    int n = 0;
    if (_sCarrId.matches("[A-Za-z]{2}\\d{14}"))
      return "CQ0001";
    if (_sCarrId.matches("/[a-zA-Z0-9-+.]{7}"))
      return "3J0002";
    return "";

  }

  public static boolean checkDonate(String _sDonatId) {
    boolean temp = _sDonatId.matches("\\d{3,7}");
    return _sDonatId.matches("\\d{3,7}");
  }

  public static String DateTrans(String date) {
    String finalDate = "";
    String year = date.substring(0, 4);
    finalDate = Integer.toString(Integer.valueOf(year) - 1911) + date.substring(4);
    return finalDate;
  }

  public static String DateFormat(String date) {
    String enc = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
    return enc;
  }

  public static String TimeFormat(String time) {
    String enc = time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4);
    return enc;
  }

  public static String getMonthFormat(String date) {
    String year = date.substring(0, 4);
    String month = date.substring(4, 6);
    String yearT = Integer.toString(Integer.valueOf(year) - 1911);
    String monthT;
    switch (month) {
      case "01":
        monthT = "01-02";
        break;
      case "02":
        monthT = "01-02";
        break;
      case "03":
        monthT = "03-04";
        break;
      case "04":
        monthT = "03-04";
        break;
      case "05":
        monthT = "05-06";
        break;
      case "06":
        monthT = "05-06";
        break;
      case "07":
        monthT = "07-08";
        break;
      case "08":
        monthT = "07-08";
        break;
      case "09":
        monthT = "09-10";
        break;
      case "10":
        monthT = "09-10";
        break;
      case "11":
        monthT = "11-12";
        break;
      case "12":
        monthT = "11-12";
        break;
      default:
        monthT = "00-00";
        break;

    }
    return yearT + "年" + monthT + "月";
  }

  public static String getMonthFormatBAR(String date) {
    String year = date.substring(0, 4);
    String month = date.substring(4, 6);
    String yearT = Integer.toString(Integer.valueOf(year) - 1911);
    String monthT;
    switch (month) {
      case "01":
      case "02":
        monthT = "02";
        break;
      case "03":
      case "04":
        monthT = "04";
        break;
      case "05":
      case "06":
        monthT = "06";
        break;
      case "07":
      case "08":
        monthT = "08";
        break;
      case "09":
      case "10":
        monthT = "10";
        break;
      case "11":
      case "12":
        monthT = "12";
        break;
      default:
        monthT = "00";
        break;

    }
    return yearT + monthT;
  }

  public static String[] getEinvMonth() {
    String[] date = new String[2];
    String today = emisUtil.todayDateAD();
    String year = today.substring(0, 4);
    String month = today.substring(4, 6);
    switch (month) {
      case "01":
      case "02":
        date[0] = year + "01";
        date[1] = year + "02";
        break;
      case "03":
      case "04":
        date[0] = year + "03";
        date[1] = year + "04";
        break;
      case "05":
      case "06":
        date[0] = year + "05";
        date[1] = year + "06";
        break;
      case "07":
      case "08":
        date[0] = year + "07";
        date[1] = year + "08";
        break;
      case "09":
      case "10":
        date[0] = year + "09";
        date[1] = year + "10";
        break;
      case "11":
      case "12":
        date[0] = year + "11";
        date[1] = year + "12";
        break;
    }

    return date;
  }

  public static String[] getEinvMonth(String sl_date) {
    String[] date = new String[2];
    String year = sl_date.substring(0, 3);
    String month = sl_date.substring(3, 5);
    switch (month) {
      case "01":
      case "02":
        date[0] = year + "01";
        date[1] = year + "02";
        break;
      case "03":
      case "04":
        date[0] = year + "03";
        date[1] = year + "04";
        break;
      case "05":
      case "06":
        date[0] = year + "05";
        date[1] = year + "06";
        break;
      case "07":
      case "08":
        date[0] = year + "07";
        date[1] = year + "08";
        break;
      case "09":
      case "10":
        date[0] = year + "09";
        date[1] = year + "10";
        break;
      case "11":
      case "12":
        date[0] = year + "11";
        date[1] = year + "12";
        break;
    }
    return date;
  }

}
