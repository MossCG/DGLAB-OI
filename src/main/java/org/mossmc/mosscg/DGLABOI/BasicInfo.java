package org.mossmc.mosscg.DGLABOI;

import org.mossmc.mosscg.MossLib.Object.ObjectConfig;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

public class BasicInfo {
    public static String version = "V1.0.0.1.2038";
    public static String author = "MossCG";

    public static ObjectLogger logger;
    public static ObjectConfig config;

    public static boolean debug = false;
    public static void sendDebug(String message) {
        if (debug) logger.sendAPI(message);
    }
}
