package ServiceObjects.SchaduledServices;


import ServiceObjects.Other.LoggerToDB;
import utils.LoggerSettings;
import utils.PropertiesUtils;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 5/8/17.
 */
public class LoggerScheduler {

    static long periodInMilliSeconds=70;

    public  static void scheduleQueueCheck(){

        periodInMilliSeconds= Long.valueOf(PropertiesUtils.getQueueLoggerIntervalTime());
        ScheduledExecutorService   scheduledExecutorService=
                Executors.newSingleThreadScheduledExecutor();

        ScheduledFuture scheduledFuture= scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    doOperations();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },0, periodInMilliSeconds,TimeUnit.MILLISECONDS);

    }

    private static void doOperations() throws Exception {

        if (LoggerSettings.dataBaseLoggerQueue.size()>10000){

        }
        logAnObject();

    }

    private static void logAnObject() throws Exception {
        LoggerToDB loggerToDB= LoggerSettings.dataBaseLoggerQueue.dequeue();
        if (loggerToDB!=null){
            loggerToDB.doLogByQueue();
            if (!loggerToDB.getResultOfLog()){
                LoggerSettings.dataBaseLoggerQueue.enqueue(loggerToDB);
                System.out.println("Log new Object To DB failed:queue size is :"+LoggerSettings.dataBaseLoggerQueue.size());
                System.out.println("______________________________________________________________");
            }else{

                System.out.println("Log new Object is Success:queue size is :"+LoggerSettings.dataBaseLoggerQueue.size());
                System.out.println("______________________________________________________________");
            }
        }
    }
}
