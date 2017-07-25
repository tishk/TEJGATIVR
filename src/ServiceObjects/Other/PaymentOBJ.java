package ServiceObjects.Other;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 12/8/2015.
 */
public class PaymentOBJ {


    private String TRANSACTIONID="";
    private String ISACCOUNT="";
    private String ACTIONCODE="";
    private Timestamp DATETIME;
    private String ISACTIVEFORRETRY="";
    private String FOLLOWCODE="";
    private String TRANCEDATE="";
    private String TRANSTIME="";
    private String CALLID="";
    private String SOURCEOFPAYMENT="";
    private String BILLID ="";
    private String PAYMENTID ="";
    private String AMOUNT ="";
    private String ISFUNDTRUSFER ="";
    private String ISFUNDTRANSFERWITHID="";
    private String ISBILLPAYMENTOFACCOUNT ="";
    private String ISINSTALLMENTPAYMENT ="";
    private String ISBILLPAYMENTOFPAN ="";
    private Date   DATEOFTRANSACTION ;
    private String DONEFLAG ="";
    private String DESTINATIONACCOUNT ="";
    private String TRANFERID ="";

    public  void   setTRANSACTIONID(String tRANSACTIONID){
        TRANSACTIONID=tRANSACTIONID;
    }
    public  String getTRANSACTIONID(){
        return TRANSACTIONID;
    }

    public  void   setISACCOUNT(String iSACCOUNT){
        ISACCOUNT=iSACCOUNT;
    }
    public  String getISACCOUNT(){
        return ISACCOUNT;
    }

    public  void   setACTIONCODE(String aCTIONCODE){
        ACTIONCODE=aCTIONCODE;
    }
    public  String getACTIONCODE(){
        return ACTIONCODE;
    }

    public  void   setDATETIME(Timestamp dATETIME){
        DATETIME=dATETIME;
    }
    public  Timestamp getDATETIME(){
        return DATETIME;
    }

    public String getCALLID() {
        return CALLID;
    }

    public void setCALLID(String CALLID) {
        this.CALLID = CALLID;
    }

    public  void   setISACTIVEFORRETRY(String iSACTIVEFORRETRY){
        ISACTIVEFORRETRY=iSACTIVEFORRETRY;
    }
    public  String getISACTIVEFORRETRY(){
        return ISACTIVEFORRETRY;
    }

    public  void   setFOLLOWCODE(String fOLLOWCODE){
        FOLLOWCODE=fOLLOWCODE;
    }
    public  String getFOLLOWCODE(){
        return FOLLOWCODE;
    }

    public  void   setTRANCEDATE(String tRANCEDATE){
        TRANCEDATE=tRANCEDATE;
    }
    public  String getTRANCEDATE(){
        return TRANCEDATE;
    }

    public  void   setTRANSTIME(String tRANSTIME){
        TRANSTIME=tRANSTIME;
    }
    public  String getTRANSTIME(){
        return TRANSTIME;
    }

    public  void   setSOURCEOFPAYMENT(String sOURCEOFPAYMENT){
        SOURCEOFPAYMENT=sOURCEOFPAYMENT;
    }
    public  String getSOURCEOFPAYMENT(){
        return SOURCEOFPAYMENT;
    }

    public String getBILLID() {
        return BILLID;
    }

    public void setBILLID(String BILLID) {
        this.BILLID = BILLID;
    }

    public String getPAYMENTID() {
        return PAYMENTID;
    }

    public void setPAYMENTID(String PAYMENTID) {
        this.PAYMENTID = PAYMENTID;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getISFUNDTRUSFER() {
        return ISFUNDTRUSFER;
    }

    public void setISFUNDTRUSFER(String ISFUNDTRUSFER) {
        this.ISFUNDTRUSFER = ISFUNDTRUSFER;
    }

    public String getISFUNDTRANSFERWITHID() {
        return ISFUNDTRANSFERWITHID;
    }

    public void setISFUNDTRANSFERWITHID(String ISFUNDTRANSFERWITHID) {
        this.ISFUNDTRANSFERWITHID = ISFUNDTRANSFERWITHID;
    }

    public String getISBILLPAYMENTOFACCOUNT() {
        return ISBILLPAYMENTOFACCOUNT;
    }

    public void setISBILLPAYMENTOFACCOUNT(String ISBILLPAYMENTOFACCOUNT) {
        this.ISBILLPAYMENTOFACCOUNT = ISBILLPAYMENTOFACCOUNT;
    }

    public String getISINSTALLMENTPAYMENT() {
        return ISINSTALLMENTPAYMENT;
    }

    public void setISINSTALLMENTPAYMENT(String ISINSTALLMENTPAYMENT) {
        this.ISINSTALLMENTPAYMENT = ISINSTALLMENTPAYMENT;
    }

    public String getISBILLPAYMENTOFPAN() {
        return ISBILLPAYMENTOFPAN;
    }

    public void setISBILLPAYMENTOFPAN(String ISBILLPAYMENTOFPAN) {
        this.ISBILLPAYMENTOFPAN = ISBILLPAYMENTOFPAN;
    }

    public Date getDATEOFTRANSACTION() {
        return DATEOFTRANSACTION;
    }

    public void setDATEOFTRANSACTION(Date DATEOFTRANSACTION) {
        this.DATEOFTRANSACTION = DATEOFTRANSACTION;
    }

    public String getDONEFLAG() {
        return DONEFLAG;
    }

    public void setDONEFLAG(String DONEFLAG) {
        this.DONEFLAG = DONEFLAG;
    }

    public String getDESTINATIONACCOUNT() {
        return DESTINATIONACCOUNT;
    }

    public void setDESTINATIONACCOUNT(String DESTINATIONACCOUNT) {
        this.DESTINATIONACCOUNT = DESTINATIONACCOUNT;
    }

    public String getTRANFERID() {
        return TRANFERID;
    }

    public void setTRANFERID(String TRANFERID) {
        this.TRANFERID = TRANFERID;
    }
}
