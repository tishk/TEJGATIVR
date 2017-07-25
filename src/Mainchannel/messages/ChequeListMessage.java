//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import java.util.Vector;

public class ChequeListMessage extends ChequeStatusMessage {
    private String listCount;
    private String fromDate;
    private String toDate;
    private String fromTime;
    private String toTime;
    private String minAmnt;
    private String maxAmnt;
    private String seri;
    private String startSerial;
    private String endSerial;
    private String issuedDate;
    private String issuedTime;
    private Vector chequeVec;

    public ChequeListMessage() {
    }

    public ChequeListMessage(String accNo, String cqNo, String cqSt, String cqLsNo, String fromDate, String toDate, String fromTime, String toTime, String minAmnt, String maxAmnt) {
        super.accountNumber = accNo;
        super.chequeNumber = cqNo;
        super.chequeStatus = cqSt;
        this.listCount = cqLsNo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.minAmnt = minAmnt;
        this.maxAmnt = maxAmnt;
    }

    public ChequeListMessage(String cqNo, String cqSt, String cqAmnt, String cqDate, String cqTime) {
        super.chequeNumber = cqNo;
        super.chequeStatus = cqSt;
        super.chequeAmount = cqAmnt;
        super.chequeOpDate = cqDate;
    }

    public ChequeListMessage(Vector chequeVec) {
        this.chequeVec = chequeVec;
    }

    public void setListCount(String listCount) {
        this.listCount = listCount;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public void setMinAmnt(String minAmnt) {
        this.minAmnt = minAmnt;
    }

    public void setMaxAmnt(String maxAmnt) {
        this.maxAmnt = maxAmnt;
    }

    public String getListCount() {
        return this.listCount;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public String getToDate() {
        return this.toDate;
    }

    public String getFromTime() {
        return this.fromTime;
    }

    public String getToTime() {
        return this.toTime;
    }

    public String getMinAmnt() {
        return this.minAmnt;
    }

    public String getMaxAmnt() {
        return this.maxAmnt;
    }

    public String getSeri() {
        return this.seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public String getStartSerial() {
        return this.startSerial;
    }

    public void setStartSerial(String startSerial) {
        this.startSerial = startSerial;
    }

    public String getEndSerial() {
        return this.endSerial;
    }

    public void setEndSerial(String endSerial) {
        this.endSerial = endSerial;
    }

    public String getIssuedDate() {
        return this.issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getIssuedTime() {
        return this.issuedTime;
    }

    public void setIssuedTime(String issuedTime) {
        this.issuedTime = issuedTime;
    }

    public ChequeStatusMessage getCheques(int transNumber) {
        return (ChequeStatusMessage)this.chequeVec.elementAt(transNumber);
    }

    public ChequeListMessage getChequeList(int transNumber) {
        return (ChequeListMessage)this.chequeVec.elementAt(transNumber);
    }
}
