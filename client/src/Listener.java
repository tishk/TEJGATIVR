import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Other.ClientJarSettings;
import SystemStatus.Status_All;
import utils.strUtils;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 5/7/16.
 */
public class Listener {

    strUtils strutils=new strUtils();
    long idOfRequest=0;

    public Listener(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {

                    startCapitalizeServer();
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
//                    executorService.shutdownNow();
                }
            }
        });
    }

    public  void start(String ipOfGateway,Integer port){

        try {
            QueueWorker worker = new QueueWorker();
            new Thread(worker).start();

            InetAddress address = InetAddress.getByName(ipOfGateway);
            new Thread(new Server(address, port, worker)).start();
            // System.out.println("_____________________________________");
            System.out.println("Monitoring Started...");
            System.out.println("_____________________________________");
        } catch (IOException e) {
            e.printStackTrace();
            // System.out.println("_____________________________________");
            System.out.println("Monitoring Start Error:"+e.toString());
            System.out.println("_____________________________________");
        }

    }
    private void loadSettings(){

    }
    class Server implements Runnable {
        private InetAddress hostAddress;
        private int port;
        private ServerSocketChannel serverChannel;
        private Selector selector;
        private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
        private QueueWorker worker;
        private List pendingChanges = new LinkedList();
        private Map pendingData = new HashMap();
        public Server(InetAddress hostAddress, int port, QueueWorker worker) throws IOException {
            this.hostAddress = hostAddress;
            this.port = port;
            this.selector = this.initSelector();
            this.worker = worker;
        }
        public void send(SocketChannel socket, byte[] data) {
            synchronized (this.pendingChanges) {
                // Indicate we want the interest ops set changed
                this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

                // And queue the data we want written
                synchronized (this.pendingData) {
                    List queue = (List) this.pendingData.get(socket);
                    if (queue == null) {
                        queue = new ArrayList();
                        this.pendingData.put(socket, queue);
                    }
                    queue.add(ByteBuffer.wrap(data));
                }
            }

            // Finally, wake up our selecting thread so it can make the required changes
            this.selector.wakeup();
        }
        public void run() {
            while (true) {
                try {
                    synchronized (this.pendingChanges) {
                        Iterator changes = this.pendingChanges.iterator();
                        while (changes.hasNext()) {
                            ChangeRequest change = (ChangeRequest) changes.next();
                            switch (change.type) {
                                case ChangeRequest.CHANGEOPS:
                                    SelectionKey key = change.socket.keyFor(this.selector);
                                    key.interestOps(change.ops);
                            }
                        }
                        this.pendingChanges.clear();
                    }
                    this.selector.select();
                    Iterator selectedKeys = this.selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        SelectionKey key = (SelectionKey) selectedKeys.next();
                        selectedKeys.remove();
                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isAcceptable()) {
                            this.accept(key);
                        } else if (key.isReadable()) {
                            this.read(key);
                        } else if (key.isWritable()) {
                            this.write(key);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        private void accept(SelectionKey key) throws IOException {
            // For an accept to be pending the channel must be a server socket channel.
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

            // Accept the connection and make it non-blocking
            SocketChannel socketChannel = serverSocketChannel.accept();
            Socket socket = socketChannel.socket();
            socketChannel.configureBlocking(false);

            // Register the new SocketChannel with our Selector, indicating
            // we'd like to be notified when there's data waiting to be read
            socketChannel.register(this.selector, SelectionKey.OP_READ);
        }
        private void read(SelectionKey key) throws IOException {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            this.readBuffer.clear();
            int numRead;
            try {
                numRead = socketChannel.read(this.readBuffer);
            } catch (IOException e) {
                key.cancel();
                socketChannel.close();
                return;
            }

            if (numRead == -1) {
                key.channel().close();
                key.cancel();
                return;
            }
            this.worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
        }
        private void write(SelectionKey key) throws IOException {
            SocketChannel socketChannel = (SocketChannel) key.channel();

            synchronized (this.pendingData) {
                List queue = (List) this.pendingData.get(socketChannel);

                // Write until there's not more data ...
                while (!queue.isEmpty()) {
                    ByteBuffer buf = (ByteBuffer) queue.get(0);
                    socketChannel.write(buf);
                    if (buf.remaining() > 0) {
                        // ... or the socket's buffer fills up
                        break;
                    }
                    queue.remove(0);
                }

                if (queue.isEmpty()) {
                    // We wrote away all data, so we're no longer interested
                    // in writing on this socket. Switch back to waiting for
                    // data.
                    key.interestOps(SelectionKey.OP_READ);
                }
            }
        }
        private Selector initSelector() throws IOException {
            // Create a new selector
            Selector socketSelector = SelectorProvider.provider().openSelector();

            // Create a new non-blocking server socket channel
            this.serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            // Bind the server socket to the specified address and port
            InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
            serverChannel.socket().bind(isa);

            // Register the server socket channel, indicating an interest in
            // accepting new connections
            serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

            return socketSelector;
        }
    }
    class QueueWorker implements Runnable {
        private List queue = new LinkedList();
        public void processData(Server server, SocketChannel socket, byte[] data, int count) {
            byte[] dataCopy = new byte[count];
            System.arraycopy(data, 0, dataCopy, 0, count);
            synchronized(queue) {
                queue.add(new ServerDataEvent(server, socket, dataCopy));
                queue.notify();
            }
        }
        public void run() {
            ServerDataEvent dataEvent;
            while(true) {
                synchronized(queue) {
                    while(queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    dataEvent = (ServerDataEvent) queue.remove(0);
                }
                dataEvent.server.send(dataEvent.socket, dataEvent.data);
            }
        }
    }
    class ServerDataEvent {
        public Server server;
        public SocketChannel socket;
        private String response="";
        public byte[] data;
        public ServerDataEvent(Server server, SocketChannel socket, byte[] data) {
            this.server = server;
            this.socket = socket;
            this.data = data;
            String RCVMessage=new String(data);
            processMessage(RCVMessage);
            server.send(socket,response.getBytes());

        }
        public  String  getNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }

        private void processMessage(final String RCVMessage){
            // System.out.println("Start Message Process...");
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    //   System.out.println("_____________________________________");
                    System.out.println(getNowTimeWithSeparator()+":receive message is:"+RCVMessage);
                    System.out.println("_____________________________________");
                    try {
                        ObjectFromRequest obj = new ObjectFromRequest(RCVMessage);
                        ResponseFromObject stringResponse = new ResponseFromObject(obj.getCreatedObject(),RCVMessage);
                        response = stringResponse.getResponse();
                        obj=null;
                        stringResponse=null;
                    }catch (Exception e){

                    } finally {
                        executorService.shutdownNow();
                    }

                }
            });

        }

    }
    class ChangeRequest {
        public static final int REGISTER = 1;
        public static final int CHANGEOPS = 2;
        public SocketChannel socket;
        public int type;
        public int ops;
        public ChangeRequest(SocketChannel socket, int type, int ops) {
            this.socket = socket;
            this.type = type;
            this.ops = ops;
        }
    }

    public class SimpleClient {
        public  void main(String args[]){
            try{
                Socket s = new Socket("localhost",2002);
                OutputStream os = s.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                Status_All to = new Status_All();
                oos.writeObject(to);
                oos.writeObject(new String("another object from the client"));
                oos.close();
                os.close();
                s.close();
            }catch(Exception e){System.out.println(e);}
        }
    }

    public   Object submitRequestToGateway(Object object) throws IOException, ServerNotActiveException {
        Bank b=null;
        try {
            b=(Bank) Naming.lookup("rmi://"+Properties_Client.getGateway_listener_IP()+":1364/Gateway");
            object=b.submitRequest(object);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (ResponseParsingException e) {
            e.printStackTrace();
        } catch (SenderException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return object;
    }
    private  class Capitalizer extends Thread {
        private Socket socket;
        private Socket socketToGateway;
        private BufferedReader in = null;
        private String inputForGateway = null;
        private InputStreamReader ioe = null;
        private BufferedReader bufferedreader = null;
        private PrintWriter printwriter=null;
        private String input = null;
        private ObjectFromRequest objectFromRequest=null;
        private ResponseFromObject responseFromObject=null;

        private boolean doNotUseNewOperation(){
            String kind="!";
            String tempRequest=input;
            try{

                tempRequest=strutils.rightString(tempRequest,tempRequest.length()-15);
                tempRequest=strutils.rightString(tempRequest,tempRequest.length()-20);
                tempRequest=strutils.rightString(tempRequest,tempRequest.length()-17);
                kind=String.valueOf(tempRequest.charAt(0)).toLowerCase();

            }catch (Exception e){

            }

            switch (kind){
                case "e":return Properties_Client.isUseOldPayment() ;
                case "c":return Properties_Client.isUseOldPayment() ;
                case "f":return Properties_Client.isUseOldPayment() ;
                case "p":return Properties_Client.isUseOldPayment() ;
                case "g":return Properties_Client.isUseOldPayment() ;
                case "q":return Properties_Client.isUseOldTelSwitch();
                case "u":return Properties_Client.isUseOldTelSwitch() ;
                default:return false;

            }

        }

        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;


        }
        public void run() {

            try {
                receiveRequestFromTelBank();
                if   (Properties_Client.isUseOldGateway())
                    processMessagePreMethod();
                else
                {
                    if (doNotUseNewOperation())
                        processMessagePreMethod();
                    else processMessageNewMethod();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public  String  getNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }
        private void    freeResources(){
            try {socket.close();} catch (IOException e) {}
            try {in.close();} catch (IOException e) {}
            try {socketToGateway.close();} catch (IOException e) {}
            try {ioe.close();} catch (IOException e) {}
            try {bufferedreader.close();} catch (IOException e) {}
            try
            {
                printwriter.close();
                socket=null;socketToGateway=null;ioe=null;bufferedreader=null;in=null;printwriter=null;
                objectFromRequest=null;
                responseFromObject=null;
            } catch (Exception e)
            {}



            System.gc();

        }
        private void    receiveRequestFromTelBank() throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            input = in.readLine();
            System.out.println("_________________________________________________________");
            System.out.println(getNowTimeWithSeparator()+":Receive String is:" + input);
            loggerToFile.getInstance().logInfo("From TelBank ID("+String.valueOf(idOfRequest)+"):"+input);
        }


        private void processMessageNewMethod(){

            try {

                playSendAndReceiveGameWithNewGateway();
                sendResponseToTelBankNewMethod();
                freeResources();
            } catch (IOException ee) {

            } catch (ServerNotActiveException e) {

            } catch (Exception e){

            }

        }
        private void playSendAndReceiveGameWithNewGateway() throws IOException, ServerNotActiveException {

             objectFromRequest=new ObjectFromRequest(input);
             responseFromObject=new ResponseFromObject(objectFromRequest.getCreatedObject(),input);

        }
        private void sendResponseToTelBankNewMethod() throws IOException {
           // PrintWriter printwriter1 = null;

            printwriter = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true);

            printwriter.println(responseFromObject.getResponse());


            loggerToFile.getInstance().logInfo(responseFromObject.getResponse()); // new String(responseFromObject.getResponse().getBytes("UTF-16")));

            System.out.println(getNowTimeWithSeparator()+":Response String is:" + responseFromObject.getResponse());
            System.out.println("_________________________________________________________");
        }

        private void processMessagePreMethod()  {
            idOfRequest++;
            try {
                playSendAndReceiveGameWithOldGateway();
                sendResponseToTelBankOldMwthod();
                freeResources();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        private void playSendAndReceiveGameWithOldGateway() throws IOException {
            socketToGateway = new Socket("10.39.213.253", 19696);
            socketToGateway.setSoTimeout(10000);
            ioe = new InputStreamReader(socketToGateway.getInputStream());
            bufferedreader = new BufferedReader(ioe);
            printwriter = new PrintWriter(socketToGateway.getOutputStream(), true);
            printwriter.println(input);
            inputForGateway="";
            for(String lineread = ""; (lineread = bufferedreader.readLine()) != null; inputForGateway = inputForGateway + lineread){}

        }
        private void sendResponseToTelBankOldMwthod() throws IOException {
            PrintWriter printwriter1 = null;

            printwriter1 = new PrintWriter(socket.getOutputStream(), true);
            printwriter1.println(inputForGateway);

            loggerToFile.getInstance().logInfo(inputForGateway); //new String(inputForGateway.getBytes("UTF-16")));

            System.out.println(getNowTimeWithSeparator() + ":Response String is:" + inputForGateway);
            System.out.println("_________________________________________________________");
        }



    }
    private  void startCapitalizeServer()   {

        int clientNumber = 0;
        ServerSocket listener = null;
        try {
            System.out.println("@@@@@@@@@@@:listener Port: "+Properties_Client.getLocal_listener_Port());
            listener = new ServerSocket(Integer.valueOf(Properties_Client.getLocal_listener_Port()));
//            ClientJarSettings clientJarSettings=new ClientJarSettings();
//            clientJarSettings=(ClientJarSettings)submitRequestToGateway(clientJarSettings);
            Properties_Client.setUseOldAuthenticate(Properties_Client.isUseOldAuthenticate());

            try {
                while (true) {
                    new Capitalizer(listener.accept(), clientNumber++).start();
                }
            } finally {
                listener.close();
            }
        } catch (IOException e) {

        }

    }

}
