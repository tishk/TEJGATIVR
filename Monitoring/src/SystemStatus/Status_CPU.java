package SystemStatus;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * Created by root on 4/23/16.
 */
public class Status_CPU {
    private Sigar sigar=null;
    private long CpuWorkTime=0, CpuSystemTime, CpuIdleTime=0,CpuWaitTime=0,CPUIdleTimePercent=0,CPUWorkTimePercent=0;
    public Status_CPU() throws SigarException {
        gatherSystemData();
    }

    private void gatherSystemData()throws SigarException {

         sigar=new Sigar();
        Cpu cpu = sigar.getCpu();
        //Cpu cpu=new Cpu();


        CpuWorkTime = cpu.getUser() + cpu.getNice() + cpu.getSys();
        CpuIdleTime = cpu.getIdle();
        CpuWaitTime=cpu.getWait();
        CpuSystemTime = cpu.getTotal(); // sum of user+nice+sys+idle
        CPUIdleTimePercent=(100*CpuIdleTime)/CpuSystemTime;
        CPUWorkTimePercent=(100*CpuWorkTime)/CpuSystemTime;
        sigar.close();
        cpu=null;

    }

    public String getCpuWorkTime(){
        return String.valueOf(CpuWorkTime);
    }
    public String getCpuIdleTime(){
        return String.valueOf(CpuIdleTime);
    }
    public String getCpuWaitTime(){
        return String.valueOf(CpuWaitTime);
    }
    public String getCpuSystemTime(){
        return String.valueOf(CpuSystemTime);
    }
    public String getCPUIdleTimePercent(){
        return String.valueOf(CPUIdleTimePercent);
    }
    public String getCPUWorkTimePercent(){
        return String.valueOf(CPUWorkTimePercent);
    }

}
