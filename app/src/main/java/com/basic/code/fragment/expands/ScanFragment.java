
package com.basic.code.fragment.expands;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basic.aop.annotation.IOThread;
import com.basic.aop.annotation.Permission;
import com.basic.aop.enums.ThreadType;
import com.basic.page.annotation.Page;
import com.basic.page.core.PageOption;
import com.basic.scan.Scan;
import com.basic.scan.util.QRCodeAnalyzeUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.fragment.expands.qrcode.CustomCaptureActivity;
import com.basic.code.fragment.expands.qrcode.CustomCaptureFragment;
import com.basic.code.fragment.expands.qrcode.QRCodeProduceFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.app.IntentUtils;
import com.basic.tools.app.PathUtils;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.basic.aop.consts.PermissionConsts.CAMERA;
import static com.basic.aop.consts.PermissionConsts.STORAGE;

/**
 * 二维码扫描
 *

 * @since 2018/12/29 下午12:37
 */
@Page(name = "二维码", extra = R.drawable.ic_expand_qrcode)
public class ScanFragment extends BaseSimpleListFragment {
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;
    /**
     * 定制化扫描界面Request Code
     */
    public static final int REQUEST_CUSTOM_SCAN = 113;


    @Override
    protected void initArgs() {
        initPermission();
    }

    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("默认扫描界面");
        lists.add("默认扫描界面(自定义主题)");
        lists.add("定制化扫描界面");
        lists.add("远程扫描界面");
        lists.add("生成二维码图片");
        lists.add("选择二维码进行解析");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                startScan(ScanType.DEFAULT);
                break;
            case 1:
                startScan(ScanType.DEFAULT_Custom);
                break;
            case 2:
                startScan(ScanType.CUSTOM);
                break;
            case 3:
                startScan(ScanType.REMOTE);
                break;
            case 4:
                openPage(QRCodeProduceFragment.class);
                break;
            case 5:
                selectQRCode();
                break;
            default:
                break;
        }
    }

    @Permission(STORAGE)
    private void selectQRCode() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.IMAGE), REQUEST_IMAGE);
    }

    /**
     * 开启二维码扫描
     */
    @Permission(CAMERA)
    @IOThread(ThreadType.Single)
    private void startScan(ScanType scanType) {
        switch (scanType) {
            case CUSTOM:
                PageOption.to(CustomCaptureFragment.class)
                        .setRequestCode(REQUEST_CUSTOM_SCAN)
                        .setNewActivity(true)
                        .open(this);
                break;
            case DEFAULT:
                Scan.startScan(this, REQUEST_CODE);
                break;
            case DEFAULT_Custom:
                CustomCaptureActivity.start(this, REQUEST_CODE, R.style.ScanTheme_Custom);
                break;
            case REMOTE:
                Intent intent = new Intent(Scan.ACTION_DEFAULT_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CUSTOM_SCAN && resultCode == RESULT_OK) {
            handleScanResult(data);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //处理扫描结果（在界面上显示）
            handleScanResult(data);
        }

        //选择系统图片并解析
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                getAnalyzeQRCodeResult(uri);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getAnalyzeQRCodeResult(Uri uri) {
        Scan.analyzeQRCode(PathUtils.getFilePathByUri(getContext(), uri), new QRCodeAnalyzeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                XToastUtils.toast("解析结果:" + result, Toast.LENGTH_LONG);
            }

            @Override
            public void onAnalyzeFailed() {
                XToastUtils.toast("解析二维码失败", Toast.LENGTH_LONG);
            }
        });
    }


    /**
     * 处理二维码扫描结果
     *
     * @param data
     */
    private void handleScanResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getInt(Scan.RESULT_TYPE) == Scan.RESULT_SUCCESS) {
                    String result = bundle.getString(Scan.RESULT_DATA);
                    XToastUtils.toast("解析结果:" + result, Toast.LENGTH_LONG);
                } else if (bundle.getInt(Scan.RESULT_TYPE) == Scan.RESULT_FAILED) {
                    XToastUtils.toast("解析二维码失败", Toast.LENGTH_LONG);
                }
            }
        }
    }

    @Permission(CAMERA)
    private void initPermission() {
        XToastUtils.toast("相机权限已获取！");
        Scan.setAutoFocusInterval(800);
    }

    /**
     * 二维码扫描类型
     */
    public enum ScanType {
        /**
         * 默认
         */
        DEFAULT,
        /**
         * 默认(修改主题）
         */
        DEFAULT_Custom,
        /**
         * 远程
         */
        REMOTE,
        /**
         * 自定义
         */
        CUSTOM,
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/zhiqiang/Scan");
            }
        });
        return titleBar;
    }

}
