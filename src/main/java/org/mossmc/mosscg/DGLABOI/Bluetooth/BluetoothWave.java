package org.mossmc.mosscg.DGLABOI.Bluetooth;

import org.mossmc.mosscg.DGLABOI.BasicInfo;

public class BluetoothWave {
    @SuppressWarnings({"InfiniteLoopStatement"})
    public static void waveSender() {
        while (true) {
            try {
                //随便照着郊狼文档搓了个波形，无所谓，能用就行
                sendBoth(5,135,20);
                sendBoth(5,125,20);
                sendBoth(5,115,20);
                sendBoth(5,105,20);
                sendBoth(5,95,20);
                sendBoth(4,86,20);
                sendBoth(4,76,20);
                sendBoth(4,66,20);
                sendBoth(3,57,20);
                sendBoth(3,47,20);
                sendBoth(3,37,20);
                sendBoth(2,28,20);
                sendBoth(2,18,20);
                sendBoth(1,14,20);
                sendBoth(1,9,20);
            } catch (Exception e) {
                BasicInfo.logger.sendException(e);
            }
        }
    }

    public static void sendBoth(int x,int y,int z) throws InterruptedException {
        BluetoothControl.sendWave("A",x,y,z);
        BluetoothControl.sendWave("B",x,y,z);
        Thread.sleep(100);
    }
}
