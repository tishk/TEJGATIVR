//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class CardAccountMessage extends BaseMessage {
    private String accStatus;
    private String blockAmount;
    private String accNo;
    private String cfsAccNo;
    private String farAccNo;
    private String accPass;
    private String accBalance;
    private String branchId;
    private String blckNo;
    private String blockStatus;
    private String isForce;
    private String pan;
    private String series;

    public String getAccStatus() {
        return this.accStatus;
    }

    public String getBlockAmount() {
        return this.blockAmount;
    }

    public String getAccNo() {
        return this.accNo;
    }

    public String getCfsAccNo() {
        return this.cfsAccNo;
    }

    public String getFarAccNo() {
        return this.farAccNo;
    }

    public String getAccPass() {
        return this.accPass;
    }

    public String getAccBalance() {
        return this.accBalance;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public String getBlckNo() {
        return this.blckNo;
    }

    public String getPan() {
        return this.pan;
    }

    public String getSeries() {
        return this.series;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public void setBlockAmount(String blockAmount) {
        this.blockAmount = blockAmount;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public void setCfsAccNo(String cfsAccNo) {
        this.cfsAccNo = cfsAccNo;
    }

    public void setFarAccNo(String farAccNo) {
        this.farAccNo = farAccNo;
    }

    public void setAccPass(String accPass) {
        this.accPass = accPass;
    }

    public void setAccBalance(String accBalance) {
        this.accBalance = accBalance;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setBlckNo(String blckNo) {
        this.blckNo = blckNo;
    }

    public String getBlockStatus() {
        return this.blockStatus;
    }

    public void setBlockStatus(String blockStatus) {
        this.blockStatus = blockStatus;
    }

    public String getForce() {
        return this.isForce;
    }

    public void setForce(String force) {
        this.isForce = force;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public CardAccountMessage(String accNo, String accStatus, String blockAmnt, String accBalance, String branchId) {
        this.accNo = accNo;
        this.accStatus = accStatus;
        this.blockAmount = blockAmnt;
        this.accBalance = accBalance;
        this.branchId = branchId;
    }

    public CardAccountMessage(String accNo, String blockAmount) {
        this.accNo = accNo;
        this.blockAmount = blockAmount;
    }

    public CardAccountMessage(String accNo) {
        this.accNo = accNo;
    }

    public CardAccountMessage() {
    }
}
