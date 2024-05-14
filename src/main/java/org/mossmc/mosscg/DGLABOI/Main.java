package org.mossmc.mosscg.DGLABOI;

import org.mossmc.mosscg.DGLABOI.Bluetooth.*;
import org.mossmc.mosscg.DGLABOI.Command.CommandReload;
import org.mossmc.mosscg.DGLABOI.Judge.JudgeData;
import org.mossmc.mosscg.DGLABOI.Strength.StrengthCalculate;
import org.mossmc.mosscg.MossLib.Command.CommandManager;
import org.mossmc.mosscg.MossLib.Config.ConfigManager;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Main {
    public static void main(String[] args) {
        //预加载部分
        FileCheck.checkDirExist("./DGLABOI");
        ObjectLogger logger = new ObjectLogger("./DGLABOI/logs");
        BasicInfo.logger = logger;

        //基础信息输出
        logger.sendInfo("欢迎使用DGLABOI软件");
        logger.sendInfo("软件版本：" + BasicInfo.version);
        logger.sendInfo("软件作者：" + BasicInfo.author);
        logger.sendInfo("蒟蒻杂鱼是会被电的哦~zako~zako~");

        //配置读取
        logger.sendInfo("正在读取配置文件......");
        BasicInfo.config = ConfigManager.getConfigObject("./DGLABOI", "config.yml", "config.yml");
        if (!BasicInfo.config.getBoolean("enable")) {
            logger.sendInfo("你还没有完成配置文件的设置哦~");
            logger.sendInfo("快去配置一下吧~");
            logger.sendInfo("配置文件位置：./DGLABOI/config.yml");
            System.exit(0);
        }
        BasicInfo.debug = BasicInfo.config.getBoolean("debug");
        //Http初始化
        System.setProperty("sun.net.client.defaultConnectTimeout", "3000");
        System.setProperty("sun.net.client.defaultReadTimeout", "3000");
        System.setProperty("http.keepAlive", "false");
        //OI模块
        logger.sendInfo("正在注册OJ数据......");
        JudgeData.registerAll();
        logger.sendInfo("----------------------------");
        logger.sendInfo("支持OJ列表：");
        JudgeData.judgeNameMap.forEach((ID, name) -> logger.sendInfo(ID+"："+name));
        logger.sendInfo("----------------------------");
        while (true) {
            logger.sendInfo("请选择你的OJ：");
            String OJ = readInput();
            if (JudgeData.judgeNameMap.containsKey(OJ)) {
                JudgeData.OJSelect = OJ;
                logger.sendInfo("已选择OJ："+OJ);
                break;
            } else {
                logger.sendInfo("未知的OJ："+OJ+"，请重新选择！");
            }
        }
        JudgeData.dataInit();
        //蓝牙模块
        logger.sendInfo("正在加载蓝牙模块......");
        BluetoothBasic.init();
        BluetoothDevice.scan();
        BluetoothControl.updateBattery();
        logger.sendInfo("正在启动强度更新线程......");
        Thread strengthUpdate = new Thread(BluetoothStrength::smoothUpdate);
        strengthUpdate.start();
        logger.sendInfo("正在启动波形更新线程......");
        Thread waveUpdate = new Thread(BluetoothWave::waveSender);
        waveUpdate.start();
        logger.sendInfo("正在启动数据更新线程......");
        Thread dataUpdate = new Thread(StrengthCalculate::updateThread);
        dataUpdate.start();
        //命令行
        CommandManager.initCommand(logger,true);
        CommandManager.registerCommand(new CommandReload());
        logger.sendInfo("启动完成！开始你的OI旅程吧~");
    }

    public static void reload() {
        BasicInfo.config = ConfigManager.getConfigObject("./DGLABOI", "config.yml", "config.yml");
        BasicInfo.logger.sendInfo("配置文件重载成功！");
    }

    public static String readInput() {
        BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            BasicInfo.logger.sendException(e);
            return "null";
        }
    }
}
