//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class deliverC2CMessageFromSATNA2FaragirReq extends RTGSBaseMessageRequest {
    static String deliverMessageStatusFromSATNA2FaragirCommand = "32";
    protected String reqType;
    protected String createDate;
    protected String serial;
    protected String nameSend;
    protected String addressSend;
    protected String tellSend;
    protected String srlAccountSend;
    protected String branchSend;
    protected String bankSend;
    protected String nameRecive;
    protected String addressRecive;
    protected String tellRecive;
    protected String srlAccountRecive;
    protected String branchRecive;
    protected String bankRecive;
    protected String amount;
    protected String status;
    protected String step;
    protected String createUser;
    protected String description;

    public deliverC2CMessageFromSATNA2FaragirReq() {
        this.setCommand(deliverMessageStatusFromSATNA2FaragirCommand);
    }

    public deliverC2CMessageFromSATNA2FaragirReq(String sessionID, String service, String cmUserID, String cmUserPWD, String date, String time, String deviceID, String customerID, String customerPWD, String additionalData, String reqType, String createDate, String serial, String nameSend, String addressSend, String tellSend, String srlAccountSend, String branchSend, String bankSend, String nameRecive, String addressRecive, String tellRecive, String srlAccountRecive, String branchRecive, String bankRecive, String amount, String status, String step, String createUser, String description) {
        super(deliverMessageStatusFromSATNA2FaragirCommand, sessionID, service, cmUserID, cmUserPWD, date, time, deviceID, customerID, customerPWD, additionalData);
        this.reqType = reqType;
        this.createDate = createDate;
        this.serial = serial;
        this.nameSend = nameSend;
        this.addressSend = addressSend;
        this.tellSend = tellSend;
        this.srlAccountSend = srlAccountSend;
        this.branchSend = branchSend;
        this.bankSend = bankSend;
        this.nameRecive = nameRecive;
        this.addressRecive = addressRecive;
        this.tellRecive = tellRecive;
        this.srlAccountRecive = srlAccountRecive;
        this.branchRecive = branchRecive;
        this.bankRecive = bankRecive;
        this.amount = amount;
        this.status = status;
        this.step = step;
        this.createUser = createUser;
        this.description = description;
    }

    public String getReqType() {
        return this.reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getNameSend() {
        return this.nameSend;
    }

    public void setNameSend(String nameSend) {
        this.nameSend = nameSend;
    }

    public String getAddressSend() {
        return this.addressSend;
    }

    public void setAddressSend(String addressSend) {
        this.addressSend = addressSend;
    }

    public String getTellSend() {
        return this.tellSend;
    }

    public void setTellSend(String tellSend) {
        this.tellSend = tellSend;
    }

    public String getSrlAccountSend() {
        return this.srlAccountSend;
    }

    public void setSrlAccountSend(String srlAccountSend) {
        this.srlAccountSend = srlAccountSend;
    }

    public String getBranchSend() {
        return this.branchSend;
    }

    public void setBranchSend(String branchSend) {
        this.branchSend = branchSend;
    }

    public String getBankSend() {
        return this.bankSend;
    }

    public void setBankSend(String bankSend) {
        this.bankSend = bankSend;
    }

    public String getNameRecive() {
        return this.nameRecive;
    }

    public void setNameRecive(String nameRecive) {
        this.nameRecive = nameRecive;
    }

    public String getAddressRecive() {
        return this.addressRecive;
    }

    public void setAddressRecive(String addressRecive) {
        this.addressRecive = addressRecive;
    }

    public String getTellRecive() {
        return this.tellRecive;
    }

    public void setTellRecive(String tellRecive) {
        this.tellRecive = tellRecive;
    }

    public String getSrlAccountRecive() {
        return this.srlAccountRecive;
    }

    public void setSrlAccountRecive(String srlAccountRecive) {
        this.srlAccountRecive = srlAccountRecive;
    }

    public String getBranchRecive() {
        return this.branchRecive;
    }

    public void setBranchRecive(String branchRecive) {
        this.branchRecive = branchRecive;
    }

    public String getBankRecive() {
        return this.bankRecive;
    }

    public void setBankRecive(String bankRecive) {
        this.bankRecive = bankRecive;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStep() {
        return this.step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
