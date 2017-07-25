package SystemStatus;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * Created by root on 4/23/16.
 */

public class Status_Memory {
    private Sigar sigar=null;
    private Mem mem=null ;

    private long actualFree=0,actualUsed=0,free=0,ram=0,total=0,used=0;
    private double freePercent=0,usedPercent=0;

    public Status_Memory() throws SigarException {
        processMemory();
    }
    private  void processMemory() throws SigarException {
        sigar=new Sigar();
        mem = sigar.getMem();
        actualFree = mem.getActualFree();
        actualUsed = mem.getActualUsed();
        free = mem.getFree();
        freePercent = mem.getFreePercent();
        ram = mem.getRam();
        total = mem.getTotal();
        used = mem.getUsed();
        usedPercent = mem.getUsedPercent();
        sigar.close();
        mem=null;
    }

    public String getActualFree(){
        return String.valueOf(actualFree);
    }
    public String getActualUsed(){
        return String.valueOf(actualUsed);
    }
    public String getFree(){
        return String.valueOf(free);
    }
    public String getRam(){
        return String.valueOf(ram);
    }
    public String getTotal(){
        return String.valueOf(total);
    }
    public String getUsed(){
        return String.valueOf(used);
    }
    public String getFreePercent(){
        return String.valueOf((int) freePercent);
    }
    public String getUsedPercent(){
        return String.valueOf(((int) usedPercent)+1);
    }


}

