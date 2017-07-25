//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

public class getC2CMessageFromFaragirReq extends RTGSBaseMessageRequest {
    static String C2CMessageFromFaragirCommand = "30";

    public getC2CMessageFromFaragirReq(String sessionID, String service, String cmUserID, String cmUserPWD, String date, String time, String deviceID, String customerID, String customerPWD, String additionalData) {
        super(C2CMessageFromFaragirCommand, sessionID, service, cmUserID, cmUserPWD, date, time, deviceID, customerID, customerPWD, additionalData);
    }

    public getC2CMessageFromFaragirReq() {
        this.setCommand(C2CMessageFromFaragirCommand);
    }
}
