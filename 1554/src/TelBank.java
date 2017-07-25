//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import ProjectClasses.IVR;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.xml.sax.SAXException;

public class TelBank extends BaseAgiScript {
    public TelBank() {
    }


    private  boolean startClient() {


        try {
            String command = " java -jar /1554/client/client.jar";
            // Valuesmain.test("Here");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            System.out.println("client run successful");
            return true;

        } catch (Throwable t) {
            //  ShowMessage("Ivr Started");
            return false;
        }
    }

    private  boolean clientNotRunning(){
        try {
            Socket socket = new Socket("127.0.0.1", 1360);
            socket.close();
            System.out.println("client jar file check:it is in running mode!");
            return false;
        } catch (IOException e) {
            System.out.println("client jar file check:it is not running mode!");
            return true;
        }
    }
    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        IVR RequestForCall = new IVR();
        if (clientNotRunning()){
            startClient();
        }
        try {
            RequestForCall.AsteriskStart();
        } catch (FileNotFoundException var5) {
            Logger.getLogger(TelBank.class.getName()).log(Level.SEVERE, (String)null, var5);
        } catch (IOException var6) {
            Logger.getLogger(TelBank.class.getName()).log(Level.SEVERE, (String)null, var6);
        } catch (SAXException | InterruptedException | ParserConfigurationException var7) {
            Logger.getLogger(TelBank.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

    }
}
