package org.mossmc.mosscg.DGLABOI.Bluetooth;

import org.sputnikdev.bluetooth.manager.*;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

import java.util.Set;

public class BlueToothDevice {
    public static void findDevice() {
        BluetoothManagerBuilder builder = new BluetoothManagerBuilder();
        builder.withDiscovering(true);
        builder.withRediscover(true);
        builder.withTinyBTransport(true);
        //builder.withBlueGigaTransport("^*.$");
        BluetoothManager manager = builder.build();
        manager.addDeviceDiscoveryListener(deviceListener);
        manager.addAdapterDiscoveryListener(adapterListener);
        manager.start(true);
        for (DiscoveredAdapter adapter : manager.getDiscoveredAdapters()) {
            System.out.println("Found adapter: " + adapter.getName() + " (" + adapter.getURL() + ")");
        }
        Set<DiscoveredDevice> devices = manager.getDiscoveredDevices();
        for (DiscoveredDevice device : devices) {
            System.out.println("Found device: " + device.getName() + " (" + device.getURL() + ")");
        }

    }

    public static DeviceDiscoveryListener deviceListener = device -> {
        System.out.println("Found device: " + device.getName() + " (" + device.getURL() + ")");
    };
    public static AdapterDiscoveryListener adapterListener = adapter -> {
        System.out.println("Found adapter: " + adapter.getName() + " (" + adapter.getURL() + ")");
    };
}
