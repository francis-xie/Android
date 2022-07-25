package com.google.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.emis.venus.R;
import com.google.zxing.*;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.clipboard.ClipboardInterface;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.decode.DecodeFormatManager;
import com.google.zxing.decode.DecodeHintManager;
import com.google.zxing.decode.RGBLuminanceSource;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.result.ResultButtonListener;
import com.google.zxing.result.ResultHandler;
import com.google.zxing.result.ResultHandlerFactory;
import com.google.zxing.share.ShareActivity;
import com.google.zxing.util.*;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Map;

public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

  private static final String TAG = CaptureActivity.class.getSimpleName();

  private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
  private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

  private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES =
    EnumSet.of(ResultMetadataType.ISSUE_NUMBER,
      ResultMetadataType.SUGGESTED_PRICE,
      ResultMetadataType.ERROR_CORRECTION_LEVEL,
      ResultMetadataType.POSSIBLE_COUNTRY);

  private CameraManager cameraManager;
  private CaptureActivityHandler handler;
  private Result savedResultToShow;
  private ViewfinderView viewfinderView;
  private TextView statusView;
  private View resultView;
  private Result lastResult;
  private boolean hasSurface;
  private boolean copyToClipboard;
  private IntentSource source;
  private String sourceUrl;
  private ScanFromWebPageManager scanFromWebPageManager;
  private Collection<BarcodeFormat> decodeFormats;
  private Map<DecodeHintType, ?> decodeHints;
  private String characterSet;
  private InactivityTimer inactivityTimer;
  private BeepManager beepManager;
  private AmbientLightManager ambientLightManager;

  /**
   * 当活动第一次被创建时调用
   */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //让屏幕保持常亮
    setContentView(R.layout.capture);

    hasSurface = false;
    inactivityTimer = new InactivityTimer(this);
    beepManager = new BeepManager(this);
    ambientLightManager = new AmbientLightManager(this);

    // 偏好设置
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
  }

  /**
   * 当活动可见时调用；这个回调在应用程序与用户开始可交互的时候调用
   */
  @Override
  protected void onResume() {
    super.onResume();
    // cameranager必须在这里初始化，而不是在onCreate()中。这是必要的，因为我们没有
    // 打开相机驱动程序并测量屏幕大小，如果我们要显示帮助
    // 第一次发射。这导致了错误的扫描矩形大小和部分
    // 关闭屏幕。
    cameraManager = new CameraManager(getApplication());

    viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
    viewfinderView.setCameraManager(cameraManager);

    resultView = findViewById(R.id.result_view);
    statusView = (TextView) findViewById(R.id.status_view);

    handler = null;
    lastResult = null;

    // Sharedpreferences是Android平台上一个轻量级的存储类，用来保存应用程序的各种配置信息
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if (prefs.getBoolean(PreferencesActivity.KEY_DISABLE_AUTO_ORIENTATION, true)) {
      setRequestedOrientation(getCurrentOrientation()); // 通过程序改变屏幕显示的方向
    } else {
      // 通过程序改变屏幕显示的方向；ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE 横屏
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT); //竖屏
    }

    resetStatusView();

    beepManager.updatePrefs();
    ambientLightManager.start(cameraManager);

    inactivityTimer.onResume();

    Intent intent = getIntent();

    copyToClipboard = prefs.getBoolean(PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true)
      && (intent == null || intent.getBooleanExtra(Intents.Scan.SAVE_HISTORY, true));

    source = IntentSource.NONE;
    sourceUrl = null;
    scanFromWebPageManager = null;
    decodeFormats = null;
    characterSet = null;

    if (intent != null) {
      String action = intent.getAction();
      String dataString = intent.getDataString();

      if (Intents.Scan.ACTION.equals(action)) {
        // 扫描意图请求的格式，并将结果返回给调用活动。
        source = IntentSource.NATIVE_APP_INTENT;
        decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
        decodeHints = DecodeHintManager.parseDecodeHints(intent);

        if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
          int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
          int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
          if (width > 0 && height > 0) {
            cameraManager.setManualFramingRect(width, height);
          }
        }

        if (intent.hasExtra(Intents.Scan.CAMERA_ID)) {
          int cameraId = intent.getIntExtra(Intents.Scan.CAMERA_ID, -1);
          if (cameraId >= 0) {
            cameraManager.setManualCameraId(cameraId);
          }
        }

        String customPromptMessage = intent.getStringExtra(Intents.Scan.PROMPT_MESSAGE);
        if (customPromptMessage != null) {
          statusView.setText(customPromptMessage);
        }
      }

      characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
    }

    // View适用于主动更新的情况，而SurfaceView适用于被动更新，如频繁刷新，这是因为如果使用View频繁刷新会阻塞主线程，导致界面卡顿
    // SurfaceView 提供嵌入视图层次结构内部的专用绘图表面
    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
    SurfaceHolder surfaceHolder = surfaceView.getHolder();
    if (hasSurface) {
      // 活动暂停了，但没有停止，因此表面仍然存在。因此
      // surfaceCreated()不会被调用，所以在这里初始化相机。
      initCamera(surfaceHolder);
    } else {
      // 安装回调函数并等待surfaceCreated()初始化相机。
      surfaceHolder.addCallback(this);
    }
  }

  /**
   * 获取屏幕旋转的方向
   *
   * @return
   */
  private int getCurrentOrientation() {
    int rotation = getWindowManager().getDefaultDisplay().getRotation(); //获取屏幕旋转的方向
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      switch (rotation) {
        case Surface.ROTATION_0:
        case Surface.ROTATION_90:
          return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        default:
          return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
      }
    } else {
      switch (rotation) {
        case Surface.ROTATION_0:
        case Surface.ROTATION_270:
          return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        default:
          return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
      }
    }
  }

  /**
   * 当其他活动获得焦点时调用；被暂停的活动无法接受用户输入，不能执行任何代码。当前活动将要被暂停，上一个活动将要被恢复时调用
   */
  @Override
  protected void onPause() {
    if (handler != null) {
      handler.quitSynchronously();
      handler = null;
    }
    inactivityTimer.onPause();
    ambientLightManager.stop();
    beepManager.close();
    cameraManager.closeDriver();
    //historyManager = null; // 保持onActivityResult
    if (!hasSurface) {
      SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
      SurfaceHolder surfaceHolder = surfaceView.getHolder();
      surfaceHolder.removeCallback(this);
    }
    super.onPause();
  }

  /**
   * 当活动将被销毁时调用，当活动被系统销毁之前调用
   */
  @Override
  protected void onDestroy() {
    inactivityTimer.shutdown();
    super.onDestroy();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // 监听手机屏幕上的按键
    switch (keyCode) {
      case KeyEvent.KEYCODE_BACK: //返回键
        if (source == IntentSource.NATIVE_APP_INTENT) {
          setResult(RESULT_CANCELED);
          finish();
          return true;
        }
        if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
          restartPreviewAfterDelay(0L);
          return true;
        }
        break;
      case KeyEvent.KEYCODE_ENTER: //回车键
      case KeyEvent.KEYCODE_NUMPAD_ENTER: //小键盘按键回车
        break;
      case KeyEvent.KEYCODE_FOCUS: //拍照对焦键
      case KeyEvent.KEYCODE_CAMERA: //拍照键
        // Handle these events so they don't launch the Camera app
        return true;
      // Use volume up/down to turn on light
      case KeyEvent.KEYCODE_VOLUME_DOWN: //音量减小键
        cameraManager.setTorch(false);
        return true;
      case KeyEvent.KEYCODE_VOLUME_UP: //音量增加键
        cameraManager.setTorch(true);
        return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // 创建Menu菜单的项目
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.capture, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // 处理菜单被选中运行后的事件处理
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.addFlags(Intents.FLAG_NEW_DOC);
    switch (item.getItemId()) {
      case R.id.menu_images:
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
        break;
      case R.id.menu_share:
        intent.setClassName(this, ShareActivity.class.getName());
        startActivity(intent);
        break;
      /*case R.id.menu_history:
        intent.setClassName(this, HistoryActivity.class.getName());
        startActivityForResult(intent, HISTORY_REQUEST_CODE);
        break;*/
      case R.id.menu_settings:
        intent.setClassName(this, PreferencesActivity.class.getName());
        startActivity(intent);
        break;
      default:
        return super.onOptionsItemSelected(item);
    }
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    // onActivityResult的用法：ActivityA调用startActivityForResult(intent, requestCode)启动了ActivityB；
    // 当ActivityB给ActivityA回复时调用setResult设置返回的结果，然后调用finish；最后在ActivityA的onActivityResult处理B的返回结果；
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case 100:
          handlePic(intent);
          break;
      }
    }
  }

  /**
   * 处理选择的图片
   *
   * @param data
   */
  private void handlePic(Intent data) {
    //获取选中图片的路径
    final Uri uri = data.getData();

    ProgressDialog mProgress = new ProgressDialog(CaptureActivity.this);
    mProgress.setMessage("正在扫描...");
    mProgress.setCancelable(false);
    mProgress.show();

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Result result = scanningImage(uri);
        mProgress.dismiss();
        if (result != null) {
          decodeOrStoreSavedBitmap(null, result);

          /*Intent resultIntent = new Intent();
          Bundle bundle = new Bundle();
          bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN ,result.getText());
          bundle.putParcelable("bitmap",result.get);
          resultIntent.putExtras(bundle);
          setResult(RESULT_OK, resultIntent);
          finish();*/
        } else {
          Toast.makeText(CaptureActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  /**
   * 扫描二维码图片的方法
   *
   * @param uri
   * @return
   */
  public Result scanningImage(Uri uri) {
    if (uri == null) {
      return null;
    }
    Hashtable<DecodeHintType, String> hints = new Hashtable<>();
    hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

    Bitmap scanBitmap = BitmapUtil.decodeUri(this, uri, 500, 500);
    RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    QRCodeReader reader = new QRCodeReader();
    try {
      return reader.decode(bitmap, hints);
    } catch (NotFoundException e) {
      e.printStackTrace();
    } catch (ChecksumException e) {
      e.printStackTrace();
    } catch (FormatException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
    // 位图还没有使用——很快就会使用
    if (handler == null) {
      savedResultToShow = result;
    } else {
      if (result != null) {
        savedResultToShow = result;
      }
      if (savedResultToShow != null) {
        Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
        handler.sendMessage(message);
      }
      savedResultToShow = null;
    }
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    // 第一次创建Surface后会立即调用该函数
    if (holder == null) {
      Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
    }
    if (!hasSurface) {
      hasSurface = true; //设置绘制状态
      initCamera(holder);
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    // Surface被摧毁前会调用该函数
    hasSurface = false;
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    // Surface的状态发生变化的时候会调用该函数
  }

  /**
   * 已经找到了有效的条形码，因此给出成功的指示并显示结果。
   *
   * @param rawResult   条形码的内容
   * @param scaleFactor 缩略图缩放的数量
   * @param barcode     一个被解码的相机数据的灰度位图
   */
  public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
    inactivityTimer.onActivity();
    lastResult = rawResult;

    //返回扫码结果
    //handleDecodeResult();

    //显示扫码结果
    bindResultHandler(rawResult, barcode, scaleFactor);
  }

  private void handleDecodeResult() {
    if (lastResult == null) return;
    String resultString = lastResult.getText();
    if (TextUtils.isEmpty(resultString)) {
      Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
    } else {
      System.out.println("sssssssssssssssss scan 0 = " + resultString);
      Intent resultIntent = new Intent();
      //Bundle bundle = new Bundle();
      //bundle.putString("result", resultString);
      // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
      //bundle.putParcelable("bitmap", barcode);
      //Logger.d("saomiao",resultString);
      //resultIntent.putExtras(bundle);
      resultIntent.putExtra("P_NO", resultString);
      setResult(RESULT_OK, resultIntent);
    }
    finish();
  }

  private void bindResultHandler(Result rawResult, Bitmap barcode, float scaleFactor) {
    ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);

    boolean fromLiveScan = barcode != null;
    if (fromLiveScan) {
      // 然后不是从历史中来的，所以哔/振动，我们有一个图像来借鉴
      beepManager.playBeepSoundAndVibrate();
      drawResultPoints(barcode, scaleFactor, rawResult);
    }

    switch (source) {
      case NATIVE_APP_INTENT:
      case PRODUCT_SEARCH_LINK:
        handleDecodeExternally(rawResult, resultHandler, barcode);
        break;
      case ZXING_LINK:
        if (scanFromWebPageManager == null || !scanFromWebPageManager.isScanFromWebPage()) {
          handleDecodeInternally(rawResult, resultHandler, barcode);
        } else {
          handleDecodeExternally(rawResult, resultHandler, barcode);
        }
        break;
      case NONE:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (fromLiveScan && prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
          Toast.makeText(getApplicationContext(),
            getResources().getString(R.string.msg_bulk_mode_scanned) + " (" + rawResult.getText() + ')',
            Toast.LENGTH_SHORT).show();
          maybeSetClipboard(resultHandler);
          // 稍等片刻，否则它将连续扫描相同的条形码约3次
          restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
        } else {
          handleDecodeInternally(rawResult, resultHandler, barcode);
        }
        break;
    }
  }

  /**
   * 叠加1D的线或2D的点，以突出条形码的关键特征
   *
   * @param barcode     捕获图像的位图
   * @param scaleFactor 缩略图缩放的数量
   * @param rawResult   包含要绘制的点的解码结果
   */
  private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
    ResultPoint[] points = rawResult.getResultPoints();
    if (points != null && points.length > 0) {
      Canvas canvas = new Canvas(barcode);
      Paint paint = new Paint();
      paint.setColor(getResources().getColor(R.color.result_points));
      if (points.length == 2) {
        paint.setStrokeWidth(4.0f);
        drawLine(canvas, paint, points[0], points[1], scaleFactor);
      } else if (points.length == 4 &&
        (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
          rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
        // Hacky special case -- draw two lines, for the barcode and metadata
        drawLine(canvas, paint, points[0], points[1], scaleFactor);
        drawLine(canvas, paint, points[2], points[3], scaleFactor);
      } else {
        paint.setStrokeWidth(10.0f);
        for (ResultPoint point : points) {
          if (point != null) {
            canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
          }
        }
      }
    }
  }

  private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
    if (a != null && b != null) {
      canvas.drawLine(scaleFactor * a.getX(),
        scaleFactor * a.getY(),
        scaleFactor * b.getX(),
        scaleFactor * b.getY(),
        paint);
    }
  }

  // 建立我们自己的UI来处理解码的内容
  private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
    // 显示扫描结果
    maybeSetClipboard(resultHandler);

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    if (resultHandler.getDefaultButtonID() != null && prefs.getBoolean(PreferencesActivity.KEY_AUTO_OPEN_WEB, false)) {
      resultHandler.handleButtonPress(resultHandler.getDefaultButtonID());
      return;
    }

    statusView.setVisibility(View.GONE);
    viewfinderView.setVisibility(View.GONE);
    resultView.setVisibility(View.VISIBLE);

    ImageView barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view); // 扫码截取的图片
    if (barcode == null) {
      barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.launcher_icon));
    } else {
      barcodeImageView.setImageBitmap(barcode);
    }

    TextView formatTextView = (TextView) findViewById(R.id.format_text_view); //格式
    formatTextView.setText(rawResult.getBarcodeFormat().toString());

    TextView typeTextView = (TextView) findViewById(R.id.type_text_view); //类型
    typeTextView.setText(resultHandler.getType().toString());

    DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    TextView timeTextView = (TextView) findViewById(R.id.time_text_view); //时间
    timeTextView.setText(formatter.format(rawResult.getTimestamp()));

    TextView metaTextView = (TextView) findViewById(R.id.meta_text_view); //元数据
    View metaTextViewLabel = findViewById(R.id.meta_text_view_label);
    metaTextView.setVisibility(View.GONE);
    metaTextViewLabel.setVisibility(View.GONE);
    Map<ResultMetadataType, Object> metadata = rawResult.getResultMetadata();
    if (metadata != null) {
      StringBuilder metadataText = new StringBuilder(20);
      for (Map.Entry<ResultMetadataType, Object> entry : metadata.entrySet()) {
        if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
          metadataText.append(entry.getValue()).append('\n');
        }
      }
      if (metadataText.length() > 0) {
        metadataText.setLength(metadataText.length() - 1);
        metaTextView.setText(metadataText);
        metaTextView.setVisibility(View.VISIBLE);
        metaTextViewLabel.setVisibility(View.VISIBLE);
      }
    }

    CharSequence displayContents = resultHandler.getDisplayContents();
    TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
    contentsTextView.setText(displayContents);
    int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
    contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

    TextView supplementTextView = (TextView) findViewById(R.id.contents_supplement_text_view);
    supplementTextView.setText("");
    supplementTextView.setOnClickListener(null);

    int buttonCount = resultHandler.getButtonCount();
    ViewGroup buttonView = (ViewGroup) findViewById(R.id.result_button_view);
    buttonView.requestFocus();
    for (int x = 0; x < ResultHandler.MAX_BUTTON_COUNT; x++) {
      TextView button = (TextView) buttonView.getChildAt(x);
      if (x < buttonCount) {
        button.setVisibility(View.VISIBLE);
        button.setText(resultHandler.getButtonText(x));
        button.setOnClickListener(new ResultButtonListener(resultHandler, x));
      } else {
        button.setVisibility(View.GONE);
      }
    }

  }

  // 简单地显示条形码的内容，然后在条形码扫描器之外处理结果
  private void handleDecodeExternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
    if (barcode != null) {
      viewfinderView.drawResultBitmap(barcode);
    }

    long resultDurationMS;
    if (getIntent() == null) {
      resultDurationMS = DEFAULT_INTENT_RESULT_DURATION_MS;
    } else {
      resultDurationMS = getIntent().getLongExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS,
        DEFAULT_INTENT_RESULT_DURATION_MS);
    }

    if (resultDurationMS > 0) {
      String rawResultString = String.valueOf(rawResult);
      if (rawResultString.length() > 32) {
        rawResultString = rawResultString.substring(0, 32) + " ...";
      }
      statusView.setText(getString(resultHandler.getDisplayTitle()) + " : " + rawResultString);
    }

    maybeSetClipboard(resultHandler);

    switch (source) {
      case NATIVE_APP_INTENT:
        // Hand back whatever action they requested - this can be changed to Intents.Scan.ACTION when
        // the deprecated intent is retired.
        Intent intent = new Intent(getIntent().getAction());
        intent.addFlags(Intents.FLAG_NEW_DOC);
        intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
        intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
        byte[] rawBytes = rawResult.getRawBytes();
        if (rawBytes != null && rawBytes.length > 0) {
          intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
        }
        Map<ResultMetadataType, ?> metadata = rawResult.getResultMetadata();
        if (metadata != null) {
          if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
            intent.putExtra(Intents.Scan.RESULT_UPC_EAN_EXTENSION,
              metadata.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
          }
          Number orientation = (Number) metadata.get(ResultMetadataType.ORIENTATION);
          if (orientation != null) {
            intent.putExtra(Intents.Scan.RESULT_ORIENTATION, orientation.intValue());
          }
          String ecLevel = (String) metadata.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
          if (ecLevel != null) {
            intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, ecLevel);
          }
          @SuppressWarnings("unchecked")
          Iterable<byte[]> byteSegments = (Iterable<byte[]>) metadata.get(ResultMetadataType.BYTE_SEGMENTS);
          if (byteSegments != null) {
            int i = 0;
            for (byte[] byteSegment : byteSegments) {
              intent.putExtra(Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX + i, byteSegment);
              i++;
            }
          }
        }
        sendReplyMessage(R.id.return_scan_result, intent, resultDurationMS);
        break;

      case PRODUCT_SEARCH_LINK:
        // Reformulate the URL which triggered us into a query, so that the request goes to the same
        // TLD as the scan URL.
        int end = sourceUrl.lastIndexOf("/scan");
        String productReplyURL = sourceUrl.substring(0, end) + "?q=" +
          resultHandler.getDisplayContents() + "&source=zxing";
        sendReplyMessage(R.id.launch_product_query, productReplyURL, resultDurationMS);
        break;

      case ZXING_LINK:
        if (scanFromWebPageManager != null && scanFromWebPageManager.isScanFromWebPage()) {
          String linkReplyURL = scanFromWebPageManager.buildReplyURL(rawResult, resultHandler);
          scanFromWebPageManager = null;
          sendReplyMessage(R.id.launch_product_query, linkReplyURL, resultDurationMS);
        }
        break;
    }
  }

  private void maybeSetClipboard(ResultHandler resultHandler) {
    if (copyToClipboard && !resultHandler.areContentsSecure()) {
      ClipboardInterface.setText(resultHandler.getDisplayContents(), this);
    }
  }

  private void sendReplyMessage(int id, Object arg, long delayMS) {
    if (handler != null) {
      Message message = Message.obtain(handler, id, arg);
      if (delayMS > 0L) {
        handler.sendMessageDelayed(message, delayMS);
      } else {
        handler.sendMessage(message);
      }
    }
  }

  private void initCamera(SurfaceHolder surfaceHolder) {
    if (surfaceHolder == null) {
      throw new IllegalStateException("No SurfaceHolder provided");
    }
    if (cameraManager.isOpen()) {
      Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
      return;
    }
    try {
      cameraManager.openDriver(surfaceHolder);
      // 创建处理程序将启动预览，预览也会抛出RuntimeException。
      if (handler == null) {
        handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
      }
      decodeOrStoreSavedBitmap(null, null);
    } catch (IOException ioe) {
      Log.w(TAG, ioe);
      displayFrameworkBugMessageAndExit();
    } catch (RuntimeException e) {
      // Barcode Scanner has seen crashes in the wild of this variety:
      // java.?lang.?RuntimeException: Fail to connect to camera service
      Log.w(TAG, "Unexpected error initializing camera", e);
      displayFrameworkBugMessageAndExit();
    }
  }

  private void displayFrameworkBugMessageAndExit() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(getString(R.string.app_name));
    builder.setMessage(getString(R.string.msg_camera_framework_bug));
    builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
    builder.setOnCancelListener(new FinishListener(this));
    builder.show();
  }

  public void restartPreviewAfterDelay(long delayMS) {
    if (handler != null) {
      handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
    }
    resetStatusView();
  }

  private void resetStatusView() {
    resultView.setVisibility(View.GONE);
    statusView.setText(R.string.msg_default_status);
    statusView.setVisibility(View.VISIBLE);
    viewfinderView.setVisibility(View.VISIBLE);
    lastResult = null;
  }

  public void drawViewfinder() {
    viewfinderView.drawViewfinder();
  }

  public ViewfinderView getViewfinderView() {
    return viewfinderView;
  }

  public Handler getHandler() {
    return handler;
  }

  public CameraManager getCameraManager() {
    return cameraManager;
  }
}
