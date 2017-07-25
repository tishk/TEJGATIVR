//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class OrderChequeBookMessage extends BaseMessage {
    protected String accountNumber;
    protected String channelType;
    protected String channelID;
    protected String orderType;
    protected int quantity;
    protected int noOfPage;
    protected String dispathBy;

    public OrderChequeBookMessage(String accountNumber, String channelType, String channelID, String orderType, int quantity, int noOfPage, String dispathBy) {
        this.accountNumber = accountNumber;
        this.channelType = channelType;
        this.channelID = channelID;
        this.orderType = orderType;
        this.quantity = quantity;
        this.noOfPage = noOfPage;
        this.dispathBy = dispathBy;
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

    public String getOrderType() {
        return this.orderType;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public int getNoOfPage() {
        return this.noOfPage;
    }

    public String getDispathBy() {
        return this.dispathBy;
    }

    public String toString() {
        return super.toString() + "/[accountNumber]" + this.accountNumber + "/[channelType]" + this.channelType + "/[channelID]" + this.channelID + "/[orderType]" + this.orderType + "/[quantity]" + this.quantity + "/[noOfPage]" + this.noOfPage + "/[dispathBy]" + this.dispathBy;
    }
}
