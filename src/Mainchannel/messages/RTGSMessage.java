//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class RTGSMessage extends BaseMessage {
    private String satnaStatus;

    public RTGSMessage() {
    }

    public RTGSMessage(String satnaStatus) {
        this.satnaStatus = satnaStatus;
    }

    public String getSatnaStatus() {
        return this.satnaStatus;
    }

    public void setSatnaStatus(String satnaStatus) {
        this.satnaStatus = satnaStatus;
    }
}
