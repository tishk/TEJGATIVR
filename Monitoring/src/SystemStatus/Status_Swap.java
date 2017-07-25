package SystemStatus;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

/**
 * Created by root on 4/23/16.
 */
public class Status_Swap {

    private Sigar sigar=null;


    private long actualTotal=0,actualFree=0,actualUsed=0;
    private double freePercent=0,usedPercent=0;

    public Status_Swap() throws SigarException {
        processMemory();
    }
    private  void processMemory() throws SigarException {
        sigar=new Sigar();
        Swap swap=sigar.getSwap();


        actualTotal=swap.getTotal();
        actualFree = swap.getFree();
        actualUsed = swap.getUsed();


        freePercent = (100*actualFree)/actualTotal;
        usedPercent=  (100*actualUsed)/actualTotal;


        sigar.close();
        swap=null;
        System.gc();

    }

    public String getActualTotal(){
        return String.valueOf(actualTotal);
    }
    public String getActualFree() {
        return String.valueOf(actualFree);
    }
    public String getActualUsed() {
        return String.valueOf(actualUsed);
    }

    public String getFreePercent(){
        return String.valueOf((int) freePercent);
    }
    public String getUsedPercent(){
        return String.valueOf(((int) usedPercent));
    }

}
