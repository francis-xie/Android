package com.google.zxing.util;

import android.net.Uri;
import com.google.zxing.Result;
import com.google.zxing.result.ResultHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 管理与响应来自网页中的HTTP链接的扫描请求相关的功能。
 * 看到 <a href="https://github.com/zxing/zxing/wiki/ScanningFromWebPages">ScanningFromWebPages</a>.
 */
public final class ScanFromWebPageManager {

  private static final CharSequence CODE_PLACEHOLDER = "{CODE}";
  private static final CharSequence RAW_CODE_PLACEHOLDER = "{RAWCODE}";
  private static final CharSequence META_PLACEHOLDER = "{META}";
  private static final CharSequence FORMAT_PLACEHOLDER = "{FORMAT}";
  private static final CharSequence TYPE_PLACEHOLDER = "{TYPE}";

  private static final String RETURN_URL_PARAM = "ret";
  private static final String RAW_PARAM = "raw";

  private final String returnUrlTemplate;
  private final boolean returnRaw;

  ScanFromWebPageManager(Uri inputUri) {
    returnUrlTemplate = inputUri.getQueryParameter(RETURN_URL_PARAM);
    returnRaw = inputUri.getQueryParameter(RAW_PARAM) != null;
  }

  public boolean isScanFromWebPage() {
    return returnUrlTemplate != null;
  }

  public String buildReplyURL(Result rawResult, ResultHandler resultHandler) {
    String result = returnUrlTemplate;
    result = replace(CODE_PLACEHOLDER,
      returnRaw ? rawResult.getText() : resultHandler.getDisplayContents(), result);
    result = replace(RAW_CODE_PLACEHOLDER, rawResult.getText(), result);
    result = replace(FORMAT_PLACEHOLDER, rawResult.getBarcodeFormat().toString(), result);
    result = replace(TYPE_PLACEHOLDER, resultHandler.getType().toString(), result);
    result = replace(META_PLACEHOLDER, String.valueOf(rawResult.getResultMetadata()), result);
    return result;
  }

  private static String replace(CharSequence placeholder, CharSequence with, String pattern) {
    CharSequence escapedWith = with == null ? "" : with;
    try {
      escapedWith = URLEncoder.encode(escapedWith.toString(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // can't happen; UTF-8 is always supported. Continue, I guess, without encoding
    }
    return pattern.replace(placeholder, escapedWith);
  }

}
