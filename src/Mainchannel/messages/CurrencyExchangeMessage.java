//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class CurrencyExchangeMessage extends BaseMessage {
    protected String channelType;
    protected String channelID;
    protected String currency;
    protected String amount;
    protected String date;
    protected String time;

    public CurrencyExchangeMessage() {
    }

    public CurrencyExchangeMessage(String channelType, String channelID, String currency, String amount, String date, String time) {
        this.channelType = channelType;
        this.channelID = channelID;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getAmount() {
        return this.amount;
    }

    public String toString() {
        return super.toString() + "/[channelType]" + this.channelType + "/[channelID]" + this.channelID + "/[currency]" + this.currency + "/[amount]" + this.amount + "/[date]" + this.date + "/[time]" + this.time;
    }
}
