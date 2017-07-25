//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import java.util.Vector;

public class EasyShoppingMessage extends BaseMessage {
    private String accountNumber;
    private String srcAccountNo;
    private String dstAccountNo;
    private String accountPass;
    private String blckNo;
    private String blckAmnt;
    private String unBlckAmnt;
    private String transAmnt;
    private String payCode;
    private String branchId;
    private String opCode;
    private String transDesc;
    private String cmInternalRefNo;
    private String lsCount;
    private String row;
    private String blckSt;
    private String fromBlckDt;
    private String toBlckDt;
    private String blckDt;
    private Vector transaction;

    public EasyShoppingMessage() {
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getSrcAccountNo() {
        return this.srcAccountNo;
    }

    public String getDstAccountNo() {
        return this.dstAccountNo;
    }

    public String getAccountPass() {
        return this.accountPass;
    }

    public String getBlckNo() {
        return this.blckNo;
    }

    public String getBlckAmnt() {
        return this.blckAmnt;
    }

    public String getUnBlckAmnt() {
        return this.unBlckAmnt;
    }

    public String getTransAmnt() {
        return this.transAmnt;
    }

    public String getPayCode() {
        return this.payCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public String getOpCode() {
        return this.opCode;
    }

    public String getTransDesc() {
        return this.transDesc;
    }

    public String getCmInternalRefNo() {
        return this.cmInternalRefNo;
    }

    public String getLsCount() {
        return this.lsCount;
    }

    public String getRow() {
        return this.row;
    }

    public String getBlckSt() {
        return this.blckSt;
    }

    public String getFromBlckDt() {
        return this.fromBlckDt;
    }

    public String getToBlckDt() {
        return this.toBlckDt;
    }

    public String getBlckDt() {
        return this.blckDt;
    }

    public Vector getTransaction() {
        return this.transaction;
    }

    public EasyShoppingMessage getEasyShoppingMessage(int row) {
        return (EasyShoppingMessage)this.transaction.elementAt(row);
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setSrcAccountNo(String srcAccountNo) {
        this.srcAccountNo = srcAccountNo;
    }

    public void setDstAccountNo(String dstAccountNo) {
        this.dstAccountNo = dstAccountNo;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }

    public void setBlckNo(String blckNo) {
        this.blckNo = blckNo;
    }

    public void setBlckAmnt(String blckAmnt) {
        this.blckAmnt = blckAmnt;
    }

    public void setUnBlckAmnt(String unBlckAmnt) {
        this.unBlckAmnt = unBlckAmnt;
    }

    public void setTransAmnt(String transAmnt) {
        this.transAmnt = transAmnt;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public void setCmInternalRefNo(String cmInternalRefNo) {
        this.cmInternalRefNo = cmInternalRefNo;
    }

    public void setLsCount(String lsCount) {
        this.lsCount = lsCount;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public void setBlckSt(String blckSt) {
        this.blckSt = blckSt;
    }

    public void setFromBlckDt(String fromBlckDt) {
        this.fromBlckDt = fromBlckDt;
    }

    public void setToBlckDt(String toBlckDt) {
        this.toBlckDt = toBlckDt;
    }

    public void setBlckDt(String blckDt) {
        this.blckDt = blckDt;
    }

    public void setTransaction(Vector transaction) {
        this.transaction = transaction;
    }
}
