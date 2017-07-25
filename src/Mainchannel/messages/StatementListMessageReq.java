//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class StatementListMessageReq extends BaseMessage {
    String serviceType;
    String serviceIp;
    String servicePass;
    String accountNumber;
    String accountPass;
    String fromDate;
    String toDate;
    String fromTime;
    String toTime;
    String transCount;
    String creditDebit;
    String transMinAmount;
    String transMaxAmount;
    String transDocNo;
    String transOprationCode;
    String transDesc;
    String branchCode;
    String stmType;
    String cbPayId;
    String rrn;

    public StatementListMessageReq() {
    }

    public StatementListMessageReq(String serviceType, String serviceIp, String servicePass, String accountNumber, String fromDate, String toDate, String fromTime, String toTime, String transCount, String creditdebit, String transMinAmount, String transMaxAmount, String transDocNo, String transOprationCode, String transDesc, String branchCode, String stmType) {
        this.serviceType = serviceType;
        this.serviceIp = serviceIp;
        this.servicePass = servicePass;
        this.accountNumber = accountNumber;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.transCount = transCount;
        this.creditDebit = creditdebit;
        this.transMinAmount = transMinAmount;
        this.transMaxAmount = transMaxAmount;
        this.transDocNo = transDocNo;
        this.transOprationCode = transOprationCode;
        this.transDesc = transDesc;
        this.branchCode = branchCode;
        this.stmType = stmType;
    }

    public StatementListMessageReq(String accountNumber, String stmType, String fromDate, String toDate, String fromTime, String toTime, String transCount, String crdb, String transMinAmount, String transMaxAmount, String transdocno, String transOprationCode, String branchCode, String cbPayId) {
        this.accountNumber = accountNumber;
        this.stmType = stmType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.transCount = transCount;
        this.creditDebit = crdb;
        this.transMinAmount = transMinAmount;
        this.transMaxAmount = transMaxAmount;
        this.transDocNo = transdocno;
        this.transOprationCode = transOprationCode;
        this.branchCode = branchCode;
        this.cbPayId = cbPayId;
    }

    public String getTransDesc() {
        return this.transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceIp() {
        return this.serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public String getServicePass() {
        return this.servicePass;
    }

    public void setServicePass(String servicePass) {
        this.servicePass = servicePass;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return this.toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTransCount() {
        return this.transCount;
    }

    public void setTransCount(String transCount) {
        this.transCount = transCount;
    }

    public String getTransMinAmount() {
        return this.transMinAmount;
    }

    public void setTransMinAmount(String transMinAmount) {
        this.transMinAmount = transMinAmount;
    }

    public String getTransMaxAmount() {
        return this.transMaxAmount;
    }

    public void setTransMaxAmount(String transMaxAmount) {
        this.transMaxAmount = transMaxAmount;
    }

    public String getTransDocNo() {
        return this.transDocNo;
    }

    public void setTransDocNo(String transDocNo) {
        this.transDocNo = transDocNo;
    }

    public String getTransOprationCode() {
        return this.transOprationCode;
    }

    public void setTransOprationCode(String transOprationCode) {
        this.transOprationCode = transOprationCode;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getCreditDebit() {
        return this.creditDebit;
    }

    public void setCreditDebit(String creditDebit) {
        this.creditDebit = creditDebit;
    }

    public String getStmType() {
        return this.stmType;
    }

    public void setStmType(String stmType) {
        this.stmType = stmType;
    }

    public String getAccountPass() {
        return this.accountPass;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }

    public String getFromTime() {
        return this.fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return this.toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getCbPayId() {
        return this.cbPayId;
    }

    public void setCbPayId(String cbPayId) {
        this.cbPayId = cbPayId;
    }

    public String getRrn() {
        return this.rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

}
