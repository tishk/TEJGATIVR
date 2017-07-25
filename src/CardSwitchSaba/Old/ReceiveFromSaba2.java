package CardSwitchSaba.Old;


import CardSwitchSaba.SendToSabaSwitch;
import ServiceObjects.ISO.ISO210;
import ServiceObjects.ISO.ISO430;
import ServiceObjects.Other.LoggerToDB;
import utils.PropertiesUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReceiveFromSaba2 {

    public ReceiveFromSaba2(){
        boolean ResultOfCreateReceiveFromSabaRunning=false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                      start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        String host = PropertiesUtils.getThisHostForSaba();
        int port = Integer.valueOf(PropertiesUtils.getThisPortForSaba());
        InetSocketAddress sAddr = new InetSocketAddress(host, port);
        server.socket().bind(sAddr);
        server.register(selector, SelectionKey.OP_ACCEPT, new AcceptHandler());
        System.out.println("Listening on " + server.socket().getInetAddress() + " @ " + server.socket().getLocalPort());

        while ( selector.select() > 0 ) {
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while ( keys.hasNext() ) {
                SelectionKey key = keys.next();
                try {
                    ((Handler) key.attachment()).handle(selector, key);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                } finally {
                    keys.remove(); // [!]
                }
            }
        }
    }

    static interface Handler {
        void handle(Selector selector, SelectionKey key) throws IOException;
    }

    static final int BUFFER_SIZE_IN_BYTES = 140;
    static class AcceptHandler implements Handler {
        public void handle(Selector selector, SelectionKey key) throws IOException {
            if ( key.isAcceptable() ) {
                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                SocketChannel client = server.accept();
                client.configureBlocking(false);
                client.write(ByteBuffer.wrap(("Please leave a message no longer than " + BUFFER_SIZE_IN_BYTES + " bytes.\r\n").getBytes()));

                client.register(selector, SelectionKey.OP_READ, new ReadHandler());
            }
        }
    }

    static class ReadHandler implements Handler {
        private final ByteBuffer myStorage = ByteBuffer.allocate(BUFFER_SIZE_IN_BYTES);
        public void handle(Selector selector, SelectionKey key) throws IOException {
            if ( key.isReadable() ) {
                SocketChannel client = (SocketChannel) key.channel();
                client.read(myStorage);
                if ( !myStorage.hasRemaining() || new String(myStorage.array(), 0, myStorage.position()).endsWith("\n") ) {
                    byte bytes[] = new byte[1000];
                    myStorage.get(bytes, 0, 1000);
                    Charset cs = Charset.forName("UTF-8");
                    String msg = new String(bytes, cs);

                    myStorage.flip();
                    //client.write(myStorage);
                    myStorage.clear();
                    client.close();
                    processMessage(msg);
                } else {
                    client.register(selector, SelectionKey.OP_READ, this);
                }
            }
        }
        public  String  GetNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }
        public int getKindOfMessage(final String msg){

            try {
                if (msg.substring(4,8).equals("0210")) return  0;
                if (msg.substring(4,8).equals("0430")) return  1;
                else return  -1;
            } catch (Exception e) {
                return -1;
            }
        }
        private void processMessage(final String RCVMessage){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    int kind = getKindOfMessage(RCVMessage);
                    if (kind == 0)  logISO210(RCVMessage);
                    else if (kind == 1) logISO430(RCVMessage);
                    else ;
                }
            });

        }
        private void logISO210(final String messageString ){

            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    LoggerToDB loggerToDB =null;
                    ISO210 iso210=null;
                    try {
                        iso210=new ISO210();
                        iso210.ProcessReceiveString(messageString);
                        if (iso210.getResponseTransactionCode().equals("80") ||
                                iso210.getResponseTransactionCode().equals("84") ||
                                iso210.getResponseTransactionCode().equals("91") ||
                                iso210.getResponseTransactionCode().equals("34") ){

                            SendToSabaSwitch c=new SendToSabaSwitch(iso210);


                        }
                        // System.out.println("MESSAGE Recieved  is :  " +iso210.getReceivedString());
                        System.out.println("MESSAGE Recieved  Result is :  " +iso210.getResponseTransactionCode()+" Time is:"+GetNowTimeWithSeparator());
                        System.out.println("Pan is :  " +iso210.getPan());
                        System.out.println("Action Code is :  " +iso210.getActionCode());

                        loggerToDB = new LoggerToDB(iso210);

                    } catch (Exception e) {
                        loggerToDB = null;
                        iso210=null;
                        e.printStackTrace();
                        executorService.shutdown();
                    }finally {
                        loggerToDB = null;
                        iso210=null;
                        executorService.shutdown();

                    }
                }
            });
        }
        private void logISO430(final String messageString ){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    ISO430 iso430=null;
                    LoggerToDB loggerToDB =null;
                    try {
                        iso430=new ISO430();
                        iso430.ProcessReceiveString(messageString);
                        loggerToDB = new LoggerToDB(iso430);
                        loggerToDB = null;
                    } catch (Exception e) {
                        iso430=null;
                        loggerToDB=null;
                        e.printStackTrace();
                    }finally {
                        iso430=null;
                        loggerToDB=null;
                    }
                }
            });
        }
    }
}