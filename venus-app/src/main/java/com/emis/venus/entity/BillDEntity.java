package com.emis.venus.entity;

import java.io.Serializable;

public class BillDEntity implements Serializable {
  String SL_KEY;
  int RECNO;
  String S_NO;
  String ID_NO;
  String SL_DATE;
  String SL_NO;
  String FLS_NO;
  String P_NO;
  String DP_NO;
  String P_TAX;
  int SL_QTY;
  Double SL_PRICE;
  Double SL_AMT;
  Double SL_TAXAMT;
  Double SL_NOTAXAMT;
  Double SL_DISC_AMT;
  Double SL_NDISC_AMT;
  int RECNO_CCR;
  Double P_PRICE;
  String PK_RECNO;
  Double SL_TAXAMT_AMT;
  String REMARK;
  String SL_MEMO;
  String DD_NO;
  String SYS_DISC;
  Double SYS_DISC_AMT;
  String SYS_DIS_AMT_D;
  Double SL_TAX_AMT;
  String SEA_NO;
  Double SEA_AMT;
  String P_NAME;

  public BillDEntity() {
    SL_KEY = "";
    RECNO = 0;
    S_NO = "";
    ID_NO = "";
    SL_DATE = "";
    SL_NO = "";
    FLS_NO = "";
    P_NO = "";
    DP_NO = "";
    P_TAX = "";
    SL_QTY = 0;
    SL_PRICE = 0.0;
    SL_AMT = 0.0;
    SL_TAXAMT = 0.0;
    SL_NOTAXAMT = 0.0;
    SL_DISC_AMT = 0.0;
    SL_NDISC_AMT = 0.0;
    RECNO_CCR = 0;
    P_PRICE = 0.0;
    PK_RECNO = "";
    SL_TAXAMT_AMT = 0.0;
    REMARK = "";
    SL_MEMO = "";
    DD_NO = "";
    SYS_DISC = "";
    SYS_DISC_AMT = 0.0;
    SYS_DIS_AMT_D = "";
    SL_TAX_AMT = 0.0;
    SEA_NO = "";
    SEA_AMT = 0.0;
    P_NAME = "";
  }

  public String getSL_KEY() {
    return SL_KEY;
  }

  public void setSL_KEY(String SL_KEY) {
    this.SL_KEY = SL_KEY;
  }

  public int getRECNO() {
    return RECNO;
  }

  public void setRECNO(int RECNO) {
    this.RECNO = RECNO;
  }

  public String getS_NO() {
    return S_NO;
  }

  public void setS_NO(String s_NO) {
    S_NO = s_NO;
  }

  public String getID_NO() {
    return ID_NO;
  }

  public void setID_NO(String ID_NO) {
    this.ID_NO = ID_NO;
  }

  public String getSL_DATE() {
    return SL_DATE;
  }

  public void setSL_DATE(String SL_DATE) {
    this.SL_DATE = SL_DATE;
  }

  public String getSL_NO() {
    return SL_NO;
  }

  public void setSL_NO(String SL_NO) {
    this.SL_NO = SL_NO;
  }

  public String getFLS_NO() {
    return FLS_NO;
  }

  public void setFLS_NO(String FLS_NO) {
    this.FLS_NO = FLS_NO;
  }

  public String getP_NO() {
    return P_NO;
  }

  public void setP_NO(String p_NO) {
    P_NO = p_NO;
  }

  public String getDP_NO() {
    return DP_NO;
  }

  public void setDP_NO(String DP_NO) {
    this.DP_NO = DP_NO;
  }

  public String getP_TAX() {
    return P_TAX;
  }

  public void setP_TAX(String p_TAX) {
    P_TAX = p_TAX;
  }

  public int getSL_QTY() {
    return SL_QTY;
  }

  public void setSL_QTY(int SL_QTY) {
    this.SL_QTY = SL_QTY;
  }

  public Double getSL_PRICE() {
    return SL_PRICE;
  }

  public void setSL_PRICE(Double SL_PRICE) {
    this.SL_PRICE = SL_PRICE;
  }

  public Double getSL_AMT() {
    return SL_AMT;
  }

  public void setSL_AMT(Double SL_AMT) {
    this.SL_AMT = SL_AMT;
  }

  public Double getSL_TAXAMT() {
    return SL_TAXAMT;
  }

  public void setSL_TAXAMT(Double SL_TAXAMT) {
    this.SL_TAXAMT = SL_TAXAMT;
  }

  public Double getSL_NOTAXAMT() {
    return SL_NOTAXAMT;
  }

  public void setSL_NOTAXAMT(Double SL_NOTAXAMT) {
    this.SL_NOTAXAMT = SL_NOTAXAMT;
  }

  public double getSL_DISC_AMT() {
    return SL_DISC_AMT;
  }

  public void setSL_DISC_AMT(Double SL_DISC_AMT) {
    this.SL_DISC_AMT = SL_DISC_AMT;
  }

  public Double getSL_NDISC_AMT() {
    return SL_NDISC_AMT;
  }

  public void setSL_NDISC_AMT(Double SL_NDISC_AMT) {
    this.SL_NDISC_AMT = SL_NDISC_AMT;
  }

  public int getRECNO_CCR() {
    return RECNO_CCR;
  }

  public void setRECNO_CCR(int RECNO_CCR) {
    this.RECNO_CCR = RECNO_CCR;
  }

  public Double getP_PRICE() {
    return P_PRICE;
  }

  public void setP_PRICE(Double p_PRICE) {
    P_PRICE = p_PRICE;
  }

  public String getPK_RECNO() {
    return PK_RECNO;
  }

  public void setPK_RECNO(String PK_RECNO) {
    this.PK_RECNO = PK_RECNO;
  }

  public Double getSL_TAXAMT_AMT() {
    return SL_TAXAMT_AMT;
  }

  public void setSL_TAXAMT_AMT(Double SL_TAXAMT_AMT) {
    this.SL_TAXAMT_AMT = SL_TAXAMT_AMT;
  }

  public String getREMARK() {
    return REMARK;
  }

  public void setREMARK(String REMARK) {
    this.REMARK = REMARK;
  }

  public String getSL_MEMO() {
    return SL_MEMO;
  }

  public void setSL_MEMO(String SL_MEMO) {
    this.SL_MEMO = SL_MEMO;
  }

  public String getDD_NO() {
    return DD_NO;
  }

  public void setDD_NO(String DD_NO) {
    this.DD_NO = DD_NO;
  }

  public String getSYS_DISC() {
    return SYS_DISC;
  }

  public void setSYS_DISC(String SYS_DISC) {
    this.SYS_DISC = SYS_DISC;
  }

  public Double getSYS_DISC_AMT() {
    return SYS_DISC_AMT;
  }

  public void setSYS_DISC_AMT(Double SYS_DISC_AMT) {
    this.SYS_DISC_AMT = SYS_DISC_AMT;
  }

  public String getSYS_DIS_AMT_D() {
    return SYS_DIS_AMT_D;
  }

  public void setSYS_DIS_AMT_D(String SYS_DIS_AMT_D) {
    this.SYS_DIS_AMT_D = SYS_DIS_AMT_D;
  }

  public Double getSL_TAX_AMT() {
    return SL_TAX_AMT;
  }

  public void setSL_TAX_AMT(Double SL_TAX_AMT) {
    this.SL_TAX_AMT = SL_TAX_AMT;
  }

  public String getSEA_NO() {
    return SEA_NO;
  }

  public void setSEA_NO(String SEA_NO) {
    this.SEA_NO = SEA_NO;
  }

  public Double getSEA_AMT() {
    return SEA_AMT;
  }

  public void setSEA_AMT(Double SEA_AMT) {
    this.SEA_AMT = SEA_AMT;
  }

  public String getP_NAME() {
    return P_NAME;
  }

  public void setP_NAME(String p_NAME) {
    P_NAME = p_NAME;
  }
}
