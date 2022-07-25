package com.emis.venus.report;

import android.content.Context;
import com.emis.venus.common.emisKeeper;
import com.emis.venus.entity.Entity;
import com.emis.venus.hardware.printer.PrinterInf;
import com.emis.venus.hardware.printer.VenusPrinter;
import com.emis.venus.util.emisPropUtil;
import com.emis.venus.util.emisStringCharsetName;
import com.emis.venus.util.emisUtil;

import java.util.HashMap;

/**
 * 结账小票
 */
public class VenusBillReport extends AbstractVenusReport {
  private Entity saleH;
  private Entity saleDTableShow;
  private Entity saleD;
  private boolean isBack = false;
  private String sContactMsg = "";
  private boolean isNeedRun = true;
  private Context oContext_ = null;
  private Entity saleCard;
  private Entity saleCardForGift;
  private Entity saleCardForGiftPM;
  private Entity saleCardForGoodesGift;
  private Entity saleCardForGiftExt;
  private Entity saleCardForEtrade;
  private Entity saleCardForHzsmk;
  private Entity saleCardForVeryPay;
  private Entity saleCardForUnionPay;
  private Entity saleCardForAlittlePay;
  private Entity saleCardForUmsPosPay;
  private Entity saleCardForBankPosPay;
  private Entity saleCardForFuiouPay;
  private Entity saleCardForPinucPay;
  private Entity saleCardForSpdbPay;
  private Entity saleCardForLeXiangPay;
  private Entity saleCardForXYFLPay;
  private Entity saleCardForRoseThreeSys;
  /**
   * 是否外卖小票
   */
  private boolean isWebOrd = false;
  private Entity saleDis;
  private Entity saleCust;
  /**
   * 是否预结单
   */
  private boolean isReview = false;

  /**
   * 是否使用宜芝多特殊券
   */
  boolean IS_USE_ICHIDO_GIFT = false;

  /**
   * 需求 #45524 是否不列印纸质礼券付款记录：false 为列印，true为不打印
   */
  boolean IS_NOT_PRT_GIFT_LIST = false;

  /**
   * 需求 #45524 是否不列印提货券付款记录：false 为列印，true为不打印
   */
  boolean IS_NOT_PRT_GIFT_TK_LIST = false;

  public String getContactMsg() {
    return sContactMsg;
  }

  public void setContactMsg(String sContactMsg) {
    this.sContactMsg = emisUtil.parseString(sContactMsg);
  }

  public boolean isBack() {
    return isBack;
  }

  public void setBack(boolean isBack) {
    this.isBack = isBack;
  }

  /**
   * 是否外卖 外卖: 1 外带: 0 堂食: 3
   */
  boolean isWMBill = false;

  /**
   * 是否外带 2
   */
  boolean isWDBill = false;

  /**
   * 是否非外卖平台的外卖(前台，直接点的外卖)
   */
  boolean isWMBillInSNo = false;
  /**
   * 无选择开票时是否有成功调用单单计电子发票接口 以便打印小票单号
   */
  boolean isDdjRptInv = false;

  public VenusBillReport(Context oContext_) throws Exception {
    this.oContext_ = oContext_;
    this.isNeedPrintQrcode = true;
    this.isNeedPrintLogo = true;
    IS_USE_ICHIDO_GIFT = emisUtil.parseBoolean(emisKeeper.getInstance()
      .getEmisPropMap().get("IS_USE_ICHIDO_GIFT"));
    IS_NOT_PRT_GIFT_LIST = emisUtil.parseBoolean(emisKeeper.getInstance()
      .getEmisPropMap().get("IS_NOT_PRT_GIFT_LIST"));
    IS_NOT_PRT_GIFT_TK_LIST = emisUtil.parseBoolean(emisKeeper
      .getInstance().getEmisPropMap().get("IS_NOT_PRT_GIFT_TK_LIST"));
  }

  /**
   * 初始化打印机<br>
   * 在print方法里调用，有检查小票机 不能乱放
   *
   * @throws Exception
   */
  @Override
  public void initPrinter() {
    printer = new VenusPrinter();
    printer.initPrinter();
    ((VenusPrinter) printer).setNeedPrintQrcode(isNeedPrintQrcode);
    ((VenusPrinter) printer).setNeedPrintLogo(isNeedPrintLogo);
  }

  @Override
  public void createPrintBuf() throws Exception {
    printBuf = new StringBuffer("");

    if (printer != null) {
      try {
        ((VenusPrinter) printer).setNeedPrintQrcode(this.isNeedPrintQrcode);
        ((VenusPrinter) printer).setNeedPrintLogo(this.isNeedPrintLogo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    this.setSL_DATE_S(emisUtil.getSYSDATE());
    this.setSL_KEY(emisUtil.getSYSDATETIME());

    printBuf.append("\r\n");
    printBuf.append(emisUtil.getSYSDATETIME2());
    printBuf.append("\r\n");
    printBuf.append(emisStringCharsetName.replicate('-', this.iMaxLen));

    if (!emisUtil.isEmpty(saleDTableShow)) {
      int curIndex = saleDTableShow.getM_currRow();
      saleDTableShow.first();
      do {
        saleD.move(saleDTableShow.getM_currRow());
      } while (saleDTableShow.next());
      saleDTableShow.move(curIndex);
    }
    printBuf.append("\r\n");
    printBuf.append(emisStringCharsetName.replicate('-', this.iMaxLen));
    printBuf.append("\r\n");

    createEnd();
  }

  private void createEnd() {
    boolean isRptLine = false;
    if (emisUtil.parseBoolean(emisKeeper.getInstance().getEmisPropMap().get("IS_RPT_TEL"))) {
      if (!isRptLine) {
        printBuf.append(emisStringCharsetName.replicate('-',
          this.iMaxLen));
        isRptLine = true;
      }
      printBuf.append("\r\n");
      printBuf.append(emisUtil.parseString(emisKeeper.getInstance().getEmisPropMap().get("COMPANY_TEL")));
    }
    if (emisUtil.parseBoolean(emisKeeper.getInstance().getEmisPropMap().get("IS_RPT_ADDR"))) {
      if (!isRptLine) {
        printBuf.append(emisStringCharsetName.replicate('-',
          this.iMaxLen));
      }
      createAdrr();
    }
    // 列印三行空行
    printBuf.append("\r\n");
//    printBuf.append("\r\n");
//    printBuf.append("\r\n");
  }

  /**
   * 列印公司地址
   */
  private void createAdrr() {
    String sOneLine;
    int iOneLineLen;
    String addr = emisUtil.parseString(emisKeeper.getInstance().getEmisPropMap().get("COMPANY_ADDR"));
    if (!"".equals(addr)) {
      int addrLen = emisStringCharsetName.lengthB(addr, this.billCode);
      printBuf.append("\r\n");
      if (addrLen > 2 * (iMaxLen)) {
        sOneLine = emisStringCharsetName.subStringB(addr, 0, iMaxLen,
          billCode);
        printBuf.append(emisStringCharsetName.rPadB(sOneLine, iMaxLen,
          this.billCode));
        printBuf.append("\r\n");
        iOneLineLen = emisStringCharsetName.lengthB(sOneLine,
          this.billCode);
        sOneLine = emisStringCharsetName.subStringB(addr, iOneLineLen,
          iMaxLen, billCode);
        printBuf.append(emisStringCharsetName.rPadB(sOneLine, iMaxLen,
          this.billCode));
        printBuf.append("\r\n");
        iOneLineLen += emisStringCharsetName.lengthB(sOneLine,
          this.billCode);
        printBuf.append(emisStringCharsetName.rPadB(
          (emisStringCharsetName.subStringB(addr,
            iOneLineLen, addrLen, billCode)), iMaxLen,
          this.billCode));
      } else if (addrLen > (iMaxLen)) {
        sOneLine = emisStringCharsetName.subStringB(addr, 0, iMaxLen,
          billCode);
        printBuf.append(emisStringCharsetName.rPadB(sOneLine, iMaxLen,
          this.billCode));
        printBuf.append("\r\n");
        iOneLineLen = emisStringCharsetName.lengthB(sOneLine,
          this.billCode);
        sOneLine = emisStringCharsetName.subStringB(addr, iOneLineLen,
          iMaxLen, billCode);
        printBuf.append(emisStringCharsetName.rPadB((sOneLine),
          iMaxLen, this.billCode));
      } else {
        printBuf.append(emisStringCharsetName.rPadB(addr, iMaxLen,
          this.billCode));
      }
    }

  }

  /**
   * @param saleH the saleH to set
   */
  public void setSaleH(Entity saleH) {
    this.saleH = saleH;
  }

  /**
   * @return the saleH
   */
  public Entity getSaleH() {
    return saleH;
  }

  /**
   * @param saleD the saleD to set
   */
  public void setSaleD(Entity saleD) {
    this.saleD = saleD;
  }

  /**
   * @return the saleD
   */
  public Entity getSaleD() {
    return saleD;
  }

  /**
   * @param isNeedRun the isNeedRun to set
   */
  public void setNeedRun(boolean isNeedRun) {
    this.isNeedRun = isNeedRun;
  }

  /**
   * @return the isNeedRun
   */
  public boolean isNeedRun() {
    return isNeedRun;
  }

  /**
   * @return the saleDTableShow
   */
  public Entity getSaleDTableShow() {
    return saleDTableShow;
  }

  /**
   * @param saleDTableShow the saleDTableShow to set
   */
  public void setSaleDTableShow(Entity saleDTableShow) {
    this.saleDTableShow = saleDTableShow;
  }

  public void setSaleCard(Entity saleCard) {
    this.saleCard = saleCard;
  }

  public Entity getSaleCard() {
    return saleCard;
  }

  public void setSaleCardForGift(Entity saleCardForGift) {
    this.saleCardForGift = saleCardForGift;
  }

  public Entity getSaleCardForGift() {
    return saleCardForGift;
  }

  public void setSaleCardForGiftPM(Entity saleCardForGiftPM) {
    this.saleCardForGiftPM = saleCardForGiftPM;
  }

  public Entity getSaleCardForGiftPM() {
    return saleCardForGiftPM;
  }

  public void setSaleCardForGoodesGift(Entity saleCardForGoodesGift) {
    this.saleCardForGoodesGift = saleCardForGoodesGift;
  }

  public Entity getSaleCardForGoodesGift() {
    return saleCardForGoodesGift;
  }

  public void setSaleDis(Entity saleDis) {
    this.saleDis = saleDis;
  }

  public Entity getSaleDis() {
    return saleDis;
  }

  public void setSaleCardForGiftExt(Entity saleCardForGiftExt) {
    this.saleCardForGiftExt = saleCardForGiftExt;
  }

  public Entity getSaleCardForGiftExt() {
    return saleCardForGiftExt;
  }

  public Entity getSaleCardForEtrade() {
    return saleCardForEtrade;
  }

  public void setSaleCardForEtrade(Entity saleCardForEtrade) {
    this.saleCardForEtrade = saleCardForEtrade;
  }

  public void setSaleCardForHzsmk(Entity saleCardForHzsmk) {
    this.saleCardForHzsmk = saleCardForHzsmk;
  }

  public void setSaleCardForVeryPay(Entity saleCardForVeryPay) {
    this.saleCardForVeryPay = saleCardForVeryPay;
  }

  public void setSaleCardForUnionPay(Entity saleCardForUnionPay) {
    this.saleCardForUnionPay = saleCardForUnionPay;
  }

  public void setSaleCardForAlittlePay(Entity saleCardForAlittlePay) {
    this.saleCardForAlittlePay = saleCardForAlittlePay;
  }

  public void setSaleCardForUmsPosPay(Entity saleCardForUmsPosPay) {
    this.saleCardForUmsPosPay = saleCardForUmsPosPay;
  }

  public void setSaleCardForBankPosPay(Entity saleCardForBankPosPay) {
    this.saleCardForBankPosPay = saleCardForBankPosPay;
  }

  public void setSaleCardForFuiouPay(Entity saleCardForFuiouPay) {
    this.saleCardForFuiouPay = saleCardForFuiouPay;
  }

  public void setSaleCardForPinucPay(Entity saleCardForPinucPay) {
    this.saleCardForPinucPay = saleCardForPinucPay;
  }

  public void setSaleCardForSpdbPay(Entity saleCardForSpdbPay) {
    this.saleCardForSpdbPay = saleCardForSpdbPay;
  }

  public void setSaleCardForLeXiangPay(Entity saleCardForLeXiangPay) {
    this.saleCardForLeXiangPay = saleCardForLeXiangPay;
  }

  public void setSaleCardForXYFLPay(Entity saleCardForXYFLPay) {
    this.saleCardForXYFLPay = saleCardForXYFLPay;
  }

  public void setSaleCardForRoseThreeSys(Entity saleCardForRoseThreeSys) {
    this.saleCardForRoseThreeSys = saleCardForRoseThreeSys;
  }

  public void setSaleCust(Entity saleCust) {
    this.saleCust = saleCust;
  }

  public Entity getSaleCust() {
    return saleCust;
  }

  public void setReview(boolean isReview) {
    this.isReview = isReview;
  }

  public boolean isReview() {
    return isReview;
  }

  public void setWebOrd(boolean isWebOrd) {
    this.isWebOrd = isWebOrd;
  }

  public boolean isWebOrd() {
    return isWebOrd;
  }

}
