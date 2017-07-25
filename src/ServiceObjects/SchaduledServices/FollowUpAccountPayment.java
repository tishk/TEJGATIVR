package ServiceObjects.SchaduledServices;

import ServiceObjects.Account.FollowUpTransaction;
import ServiceObjects.Other.DBUtils;
import ServiceObjects.Other.PaymentOBJ;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 5/8/17.
 */
public class FollowUpAccountPayment {

    static int  HOURS =3;
    static int  MINUTES =0;
    static int  SECONDS =0;

    DBUtils dbUtils;
    ArrayList<PaymentOBJ> paymentOBJArrayList=new ArrayList<PaymentOBJ>();
    private long calculateStartTime(){
        Date timeToRun=new Date();
        timeToRun.setHours(HOURS);
        timeToRun.setMinutes(MINUTES);
        timeToRun.setSeconds(SECONDS);
        return new Date(timeToRun.getTime()-System.currentTimeMillis()).getTime();
    }

    public void scheduleFollowUp(){

        ScheduledExecutorService   scheduledExecutorService=
                Executors.newSingleThreadScheduledExecutor();

        ScheduledFuture scheduledFuture= scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                  //  doOperations();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },calculateStartTime(),30,TimeUnit.DAYS);

    }

    private void doOperations() throws SQLException, ClassNotFoundException {

        cashOfflinePaymentsFromeDataBase();
     /*   for (PaymentOBJ paymentOBJ:paymentOBJArrayList) {
            FollowUpTransaction  followUpTransaction=new FollowUpTransaction();
            followUpTransaction.setMsgSeq("400127362440");
            followUpTransaction.setSourceAccount(paymentOBJ.getSOURCEOFPAYMENT());
            followUpTransaction.setIsInternalFollowCode(false);
            followUpTransaction.setDestinationAccount(paymentOBJ.getDESTINATIONACCOUNT());
            followUpTransaction.setFollowUpCode(paymentOBJ.getFOLLOWCODE());

        }
*/
        System.out.println("hi... ");
    }

    private void cashOfflinePaymentsFromeDataBase() throws SQLException, ClassNotFoundException {
        dbUtils=new DBUtils();
        if (dbUtils.connected){
            paymentOBJArrayList= dbUtils.getOfflinePayments();
        }
    }


}
