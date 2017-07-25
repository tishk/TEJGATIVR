//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ProjectClasses;

import ProjectClasses.CallObject;
import ProjectClasses.Global;
import ProjectClasses.Operations;
import ProjectClasses.Global.Command;
import ProjectClasses.Global.SettingsOBJ;
import ProjectClasses.Operations.LOG;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
    public Connection() {
    }

    public class FTPConnection {
        public FTPConnection() {
        }
    }

    public class WSConnection {
        public WSConnection() {
        }
    }

    public class DBMSConnection {
        public DBMSConnection() {
        }
    }

    public class SocketConnection {
        public SocketConnection() {
        }

        public CallObject StartConnect(CallObject Call) throws UnsupportedEncodingException, FileNotFoundException, IOException, InterruptedException {
            Operations var10002 = new Operations();
            var10002.getClass();
            //new LOG(var10002);
            Connection.SocketConnection.SocketThread NewConnection = Connection.this.new SocketConnection().new SocketThread(Call);
            return NewConnection.call;
        }

        public class SocketThread extends Thread {
            LOG Log;
            CallObject call=new CallObject();
            Socket Socket_client;
            public String IP;
            public String Result;
            public int Port;

            private void Log(String S) throws FileNotFoundException, IOException {
                this.Log.Log(this.call, S);
            }

            SocketThread(CallObject CALL) throws UnsupportedEncodingException, FileNotFoundException, IOException, InterruptedException {
                Operations var10003 = new Operations();
                var10003.getClass();
                this.Log = new Operations().new LOG();
                this.call = new CallObject();
                Global var10002 = new Global();
                var10002.getClass();

                var10002 = new Global();
                var10002.getClass();
                Global.Command command = new Global().new Command();
                SettingsOBJ Setting = command.LoadSetting("GateweySettings");
                this.call = CALL;
                this.IP = Setting.IP;
                this.Port = Integer.parseInt(Setting.Port);
                this.call = this.SendMessage(CALL);
            }

            public CallObject SendMessage(CallObject CAlll) throws IOException {
                Socket socket = null;
                String Messs = "!";
                String Mess = CAlll.SendString;

                try {
                    this.Log("Connecting to " + this.IP + " on port " + this.Port);
                    socket = new Socket(this.IP, this.Port);
                    socket.setSoTimeout(10000);
                    this.Log("Connected.");

                    InputStreamReader ioe = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedreader = new BufferedReader(ioe);
                    PrintWriter printwriter = new PrintWriter(socket.getOutputStream(), true);
                    printwriter.println(CAlll.callUniqID+";"+Mess);
                    this.Log("Message send to server: " + Mess);

                    for(String lineread = ""; (lineread = bufferedreader.readLine()) != null; Messs = Messs + lineread) {
                        ;
                    }

                    this.Log("Received from Server: " + Messs);
                    this.Result = Messs;
                    CAlll.ReceiveString = Messs;
                    CAlll.Action_ConnectedToGateway = true;
                    this.Log("Closing connection.");
                    bufferedreader.close();
                    ioe.close();
                    printwriter.close();
                    socket.close();
                    return CAlll;
                } catch (UnknownHostException var9) {
                    this.Result = "!";
                    CAlll.ReceiveString = Messs;
                    CAlll.Action_ConnectedToGateway = false;
                    this.Log("UnknownHostException: ");
                    return CAlll;
                } catch (InterruptedIOException var10) {
                    this.Result = "!";
                    CAlll.ReceiveString = Messs;
                    CAlll.Action_ConnectedToGateway = false;
                    this.Log("Timeout while attempting to establish socket connection.");
                    return CAlll;
                } catch (IOException var11) {
                    this.Result = "!";
                    CAlll.ReceiveString = Messs;
                    CAlll.Action_ConnectedToGateway = false;
                    this.Log("IOException: ");
                    return CAlll;
                }
            }
        }
    }
}
