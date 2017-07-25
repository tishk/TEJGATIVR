import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.MainMQ;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.BillPayByIDAccount;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
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
    static String ipOfServer;
    static String portOfServer;
    static int threadCount;
    static ArrayList<String> accounts;

    public static void main(String parameters[]) throws Exception {

        Test_BillPayment(parameters);
    }

    public static Object submitRequestToGateway(Object object) throws IOException, ServerNotActiveException {
        Bank b = null;
        // BaseAccountRequest baseAccountRequest=new BaseAccountRequest();

        try {
            // Util.printMessage("******************in request:",false);
            b = (Bank) Naming.lookup("rmi://10.39.41.62:1364/Gateway");
            object = b.submitRequest(object);


        } catch (NotBoundException e) {
            Util.printMessage("1:" + e.toString(), false);
//            baArzePozesh();

        } catch (MalformedURLException e) {
            Util.printMessage("2:" + e.toString(), false);
//            baArzePozesh();

        } catch (RemoteException e) {
            Util.printMessage("3:" + e.toString(), false);
//            baArzePozesh();

        } catch (InvalidParameterException e) {
            Util.printMessage("4:" + e.toString(), false);
//            baArzePozesh();

        } catch (ResponseParsingException e) {
            Util.printMessage("5:" + e.toString(), false);
//            baArzePozesh();

        } catch (SenderException e) {
            Util.printMessage("6:" + e.toString(), false);
//            baArzePozesh();

        } catch (SQLException e) {
            Util.printMessage("7:" + e.toString(), false);
//            baArzePozesh();
        } catch (Exception e) {
            Util.printMessage("8:" + e.toString(), false);
        }


        return object;
    }

    private static void Test_BillPayment(String params[]) throws RemoteException {

        MainMQ AccountServices = new MainMQ();
        try {
            AccountServices.Init();
            BillPayByIDAccount b = new BillPayByIDAccount();
            b.setSourceAccount("3435531082");
            b.setAmount("155000");
            b.setBillID("9488346204120");
            b.setPaymentID("15540267");
            while (true) {
                //gatewayServices.accountInformation(null);
                b = (BillPayByIDAccount) submitRequestToGateway(b);
                System.out.println("Action Code : " + b.getResultFromChannel().getAction_code());
                //System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
                //System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
                System.out.println("XML Response is : " + b.getResultFromChannel().getResponseXml());
                Thread.sleep(5000);
            }


        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
