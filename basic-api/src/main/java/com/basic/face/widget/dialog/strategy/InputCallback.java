
package com.basic.face.widget.dialog.strategy;

import android.content.DialogInterface;
import androidx.annotation.NonNull;

/**
 * 输入内容回调
 */
public interface InputCallback {

    void onInput(@NonNull DialogInterface dialog, CharSequence input);
}
