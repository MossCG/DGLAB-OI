package org.mossmc.mosscg.DGLABOI.Bluetooth;

public class BluetoothControl {
    public static void updateBattery() {
        BluetoothBasic.writeCommand("getBattery");
    }

    public static void updateStrength(int channelA,int channelB) {
        BluetoothBasic.writeCommand("setStrength "+channelA+" "+channelB);
    }

    public static void sendWave(String channel,int x,int y,int z) {
        BluetoothBasic.writeCommand("sendWave "+channel+" "+x+" "+y+" "+z);
    }
}
