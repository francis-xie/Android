package com.emis.venus.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

  /**
   * 密鑰
   */
  private static String secretKey = "UhEUJ738dD7kdf83";
  /**
   * 向量
   */
  private static String iv = "ikeJ7d5KSd2lvUTk";
  /**
   * 加解密統一使用的編碼方式
   */
  private final static String encoding = "utf-8";
  private static boolean isDebug_ = false;

  public static String encrypt(String key, String initVector, String value) {
    try {
      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

      byte[] encrypted = cipher.doFinal(value.getBytes());
      //System.out.println("encrypted string: "
      //    + Base64.encodeBase64String(encrypted));

      return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static String encrypt(String value) {
    return encrypt(secretKey, iv, value);
  }

  public static String decrypt(String key, String initVector, String encrypted) {
    try {
      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

      byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP));

      return new String(original);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static String decrypt(String encrypted) {
    return decrypt(secretKey, iv, encrypted);
  }

  public static void setDebug(boolean isDebug) {
    isDebug_ = isDebug;
  }

  public static void main(String[] args) throws Exception {
    String key = "UhEUJ738dD7kdf83";
    String[] _aPlainText = {"0002720000000203", "0002720000000005", "0002720493090002", "0002720000000104",};

    for (int i = 0; i < _aPlainText.length; i++) {
      String _sPlainText = _aPlainText[i];
//      String str = AES.encrypt(key, iv, _sPlainText);
      String str = AES.encrypt(_sPlainText);
      System.out.print("[" + i + "]" + _sPlainText + " " + str);
//      String decrypted = AES.decrypt(key, iv, str);
      String decrypted = AES.decrypt(str);
      System.out.println(" " + decrypted);
    }

    String[] _aEncrypted = {"pGPlwPycox8DKr4xvhXvelmcYPQj+SJ4PYsTzaJD+SM=", "eZvgce5v0ka5YCeR5Xuqp0kR/QB3Ih/pLpyOt3qm3nE=", "Wd/l+XmSkPZAPtMWUje8YcB2IxYBp/uj38nL4Oa2xfI=", "mp5hiG/j0zqgtedPpc7Su8wbC8Q95lGci9sueUfqntc=",};

    for (int i = 0; i < _aEncrypted.length; i++) {
      String _sEncrypted = _aEncrypted[i];
//     String decrypted = AES.decrypt(key, iv, _sEncrypted);
      String decrypted = AES.decrypt(_sEncrypted);
      System.out.println("[" + i + "]" + decrypted + " " + _sEncrypted);
    }
    System.out.println("@@AES.java.main #103:" + AES.decrypt("PDImiy1s2qevlMNygsEt7Q=="));
  }

}
