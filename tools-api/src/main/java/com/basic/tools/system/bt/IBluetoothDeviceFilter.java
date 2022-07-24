
package com.basic.tools.system.bt;

import android.bluetooth.BluetoothDevice;

/**
 * <pre>
 *     desc   : 蓝牙设备过滤器

 *     time   : 2018/4/28 上午1:16
 * </pre>
 */
public interface IBluetoothDeviceFilter {

    /**
     * 是否是指定的蓝牙设备
     *
     * @param device 蓝牙设备
     * @return 是否是满足条件的蓝牙设备
     */
    boolean isCorrect(BluetoothDevice device);
}
