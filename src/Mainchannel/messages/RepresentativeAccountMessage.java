//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import java.util.Vector;

public class RepresentativeAccountMessage extends StatementListMessageReq {
    protected String ibanAcc;
    protected String blckId;
    protected String otherAcc;
    protected String date;
    protected String time;
    protected String effectiveDate;
    protected String lastAmnt;
    protected String balance;
    protected String tAmount;
    protected String opCode;
    protected Vector transaction;

    public RepresentativeAccountMessage() {
    }

    public RepresentativeAccountMessage(String accIban, Vector transaction, String transCount) {
        this.ibanAcc = accIban;
        this.transaction = transaction;
        this.transCount = transCount;
    }

    public void setIbanAcc(String ibanAcc) {
        this.ibanAcc = ibanAcc;
    }

    public void setBlckId(String blckId) {
        this.blckId = blckId;
    }

    public void setTransCount(String transCount) {
        this.transCount = transCount;
    }

    public void setTransaction(Vector transaction) {
        this.transaction = transaction;
    }

    public void setOtherAcc(String otherAcc) {
        this.otherAcc = otherAcc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setLastAmnt(String lastAmnt) {
        this.lastAmnt = lastAmnt;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void settAmount(String tAmount) {
        this.tAmount = tAmount;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public String getIbanAcc() {
        return this.ibanAcc;
    }

    public String getBlckId() {
        return this.blckId;
    }

    public String getTransCount() {
        return this.transCount;
    }

    public Vector getTransaction() {
        return this.transaction;
    }

    public String getOtherAcc() {
        return this.otherAcc;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public String getLastAmnt() {
        return this.lastAmnt;
    }

    public String getBalance() {
        return this.balance;
    }

    public String gettAmount() {
        return this.tAmount;
    }

    public String getOpCode() {
        return this.opCode;
    }

    public RepresentativeAccountMessage getRepAccMessage(int row) {
        return (RepresentativeAccountMessage)this.transaction.elementAt(row);
    }
}
