//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class BillPaymentValidation extends BaseMessage {
    protected String srcAccount;
    protected String amount;
    protected String company;
    protected String billID;
    protected String paymentID;
    protected String serviceCode;

    public BillPaymentValidation(String srcAccount, String amount, String company, String billID, String paymentID, String serviceCode) {
        this.srcAccount = srcAccount;
        this.amount = amount;
        this.company = company;
        this.billID = billID;
        this.paymentID = paymentID;
        this.serviceCode = serviceCode;
    }

    public BillPaymentValidation(String srcAccount, String amount, String billID, String paymentID) {
        this.srcAccount = srcAccount;
        this.amount = amount;
        this.billID = billID;
        this.paymentID = paymentID;
    }

    public BillPaymentValidation() {
        this.srcAccount = "";
        this.amount = "";
        this.company = "";
        this.billID = "";
        this.paymentID = "";
        this.serviceCode = "";
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

    public String getBillID() {
        return this.billID;
    }

    public String getPaymentID() {
        return this.paymentID;
    }

    public String getServiceCode() {
        return this.serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
