package com.emis.venus.synPosData;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class emisBMSynDataUtils {

  /**
   * map资料转String(key value键值对)
   *
   * @param map
   * @return
   */
  protected static String map2String(Map<String, Object> map) {
    StringBuffer signUrl = new StringBuffer();
    int i = 0;
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (i++ > 0) {
        signUrl.append("&");
      }
      signUrl.append(entry.getKey());
      signUrl.append("=");
      signUrl.append(entry.getValue());

    }
    return signUrl.toString();
  }

  /**
   * 向指定URL发送GET方法的请求
   */
  protected static String sendGet(String url) {
    StringBuffer result = new StringBuffer("");
    BufferedReader in = null;
    try {
      URL realUrl = new URL(url);
      // 打开和URL之间的连接
      URLConnection connection = realUrl.openConnection();
      // 设置通用的请求属性
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty("Charset", "UTF-8");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // 建立实际的连接
      connection.connect();
      // 定义 BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
      String line;
      while ((line = in.readLine()) != null) {
        result.append(line);
      }
    } catch (Exception e) {
      System.out.println("发送GET请求出现异常！" + e);
      e.printStackTrace();
    }
    // 使用finally块来关闭输入流
    finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    return result.toString();
  }

  /**
   * 向指定url发送post请求
   *
   * @param url
   * @param params
   * @return
   */
  protected static String sendPost(String url, String params) {
    OutputStream outputStream = null;
    BufferedReader in = null;
    StringBuffer result = new StringBuffer();
    try {
      URL realUrl = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

      // 设置通用的请求属性
      conn.setRequestMethod("POST");
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("Charset", "UTF-8");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // Post 请求不能使用缓存
      conn.setUseCaches(false);
      // 发送POST请求必须设置如下两行
      conn.setDoOutput(true);
      conn.setDoInput(true);
      // 获取URLConnection对象对应的输出流
      outputStream = conn.getOutputStream();
      // 发送请求参数
      outputStream.write(params.getBytes("UTF-8"));
      outputStream.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
      String line;
      while ((line = in.readLine()) != null) {
        result.append(line + "\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }//使用完毕关闭流
    finally {
      try {
        if (outputStream != null) {
          outputStream.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return result.toString();
  }

  /**
   * 向指定url发送post请求
   *
   * @param url
   * @param map
   * @return
   */
  protected static String sendPost(String url, Map<String, Object> map) {
    String params = map2String(map);
    return sendPost(url, params);
  }

  /**
   * 向指定url发送post请求
   *
   * @param url
   * @param parts
   * @return
   */
  protected static String sendPost(String url, Part[] parts) {
    HttpClient _oClient = null;
    int _iStatus = 0;
    try {
      _oClient = new HttpClient();
      PostMethod method = new PostMethod(url);

      _oClient.getParams().setContentCharset("UTF-8");
      method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
      method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

      method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

      _iStatus = _oClient.executeMethod(method);
      System.out.println("_iStatus:" + _iStatus);
      //Response.ok(method.getResponseBodyAsString(), MediaType.APPLICATION_JSON).build();
      return method.getResponseBodyAsString();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "";
  }
}
