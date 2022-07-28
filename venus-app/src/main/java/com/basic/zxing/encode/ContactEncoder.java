package com.basic.zxing.encode;

import android.telephony.PhoneNumberUtils;

import java.util.List;

/**
 * 实现编码根据编码联系信息的一些方案，如VCard或
 * MECARD.
 */
abstract class ContactEncoder {

  /**
   * @return first, the best effort encoding of all data in the appropriate format; second, a
   * display-appropriate version of the contact information
   */
  abstract String[] encode(List<String> names,
                           String organization,
                           List<String> addresses,
                           List<String> phones,
                           List<String> phoneTypes,
                           List<String> emails,
                           List<String> urls,
                           String note);

  /**
   * @return null if s is null or empty, or result of s.trim() otherwise
   */
  static String trim(String s) {
    if (s == null) {
      return null;
    }
    String result = s.trim();
    return result.isEmpty() ? null : result;
  }

  @SuppressWarnings("deprecation")
  static String formatPhone(String phoneData) {
    // Just collect the call to a deprecated method in one place
    return PhoneNumberUtils.formatNumber(phoneData);
  }

}
