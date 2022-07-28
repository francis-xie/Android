package com.emis.venus.hardware.printer;

import com.emis.venus.common.emisKeeper;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.entity.Entity;
import com.emis.venus.util.emisPropUtil;
import com.emis.venus.util.emisUtil;
import com.emis.venus.util.log4j.LogKit;
import com.emis.venus.util.log4j.emisLogger;
import com.freak.printtool.hardware.utils.DateUtil;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 小票机<br>
 * 从emisprop中取参数
 */
public class VenusPrinter extends VenusWindowsPrinter implements PrinterInf {

  /**
   * 打印机名称
   */
  String printerName = "小票机";// 小票机

  protected final Logger log = emisLogger.getLog(getClass());

  /**
   * 小票机一次列印行数
   */
  int BILL_LENGTH = emisUtil.parseInt(emisKeeper.getInstance()
    .getEmisPropMap().get("BILL_LENGTH"), 60); //$NON-NLS-1$

  /**
   * 延时时间
   */
  int delayTime = emisUtil.parseInt(emisKeeper.getInstance().getEmisPropMap()
    .get("BILL_DELAY"), 2000); //$NON-NLS-1$

  boolean IS_BILL_WINDOWS_PRINTER = emisUtil.parseBoolean(emisKeeper
    .getInstance().getEmisPropMap().get("IS_BILL_WINDOWS_PRINTER"), true);

  // 打印240*240，Level L(容錯率7%)的二維碼
  boolean IS_PRT_QRCODE = emisUtil.parseBoolean(emisKeeper.getInstance()
    .getEmisPropMap().get("IS_PRT_QRCODE"));

  // 打印公司LOGO，長192，高(8*x) 尺寸的PNG格式圖片
  boolean IS_PRT_LOGO = emisUtil.parseBoolean(emisKeeper.getInstance()
    .getEmisPropMap().get("IS_PRT_LOGO"));

  // 打印二維碼的位置：0 小票开始 1 小票结尾
  int PRT_QRCODE_LOCATION = emisUtil.parseInt(emisKeeper.getInstance()
    .getEmisPropMap().get("PRT_QRCODE_LOCATION"));
  // WINDOWS针式打印机字体微调
  int iBILL_WP_FONT_ADJUST;
  // windwos打印预留空白
  int iIsRptWindows;

  /**
   * 二维码
   */
  public String sQRCODE = "";
  /**
   * 一维码或二维码列印列表： <br>
   * String[] 0(0:二维码、1:一维码)、1(一维码或二维码内容)、2-产生图片文件名、3-前面列印相关描述信息、4-后面列印相关描述信息
   */
  public List<String[]> ticketCodes;
  /**
   * 单单计电子发票二维码[1]:开票 URL、[2]:小票单号(二维码图片文件名)
   */
  public String[] ddjQrUrl;

  public VenusPrinter() {
    setWindowsParam();
  }

  public void setWindowsParam() {
    if (IS_BILL_WINDOWS_PRINTER) {
      int BILL_WP_FONT_ADJUST1 = emisUtil.parseInt(emisKeeper.getInstance()
        .getEmisPropMap().get("BILL_WP_FONT_ADJUST"));
      int BILL_WINDOWS_BLANK1 = emisUtil.parseInt(emisKeeper.getInstance()
        .getEmisPropMap().get("BILL_WINDOWS_BLANK"));
      // 如果emisprop表里面对应的预留空白和字体微调值为 0 或 空 就在windowReport.ini里取值，否则就在emisprop里取值
      iBILL_WP_FONT_ADJUST = BILL_WP_FONT_ADJUST1 == 0 ? emisUtil
        .parseInt(emisPropUtil.getEmisProp(
          "BILL_WP_FONT_ADJUST", "0")) : BILL_WP_FONT_ADJUST1;
      iIsRptWindows = BILL_WINDOWS_BLANK1 == 0 ? emisUtil
        .parseInt(emisPropUtil.getEmisProp(
          "BILL_WINDOWS_BLANK", "0")) : BILL_WINDOWS_BLANK1;
      // 把获得的预留空白和字体微调的值保存在windowReport.ini里
      emisPropUtil.getEmisProp("BILL_WP_FONT_ADJUST",
        emisUtil.parseString(iBILL_WP_FONT_ADJUST));
      emisPropUtil.getEmisProp("BILL_WINDOWS_BLANK",
        emisUtil.parseString(iIsRptWindows));
      // 如果预留空白和字体微调两个值有一个不为0就把emisprop表里对应的值都设置为0
      if (BILL_WP_FONT_ADJUST1 != 0 || BILL_WINDOWS_BLANK1 != 0) {
        save();
      }
    }
  }

  /**
   * 把预留空白和字体微调的值改为0存入DB
   */
  public void save() {
    HashMap<String, String> emisPropMap = new HashMap<String, String>();
    emisPropMap.put("BILL_WP_FONT_ADJUST", "0");
    emisPropMap.put("BILL_WINDOWS_BLANK", "0");
    Entity emisProp = new Entity("EMISPROP"); //$NON-NLS-1$
    emisProp = emisPropUtil.getPropEntityFromHashMap(emisPropMap);
    emisSQLiteWrapper outSideDb = emisDb.getInstance();
    try {
      outSideDb.beginTransaction();
      emisProp.updateAll(outSideDb);
      outSideDb.setTransactionSuccessful();
    } catch (Exception e) {
      emisLogger.getLog(VenusPrinter.class).error(e);
    } finally {
      if (emisProp != null) {
        emisProp.cleanData();
        emisProp = null;
      }
      if (outSideDb != null) {
        outSideDb.endTransaction();
        outSideDb.close();
        outSideDb = null;
      }
    }
  }

  private boolean isNeedRun = true;

  /**
   * 发票机初始化打印发票指令
   */
  String init = null;
  /**
   * 放大字体的指令
   */
  String bigFont = null;
  /**
   * 字体倍高倍宽的指令
   */
  String bigFont1 = null;
  /**
   * 切纸的指令
   */
  String cutPage = null;
  /**
   * 跳页的指令
   */
  String jumpPage = null;
  /**
   * 开钱箱的指令
   */
  String openDesk = null;
  /**
   * 还原字体的指令
   */
  String revertFont = null;
  /**
   * 编码
   */
  String charsetName = "GBK";

  /**
   * 外部報表傳入,是否需要打印二維碼
   */
  private boolean isNeedPrintQrcode = false;

  /**
   * 外部報表傳入,是否需要打印logo
   */
  private boolean isNeedPrintLogo = false;

  public boolean isNeedPrintQrcode() {
    return isNeedPrintQrcode;
  }

  public void setNeedPrintQrcode(boolean isNeedPrintQrcode) {
    this.isNeedPrintQrcode = isNeedPrintQrcode;
  }

  public String getCharsetName() {
    return charsetName;
  }

  public void initPrinterWindows(String sPrinterName) {
    if (sPrinterName == null) {
      sPrinterName = emisUtil.parseString(emisKeeper.getInstance().getEmisPropMap()
        .get("BILL_WINDOWS_PRINTER"));
    }
    super.setPrinterName(sPrinterName);
    printerName = sPrinterName;
    super.initPrinter();
  }

  public void initPrinter() {
    if (IS_BILL_WINDOWS_PRINTER) {
      initPrinterWindows(null);
    }
  }

  public void close() {
    if (IS_BILL_WINDOWS_PRINTER) {
      super.close();
    }
  }

  public void bigFont() throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
      super.bigFont();
    }
  }

  public void cutPage() throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
      super.cutPage();
    }
  }

  public void jumpPage() throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
      super.jumpPage();
    }
  }

  public void openDesk() throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
      super.openDesk();
    }
  }

  public void revertFont() throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
      super.revertFont();
    }
  }

  public void print(String printStr, int iMaxLen) throws Exception {
    if (printStr != null && !"".equals(printStr)) {
      // 是需要切纸的分割符
      if (printStr.indexOf(PrinterInf.CUT_PAGE_MARK) > -1) {
        String contents[] = printStr.split(PrinterInf.CUT_PAGE_MARK);
        for (int i = 0; i < contents.length; i++) {
          printeOnePage(contents[i], iMaxLen);
        }
      } else {
        printeOnePage(printStr, iMaxLen);
      }
    }
  }

  /**
   * 列印一张
   *
   * @param printStr
   * @throws Exception
   * @throws IOException
   * @throws UnsupportedEncodingException
   */
  private void printeOnePage(String printStr, int iMaxLen) throws Exception, IOException,
    UnsupportedEncodingException {
    if (printStr != null && !"".equals(printStr)) {
      if (IS_BILL_WINDOWS_PRINTER) {
        printNoCut(printStr, iMaxLen);
      }
    }
  }

  public void print() throws Exception {
    // 空就行，列印(自定义格式列印的方法，适用于windows打印机)
  }

  /**
   * 打印机名称
   *
   * @param printerName the printerName to set
   */
  public void setPrinterName(String printerName) {
    this.printerName = printerName;
  }

  /**
   * 打印机名称
   *
   * @return the printerName
   */
  public String getPrinterName() {
    return printerName;
  }

  public boolean isNeedRun() throws Exception {
    return isNeedRun;
  }

  public void setNeedRun(boolean isNeedRun) {
    this.isNeedRun = isNeedRun;
  }

  /**
   * 列印不切纸
   */
  public void printNoCut(String printStr, int iMaxLen) throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
      if (printStr != null && !"".equals(printStr)) {
        if (this.windowsPrinter == null) {
          return;
        }
        if (windowsPrinter.isConnected()) {
          //中文打印要看设置设置的中文格式对应的是哪一个，这个佳博wifi打印机是串口的，编码是GB2312,如果设置不对就会乱码
          String time = DateUtil.getTime();
          BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(windowsPrinter.getOutputStream(), "GB2312"));
          String[] printStrs = printStr.split("\r\n");
          int len = printStrs.length;
          LogKit.info("windowsPrinter打印机进行列印任务：" + " 总行数:" + len);
          for (int i = 0; i < printStrs.length; i++) {
            bufferedWriter.write(printStrs[i]);
            bufferedWriter.newLine();
          }
          bufferedWriter.flush();
          bufferedWriter.close();
          //windowsPrinter.close();
          LogKit.info("已打开");
        } else {
          LogKit.error("没有打开");
        }
      }
    }
  }


  public void init() throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
      super.init();
    }
  }

  public void jumpRows(int rows) throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
    }
  }

  public void stamp() throws Exception {
  }

  public void bigFont1() throws Exception {
    if (IS_BILL_WINDOWS_PRINTER) {
    }
  }

  public boolean checkOnline() {
    if (IS_BILL_WINDOWS_PRINTER) {
      return super.checkOnline();
    }
    return false;
  }

  public void setNeedPrintLogo(boolean isNeedPrintLogo) {
    this.isNeedPrintLogo = isNeedPrintLogo;
  }

  public boolean isNeedPrintLogo() {
    return isNeedPrintLogo;
  }

  public void printNoCut(String printStr) throws Exception {
    printNoCut(printStr, 0);
  }

  /*
   * 设置二维码
   */
  public void setQRCODE(String sQRCODE) {
    this.sQRCODE = sQRCODE;
  }

  public String getQRCODE() {
    return sQRCODE;
  }

  /*
   * 添加一维码至列表中，同一内容切勿多次设置，以免重复列印
   */
  public void addBARCODE(String sBARCODE) {
    addTicketCodes("1", sBARCODE, sBARCODE, null, sBARCODE);
  }

  /*
   * 设置一维码
   */
  public void setTicketCodes(List<String[]> ticketCodes) {
    this.ticketCodes = ticketCodes;
  }

  /**
   * 设置一维码或二维码，注意列印完成后清空，以免重复列印
   *
   * @param sCodeType     0:二维码、1:一维码
   * @param sCode         一维码或二维码内容
   * @param sCodeName     产生图片文件名
   * @param sCodePreMsg   图片前面列印相关描述信息
   * @param sCodeAfterMsg 图片后面列印相关描述信息
   */
  public void addTicketCodes(String sCodeType, String sCode, String sCodeName, String sCodePreMsg,
                             String sCodeAfterMsg) {
    if (this.ticketCodes == null) {
      this.ticketCodes = new ArrayList<String[]>();
    }
    if (isExistsTicketCodes(sCodeType, sCode)) {
      // 是否需要添加上这条判断重复，后续看需求
      return;
    }
    String[] codes = new String[5];
    codes[0] = sCodeType;
    codes[1] = sCode;
    codes[2] = sCodeName;
    codes[3] = sCodePreMsg;
    codes[4] = sCodeAfterMsg;
    this.ticketCodes.add(codes);
  }

  /**
   * 列印内容是否已存在列表中
   *
   * @param sCodeType 0:二维码、1:一维码
   * @param sCode     一维码或二维码内容
   * @return
   */
  public boolean isExistsTicketCodes(String sCodeType, String sCode) {
    if (ticketCodes != null) {
      for (String[] codes : this.ticketCodes) { // 遍历列表
        if (codes != null && codes.length >= 5) {
          if (codes[0].equals(sCodeType) && codes[1].equals(sCode)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * 单单计电子发票二维码
   *
   * @param ddjQrUrl [1]:开票 URL、[2]:小票单号(二维码图片文件名，后续客订单号要注意以免重复)
   */
  public void setDdjQrUrl(String[] ddjQrUrl) {
    this.ddjQrUrl = ddjQrUrl;
  }
}