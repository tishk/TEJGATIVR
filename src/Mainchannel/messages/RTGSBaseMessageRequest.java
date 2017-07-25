//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class RTGSBaseMessageRequest {
    protected String command;
    protected String sessionID;
    protected String service;
    protected String cmUserID;
    protected String cmUserPWD;
    protected String date;
    protected String time;
    protected String deviceID;
    protected String customerID;
    protected String customerPWD;
    protected String additionalData;

    public RTGSBaseMessageRequest() {
        this.command = "";
        this.sessionID = "";
        this.service = "";
        this.cmUserID = "";
        this.cmUserPWD = "";
        this.date = "";
        this.time = "";
        this.deviceID = "";
        this.customerID = "";
        this.customerPWD = "";
        this.additionalData = "";
    }

    public RTGSBaseMessageRequest(String command, String sessionID, String service, String cmUserID, String cmUserPWD, String date, String time, String deviceID, String customerID, String customerPWD, String additionalData) {
        this.command = command;
        this.sessionID = sessionID;
        this.service = service;
        this.cmUserID = cmUserID;
        this.cmUserPWD = cmUserPWD;
        this.date = date;
        this.time = time;
        this.deviceID = deviceID;
        this.customerID = customerID;
        this.customerPWD = customerPWD;
        this.additionalData = additionalData;
    }

    protected String getCommand() {
        return this.command;
    }

    protected void setCommand(String command) {
        this.command = command;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCmUserID() {
        return this.cmUserID;
    }

    public void setCmUserID(String cmUserID) {
        this.cmUserID = cmUserID;
    }

    public String getCmUserPWD() {
        return this.cmUserPWD;
    }

    public void setCmUserPWD(String cmUserPWD) {
        this.cmUserPWD = cmUserPWD;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerPWD() {
        return this.customerPWD;
    }

    public void setCustomerPWD(String customerPWD) {
        this.customerPWD = customerPWD;
    }

    public String getAdditionalData() {
        return this.additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}
