package com.emis.venus.util.string;

/**
 * 格式化字符对象
 **/
public class FormatFld {
  /**
   * 字符内容
   */
  private String content;
  /**
   * 对齐方式，（左"L",右"R",""）
   */
  private String align;

  /**
   * 宽度（中文字个数），英文数量为：2*iFldWidth
   */
  private int iFldWidth;

  /**
   * 切割后得到的字符串数值，有多个就有多少行
   */
  private String[] lins = null;

  /**
   * 字体大小，如果一行中字体大小有不一样，就直接按各自字体，不考虑换行了
   */
  private String fontSize;
  /**
   * 字体是否加大
   */
  private boolean bold;
  /**
   * 字体是否斜体
   */
  private boolean italic;
  /**
   * 宽度 (按英文字符1码一个)
   */
  private int iWidth;
  /**
   * 处理内容, 处理完成后、是否换行<br>
   * 0 表示不换行，<br>
   * -1 表示不换行、并且也每个字段栏位都不会有超出的情况，同时每个栏位字体大小不一样。<br>
   * >0打印本内容后，要换行多少<br>
   */
  private int iTrCnt;
  /**
   * 当前打印的内容类型：S 字符文本 Q 二维码 B条码
   */
  private String contentType;

  public FormatFld() {

  }

  /**
   * @param content   字符内容
   * @param iFldWidth 宽度（中文字个数），英文数量为：2*iFldWidth
   * @param align     对齐方式，（左"L",右"R",""）
   */
  public FormatFld(String content, int iFldWidth, String align) {
    this.content = content;
    if (iFldWidth == 0) {
      iFldWidth = 1;
    }
    this.iFldWidth = iFldWidth;
    this.align = align;
  }

  /**
   * 字符内容
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * 对齐方式（"L","R",""）
   */
  public void setAlign(String align) {
    this.align = align;
  }

  /**
   * 宽度（中文字个数），英文数量为：2*iFldWidth
   */
  public void setiFldWidth(int iFldWidth) {
    if (iFldWidth == 0) {
      iFldWidth = 1;
    }
    this.iFldWidth = iFldWidth;
  }

  /**
   * 字符内容
   */
  public String getContent() {
    return content;
  }

  /**
   * 对齐方式（"L","R",""）
   */
  public String getAlign() {
    return align;
  }

  /**
   * 宽度（中文字个数），英文数量为：2*iFldWidth
   */
  public int getiFldWidth() {
    return iFldWidth;
  }

  /**
   * 切割后得到的字符串数值，有多个就有多少行
   */
  public String[] getLins() {
    return lins;
  }

  /**
   * 切割后得到的字符串数值，有多个就有多少行
   */
  public void setLins(String[] lins) {
    this.lins = lins;
  }

  public String getFontSize() {
    return fontSize;
  }

  public void setFontSize(String fontSize) {
    this.fontSize = fontSize;
  }

  public boolean isBold() {
    return bold;
  }

  public void setBold(boolean bold) {
    this.bold = bold;
  }

  public boolean isItalic() {
    return italic;
  }

  public void setItalic(boolean italic) {
    this.italic = italic;
  }

  public int getiWidth() {
    return iWidth;
  }

  public void setiWidth(int iWidth) {
    this.iWidth = iWidth;
  }

  public int getiTrCnt() {
    return iTrCnt;
  }

  public void setiTrCnt(int iTrCnt) {
    this.iTrCnt = iTrCnt;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
}
