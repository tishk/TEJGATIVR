//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class LoanCreditMessage extends BaseMessage {
    protected String accountNumber;
    protected String date;
    protected String narrative;
    protected String amount;
    protected String currency;
    protected int index;

    public LoanCreditMessage(String accountNumber, String date, String narrative, String amount, String currency, int index) {
        this.accountNumber = accountNumber;
        this.date = date;
        this.narrative = narrative;
        this.amount = amount;
        this.currency = currency;
        this.index = index;
    }

    public LoanCreditMessage() {
        this.accountNumber = "";
        this.date = "";
        this.narrative = "";
        this.amount = "";
        this.currency = "";
        this.index = 0;
    }

    public String getCurrency() {
        return this.currency;
    }

    public int getIndex() {
        return this.index;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getDate() {
        return this.date;
    }

    public String getNarrative() {
        return this.narrative;
    }

    public String getAmount() {
        return this.amount;
    }
}
