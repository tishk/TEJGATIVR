//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class FundTransferMessage extends BaseMessage {
    protected String srcAccountNumber;
    protected String dstAccountNumber;
    protected String reqsecuence;
    protected String transAmount;
    protected String accountPass;
    protected String blckNumber;
    protected String transDesc;
    protected String currency;
    protected String opCode;
    protected String fPayCode;
    protected String sPayCode;
    protected String cbPayId;
    protected String shpInf;
    protected String refNo;

    public FundTransferMessage() {
        this.srcAccountNumber = "";
        this.dstAccountNumber = "";
        this.transAmount = "";
        this.refNo = "";
        this.reqsecuence = "";
        this.currency = "";
        this.opCode = "";
        this.transDesc = "";
    }

    public FundTransferMessage(String srcAccountNumber, String dstAccountNumber, String transAmount, String currency, String transDesc, String opCode, String refNo) {
        this.srcAccountNumber = srcAccountNumber;
        this.dstAccountNumber = dstAccountNumber;
        this.transAmount = transAmount;
        this.refNo = refNo;
        this.currency = currency;
        this.opCode = opCode;
        this.transDesc = transDesc;
    }

    public String getSrcAccountNumber() {
        return this.srcAccountNumber;
    }

    public String getRefNo() {
        return this.refNo;
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

    public String getTransDesc() {
        return this.transDesc;
    }

    public String getAccountPass() {
        return this.accountPass;
    }

    public String getFPayCode() {
        return this.fPayCode;
    }

    public String getSPayCode() {
        return this.sPayCode;
    }

    public String getCbPayId() {
        return this.cbPayId;
    }

    public String getBlckNumber() {
        return this.blckNumber;
    }

    public String getShpInf() {
        return this.shpInf;
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

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public void setReqsecuence(String reqsecuence) {
        this.reqsecuence = reqsecuence;
    }

    public void setFPayCode(String fPayCode) {
        this.fPayCode = fPayCode;
    }

    public void setSPayCode(String sPayCode) {
        this.sPayCode = sPayCode;
    }

    public void setCbPayId(String cbPayId) {
        this.cbPayId = cbPayId;
    }

    public void setBlckNumber(String blckNumber) {
        this.blckNumber = blckNumber;
    }

    public void setShpInf(String shpInf) {
        this.shpInf = shpInf;
    }
}
