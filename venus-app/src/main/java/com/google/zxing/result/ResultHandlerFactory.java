package com.google.zxing.result;

import com.google.zxing.Result;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

/**
 * 基于条形码内容的类型制造特定于android的处理程序。
 */
public final class ResultHandlerFactory {
  private ResultHandlerFactory() {
  }

  public static ResultHandler makeResultHandler(CaptureActivity activity, Result rawResult) {
    ParsedResult result = parseResult(rawResult);
    switch (result.getType()) {
      default:
        return new TextResultHandler(activity, result, rawResult);
    }
  }

  private static ParsedResult parseResult(Result rawResult) {
    return ResultParser.parseResult(rawResult);
  }
}
