package org.mossmc.mosscg.DGLABOI.Strength;

import org.mossmc.mosscg.DGLABOI.BasicInfo;
import org.mossmc.mosscg.DGLABOI.Bluetooth.BluetoothStrength;
import org.mossmc.mosscg.DGLABOI.Judge.JudgeData;

public class StrengthCalculate {
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public static void updateThread() {
        while (true) {
            try {
                JudgeData.dataUpdate();
                int value = getStrengthValue();
                if (BluetoothStrength.target!=value) BluetoothStrength.updateStrength(value);
                Thread.sleep(1000L*5);
            }catch (Exception e) {
                BasicInfo.logger.sendException(e);
            }
        }
    }
    public static int getStrengthValue() {
        //基础值
        double value = BasicInfo.config.getDouble("basicValue");
        //未开始则返回基础值
        if (!JudgeData.start) return (int) value;
        //WA&AC数据
        value += JudgeData.WATime*BasicInfo.config.getDouble("WAValue");
        value += JudgeData.ACTime*BasicInfo.config.getDouble("ACValue");
        //排名数据
        value += JudgeData.rank*BasicInfo.config.getDouble("rankValue");
        //检查上下限
        if (value>BasicInfo.config.getDouble("limit")) value=BasicInfo.config.getDouble("limit");
        if (value<=0) value=0;
        //取整返回
        return (int) value;
    }
}
