import SystemStatus.Status_All;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 12/20/2015.
 */
public class MainOfTelBank {
    public  static Util util = new Util();
    private static Util.PersianDateTime PDT = new Util.PersianDateTime();

    private static Status_All getTelBankOSStatus()   {
        Status_All status_all=null;
        System.out.println("7 ");
        try{
            status_all=new Status_All();
        }catch (Exception e){
            System.out.println(e.toString());
        }

        System.out.println("11");
        status_all.setIsTelBankStatus(false);
        System.out.println("12");
        status_all.setIsDataBaseServer(false);
        System.out.println("13");
        status_all.setIsOSOfGateway(false);
        System.out.println("14");
        status_all.setIsTelBankOSStatus(true);
        System.out.println("15");
        status_all.getStatus_TelBank().setClientID(Util.ClientNo);
        System.out.println("16");
        status_all.getStatus_TelBank().setNetworkStatus("1");
        System.out.println("8");
        try {
            status_all.processSystemStatus();
        } catch (InterruptedException e) {
            System.out.println("9");
            e.printStackTrace();
        }
        System.out.println("10");
        System.out.println("Memory usage is :" + status_all.getRAMAllSpace());
        return status_all;
    }
    private  static void  deleteSentFaxOfToday() {
        try {
            FileUtils.deleteDirectory(new File(Util.FaxFile));

            FileUtils.forceMkdir(new File(Util.FaxFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static void    main(String args[]) throws Exception {
       startOperations();
       // deleteSentFaxOfToday();
    }
    public  static void startOperations() throws ExecutionException, InterruptedException, IOException {
        Thread.sleep(5000);
        startMessagePrinter();
       // Thread.sleep(5000);
        IVRStop();


       // Thread.sleep(2000);
       // Util.printMessage("Starting Fax service,Please wait...",false);
        //faxResetAll();
        Thread.sleep(3000);
      //  Util.printMessage("fax service started",false);

        startFaxListener();
        startMonitoringService();
        Util.printMessage("Starting IVR,Please wait...",false);
      // reStartAmportal();
       // Thread.sleep(3000);
        startIVR();

       startAsteriskFrechCheck();
        //Thread.sleep(10000);

      //  runCommand("/ivr/reStartTelbank.sh");
       // startOperations();


    }
    public  static void faxResetAll() throws IOException, InterruptedException {
        runCommand("faxsetup");
        Thread.sleep(3000);

    }
    public static void  runCommand(String command) throws IOException {
        try {
          //  String command = " java -cp /ivr/*:. org.asteriskjava.fastagi.DefaultAgiServer";
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
          //  while ((line = br.readLine()) != null) ;//Util.printMessage(line,false);

        } catch (Throwable t) {
            Util.printMessage(t.getMessage(),false);
        }
    }
    public  static String   createPDFFile() throws IOException {

        String faxFile_PDF="/ivr/Fax/Fax2188803601-1456988406.1.pdf";
        String faxFile_HTML="/ivr/Fax/Fax2188803601-1456988406.1.html";


            //String command = "convert "+FaxFile+".jpg "+FaxFile+".pdf";
            String command = "wkhtmltopdf --page-size A4 --dpi 2000 "+faxFile_HTML+" "+faxFile_PDF;
            // Util.printMessage(command,false);
            InputStreamReader isr =null;
            try
            {
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
            }catch (Exception e){
                Util.printMessage(command,false);
                Util.printMessage("errorrrrr"+e.toString(),false);
                return null;
            }
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null)
                if (line!="Done"){
                    return null;
                }

         return null;
    }
    private static void    startFaxListener() throws ExecutionException, InterruptedException {
        new FaxListener();
    }
    private static void    startMessagePrinter() throws ExecutionException, InterruptedException {
        new showStatus();
    }
    private static void    startMonitoringService() throws ExecutionException, InterruptedException {
        new MonitoringService();
    }
    private static void    startAsteriskFrechCheck(){
        new FreshAsterisk();
    }
    private static boolean startIVR() {


        try {
            String command = " java -cp /ivr/*:. org.asteriskjava.fastagi.DefaultAgiServer";
            // Valuesmain.test("Here");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            Util.printMessage("IVR Started..." , false);
            return true;

        } catch (Throwable t) {
            //  ShowMessage("Ivr Started");
            return false;
        }
    }
    private static boolean reStartAmportal() {

        try {
            String command = "amportal restart";
            // Valuesmain.test("Here");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            Thread.sleep(10000);
            Util.printMessage("Asterisk Restarted..." , false);
            return true;

        } catch (Throwable t) {
            //  ShowMessage("Ivr Started");
            return false;
        }
    }
    private static boolean IVRStartOnWindows() {

        try {
            String command = " java -cp D:\\Gateway\\out\\production\\Telbank\\asterisk-java.jar;. org.asteriskjava.fastagi.DefaultAgiServer";
            // Valuesmain.test("Here");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            Util.printMessage("IVR Started..." , false);
            return true;

        } catch (Throwable t) {
            //  ShowMessage("Ivr Started");
            return false;
        }
    }

    private static void    initializedIVR() throws IOException, InterruptedException {
        if (IVRIsRunning()) IVRStop();
        Thread.sleep(2000);
        startIVR();
    }
    private static boolean IVRIsRunning() {

        try {

            String command = "ps -aux | grep org.asteriskjava.fastagi.DefaultAgiServer";

            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int LineCounter = 0;
            while ((line = br.readLine()) != null) {
                // Valuesmain.test(line);
                LineCounter++;
            }
            if (LineCounter > 2) {
                return true;
            } else return false;


        } catch (Throwable t) {
            return false;
        }


    }
    private static boolean IVRStop() {
        try {
            String command = "pkill -f org.asteriskjava.fastagi.DefaultAgiServer";
            InputStreamReader isr = null;
            isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            int LineCounter = 0;
            return true;

        } catch (IOException e2) {
            return false;
        }

    }

    private void ClearScreen() {
        try {
            String command = "clear";
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
        } catch (Throwable t) {

        }
    }
    private void IVRStarted() throws IOException {

        try {
            String command = " java -cp /ivr/*:. org.asteriskjava.fastagi.DefaultAgiServer";
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

