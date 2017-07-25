import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 1/22/2016.
 */
public class FaxListener__ {

    public FaxListener__() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    StartReceiver();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



    }
    private  void StartReceiver() throws InterruptedException {
        runServer();
        while (true){
            if (!ResultOfRunning) prepareSocket();
            Thread.sleep(3000);
        }


    }
    private static showStatus server;
    private static Util.PersianDateTime PDT=new Util.PersianDateTime();
    public static boolean ResultOfRunning=false;

    private ServerSocket serverSocket;

    private void prepareSocket(){
        try{
            stopServer();
        } catch (Exception e) {

        }

        runServer();
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private void runServer() {


        try {

            //System.out.println("Starting Saba Listener");
            serverSocket = new ServerSocket(1301);
            ResultOfRunning=true;
            Socket s =null;
            Util.printMessage("Fax Modules Started...",false);
            while (true) {
                try {
                    s = serverSocket.accept();
                    executorService.submit(new ServiceRequest(s));

                } catch (IOException ioe) {
                    ResultOfRunning=false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        //Stop the executor service.
        executorService.shutdownNow();
        try {
            //Stop accepting requests.
            serverSocket.close();
            serverSocket=null;
        } catch (IOException e) {
            //  System.out.println("Error in server shutdown");
            //  e.printStackTrace();
        }
        //System.exit(0);
    }


    class ServiceRequest implements Runnable {

        private Socket socket;
        String faxFile="";
        String faxNumber="";
        public  String  GetNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }
        public ServiceRequest(Socket connection) throws SocketException {
            this.socket = connection;
            //socket.setSoTimeout(2000);
        }
        public void run() {
            byte[] messageByte = new byte[1000];
            DataInputStream in = null;
            int LenOfMessage = 0;
            int RealLenOfMessage = 0;

            boolean end = false;
            String messageString = "";
            String Reqq = GetNowTimeWithSeparator();
            int bytesRead = -1;
            int Req = 0;
            try {
                in = new DataInputStream(socket.getInputStream());
                end = false;

                bytesRead = in.read(messageByte);
                messageString += new String(messageByte, 0, bytesRead);


                try {

                    processMessage(messageString);

                } catch (Exception e) {
                }


                messageString = "";
                messageByte = null;
                in = null;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                        in = null;
                    } catch (IOException ex) {
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                        socket = null;
                    } catch (IOException ex) {
                    }
                }
            }
        }

        private void processMessage(final String RCVMessage){

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    if (getDataFromStringIsOK(RCVMessage)) {
                        try {

                                int i=0;
                                while (true){
                                    if (i==20) break;
                                  if (!Util.faxInUS){
                                      Util.faxInUS=true;
                                      sendFax();
                                      break;
                                  }else {
                                      try {
                                          Thread.sleep(2000);
                                          i++;
                                      } catch (InterruptedException e) {
                                          e.printStackTrace();
                                      }
                                  }
                                }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
        public  void  sendFax() throws IOException {

            //sendfax -d 85569999  /ivr/Fax/FaxVega200.tiff
            String command = "sendfax -n -d "+faxNumber+"  "+faxFile;

            InputStreamReader isr =null;
            try
            {
                Util.printMessage("command sent to hylafax:"+command,false);
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                while ((line = br.readLine()) != null) Util.printMessage(line,false);
            }catch (Exception e){
                Util.printMessage("Error Sendfax:"+e.toString(),false);
            }


        }
        public  boolean getDataFromStringIsOK(String rcv){
            try
            {
                Util.printMessage("Recieve Fax Command:"+rcv,false);
                String[] parts = rcv.split("@");
                faxFile = parts[0]; // 004
                faxNumber= parts[1]; // 034556
                Thread.sleep(2000);
                return true;
            }catch (Exception e){
                return false;
            }
        }
    }

}
