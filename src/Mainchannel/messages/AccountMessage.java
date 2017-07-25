//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class AccountMessage extends BaseMessage {
    protected String accountNumber;
    protected String accountPass;
    protected String accountStatus;
    protected String branchCode;
    protected String blckMode;
    protected String blckNumber;
    protected String blckAmount;
    protected String unblckAmount;

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String s) {
        this.accountNumber = s;
    }

    public String getAccountPass() {
        return this.accountPass;
    }

    public void setAccountPass(String s) {
        this.accountPass = s;
    }

    public String getAccountStatus() {
        return this.accountStatus;
    }

    public void setAccountStatus(String s) {
        this.accountStatus = s;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(String s) {
        this.branchCode = s;
    }

    public String getBlckMode() {
        return this.blckMode;
    }

    public void setBlckMode(String s) {
        this.blckMode = s;
    }

    public String getBlckNumber() {
        return this.blckNumber;
    }

    public void setBlckNumber(String blckNumber) {
        this.blckNumber = blckNumber;
    }

    public String getBlckAmount() {
        return this.blckAmount;
    }

    public void setBlckAmount(String blckAmount) {
        this.blckAmount = blckAmount;
    }

    public String getUnblckAmount() {
        return this.unblckAmount;
    }

    public void setUnblckAmount(String unblckAmount) {
        this.unblckAmount = unblckAmount;
    }

    public AccountMessage() {
    }

    public AccountMessage(String s) {
        this.accountNumber = s;
    }

    public AccountMessage(String s, String s1, String s2, String s3) {
        this.accountNumber = s;
        this.accountPass = s1;
        this.accountStatus = s2;
        this.branchCode = s3;
    }
}
