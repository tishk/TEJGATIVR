import SystemStatus.Status_All;
import org.hyperic.sigar.SigarException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 5/18/16.
 */
public class MonitoringService {


    public MonitoringService(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    startCapitalizeServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void start(){
        try {
            QueueWorker worker = new QueueWorker();
            new Thread(worker).start();
            String host = Util.IP;
            int port = Integer.valueOf(Util.MonitoringPort);
            InetAddress address = InetAddress.getByName(host);
            new Thread(new Server(address, port, worker)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        public void send_(SocketChannel socket, byte[] data) {
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
        public void send(SocketChannel socket, Status_All statusAll) {
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
                    queue.add((Object) statusAll);
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
        private void read(SelectionKey key) throws IOException, ClassNotFoundException {
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
        public void processData(Server server, SocketChannel socket, byte[] data, int count) throws IOException, ClassNotFoundException {
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
        public Status_All data;
        public ServerDataEvent(Server server, SocketChannel socket, byte[] data) throws IOException, ClassNotFoundException {
            this.server = server;
            this.socket = socket;
            this.data = (Status_All)deserialize(data);
            String RCVMessage=new String(data);
            processMessage(RCVMessage);

        }
        public  Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }

        private void    processMessage(final String RCVMessage){

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    try{
                        sendToMonitoringServer( getDBServerSStatus());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SigarException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        public  String  getNowDateTime(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("MM:dd:HH:mm:ss");
            String Now=DateFormat.format(Time);
            return Now;

        }

        private void    sendToMonitoringServer(Status_All status_all)  {
            Socket socket=null;
            OutputStream outputStream=null;
            ObjectOutputStream objectOutputStream=null;
            try{
                socket=new Socket(Util.IP,Integer.valueOf(Util.MonitoringPort));
                outputStream=socket.getOutputStream();
                objectOutputStream=new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(status_all);


            }catch (Exception e){
                if (outputStream!=null) try{outputStream.close();outputStream=null;}catch (Exception var2){}
                if (objectOutputStream!=null) try{objectOutputStream.close();objectOutputStream=null;}catch (Exception var2){}
                if (socket!=null) try{socket.close();socket=null;}catch (Exception var2){}
            }finally {
                if (outputStream!=null) try{outputStream.close();outputStream=null;}catch (Exception var2){}
                if (objectOutputStream!=null) try{objectOutputStream.close();objectOutputStream=null;}catch (Exception var2){}
                if (socket!=null) try{socket.close();socket=null;}catch (Exception var2){}

            }


        }

        private Status_All    getDBServerSStatus() throws InterruptedException, SigarException {
            Status_All status_all=new Status_All();
            status_all.setIsDataBaseServer(true);
            status_all.setIsTelBankStatus(false);
            status_all.setIsOSOfGateway(false);
            status_all.setIsTelBankOSStatus(false);
            status_all.processSystemStatus();
            return status_all;
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
    public  String  getNowDateTime(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("yyyy:MM:dd:HH:mm:ss");
        String Now=DateFormat.format(Time);
        return Now;

    }

    private  class Capitalizer extends Thread {
        private Socket socket;
        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;


        }
        public void run() {
            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String input = in.readLine();
                System.out.println(getNowDateTime()+":got new message");

                if (input.equals("osofdb")){
                    System.out.println(getNowDateTime()+":Request Format is Correct");
                    processMessage();
                }else{
                    System.out.println(getNowDateTime()+":Request Format is not Correct");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {

                }

            }
        }

        private void    processMessage(){

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    try{
                        sendToMonitoringServer( getDBServerSStatus());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SigarException e) {
                        e.printStackTrace();
                    }
                }
            });

        }



        private void    sendToMonitoringServer(Status_All status_all)  {
            Socket socket=null;
            OutputStream outputStream=null;
            ObjectOutputStream objectOutputStream=null;
            try{
                socket=new Socket(Util.MonitoringServerIP,Integer.valueOf(Util.MonitoringServerPort));
                outputStream=socket.getOutputStream();
                objectOutputStream=new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(status_all);


            }catch (Exception e){
                if (outputStream!=null) try{outputStream.close();outputStream=null;}catch (Exception var2){}
                if (objectOutputStream!=null) try{objectOutputStream.close();objectOutputStream=null;}catch (Exception var2){}
                if (socket!=null) try{socket.close();socket=null;}catch (Exception var2){}
            }finally {
                System.out.println(getNowDateTime()+":Status Sent Success");
                if (outputStream!=null) try{outputStream.close();outputStream=null;}catch (Exception var2){}
                if (objectOutputStream!=null) try{objectOutputStream.close();objectOutputStream=null;}catch (Exception var2){}
                if (socket!=null) try{socket.close();socket=null;}catch (Exception var2){}

            }


        }

        private Status_All    getDBServerSStatus() throws InterruptedException, SigarException {
            Status_All status_all=new Status_All();
            status_all.setPingStatus("1");
            status_all.setPingStatusDateTime(status_all.getNowDateTime());
            status_all.setPingStatusActionCode("0000");
            status_all.setPingStatusDesc("---");
            status_all.setIsDataBaseServer(true);
            status_all.setIsTelBankStatus(false);
            status_all.setIsOSOfGateway(false);
            status_all.setIsTelBankOSStatus(false);
            status_all.processSystemStatus();
            return status_all;
        }

    }
    private  void startCapitalizeServer() throws IOException {

        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(Integer.valueOf(Util.MonitoringPort));
        System.out.println(getNowDateTime()+":Monitoring Started");
        try {


            while (true) {
                new Capitalizer(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }


}