package com.emis.venus.hardware.printer;

import java.util.List;

/**
 * 打印机打印接口
 *
 * @author Viva
 */
public interface PrinterInf {

  /**
   * 字体放大的标识符
   */
  public final static String BIG_FONT_MARK = "####B####";

  /**
   * 切纸的标识符
   */
  public final static String CUT_PAGE_MARK = "####N####";

  /**
   * 盖章的标识符
   */
  public final static String STAMP_MARK = "####S####";


  /**
   * 字体倍高倍宽的标识符
   */
  public final static String BIG_FONT_1_MARK = "####B1####";

  /**
   * 字体倍高倍宽的标识符
   */
  public final static String BIG_FONT_2_MARK = "####B2####";

  /**
   * 小票合计标示符
   */
  public final static String SUM_MARK = "####SUM####";

  /**
   * 初始化
   *
   * @throws Exception
   */
  public abstract void initPrinter();

  /**
   * 每次列印前初始化
   */
  public abstract void init() throws Exception;

  /**
   * 切纸
   */
  public abstract void cutPage() throws Exception;

  /**
   * 跳行
   */
  public abstract void jumpPage() throws Exception;

  /**
   * 跳行
   */
  public abstract void jumpRows(int rows) throws Exception;

  /**
   * 还原字体
   */
  public abstract void revertFont() throws Exception;

  /**
   * 放大字体
   */
  public abstract void bigFont() throws Exception;

  /**
   * 字体倍高倍宽
   */
  public abstract void bigFont1() throws Exception;

  /**
   * 开钱箱
   */
  public abstract void openDesk() throws Exception;

  /**
   * 盖章
   */
  public abstract void stamp() throws Exception;

  /**
   * 列印
   */
  public abstract void print(String printStr) throws Exception;

  public abstract void print(String printStr, int iMaxLen) throws Exception;

  /**
   * 列印不切纸
   */
  public abstract void printNoCut(String printStr) throws Exception;

  public abstract void printNoCut(String printStr, int iMaxLen) throws Exception;

  /**
   * 列印(自定义格式列印的方法，适用于windows打印机)
   */
  public abstract void print() throws Exception;

  /**
   * 是否需要继续运行
   *
   * @return
   * @throws Exception
   */
  public abstract boolean isNeedRun() throws Exception;

  /**
   * 关闭
   */
  public abstract void close();

  /**
   * 检测是否连线上
   *
   * @return
   * @throws Exception
   */
  public abstract boolean checkOnline();

  /**
   * 设置二维码
   */
  public abstract void setQRCODE(String sQRCODE);

  /**
   * 得到二维码
   *
   * @return
   */
  public abstract String getQRCODE();

  /**
   * 添加一维码至列表中，注意同一内容切勿多次设置，以免重复列印
   */
  public abstract void addBARCODE(String sBARCODE);

  /**
   * 设置一维码
   */
  public abstract void setTicketCodes(List<String[]> ticketCodes);
}
