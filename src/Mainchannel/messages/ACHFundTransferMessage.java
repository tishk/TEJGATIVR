package Mainchannel.messages;

public class ACHFundTransferMessage extends FundTransferMessage {
    protected String senderName;
    protected String senderId;
    protected String depositId;
    protected String followupCode;
    protected String transDate;
    protected String transTime;
    protected String transStatus;
    protected String trackCode;
    protected String effectiveDate;
    protected String fileId;
    protected String fileSt;
    protected String flwFileName;

    public ACHFundTransferMessage() {
    }

    public ACHFundTransferMessage(String srcIban, String senderName, String senderId, String dstIban, String depositId, String amnt, String currency, String desc) {
        super.srcAccountNumber = srcIban;
        this.senderName = senderName;
        this.senderId = senderId;
        super.dstAccountNumber = dstIban;
        this.depositId = depositId;
        super.transAmount = amnt;
        super.currency = currency;
        super.transDesc = desc;
    }

    public ACHFundTransferMessage(String srcIBAN, String dstIBAN, String flwupCode) {
        super.srcAccountNumber = srcIBAN;
        super.dstAccountNumber = dstIBAN;
        this.followupCode = flwupCode;
    }

    public ACHFundTransferMessage(String fileId) {
        this.fileId = fileId;
    }

    public ACHFundTransferMessage(String fileId, String fileSt) {
        this.fileId = fileId;
        this.fileSt = fileSt;
    }

    public ACHFundTransferMessage(String srcIBAN, String followupCode, String transAmount, String transDate, String transTime, String transStatus) {
        super.srcAccountNumber = srcIBAN;
        this.followupCode = followupCode;
        super.transAmount = transAmount;
        this.transDate = transDate;
        this.transTime = transTime;
        this.transStatus = transStatus;
    }

    public String getSrcAccountNumber() {
        return this.srcAccountNumber;
    }

    public void setSrcAccountNumber(String srcIBAN) {
        super.setSrcAccountNumber(srcIBAN);
    }

    public String getDstAccountNumber() {
        return this.dstAccountNumber;
    }

    public void setDstAccountNumber(String dstIBAN) {
        super.setDstAccountNumber(dstIBAN);
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getDepositId() {
        return this.depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getFollowupCode() {
        return this.followupCode;
    }

    public void setFollowupCode(String followupCode) {
        this.followupCode = followupCode;
    }

    public String getTransStatus() {
        return this.transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getTransTime() {
        return this.transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransDate() {
        return this.transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public String getTrackCode() {
        return this.trackCode;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileSt() {
        return this.fileSt;
    }

    public void setFileSt(String fileSt) {
        this.fileSt = fileSt;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getFlwFileName() {
        return this.flwFileName;
    }

    public void setFlwFileName(String flwFileName) {
        this.flwFileName = flwFileName;
    }
}
