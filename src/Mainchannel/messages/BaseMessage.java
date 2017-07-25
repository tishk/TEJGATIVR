

package Mainchannel.messages;

import java.io.Serializable;

public class BaseMessage implements java.io.Serializable {
    String txDate = "";
    String txTime = "";
    String action_code = "";
    String action_desc = "";
    String msgSequence = "";
    String channelId = "";
    String channelType = "";
    String MAC = "";
    String encAlgorytm = "";
    String reqDateTime = "";
    String respDateTime = "";
    String responseXml = "";
    String requestXml = "";

    public BaseMessage() {
    }

    public String getResponseXml() {
        return this.responseXml;
    }

    public void setResponseXml(String responseXml) {
        this.responseXml = responseXml;
    }

    public String getRequestXml() {
        return this.requestXml;
    }

    public void setRequestXml(String RequestXml) {
        this.requestXml = RequestXml;
    }

    public String getAction_code() {
        return this.action_code;
    }

    public void setAction_code(String action_code) {
        this.action_code = action_code;
    }

    public String getAction_desc() {
        return this.action_desc;
    }

    public void setAction_desc(String action_desc) {
        this.action_desc = action_desc;
    }

    public String getTxDate() {
        return this.txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public String getTxTime() {
        return this.txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getMsgSequence() {
        return this.msgSequence;
    }

    public void setMsgSequence(String msgSequence) {
        this.msgSequence = msgSequence;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getMAC() {
        return this.MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getReqDateTime() {
        return this.reqDateTime;
    }

    public void setReqDateTime(String reqDateTime) {
        this.reqDateTime = reqDateTime;
    }

    public String getRespDateTime() {
        return this.respDateTime;
    }

    public void setRespDateTime(String respDateTime) {
        this.respDateTime = respDateTime;
    }

    public String getEncAlgorytm() {
        return this.encAlgorytm;
    }

    public void setEncAlgorytm(String encAlgorytm) {
        this.encAlgorytm = encAlgorytm;
    }
}
