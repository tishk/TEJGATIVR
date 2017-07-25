import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import SystemStatus.Status_All;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 4/27/16.
 */
public class ProcessRequest {

    String request=null;
    String uniqueID=null;
    String requestType=null;
    String requestParameters[]=null;
    String account=null;
    String pin1=null;
    String pin2=null;
    String pan=null;
    String pinOfPan=null;
    String response=null;
    String tempResponse="";
    String serviceCode="";

    public ProcessRequest(String req) throws SQLException, ClassNotFoundException {
        this.request=req;
        this.requestParameters=request.split("#");
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
           Future future = executorService.submit(new Runnable() {
                    public void run() {
                        try {
                            process();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
               });




    }

    private void process() throws SQLException, ClassNotFoundException {
        switch (getKindOfRequest()){
            case "services"  :processServices();
                break;
            case "service"   :processService();
                break;
            case "telbanks"  :processTelbanks();
                break;
            case "telbank"   :processTelbank(requestParameters);
                break;
            case "all"       :processAll();
                break;
            case "osgateway" :processOSofGateway();
                break;
            case "osdbserver":processOSofDBServer();
                break;
            case "ostelbank" :processOSofTelbank(requestParameters);
                break;
            case "ostelbanks":processOSofTelbanks();
                break;
            default:processInvalidRequest();
        }
    }

    private String getKindOfRequest(){
        try{
            requestParameters=request.split("#");
            switch (requestParameters[1]){
                case "services" :return "services";
                case "service"  :return "service";
                case "telbanks" :return "telbanks";
                case "telbank"  :return "telbank";
                case "all"      :return "all";
                case "os"       :{
                                    switch (requestParameters[2]){
                                        case "gateway":return "osgateway";
                                        case "dbserver":return "osdbserver";
                                        case "telbank":return "ostelbank";
                                        case "telbanks":return "ostelbanks";
                                        default:return "InvalidType";
                                    }
                                }

                default:return "InvalidType";
            }
        }catch (Exception e){
            return "Invalid Request";
        }
    }

    public  String getResponse(){
        return response;
    }

    private boolean accountParametersIsOK(String accParams){
        try{
            String accparams[]=accParams.split("@");
            account=accparams[0];
            pin1=accparams[1];
            pin2=accparams[2];
            pan=accparams[3];
            pinOfPan=accparams[4];
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void processServices() throws SQLException, ClassNotFoundException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
           public void run() {
                try {
                    if (requestParameters.length!=3)
                        response=makeInvalidEntryResponse();
                    else{
                        if (accountParametersIsOK(requestParameters[2])) new AllServicesOfGateway(requestParameters);
                        else new DBUtils(makeInvalidEntryResponse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                   executorService.shutdownNow();
                }
            }
        });

    }

    private void processService(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    if (requestParameters.length!=4)
                        new DBUtils(makeInvalidEntryResponse());
                    else{
                        if (accountParametersIsOK(requestParameters[2])){
                            try {
                                serviceCode=requestParameters[3];
                                Gateway gateway=null;
                                Authenticate authenticate=null;
                                switch (serviceCode){
                                    case "01": gateway=new Gateway(requestParameters,serviceCode);
                                        new DBUtils(gateway);
                                        break;
                                    case "02":gateway=new Gateway(requestParameters,serviceCode);
                                        new DBUtils(gateway);
                                        break;
                                    case "08":gateway=new Gateway(requestParameters,serviceCode);
                                        new DBUtils(gateway);
                                        break;
                                    case "03":Channel channel=new Channel(requestParameters);
                                        new DBUtils(channel);
                                        break;
                                    case "04":CardSwitch cardSwitch=new CardSwitch(requestParameters);
                                        new DBUtils(cardSwitch);
                                        break;
                                    case "05":TelSwitch telSwitch=new TelSwitch(requestParameters);
                                        new DBUtils(telSwitch);
                                        break;
                                    case "06":authenticate=new Authenticate(requestParameters,true);
                                        new DBUtils(authenticate);
                                        break;
                                    case "07":authenticate=new Authenticate(requestParameters,false);
                                        new DBUtils(authenticate);
                                        break;
                                    default  :makeInvalidEntryResponse();
                                }
                            }catch (Exception e){
                                new DBUtils(makeInvalidEntryResponse());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });

    }

    private void processTelbank(final String[] request){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                   Telbank telbank= new Telbank(request,false);
                    new DBUtils(telbank);
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });

    }

    private void processTelbanks() throws SQLException, ClassNotFoundException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    String telBankRequest[]=new String[4];
                    String clients[]=Properties_Monitoring.getTelbankList();
                    String client[]=null;
                    for (int i=0;i<=clients.length;i++){
                        client=clients[i].split("#");
                        telBankRequest[0]=requestParameters[0];
                        telBankRequest[1]="telbank";
                        telBankRequest[2]=client[0];
                        telBankRequest[3]=requestParameters[2];
                        processTelbank(telBankRequest);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });

    }

    private void processAll() throws SQLException, ClassNotFoundException {
        processServices();
        processTelbanks();
        processOSofAll();

    }

    private void processOSofAll(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    processOSofGateway();
                    processOSofTelbanks();
                    processOSofDBServer();
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });
    }

    private void processOSofGateway(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    Status_All status_all=new Status_All();
                    status_all.setIsOSOfGateway(true);
                    status_all=(Status_All) submitRequestToGateway(status_all);

                    new DBUtils(status_all);

                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });
    }

    private void processOSofDBServer(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {


                    String  req="osofdb";

                    int port=Integer.valueOf(Properties_Monitoring.getDBMonitoring_listener_Port());
                    String ip=Properties_Monitoring.getDBMonitoring_listener_IP();


                    InetAddress inet = InetAddress.getByName(ip);
                    if (inet.isReachable(2000)) {
                        try
                        {
                            Socket client = new Socket(ip, port);
                            OutputStream outToServer = client.getOutputStream();
                            DataOutputStream out = new DataOutputStream(outToServer);
                            out.writeBytes(req);
                            client.close();
                        }catch(IOException e) {}

                    }
                    else {
                        Status_All status_all=new Status_All();
                        status_all.setIsDataBaseServer(true);
                        status_all.setPingStatus("0");
                        status_all.setPingStatusActionCode("9126");
                        status_all.setPingStatusDateTime(status_all.getNowDateTime());
                        status_all.setPingStatusDesc("---");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });
    }

    private void processOSofTelbanks(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    String telBankRequest[]=new String[4];
                    String clients[]=Properties_Monitoring.getTelbankList();
                    String client[]=null;
                    for (int i=0;i<=clients.length;i++){
                        client=clients[i].split("#");
                        telBankRequest[0]=requestParameters[0];
                        telBankRequest[1]="os";
                        telBankRequest[2]=client[0];
                        telBankRequest[3]=requestParameters[2];
                        processOSofTelbank(telBankRequest);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });


    }

    private void processOSofTelbank(final String[] request){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    Telbank telbank=new Telbank(request,true);
                    new DBUtils(telbank);
                } catch (Exception e) {
                    e.printStackTrace();
                   // executorService.shutdownNow();
                }finally {
                   // executorService.shutdownNow();
                }
            }
        });
    }

    private void processInvalidRequest(){

    }

    private String makeInvalidEntryResponse(){
        return uniqueID+"#service#"+serviceCode+"#InvalidRequest";
    }

    public  Object submitRequestToGateway(Object object) throws IOException, ServerNotActiveException {
        Bank b=null;
        try {
            b=(Bank) Naming.lookup("rmi://" + Properties_Monitoring.getGateway_IP()+":" +Properties_Monitoring.getGateway_Port()+ "/Gateway");
            object=b.submitRequest(object);

        } catch (NotBoundException e) {


        } catch (MalformedURLException e) {

        } catch (RemoteException e) {

        } catch (InvalidParameterException e) {

        } catch (ResponseParsingException e) {

        } catch (SenderException e) {

        } catch (SQLException e) {

        }

        return object;
    }
}
