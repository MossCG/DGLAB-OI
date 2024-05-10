package org.mossmc.mosscg.DGLABOI.Bluetooth;

import org.mossmc.mosscg.DGLABOI.BasicInfo;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDevice {
    public static boolean connected = false;
    public static List<String> deviceFound = new ArrayList<>();
    @SuppressWarnings("BusyWait")
    public static void scan() {
        while (true) {
            try {
                deviceFound.clear();
                BluetoothBasic.writeCommand("scan");
                synchronized (BluetoothBasic.scanCompleteEvent) {
                    BluetoothBasic.scanCompleteEvent.wait();
                }
                for (String address : deviceFound) {
                    BasicInfo.logger.sendInfo("正在尝试连接："+address);
                    BluetoothBasic.writeCommand("connect "+address);
                    synchronized (BluetoothBasic.connectCompleteEvent) {
                        BluetoothBasic.connectCompleteEvent.wait();
                    }
                    if (connected) {
                        BasicInfo.logger.sendInfo("设备连接成功："+address);
                        break;
                    } else {
                        BasicInfo.logger.sendInfo("设备连接失败："+address);
                    }
                }
                if (connected) break;
                Thread.sleep(1000);
            } catch (Exception e) {
                BasicInfo.logger.sendException(e);
                BasicInfo.logger.sendWarn("无法从蓝牙核心搜索设备！");
            }
        }
    }
}
