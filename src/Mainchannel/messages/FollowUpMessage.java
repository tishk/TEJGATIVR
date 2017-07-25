//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class FollowUpMessage extends BaseMessage {
    protected String followUpCode;
    protected String srcAccountNumber;
    protected String dstAccountNumber;
    protected String transAmount;
    protected String transDate;
    protected String transTime;
    protected String transStatus;
    protected String origTransDateTime;
    protected String billId;
    protected String payId;
    protected String brnch;

    public FollowUpMessage(String srcAccountNumber, String dstAccountNumber, String transAmount, String followUpCode, String transDate, String transTime, String transStatus) {
        this.srcAccountNumber = srcAccountNumber;
        this.dstAccountNumber = dstAccountNumber;
        this.followUpCode = followUpCode;
        this.transAmount = transAmount;
        this.transDate = transDate;
        this.transTime = transTime;
        this.transStatus = transStatus;
    }

    public FollowUpMessage() {
        this.srcAccountNumber = "";
        this.followUpCode = "";
        this.dstAccountNumber = "";
        this.transAmount = "";
        this.transDate = "";
        this.transTime = "";
        this.transStatus = "";
    }

    public FollowUpMessage(String billId, String payId) {
        this.billId = billId;
        this.payId = payId;
    }

    public FollowUpMessage(String billAmnt, String payBrnch, String payDate, String payTime) {
        this.transAmount = billAmnt;
        this.brnch = payBrnch;
        this.transDate = payDate;
        this.transTime = payTime;
    }

    public FollowUpMessage(String srcAccNo, String dstAccNo, String flwupCode) {
        this.srcAccountNumber = srcAccNo;
        this.dstAccountNumber = dstAccNo;
        this.followUpCode = flwupCode;
    }

    public String getFollowUpCode() {
        return this.followUpCode;
    }

    public String getSrcAccountNumber() {
        return this.srcAccountNumber;
    }

    public String getDstAccountNumber() {
        return this.dstAccountNumber;
    }

    public String getTransAmount() {
        return this.transAmount;
    }

    public String getTransDate() {
        return this.transDate;
    }

    public String getTransTime() {
        return this.transTime;
    }

    public String getTransStatus() {
        return this.transStatus;
    }

    public String getOrigTransDateTime() {
        return this.origTransDateTime;
    }

    public String getBillId() {
        return this.billId;
    }

    public String getPayId() {
        return this.payId;
    }

    public String getBrnch() {
        return this.brnch;
    }

    public void setOrigTransDateTime(String origTransDateTime) {
        this.origTransDateTime = origTransDateTime;
    }
}
