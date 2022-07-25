package com.emis.venus.hardware.printer;

import com.emis.venus.common.emisKeeper;
import com.emis.venus.logic.EmisPropValue;
import com.emis.venus.util.emisPropUtil;
import com.emis.venus.util.emisUtil;
import com.emis.venus.util.log4j.LogKit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

/**
 * windows打印机抽象类，需要实现print的方法
 * https://blog.csdn.net/freak_csh/article/details/99622827
 */
public abstract class VenusWindowsPrinter implements PrinterInf {
  //定义编码方式
  private String encoding;
  /**
   * 打印机IP
   */
  private String ip;
  /**
   * 打印机端口号
   */
  private int port;
  /**
   * 连接超时时间
   */
  private final int SOCKET_RECEIVE_TIME_OUT = 2500;

  /**
   * 启用后结账功能
   */
  public boolean IS_SETTLE_ORDER_LATER = emisUtil.parseBoolean(emisKeeper
    .getInstance().getEmisPropMap().get("IS_SETTLE_ORDER_LATER"));

  private String printerName = null;

  protected Socket windowsPrinter = null;

  private String ipName = null;

  private String showName = null;

  /**
   * 是否BOSHI网口打印机 0 不侦测 1是为BOSHI网口打印机 2是为EPSON网口打印机
   */
  private String IS_BS_NET = "";

  /**
   * 是否错误
   */
  private boolean isError = false;

  /**
   * 字体默认字体大小
   */
  protected int SYS_FONT_SIZE = emisUtil.parseInt(emisPropUtil
    .getEmisProp("SYS_FONT_SIZE", "20"));

  /**
   * 字体放大字体大小
   */
  protected int SYS_FONT_BIG_SIZE = emisUtil.parseInt(emisPropUtil
    .getEmisProp("SYS_FONT_BIG_SIZE", "30"));

  /**
   * 字体放大字体大小（流水号）
   */
  protected int SYS_FONT_BIG_1_SIZE = emisUtil.parseInt(emisPropUtil
    .getEmisProp("SYS_FONT_BIG_1_SIZE", "35"));

  /**
   * 字体放大字体大小（门店名称）
   */
  protected int SYS_FONT_BIG_2_SIZE = emisUtil.parseInt(emisPropUtil
    .getEmisProp("SYS_FONT_BIG_2_SIZE", "35"));

  /**
   * 字体是否全部加粗
   */
  protected boolean IS_WINDOWREPORT_BOLD = emisUtil.parseBoolean(emisPropUtil
    .getEmisProp("IS_WINDOWREPORT_BOLD", "N"));

  /**
   * 小票机一次列印行数BILL_LENGTH_WIN
   * 2020/11/25 viva modify window打印机 一次列印行数参数改成用BILL_LENGTH_WIN 默认60，避免和客户现有BILL_LENGTH参数冲突
   */
  protected int BILL_LENGTH_WIN = emisUtil.parseInt(emisKeeper.getInstance()
    .getEmisPropMap().get("BILL_LENGTH_WIN"), 60);

  public VenusWindowsPrinter() {
  }

  public VenusWindowsPrinter(String printerName) {
    this.setPrinterName(printerName);
  }

  /**
   * @param printerName
   * @param ipName
   * @param showName
   * @param IS_BS_NET
   */
  public VenusWindowsPrinter(String printerName, String ipName,
                             String showName, String IS_BS_NET) {
    this.setPrinterName(printerName);
    this.setIpName(ipName);
    this.showName = showName;
    this.IS_BS_NET = IS_BS_NET;
  }

  /**
   * 初始化Pos实例
   *
   * @param ip       打印机IP
   * @param port     打印机端口号
   * @param encoding 编码
   * @throws IOException
   */
  public VenusWindowsPrinter(String ip, int port, String encoding) {
    this.ip = ip;
    this.port = port;
    this.encoding = encoding;
  }

  public void initPrinter() {
    this.ip = "10.9.1.168";
    this.port = 9100;
    this.encoding = "GB2312";
    this.printerName = "10.9.1.168";

    setDefaultPrinter();

    checkOnline();

    //再执行一次，避免切换默认打印机没切换成功
    //setDefaultPrinter();

    //2016/08/03 viva modify 先进行关闭，避免一直开着,占用handles
    try {
      if (windowsPrinter != null) {
        this.close();
      }
      //再执行一次，避免切换默认打印机没切换成功
      setDefaultPrinter();

      SocketAddress socketAddress = new InetSocketAddress(ip, port);
      windowsPrinter = new Socket();
      windowsPrinter.connect(socketAddress, SOCKET_RECEIVE_TIME_OUT);
    } catch (Throwable e) {
      LogKit.error("[ERR_CODE][4019]");
      LogKit.error("===initPrinter Error:", e);
      String sError = e.getMessage();
      // 未知的异常
      try {
        if (sError.indexOf("OutOfMemoryError") > 0
          || sError.indexOf("No more handles") > 0) {
          String sMsg = "[系统资源不足,导致打印机异常],建议立即重启电脑!!!";
          emisUtil.turnOutErrorLog(sMsg, "SALE");
        }
      } catch (Throwable e2) {
        LogKit.error(e2, e2);
      }
      return;
    }
    LogKit.error("===initPrinter:" + getPrinterName());
  }

  public void close() {
    if (windowsPrinter != null) {
      try {
        //关闭IO流和Socket
        windowsPrinter.close();
      } catch (Exception e) {
        LogKit.error(e, e);
      }
      //log.error(this.printerName+" dispose");
      windowsPrinter = null;
    }
  }

  public void bigFont() throws Exception {
  }

  public void cutPage() throws Exception {
  }

  public void jumpPage() throws Exception {
  }

  public void openDesk() throws Exception {
    LogKit.info("VenusWindowsPrinter:window开钱箱");
    //2016/10/09 viva modify 小票机如果是window打印机，开钱箱需要打印机进行打印才能开启
    print("___");
  }

  public void revertFont() throws Exception {
  }

  public void print(String printStr) throws Exception {
    if (this.windowsPrinter == null) {
      return;
    }
  }

  public void init() throws Exception {
  }

  public void jumpRows(int rows) throws Exception {
  }

  public void stamp() throws Exception {
  }

  /**
   * @param printerName the printerName to set
   */
  public void setPrinterName(String printerName) {
    this.printerName = printerName;
  }

  /**
   * @return the printerName
   */
  public String getPrinterName() {
    return printerName;
  }

  public boolean checkOnline() {
    if (isError) {
      return true;
    }
    return true;
  }

  /**
   * @param ipName the ipName to set
   */
  public void setIpName(String ipName) {
    this.ipName = ipName;
  }

  /**
   * @return the ipName
   */
  public String getIpName() {
    return ipName;
  }

  /**
   * @param showName the showName to set
   */
  public void setShowName(String showName) {
    this.showName = showName;
  }

  /**
   * @return the showName
   */
  public String getShowName() {
    return showName;
  }

  /**
   * @param iS_BS_NET the iS_BS_NET to set
   */
  public void setIS_BS_NET(String iS_BS_NET) {
    this.IS_BS_NET = iS_BS_NET;
  }

  /**
   * @return the iS_BS_NET
   */
  public String getIS_BS_NET() {
    return IS_BS_NET;
  }

  /**
   * 设定为默认打印机
   */
  public void setDefaultPrinter() {
    //log.info("DefaultPrinter_START:"+ Printer.getDefaultPrinterData().toString());

    //2018/04/18 viva modify 添加参数控制，某些厂商的打印机驱动很奇怪，必须打印之前设置为默认打印机才能进行正常的切纸和打印。
    /**
     * 是否必须切换默认打印机 IS_NEED_SET_DEFAULT_PRINTER
     */
    boolean isNeedSetDefaultPrinter = EmisPropValue.getUniqueValueBol("IS_NEED_SET_DEFAULT_PRINTER");
    if (!isNeedSetDefaultPrinter) {
      /**
       * 是否自动列印标签 ，结账时自动列印标签
       */
      if (!emisUtil.parseBoolean(emisKeeper.getInstance().getEmisPropMap().get("PRT_TAG"))) {
        return;
      }
      // 是否使用标签列印机
      if (!emisUtil.parseBoolean(emisKeeper.getInstance().getEmisPropMap().get("IS_LABLE_PRINTER"))) {
        return;
      }
    }
  }

  /*
   * 设置二维码
   */
  public void setQRCODE(String sQRCODE) {
  }

  public String getQRCODE() {
    return "";
  }

  /* 添加一维码至列表中，注意同一内容切勿多次设置，以免重复列印 */
  public void addBARCODE(String sBARCODE) {
  }

  /* 设置一维码 */
  public void setTicketCodes(List<String[]> ticketCodes) {
  }
}