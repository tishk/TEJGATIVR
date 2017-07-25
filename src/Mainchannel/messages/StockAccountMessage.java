//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import java.util.Vector;

public class StockAccountMessage extends FundTransferMessage {
    protected String accNo;
    protected String accPass;
    protected String accGroup;
    protected String blckNo;
    protected String blckAmnt;
    protected String TotalBlckAmnt;
    protected String unblckAmnt;
    protected String blckDesc;
    protected String bokerNo;
    protected String blckStatus;
    protected String blckDate;
    protected String blckTime;
    protected String payCode;
    protected String provideId;
    protected String brchCode;
    protected String listNo;
    protected String fromDateTime;
    protected String toDateTime;
    protected String minAmnt;
    protected String maxAmnt;
    protected String accSt;
    protected String fDt;
    protected String tDt;
    protected String fTm;
    protected String tTm;
    protected Vector transaction;
    protected String availabeBalance;
    protected String actualBalance;
    protected String creditDebit;
    protected String lastTransDate;
    protected String hostId;

    public StockAccountMessage() {
    }

    public StockAccountMessage(String srcAcc, String dstAcc, String amnt, String currency, String opCode, String transDesc, String brchCode) {
        super.srcAccountNumber = srcAcc;
        super.dstAccountNumber = dstAcc;
        super.transAmount = amnt;
        super.currency = currency;
        super.opCode = opCode;
        super.transDesc = transDesc;
        this.brchCode = brchCode;
    }

    public StockAccountMessage(String srcAcc, String dstAcc, String amnt, String currency, String opCode, String transDesc) {
        super.srcAccountNumber = srcAcc;
        super.dstAccountNumber = dstAcc;
        super.transAmount = amnt;
        super.currency = currency;
        super.opCode = opCode;
        super.transDesc = transDesc;
    }

    public StockAccountMessage(String accNo, String transCount, String blckNo, String blckSt, String fromDtTm, String toDtTm, String minAmnt, String maxAmnt, String brokerNo) {
        this.accNo = accNo;
        this.listNo = transCount;
        this.blckNo = blckNo;
        this.blckStatus = blckSt;
        this.fromDateTime = fromDtTm;
        this.toDateTime = toDtTm;
        this.minAmnt = minAmnt;
        this.maxAmnt = maxAmnt;
        this.bokerNo = brokerNo;
    }

    public StockAccountMessage(String accountNumber, String transCount, Vector transaction) {
        this.accNo = accountNumber;
        this.transaction = transaction;
        this.listNo = transCount;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public void setAccPass(String accPass) {
        this.accPass = accPass;
    }

    public void setAccGroup(String accGroup) {
        this.accGroup = accGroup;
    }

    public void setBlckNo(String blckNo) {
        this.blckNo = blckNo;
    }

    public void setBlckAmnt(String blckAmnt) {
        this.blckAmnt = blckAmnt;
    }

    public void setTotalBlckAmnt(String totalBlckAmnt) {
        this.TotalBlckAmnt = totalBlckAmnt;
    }

    public void setUnblckAmnt(String unblckAmnt) {
        this.unblckAmnt = unblckAmnt;
    }

    public void setBlckDesc(String blckDesc) {
        this.blckDesc = blckDesc;
    }

    public void setBokerNo(String bokerNo) {
        this.bokerNo = bokerNo;
    }

    public void setBlckStatus(String blckStatus) {
        this.blckStatus = blckStatus;
    }

    public void setBlckDate(String blckDate) {
        this.blckDate = blckDate;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public void setProvideId(String provideId) {
        this.provideId = provideId;
    }

    public void setBrchCode(String brchCode) {
        this.brchCode = brchCode;
    }

    public void setListNo(String listNo) {
        this.listNo = listNo;
    }

    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }

    public void setMinAmnt(String minAmnt) {
        this.minAmnt = minAmnt;
    }

    public void setMaxAmnt(String maxAmnt) {
        this.maxAmnt = maxAmnt;
    }

    public void setTransaction(Vector transaction) {
        this.transaction = transaction;
    }

    public void setBlckTime(String blckTime) {
        this.blckTime = blckTime;
    }

    public void setAccSt(String accSt) {
        this.accSt = accSt;
    }

    public void setfDt(String fDt) {
        this.fDt = fDt;
    }

    public void settDt(String tDt) {
        this.tDt = tDt;
    }

    public void setfTm(String fTm) {
        this.fTm = fTm;
    }

    public void settTm(String tTm) {
        this.tTm = tTm;
    }

    public void setAvailabeBalance(String availabeBalance) {
        this.availabeBalance = availabeBalance;
    }

    public void setActualBalance(String actualBalance) {
        this.actualBalance = actualBalance;
    }

    public void setCreditDebit(String creditDebit) {
        this.creditDebit = creditDebit;
    }

    public void setLastTransDate(String lastTransDate) {
        this.lastTransDate = lastTransDate;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getAccNo() {
        return this.accNo;
    }

    public String getAccPass() {
        return this.accPass;
    }

    public String getAccGroup() {
        return this.accGroup;
    }

    public String getBlckNo() {
        return this.blckNo;
    }

    public String getBlckAmnt() {
        return this.blckAmnt;
    }

    public String getTotalBlckAmnt() {
        return this.TotalBlckAmnt;
    }

    public String getUnblckAmnt() {
        return this.unblckAmnt;
    }

    public String getBlckDesc() {
        return this.blckDesc;
    }

    public String getBokerNo() {
        return this.bokerNo;
    }

    public String getBlckStatus() {
        return this.blckStatus;
    }

    public String getBlckDate() {
        return this.blckDate;
    }

    public String getPayCode() {
        return this.payCode;
    }

    public String getProvideId() {
        return this.provideId;
    }

    public String getBrchCode() {
        return this.brchCode;
    }

    public String getListNo() {
        return this.listNo;
    }

    public String getFromDateTime() {
        return this.fromDateTime;
    }

    public String getToDateTime() {
        return this.toDateTime;
    }

    public String getMinAmnt() {
        return this.minAmnt;
    }

    public String getMaxAmnt() {
        return this.maxAmnt;
    }

    public Vector getTransaction() {
        return this.transaction;
    }

    public String getBlckTime() {
        return this.blckTime;
    }

    public String getAccSt() {
        return this.accSt;
    }

    public String getfDt() {
        return this.fDt;
    }

    public String gettDt() {
        return this.tDt;
    }

    public String getfTm() {
        return this.fTm;
    }

    public String gettTm() {
        return this.tTm;
    }

    public String getAvailabeBalance() {
        return this.availabeBalance;
    }

    public String getActualBalance() {
        return this.actualBalance;
    }

    public String getCreditDebit() {
        return this.creditDebit;
    }

    public String getLastTransDate() {
        return this.lastTransDate;
    }

    public String getHostId() {
        return this.hostId;
    }

    public StockAccountMessage getStockAccMessage(int transNumber) {
        return (StockAccountMessage)this.transaction.elementAt(transNumber);
    }
}
