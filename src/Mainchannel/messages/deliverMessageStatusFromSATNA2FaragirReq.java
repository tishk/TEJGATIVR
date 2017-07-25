//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class deliverMessageStatusFromSATNA2FaragirReq extends RTGSBaseMessageRequest {
    static String deliverMessageStatusFromSATNA2FaragirCommand = "31";
    protected String branchID;
    protected String bankID;
    protected String serial;
    protected String step0;
    protected String stepN;
    protected String status0;
    protected String statusN;

    public deliverMessageStatusFromSATNA2FaragirReq() {
        this.setCommand(deliverMessageStatusFromSATNA2FaragirCommand);
    }

    public deliverMessageStatusFromSATNA2FaragirReq(String sessionID, String service, String cmUserID, String cmUserPWD, String date, String time, String deviceID, String customerID, String customerPWD, String additionalData, String branchID, String bankID, String serial, String step0, String stepN, String status0, String statusN) {
        super(deliverMessageStatusFromSATNA2FaragirCommand, sessionID, service, cmUserID, cmUserPWD, date, time, deviceID, customerID, customerPWD, additionalData);
        this.branchID = branchID;
        this.bankID = bankID;
        this.serial = serial;
        this.step0 = step0;
        this.stepN = stepN;
        this.status0 = status0;
        this.statusN = statusN;
    }

    public String getBranchID() {
        return this.branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getBankID() {
        return this.bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getStep0() {
        return this.step0;
    }

    public void setStep0(String step0) {
        this.step0 = step0;
    }

    public String getStepN() {
        return this.stepN;
    }

    public void setStepN(String stepN) {
        this.stepN = stepN;
    }

    public String getStatus0() {
        return this.status0;
    }

    public void setStatus0(String status0) {
        this.status0 = status0;
    }

    public String getStatusN() {
        return this.statusN;
    }

    public void setStatusN(String statusN) {
        this.statusN = statusN;
    }
}
