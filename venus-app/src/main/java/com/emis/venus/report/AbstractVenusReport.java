package com.emis.venus.report;

import com.emis.venus.common.emisKeeper;
import com.emis.venus.hardware.printer.PrinterInf;
import com.emis.venus.hardware.printer.VenusPrinter;
import com.emis.venus.util.emisUtil;
import com.emis.venus.util.log4j.LogKit;
import com.emis.venus.util.log4j.emisLogger;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 列印的抽象类
 */
public abstract class AbstractVenusReport {
  /**
   * 外部報表傳入,是否需要打印二維碼
   */
  protected boolean isNeedPrintQrcode = false;

  /**
   * 外部報表傳入,是否需要打印logo
   */
  protected boolean isNeedPrintLogo = false;

  /**
   * 是否58纸张
   */
  public boolean is58Len = false;
  /**
   * 启用后结账功能
   */
  public boolean IS_SETTLE_ORDER_LATER = emisUtil.parseBoolean(emisKeeper
    .getInstance().getEmisPropMap().get("IS_SETTLE_ORDER_LATER"));

  public AbstractVenusReport() {
    setWindowsMaxLen();
  }

  public AbstractVenusReport(String SL_DATE_S, String SL_DATE_E) {
    this.SL_DATE_S = SL_DATE_S;
    this.SL_DATE_E = SL_DATE_E;
    setWindowsMaxLen();
  }

  public void setWindowsMaxLen() {
    // window打印机需要特殊处理 2014/05/21 viva modify 没办法的办法
    if (IS_BILL_WINDOWS_PRINTER) {
      this.iMaxLen = this.iInit_MaxLen - 2;
      if (this.iInit_MaxLen >= 32 && this.iInit_MaxLen <= 40) {
        this.iMaxLen = this.iInit_MaxLen - 4;
      }
    }
    is58Len = (this.iMaxLen <= 32);
    LogKit.info("字符最大个数：" + iMaxLen + ",纸张大小：" + (is58Len ? "58" : "80"));
  }

  protected final Logger log = emisLogger.getLog(getClass());

  /**
   * 原始小票的宽度
   */
  protected int iInit_MaxLen = emisUtil.parseInt(emisKeeper.getInstance()
    .getEmisPropMap().get("BILL_MAXLEN"), 32) + emisUtil.parseInt(emisKeeper.getInstance()
    .getEmisPropMap().get("BILL_WIDTH_ADJUST"), 0);

  /**
   * 小票的宽度
   */
  protected int iMaxLen = emisUtil.parseInt(emisKeeper.getInstance()
    .getEmisPropMap().get("BILL_MAXLEN"), 32);

  /**
   * bill打印机是否window打印机 （VenusPrinter.java）
   */
  protected boolean IS_BILL_WINDOWS_PRINTER = emisUtil.parseBoolean(emisKeeper
    .getInstance().getEmisPropMap().get("IS_BILL_WINDOWS_PRINTER"));

  /**
   * 小票编码格式
   */
  protected String billCode = emisUtil.parseString(emisKeeper.getInstance()
    .getEmisPropMap().get("BILL_CODE")); //$NON-NLS-1$

  protected String S_NO = emisUtil.parseString(emisKeeper.getInstance()
    .getEmisPropMap().get("S_NO")); //$NON-NLS-1$
  protected String ID_NO = emisUtil.parseString(emisKeeper.getInstance()
    .getEmisPropMap().get("ID_NO")); //$NON-NLS-1$
  protected String COMPANY_NAME = emisUtil.parseString(emisKeeper.getInstance()
    .getEmisPropMap().get("COMPANY_NAME")); //$NON-NLS-1$

  /**
   * 小票内容
   */
  protected StringBuffer printBuf = null;

  /**
   * @return the printBuf
   */
  public StringBuffer getPrintBuf() {
    return printBuf;
  }

  /**
   * @param printBuf the printBuf to set
   */
  public void setPrintBuf(StringBuffer printBuf) {
    this.printBuf = printBuf;
  }

  /**
   * 产生列印内容
   */
  public abstract void createPrintBuf() throws Exception;

  /**
   * 是否统一添加门店机台信息
   */
  private boolean isAddSnoInfor = false;

  /**
   * 是否多机台报表
   */
  private boolean isMoreRpt = false;

  /**
   * 工作单打印机群组编号
   */
  private String groupId;

  /**
   * //2019/04/04 viva modify 需求 #47096 标准版需求：前台各项报表添加门店显示
   * 报表添加打印门店号，机台号信息(统一加在报表结尾)
   */
  public final void addPrintBufSnoInfor() {
    if (printBuf == null) {
      printBuf = new StringBuffer();
    }
    printBuf.append("\r\n");
    printBuf.append("---------------------------");
    printBuf.append("\r\n");
    printBuf.append(this.COMPANY_NAME);
    printBuf.append("\r\n");
    printBuf.append("[门店号]:" + this.S_NO);
    printBuf.append("\r\n");
    printBuf.append("[机台号]:" + this.ID_NO);
    printBuf.append("\r\n");
    printBuf.append("---------------------------");
    printBuf.append("\r\n");
  }


  /**
   * 列印接口
   */
  protected PrinterInf printer = null;

  /**
   * 列印类型：1列印小票 2产生文件 3列印小票 +产生文件（调用createTxt()）
   */
  int rptFlag = 1;

  /**
   * 产生文本文件的名称
   */
  String fileName;

  /**
   * 销售编号
   */
  String SL_KEY;
  /**
   * 路径
   */
  String path;

  /**
   * 产生sale资料备份
   */
  boolean isCreateSaleData = false;

  /**
   * 营业日期
   */
  String SL_DATE_S;

  /**
   * 营业日期
   */
  String SL_DATE_E;

  /**
   * 打印延迟时间
   */
  int delayTime = emisUtil.parseInt(emisKeeper.getInstance().getEmisPropMap()
    .get("BILL_DELAY"), 2000);

  /**
   * 产生txt,必须调用此方法，并且rptFlag=3 才能产生txt<br>
   *
   * @param isNeedRepace 是否需要替换特殊字符
   *                     ：比如放大字体的标示
   */
  public void createTxt(boolean isNeedRepace) {
    this.iMaxLen = this.iInit_MaxLen;
    try {
      if (printBuf == null || printBuf.length() <= 0) {
        createPrintBuf();
        if (isAddSnoInfor) {
          this.addPrintBufSnoInfor();
        }
      }
      String printContent = printBuf.toString();
      // 产生文本，目前只保留小票
      if (rptFlag == 3) {
        createTxt(printContent, isNeedRepace);
      }
    } catch (Exception e) {
      log.error("createTxt error " + e); //$NON-NLS-1$
    }
  }

  /**
   * 列印 如果子类重装过此方法，注意要对 window打印机需要特殊处理，如VenusDailyCloseReport
   *
   * @return
   */
  public void print() throws Exception {
    this.iMaxLen = this.iInit_MaxLen;
    // window打印机需要特殊处理
    IS_BILL_WINDOWS_PRINTER = true;
    if (IS_BILL_WINDOWS_PRINTER) {
      if (this.printer == null) {
        initPrinter();
        this.printer.init();
      }
      if (printer != null && printer instanceof VenusPrinter) {
        // windows打印机把格式缩进2个长度
        this.iMaxLen = this.iInit_MaxLen - 2;
        if (this.iInit_MaxLen >= 32 && this.iInit_MaxLen <= 40) {
          this.iMaxLen = this.iInit_MaxLen - 4;
        }
        printBuf = null;
      }
    }
    String printContent = null;
    try {
      if (printBuf == null || printBuf.length() <= 0) {
        createPrintBuf();
        //2019/04/04 viva modify 需求 #47096 标准版需求：前台各项报表添加门店显示
        if (isAddSnoInfor) {
          this.addPrintBufSnoInfor();
        }
      }
      printContent = printBuf.toString();
    } catch (Exception e) {
      log.error("createPrintBuf Error:" + e);
    }
    print(printContent, true);
  }

  /**
   * 列印
   *
   * @param printContent
   * @param isNeedCut    是否需要切纸
   * @throws Exception
   */
  public void print(String printContent, boolean isNeedCut) throws Exception {
    try {
      if ("".equals(emisUtil.parseString(printContent))) {
        return;
      }
      if (this.printer == null) {
        initPrinter();
        this.printer.init();
      }
      if (!this.printer.checkOnline()) {
        return;
      }
      // 列印
      if (isNeedCut) {
        printer.print(printContent, iMaxLen);
      } else {
        printer.printNoCut(printContent, iMaxLen);
      }
    } catch (Exception e) {
      log.error("AbstractVenusReport error ", e);
      if (printer instanceof VenusPrinter) {
        String printerName = ((VenusPrinter) printer).getPrinterName();
      }
      this.printer = null;
      return;
    } finally {
      /**
       * 关闭打印机
       */
      if (this.printer != null) {
        this.printer.close();
        this.printer = null;
      }
    }
  }

  /**
   * @param printContent
   * @param isNeedRepace 是否需要替换特殊字符：比如放大字体的标示
   * @throws IOException
   */
  public void createTxt(String printContent, boolean isNeedRepace)
    throws IOException {
    try {
      // 产生小票txt
      File file = new File(this.getFileName());
      // FileUtils.writeStringToFile(file, printContent.replaceAll(
      // PrinterInf.CUT_PAGE_MARK, "").replaceAll(
      // PrinterInf.BIG_FONT_MARK, ""), "UTF-8");

      List lines = new LinkedList();
      try {
        if (file != null && file.exists() && file.isFile()) {
          // 文件大于1M时
          if (file.length() > (1 * 1024 * 1024)) {
            File file2 = new File(this.getFileName().replaceAll(".txt", "." + emisUtil.getSYSDATETIME() + ".txt"));
            file.renameTo(file2);
            file2 = null;

            file = new File(this.getFileName());
          }
          lines = FileUtils.readLines(file, "UTF-8");
          lines.add(" \r\n");
        } else {
          lines = new LinkedList();
        }
      } catch (Exception e) {
        lines = new LinkedList();
      }
      if (isNeedRepace) {
        lines.add(printContent.replaceAll(PrinterInf.CUT_PAGE_MARK, "")
          .replaceAll(PrinterInf.BIG_FONT_MARK, "")
          .replaceAll(PrinterInf.BIG_FONT_1_MARK, "")
          .replaceAll(PrinterInf.BIG_FONT_2_MARK, "")
          .replaceAll(PrinterInf.SUM_MARK, ""));
      } else {
        lines.add(printContent);
      }
      FileUtils.writeLines(file, "UTF-8", lines);

      // 再写一次文件，就可以用记事本直接开启文件
      // lines = FileUtils.readLines(file, "UTF-8");
      // FileUtils.writeLines(file, "UTF-8", lines);

      log.info("Create " + file.getName() + " End");
      if (lines != null) {
        lines.clear();
        lines = null;
      }
      file = null;
    } catch (Exception e) {
      log.error("create " + this.fileName + " Error:", e);
    }

  }

  /**
   * 产生独立可供列印txt,在产生文本createTxt和列印print之前调用 以免影响使用
   *
   * @param fileName     完整文件名
   * @param isRetract    window打印机是否需要处理缩进
   * @param isNeedRepace 是否需要替换特殊字符 ：比如放大字体的标示
   */
  public void createRptTxt(String fileName, boolean isRetract, boolean isNeedRepace) {
    if (fileName == null || "".equals(fileName))
      return;
    this.iMaxLen = this.iInit_MaxLen;
    // window打印机需要特殊处理
    if (IS_BILL_WINDOWS_PRINTER && isRetract) {
      // windows打印机把格式缩进2个长度
      this.iMaxLen = this.iInit_MaxLen - 2;
      if (this.iInit_MaxLen >= 32 && this.iInit_MaxLen <= 40) {
        this.iMaxLen = this.iInit_MaxLen - 4;
      }
    }
    printBuf = null;
    try {
      if (printBuf == null || printBuf.length() <= 0) {
        createPrintBuf();
        if (isAddSnoInfor) {
          this.addPrintBufSnoInfor();
        }
      }
      String printContent = printBuf.toString();
      File file = new File(fileName);
      List lines = new LinkedList();
      if (isNeedRepace) {
        lines.add(printContent.replaceAll(PrinterInf.CUT_PAGE_MARK, "")
          .replaceAll(PrinterInf.BIG_FONT_MARK, "").replaceAll(PrinterInf.BIG_FONT_1_MARK, "")
          .replaceAll(PrinterInf.BIG_FONT_2_MARK, "").replaceAll(PrinterInf.SUM_MARK, ""));
      } else {
        lines.add(printContent);
      }
      FileUtils.writeLines(file, "UTF-8", lines);

      log.info("Create " + file.getName() + " End");
      if (lines != null) {
        lines.clear();
        lines = null;
      }
      file = null;
    } catch (Exception e) {
      log.error("createRptTxt error ", e); //$NON-NLS-1$
    } finally {
      printBuf = null;
    }
  }

  /**
   * 初始化打印机<br>
   * 在print方法里调用，有检查小票机 不能乱放
   *
   * @throws Exception
   */
  public abstract void initPrinter();

  public void close() {
    if (printer != null) {
      printer.close();
      printer.setQRCODE("");
      printer.setTicketCodes(null);
    }
  }

  public void cleanReport() {
    if (this.printBuf != null) {
      this.printBuf.delete(0, this.printBuf.length());
      this.printBuf = null;
      if (printer != null) {
        this.printer.setQRCODE("");
        this.printer.setTicketCodes(null);
      }
    }
  }

  /**
   * 得到文件的路径
   *
   * @return
   */
  public String getFileName() {
    String name = ""; //$NON-NLS-1$
    path = emisUtil.getFileRptRoot() + SL_DATE_S + "/";
    File dir = new File(path);
    dir.mkdirs();
    try {
      if (!dir.exists())
        dir.createNewFile();
    } catch (Exception e) {
      LogKit.error(e, e);
    }
    dir = null;
    name = path + fileName + ".txt"; //$NON-NLS-1$ //$NON-NLS-2$
    return name;
  }

  /**
   * 列印类型：1列印小票 2产生文件 3列印小票 +产生文件（调用createTxt()）
   *
   * @param rptFlag the rptFlag to set
   */
  public void setRptFlag(int rptFlag) {
    this.rptFlag = rptFlag;
  }

  /**
   * @param fileName the fileName to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * 是否用小票机开钱箱，如果是window打印机，请关闭这个设定。
   */
  public void openDesk() throws Exception {
    log.info("AbstractVenusReport：用小票机开钱箱（非window）");
    /*if (!SaleUtil.isOpenDeskByPayType()) {
      log.info("该付款别未设定要开启钱箱");
      return;
    }*/
    if (!IS_BILL_WINDOWS_PRINTER && emisUtil.parseBoolean(emisKeeper.getInstance().getEmisPropMap()
      .get("BILL_ISOPENDESK"))) { //$NON-NLS-1$
      boolean flag = false;
      try {
        if (this.printer == null) {
          initPrinter();
          flag = true;
        }
        this.printer.openDesk();
      } catch (Exception e) {
        // 说明initPrinter();就抛异常了
        if (!flag) {
          this.printer = null;
        }
        log.error("openDesk error " + e, e); //$NON-NLS-1$
        return;
      }
      log.info("open desk "); //$NON-NLS-1$
    }
  }

  public void setSL_KEY(String sL_KEY) {
    SL_KEY = sL_KEY;
  }

  public String getSL_KEY() {
    return SL_KEY;
  }

  public void setSL_DATE_S(String sL_DATE) {
    SL_DATE_S = sL_DATE;
  }

  public String getSL_DATE_S() {
    return SL_DATE_S;
  }

  /**
   * @param sL_DATE_E the sL_DATE_E to set
   */
  public void setSL_DATE_E(String sL_DATE_E) {
    SL_DATE_E = sL_DATE_E;
  }

  /**
   * @return the sL_DATE_E
   */
  public String getSL_DATE_E() {
    return SL_DATE_E;
  }

  /**
   * @return the rptFlag
   */
  public int getRptFlag() {
    return rptFlag;
  }

  /**
   * 放大字体
   *
   * @param line 需放大内容
   */
  public String doBigFontMark(String line) {
    if (line != null && !"".equals(line)) { //$NON-NLS-1$
      StringBuffer strBuf = new StringBuffer();
      if (IS_BILL_WINDOWS_PRINTER) {
        strBuf.append(PrinterInf.BIG_FONT_1_MARK);
      } else {
        strBuf.append(PrinterInf.BIG_FONT_MARK);
      }
      strBuf.append(line);
      if (IS_BILL_WINDOWS_PRINTER) {
        strBuf.append(PrinterInf.BIG_FONT_1_MARK);
      } else {
        strBuf.append(PrinterInf.BIG_FONT_MARK);
      }
      return strBuf.toString();
    }
    return line;
  }

  public void setiInit_MaxLen(int iInit_MaxLen) {
    this.iInit_MaxLen = iInit_MaxLen;
  }

  public void setiMaxLen(int iMaxLen) {
    this.iMaxLen = iMaxLen;
  }

  public boolean isNeedPrintQrcode() {
    return isNeedPrintQrcode;
  }

  public void setNeedPrintQrcode(boolean isNeedPrintQrcode) {
    this.isNeedPrintQrcode = isNeedPrintQrcode;
  }

  public void setNeedPrintLogo(boolean isNeedPrintLogo) {
    this.isNeedPrintLogo = isNeedPrintLogo;
  }

  public boolean isNeedPrintLogo() {
    return isNeedPrintLogo;
  }

  /**
   * 报表添加打印门店号，机台号信息(统一加在报表结尾)
   *
   * @param isAddSnoInfor
   */
  public void setAddSnoInfor(boolean isAddSnoInfor) {
    this.isAddSnoInfor = isAddSnoInfor;
  }

  public boolean isAddSnoInfor() {
    return isAddSnoInfor;
  }

  public void setMoreRpt(boolean isMoreRpt) {
    this.isMoreRpt = isMoreRpt;
  }

  /**
   * 是否多机台报表
   *
   * @return
   */
  public boolean isMoreRpt() {
    return isMoreRpt;
  }

  public void setIS_BILL_WINDOWS_PRINTER(boolean iS_BILL_WINDOWS_PRINTER) {
    IS_BILL_WINDOWS_PRINTER = iS_BILL_WINDOWS_PRINTER;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }
}
