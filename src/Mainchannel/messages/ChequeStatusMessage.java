//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class ChequeStatusMessage extends BaseMessage {
    protected String date;
    protected String time;
    protected String chequeNumber;
    protected String chequeStatus;
    protected String chequeAmount;
    protected String chequeOpDate;
    protected String chequeStatusDesc;
    protected String accountNumber;
    protected String chequeTime;
    protected String chequePayee;
    protected String chequeReson;
    protected String chqRgDt;
    protected String chqRgTm;

    public ChequeStatusMessage() {
    }

    public ChequeStatusMessage(String accountNumber, String chequeNumber, String chequeAmount, String chequeOpDate) {
        this.accountNumber = accountNumber;
        this.chequeNumber = chequeNumber;
        this.chequeAmount = chequeAmount;
        this.chequeOpDate = chequeOpDate;
    }

    public ChequeStatusMessage(String chequeNumber, String chequeStatus, String chequeAmount, String date, String time, String payee, String reason) {
        this.chequeNumber = chequeNumber;
        this.chequeStatus = chequeStatus;
        this.chequeAmount = chequeAmount;
        this.date = date;
        this.time = time;
        this.chequePayee = payee;
        this.chequeReson = reason;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getChequeNumber() {
        return this.chequeNumber;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public String getChequeStatus() {
        return this.chequeStatus;
    }

    public String getChequeAmount() {
        return this.chequeAmount;
    }

    public String getChequeOpDate() {
        return this.chequeOpDate;
    }

    public String getChequeStatusDesc() {
        return this.chequeStatusDesc;
    }

    public void setChequeStatusDesc(String chequeStatusDesc) {
        this.chequeStatusDesc = chequeStatusDesc;
    }

    public void setChequeStatus(String chequeStatus) {
        this.chequeStatus = chequeStatus;
    }

    public String getChequeTime() {
        return this.chequeTime;
    }

    public void setChequeTime(String chequeTime) {
        this.chequeTime = chequeTime;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setChequeOpDate(String chequeOpDate) {
        this.chequeOpDate = chequeOpDate;
    }

    public void setChequeAmount(String chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChequePayee() {
        return this.chequePayee;
    }

    public void setChequePayee(String chequePayee) {
        this.chequePayee = chequePayee;
    }

    public String getChequeReson() {
        return this.chequeReson;
    }

    public void setChequeReson(String chequeReson) {
        this.chequeReson = chequeReson;
    }

    public String getChqRgDt() {
        return this.chqRgDt;
    }

    public void setChqRgDt(String chqRgDt) {
        this.chqRgDt = chqRgDt;
    }

    public String getChqRgTm() {
        return this.chqRgTm;
    }

    public void setChqRgTm(String chqRgTm) {
        this.chqRgTm = chqRgTm;
    }
}
