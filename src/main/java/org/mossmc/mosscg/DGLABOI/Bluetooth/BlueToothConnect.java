package org.mossmc.mosscg.DGLABOI.Bluetooth;

import org.mossmc.mosscg.DGLABOI.BasicInfo;

import javax.bluetooth.*;
import java.util.*;

public class BlueToothConnect {

    public static List<RemoteDevice> deviceDiscovered = new ArrayList<>();
    public static List<String> serviceDiscovered = new ArrayList<>();

    public static void search() {
        try {
            LocalDevice ld = LocalDevice.getLocalDevice();
            BasicInfo.logger.sendInfo("本机蓝牙名称:" + ld.getFriendlyName());
            findDevices();
            //findServices();
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }

    public static void findDevices() throws Exception {
        BasicInfo.logger.sendInfo("正在搜索设备喵~");
        deviceDiscovered.clear();
        LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC,listener);
        synchronized (inquiryCompletedEvent) {
            inquiryCompletedEvent.wait();
            LocalDevice.getLocalDevice().getDiscoveryAgent().cancelInquiry(listener);
            BasicInfo.logger.sendInfo("搜索设备完成喵~");
        }
    }

    public static void findServices() throws Exception{
        BasicInfo.logger.sendInfo("正在搜索服务喵~");
        serviceDiscovered.clear();
        //LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC,listener);
        synchronized (serviceSearchCompletedEvent) {
            serviceSearchCompletedEvent.wait();
           // LocalDevice.getLocalDevice().getDiscoveryAgent().cancelInquiry(listener);
            BasicInfo.logger.sendInfo("搜索服务完成喵~");
        }
    }

    //事件及监听器
    public static final Object serviceSearchCompletedEvent = new Object();
    public static final Object inquiryCompletedEvent = new Object();
    //监听器
    public static DiscoveryListener listener = new DiscoveryListener() {
        @Override
        public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
            deviceDiscovered.add(remoteDevice);
            try {
                BasicInfo.logger.sendInfo("发现蓝牙设备："+remoteDevice.getFriendlyName(false));
            } catch (Exception e) {
                BasicInfo.logger.sendException(e);
            }
        }
        @Override
        public void servicesDiscovered(int id, ServiceRecord[] serviceRecords) {
            for (ServiceRecord serviceRecord : serviceRecords) {
                String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                if (url == null) continue;
                serviceDiscovered.add(url);
                DataElement serviceName = serviceRecord.getAttributeValue(0x0100);
                BasicInfo.logger.sendInfo("发现"+serviceName.getValue()+"服务于："+url);
            }
        }
        @Override
        public void serviceSearchCompleted(int i, int i1) {
            BasicInfo.logger.sendInfo("服务搜索完成！");
            synchronized (serviceSearchCompletedEvent) {
                serviceSearchCompletedEvent.notifyAll();
            }
        }
        @Override
        public void inquiryCompleted(int i) {
            BasicInfo.logger.sendInfo("蓝牙搜索完成！");
            synchronized (inquiryCompletedEvent) {
                inquiryCompletedEvent.notifyAll();
            }
        }
    };
}
