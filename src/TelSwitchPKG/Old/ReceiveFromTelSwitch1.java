package TelSwitchPKG.Old;

import CardSwitchSaba.Old.ReceiveFromSaba0;
import ServiceObjects.ISO.ISO110;
import ServiceObjects.ISO.ISO210;
import ServiceObjects.ISO.ISO400;
import ServiceObjects.Other.LoggerToDB;
import ServiceObjects.Pan.SabaSwitchResponse;
import utils.PropertiesUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 6/21/2015.
 */
public class ReceiveFromTelSwitch1 {

    public ReceiveFromTelSwitch1() throws InterruptedException {
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

    private static ReceiveFromSaba0 server;

    public boolean ResultOfRunning=false;

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

            System.out.println("Starting TelSwitchPKG Listener");
            serverSocket = new ServerSocket(Integer.valueOf(PropertiesUtils.getThisPortForTelSwitch()));
            ResultOfRunning=true;
            while (true) {
                try {
                    Socket s = serverSocket.accept();
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
            ResultOfRunning=false;
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
        InputStream inputStream = null;
        byte[] content = new byte[ 2048 ];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        public ServiceRequest(Socket connection) throws SocketException {
            this.socket = connection;
            //socket.setSoTimeout(2000);
        }
        public void run()  {
            byte[] messageByte = new byte[1000];
            DataInputStream in =null;
            int LenOfMessage=0;
            int RealLenOfMessage=0;
            ISO110 iso110=new ISO110();
            boolean end = false;
            String messageString = "";
            int bytesRead=-1;
            int Req=0;
            try
            {
                in = new DataInputStream(socket.getInputStream());
                end = false;
                while (!end) {
                    bytesRead = in.read(messageByte);
                    messageString += new String(messageByte, 0, bytesRead);
                    LenOfMessage = 0;
                    RealLenOfMessage = messageString.length();
                    try {

                        LenOfMessage = Integer.valueOf(messageString.substring(0, 4));
                    } catch (Exception e) {
                        LenOfMessage = 0;
                    }
                    if (RealLenOfMessage == LenOfMessage + 4) end = true;
                }
                Req++;
                try
                {
                    System.out.println("MESSAGE no ("+Req+"): " + messageString);
                    int kind = getKindOfMessage(messageString);
                    if (kind == 0){
                        iso110.ProcessReceiveString(messageString);
                        LoggerToDB loggerToDB = new LoggerToDB(iso110);
                    } else ;

                } catch (Exception e) {}


            }
            catch (IOException e) {
                if (in != null) {try {in.close();in=null;} catch (IOException ex) {}}
                if (socket != null) {try {socket.close();socket=null;} catch (IOException ex) {}}
                messageByte = null;
                LenOfMessage = 0;
                RealLenOfMessage = 0;
                iso110=null;
                messageString = "";
                bytesRead = 0;
                Req = 0;
                e.printStackTrace();}
            finally {
                if (in != null) {try {in.close();in=null;} catch (IOException ex) {}}
                if (socket != null) {try {socket.close();socket=null;} catch (IOException ex) {}}
                messageByte = null;
                LenOfMessage = 0;
                RealLenOfMessage = 0;
                iso110=null;
                messageString = "";
                bytesRead = 0;
                Req = 0;
            }

        }
        public int getKindOfMessage(String msg){
            try{
                if (msg.substring(4,8).equals("0110")) return 0;
                else return -1;

            }catch (Exception var1){
                return -1;
            }

        }
        private String ISOMessageReverse(final ISO210 request){

            ISO400 Reverse=new ISO400(){{
                setPan(request.getPan());
                setPanWithLen(request.getPan());
                setPin(request.getPin());
                seMessageType("0420");
                seBitmapEarly("F6788001AEE1A000");
                seBitmapSecondary("0000004210000001");
                setTransactionISOCode(request.getTransactionISOCode());
                setAmountCurrencyAcceptor(request.getAmountCurrencyAcceptor());
                setAmountCurrencyCardIssuer(request.getAmountCurrencyCardIssuer());
                setSendDateTimeToSwitch();
                setAmountCurrencyConvertRate(request.getAmountCurrencyConvertRate());
                setTraceCode(request.getTraceCode());
                setRegisterTime(request.getRegisterTime());
                setRegisterDate(request.getRegisterDate());
                setProcessDateTime(request.getProcessDateTimeInDestination());
                setIDOfPayer(request.getIDOfPayer());
                setIDOfAcceptor(request.getIDOfIssuer());
                setDataOfMagneticTapeTrack2(request.getDataOfMagneticTapeTrack2());
                setReferenceCode(request.getReferenceCode());
                setResponseForRecognizedRequest("");
                setResponseCode("");
                setIDofCardAcceptorTerminal(request.getIDofCardAcceptorTerminal());
                setCodeOfCardAcceptorTerminal(request.getIDofCardAcceptorTerminal());
                setNameAndLocationOfAcceptor(request.getNameAndLocationOfAcceptor());
                setAdditionalData(request.getAdditionalData());
                setCodeOfCurrencyTransactionAccepter(request.getCodeOfCurrencyTransactionAccepter());
                setCodeOfCurrencyTransactionIssuer(request.getCodeOfCurrencyTransactionIssuer());
                setPreliminaryDataElements(request.getPreliminaryDataElements());
                setEncryptedRequest();
            }};

            return Reverse.getEncryptedRequest();
        }
        private SabaSwitchResponse CreateResponse(String ISOMessage) {
            return null;
        }
    }
}







