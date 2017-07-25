//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class CheckFollowMessage extends BaseMessage {
    protected String channelType;
    protected String channelID;
    protected String accountNumber;
    protected String dateTime;
    protected String refNo;

    public CheckFollowMessage() {
        this.accountNumber = "";
        this.dateTime = "";
        this.refNo = "";
        this.channelType = "";
        this.channelID = "";
    }

    public CheckFollowMessage(String channelType, String channelID, String accountNumber, String dateTime, String refNo) {
        this.accountNumber = accountNumber;
        this.channelType = channelType;
        this.channelID = channelID;
        this.dateTime = dateTime;
        this.refNo = refNo;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public String getRefNo() {
        return this.refNo;
    }
}
