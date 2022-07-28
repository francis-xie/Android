
package com.basic.web.core.web.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.basic.web.R;
import com.basic.web.utils.WebUtils;
import com.basic.web.utils.LogUtils;
import com.basic.web.widget.WebParentLayout;

public class DefaultDesignUIController extends DefaultUIController {
    private BottomSheetDialog mBottomSheetDialog;
    private static final int RECYCLER_VIEW_ID = 0x1001;
    private Activity mActivity = null;
    private WebParentLayout mWebParentLayout;
    private LayoutInflater mLayoutInflater;

    @Override
    public void onJsAlert(WebView view, String url, String message) {
        onJsAlertInternal(view, message);
    }

    private void onJsAlertInternal(WebView view, String message) {
        Activity mActivity = this.mActivity;
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        try {
            WebUtils.show(view,
                    message,
                    Snackbar.LENGTH_SHORT,
                    Color.WHITE,
                    mActivity.getResources().getColor(R.color.black),
                    null,
                    -1,
                    null);
        } catch (Throwable throwable) {
            if (LogUtils.isDebug()){
                throwable.printStackTrace();
            }
        }
    }


    @Override
    public void onJsConfirm(WebView view, String url, String message, JsResult jsResult) {
        super.onJsConfirm(view, url, message, jsResult);
    }


    @Override
    public void onSelectItemsPrompt(WebView view, String url, String[] ways, Handler.Callback callback) {
        showChooserInternal(view, url, ways, callback);
    }

    @Override
    public void onForceDownloadAlert(String url, final Handler.Callback callback) {
        super.onForceDownloadAlert(url, callback);
    }

    private void showChooserInternal(WebView view, String url, final String[] ways, final Handler.Callback callback) {
        LogUtils.i(TAG, "url:" + url + "  ways:" + ways[0]);
        RecyclerView mRecyclerView;
        if (mBottomSheetDialog == null) {
            mBottomSheetDialog = new BottomSheetDialog(mActivity);
            mRecyclerView = new RecyclerView(mActivity);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            mRecyclerView.setId(RECYCLER_VIEW_ID);
            mBottomSheetDialog.setContentView(mRecyclerView);
        }
        mRecyclerView = (RecyclerView) mBottomSheetDialog.getDelegate().findViewById(RECYCLER_VIEW_ID);
        mRecyclerView.setAdapter(getAdapter(ways, callback));
        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (callback != null) {
                    callback.handleMessage(Message.obtain(null, -1));
                }
            }
        });
        mBottomSheetDialog.show();
    }

    private RecyclerView.Adapter getAdapter(final String[] ways, final Handler.Callback callback) {
        return new RecyclerView.Adapter<BottomSheetHolder>() {
            @Override
            public BottomSheetHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BottomSheetHolder(mLayoutInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(BottomSheetHolder bottomSheetHolder, final int i) {
                TypedValue outValue = new TypedValue();
                mActivity.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                bottomSheetHolder.mTextView.setBackgroundResource(outValue.resourceId);
                bottomSheetHolder.mTextView.setText(ways[i]);

                bottomSheetHolder.mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                            mBottomSheetDialog.dismiss();
                        }
                        Message mMessage = Message.obtain();
                        mMessage.what = i;
                        callback.handleMessage(mMessage);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return ways.length;
            }
        };
    }

    private static class BottomSheetHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public BottomSheetHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(android.R.id.text1);
        }
    }

    @Override
    public void onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult jsPromptResult) {
        super.onJsPrompt(view, url, message, defaultValue, jsPromptResult);
    }


    @Override
    public void bindSupportWebParent(WebParentLayout webParentLayout, Activity activity) {
        super.bindSupportWebParent(webParentLayout, activity);
        this.mActivity = activity;
        this.mWebParentLayout = webParentLayout;
        mLayoutInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public void onShowMessage(String message, String from) {
        if (!TextUtils.isEmpty(from) && from.contains("performDownload")) {
            return;
        }
        onJsAlertInternal(mWebParentLayout.getWebView(), message);
    }
}
