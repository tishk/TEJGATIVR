import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.Transaction;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

/**
 * Created by root on 7/6/17.
 */
public class main {

    public static void main(String[] arg) throws IOException, ServerNotActiveException {

//        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
//                    testPin1();
//            testTransactionarzi();
    }
    private static void testTransactionarzi() throws IOException, ServerNotActiveException {
        Transaction transaction=new Transaction();
        transaction.setAccountNumber("0002361108");
        transaction.setStatementType("8");
        transaction.setTransactionCount("3");
        //transaction.setMAC(call.getMACAddress());
        transaction.setCallUniqID("123456789987654");

        transaction=(Transaction) submitRequestToGateway(transaction);
        System.out.println(transaction.getTransactionCount());
    }

    private static void testPin1() throws IOException, ServerNotActiveException {

        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
        authenticatePin1.setAccountNumber("3435531082");
        authenticatePin1.setPin("0231");
        authenticatePin1.setCallerID("09379707473");
        authenticatePin1.setDoChangePin(false);
        authenticatePin1.setCallUniqID("7777777777777");
        authenticatePin1=(AuthenticatePin1)submitRequestToGateway(authenticatePin1);
        System.out.println(authenticatePin1.getActionCode());



    }

    private static void testPin2() throws IOException, ServerNotActiveException {

        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        authenticatePin2.setAccountNumber("0039129981");
        authenticatePin2.setPin("8989");
        authenticatePin2.setCallerID("09379707473");
//        authenticatePin2.setDoChangePin(false);
        authenticatePin2.setCallUniqID("7777777777777");
        authenticatePin2=(AuthenticatePin2)submitRequestToGateway(authenticatePin2);
        System.out.println(authenticatePin2.getActionCode());


    }
    public static Object submitRequestToGateway(Object object) throws IOException, ServerNotActiveException {
        Bank b=null;
        try {
            b=(Bank) Naming.lookup("rmi://10.39.41.62:1364/Gateway");
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

}
