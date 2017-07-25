//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class LoanBalanceMessage extends BaseMessage {
    protected String accountNumber;
    protected String channelType;
    protected String channelID;
    protected String accountType;
    protected String currency;
    protected String amount;
    protected String startDate;
    protected String rePaymentDate;
    protected String unutilised;
    protected String interest;

    public LoanBalanceMessage(String accountNumber, String channelType, String channelID, String accountType, String currency, String amount, String startDate, String rePaymentDate, String unutilised, String interest) {
        this.accountNumber = accountNumber;
        this.channelType = channelType;
        this.channelID = channelID;
        this.accountType = accountType;
        this.currency = currency;
        this.amount = amount;
        this.startDate = startDate;
        this.rePaymentDate = rePaymentDate;
        this.unutilised = unutilised;
        this.interest = interest;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getRePaymentDate() {
        return this.rePaymentDate;
    }

    public String getUnutilised() {
        return this.unutilised;
    }

    public String getInterest() {
        return this.interest;
    }

    public String toString() {
        return super.toString() + "/[accountNumber]" + this.accountNumber + "/[channelType]" + this.channelType + "/[channelID]" + this.channelID + "/[accountType]" + this.accountType + "/[currency]" + this.currency + "/[amount]" + this.amount + "/[startDate]" + this.startDate + "/[rePaymentDate]" + this.rePaymentDate + "/[unutilised]" + this.unutilised + "/[interest]" + this.interest;
    }
}
