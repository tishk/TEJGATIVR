//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import java.util.Calendar;

public class Message2XMLConvertor {
    public Message2XMLConvertor() {
    }

    public String GenerateBalanceRequest(String accountNumber, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "BALANCE";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("ACCNO")).setText(accountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateRegistrationRequest(String accountNumber, String accountPass, String serviceType, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "REGISTRATION";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("ACCNO")).setText(accountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("SERVICETYPE")).setText(serviceType));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateCurrencyRateRequest(String currency, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "CURRENCYRATE";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateFollowUpRequest(String srcAccountNumber, String dstAccountNumber, String followUpCode, String accountPass, String origTransDateTime, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "FOLLOWUP";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("SRCACCNO")).setText(srcAccountNumber));
        account.addContent((new Element("DSTACCNO")).setText(dstAccountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("FOLLOWUPCODE")).setText(followUpCode));
        account.addContent((new Element("ORIGDATETIME")).setText(origTransDateTime));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateBillPaymentRequest(String msgSequence, String srcAccount, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String amount, String billID, String paymentID, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "BILLPAYMENT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("SRCACC")).setText(srcAccount));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("AMOUNT")).setText(amount));
        account.addContent((new Element("BILLID")).setText(billID));
        account.addContent((new Element("PAYMENTID")).setText(paymentID));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateBillPaymentValidationRequest(String msgSequence, String srcAccount, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String amount, String billID, String paymentID, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "BILLPAYMENTVALIDATION";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("SRCACC")).setText(srcAccount));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("AMOUNT")).setText(amount));
        account.addContent((new Element("BILLID")).setText(billID));
        account.addContent((new Element("PAYMENTID")).setText(paymentID));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateAccountListRequest(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String msgSeq, String requestDateTime, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "ACCOUNTLIST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText("ACCOUNTINFORMATION"));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("ACCNO")).setText(accountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateLoanBalanceRequest(String accountNumber, String customerPass, String channelType, String channelPass, String channelID) {
        String xmlstr = "";
        String MAC = "1";
        String encAlgorytm = "1";
        String reqType = "LOANBALANCE";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        Element account = new Element(reqType);
        account.addContent((new Element("ACCOUNTNUMBER")).setText(accountNumber));
        account.addContent((new Element("CUSTOMERPASS")).setText(customerPass));
        account.addContent((new Element("CHANNELTYPE")).setText(channelType));
        account.addContent((new Element("CHANNELPASS")).setText(channelPass));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("CHANNELID")).setText(channelID));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateLoanCreditListRequest(String accountNumber, String customerPass, String channelType, String channelPass, String channelID) {
        String xmlstr = "";
        String MAC = "1";
        String encAlgorytm = "1";
        String reqType = "LOANCREDITLIST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        Element account = new Element(reqType);
        account.addContent((new Element("ACCOUNTNUMBER")).setText(accountNumber));
        account.addContent((new Element("CUSTOMERPASS")).setText(customerPass));
        account.addContent((new Element("CHANNELTYPE")).setText(channelType));
        account.addContent((new Element("CHANNELPASS")).setText(channelPass));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("CHANNELID")).setText(channelID));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateOrderChequeBookRequest(String accountNumber, String customerPass, String channelType, String channelPass, String channelID, String orderType, int quantity, int noOfPage, String dispathBy) {
        String xmlstr = "";
        String MAC = "1";
        String encAlgorytm = "1";
        String reqType = "ORDERCHEQUEBOOK";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        Element account = new Element(reqType);
        account.addContent((new Element("ACCOUNTNUMBER")).setText(accountNumber));
        account.addContent((new Element("CUSTOMERPASS")).setText(customerPass));
        account.addContent((new Element("CHANNELTYPE")).setText(channelType));
        account.addContent((new Element("CHANNELPASS")).setText(channelPass));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("CHANNELID")).setText(channelID));
        account.addContent((new Element("ORDERTYPE")).setText(orderType));
        account.addContent((new Element("QUANTITY")).setText(String.valueOf(quantity)));
        account.addContent((new Element("NOOFPAGE")).setText(String.valueOf(noOfPage)));
        account.addContent((new Element("DISPATHBY")).setText(dispathBy));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateCurrencyExchangeRequest(String customerPass, String channelType, String channelPass, String channelID, String currency) {
        String xmlstr = "";
        String MAC = "1";
        String encAlgorytm = "1";
        String reqType = "CURRENCYEXCHANGE";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        Element account = new Element(reqType);
        account.addContent((new Element("CUSTOMERPASS")).setText(customerPass));
        account.addContent((new Element("CHANNELTYPE")).setText(channelType));
        account.addContent((new Element("CHANNELPASS")).setText(channelPass));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("CHANNELID")).setText(channelID));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateChequeStatusRequest(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String chequeNo, String chequeAmnt, String chequeOpDate, String requestDateTime, String msgSequence, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "CHEQUESTATUS";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("ACCNO")).setText(accountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("CQNO")).setText(chequeNo));
        account.addContent((new Element("CQAMNT")).setText(chequeAmnt));
        account.addContent((new Element("CQOPDATE")).setText(chequeOpDate));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("MAC")).setText(MAC));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateChequeRegistrationRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String cqNo, String cqAmnt, String cqDate, String payee, String reson, String reqDateTime, String encAlgthm, String mac) {
        String xmlstr = "";
        String reqType = "CHEQUEREGISTRATION";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("CQNO")).setText(cqNo));
        account.addContent((new Element("CQAMNT")).setText(cqAmnt));
        account.addContent((new Element("TO")).setText(payee));
        account.addContent((new Element("FOR")).setText(reson));
        account.addContent((new Element("CQDATE")).setText(cqDate));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("MAC")).setText(mac));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateChequeRevocationRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String cqNo, String cqAmnt, String cqDate, String reqDateTime, String encAlgthm, String mac) {
        String xmlstr = "";
        String reqType = "CHEQUEREVOCATION";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("CQNO")).setText(cqNo));
        account.addContent((new Element("CQAMNT")).setText(cqAmnt));
        account.addContent((new Element("CQDATE")).setText(cqDate));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("MAC")).setText(mac));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateStatementListRequest(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String fromDate, String toDate, String fromTime, String toTime, String transCount, String creditDebit, String transMinAmount, String transMaxAmount, String transDocNo, String transOprationCode, String transDesc, String BranchCode,String Brn, String stmType, String  msgSequence, String requestDateTime, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "STATEMENTLIST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
       // //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("ACCNO")).setText(accountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("STATEMENTTYPE")).setText(stmType));
        account.addContent((new Element("FDATE")).setText(fromDate));
        account.addContent((new Element("TDATE")).setText(toDate));
        account.addContent((new Element("FTIME")).setText(fromTime));
        account.addContent((new Element("TTIME")).setText(toTime));
        account.addContent((new Element("TRANSCOUNT")).setText(String.valueOf(transCount)));
        account.addContent((new Element("CRDB")).setText(creditDebit));
        account.addContent((new Element("TRANSMINAMOUNT")).setText(transMinAmount));
        account.addContent((new Element("TRANSMAXAMOUNT")).setText(transMaxAmount));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("TRANSDOCNO")).setText(transDocNo));
        account.addContent((new Element("TRANSOPCODE")).setText(transOprationCode));
        account.addContent((new Element("BRNCODE")).setText(BranchCode));
        account.addContent((new Element("RRN")).setText(Brn));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateCBStatementListRequest(String messageSequence, String channelType, String channelID, String channelPass, String accountNumber, String accountPass, String stmType, String fromDate, String toDate, String fromTime, String toTime, String transCount, String creditDebit, String transMinAmount, String transMaxAmount, String transDocNo, String oprationCode, String BranchCode, String cbPayId, String reqDateTime, String encAlgorytm, String mac) {
        String xmlstr = "";
        String reqType = "CBSTATEMENTLIST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(messageSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("ACCNO")).setText(accountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("STMTYPE")).setText(stmType));
        account.addContent((new Element("FDATE")).setText(fromDate));
        account.addContent((new Element("TDATE")).setText(toDate));
        account.addContent((new Element("FTIME")).setText(fromTime));
        account.addContent((new Element("TTIME")).setText(toTime));
        account.addContent((new Element("TRANSCOUNT")).setText(String.valueOf(transCount)));
        account.addContent((new Element("CRDB")).setText(creditDebit));
        account.addContent((new Element("MINAMOUNT")).setText(transMinAmount));
        account.addContent((new Element("MAXAMOUNT")).setText(transMaxAmount));
        account.addContent((new Element("DOCNO")).setText(transDocNo));
        account.addContent((new Element("OPCODE")).setText(oprationCode));
        account.addContent((new Element("BRNCODE")).setText(BranchCode));
        account.addContent((new Element("CBPAYID")).setText(cbPayId));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateFundTransferRequest(String srcAccountNumber, String dstAccountNumber, String transAmount, String currency, String transDesc, String accountPass, String channelType, String channelPass, String channelID, String opCode, String fPayCode, String sPayCode, String shpInf, String requestDateTime, String encAlgorytm, String MAC, String msgSeq) {
        String xmlstr = "";
        String reqType = "FUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("SRCACCNO")).setText(srcAccountNumber));
        account.addContent((new Element("DSTACCNO")).setText(dstAccountNumber));
        account.addContent((new Element("TRANSAMOUNT")).setText(transAmount));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("PAYCODE1")).setText(fPayCode));
        account.addContent((new Element("PAYCODE2")).setText(sPayCode));
        account.addContent((new Element("EXTRAINFO")).setText(shpInf));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }
    public String GenerateFundTransferNoCostRequest(String srcAccountNumber, String dstAccountNumber, String transAmount, String currency, String ftype, String transDesc, String accountPass, String channelType, String channelPass, String channelID, String opCode, String fPayCode, String sPayCode, String shpInf, String followupCode, String requestDateTime, String encAlgorytm, String MAC, String msgSeq) {
        String xmlstr = "";
        String reqType = "NOCHRGFT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("SRCACCNO")).setText(srcAccountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("DSTACCNO")).setText(dstAccountNumber));
        account.addContent((new Element("FTYPE")).setText(ftype));
        account.addContent((new Element("TRANSAMOUNT")).setText(transAmount));
        account.addContent((new Element("FOLLOWUPCODE")).setText(followupCode));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("PAYCODE1")).setText(fPayCode));
        account.addContent((new Element("PAYCODE2")).setText(sPayCode));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateCBFundTransferRequest(String msgSeq, String channelType, String channelID, String channelPass, String srcAccountNumber, String accountPass, String dstAccountNumber, String transAmount, String currency, String opCode, String fPayCode, String sPayCode, String cbPayId, String requestDateTime, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "CBFUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelID));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("SRCACCNO")).setText(srcAccountNumber));
        account.addContent((new Element("ACCPASS")).setText(accountPass));
        account.addContent((new Element("DSTACCNO")).setText(dstAccountNumber));
        account.addContent((new Element("AMOUNT")).setText(transAmount));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("FPAYCODE")).setText(fPayCode));
        account.addContent((new Element("SPAYCODE")).setText(sPayCode));
        account.addContent((new Element("CBPAYID")).setText(cbPayId));
        account.addContent((new Element("REQDATETIME")).setText(requestDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateRTGSFundTransferRequest(String msgSeq, String channelType, String channelId, String channelPass, String srcBank, String srcAccNo, String accPass, String senderName, String senderFamily, String senderNationalCode, String senderAdd, String senderPostalCode, String senderPhone, String srcBrnchCode, String dstBank, String dstAccNo, String recieverName, String recieverFamily, String recieverNationalCode, String recieverAdd, String recieverPostalCode, String recieverPhone, String dstBranchCode, String transAmount, String currency, String opCode, String payCode, String transDesc, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "RTGSFUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelId));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("SRCBNK")).setText(srcBank));
        account.addContent((new Element("SRCIBAN")).setText(srcAccNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("SENDNAME")).setText(senderName));
        account.addContent((new Element("SENDFAMILY")).setText(senderFamily));
        account.addContent((new Element("SENDNCODE")).setText(senderNationalCode));
        account.addContent((new Element("SENDADDRESS")).setText(senderAdd));
        account.addContent((new Element("SENDPCODE")).setText(senderPostalCode));
        account.addContent((new Element("SENDPHONE")).setText(senderPhone));
        account.addContent((new Element("SRCBRNCODE")).setText(srcBrnchCode));
        account.addContent((new Element("DSTBNK")).setText(dstBank));
        account.addContent((new Element("DSTIBAN")).setText(dstAccNo));
        account.addContent((new Element("RECVNAME")).setText(recieverName));
        account.addContent((new Element("RECVFAMILY")).setText(recieverFamily));
        account.addContent((new Element("RECVNCODE")).setText(recieverNationalCode));
        account.addContent((new Element("RECVADDRESS")).setText(recieverAdd));
        account.addContent((new Element("RECVPCODE")).setText(recieverPostalCode));
        account.addContent((new Element("RECVPHONE")).setText(recieverPhone));
        account.addContent((new Element("DSTBRNCODE")).setText(dstBranchCode));
        account.addContent((new Element("TRANSAMOUNT")).setText(transAmount));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("PAYMENTCODE")).setText(payCode));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String GenerateFundTransferReverseRequest(String reqsecuence, String srcAccountNumber, String dstAccountNumber, String transAmount, String currency, String ftDate, String ftTime, String customerPass, String channelType, String channelPass, String channelID) {
        String xmlstr = "";
        String MAC = "1";
        String encAlgorytm = "1";
        String reqType = "FUNDTRANSFERREVERSE";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        Element account = new Element(reqType);
        account.addContent((new Element("REQUESTTYPE")).setText(reqType));
        account.addContent((new Element("REQSECUENCE")).setText(reqsecuence));
        account.addContent((new Element("CUSTOMERPASS")).setText(customerPass));
        account.addContent((new Element("CHANNELTYPE")).setText(channelType));
        account.addContent((new Element("CHANNELPASS")).setText(channelPass));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("CHANNELID")).setText(channelID));
        account.addContent((new Element("SRCACCOUNTNUMBER")).setText(srcAccountNumber));
        account.addContent((new Element("DSTACCOUNTNUMBER")).setText(dstAccountNumber));
        account.addContent((new Element("TRANSAMOUNT")).setText(transAmount));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("FTDATE")).setText(ftDate));
        account.addContent((new Element("FTTIME")).setText(ftTime));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateBlockCardAccRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String amount, String reqDateTime, String encAlgrthm, String mac) {
        String xmlstr = "";
        String reqType = "BLOCKING";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("AMNT")).setText(amount));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateUnBlockCardAccRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgrthm, String mac) {
        String xmlstr = "";
        String reqType = "UNBLOCKING";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateShowCardAccStatusRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgrthm, String mac) {
        String xmlstr = "";
        String reqType = "SHOWACCSTATUS";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateBlockAccountRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckMode, String reqDateTime, String encAlgthm, String mac) {
        String xmlStr = "";
        String reqType = "ACCBLCK";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated: " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKMODE")).setText(blckMode));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generateBlckAccStatusRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgthm, String mac) {
        String xmlStr = "";
        String reqType = "GETACCBLCKST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated: " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generareBlckTransAmntRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String blckAmnt, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "BLCKTRNSAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BLCKAMNT")).setText(blckAmnt));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generareFundTransferByBlockNumberRequest(String msgSeq, String chnType, String chnId, String chnPass, String srcAccNo, String accPass, String dstAccNo, String blckNo, String transAmnt, String payCode, String branchId, String opCode, String transDesc, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "FTBYBLCKNO";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCACCNO")).setText(srcAccNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("DSTACCNO")).setText(dstAccNo));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("TRANSAMNT")).setText(transAmnt));
        account.addContent((new Element("PAYCODE")).setText(payCode));
        account.addContent((new Element("BRANCHID")).setText(branchId));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generareUnBlockTransactionAmountRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String unBlckAmnt, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "UNBLCKTRNSAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("UNBLCKAMNT")).setText(unBlckAmnt));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generareblockStatusInquiryListRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String blckSt, String fromBlckDt, String toBlckDt, String lsCount, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "BLCKSTINQRLS";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BLCKST")).setText(blckSt));
        account.addContent((new Element("FROMBLCKDT")).setText(fromBlckDt));
        account.addContent((new Element("TOBLCKDT")).setText(toBlckDt));
        account.addContent((new Element("LSCOUNT")).setText(lsCount));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String geterateGetPanRequest(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "GETPAN";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String geterateGetCardAccNoRequest(String msgSeq, String chnType, String chnId, String chnPass, String pan, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "GETCARDACCNO";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("PAN")).setText(pan));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generateInformativeIbanRequest(String msgSeq, String chnType, String chnId, String chnPass, String IbanAcc, String accPass, String refNo, String payCode, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "INFORMATIVEIBAN";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calc.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(IbanAcc));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REFNO")).setText(refNo));
        account.addContent((new Element("PAYCODE")).setText(payCode));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generateRTGSFollowUpRequest(String msgSeq, String chnType, String chnId, String chnPass, String srcBnkId, String srcAccNo, String dstBnkId, String dstAccNo, String followUpCode, String reqDateTime, String encAlgorithm, String MAC) {
        String xmlstr = "";
        String reqType = "RTGSFOLLOWUP";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCBNK")).setText(srcBnkId));
        account.addContent((new Element("SRCACCNO")).setText(srcAccNo));
        account.addContent((new Element("DSTBNK")).setText(dstBnkId));
        account.addContent((new Element("DESTACCNO")).setText(dstAccNo));
        account.addContent((new Element("FOLLOWUPCODE")).setText(followUpCode));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateIsSatnaOpenRequest(String msgSeq, String chnType, String chnId, String chnPass, String reqDateTime, String encAlgorytm, String MAC) {
        String xmlstr = "";
        String reqType = "ISSATNAOPEN";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorytm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String chequeListRequestGenerator(String msgSeq, String chnType, String chnPass, String chnId, String accNo, String accPass, String cqNo, String cqStatus, String cqCount, String cqFromDate, String cqToDate, String cqFromTime, String cqToTime, String cqMinAmnt, String cqMaxAmnt, String reqDateTime, String encAlgthm, String mac) {
        String xmlstr = "";
        String reqType = "CHEQUELIST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("CQNO")).setText(cqNo));
        account.addContent((new Element("CQST")).setText(cqStatus));
        account.addContent((new Element("CQCOUNT")).setText(cqCount));
        account.addContent((new Element("FROMDATE")).setText(cqFromDate));
        account.addContent((new Element("TODATE")).setText(cqToDate));
        account.addContent((new Element("FROMTIME")).setText(cqFromTime));
        account.addContent((new Element("TOTIME")).setText(cqToTime));
        account.addContent((new Element("MINAMNT")).setText(cqMinAmnt));
        account.addContent((new Element("MAXAMNT")).setText(cqMaxAmnt));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String customerChequeListRequestGenerator(String msgSeq, String chnType, String chnPass, String chnId, String accNo, String accPass, String FromDate, String ToDate, String FromTime, String ToTime, String lsCount, String reqDateTime, String encAlgthm, String mac) {
        String xmlstr = "";
        String reqType = "CHEQUEBOOKLIST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("FROMDATE")).setText(FromDate));
        account.addContent((new Element("TODATE")).setText(ToDate));
        account.addContent((new Element("FROMTIME")).setText(FromTime));
        account.addContent((new Element("TOTIME")).setText(ToTime));
        account.addContent((new Element("LSCOUNT")).setText(lsCount));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String blckStockAmntReqGenerator(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String blckAmnt, String brokerNo, String blckDesc, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "BLCKSTCKAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BLCKAMNT")).setText(blckAmnt));
        account.addContent((new Element("BROKERNO")).setText(brokerNo));
        account.addContent((new Element("BLCKDESC")).setText(blckDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String unblckStockAmntReqGenerator(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String brokerNo, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "UNBLCKSTCKAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BROKERNO")).setText(brokerNo));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String blockShareStockAmntReqGenerator(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String blckAmnt, String blckDesc, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "BLCKSHRSTCKAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BLCKAMNT")).setText(blckAmnt));
        account.addContent((new Element("BLCKDESC")).setText(blckDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String unblockShareStockAmntReqGenerator(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String unblockAmnt, String blckDesc, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "UNBLCKSHRSTCKAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("UNBLCKAMNT")).setText(unblockAmnt));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generatEditBlckShareStockAmntReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String blckAmnt, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "EDTSHRSTCKAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BLCKAMNT")).setText(blckAmnt));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generatGetShareStockAccStReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String providerId, String brokerNo, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "SHSTCKACCST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("PROVIDERID")).setText(providerId));
        account.addContent((new Element("BROKERNO")).setText(brokerNo));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generatStockAccStatusReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "RPSTCKACCST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateStockFTReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String srcAccNo, String accPass, String dstAccNo, String blckNo, String brkNo, String transAmount, String currency, String opCode, String payCode, String transDesc, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "RPSTCKFUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCIBAN")).setText(srcAccNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("DSTIBAN")).setText(dstAccNo));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BROKERNO")).setText(brkNo));
        account.addContent((new Element("TRANSAMOUNT")).setText(transAmount));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("PAYMENTCODE")).setText(payCode));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateBlckStckTransactionLstReqMsg(String msgSeq, String channelType, String channelId, String channelPass, String accNo, String accPass, String blckNo, String transCount, String fromBlckDTTM, String toBlckDTTM, String reqDateTime, String encAlgrthm, String mac) {
        String reqType = "BLCKSTCKTRANSLIST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar cal = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated: " + cal.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelId));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("TRANSCOUNT")).setText(transCount));
        account.addContent((new Element("FROMBLCKDTTM")).setText(fromBlckDTTM));
        account.addContent((new Element("TOBLCKDTTM")).setText(toBlckDTTM));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generateRepStockAccTransLstReqMsg(String msgSequence, String channelType, String channelId, String channelPass, String accIBAN, String accPass, String blockId, String fDate, String tDate, String fTime, String tTime, String transCount, String crdb, String opCode, String minAmount, String maxAmount, String reqDateTime, String encAlgrthm, String mac) {
        String reqType = "REPSTCKTRANSLS";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar cal = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + cal.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(channelType));
        account.addContent((new Element("CHNID")).setText(channelId));
        account.addContent((new Element("CHNPASS")).setText(channelPass));
        account.addContent((new Element("IBANACC")).setText(accIBAN));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKID")).setText(blockId));
        account.addContent((new Element("FDATE")).setText(fDate));
        account.addContent((new Element("TDATE")).setText(tDate));
        account.addContent((new Element("FTIME")).setText(fTime));
        account.addContent((new Element("TTIME")).setText(tTime));
        account.addContent((new Element("TRANSCOUNT")).setText(transCount));
        account.addContent((new Element("CRDB")).setText(crdb));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("MINAMNT")).setText(minAmount));
        account.addContent((new Element("MAXAMNT")).setText(maxAmount));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generateShareStockFTReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String srcAccNo, String accPass, String dstAccNo, String blckNo, String transAmount, String opCode, String payCode, String brchId, String transDesc, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "SHSTCKFUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCACCNO")).setText(srcAccNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("DSTACCNO")).setText(dstAccNo));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("TRANSAMOUNT")).setText(transAmount));
        account.addContent((new Element("PAYMENTCODE")).setText(payCode));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("BRANCHID")).setText(brchId));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateShareStockTransFinderReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String transCount, String blckSt, String fromDtTm, String toDtTm, String minAmnt, String maxAmnt, String brokerNo, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "SHSTCKTRANSLIST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("TRANSCOUNT")).setText(transCount));
        account.addContent((new Element("BLCKST")).setText(blckSt));
        account.addContent((new Element("FROMDTTM")).setText(fromDtTm));
        account.addContent((new Element("TODTTM")).setText(toDtTm));
        account.addContent((new Element("MINAMNT")).setText(minAmnt));
        account.addContent((new Element("MAXAMNT")).setText(maxAmnt));
        account.addContent((new Element("BROKERNO")).setText(brokerNo));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generatActiveAbdcStckAccReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "ACTIVABDICTEDACC";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generatDeactiveAbdcStckAccReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "DEACTIVABDICTEDACC";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generatGetAbdcStckAccStReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgthm, String mac) {
        String reqType = "ABDICATEDACCSTATUS";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateFutureFTReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String srcAccNo, String accPass, String dstAccNo, String brokerNo, String transAmount, String currency, String opCode, String payCode, String transDesc, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "ABDCFUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCIBAN")).setText(srcAccNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("DSTIBAN")).setText(dstAccNo));
        account.addContent((new Element("BROKERNO")).setText(brokerNo));
        account.addContent((new Element("TRANSAMOUNT")).setText(transAmount));
        account.addContent((new Element("CURRENCY")).setText(transAmount));
        account.addContent((new Element("PAYMENTCODE")).setText(payCode));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generatePayaFTReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String srcIban, String accPass, String senderName, String senderId, String dstIban, String depositId, String amnt, String currency, String effectiveDate, String desc, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "ACHFUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCIBAN")).setText(srcIban));
        account.addContent((new Element("ACCPASS")).setText(chnPass));
        account.addContent((new Element("SENDNAME")).setText(senderName));
        account.addContent((new Element("SENDID")).setText(senderId));
        account.addContent((new Element("DSTIBAN")).setText(dstIban));
        account.addContent((new Element("PAYMENTCODE")).setText(depositId));
        account.addContent((new Element("TRANSAMOUNT")).setText(amnt));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("EFFCTVDATE")).setText(effectiveDate));
        account.addContent((new Element("TRANSDESC")).setText(desc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateACHFollowupRequest(String msgSequence, String chnType, String chnPass, String chnId, String srcIBaAN, String accPass, String dstIBAN, String followupCode, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "ACHFOLLOWUP";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calcl = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calcl.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("SRCIBAN")).setText(srcIBaAN));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("DSTIBAN")).setText(dstIBAN));
        account.addContent((new Element("FOLLOWUPCODE")).setText(followupCode));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String genereteACHFileRegistrationRequest(String msgSequence, String chnType, String chnPass, String chnId, String accPass, String fileId, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "ACHFILEREGISTRATION";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar cal = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + cal.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("FILEID")).setText(fileId));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generateACHRegFileFlwRequest(String msgSequence, String chnType, String chnPass, String chnId, String accPass, String fileId, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "ACHREGFILEFOLLOWUP";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar cal = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + cal.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("FILEID")).setText(fileId));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generateACHFileFollowupRequest(String msgSequence, String chnType, String chnPass, String chnId, String accPass, String fileId, String reqDateTime, String encAlgrthm, String mac) {
        String xmlStr = "";
        String reqType = "ACHFILEFOLLOWUP";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar cal = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + cal.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSequence));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("FILEID")).setText(fileId));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generateACHFlwupByTrackIdRequest(String msgSeq, String chnType, String chnPass, String chnId, String trackId, String reqDateTime, String encAlgrthm, String mac) {
        String reqType = "ACHFLWUPBYTRACKID";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calendar = Calendar.getInstance();
        doc.getContent().add(0, new Comment("Generated : " + calendar.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("TRCKID")).setText(trackId));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlStr = out.outputString(doc);
        return xmlStr;
    }

    public String generatePayaFTIPOReqMsg(String msgSeq, String chnType, String chnId, String chnPass, String srcIban, String accPass, String senderName, String senderId, String dstIban, String depositId, String amnt, String currency, String desc, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "ACHFUNDTRANSFER";
        Element root = new Element("RequestService");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCIBAN")).setText(srcIban));
        account.addContent((new Element("ACCPASS")).setText(chnPass));
        account.addContent((new Element("SENDNAME")).setText(senderName));
        account.addContent((new Element("SENDID")).setText(senderId));
        account.addContent((new Element("DSTIBAN")).setText(dstIban));
        account.addContent((new Element("DEPOSITID")).setText(depositId));
        account.addContent((new Element("TRANSAMOUNT")).setText(amnt));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("TRANSDESC")).setText(desc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateSatnaFlwByTrckCd(String msgSeq, String chnType, String chnId, String chnPass, String trackCode, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "RTGSFLWUPBYTRCKCD";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("TRACKCODE")).setText(trackCode));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateMncpalityBillPaymentReq(String msgSeq, String chnType, String chnId, String chnPass, String billId, String payId, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "MNCBILLPAYFLW";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("BILLID")).setText(billId));
        account.addContent((new Element("PAYID")).setText(payId));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateBlockCardAccByBlckNoReq(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String blckAmnt, String isForce, String reqDateTime, String encAlgrthm, String mac) {
        String xmlstr = "";
        String reqType = "BLCKCARDACCAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BLCKAMNT")).setText(blckAmnt));
        account.addContent((new Element("ISFORCE")).setText(isForce));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateUnblockCardAccByBlckNoReq(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String reqDateTime, String encAlgrthm, String mac) {
        String xmlstr = "";
        String reqType = "UNBLCKCARDACCAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgrthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String generateCardAccBlockStatusReq(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blockNo, String reqDateTime, String encAlgorithm, String mac) {
        String xmlstr = "";
        String reqType = "CARDACCBLCKST";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blockNo));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String allChequeReportRequestGenerator(String msgSeq, String chnType, String chnPass, String chnId, String accNo, String accPass, String cqNo, String cqSt, String reqDateTime, String encAlgthm, String mac) {
        String xmlstr = "";
        String reqType = "CHEQUESTREP";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("CQNO")).setText(cqNo));
        account.addContent((new Element("CQST")).setText(cqSt));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String blckChrgrAccAmntRequestGenerator(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String blckAmnt, String reqDateTime, String encAlgthm, String mac) {
        String xmlstr = "";
        String reqType = "BLCKCHRGRACCAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BLCKAMNT")).setText(blckAmnt));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String chrgFundTransferRequestGenerator(String msgSeq, String chnType, String chnId, String chnPass, String srcAcc, String accPass, String dstAcc, String blckNo, String opCode, String transDesc, String reqDateTime, String encAlg, String mac) {
        String xmlstr = "";
        String reqType = "CHRGFUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCACC")).setText(srcAcc));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("DSTACC")).setText(dstAcc));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlg));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String unblckChrgrAccAmntRequestGenerator(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckNo, String reqDateTime, String encAlg, String mac) {
        String xmlstr = "";
        String reqType = "UNBLCKCHRGRACCAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlg));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String blockEnExAmountRequestGenerator(String msgSeq, String chnType, String chnId, String chnPass, String ibanAcc, String accPass, String blckNo, String blckAmnt, String brokerNo, String blckDesc, String reqDateTime, String encAlg, String mac) {
        String xmlstr = "";
        String reqType = "BLCKENEXAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(ibanAcc));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BLCKAMNT")).setText(blckAmnt));
        account.addContent((new Element("BROKERNO")).setText(brokerNo));
        account.addContent((new Element("BLCKDESC")).setText(blckDesc));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlg));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String unblockEnExAmountRequestGenerator(String msgSeq, String chnType, String chnId, String chnPass, String ibanAcc, String accPass, String blckNo, String unblckAmnt, String brokerNo, String reqDateTime, String encAlg, String mac) {
        String xmlstr = "";
        String reqType = "UNBLCKENEXAMNT";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        //doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(ibanAcc));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("UNBLCKAMNT")).setText(unblckAmnt));
        account.addContent((new Element("BROKERNO")).setText(brokerNo));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlg));
        account.addContent((new Element("MAC")).setText(mac));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String findEnExBlockListReqGnrt(String msgSeq, String chnType, String chnId, String chnPass, String ibanAcc, String accPass, String blckNo, String lCounter, String blckSt, String fDt, String tDt, String fTm, String tTm, String minAmnt, String maxAmnt, String reqDateTime, String encAlgthm, String MAC) {
        String xmlstr = "";
        String reqType = "ENEXBLCKLS";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("IBANACC")).setText(ibanAcc));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("TRANSCOUNT")).setText(lCounter));
        account.addContent((new Element("BLCKST")).setText(blckSt));
        account.addContent((new Element("FDT")).setText(fDt));
        account.addContent((new Element("TDT")).setText(tDt));
        account.addContent((new Element("FTM")).setText(fTm));
        account.addContent((new Element("TTM")).setText(tTm));
        account.addContent((new Element("MINAMNT")).setText(minAmnt));
        account.addContent((new Element("MAXAMNT")).setText(maxAmnt));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgthm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String enexFTReqGntr(String msgSeq, String chnType, String chnId, String chnPass, String srcIban, String accPass, String dstIban, String blckNo, String brkId, String transAmount, String currency, String payCode, String transDesc, String opCode, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "ENEXFUNDTRANSFER";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("SRCIBAN")).setText(srcIban));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("DSTIBAN")).setText(dstIban));
        account.addContent((new Element("BLCKNO")).setText(blckNo));
        account.addContent((new Element("BROKERNO")).setText(brkId));
        account.addContent((new Element("TRANSAMNT")).setText(transAmount));
        account.addContent((new Element("CURRENCY")).setText(currency));
        account.addContent((new Element("PAYMENTCODE")).setText(payCode));
        account.addContent((new Element("TRANSDESC")).setText(transDesc));
        account.addContent((new Element("OPCODE")).setText(opCode));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }

    public String enexBalanceReqGntr(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgorithm, String MAC) {
        String reqType = "EXBALANCE";
        Element root = new Element("REQUEST");
        Document doc = new Document(root);
        Calendar calc1 = Calendar.getInstance();
        ////doc.getContent().add(0, new Comment(" Generated: " + calc1.getTime().toString()));
        root.addContent((new Element("REQTYPE")).setText(reqType));
        Element account = new Element(reqType);
        account.addContent((new Element("MSGSEQ")).setText(msgSeq));
        account.addContent((new Element("CHNTYPE")).setText(chnType));
        account.addContent((new Element("CHNID")).setText(chnId));
        account.addContent((new Element("CHNPASS")).setText(chnPass));
        account.addContent((new Element("ACCNO")).setText(accNo));
        account.addContent((new Element("ACCPASS")).setText(accPass));
        account.addContent((new Element("REQDATETIME")).setText(reqDateTime));
        account.addContent((new Element("ENCALGORYTM")).setText(encAlgorithm));
        account.addContent((new Element("MAC")).setText(MAC));
        root.addContent(account);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }
}
