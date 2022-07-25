package com.emis.venus.util;

import java.util.Calendar;
import java.util.Vector;

/**
 * 包含一些和中文相關的函式 Class
 */
public class emisChinese {

  private emisChinese() {
  }

  private static final String[] NumList =
    {
      "０１２３４５６７８９",
      "○一二三四五六七八九",
      "零壹貳參肆伍陸柒捌玖",
      "○一二三四五六七八九",
      "零壹貳參肆伍陸柒捌玖"
    };


  /**
   * Transfer flag used by NumToChinese<BR>
   * <p>
   * transfer number to "１２３４５６７８９０"
   */
  public static final int NUM_TRANS_FULL_STYLE = 0;


  /**
   * Transfer flag used by NumToChinese<BR>
   * <p>
   * transfer number to "一二三四五六七八九○"
   */
  public static final int NUM_TRANS_SIMPLE_CHINESE = 1;


  /**
   * Transfer flag used by NumToChinese<BR>
   * <p>
   * transfer number to "壹貳參肆伍陸柒捌玖零"
   */
  public static final int NUM_TRANS_TRADITION_CHINESE = 2;


  /**
   * Transfer flag used by NumToChinese<BR>
   * <p>
   * Ex:transfer number 123 to "一百二十三";
   */
  public static final int NUM_TRANS_SIMPLE_CHINESE_EX = 3;


  /**
   * Transfer flag used by NumToChinese<BR>
   * <p>
   * Ex:transfer number 123 to "壹佰貳拾參";
   */
  public static final int NUM_TRANS_TRADITION_CHINESE_EX = 4;


  /**
   * 轉換文數字混合之字串函數<BR>
   * <p>
   * 支援負數轉換<BR>
   *
   * @param _str - Ex:"中文123也可以"<BR>
   * @param transfer_flag - <BR>
   *               <p>
   *               NUM_TRANS_FULL_STYLE (Ex:"中文１２３也可以")<BR>
   *               <p>
   *               NUM_TRANS_SIMPLE_CHINESE (Ex:"中文一二三也可以")<BR>
   *               <p>
   *               NUM_TRANS_TRADITION_CHINESE (Ex:"中文壹貳參也可以")<BR>
   *               <p>
   *               NUM_TRANS_SIMPLE_CHINESE_EX (Ex:"中文一百二十三也可以")<BR>
   *               <p>
   *               NUM_TRANS_TRADITION_CHINESE_EX (Ex:"中文壹佰貳拾參也可以")<BR>
   */
  public static String MixStrToChina(String _str, int transfer_flag) {
    StringBuffer _sBuf = new StringBuffer();

    int len = _str.length();
    int encount = 0;
    boolean negative = false;

    for (int idx = 0; idx < len; idx++) {
      negative = false;

      if (_str.charAt(idx) == '-') // maybe it is negative ,or not
      {
        negative = true;
        idx++;

        if (idx == len) {
          _sBuf.append('-');    // '-'在字串的尾端，不處裡
          return _sBuf.toString();
        }
      }

      if (Character.isDigit(_str.charAt(idx))) {
        encount = idx++;    // encount == 數字字串的第一個pos

        while (idx < len) {
          if (Character.isDigit(_str.charAt(idx)))
            idx++;
          else
            break;
        }
        --idx;

        String _tmp = _str.substring(encount, idx + 1);  // _tmp==整個數字字串
        int _iNum = Integer.parseInt(_tmp);

        if (negative)
          _iNum = -_iNum;

        _tmp = NumToChinese(_iNum, transfer_flag);
        _sBuf.append(_tmp);

        continue;
      }
      _sBuf.append(_str.charAt(idx));
    }
    return _sBuf.toString();
  }


  /**
   * 將數字字串轉換成中文字串<BR>
   * <p>
   * 支援負數<BR>
   *
   * @param iNum          - A digital string prepared to transfered to string<BR>
   * @param transfer_flag - <BR>
   *                      <p>
   *                      NUM_TRANS_FULL_STYLE<BR>
   *                      <p>
   *                      NUM_TRANS_SIMPLE_CHINESE<BR>
   *                      <p>
   *                      NUM_TRANS_TRADITION_CHINESE<BR>
   *                      <p>
   *                      NUM_TRANS_SIMPLE_CHINESE_EX<BR>
   *                      <p>
   *                      NUM_TRANS_TRADITION_CHINESE_EX<BR>
   */
  public static String NumToChinese(String iNum, int transfer_flag) {
    if (iNum == null) return "";

    if (iNum.length() == 0) return "";

    int zerocount = 0;
    boolean negavite = false;

    if (iNum.charAt(0) == '-')  // 處裡負數
    {
      if (iNum.length() > 1) {
        iNum = iNum.substring(1);
      } else {
        return "";
      }
      negavite = true;
    }

    for (int i = 0; i < iNum.length(); i++) {
      char c = iNum.charAt(i);
      if ((c != '0') && (c != '-'))
        break;

      if (c != '-')   // c == '0'
        zerocount++;

      if (i == (iNum.length() - 1))  // c=='0' or '-' && 最後一個字元
        zerocount = 0;
    }

    String ret = NumToChinese(Integer.parseInt(iNum), transfer_flag);
    if (zerocount == 0) {

      if (negavite)
        return "負" + ret;

      return ret;
    }

    String append = "";

    if (transfer_flag <= NUM_TRANS_TRADITION_CHINESE_EX) {
      String dup = NumList[transfer_flag].substring(0, 1);
      append = duplicate(dup, zerocount);
    }

    if (negavite)
      return "負" + append + ret;

    return append + ret;
  }

  /**
   * 將數字轉換成中文字串<BR>
   *
   * @param iNum          - An Integer Number prepared to transfered to string<BR>
   * @param transfer_flag - <BR>
   *                      <p>
   *                      NUM_TRANS_FULL_STYLE<BR>
   *                      <p>
   *                      NUM_TRANS_SIMPLE_CHINESE<BR>
   *                      <p>
   *                      NUM_TRANS_TRADITION_CHINESE<BR>
   *                      <p>
   *                      NUM_TRANS_SIMPLE_CHINESE_EX<BR>
   *                      <p>
   *                      NUM_TRANS_TRADITION_CHINESE_EX<BR>
   */
  public static String NumToChinese(int iNum, int transfer_flag) {
    String _Chinese;

    switch (transfer_flag) {
      case NUM_TRANS_FULL_STYLE:
        _Chinese = _NumToChineseFullStyle(iNum);
        break;
      case NUM_TRANS_SIMPLE_CHINESE:
        _Chinese = _NumToChineseSimpleStyle(iNum);
        break;
      case NUM_TRANS_TRADITION_CHINESE:
        _Chinese = _NumToChineseTraditionStyle(iNum);
        break;
      case NUM_TRANS_SIMPLE_CHINESE_EX:
        _Chinese = _NumToChineseSimpleStyleEx(iNum);
        break;
      case NUM_TRANS_TRADITION_CHINESE_EX:
        _Chinese = _NumToChineseTraditionStyleEx(iNum);
        break;
      default:
        _Chinese = _NumToChineseFullStyle(iNum);
    }
    return _Chinese;
  }

  private static String _NumToChineseFullStyle(int _iNum) {
    return _NumToChineseSharedTransfer(_iNum, "０１２３４５６７８９");
  }

  private static String _NumToChineseSimpleStyle(int _iNum) {
    return _NumToChineseSharedTransfer(_iNum, "○一二三四五六七八九");
  }

  private static String _NumToChineseTraditionStyle(int _iNum) {
    return _NumToChineseSharedTransfer(_iNum, "零壹貳參肆伍陸柒捌玖");
  }


  /*
   *  將數字轉換成中文字串
   */
  private static String _NumToChineseSharedTransfer(int _iNum, String _pattern) {
    char[] buf = new char[12];
    boolean negative = (_iNum < 0);
    int charPos = 12;

    if (_iNum == Integer.MIN_VALUE) {

      if (_pattern.charAt(0) == '０')
        return "-２１４７４８３６４８";
      else if (_pattern.charAt(0) == '○')
        return "負二一四七四八三六四八";
      else if (_pattern.charAt(0) == '零')
        return "\u8ca0\u8cb3\u58f9\u8086\u67d2\u8086\u634c\u53c3\u9678\u8086\u634c";
      else return "";

    }

    if (negative) {
      _iNum = -_iNum;
    }

    do {
      int digit = _iNum % 10;
      buf[--charPos] = _pattern.charAt(digit);
      _iNum = _iNum / 10;
    } while (_iNum != 0);

    if (negative) {
      if (_pattern.charAt(0) == '０')
        buf[--charPos] = '-';
      else
        buf[--charPos] = '負';
    }

    return new String(buf, charPos, (12 - charPos));
  }

  private static String _NumToChineseSimpleStyleEx(int _iNum) {
    return _NumToChineseSharedTransferEx(_iNum, "○一二三四五六七八九", "十百千");
  }

  private static String _NumToChineseTraditionStyleEx(int _iNum) {
    return _NumToChineseSharedTransferEx(_iNum, "零壹貳參肆伍陸柒捌玖", "拾佰仟");
  }


  /*
   *  將數字轉換成中文字串(自動加入'佰''仟'等進位文字)<BR>
   *  做法: 先用 sys 呼叫把 123 轉成 "123"<BR>
   *  再從 1 開始往 3 做轉換
   */
  private static String _NumToChineseSharedTransferEx(int _iNum, String _pattern, String _degree) {
    boolean negative = (_iNum < 0);

    if (_iNum == Integer.MIN_VALUE) {

      if (_pattern.charAt(0) == '○')
        return "負二兆一億四千七百四十八萬三千六百四十八";

      return "負貳兆壹億肆仟柒佰肆拾捌萬參仟陸佰肆拾捌";
    }

    if (_iNum == 0) {
      return new String(_pattern.substring(0, 1));
    }

    StringBuffer _sBuf = new StringBuffer();

    if (negative) {
      _sBuf.append("負");
      _iNum = -_iNum;
    }

    int _iTmp = 0;

    if (_iNum > 99999999) {
      _sBuf.append(_NumToChineseSharedExDivider(_iNum / 100000000, _pattern, _degree));
      _sBuf.append('億');
      _iNum = _iNum % 100000000;
    }

    if (_iNum > 9999) {
      _iTmp = _iNum / 10000;

      if ((_iTmp < 1000) & (_sBuf.length() > 0)) // 小於 1000
        _sBuf.append('零');

      _sBuf.append(_NumToChineseSharedExDivider(_iTmp, _pattern, _degree));
      _sBuf.append('萬');

      _iNum = _iNum % 10000;
    }

    if (_iNum > 0) {

      if ((_iTmp < 1000) & (_sBuf.length() > 0)) // 小於 1000
        _sBuf.append('零');

      _sBuf.append(_NumToChineseSharedExDivider(_iNum, _pattern, _degree));
    }

    return _sBuf.toString();
  }

  private static String _NumToChineseSharedExDivider(int _iNum, String _pattern, String _degree) {
    String _tmp = String.valueOf(_iNum);
    StringBuffer _sBuf = new StringBuffer();

    if (_iNum == 10) return _degree.substring(0, 1);

    if ((_iNum > 10) & (_iNum < 20)) {
      _sBuf.append(_degree.charAt(0));
      _sBuf.append(_pattern.charAt(_iNum % 10));
      return _sBuf.toString();
    }

    boolean encount_zero = false;
    int _strLength = _tmp.length();

    if (_strLength > 4) return ""; // it must be error

    for (int i = 0; i < _strLength; i++) {
      int digit = (_tmp.charAt(i) - '0');

      if (digit == 0) {
        encount_zero = true;
      } else {
        if (encount_zero)
          _sBuf.append("零"); // \u96f6

        encount_zero = false;
        _sBuf.append(_pattern.charAt(digit));

        if (i == _strLength - 1) break;

        _sBuf.append(_degree.charAt(_strLength - i - 2));
      }
    }
    return _sBuf.toString();
  }

  public static final int TIME_TRANS_SIMPLE_TYPE = 1;
  public static final int TIME_TRANS_SLASH_TYPE = 2;
  public static final int TIME_TRANS_SIMPLE_CHINESE = 3;
  public static final int TIME_TRANS_TRADITION_CHINESE = 4;

  /**
   * 轉換日期成為中文字串
   *
   * @param oDate      java.util.Date, you can put <BR>
   *                   <p>
   *                   java.sql.Timestamp ,too.<BR>
   *                   <p>
   *                   you can know current date by <BR>
   *
   *                   <H5>emisChinese.DateToString( new Date(), emisChinese.TIME_TRANS_SLASH_TYPE); </H5>
   * @param trans_flag - (see below)
   *                   <p>
   *                   Ex:"0881102"         ,flag == TIME_TRANS_SIMPLE_TYPE<BR>
   *                   <p>
   *                   "088/11/02"       ,flag == TIME_TRANS_SLASH_TYPE<BR>
   *                   <p>
   *                   "八十八年十一月二日",flag == TIME_TRANS_SIMPLE_CHINESE<BR>
   *                   <p>
   *                   "捌拾捌年拾壹月二日",flag == TIME_TRANS_TRADITION_CHINESE
   */
  public static String dateToString(java.util.Date oDate, int trans_flag) {
    Calendar _oCalendar = emisUtil.getLocaleCalendar();

    int year;
    int month;
    int date;

    _oCalendar.setTime(oDate);

    year = _oCalendar.get(Calendar.YEAR) - 1911;
    month = _oCalendar.get(Calendar.MONTH) + 1;  // month is based by 0, 0 means January
    date = _oCalendar.get(Calendar.DATE);

    switch (trans_flag) {
      case TIME_TRANS_SIMPLE_TYPE:
        return _DateToStringSimpleType(year, month, date);
      case TIME_TRANS_SLASH_TYPE:
        return _DateToStringSlashType(year, month, date);
      case TIME_TRANS_SIMPLE_CHINESE:
        return _DateToStringSimpleChinese(year, month, date);
      case TIME_TRANS_TRADITION_CHINESE:
        return _DateToStringTraditionChinese(year, month, date);
      default:
        return _DateToStringSimpleType(year, month, date);
    }
  }

  private static String _DateToStringSimpleType(int _year, int _month, int _date) {
    StringBuffer _sBuf = new StringBuffer();

    String year = String.valueOf(_year);
    if (year.length() < 3) {
      year = duplicate("0", (3 - year.length())) + year;
    }
    _sBuf.append(year);

    if (_month < 10)  // only one digits
      _sBuf.append('0');

    _sBuf.append(String.valueOf(_month));

    if (_date < 10)  // only one digits
      _sBuf.append('0');

    _sBuf.append(String.valueOf(_date));

    return _sBuf.toString();
  }

  private static String _DateToStringSlashType(int _year, int _month, int _date) {
    StringBuffer _sBuf = new StringBuffer();

    String year = String.valueOf(_year);
    if (year.length() < 3) {
      year = duplicate("0", (3 - year.length())) + year;
    }
    _sBuf.append(year);
    _sBuf.append("/");

    if (_month < 10)  // only one digits
      _sBuf.append('0');

    _sBuf.append(String.valueOf(_month));
    _sBuf.append("/");

    if (_date < 10)  // only one digits
      _sBuf.append('0');

    _sBuf.append(String.valueOf(_date));

    return _sBuf.toString();
  }

  private static String _DateToStringSimpleChinese(int _year, int _month, int _date) {
    StringBuffer _sBuf = new StringBuffer();
    _sBuf.append(NumToChinese(_year, NUM_TRANS_SIMPLE_CHINESE_EX));
    _sBuf.append("年");
    _sBuf.append(NumToChinese(_month, NUM_TRANS_SIMPLE_CHINESE_EX));
    _sBuf.append("月");
    _sBuf.append(NumToChinese(_date, NUM_TRANS_SIMPLE_CHINESE_EX));
    _sBuf.append("日");

    return _sBuf.toString();
  }

  private static String _DateToStringTraditionChinese(int _year, int _month, int _date) {
    StringBuffer _sBuf = new StringBuffer();
    _sBuf.append(NumToChinese(_year, NUM_TRANS_TRADITION_CHINESE_EX));
    _sBuf.append("年");
    _sBuf.append(NumToChinese(_month, NUM_TRANS_TRADITION_CHINESE_EX));
    _sBuf.append("月");
    _sBuf.append(NumToChinese(_date, NUM_TRANS_TRADITION_CHINESE_EX));
    _sBuf.append("日");

    return _sBuf.toString();
  }

  /**
   * 產生時間字串
   * <p>
   * 由 "14:35" 產生 <BR>
   * <p>
   * 十四時三十五分<BR>
   * <p>
   * 由 "14:35:21" 產生 <BR>
   * <p>
   * 十四時三十五分二十一秒<BR>
   * <p>
   * 的字串 <BR>
   */
  public static String toCTimeStr(String _timeStr) {
    String _TIME_DIVIDE = "時分秒";

    StringBuffer _sBuf = new StringBuffer();

    int idx = 0;
    int separator = 0;

    for (idx = 0; idx < _timeStr.length(); idx++) {

      if (_timeStr.charAt(idx) == ':') // the separator;
      {
        if (separator > 2) return _sBuf.toString();

        _sBuf.append(_TIME_DIVIDE.charAt(separator++));

        continue;
      }

      if (idx == _timeStr.length() - 1)  // 剛好到底
      {
        _toCTimeStr(_sBuf, 0, _timeStr.charAt(idx) - '0');

        if (separator < 3)
          _sBuf.append(_TIME_DIVIDE.charAt(separator++));

        return _sBuf.toString();
      } else {
        if (_timeStr.charAt(idx + 1) == ':') {
          _toCTimeStr(_sBuf, 0, _timeStr.charAt(idx) - '0');
        } else {
          _toCTimeStr(_sBuf, _timeStr.charAt(idx) - '0', _timeStr.charAt(idx + 1) - '0');
          ++idx;
        }
      }
    }

    if (separator < 3)
      _sBuf.append(_TIME_DIVIDE.charAt(separator));

    return _sBuf.toString();
  }

  /*
   *  Since time only has two digits
   */
  private static void _toCTimeStr(StringBuffer _sBuf, int _ten, int _dec) {
    String pattern = "○一二三四五六七八九";

    if (_ten >= 10 || _dec >= 10) return; // too big , error , it must be input data error

    int total = _ten * 10 + _dec;
    if (total == 0) {
      _sBuf.append(pattern.charAt(0));
      return;
    }

    if ((total % 10) == 0) {

      if (_ten != 1)
        _sBuf.append(pattern.charAt(_ten));

      _sBuf.append("十");
      return;
    }

    if (total < 10) {
      _sBuf.append(pattern.charAt(total));
      return;
    }

    if (total > 19) {
      _sBuf.append(pattern.charAt(_ten));
    }

    _sBuf.append("十");
    _sBuf.append(pattern.charAt(_dec));
  }

  /**
   * 檢查一個 char 是否為中文 char
   */
  public static boolean isChinaChr(char c)    // 不懂，待問
  {
    try {
      char[] cb = new char[1];
      cb[0] = c;
      String s = new String(cb);
      byte[] bb = s.getBytes(emisUtil.FILENCODING);

      int nByte = (int) (bb[0] & 0xFF);
      if (nByte > 128)
        return true;

      return false;
    } catch (Exception e) {
      return true;
    }
  }

  /**
   * 將 str 切成 Len 長度的 Vector(可切割中文)<BR>
   *
   * @param str<BR>
   * @param Len     - 所希望切割的長度(Byte 為單位),<BR>
   *                <p>
   *                不可小於 2(此函數是切中文字串),否則直接傳回 null<BR>
   *                <p>
   *                Ex:Vector V = split ("中啦A文B啦C", 3);<BR>
   *                <p>
   *                V.elemetAt(0) = "中";<BR>
   *                <p>
   *                V.elemetAt(1) = "拉A";<BR>
   *                <p>
   *                V.elemetAt(2) = "文B";<BR>
   *                <p>
   *                V.elemetAt(3) = "啦C";<BR>
   */
  public static Vector split(String str, int Len) {
    if (str == null || Len <= 1) return null;

    char[] array = str.toCharArray();
    Vector V = new Vector();
    int totalbyte = 0;             // 計數每個分割的字元數量
    int lasti = 0;
    int i = 0;

    StringBuffer buf = new StringBuffer();

    while (i < array.length) {
      char c = array[i];
      int val = (int) c;

      if (emisChinese.isChinaChr(c)) // it is chinese char
      {
        if ((totalbyte + 2) > Len) {
          if ((totalbyte + 1) == Len) {
            buf.append(' ');
          }

          totalbyte = 0;
          String tmp = buf.toString();
          V.addElement(tmp);
          buf.setLength(0);
          continue;
        } else {
          totalbyte = totalbyte + 2;
          buf.append(c);
        }
      } else {
        if (totalbyte + 1 > Len) {
          totalbyte = 0;
          String tmp = buf.toString();
          V.addElement(tmp);
          buf.setLength(0);
          continue;
        } else {
          totalbyte++;
          buf.append(c);
        }
      }
      i++;
    }

    if (buf.length() != 0) {
      String pad = buf.toString();
      try {
        pad = rpad(pad, " ", Len);
        V.addElement(pad);
      } catch (Exception ignore) {
      }
    } else {
      lpadLastLine(V, Len);
    }
    return V;
  }


  /**
   * 將 str 切成 Len 長度的 Vector(可切割中文)<BR>
   *
   * @param str<BR>
   * @param Len     - 所希望切割的長度(Byte 為單位),<BR>
   *                <p>
   *                不可小於 2(此函數是切中文字串),否則直接傳回 null<BR>
   * @param lines   - 希望到第幾行就停止<BR>
   *                <p>
   *                Ex:Vector V = split ("中啦A文B啦C", 3, 2);<BR>
   *                <p>
   *                V.elemetAt(0) = "中";<BR>
   *                <p>
   *                V.elemetAt(1) = "拉A";<BR>
   */
  public static Vector split(String str, int Len, int lines) {
    if (str == null || Len <= 1) return null;

    char[] array = str.toCharArray();
    Vector V = new Vector();
    int totalbyte = 0;             // 計數每個分割的字元數量
    int lasti = 0;
    int i = 0;
    int lineCounter = 0;

    StringBuffer buf = new StringBuffer();

    while (i < array.length) {
      char c = array[i];
      int val = (int) c;

      if (isChinaChr(c)) // it is chinese char
      {
        if ((totalbyte + 2) > Len) {
          if ((totalbyte + 1) == Len) {
            buf.append(' ');
          }

          totalbyte = 0;
          String tmp = buf.toString();
          V.addElement(tmp);
          lineCounter++;

          if (lineCounter >= lines)  // 切割停止
          {
            return V;
          }

          buf.setLength(0);
          continue;
        } else {
          totalbyte = totalbyte + 2;
          buf.append(c);
        }
      } else {
        if (totalbyte + 1 > Len) {
          totalbyte = 0;
          String tmp = buf.toString();
          V.addElement(tmp);
          lineCounter++;

          if (lineCounter >= lines)  // 切割停止
          {
            return V;
          }

          buf.setLength(0);
          continue;
        } else {
          totalbyte++;
          buf.append(c);
        }
      }
      i++;
    } // end of while

    if (lineCounter >= lines)  // 切割停止
    {
      return V;
    }

    if (buf.length() != 0) {
      String pad = buf.toString();
      try {
        pad = rpad(pad, " ", Len);
        V.addElement(pad);
      } catch (Exception ignore) {
      }
    } else {
      lpadLastLine(V, Len);
    }
    return V;
  }

  /*
   *  將Vector V的最後一個element填補空白至len的長度<BR>
   *  Used by split function
   */
  private static void lpadLastLine(Vector V, int len) {
    if (V == null) return;

    int last = V.size() - 1;

    if (last < 0) return;

    String pad = (String) V.elementAt(last);
    try {
      pad = rpad(pad, " ", len);
      V.setElementAt(pad, last);
    } catch (Exception ignore) {
    }
  }

  /**
   * 將dNumber轉成字串，並在右邊填滿空白直到長度 = len
   */
  public static String rpad(double dNumber, int len) {
    return rpad(String.valueOf(dNumber), " ", len);
  }

  /**
   * 在 oStr 的右邊填滿 cPad 直到 oStr 的長度 = len
   * <p>
   * cPad 必需是長度為 1 的 String
   */
  public static String rpad(String oStr, String cPad, int len) {
    if (oStr == null) return duplicate(cPad, len);

    if (len <= 0) return oStr;

    int _nStrLen = getCharSpace(oStr);
    if (len <= _nStrLen) return oStr;

    return oStr + duplicate(cPad, len - _nStrLen);
  }

  /**
   * 在 oStr 右邊填滿空白直到長度 = len
   */
  public static String rpad(String oStr, int len) {
    return rpad(oStr, " ", len);
  }

  /**
   * 將dNumber轉成字串，並在左邊填滿空白直到長度 = len
   */
  public static String lpad(double dNumber, int len) {
    return lpad(String.valueOf(dNumber), " ", len);
  }

  /**
   * 在 oStr 的左邊填滿 cPad 直到 oStr 的長度 = len
   * <p>
   * cPad 必需是長度為 1 的 String
   */
  public static String lpad(String oStr, String cPad, int len) {
    if (oStr == null) return duplicate(cPad, len);

    if (len <= 0) return oStr;

    int _nStrLen = getCharSpace(oStr);
    if (len <= _nStrLen) return oStr;

    return duplicate(cPad, len - _nStrLen) + oStr;
  }

  /**
   * 在 oStr 左邊填滿空白直到長度 = len
   */
  public static String lpad(String oStr, int len) {
    return lpad(oStr, " ", len);
  }


  /**
   * 計算 oStr 之字串空間<BR>
   * <p>
   * PS:一個中文字佔據兩個英文字的空間
   */
  public static int getCharSpace(String oStr) {
    int _nLen = 0;
    if (oStr == null) return _nLen;

    try {
      byte[] abyte = oStr.getBytes(emisUtil.FILENCODING);
      return abyte.length;
    } catch (Exception ignore) {
      return _nLen;
    }
  }

  /**
   * 將 oStr duplicate N 次 , 並回傳
   */
  public static String duplicate(String oStr, int nDup) {
    if (oStr == null) return null;
    if (oStr.length() == 0) return "";

    if (nDup > 0) {
      int _nInitSize = oStr.length() * nDup;
      StringBuffer buf = new StringBuffer(_nInitSize);
      for (int i = 0; i < nDup; i++) {
        buf.append(oStr);
      }

      return buf.toString();
    }
    return ""; // duplicate 0 times will be empty string
  }

  /**
   * 傳回以中文字長度為 2 的字串長度
   * <p>
   * Ex: "中文ABC" 傳回 7
   */
  public static int clen(String sStr) {
    if (sStr == null) return 0;

    try {
      byte[] b = sStr.getBytes("UTF-8");
      return b.length;
    } catch (Exception ignore) {
      return sStr.length();
    }
  }
}
