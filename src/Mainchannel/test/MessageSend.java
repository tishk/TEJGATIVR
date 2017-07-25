//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.test;

import Mainchannel.MainMQ;
import Mainchannel.util.DbUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MessageSend {
    private static Logger log = Logger.getLogger(MessageSend.class);
    static int i = 0;
    static String lastMsgSeq;

    public MessageSend() {
    }

    synchronized int getI() {
        return i++;
    }

    void makeThread(int threadNumber) {
        String account = "593281";

        for(int j = 0; j < threadNumber; ++j) {
            (new MessageSend.Thread1(account, System.currentTimeMillis())).start();

            try {
                Thread.sleep(10L);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

    }

    public static synchronized String getMsgSequence() {
        String seq;
        do {
            seq = String.valueOf(System.nanoTime()).substring(0,12);
            System.out.println("seq is:" + seq);
        } while(seq.equals(lastMsgSeq));

        lastMsgSeq = seq;
        return seq;
    }

    public static void main(String[] args) {
        try {
            MainMQ.Init();
            String e = "0000751084366";
            String amount = "114000";
            String paymentId = "11407289";
            String billId = "56038111227";
           // System.out.println("response = " + MainMQ.getCustomerChequeListStr(getMsgSequence(), "CB", "1234", "15", "400112887", "1234", "13640712", "13930915", "", "", "3", "13930915090352", "1", "1"));
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static List getAccountNumbers(int count) {
        ArrayList accounts = new ArrayList();

        try {
            DbUtil e = new DbUtil("classes/db2.properties");
            Connection toConnection = e.connect();
            Statement accountSt = toConnection.createStatement(1004, 1008);
            Statement deviceSt = toConnection.createStatement(1004, 1008);
            toConnection.setAutoCommit(false);
            String accountSql = "select ACCOUNT_NO  from tbcustomersrv where HOST_ID = 1 fetch first " + count + " rows only with ur";
            ResultSet result = accountSt.executeQuery(accountSql);

            while(result.next()) {
                accounts.add(result.getString("ACCOUNT_NO"));
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return accounts;
    }

    class Thread1 extends Thread {
        String account;
        long fireTime;
        int id;

        Thread1(String account, long fireTime) {
            this.account = account;
            this.fireTime = fireTime;
            this.id =MessageSend.this.getI();
        }

        public void run() {
            try {
                long e = System.currentTimeMillis();
                System.out.println(System.currentTimeMillis() - e);
                ++this.id;
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    }
}
