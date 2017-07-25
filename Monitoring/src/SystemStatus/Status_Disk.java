package SystemStatus;

import org.hyperic.sigar.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 4/28/16.
 */
public class Status_Disk {

    private Sigar sigar=null;
    private long TotalSpace=0;
    public String getTotalSpace(){
        return String.valueOf(TotalSpace);
    }
    private long FreeSpace=0;
    public String getFreeSpace(){
        return String.valueOf(FreeSpace);
    }
    private long UsedSpace=0;
    public String getUsedSpace(){
        return String.valueOf(UsedSpace);
    }
    private long AvailableSpace=0;
    public String getAvailableSpace(){
        return String.valueOf(AvailableSpace);
    }
    Map<Object, Object> nodeDiskInfo;
    private long CpuWorkTime=0, CpuSystemTime, CpuIdleTime=0,CpuWaitTime=0,CPUIdleTimePercent=0,CPUWorkTimePercent=0;
    public Status_Disk() throws SigarException {
       // gatherSystemData();
        getNodeDiskInfos();
    }

    private void gatherSystemData()throws SigarException {

        try {
            Sigar sigar=new Sigar();
            FileSystem fileSystem=new FileSystem();

            String devName=fileSystem.getDevName();
            DiskUsage diskUsage=sigar.getDiskUsage(devName);
            System.out.println("devname:"+devName);
            System.out.println("usage:"+diskUsage);
            sigar.close();

        } catch (final SigarException e) {
            sigar.close();

        }

    }

    public List<Map<Object, Object>> getNodeDiskInfos() {

        sigar=new Sigar();
        List<Map<Object, Object>> nodeInfoList = new ArrayList<Map<Object, Object>>();

        FileSystem[] fileSystemList = null;
        FileSystemUsage fileSystemUsage = null;

        try {
            fileSystemList = sigar.getFileSystemList();
        } catch (Exception e) {

        }
        for (FileSystem fileSystem : fileSystemList) {

            if (! fileSystem.getTypeName().equals("none")){
                nodeDiskInfo = new HashMap<Object, Object>();
                nodeDiskInfo.put("deviceName", fileSystem.getDevName());
                nodeDiskInfo.put("dirName", fileSystem.getDirName());
                nodeDiskInfo.put("fileSystemEnvironmentType",
                        fileSystem.getTypeName());
                nodeDiskInfo.put("fileSystemType", fileSystem.getSysTypeName());
                nodeDiskInfo.put("partitionFlags", fileSystem.getFlags());
                nodeDiskInfo.put("options", fileSystem.getOptions());

                fileSystemUsage = new FileSystemUsage();
                try {
                    fileSystemUsage.gather(sigar, fileSystem.getDirName());

                } catch (SigarException e) {

                }
                nodeDiskInfo.put("availableMemory", fileSystemUsage.getAvail());
                AvailableSpace=AvailableSpace+fileSystemUsage.getAvail();
                nodeDiskInfo.put("freeMemory", fileSystemUsage.getFree());
                FreeSpace=FreeSpace+fileSystemUsage.getFree();
                nodeDiskInfo.put("totalMemory", fileSystemUsage.getTotal());
                TotalSpace=TotalSpace+fileSystemUsage.getTotal();
                nodeDiskInfo.put("usedMemory", fileSystemUsage.getUsed());
                UsedSpace=UsedSpace+fileSystemUsage.getUsed();
                nodeDiskInfo.put("readBytes", fileSystemUsage.getDiskReadBytes());
                nodeDiskInfo.put("reads", fileSystemUsage.getDiskReads());
                nodeDiskInfo.put("writeBytes", fileSystemUsage.getDiskWriteBytes());
                nodeDiskInfo.put("writes", fileSystemUsage.getDiskWrites());


                nodeInfoList.add(nodeDiskInfo);
            }


        }

        return nodeInfoList;
    }
   /*
    System.out.println("total  memory:"+nodeDiskInfo.get("totalMemory"));
                System.out.println("free  memory:"+nodeDiskInfo.get("freeMemory"));
                System.out.println("used  memory:"+nodeDiskInfo.get("usedMemory"));
                System.out.println("available memory:"+nodeDiskInfo.get("availableMemory"));
    */




}
