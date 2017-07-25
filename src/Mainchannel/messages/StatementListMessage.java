//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import java.util.Vector;

public class StatementListMessage extends BaseMessage {
    protected String accountNumber;
    private static String transactionsSeparator = "transaction";
    protected String fromDate;
    protected String toDate;
    protected Vector transaction;
    protected String transCount;
    protected String balance;
    protected String payId1;
    protected String payId2;
    protected String hostId;

    public StatementListMessage() {
        this.accountNumber = "";
        this.fromDate = "";
        this.toDate = "";
        this.transaction = null;
        this.transCount = "0";
        this.balance = "";
    }

    public StatementListMessage(String accountNumber, String balance, String fromDate, String toDate, String transCount, Vector transaction) {
        this.accountNumber = accountNumber;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.transaction = transaction;
        this.transCount = transCount;
        this.balance = balance;
    }

    public StatementListMessage(String accountNumber, String balance, String transCount, Vector transaction) {
        this.accountNumber = accountNumber;
        this.transaction = transaction;
        this.transCount = transCount;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public String getToDate() {
        return this.toDate;
    }

    public StatementMessage getStatementMessage(int transNumber) {
        return (StatementMessage)this.transaction.elementAt(transNumber);
    }

    public String getTransCount() {
        return this.transCount;
    }

    public String getBalance() {
        return this.balance;
    }

    public String getPayId1() {
        return this.payId1;
    }

    public void setPayId1(String payId1) {
        this.payId1 = payId1;
    }

    public String getPayId2() {
        return this.payId2;
    }

    public void setPayId2(String payId2) {
        this.payId2 = payId2;
    }

    public String getHostId() {
        return this.hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}
