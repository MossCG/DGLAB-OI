package org.mossmc.mosscg.DGLABOI.Bluetooth;

import org.mossmc.mosscg.DGLABOI.BasicInfo;

public class BluetoothStrength {
    public static int target = 0;
    public static int now  = 0;
    public static void updateStrength(int newStrength) {
        if (newStrength==target) return;
        if (target < newStrength) {
            BasicInfo.logger.sendInfo("杂鱼杂鱼~强度提高了哦~新的强度是："+newStrength);
        } else {
            BasicInfo.logger.sendInfo("还不错嘛~强度降低了哦~新的强度是："+newStrength);
        }
        target = newStrength;
    }
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public static void smoothUpdate() {
        while (true) {
            try {
                if (target!=now) {
                    int next = now;
                    if (target > now) next++;
                    if (target < now) next--;
                    BluetoothControl.updateStrength(next,next);
                }
                BasicInfo.sendDebug("当前实时强度："+now);
                Thread.sleep(1000);
            }catch (Exception e) {
                BasicInfo.logger.sendException(e);
            }
        }
    }
}
