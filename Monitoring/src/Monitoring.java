

import SystemStatus.*;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 4/20/16.
 */
public class Monitoring {
    private static Bank b = null;

    //public static ListenerOfMonitoring Listener=new ListenerOfMonitoring();

    public static void main(String parameters[]) throws SigarException, InterruptedException, SQLException, ClassNotFoundException, IOException {

             //Listener.start();
        //while (true){
           // getStatusOfMemoryUsage();
       // getStatusCpuUsage();
            //getStatusNetwork();
           // getStatusOfSwapUsage();
         //   Thread.sleep(1000);
       // }
       // getStatusCpuUsage();
      //  getStatusOfSystemProcess();
        //new Properties_Monitoring();
        //DBUtils dbUtils=new DBUtils(true);
        //new ListenerOfMonitoring();
        //String s[]=dbUtils.getTelBankList();
       // getStatusOfDisk();
        System.out.println(getNowDateTime());
        new Properties_Monitoring();
        new WebListenerOfMonitoring();
        new InternalListener();

    }
    public static String  getNowDateTime(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("MM:dd:HH:mm:ss");
        String Now=DateFormat.format(Time);
        return Now;

    }
    public static void getStatusOfMemoryUsage() throws SigarException {
        Status_Memory memoryUsage=new Status_Memory();
        System.out.println("ActualFree:"+memoryUsage.getActualFree());
        System.out.println("ActualUsed:"+memoryUsage.getActualUsed());
        System.out.println("Free:"+memoryUsage.getFree());
        System.out.println("Ram:"+memoryUsage.getRam());
        System.out.println("Total:"+memoryUsage.getTotal());
        System.out.println("Used:"+memoryUsage.getUsed());
        System.out.println("FreePercent:"+memoryUsage.getFreePercent());
        System.out.println("UsedPercent:"+memoryUsage.getUsedPercent());
    }
    public static void getStatusCpuUsage() throws SigarException {
        Status_CPU cpuStatus=new Status_CPU();
        System.out.println("CpuIdleTime:"+cpuStatus.getCpuIdleTime());
        System.out.println("CpuSystemTime:"+cpuStatus.getCpuSystemTime());
        System.out.println("CpuWaitTime:"+cpuStatus.getCpuWaitTime());
        System.out.println("CpuWorkTime:"+cpuStatus.getCpuWorkTime());
        System.out.println("CpuIdleTimePercent:"+cpuStatus.getCPUIdleTimePercent());
        System.out.println("CpuWorkTimePercent:"+cpuStatus.getCPUWorkTimePercent());

    }
    public static void getStatusNetwork() throws InterruptedException, SigarException {
        Status_Network status_network=new Status_Network();
        System.out.println("DownloadRate:"+status_network.getDownloadRate());
        System.out.println("UploadRate:"+status_network.getUploadRate());
        System.out.println("NetworkData:"+status_network.getNetworkData());
    }
    public static void getStatusOfSwapUsage() throws SigarException {
        Status_Swap status_swap=new Status_Swap();
        System.out.println("ActualFree :"+status_swap.getActualFree());
        System.out.println("ActualUsed :"+status_swap.getActualUsed());
        System.out.println("ActualTotal:"+status_swap.getActualTotal());
        System.out.println("FreePercent:"+status_swap.getFreePercent());
        System.out.println("UsedPercent:"+status_swap.getUsedPercent());
    }
    public static void getStatusOfSystemProcess() throws SigarException {
        Status_Process status_process=new Status_Process(new Sigar());

    }
    public static void getStatusOfDisk() throws SigarException {
        Status_Disk status_disk=new Status_Disk();

    }
}
