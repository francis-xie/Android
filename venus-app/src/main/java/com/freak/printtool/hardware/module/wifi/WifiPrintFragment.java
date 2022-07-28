package com.freak.printtool.hardware.module.wifi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.emis.venus.R;
import com.freak.printtool.hardware.app.App;
import com.freak.printtool.hardware.module.wifi.adapter.PrinterSettingAdapter;
import com.freak.printtool.hardware.module.wifi.adapter.bean.PrinterSettingBean;
import com.freak.printtool.hardware.module.wifi.printerutil.SocketPrint;
import com.freak.printtool.hardware.module.wifi.util.SearchPrinterUtil;
import com.freak.printtool.hardware.utils.DialogUtil;
import com.freak.printtool.hardware.utils.LogUtil;
import com.freak.printtool.hardware.utils.SharedPreferencesUtils;
import com.freak.printtool.hardware.utils.ThreadPoolProxyFactory;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.freak.printtool.hardware.module.wifi.addprinter.AddPrinterActivity.ADD_PRINTER_REQUEST_CODE;
import static com.freak.printtool.hardware.module.wifi.addprinter.AddPrinterActivity.ADD_PRINTER_RESULT_CODE;

public class WifiPrintFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout wifi_print_connect, wifi_hand_add_printer, wifi_test, wifi_no_sdk_print_test, wifi_off_print;
    private RecyclerView wifi_printer_recycle;
    private boolean isPrinterOn = false;
    private boolean isVoiceOn = false;
    private PrinterSettingAdapter mPrinterSettingAdapter;
    private List<PrinterSettingBean> mList;

    private ExecutorService mExecutorService;
    /**
     * 获取wifi的ip地址
     */
    private String mIpAddress;
    /**
     * 打印机端口
     */
    private final static int PORT = 9100;
    /**
     * 是否开启打印机、语音播报
     */
    private boolean isOpenPrinter, isOpenVoice;
    private static int count;
    private static ThreadPoolProxyFactory sThreadPoolProxyFactory;
    private SocketPrint socketPrint;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_print, container, false);
        App.getPrinterUtilInstance();
        mList = new ArrayList<>();
        wifi_print_connect = view.findViewById(R.id.wifi_print_connect);
        wifi_test = view.findViewById(R.id.wifi_print_test);
        wifi_hand_add_printer = view.findViewById(R.id.wifi_hand_add_printer);
        wifi_printer_recycle = view.findViewById(R.id.wifi_printer_recycle);
        wifi_no_sdk_print_test = view.findViewById(R.id.wifi_no_sdk_print_test);
        wifi_off_print = view.findViewById(R.id.wifi_off_print);
        wifi_print_connect.setOnClickListener(this);
        wifi_hand_add_printer.setOnClickListener(this);
        wifi_test.setOnClickListener(this);
        wifi_no_sdk_print_test.setOnClickListener(this);
        wifi_off_print.setOnClickListener(this);

        if (isOpenPrinter) {
            wifi_print_connect.setSelected(true);
            isPrinterOn = true;
            mList.clear();
            searchPrinter();
        } else {
            wifi_print_connect.setSelected(false);
            isPrinterOn = false;
        }
        mList.clear();
        initRecycleView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getPrinterUtilInstance().printerOnDestroy();
    }

    private String getIpAddress() {
        return SharedPreferencesUtils.getString(getActivity(), SharedPreferencesUtils.Printer_IP_Address);
    }

    private void initRecycleView() {
        wifi_printer_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPrinterSettingAdapter = new PrinterSettingAdapter(R.layout.layout_list_item_printer_ip_address, mList);
        mPrinterSettingAdapter.bindToRecyclerView(wifi_printer_recycle);
        mPrinterSettingAdapter.setEmptyView(R.layout.layout_no_data);
        wifi_printer_recycle.setAdapter(mPrinterSettingAdapter);
        mPrinterSettingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                refreshRecycleView(position);


            }
        });

    }

    private void refreshRecycleView(final int position) {
        for (int i = 0; i < mList.size(); i++) {
            if (i == position) {
                mList.get(i).setSelect(true);
            } else {
                mList.get(i).setSelect(false);
            }
        }
        mPrinterSettingAdapter.notifyDataSetChanged();
        SharedPreferencesUtils.save(getActivity(), SharedPreferencesUtils.Printer_IP_Address, mList.get(position).getIp());
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (mExecutorService == null) {
                    mExecutorService = Executors.newCachedThreadPool();
                }
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        SearchPrinterUtil searchPrinterUtil = new SearchPrinterUtil();
                        searchPrinterUtil.search(mList.get(position).getIp(), 9100);
                        if (searchPrinterUtil.printerIsOpen) {
                            mHandler.sendEmptyMessage(4);
                        } else {
                            mHandler.sendEmptyMessage(6);
                        }
                    }
                });
            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //打印机
            case R.id.wifi_print_connect:
                if (isPrinterOn) {
                    wifi_print_connect.setSelected(false);

                    isPrinterOn = false;
                    SharedPreferencesUtils.save(getActivity(), SharedPreferencesUtils.Printer_Open_Status, false);
                } else {
                    wifi_print_connect.setSelected(true);

                    isPrinterOn = true;
                    SharedPreferencesUtils.save(getActivity(), SharedPreferencesUtils.Printer_Open_Status, true);
                    SharedPreferencesUtils.save(getActivity(), SharedPreferencesUtils.Printer_IP_Address, "");
                    mList.clear();
                    searchPrinter();
                }
                break;
            case R.id.wifi_hand_add_printer:
//                AddPrinterActivity.startAction(getActivity());
                break;
            case R.id.wifi_off_print:
                App.getPrinterUtilInstance().disConnectToDevice();
                break;
            //使用socket打印，不使用sdk
            case R.id.wifi_no_sdk_print_test:
                ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        socketPrint=new SocketPrint("10.9.1.168",9100,"GB2312");
                    }
                });
                break;
            case R.id.wifi_print_test:
                LogUtil.e("ip地址--》" + getIpAddress());
                checkLinStatusBeforePrint(getIpAddress(), 9100);
                break;
            default:
                break;


        }
    }

    public void checkLinStatusBeforePrint(final String ip, final int port) {
        int mLin = App.getPrinterUtilInstance().isLin();
        LogUtil.e("连接返回状态--》" + mLin);
        switch (mLin) {
            //连接断开
            case 0:
                mHandler.sendEmptyMessage(0);
                break;
            //监听状态
            case 1:
                break;
            //正在连接,延迟一秒再次查询状态
            case 2:
                //延迟1秒后再检查连接状态
                if (count > 20) {
                    ToastUtil.show("无法连接打印机，请检查打印机是否正常工作，稍后重试");
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(2);
                        }
                    }, 100);
                    count++;
                }

                break;
            //连接成功
            case 3:
                count = 0;
                ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        App.getPrinterUtilInstance().printFormat(
                                "<center_bold>打印测试</center_bold>" +
                                        "<left>测试金额：66686.3</left>"
                        );
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 开始搜索打印机
     */
    private void searchPrinter() {
        mIpAddress = SearchPrinterUtil.getIpAddress(getActivity());
        LogUtil.e("获取的ip地址--》" + mIpAddress);
        if (TextUtils.isEmpty(mIpAddress)) {
            DialogUtil.showToastDialog((AppCompatActivity) getActivity(), "温馨提示", "wifi暂未连接，请先连接wifi，然后使用打印机开关进行搜索打印机", "知道了");
        } else {
            RunThread runThread = new RunThread();
            runThread.start();
        }

    }

    /**
     * 搜索打印机线程
     */
    private class RunThread extends Thread {
        public RunThread() {
        }

        @Override
        public void run() {
            super.run();
            if (mExecutorService == null) {
                mExecutorService = Executors.newCachedThreadPool();
            }
            //循环查找打印机  从2-255
            for (int i = 2; i < 255; i++) {
                final int index = i;
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String ipAddress = mIpAddress + index;
                        SearchPrinterUtil mSearchPrinterUtil = new SearchPrinterUtil();
                        mSearchPrinterUtil.search(ipAddress, PORT);
                        if (mSearchPrinterUtil.printerIsOpen) {
                            Logger.e("开启的打印机--》" + ipAddress);
                            PrinterSettingBean printerSettingBean = new PrinterSettingBean();
                            printerSettingBean.setIp(ipAddress);
                            if (TextUtils.isEmpty(getIpAddress())) {
                                printerSettingBean.setSelect(false);
                            } else {
                                if (getIpAddress().equals(ipAddress)) {
                                    printerSettingBean.setSelect(true);
                                } else {
                                    printerSettingBean.setSelect(false);
                                }
                            }
                            mList.add(printerSettingBean);
                            mHandler.sendEmptyMessage(5);
                            return;
                        } else {
                            if (index == 254) {
                                mHandler.sendEmptyMessage(1);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 查找打印Handel、检查打印机状态Handel
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //断开连接
                case 0:
                    //sp文件保存的是否开启打印
                    boolean isOpenPrinter = SharedPreferencesUtils.getBoolean(getActivity(), SharedPreferencesUtils.Printer_Open_Status);
                    if (isOpenPrinter) {
                        if (TextUtils.isEmpty(getIpAddress())) {
                            ToastUtil.show("请先设置打印机ip地址");
                        } else {
                            App.getPrinterUtilInstance().connectToDevice(getIpAddress(), 9100);
                            checkLinStatusBeforePrint(getIpAddress(), 9100);
                        }
                    } else {
                        ToastUtil.show("打印机未打开，请到打印机设置中打开打印机");
                    }
                    break;
                //搜索结束
                case 1:
                    setSharedPreferencesIpAddress();
                    break;
                //正在连接
                case 2:
                    //sp文件保存的是否开启打印
                    boolean mIsOpenPrinter = SharedPreferencesUtils.getBoolean(getActivity(), SharedPreferencesUtils.Printer_Open_Status);
                    if (mIsOpenPrinter) {
                        if (TextUtils.isEmpty(getIpAddress())) {
                            ToastUtil.show("请先设置打印机ip地址");
                        } else {
                            checkLinStatusBeforePrint(getIpAddress(), 9100);
                        }
                    } else {
                        ToastUtil.show("打印机未打开，请到打印机设置中打开打印机");
                    }
                    break;
                case 4:
                    ToastUtil.show("连接成功");
                    break;
                case 5:
                    mPrinterSettingAdapter.setNewData(mList);
                    break;
                case 6:
                    ToastUtil.show("连接失败");
                    break;
                default:
                    break;

            }
        }
    };

    private void setSharedPreferencesIpAddress() {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isSelect()) {
                SharedPreferencesUtils.save(getActivity(), SharedPreferencesUtils.Printer_IP_Address, mList.get(i).getIp());
            } else {
                SharedPreferencesUtils.save(getActivity(), SharedPreferencesUtils.Printer_IP_Address, "");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRINTER_REQUEST_CODE) {
            if (resultCode == ADD_PRINTER_RESULT_CODE) {
                String ip = data.getStringExtra("ip");
                Logger.e("添加的ip地址为--》" + ip);
                PrinterSettingBean printerSettingBean = new PrinterSettingBean();
                printerSettingBean.setIp(ip);
                printerSettingBean.setSelect(true);
                mList.add(printerSettingBean);
                refreshRecycleView(mList.size() - 1);
            }
        }

    }
}
