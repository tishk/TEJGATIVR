//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class InformativeIbanMessage extends BaseMessage {
    public String accountNo;
    public String ibanAcc;
    public String AccPass;
    public String refNo;
    public String payCode;
    public String accStatus;
    public String accComments;
    public String ownerName;
    public String isPayCodeNeeded;
    public String payCodeValidity;

    public InformativeIbanMessage() {
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public String getIbanAcc() {
        return this.ibanAcc;
    }

    public String getAccPass() {
        return this.AccPass;
    }

    public String getRefNo() {
        return this.refNo;
    }

    public String getPayCode() {
        return this.payCode;
    }

    public String getAccStatus() {
        return this.accStatus;
    }

    public String getAccComments() {
        return this.accComments;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public String isPayCodeNeeded() {
        return this.isPayCodeNeeded;
    }

    public String getPayCodeValidity() {
        return this.payCodeValidity;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setIbanAcc(String ibanAcc) {
        this.ibanAcc = ibanAcc;
    }

    public void setAccPass(String accPass) {
        this.AccPass = accPass;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public void setAccComments(String accComments) {
        this.accComments = accComments;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setPayCodeNeeded(String payCodeNeeded) {
        this.isPayCodeNeeded = payCodeNeeded;
    }

    public void setPayCodeValidity(String payCodeValidity) {
        this.payCodeValidity = payCodeValidity;
    }
}
