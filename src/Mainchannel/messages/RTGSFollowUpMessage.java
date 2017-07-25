//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class RTGSFollowUpMessage extends BaseMessage {
    private String srcBankId;
    private String srcAccNo;
    private String dstBankId;
    private String dstAccNo;
    private String followUpCode;
    private String transAmount;
    private String transDate;
    private String transTime;
    private String transSatus;
    private String trackCode;
    private String brchId;

    public RTGSFollowUpMessage() {
    }

    public RTGSFollowUpMessage(String srcBankId, String srcAccNo, String dstBankId, String dstAccNo, String followUpCode) {
        this.srcBankId = srcBankId;
        this.srcAccNo = srcAccNo;
        this.dstBankId = dstBankId;
        this.dstAccNo = dstAccNo;
        this.followUpCode = followUpCode;
    }

    public RTGSFollowUpMessage(String srcBankId, String srcAccNo, String dstBankId, String dstAccNo, String followUpCode, String transAmount, String transDate, String transTime, String transSatatus) {
        this.srcBankId = srcBankId;
        this.srcAccNo = srcAccNo;
        this.dstBankId = dstBankId;
        this.dstAccNo = dstAccNo;
        this.followUpCode = followUpCode;
        this.transAmount = transAmount;
        this.transDate = transDate;
        this.transTime = transTime;
        this.transSatus = transSatatus;
    }

    public String getSrcBankId() {
        return this.srcBankId;
    }

    public String getSrcAccNo() {
        return this.srcAccNo;
    }

    public String getFollowUpCode() {
        return this.followUpCode;
    }

    public String getDstBankId() {
        return this.dstBankId;
    }

    public String getDstAccNo() {
        return this.dstAccNo;
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

    public String getTransSatus() {
        return this.transSatus;
    }

    public String getTrackCode() {
        return this.trackCode;
    }

    public String getBrchId() {
        return this.brchId;
    }

    public void setSrcBankId(String srcBankId) {
        this.srcBankId = srcBankId;
    }

    public void setSrcAccNo(String srcAccNo) {
        this.srcAccNo = srcAccNo;
    }

    public void setFollowUpCode(String followUpCode) {
        this.followUpCode = followUpCode;
    }

    public void setDstBankId(String dstBankId) {
        this.dstBankId = dstBankId;
    }

    public void setDstAccNo(String dstAccNo) {
        this.dstAccNo = dstAccNo;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public void setTransSatus(String transSatus) {
        this.transSatus = transSatus;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public void setBrchId(String brchId) {
        this.brchId = brchId;
    }
}
