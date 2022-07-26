package com.freak.printtool.hardware.module.scanner;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.emis.venus.R;

public class BarcodeScannerFragment extends Fragment {
    private TextView text_view_scan;
    private EditText edt;
    private StringBuilder mStringBuilder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
        text_view_scan = view.findViewById(R.id.text_view_scan);
        edt = view.findViewById(R.id.edt);
        mStringBuilder = new StringBuilder();
        //这是小键盘输入
        edt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {

                    return true;
                }
                return false;
            }
        });
        //这是扫码枪输入
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /**
                 * 当actionId == XX_SEND 或者 XX_DONE时都触发
                 * 或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                 * 注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                 */
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    Log.e("TAG", "扫码结束");
                    if (!TextUtils.isEmpty(edt.getText().toString())) {
                        mStringBuilder.append(edt.getText().toString().trim() + "\n");
                        text_view_scan.setText(mStringBuilder);
                        edt.setText("");
                    }

                }
                return false;
            }
        });
        return view;
    }
}
