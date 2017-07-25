import ServiceObjects.Account.Transaction;
import ServiceObjects.Other.BillPaySayByTelNumber;
import ServiceObjects.Pan.BalanceForCard;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pin.AuthenticatePin1;
import SystemStatus.Status_All;
import SystemStatus.Status_CPU;
import org.asteriskjava.fastagi.AgiException;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

/**
 * Created by Administrator on 12/23/2015.
 */
public class testClass {
    private Call call=new Call();
    BillPaySayByTelNumber billPaySayByTelNumber=new BillPaySayByTelNumber();
    Voices say=new Voices();
    public testClass(Call c){
        call=c;
    }
    public testClass() throws IOException {
        Util.printMessage("in test class",false);
        testSystemStatus();
    }
    public void balanceForCard() throws IOException, RemoteException, SQLException, AgiException, ServerNotActiveException {
        BalanceForCard balanceForCard=new BalanceForCard();
        balanceForCard.setPan("5859831000001247");
        balanceForCard.setPin("259100");
        Util.printMessage("in test balance ",false);
        balanceForCard=(BalanceForCard)call.submitRequestToGateway(balanceForCard);
        Util.printMessage(balanceForCard.getActionCode(),false);
        Util.printMessage(balanceForCard.getResultFromServer().getActualBalance(),false);
    }
    public void billpaymentMenu() throws Exception {
        Util.printMessage("in bill test",false);
        call.setPin("259100");
        call.setPan("585983100001247");
        Service_BillPayment service_billPayment= new Service_BillPayment(call, true);
        service_billPayment=null;
    }
    public void billpaymentCard() throws Exception {
        BillPayByBillIDPan billPayByIDPan=new BillPayByBillIDPan();
        billPayByIDPan.setPan("5859831000001247");
        billPayByIDPan.setPin("259100");
        billPayByIDPan.setBillID("9488346204120");
        billPayByIDPan.setPaymentID("3620501");
        Util.printMessage("in bill test ",false);
        billPayByIDPan= (BillPayByBillIDPan)call.submitRequestToGateway(billPayByIDPan);
        String actionCode=billPayByIDPan.getActionCode();
        Util.printMessage(actionCode,false);
//        /Util.printMessage("",false);
    }
    public void billpaymenttelnumber() throws Exception {
        BillPayByBillIDPan billPayByIDPan=new BillPayByBillIDPan();
        billPayByIDPan.setPan("5859831000001247");
        billPayByIDPan.setPin("259100");
        billPayByIDPan.setBillID("9488346204120");
        billPayByIDPan.setPaymentID("3620501");
        Util.printMessage("in bill test ",false);
        billPayByIDPan= (BillPayByBillIDPan)call.submitRequestToGateway(billPayByIDPan);
        String actionCode=billPayByIDPan.getActionCode();
        Util.printMessage(actionCode,false);
//        /Util.printMessage("",false);
    }
    public void BillPayHappened() throws Exception {
        int i=0;
        Util.printMessage("in test say bill",false);
     /*   billPaySayByTelNumber.setPan("6037991467916494");
        billPaySayByTelNumber.setTraceCode("523146");
        billPaySayByTelNumber.setReferenceCode("412563698574");
        billPaySayByTelNumber.setBillID("9488346204120");
        billPaySayByTelNumber.setPaymentID("3620501");
        billPaySayByTelNumber.setIsMobile(false);
        billPaySayByTelNumber.setPayDate("13921015");
        billPaySayByTelNumber.setTelNo("66893627");

       // BillPaySayByTelNumber billPaySayByTelNumber=new BillPaySayByTelNumber();
*/

        billPaySayByTelNumber.setPan("3901510465");
        billPaySayByTelNumber.setTraceCode("837104");
        billPaySayByTelNumber.setReferenceCode("100038892971");
        billPaySayByTelNumber.setIsMobile(true);
        billPaySayByTelNumber.setBillID("2567100809050");
        billPaySayByTelNumber.setPaymentID("14442807");
        billPaySayByTelNumber.setPayDate("151205");
        billPaySayByTelNumber.setTelNo("09125671008");
        billPaySayByTelNumber.setIsMobile(true);



        Util.printMessage("in test say bill2",false);
       // while (i++<3){
            billPaySayByTelNumber=(BillPaySayByTelNumber)call.submitRequestToGateway(billPaySayByTelNumber);
        Util.printMessage("ActionCode  Is:" + billPaySayByTelNumber.getActionCode(),false);
            Util.printMessage("in test say bill3",false);
            if (billPaySayByTelNumber.getActionCode().equals("0000")){
                Util.printMessage("in test say bill4",false);
                say.paySuccess();
                say.shomarePeygireiShma();
                say.SayPersianDigitsSeparate("523146");
                say.mibashad();
              //  break;
            }

       // }
        Util.printMessage("in test say bill5",false);
    }

    public void accountautPin1() throws IOException, RemoteException, SQLException, AgiException, ServerNotActiveException {
        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
        authenticatePin1.setAccountNumber("3435531082");
        authenticatePin1.setPin("9221");
        authenticatePin1.setCallerID(call.getCallerID());
        authenticatePin1.setDoChangePin(false);
        authenticatePin1=(AuthenticatePin1)call.submitRequestToGateway(authenticatePin1);
        if (authenticatePin1.getActionCode().equals("0000")) Util.printMessage("Yesk",false);
        else call.getParentStart().Say.errorCode(authenticatePin1.getActionCode());
    }
    public void sayAmount() throws IOException, RemoteException, SQLException, AgiException {
       say.SayPersianDigitsSeparate("02166893627");
    }

    public void say3trans() throws IOException, AgiException, InterruptedException, ServerNotActiveException {
        Util.printMessage("in 3 trans",false);
        Transaction transaction=new Transaction();
        transaction.setAccountNumber("3435010031");
        transaction.setStatementType("8");
        transaction.setTransactionCount("3");
        //transaction.setStartDate("13930101");
        //transaction.setEndDate("13940529");
       // transaction.setStartTime("000000");
       // transaction.setEndTime("235959");
        // b.setCreditOrDebit("C");
        transaction.setrrn("");
        transaction=(Transaction)call.submitRequestToGateway(transaction);
        Thread.sleep(5000);
        Util.printMessage("in 3 trans actioncode:"+transaction.getResultFromCM().getAction_code().trim().toString(),false);
    }
    public void changePin1() throws IOException, AgiException, ServerNotActiveException {
        AuthenticatePin1 authenticatePin1 = new AuthenticatePin1();
        authenticatePin1.setAccountNumber("3435531082");
        authenticatePin1.setCallerID(call.getCallerID());
        authenticatePin1.setDoChangePin(true);
        authenticatePin1 = (AuthenticatePin1) call.submitRequestToGateway(authenticatePin1);
        if (!authenticatePin1.getActionCode().equals("0000")) {
            call.getParentStart().Say.pinChanged();
        } else {
            call.getParentStart().Say.errorCode(authenticatePin1.getActionCode());
            call.getParentStart().Say.pinNotChanged();

        }
    }
    public void faxMenu() throws Exception {
        Util.printMessage("in test fax",false);
        new Service_Account_Fax(call);
    }
    public void testSystemStatus() throws IOException {
        Util.printMessage("in test Calss",false);
        Status_All status_all=new Status_All();
        try {
           // status_all.processSystemStatus();
            Status_CPU status_cpu=new Status_CPU();

            status_cpu=null;
            Util.printMessage("CPU Usage is:"+status_cpu.getCpuIdleTime(),false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SigarException e) {
            Util.printMessage(e.toString(),false);
        }
    }
}
