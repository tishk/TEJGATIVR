//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class BalanceMessage extends BaseMessage {
    protected String accountNumber;
    protected String accountGroup;
    protected String actualBalance;
    protected String availableBalance;
    protected String currency;
    protected String lastTransactionDate;
    protected String CreditDebit;
    protected String hostId;
    protected String branchId;

    public BalanceMessage(String accountNumber, String accountGroup, String availableBalance, String actualBalance, String currency, String lastTransactionDate, String creditDebit, String hostId, String branchId) {
        this.accountNumber = accountNumber;
        this.accountGroup = accountGroup;
        this.actualBalance = actualBalance;
        this.availableBalance = availableBalance;
        this.currency = currency;
        this.lastTransactionDate = lastTransactionDate;
        this.CreditDebit = creditDebit;
        this.hostId = hostId;
        this.branchId = branchId;
    }

    public BalanceMessage(String accountNumber) {
        this.accountNumber = accountNumber;
        this.accountGroup = "";
        this.actualBalance = "";
        this.availableBalance = "";
        this.currency = "";
        this.lastTransactionDate = "";
        this.CreditDebit = "";
        this.hostId = "";
        this.branchId = "";
    }

    public BalanceMessage() {
        this.accountNumber = "";
        this.accountGroup = "";
        this.actualBalance = "";
        this.availableBalance = "";
        this.currency = "";
        this.lastTransactionDate = "";
        this.CreditDebit = "";
        this.hostId = "";
        this.branchId = "";
    }

    public String getAccountGroup() {
        return this.accountGroup;
    }

    public String getActualBalance() {
        return this.actualBalance;
    }

    public String getAvailableBalance() {
        return this.availableBalance;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getLastTransactionDate() {
        return this.lastTransactionDate;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getCreditDebit() {
        return this.CreditDebit;
    }

    public String getHostId() {
        return this.hostId;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
