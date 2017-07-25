//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import java.util.Vector;

public class LoanCreditListMessage extends BaseMessage {
    protected String accountNumber;
    protected String channelType;
    protected String channelID;
    protected int loanCreditCount;
    protected Vector loanCreditVector;

    public LoanCreditMessage getLoanCreditMessage(int index) {
        return (LoanCreditMessage)this.loanCreditVector.elementAt(index);
    }

    public LoanCreditListMessage() {
        this.accountNumber = "";
        this.channelType = "";
        this.channelID = "";
        this.loanCreditCount = 0;
        this.loanCreditVector = null;
    }

    public LoanCreditListMessage(String accountNumber, String channelType, String channelID, int loanCreditCount, Vector loanCreditVector) {
        this.accountNumber = accountNumber;
        this.channelType = channelType;
        this.channelID = channelID;
        this.loanCreditCount = loanCreditCount;
        this.loanCreditVector = loanCreditVector;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public int getLoanCreditCount() {
        return this.loanCreditCount;
    }

    public Vector getLoanCreditVector() {
        return this.loanCreditVector;
    }

    public String toString() {
        return super.toString() + "/[accountNumber]" + this.accountNumber + "/[channelType]" + this.channelType + "/[channelID]" + this.channelID + "/[loanCreditCount]" + this.loanCreditCount + "/[loanCreditVector]" + this.loanCreditVector;
    }
}
