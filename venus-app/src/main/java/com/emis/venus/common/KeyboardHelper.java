package com.emis.venus.common;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.emis.venus.R;
import com.emis.venus.util.KeyboardUtil;
import com.emis.venus.util.emisAndroidUtil;

public class KeyboardHelper {

  public static final int LAST = -7;
  public static final int NEXT = -8;
  public static final int ALL = -9;

  private Context context;
  private KeyboardView keyboardView;
  private EditText editText;
  private Keyboard keyboard;
  private KeyboardCallBack callBack;

  public KeyboardHelper(Context context, KeyboardView keyboardView) {
    this(context, keyboardView, null);
  }

  public KeyboardHelper(Context context, KeyboardView keyboardView, KeyboardCallBack callBack) {

    this.context = context;

    keyboard = new Keyboard(context, R.xml.keyboard);

    this.keyboardView = keyboardView;
    this.keyboardView.setOnKeyboardActionListener(listener);
    this.keyboardView.setKeyboard(keyboard);//设置默认键盘
    this.keyboardView.setEnabled(true);
    this.keyboardView.setPreviewEnabled(false);
    this.callBack = callBack;
  }

  private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

      Editable editable = editText.getText();
      int start = editText.getSelectionStart();
      int end = editText.getSelectionEnd();

      switch (primaryCode) {

        case Keyboard.KEYCODE_DELETE:

          if ((editable != null) && (editable.length() > 0)) {

            // System.out.println("start:" + start);
            // System.out.println("end:" + end);

            if ((start != 0) && (end != 0)) {

              if (start == end) {

                editable.delete(start - 1, start);
              } else {

                editable.delete(start, end);
              }
            }
          }
          break;
        case Keyboard.KEYCODE_CANCEL:
          keyboardView.setVisibility(View.GONE);
          break;
        case ALL:
          editText.selectAll();
          break;
        case LAST:
        case NEXT:
          break;
        case -4:
          hide();
          break;
        default:
          if (start != end) {
            editable.delete(start, end);
          }

          String temp = Character.toString((char) primaryCode);
          editable.insert(start, Character.toString((char) primaryCode));
          emisAndroidUtil.hideKeyboard(editText);
          break;
      }

      if (callBack != null) callBack.keyCall(primaryCode);
    }

    @Override
    public void onText(CharSequence text) {

      Editable editable = editText.getText();
      int end = editText.getSelectionEnd();

      //editable.delete(0, end);
      editable.insert(end, text);
      //KeyboardUtil.hideSoftInput(editText);
      emisAndroidUtil.hideKeyboard(editText);
    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
  };

  // 在显示键盘前应调用此方法，指定EditText与KeyboardView绑定
  public void setEditText(EditText editText) {

    this.editText = editText;
    int type = editText.getInputType();

    if (type == InputType.TYPE_CLASS_TEXT || type == 129 || type == 97) {
      keyboard = new Keyboard(context, R.xml.keyboard);
    } else {
      keyboard = new Keyboard(context, R.xml.keyboard_num);
    }

    keyboardView.setKeyboard(keyboard);

    // 关闭进入该界面获取焦点后弹出的系统键盘
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    // 隐藏该EditText获取焦点而要弹出的系统键盘
    KeyboardUtil.hideSoftInput(editText);
  }

  //Activity中获取焦点时调用，显示出键盘
  public void show() {
    emisAndroidUtil.hideKeyboard(editText);
    int visibility = keyboardView.getVisibility();
    if (visibility == View.GONE || visibility == View.INVISIBLE) {
      keyboardView.setVisibility(View.VISIBLE);
    }
  }

  //隐藏键盘
  public void hide() {
    int visibility = keyboardView.getVisibility();
    if (visibility == View.VISIBLE) {
      keyboardView.setVisibility(View.GONE);
    }
  }

  public boolean isVisibility() {
    if (keyboardView.getVisibility() == View.VISIBLE) {
      return true;
    } else {
      return false;
    }
  }

  public void setCallBack(KeyboardCallBack callBack) {
    this.callBack = callBack;
  }

  public interface KeyboardCallBack {
    void keyCall(int code);
  }
}
