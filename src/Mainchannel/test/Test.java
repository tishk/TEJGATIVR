//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Test {
    private static Logger logger = Logger.getLogger(Test.class);

    public Test() {
    }

    public static void main(String[] args) {
        PropertyConfigurator.configureAndWatch("c:/log.properties");
        Logger logger = Logger.getLogger(Test.class);
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("1");
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 10000L);
        timer.cancel();
        timer.schedule(task, 10000L);
    }

    public static boolean isChannelReachable() {
        try {
            return InetAddress.getByName("10.10.2.128").isReachable(10000);
        } catch (UnknownHostException var1) {
            return false;
        } catch (IOException var2) {
            return false;
        }
    }
}
