package SystemStatus;

import org.hyperic.sigar.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Status_Network {

    private Sigar sigar;

    private String Download=null;
    private String Upload=null;
    private String NetworkInfo=null;
    private String DefaultGateway=null;
    private Map<String, Long> rxCurrentMap = new HashMap<String, Long>();
    private Map<String, List<Long>> rxChangeMap = new HashMap<String, List<Long>>();
    private Map<String, Long> txCurrentMap = new HashMap<String, Long>();
    private Map<String, List<Long>> txChangeMap = new HashMap<String, List<Long>>();



    public Status_Network() throws SigarException, InterruptedException {
        sigar = new Sigar();
        getMetric();
        NetworkInfo=networkInfo();
        DefaultGateway=getDefaultGateway();
        Thread.sleep(500);
        startMetricTest();
        sigar.close();
        sigar=null;

    }

    public  String getDownloadRate(){
        return Download;
    }
    public  String getUploadRate(){
        return Upload;
    }
    public  String getNetworkData(){
        return NetworkInfo;
    }

    private String getDefaultGateway() throws SigarException {
        return sigar.getNetInfo().getDefaultGateway();
    }
    private String networkInfo() throws SigarException {
        String info = sigar.getNetInfo().toString();
        info += "\n"+ sigar.getNetInterfaceConfig().toString();
        return info;
    }
    private void startMetricTest() throws SigarException, InterruptedException {
       //while (true) {
            Long[] m = getMetric();
            long totalrx = m[0];
            long totaltx = m[1];
             Download = Sigar.formatSize(totalrx);
             Upload =   Sigar.formatSize(totaltx);
        //  }

    }
    private Long[] getMetric() throws SigarException {
        for (String ni : sigar.getNetInterfaceList()) {
            // System.out.println(ni);
            NetInterfaceStat netStat = sigar.getNetInterfaceStat(ni);
            NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ni);
            String hwaddr = null;
            if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
                hwaddr = ifConfig.getHwaddr();
            }
            if (hwaddr != null) {
                long rxCurrenttmp = netStat.getRxBytes();
                saveChange(rxCurrentMap, rxChangeMap, hwaddr, rxCurrenttmp, ni);
                long txCurrenttmp = netStat.getTxBytes();
                saveChange(txCurrentMap, txChangeMap, hwaddr, txCurrenttmp, ni);
            }
        }
        long totalrxDown = getMetricData(rxChangeMap);
        long totaltxUp = getMetricData(txChangeMap);
        for (List<Long> l : rxChangeMap.values())
            l.clear();
        for (List<Long> l : txChangeMap.values())
            l.clear();
        return new Long[] { totalrxDown, totaltxUp };
    }
    private long getMetricData(Map<String, List<Long>> rxChangeMap) {
        long total = 0;
        for (Map.Entry<String, List<Long>> entry : rxChangeMap.entrySet()) {
            int average = 0;
            for (Long l : entry.getValue()) {
                average += l;
            }
            total += average / entry.getValue().size();
        }
        return total;
    }
    private void saveChange(Map<String, Long> currentMap,Map<String, List<Long>> changeMap, String hwaddr, long current,String ni) {
        Long oldCurrent = currentMap.get(ni);
        if (oldCurrent != null) {
            List<Long> list = changeMap.get(hwaddr);
            if (list == null) {
                list = new LinkedList<Long>();
                changeMap.put(hwaddr, list);
            }
            list.add((current - oldCurrent));
        }
        currentMap.put(ni, current);
    }

}
