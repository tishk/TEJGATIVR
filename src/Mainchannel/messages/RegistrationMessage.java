//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class RegistrationMessage extends BaseMessage {
    protected String accountNumber;
    protected String serviceType;

    public RegistrationMessage(String accountNumber, String serviceType) {
        this.accountNumber = accountNumber;
        this.serviceType = serviceType;
    }

    public RegistrationMessage(String accountNumber) {
        this.accountNumber = accountNumber;
        this.serviceType = "";
    }

    public RegistrationMessage() {
        this.accountNumber = "";
        this.serviceType = "";
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getServiceType() {
        return this.serviceType;
    }
}
