//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class FundTransferMessageReverse extends BaseMessage {
    protected String srcAccountNumber;
    protected String dstAccountNumber;
    protected String transAmount;
    protected String afterTransBalance;
    protected String ftDesc;
    protected String ftDate;
    protected String ftTime;
    protected String refNo;
    protected String channelType;
    protected String channelID;
    protected String reqsecuence;

    public FundTransferMessageReverse() {
        this.channelType = "";
        this.channelID = "";
        this.srcAccountNumber = "";
        this.dstAccountNumber = "";
        this.transAmount = "";
        this.ftDesc = "";
        this.afterTransBalance = "";
        this.ftTime = "";
        this.ftDate = "";
        this.refNo = "";
        this.reqsecuence = "";
    }

    public FundTransferMessageReverse(String reqsecuence, String channelType, String channelID, String srcAccountNumber, String dstAccountNumber, String transAmount, String ftDesc, String afterTransBalance, String ftDate, String ftTime, String refNo) {
        this.reqsecuence = reqsecuence;
        this.srcAccountNumber = srcAccountNumber;
        this.dstAccountNumber = dstAccountNumber;
        this.transAmount = transAmount;
        this.ftDesc = ftDesc;
        this.afterTransBalance = afterTransBalance;
        this.ftDate = ftDate;
        this.ftTime = ftTime;
        this.refNo = refNo;
        this.channelType = channelType;
        this.channelID = channelID;
    }

    public String getSrcAccountNumber() {
        return this.srcAccountNumber;
    }

    public String getRefNo() {
        return this.refNo;
    }

    public String getAfterTransBalance() {
        return this.afterTransBalance;
    }

    public String getDstAccountNumber() {
        return this.dstAccountNumber;
    }

    public String getTransAmount() {
        return this.transAmount;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public String getReqsecuence() {
        return this.reqsecuence;
    }

    public String getFtDate() {
        return this.ftDate;
    }

    public String getFtTime() {
        return this.ftTime;
    }

    public String getFtDesc() {
        return this.ftDesc;
    }

    public String toString() {
        return super.toString() + "/[reqsecuence]" + this.reqsecuence + "/[srcAccountNumber]" + this.srcAccountNumber + "/[dstAccountNumber]" + this.dstAccountNumber + "/[transAmount]" + this.transAmount + "/[ftDesc]" + this.ftDesc + "/[afterTransBalance]" + this.afterTransBalance + "/[ftDate]" + this.ftDate + "/[ftTime]" + this.ftTime + "/[refNo]" + this.refNo + "/[channelType]" + this.channelType + "/[channelID]" + this.channelID;
    }
}
