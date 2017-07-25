//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class RTGSFundTransferMessage extends FundTransferMessage {
    private String srcBank;
    private String dstBank;
    private String srcBranchCode;
    private String dstBranchCode;
    private String senderName;
    private String senderFamily;
    private String senderPostalCode;
    private String senderNationalId;
    private String senderPhoneNo;
    private String senderAddress;
    private String recieverName;
    private String recieverFamily;
    private String recieverPostalCode;
    private String recieverNationalId;
    private String recieverPhoneNo;
    private String recieverAddress;
    private String payCode;
    private String trackCode;

    public RTGSFundTransferMessage(String srcBank, String dstBnk, String srcBrchId, String dstBrchId, String srcAccountNumber, String dstAccountNumber, String transAmount, String currency, String transDesc, String opCode, String senderName, String senderFamily, String senderPostalCode, String senderNationalId, String senderPhoneNo, String senderAddress, String recieverName, String recieverFamily, String recieverPostalCode, String recieverNationalId, String recieverPhoneNo, String recieverAddress, String payCode) {
        this.srcBank = srcBank;
        this.dstBank = dstBnk;
        this.srcBranchCode = srcBrchId;
        this.dstBranchCode = dstBrchId;
        this.senderName = senderName;
        this.senderFamily = senderFamily;
        this.senderPostalCode = senderPostalCode;
        this.senderNationalId = senderNationalId;
        this.senderPhoneNo = senderPhoneNo;
        this.senderAddress = senderAddress;
        this.recieverName = recieverName;
        this.recieverFamily = recieverFamily;
        this.recieverPostalCode = recieverPostalCode;
        this.recieverNationalId = recieverNationalId;
        this.recieverPhoneNo = recieverPhoneNo;
        this.recieverAddress = recieverAddress;
        this.payCode = payCode;
        super.srcAccountNumber = srcAccountNumber;
        super.dstAccountNumber = dstAccountNumber;
        super.transAmount = transAmount;
        super.currency = currency;
        super.transDesc = transDesc;
        super.opCode = opCode;
    }

    public RTGSFundTransferMessage(String srcBank, String dstBnk, String srcAccNo, String dstAccNo, String transAmount, String transDesc, String opCode, String refNo) {
        this.srcBank = srcBank;
        this.dstBank = dstBnk;
        super.srcAccountNumber = srcAccNo;
        super.dstAccountNumber = dstAccNo;
        super.transAmount = transAmount;
        super.transDesc = transDesc;
        super.opCode = opCode;
        super.refNo = refNo;
    }

    public RTGSFundTransferMessage() {
    }

    public void setSrcBank(String srcBank) {
        this.srcBank = srcBank;
    }

    public void setDstBank(String dstBank) {
        this.dstBank = dstBank;
    }

    public void setSrcBranchCode(String srcBranchCode) {
        this.srcBranchCode = srcBranchCode;
    }

    public void setDstBranchCode(String dstBranchCode) {
        this.dstBranchCode = dstBranchCode;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderFamily(String senderFamily) {
        this.senderFamily = senderFamily;
    }

    public void setSenderPostalCode(String senderPostalCode) {
        this.senderPostalCode = senderPostalCode;
    }

    public void setSenderNationalId(String senderNationalId) {
        this.senderNationalId = senderNationalId;
    }

    public void setSenderPhoneNo(String senderPhoneNo) {
        this.senderPhoneNo = senderPhoneNo;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public void setRecieverFamily(String recieverFamily) {
        this.recieverFamily = recieverFamily;
    }

    public void setRecieverPostalCode(String recieverPostalCode) {
        this.recieverPostalCode = recieverPostalCode;
    }

    public void setRecieverNationalId(String recieverNationalId) {
        this.recieverNationalId = recieverNationalId;
    }

    public void setRecieverPhoneNo(String recieverPhoneNo) {
        this.recieverPhoneNo = recieverPhoneNo;
    }

    public void setRecieverAddress(String recieverAddress) {
        this.recieverAddress = recieverAddress;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public String getSrcBank() {
        return this.srcBank;
    }

    public String getDstBank() {
        return this.dstBank;
    }

    public String getSrcBranchCode() {
        return this.srcBranchCode;
    }

    public String getDstBranchCode() {
        return this.dstBranchCode;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public String getSenderFamily() {
        return this.senderFamily;
    }

    public String getSenderPostalCode() {
        return this.senderPostalCode;
    }

    public String getSenderNationalId() {
        return this.senderNationalId;
    }

    public String getSenderPhoneNo() {
        return this.senderPhoneNo;
    }

    public String getSenderAddress() {
        return this.senderAddress;
    }

    public String getRecieverName() {
        return this.recieverName;
    }

    public String getRecieverFamily() {
        return this.recieverFamily;
    }

    public String getRecieverPostalCode() {
        return this.recieverPostalCode;
    }

    public String getRecieverNationalId() {
        return this.recieverNationalId;
    }

    public String getRecieverPhoneNo() {
        return this.recieverPhoneNo;
    }

    public String getRecieverAddress() {
        return this.recieverAddress;
    }

    public String getPayCode() {
        return this.payCode;
    }

    public String getTrackCode() {
        return this.trackCode;
    }
}
