import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 7/10/2016.
 */
public class FaxListener_NIO {

    public static Object faxExtension=900;
    public FaxListener_NIO(){
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
    private void start(){
        try {
            QueueWorker worker = new QueueWorker();
            new Thread(worker).start();
            String host = Util.IP;
            System.out.println("host:" + Util.IP);
            int port = Integer.valueOf(Util.faxPort);
            InetAddress address = InetAddress.getByName(host);
            new Thread(new Server(address, port, worker)).start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Util:" + Util.faxPort);
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
        public void send(SocketChannel socket, byte[] data) {
            synchronized (this.pendingChanges) {
                this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

                synchronized (this.pendingData) {
                    List queue = (List) this.pendingData.get(socket);
                    if (queue == null) {
                        queue = new ArrayList();
                        this.pendingData.put(socket, queue);
                    }
                    queue.add(ByteBuffer.wrap(data));
                }
            }

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
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

            SocketChannel socketChannel = serverSocketChannel.accept();
            Socket socket = socketChannel.socket();
            socketChannel.configureBlocking(false);

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

                while (!queue.isEmpty()) {
                    ByteBuffer buf = (ByteBuffer) queue.get(0);
                    socketChannel.write(buf);
                    if (buf.remaining() > 0) {
                        break;
                    }
                    queue.remove(0);
                }

                if (queue.isEmpty()) {
                    key.interestOps(SelectionKey.OP_READ);
                }
            }
        }
        private Selector initSelector() throws IOException {
            Selector socketSelector = SelectorProvider.provider().openSelector();

            this.serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
            serverChannel.socket().bind(isa);

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
        String faxFile="";
        String faxNumber="";
        private String getNextFaxNo(){

            synchronized (faxExtension){
                int faxext=(int)faxExtension;
                faxext++;
                if (faxext==950) faxext=901;
                faxExtension=(Object)faxext;
                return String.valueOf(faxext);
            }



        }
        public byte[] data;
        public ServerDataEvent(Server server, SocketChannel socket, byte[] data) {
            this.server = server;
            this.socket = socket;
            this.data = data;
            String RCVMessage=new String(data);
            processMessage(RCVMessage);
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
                if (msg.substring(4,8).equals("0110")) return  0;
                else return  -1;
            } catch (Exception e) {
                return -1;
            }
        }
        private  void processMessage(final String RCVMessage){

            //  ExecutorService executorService = Executors.newSingleThreadExecutor();
            // Future future = executorService.submit(new Runnable() {
            // public void run() {
            // if (getDataFromStringIsOK(RCVMessage)) {
            try {
                faxFile = RCVMessage;
                Util.printMessage("RCV Fax File="+RCVMessage,false);
                //if (Util.faxServiceOpened)
                sendFax();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // }
            //  }
            //  });

        }
        public   void  sendFax() throws IOException, InterruptedException {
            //http://www.hylafax.org/man/current/faxsetup.1m.html
            //sendfax -d 85569999  /ivr/Fax/FaxVega200.tiff

            String command = "sendfax -G -n -T1 -t1 -d "+getNextFaxNo()+"  "+faxFile;


            //http://linux.die.net/man/1/sendfax
            //Thread.sleep(2000);
            InputStreamReader isr =null;
            try
            {
                Util.printMessage("command sent to hylafax:"+command,false);
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                line = br.readLine();
                Util.printMessage(line,false);
                // while ((line = br.readLine()) != null) Util.printMessage(line,false);
            }catch (Exception e){
                Util.printMessage("Error Sendfax:"+e.toString(),false);
            }


        }
        public   void  sendFax__() throws IOException, InterruptedException {
            //http://www.hylafax.org/man/current/faxsetup.1m.html
            //sendfax -d 85569999  /ivr/Fax/FaxVega200.tiff
            faxFile="/ivr/Fax/7777777.pdf";
            //String command = "sendfax -G -n -T1 -t1 -d "+getNextFaxNo()+"  "+faxFile;
            String command="sendfax -G -n -T1 -t1 -d 803  "+faxFile;
            // String[] cmd = {"/bin/bash","-c","echo Tb4tej@rat| sudo -S "+command};

            //http://linux.die.net/man/1/sendfax
            //Thread.sleep(2000);
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
        public   boolean getDataFromStringIsOK(String rcv){
            try
            {
                Util.printMessage("Recieve Fax Command:"+rcv,false);
                //String[] parts = rcv.split("@");
                //faxFile = parts[0]; // 004
                // faxNumber= parts[1]; // 034556
                faxFile = rcv;
                Thread.sleep(2000);
                return true;
            }catch (Exception e){
                return false;
            }
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

}
