//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class BillPaymentMessage extends BaseMessage {
    protected String reqSequence;
    protected String srcAccount;
    protected String amount;
    protected String company;
    protected String serviceCode;
    protected String billID;
    protected String paymentID;
    protected String refNo;
    protected String accountPass;

    public BillPaymentMessage(String reqSequence, String srcAccount, String amount, String company, String billID, String paymentID, String refNo, String serviceCode) {
        this.reqSequence = reqSequence;
        this.srcAccount = srcAccount;
        this.amount = amount;
        this.company = company;
        this.serviceCode = serviceCode;
        this.billID = billID;
        this.paymentID = paymentID;
        this.refNo = refNo;
    }

    public BillPaymentMessage(String srcAccount, String amount, String company, String billID, String paymentID, String refNo, String serviceCode) {
        this.srcAccount = srcAccount;
        this.amount = amount;
        this.company = company;
        this.serviceCode = serviceCode;
        this.billID = billID;
        this.paymentID = paymentID;
        this.refNo = refNo;
    }

    public BillPaymentMessage(String srcAccount, String amount, String billID, String paymentID) {
        this.srcAccount = srcAccount;
        this.amount = amount;
        this.billID = billID;
        this.paymentID = paymentID;
    }

    public BillPaymentMessage() {
        this.reqSequence = "";
        this.srcAccount = "";
        this.amount = "";
        this.company = "";
        this.serviceCode = "";
        this.billID = "";
        this.paymentID = "";
        this.refNo = "";
    }

    public String getReqSequence() {
        return this.reqSequence;
    }

    public String getSrcAccount() {
        return this.srcAccount;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getCompany() {
        return this.company;
    }

    public String getServiceCode() {
        return this.serviceCode;
    }

    public String getBillID() {
        return this.billID;
    }

    public String getPaymentID() {
        return this.paymentID;
    }

    public String getRefNo() {
        return this.refNo;
    }

    public String getAccountPass() {
        return this.accountPass;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }
}
