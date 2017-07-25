import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 4/12/16.
 */
public class Main {

    private static Bank b = null;
    private static String errorCodeIfExist = "";
    static String  ipOfServer;
    static String portOfServer;
    static int threadCount;
    static ArrayList<String> accounts ;

    public  static void main(String parameters[]) throws Exception {

        clearHylafaxLog();
    }
    private static void startTest(String parameters[]) throws IOException, SQLException, InterruptedException {
        loadAccounts();
        if (parameters[4].equals("0")||parameters[4].equals("1")){
            testAsync(parameters,accounts);
        }else if (parameters[4].equals("2")||parameters[4].equals("3")) {
            testCardSwitchAsync(parameters,accounts);
        }
    }
    private static void testAsync(String par[],ArrayList<String> accounts) throws InterruptedException, SQLException, IOException {
        new ChannelTestAsync(par,accounts);
    }
    private static void testCardSwitchAsync(String par[],ArrayList<String> accounts) throws InterruptedException, SQLException, IOException {
        new CardSwitchTest(par);
    }
    private static void loadAccounts() throws IOException {


        final File FileOfSettings=new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String Path1 =FileOfSettings.toString();
        int i="StresTest.jar".length();
        String PathOfaccountFile =Path1.substring(0,Path1.length()-i)+"/accounts";
       // PathOfaccountFile =Path1+"/accounts";
        Scanner s = new Scanner(new File(PathOfaccountFile));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNext()){
            list.add(s.next());
        }
        s.close();
        accounts=list;
    }

    private  static void  clearHylafaxLog() throws IOException, InterruptedException {
        deleteContentOdDirectory("/var/spool/hylafax/doneq");
        deleteContentOdDirectory("/var/spool/hylafax/recvq");
        deleteContentOdDirectory("/var/spool/hylafax/log");
        deleteContentOdDirectory("/var/spool/hylafax/sendq");
        deleteContentOdDirectory("/var/spool/hylafax/docq");
        deleteContentOdDirectory("/var/spool/hylafax/pollq");
    }
    private  static void  deleteContentOdDirectory(final String dir) throws IOException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    runCommand("rm -rf "+dir+"/*");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    private  static void  runCommand(String command) throws IOException {
        try {
            //  String command = " java -cp /ivr/*:. org.asteriskjava.fastagi.DefaultAgiServer";
            Runtime rt = Runtime.getRuntime();
            System.out.println("start command:"+command);
            Process proc = rt.exec(command);
            System.out.println("stop command:"+command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) ;System.out.println(line);

        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }
}
