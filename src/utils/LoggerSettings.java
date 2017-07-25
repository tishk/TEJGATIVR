package utils;

import ServiceObjects.Other.LoggerToDB;

/**
 * Created by root on 7/17/17.
 */
public class LoggerSettings {

    public static boolean useQueueForLogToDataBase=false;
    public static DataBaseLoggerQueue<LoggerToDB> dataBaseLoggerQueue=new DataBaseLoggerQueue<LoggerToDB>();

    public static boolean getUseQueueForLogToDataBase() {
        return useQueueForLogToDataBase;
    }

    public static void setUseQueueForLogToDataBase(boolean useQueueForLogToDataBase) {
        LoggerSettings.useQueueForLogToDataBase = useQueueForLogToDataBase;
    }


}
