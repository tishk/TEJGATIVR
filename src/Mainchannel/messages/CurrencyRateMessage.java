//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class CurrencyRateMessage extends BaseMessage {
    protected String currency;
    protected String currencyDesc;
    protected String sellRate;
    protected String buyRate;
    protected String currencyUnit;
    protected String fcDate;
    protected String fcTime;

    public CurrencyRateMessage(String currency, String sellRate, String buyRate, String currencyUnit, String fcDate, String fcTime) {
        this.sellRate = sellRate;
        this.buyRate = buyRate;
        this.currencyUnit = currencyUnit;
        this.fcDate = fcDate;
        this.currency = currency;
        this.fcTime = fcTime;
    }

    public CurrencyRateMessage(String currency) {
        this.currency = currency;
        this.sellRate = "";
        this.buyRate = "";
        this.currencyUnit = "";
        this.fcDate = "";
        this.fcTime = "";
    }

    public CurrencyRateMessage() {
        this.sellRate = "";
        this.buyRate = "";
        this.currencyUnit = "";
        this.fcDate = "";
        this.fcTime = "";
        this.currency = "";
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getSellRate() {
        return this.sellRate;
    }

    public void setSellRate(String sellRate) {
        this.sellRate = sellRate;
    }

    public String getBuyRate() {
        return this.buyRate;
    }

    public void setBuyRate(String buyRate) {
        this.buyRate = buyRate;
    }

    public String getCurrencyUnit() {
        return this.currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public String getFcdate() {
        return this.fcDate;
    }

    public void setFcdate(String fcdate) {
        this.fcDate = fcdate;
    }

    public String getFcTime() {
        return this.fcTime;
    }

    public void setFcTime(String fcTime) {
        this.fcTime = fcTime;
    }

    public String getCurrencyDesc() {
        return this.currencyDesc;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public String getFcDate() {
        return this.fcDate;
    }

    public void setFcDate(String fcDate) {
        this.fcDate = fcDate;
    }
}
