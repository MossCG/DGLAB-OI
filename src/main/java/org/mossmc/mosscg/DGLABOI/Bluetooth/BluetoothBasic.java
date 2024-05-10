package org.mossmc.mosscg.DGLABOI.Bluetooth;

import org.mossmc.mosscg.DGLABOI.BasicInfo;
import org.mossmc.mosscg.MossLib.File.FileCheck;

import java.io.*;

public class BluetoothBasic {
    public static Process process;
    public static BufferedWriter writer;
    public static BufferedReader reader;
    public static final Object startCompleteEvent = new Object();
    public static final Object scanCompleteEvent = new Object();
    public static final Object connectCompleteEvent = new Object();
    public static void init() {
        FileCheck.checkDirExist("./DGLABOI/libs");
        FileCheck.checkFileExist("./DGLABOI/libs/DGLAB-BT-EXE.exe","DGLAB-BT-EXE.exe");
        try {
            Runtime run = Runtime.getRuntime();
            process = new ProcessBuilder("./DGLABOI/libs/DGLAB-BT-EXE.exe").start();
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Thread thread = new Thread(BluetoothBasic::readCommand);
            thread.start();
            run.addShutdownHook(new Thread(() -> process.destroyForcibly()));
            synchronized (startCompleteEvent) {
                startCompleteEvent.wait();
            }
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendError("无法启动蓝牙核心！请反馈报错！");
            System.exit(1);
        }
    }

    public static void writeCommand(String cmd) {
        try {
            writer.write(cmd);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("无法向蓝牙核心发送指令！");
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void readCommand() {
        while (true) {
            try {
                String cmd = reader.readLine();
                if (BasicInfo.debug) BasicInfo.sendDebug(cmd);
                String[] keys = cmd.split("\\s+");
                switch (keys[0]) {
                    case "event":
                        switch (keys[1]) {
                            case "start":
                                synchronized (startCompleteEvent) {
                                    startCompleteEvent.notifyAll();
                                }
                                break;
                            case "scanComplete":
                                synchronized (scanCompleteEvent) {
                                    scanCompleteEvent.notifyAll();
                                }
                                break;
                            case "deviceFound":
                                BluetoothDevice.deviceFound.add(keys[2]);
                                break;
                            case "connectSucceed":
                                BluetoothDevice.connected = true;
                                synchronized (connectCompleteEvent) {
                                    connectCompleteEvent.notifyAll();
                                }
                                break;
                            case "connectFailed":
                                synchronized (connectCompleteEvent) {
                                    connectCompleteEvent.notifyAll();
                                }
                                break;
                            case "updateBattery":
                                BasicInfo.logger.sendInfo("当前郊狼设备电量："+keys[2]);
                                break;
                            case "updateStrength":
                                BluetoothStrength.now = Integer.parseInt(keys[2]);
                                break;
                            default:
                                break;
                        }
                        break;
                    case "msg":
                        BasicInfo.logger.sendInfo(cmd.substring(4));
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                BasicInfo.logger.sendException(e);
                BasicInfo.logger.sendWarn("无法从蓝牙核心读取指令！");
            }
        }
    }
}