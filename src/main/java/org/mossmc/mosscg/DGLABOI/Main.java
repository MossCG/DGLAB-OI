package org.mossmc.mosscg.DGLABOI;

import org.mossmc.mosscg.DGLABOI.Bluetooth.BlueToothConnect;
import org.mossmc.mosscg.DGLABOI.Command.CommandReload;
import org.mossmc.mosscg.DGLABOI.Http.HttpAPI;
import org.mossmc.mosscg.MossLib.Command.CommandManager;
import org.mossmc.mosscg.MossLib.Config.ConfigManager;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectConfig;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.Arrays;

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
        //搜索设备（蓝牙）
        //BlueToothConnect.search();
        //WebAPI控制
        HttpAPI.initAPI();
        //命令行
        CommandManager.initCommand(logger,true);
        CommandManager.registerCommand(new CommandReload());
        logger.sendInfo("启动完成！开始你的OI旅程吧~");
    }

    public static void reload() {
        BasicInfo.config = ConfigManager.getConfigObject("./DGLABOI", "config.yml", "config.yml");
        BasicInfo.logger.sendInfo("配置文件重载成功！");
    }
}
