//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import java.io.Serializable;

public class StatementMessage extends BaseMessage implements Serializable{
    private static final long serialVersionUID = 7933179333322788700L;
    //
    protected String creditDebit;
    protected String transOprationCode;
    protected String amount;
    protected String lastAmount;
    protected String transDate;
    protected String transTime;
    protected String transEffectiveDate;
    protected String transDocNo;
    protected String branchCode;
    protected String branchName;
    protected String transNo;
    protected String transDesc;
    protected String payId1;
    protected String payId2;
    protected String cbPayId;
    protected String trckId;
    protected String termType;
    protected String shpInf;
    protected String otherAcc;
    protected String rrn;

    public StatementMessage(String creditDebit, String transOprationCode, String amount, String lastAmount, String transDate, String transEffectiveDate, String transDocNo, String transDesc, String branchCode, String transNo, String transTime) {
        this.creditDebit = creditDebit;
        this.transOprationCode = transOprationCode;
        this.amount = amount;
        this.lastAmount = lastAmount;
        this.transDate = transDate;
        this.transTime = transTime;
        this.transEffectiveDate = transEffectiveDate;
        this.transDocNo = transDocNo;
        this.branchCode = branchCode;
        this.transNo = transNo;
        this.transDesc = transDesc;
    }

    public StatementMessage(String transNo, String transOprationCode, String amount, String crdb, String transDate, String transTime, String transEffectiveDate, String transDocNo, String branchCode, String lastAmount, String payId1, String payId2, String cbPayId) {
        this.transNo = transNo;
        this.transOprationCode = transOprationCode;
        this.amount = amount;
        this.creditDebit = crdb;
        this.transDate = transDate;
        this.transTime = transTime;
        this.transEffectiveDate = transEffectiveDate;
        this.transDocNo = transDocNo;
        this.branchCode = branchCode;
        this.lastAmount = lastAmount;
        this.payId1 = payId1;
        this.payId2 = payId2;
        this.cbPayId = cbPayId;
    }

    public StatementMessage() {
        this.creditDebit = "";
        this.transOprationCode = "";
        this.amount = "";
        this.lastAmount = "";
        this.transDate = "";
        this.transEffectiveDate = "";
        this.transDocNo = "";
        this.branchCode = "";
        this.transNo = "";
        this.transDesc = "";
        this.transTime = "";
    }

    public String getTransDesc() {
        return this.transDesc;
    }

    public String getCreditDebit() {
        return this.creditDebit;
    }

    public String getTransOprationCode() {
        return this.transOprationCode;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getTransDate() {
        return this.transDate;
    }

    public String getTransEffectiveDate() {
        return this.transEffectiveDate;
    }

    public String getTransDocNo() {
        return this.transDocNo;
    }

    public String getLastAmount() {
        return this.lastAmount;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public String getBranchName() {
        return this.branchName;
    }
    public void setBranchName(String brName) {
         this.branchName=brName;
    }

    public String getTransNo() {
        return this.transNo;
    }

    public String getTransTime() {
        return this.transTime;
    }

    public String getPayId1() {
        return this.payId1;
    }

    public String getPayId2() {
        return this.payId2;
    }

    public void setPayId2(String payId2) {
        this.payId2 = payId2;
    }

    public String getCbPayId() {
        return this.cbPayId;
    }

    public String getShpInf() {
        return this.shpInf;
    }

    public void setCbPayId(String cbPayId) {
        this.cbPayId = cbPayId;
    }

    public void setPayId1(String payId1) {
        this.payId1 = payId1;
    }

    public void setShpInf(String shpInf) {
        this.shpInf = shpInf;
    }

    public String getTrckId() {
        return this.trckId;
    }

    public void setTrckId(String trckId) {
        this.trckId = trckId;
    }

    public String getTermType() {
        return this.termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public String getOtherAcc() {
        return this.otherAcc;
    }

    public void setOtherAcc(String otherAcc) {
        this.otherAcc = otherAcc;
    }

    public String getRrn() {
        return this.rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }
}
