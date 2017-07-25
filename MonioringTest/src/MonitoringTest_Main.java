import ServiceObjects.Account.AccountInformation;
import ServiceObjects.Account.ShebaID;
import SystemStatus.Status_All;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 5/12/16.
 */
public class MonitoringTest_Main {


    public static void main(String params[]){
      // testMonitoringWebListener(params);
        ShebaID shebaID=new ShebaID();

        shebaID.setAccountNumber("2298000113244411");
        shebaID.calculateShebaID();
        System.out.println(shebaID.getShebaID());
        System.out.println(GetNowTime());
        //123456789123#services#3435531082$9221$13601365$5859831000001247$123456





    }
    public static void testOSstatus(){
        Status_All status_all=new Status_All();
        try {
             status_all.processSystemStatus();
            System.out.println(status_all.getCPUFreeTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void testMonitoringWebListener(String parameters[]){
        try {
            Socket socket=new Socket(parameters[0],Integer.valueOf(parameters[1]));
            OutputStream outputStream=socket.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(parameters[2]);
            dataOutputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  String  GetNowTime(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("HH");
        String Now=DateFormat.format(Time);
        return Now;

    }
}
