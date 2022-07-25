package com.emis.venus.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import com.emis.venus.util.log4j.LogKit;
import com.emis.venus.util.log4j.emisLogger;
import com.emis.venus.util.string.FormatFld;

/**
 * Title: emisStringCharsetName Description: 字串处理 需传入编码格式
 */
final public class emisStringCharsetName {

  private String sStr_ = "";

  private String charsetName = "UTF-8";

  public emisStringCharsetName(String s, String charsetName) {
    this.sStr_ = s;
    this.charsetName = charsetName;
  }

  public String subStringB(int beginIndex, int length) {
    return subStringB(sStr_, beginIndex, length, charsetName);
  }

  public String subStringB(int beginIndex) {
    return subStringB(sStr_, beginIndex, charsetName);
  }

  public String leftB(int length) {
    return leftB(sStr_, length, charsetName);
  }

  public String rightB(int length) {
    return rightB(sStr_, length, charsetName);
  }

  public String rTrim() {
    return rTrim(sStr_);
  }

  public String lTrim() {
    return lTrim(sStr_);
  }

  public String trim() {
    return trim(sStr_);
  }

  public int lengthB() {
    return lengthB(sStr_, charsetName);
  }

  public String lPadB(int length) {
    return lPadB(sStr_, length, ' ', this.charsetName);
  }

  /* subString() 以 byte 数取子字串 传入: String s => 来源字串 int beginIndex => 开始位元组位置 int
   * length => 欲取位元组长度 传回: String */
  public static String subStringB(String s, int beginIndex, int length, String charsetName) {
    return subStringB(s, beginIndex, length, charsetName, true);
  }

  /**
   * subString() 以 byte 数取子字串
   *
   * @param s           来源字串
   * @param beginIndex  开始位元组位置 int
   * @param length      欲取位元组长度
   * @param charsetName
   * @param isFillSpace 已达欲取长度, 最后为中文字的前半, 该字元不取, 是否要添加一空格满足返回length长度字串
   * @return
   */
  public static String subStringB(String s, int beginIndex, int length, String charsetName,
                                  boolean isFillSpace) {
    if (s == null)
      s = "";
    String _sRet = ""; // 传回字串
    int _iByteOff = 0; // 以 byte 计算的累进位置
    int _iNumBytes = 0; // 取得 byte 数
    if (charsetName == null || "".equals(charsetName.trim())) {
      charsetName = "GBK";
    }
    try {
      //LogKit.info(s+"进行截取,从"+beginIndex+"长度:"+length);
      StringBuffer sb = new StringBuffer(length);
      // 中文字元
      int len = "一".getBytes(charsetName).length;
      for (int i = 0; i < s.length(); i++) {

        // 判断字元是否为中文
        if (Character.UnicodeBlock.of(s.charAt(i)) != Character.UnicodeBlock.BASIC_LATIN) {
          if (_iByteOff == (beginIndex - 1)) {
            // 第一byte为中文字后半开始, 该字元需要取出来
            sb.append(s.charAt(i));
            _iNumBytes += len;// 中文字元 byte 位置累进 2
          } else if (_iByteOff >= beginIndex) {
            if (_iNumBytes + len > length) {
              // 已达欲取长度, 最后为中文字的前半, 该字元不取, 以 1byte 空白取代
              if (isFillSpace) {
                sb.append(" ");
              }
              _iNumBytes++;
            } else {
              sb.append(s.charAt(i));
              _iNumBytes += len;
            }
          }
          _iByteOff += len; // 中文字元 byte 位置累进 2
        } else {
          // 非中文字元
          if (_iByteOff >= beginIndex) {
            sb.append(s.charAt(i));
            _iNumBytes++;
          }
          _iByteOff++; // 非中文字元 byte 位置累进 1
        }

        if (_iNumBytes >= length)
          break; // 已达欲取长度, 结束回圈
      } // end for
      _sRet = sb.toString();
    } catch (Exception e) {
      LogKit.error(e);
    } finally {

    } // end try
    return _sRet;

  } // end subStringB()

  public static String subStringB(String s, int beginIndex, int length,
                                  boolean isGetFirstWord, String charsetName) {
    if (isGetFirstWord) {
      return emisStringCharsetName.subStringB(s, beginIndex, length,
        charsetName);
    } else {
      return emisStringCharsetName.subStringBE(s, beginIndex, length,
        charsetName);
    }
  }

  public static String subStringBE(String s, int beginIndex, int length,
                                   String charsetName) {
    if (s == null) {
      return "";
    }
    if (beginIndex < 0 || length < 0 || (length - beginIndex) < 0) {
      return "";
    }
    if (s == null)
      s = "";
    String _sRet = ""; // 传回字串
    int _iByteOff = 0; // 以 byte 计算的累进位置
    int _iNumBytes = 0; // 取得 byte 数

    try {
      StringBuffer sb = new StringBuffer(length);

      for (int i = 0; i < s.length(); i++) {

        // 判断字元是否为中文
        if (Character.UnicodeBlock.of(s.charAt(i)) != Character.UnicodeBlock.BASIC_LATIN) {
          // 中文字元
          int len = "一".getBytes(charsetName).length;
          if (_iByteOff == (beginIndex - 1)) {
            // 第一byte为中文字后半开始, 该字元不取, 以 1byte 空白取代
            sb.append(s.charAt(i));
            _iNumBytes += len;
            // _iNumBytes++;
          } else if (_iByteOff >= beginIndex) {
            if (_iNumBytes + len > length) {
              // 已达欲取长度, 最后为中文字的前半, 该字元不取, 以 1byte 空白取代
              sb.append(" ");
              _iNumBytes++;
            } else {
              sb.append(s.charAt(i));
              _iNumBytes += len;
            }
          }
          _iByteOff += len; // 中文字元 byte 位置累进 2
        } else {
          // 非中文字元
          if (_iByteOff >= beginIndex) {
            sb.append(s.charAt(i));
            _iNumBytes++;
          }
          _iByteOff++; // 非中文字元 byte 位置累进 1
        }

        if (_iNumBytes >= length)
          break; // 已达欲取长度, 结束回圈
      } // end for
      _sRet = sb.toString();
    } catch (Exception e) {
      LogKit.error(e);
    } finally {

    } // end try
    return _sRet;
  }

  public static String subStringB(String s, int beginIndex, String charsetName) {
    //2015/02/09 viva modify 第3个参数直接给字符长度，只是为了能取到最后
    return subStringB(s, beginIndex,
      (lengthB(s, charsetName)), charsetName);
  }

  public static String leftB(String s, int length, String charsetName) {
    return subStringB(s, 0, length, charsetName);
  }

  public static String rightB(String s, int length, String charsetName) {
    if (s == null)
      s = "";
    StringBuffer sb = new StringBuffer(s);
    String sTemp = subStringB(new String(sb.reverse()), 0, length,
      charsetName);
    sb = new StringBuffer(sTemp);
    return new String(sb.reverse());
  }

  public static String replicate(char c, int length) {
    StringBuffer sb = new StringBuffer(length);
    for (int i = 0; i < length; i++)
      sb.append(c);

    return sb.toString();
  }

  public static String replicate(String s, int length) {
    return replicate(s.charAt(0), length);
  }

  public static String space(int length) {
    return replicate(' ', length);
  }

  /**
   * 以传入长度来填左方字元.
   *
   * @param s
   * @param length
   * @return 填完后之字串
   */
  public static String lPadB(String s, int length, String charsetName) {
    return lPadB(s, length, ' ', charsetName);
  }

  /**
   * 以传入长度来填左方字元.
   *
   * @param s
   * @param length
   * @param cPad   ; 要填入的字元, 预设是空白
   * @return 填完后之字串
   */
  public static String lPadB(String s, int length, char cPad,
                             String charsetName) {
    if (s == null)
      s = "";
    String _sRet = "";
    int _n = length - lengthB(s, charsetName);

    if (_n <= 0) {
      _sRet = subStringB(s, 0, length, charsetName);
    } else {
      _sRet = replicate(cPad, _n) + s;
    }
    return _sRet;
  }

  public String rPadB(int length, String charsetName) {
    return rPadB(sStr_, length, ' ', charsetName);
  }

  /**
   * 以传入长度来填右方字元.
   *
   * @param s
   * @param length
   * @return 填完后之字串
   */
  public static String rPadB(String s, int length, String charsetName) {
    return rPadB(s, length, ' ', charsetName);
  }

  /**
   * 以传入长度来填右方字元.
   *
   * @param s
   * @param length
   * @param cPad   ; 要填入的字元, 预设是空白
   * @return 填完后之字串
   */
  public static String rPadB(String s, int length, char cPad, String charsetName) {
    if (s == null)
      s = "";
    String _sRet = "";
    int _n = length - lengthB(s, charsetName);

    if (_n <= 0) {
      _sRet = subStringB(s, 0, length, charsetName);
    } else {
      _sRet = s + replicate(cPad, _n);
    }
    return _sRet;
  }

  public String cPadB(int length) {
    return cPadB(sStr_, length, this.charsetName);
  }

  public static String cPadB(String s, int length, String charsetName) {
    return cPadB(s, length, ' ', charsetName);
  }

  /**
   * 以传入长度来填中间字元.
   *
   * @param s
   * @param length
   * @param cPad   ; 要填入的字元, 预设是空白
   * @return 填完后之字串
   */
  public static String cPadB(String s, int length, char cPad, String charsetName) {
    if (s == null)
      s = "";
    String _sRet = "";
    int _n = length - lengthB(s, charsetName);

    if (_n <= 0) {
      _sRet = subStringB(s, 0, length, charsetName);
    } else {
      int _mod = _n % 2;
      _n = (int) _n / 2;
      _sRet = replicate(cPad, _n) + s + replicate(cPad, _n)
        + ((_mod == 1) ? " " : "");
    }
    return _sRet;
  }

  public static String lTrim(String s) {
    if (s == null)
      s = "";
    String _sRet = "";
    int _iCharOff = 0;

    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) != ' ' && s.charAt(i) != '　'
        && s.charAt(i) != '\012' && s.charAt(i) != '\015'
        && s.charAt(i) != '\011')
        break;
      _iCharOff++;
    }
    _sRet = s.substring(_iCharOff);
    return _sRet;
  }

  public static String rTrim(String s) {
    if (s == null)
      s = "";
    String _sRet = "";
    // String _sSpace = " 　";
    int _iCharOff = s.length();

    for (int i = s.length() - 1; i >= 0; i--) {
      if (s.charAt(i) != ' ' && s.charAt(i) != '　'
        && s.charAt(i) != '\012' && s.charAt(i) != '\015'
        && s.charAt(i) != '\011')
        break;
      _iCharOff--;
    }
    _sRet = s.substring(0, _iCharOff);
    return _sRet;
  }

  public static String trim(String s) {
    return lTrim(rTrim(s));
  }

  public static int lengthB(String s, String charsetName) {
    try {
      if (charsetName == null || "".equals(charsetName.trim())) {
        charsetName = "GBK";
      }
      return s.getBytes(charsetName).length;
    } catch (UnsupportedEncodingException e) {
      LogKit.error(e);
      return 0;
    }
  }

  public static String getStringByEnter(int length, String string) throws Exception {
    return getStringByEnter(length, string, "GBK");
  }

  public static String getStringByEnter(int length, String string, String charsetName) throws Exception {
    if (charsetName == null || "".equals(charsetName.trim())) {
      charsetName = "GBK";
    }
    for (int i = 1; i <= string.length(); i++) {
      if (string.substring(0, i).getBytes(charsetName).length > length) {
        return string.substring(0, i - 1) + "\n" +
          getStringByEnter(length, string.substring(i - 1));
      }
    }
    return string;
  }

  public static void main(String[] args) throws UnsupportedEncodingException {

    String strTest1 = "拉斯帕庄园里奥哈・克斯特拉伯干红（杯）";
    System.out.println(strTest1 + "\r\n-------------------------------------------------");
    try {
      System.out.println(getStringByEnter(7, strTest1));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("-------------------------------------------------");


//		String strTest="拉斯帕庄园里奥哈・克斯特拉伯干红（杯）";
//		System.out.println(newline(
//				strTest, 9, "  "));


    System.out.println("。字符的GBK长度" + ("。".getBytes("GBK").length));
    System.out.println("・字符的GBK长度" + ("・".getBytes("GBK").length));
    System.out.println("·字符的GBK长度" + ("·".getBytes("GBK").length));
    System.out.println(".字符的GBK长度" + (".".getBytes("GBK").length));
    System.out.println("√字符的GBK长度" + ("√".getBytes("GBK").length));
    System.out.println("×字符的GBK长度" + ("×".getBytes("GBK").length));

    String strTest2 = "拉斯帕庄园里奥哈・克斯特拉伯干红（杯）";
    System.out.println(strTest2 + "\r\n-------------------------------------------------");
    System.out.println(newline(
      strTest2, 9, "  "));
    System.out.println("-------------------------------------------------");


    String strTest3 = "拉斯帕庄园里奥哈。克斯特拉伯干红（杯）";
    System.out.println(strTest3 + "\r\n-------------------------------------------------");
    System.out.println(newline(
      strTest3, 10, "  "));
    System.out.println("-------------------------------------------------");

    String strTest4 = "拉斯帕庄园里奥哈.克斯特拉伯干红（杯）";
    System.out.println(strTest4 + "\r\n-------------------------------------------------");
    System.out.println(newline(
      strTest4, 10, "  "));
    System.out.println("-------------------------------------------------");
    if (true) {
      return;
    }

    System.out.println(newline(
      "台1湾2金3牌4牛5肉6米7粉*加辣8*套9餐0台1湾2金3牌4牛5肉6米7粉*加辣8*套9餐", 10, "  "));
    System.out.println("-------------------------------------------------");
    // 台1湾2金3牌4牛5肉6米7粉*加辣8*套9餐0台1湾2金3牌4牛5肉6米7粉*加辣8*套9餐
    System.out.println("");
    System.out.print(emisStringCharsetName.format("数量", 4, "\r\n", "  ",
      "L", ' ', "GBK"));
    System.out.print(emisStringCharsetName.format("品名", 10, "\r\n", "  ",
      "L", ' ', "GBK"));
    System.out.print(emisStringCharsetName.format("单价", 6, "\r\n", "  ",
      "R", ' ', "GBK"));
    System.out.println("");
    System.out.println(emisStringCharsetName.formatAlignL("-", 20, '-', "GBK"));
    System.out.println("");
    System.out.print(emisStringCharsetName.formatAlignL("1", 4, "GBK"));
    System.out.print(emisStringCharsetName.formatAlignL("卤肉饭", 10, "GBK"));
    System.out.print(emisStringCharsetName.formatAlignR("8", 6, "GBK"));
    System.out.println("");
    System.out.println(emisStringCharsetName.formatAlignL("-", 20, '-', "GBK"));
    System.out.print(emisStringCharsetName.formatAlignL("10.0", 4, "GBK"));
    System.out.print(emisStringCharsetName.formatAlignL(
      "台1湾2金3牌4牛5肉6米7粉*加辣8*套9餐0台1湾2金3牌4牛5肉6米7粉*加辣8*套9餐", 10, "GBK"));
    System.out.print(emisStringCharsetName.formatAlignR("11.0", 6, "GBK"));
    System.out.println("");
    System.out.println(emisStringCharsetName.formatAlignL("-", 20, '-', "GBK"));

    FormatFld fld = new FormatFld("数量", 4, "L");
    FormatFld fld2 = new FormatFld("品名", 10, "L");
    FormatFld fld3 = new FormatFld("单价", 6, "R");
    String[] str = emisStringCharsetName.formatLines("GBK", fld, fld2, fld3);
    for (int i = 0; i < str.length; i++) {
      System.out.println(str[i]);
    }
    System.out.println(emisStringCharsetName.formatAlignL("-", 20, '-', "GBK"));
    fld.setContent("10.0");
    fld2.setContent("台1湾2金3牌4牛5肉6米7粉*加辣8*套9餐0台1湾2金3牌4牛5肉6米7粉*加辣8*套9餐");
    fld3.setContent("11.0");
    str = emisStringCharsetName.formatLines("GBK", fld, fld2, fld3);
    for (int i = 0; i < str.length; i++) {
      System.out.println(str[i]);
    }
    fld.setContent("3.0");
    fld2.setContent("红烧鸡腿饭");
    fld3.setContent("15.0");
    System.out.print(emisStringCharsetName.formatLinesToStrBuf("GBK", fld, fld2, fld3));

    System.out.println(emisStringCharsetName.formatAlignL("-", 20, '-', "GBK"));
    System.out.println(emisStringCharsetName.formatAlignC("测试居中", 20, '-', "GBK"));
  }

  /**
   * 多字段行处理，可按各个栏位宽度分割好
   *
   * @param args formatFld
   * @return
   */
  public static String[] formatLines(String charsetName, FormatFld... args) {
    FormatFld fld = null;
    int lineCnt = 1;// 多少行
    for (int i = 0; i < args.length; i++) {
      fld = args[i];
      fld.setLins(emisStringCharsetName.formatLinsAlign(fld.getContent(),
        fld.getiFldWidth(), fld.getAlign(), charsetName));
      lineCnt = Math.max(fld.getLins().length, lineCnt);
    }

    String[] newLines = new String[lineCnt];
    StringBuffer sbf = null;
    for (int i = 0; i < lineCnt; i++) {
      sbf = new StringBuffer();
      for (int j = 0; j < args.length; j++) {
        fld = args[j];
        if (i > fld.getLins().length - 1) {
          // 如果这一行没有，就补足空格
          sbf.append(emisStringCharsetName.formatAlignL(" ", fld
            .getiFldWidth(), charsetName));
        } else {
          sbf.append(fld.getLins()[i]);
        }
      }
      newLines[i] = sbf.toString();
    }
    return newLines;
  }

  /**
   * 多字段行处理，可按各个栏位宽度分割好
   *
   * @param list
   * @param charsetName
   * @return
   */
  public static String[] formatLines(ArrayList<FormatFld> list, String charsetName) {
    if (list == null || list.size() < 1) {
      return null;
    }
    FormatFld fld = null;
    int lineCnt = 1;// 多少行
    for (Iterator<FormatFld> iterator = list.iterator(); iterator.hasNext(); ) {
      fld = (FormatFld) iterator.next();
      fld.setLins(emisStringCharsetName.formatLinsAlign(fld.getContent(),
        fld.getiFldWidth(), fld.getAlign(), charsetName));
      lineCnt = Math.max(fld.getLins().length, lineCnt);
    }

    String[] newLines = new String[lineCnt];
    StringBuffer sbf = null;
    for (int i = 0; i < lineCnt; i++) {
      sbf = new StringBuffer();
      for (Iterator<FormatFld> iterator = list.iterator(); iterator
        .hasNext(); ) {
        fld = (FormatFld) iterator.next();
        if (i > fld.getLins().length - 1) {
          // 如果这一行没有，就补足空格
          sbf.append(emisStringCharsetName.formatAlignL(" ", fld
            .getiFldWidth(), charsetName));
        } else {
          sbf.append(fld.getLins()[i]);
        }
      }
      newLines[i] = sbf.toString();
    }
    return newLines;
  }

  /**
   * 多字段行处理，可按各个栏位宽度分割好,返回StringBuffer
   *
   * @param args formatFld
   * @return
   */
  public static StringBuffer formatLinesToStrBuf(String charsetName, FormatFld... args) {
    String[] newLines = formatLines(charsetName, args);
    StringBuffer sbf = new StringBuffer();
    for (int i = 0; i < newLines.length; i++) {
      sbf.append(newLines[i] + "\r\n");
    }
    return sbf;
  }

  /**
   * 处理字符串，超过*个中文字自动换行 ("\r\n"换行)
   *
   * @param str
   * @param oneLineNum 一行字的个数（中文）
   * @return
   */
  public static String newline(String str, int oneLineNum, String charsetName) {
    return newline(str, oneLineNum, "\r\n", "", charsetName);
  }

  /**
   * 处理字符串，超过*个中文字自动切割 ("\r\n"换行)
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @param trPrStr    换行后前缀字符
   * @return
   */
  public static String newline(String str, int oneLineNum, String trPrStr, String charsetName) {
    return newline(str, oneLineNum, "\r\n", trPrStr, charsetName);
  }

  /**
   * 处理字符串，超过*个中文字自动切割，返回String[]
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @return
   */
  public static String[] newlines(String str, int oneLineNum) {
    String[] strRows = emisStringCharsetName.newline(str, oneLineNum,
      "@@@", "").split("@@@");
    return strRows;
  }

  /**
   * 处理字符串，超过*个中文字自动切割，返回String[]
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @param trPrStr    换行后前缀字符
   * @return
   */
  public static String[] newlines(String str, int oneLineNum, String trPrStr) {
    String[] strRows = emisStringCharsetName.newline(str, oneLineNum,
      "@@@", trPrStr).split("@@@");
    return strRows;
  }

  /**
   * 处理字符串，超过*个中文字自动切割
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @param splitStr   换行标记符号
   * @param trPrStr    换行后前缀字符
   * @return
   */
  public static String newline(String str, int oneLineNum, String splitStr,
                               String trPrStr, String charsetName) {
    return format(str, oneLineNum, splitStr, trPrStr, "", ' ', charsetName);
  }

  /**
   * 两个数字/英文
   *
   * @param str 字符串
   * @param num 每隔几个字符插入一个字符串
   * @return int 最终索引
   * @throws UnsupportedEncodingException
   */
  public static int getEndIndex(String str, double num, String charsetName)
    throws UnsupportedEncodingException {
    int idx = 0;
    double val = 0.00;
    // 判断是否是英文/中文
    for (int i = 0; i < str.length(); i++) {
      if (String.valueOf(str.charAt(i)).getBytes(charsetName).length >= 3) {
        // 中文字符或符号
        val += 1.00;
      } else {
        // 英文字符或符号
        val += 0.50;
      }
      if (val >= num) {
        idx = i;
        if (val - num == 0.5) {
          idx = i - 1;
        }
        break;
      }
    }
    if (idx == 0) {
      idx = str.length() - 1;
    }
    return idx;
  }

  /**
   * 对齐处理,并且多行
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @return
   */
  public static String[] formatLinsAlign(String str, int oneLineNum,
                                         String align, String charsetName) {
    String[] strRows = format(str, oneLineNum, "@@@", "", align, ' ', charsetName)
      .split("@@@");
    return strRows;
  }

  /**
   * 左对齐处理,并且多行
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @return
   */
  public static String[] formatLinsAlignL(String str, int oneLineNum, String charsetName) {
    String[] strRows = format(str, oneLineNum, "@@@", "", "L", ' ', charsetName).split(
      "@@@");
    return strRows;
  }

  /**
   * 居中对齐处理,并且多行
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @return
   */
  public static String[] formatLinsAlignC(String str, int oneLineNum, String charsetName) {
    String[] strRows = format(str, oneLineNum, "@@@", "", "C", ' ', charsetName).split(
      "@@@");
    return strRows;
  }

  /**
   * 右对齐处理,并且多行
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @return
   */
  public static String[] formatLinsAlignR(String str, int oneLineNum, String charsetName) {
    String[] strRows = format(str, oneLineNum, "@@@", "", "R", ' ', charsetName).split(
      "@@@");
    return strRows;
  }

  /**
   * 左对齐处理
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @param alignChar  补充的字符
   * @return
   */
  public static String formatAlignL(String str, int oneLineNum, char alignChar, String charsetName) {
    return format(str, oneLineNum, "\r\n", "", "L", alignChar, charsetName);
  }

  /**
   * 右对齐处理
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @param alignChar  补充的字符
   * @return
   */
  public static String formatAlignR(String str, int oneLineNum, char alignChar, String charsetName) {
    return format(str, oneLineNum, "\r\n", "", "R", alignChar, charsetName);
  }

  /**
   * 居中对齐处理
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @param alignChar  补充的字符
   * @return
   */
  public static String formatAlignC(String str, int oneLineNum, char alignChar, String charsetName) {
    return format(str, oneLineNum, "\r\n", "", "C", alignChar, charsetName);
  }

  /**
   * 补空格、左对齐处理(超出"\r\n" 换行)
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文），英文数量为：2*oneLineNum
   * @return
   */
  public static String formatAlignL(String str, int oneLineNum, String charsetName) {
    return format(str, oneLineNum, "\r\n", "", "L", ' ', charsetName);
  }

  /**
   * 补空格、居中处理(超出"\r\n" 换行)
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文），英文数量为：2*oneLineNum
   * @return
   */
  public static String formatAlignC(String str, int oneLineNum, String charsetName) {
    return format(str, oneLineNum, "\r\n", "", "C", ' ', charsetName);
  }

  /**
   * 补空格、右对齐处理 (超出"\r\n" 换行)
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文），英文数量为：2*oneLineNum
   * @return
   */
  public static String formatAlignR(String str, int oneLineNum, String charsetName) {
    return format(str, oneLineNum, "\r\n", "", "R", ' ', charsetName);
  }

  /**
   * 处理字符串，超过*个中文字自动切割,如果不超过，补字符对齐
   *
   * @param str        原字符串
   * @param oneLineNum 一行字的个数（中文）
   * @param splitStr   换行标记符号
   * @param trPrStr    换行后前缀字符
   * @param align      长度不足时,是否补空格:""不补,左L,右R
   * @param alignChar  补充的字符
   * @return
   */
  public static String format(String str, int oneLineNum, String splitStr,
                              String trPrStr, String align, char alignChar, String charsetName) {
    try {
      StringBuffer sb = new StringBuffer();
      String temp = str;
      int prlen = trPrStr.length();
      int len = str.length();
      boolean isNeedAlign = !"".equals(align);// 是否需要考虑左补或者右补空格
      boolean isNeedAddPrStr = false;// 换行后是否需要添加前缀
      boolean isFirst = true;
      if ("".equals(alignChar)) {
        alignChar = ' ';
      }
      if (charsetName == null || "".equals(charsetName.trim())) {
        charsetName = "GBK";
      }
      while (len > 0) {
        if (isNeedAddPrStr) {
          temp = trPrStr + temp;
          isNeedAddPrStr = false;
        }
        int iLen = emisStringCharsetName.lengthB(temp, charsetName);

        // int idx = getEndIndex(temp, oneLineNum, charsetName);
        int idx = Math.min(oneLineNum * 2, iLen);
        // 第一行字符
        String tempStr = emisStringCharsetName.subStringB(temp, 0, idx, charsetName, (iLen > idx));
        int iOneLineLen = emisStringCharsetName.lengthB(tempStr, charsetName);
        if (idx != iOneLineLen) {
          //2021/03/01 viva modify 特殊字符截取有问题：System.out.println("・字符的GBK长度"+("・".getBytes("GBK").length) ); 为1
          LogKit.error("【可能存在特殊字符】字符:" + tempStr + ",一行的字符Byte个数应是:" + idx + ",但实际字符Byte个数：" + iOneLineLen);
        }
        // 切去第一行后剩下的字符
        String secTempStr = emisStringCharsetName.subStringB(temp, iOneLineLen, iLen, charsetName, false);
        // 切去第一行后剩下的字符长度
        int secLen = secTempStr.length();
        // 是否需要考虑左补或者右补空格(有对齐方式、字符不一样、第二行为空，表示要补空格)
        if (isFirst && isNeedAlign && tempStr.equals(str)
          && secLen == 0) {
          // 补空格？多少个呢？
          if ("R".equalsIgnoreCase(align)) {
            // 右对齐，左补空格
            // sb.append(StringUtils.leftPad(str,
            // oneLineNum));//中文有问题
            sb.append(emisStringCharsetName.lPadB(str,
              oneLineNum * 2, alignChar, charsetName));
          } else if ("L".equalsIgnoreCase(align)) {
            // 左对齐，右补空格
            // sb.append(StringUtils.rightPad(str,
            // oneLineNum));//中文有问题
            sb.append(emisStringCharsetName.rPadB(str,
              oneLineNum * 2, alignChar, charsetName));
          } else if ("C".equalsIgnoreCase(align)) {
            // 左对齐，右补空格
            // sb.append(StringUtils.rightPad(str,
            // oneLineNum));//中文有问题
            sb.append(emisStringCharsetName.cPadB(str,
              oneLineNum * 2, alignChar, charsetName));
          }
          // 处理完后，直接退出
          len = 0;
          break;
        }
        sb.append(tempStr);
        temp = secTempStr;
        len = secLen;
        if (len > 0) {
          sb.append(splitStr);
          isNeedAddPrStr = (prlen > 0);
        }
        isFirst = false;
      }
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return str;
    }
  }
}
