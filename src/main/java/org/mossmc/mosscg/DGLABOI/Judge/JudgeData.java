package org.mossmc.mosscg.DGLABOI.Judge;

import org.mossmc.mosscg.DGLABOI.BasicInfo;

import java.util.HashMap;
import java.util.Map;

public class JudgeData {
    public static boolean start = false;
    public static int WATime = 0;
    public static int ACTime = 0;
    public static int rank = 1;
    public static String OJSelect;
    public static Map<String,String> judgeClassMap = new HashMap<>();
    public static Map<String,String> judgeNameMap = new HashMap<>();
    public static void updateData(String key,int valueI,boolean valueB) {
        switch (key) {
            case "start":
                if (!start && valueB) {
                    BasicInfo.logger.sendInfo("比赛开始了哦~");
                }
                if (start && !valueB) {
                    BasicInfo.logger.sendInfo("比赛结束了哦~");
                }
                start = valueB;
                break;
            case "WATime":
                if (WATime < valueI) {
                    BasicInfo.logger.sendInfo("杂鱼~已经WA了"+valueI+"发了哦~");
                }
                WATime = valueI;
                break;
            case "ACTime":
                if (ACTime < valueI) {
                    BasicInfo.logger.sendInfo("已经AC了"+valueI+"发了~还不错嘛~");
                }
                ACTime = valueI;
                break;
            case "rank":
                if (rank < valueI) {
                    BasicInfo.logger.sendInfo("zako~zako~排名掉到"+valueI+"了哦~");
                }
                if (rank > valueI) {
                    BasicInfo.logger.sendInfo("嘛~还不错哦，排名升到"+valueI+"了呢~");
                }
                rank = valueI;
                break;
            default:
                break;
        }
    }
    public static void registerAll() {
        try {
            //如果你想要注册你们学校的OJ
            //写好OJ类之后在这里加上注册就行
            registerJudge("ZUCC","浙大城院OJ","org.mossmc.mosscg.DGLABOI.Judge.OJ.ZUCC");
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }
    public static void registerJudge(String ID,String name,String classPath) {
        judgeClassMap.put(ID,classPath);
        judgeNameMap.put(ID,name);
        BasicInfo.sendDebug("已注册"+ID+"|"+name+"，类路径："+classPath);
    }
    public static void dataInit() {
        try {
            Class<?> classOJ = Class.forName(judgeClassMap.get(OJSelect));
            classOJ.getMethod("init").invoke(classOJ.newInstance());
        }catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }
    public static void dataUpdate() {
        try {
            Class<?> classOJ = Class.forName(judgeClassMap.get(OJSelect));
            classOJ.getMethod("update").invoke(classOJ.newInstance());
        }catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }
}
