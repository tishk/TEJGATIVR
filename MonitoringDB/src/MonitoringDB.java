import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by root on 5/7/16.
 */
public class MonitoringDB {

    public static void main(String params[]){
        registerSystemStatusAPI();
        new Util();
        new MonitoringService();
    }
    public static void registerSystemStatusAPI(){
        try {
            String command = "export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/monitoring/lib";
            // Valuesmain.test("Here");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
        } catch (Throwable t) {

        }
    }
}
