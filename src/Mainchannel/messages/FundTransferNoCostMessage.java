package Mainchannel.messages;

/**
 * Created by Administrator on 2/16/2017.
 */
public class FundTransferNoCostMessage extends BaseMessage {

    protected String reqsecuence;
    protected String srcAccountNumber;
    protected String accountPass;
    protected String dstAccountNumber;
    protected String fType;
    protected String transAmount;
    protected String followupCode;
    protected String currency;
    protected String opCode;
    protected String payCode1;
    protected String payCode2;
    protected String blckNumber;
    protected String refCode;
    protected String ftDesc;

    public FundTransferNoCostMessage() {
        this.srcAccountNumber = "";
        this.dstAccountNumber = "";
        this.transAmount = "";
        this.fType = "";
        this.reqsecuence = "";
        this.currency = "";
        this.opCode = "";
        this.followupCode = "";
    }

    public FundTransferNoCostMessage(String srcAccountNumber, String dstAccountNumber, String transAmount, String currency,String ftDesc, String opCode, String refCode) {

        this.srcAccountNumber = srcAccountNumber;
        this.dstAccountNumber = dstAccountNumber;
        this.transAmount = transAmount;
        this.refCode = refCode;
        this.currency = currency;
        this.opCode = opCode;
        this.ftDesc = ftDesc;
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

    public String getReqsecuence() {
        return this.reqsecuence;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getOpCode() {
        return this.opCode;
    }



    public String getAccountPass() {
        return this.accountPass;
    }

    public String getFPayCode() {
        return this.payCode1;
    }

    public String getSPayCode() {
        return this.payCode2;
    }


    public String getBlckNumber() {
        return this.blckNumber;
    }


    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }

    public void setSrcAccountNumber(String srcAccountNumber) {
        this.srcAccountNumber = srcAccountNumber;
    }

    public void setDstAccountNumber(String dstAccountNumber) {
        this.dstAccountNumber = dstAccountNumber;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }



    public void setReqsecuence(String reqsecuence) {
        this.reqsecuence = reqsecuence;
    }

    public void setFPayCode(String fPayCode) {
        this.payCode1 = fPayCode;
    }

    public void setSPayCode(String sPayCode) {
        this.payCode2 = sPayCode;
    }


    public void setBlckNumber(String blckNumber) {
        this.blckNumber = blckNumber;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public String getFollowupCode() {
        return followupCode;
    }

    public void setFollowupCode(String followupCode) {
        this.followupCode = followupCode;
    }

    public String getPayCode1() {
        return payCode1;
    }

    public void setPayCode1(String payCode1) {
        this.payCode1 = payCode1;
    }

    public String getPayCode2() {
        return payCode2;
    }

    public void setPayCode2(String payCode2) {
        this.payCode2 = payCode2;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getFtDesc() {
        return ftDesc;
    }

    public void setFtDesc(String ftDesc) {
        this.ftDesc = ftDesc;
    }
}
