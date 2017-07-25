//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.messages;

import Mainchannel.Exceptions.ResponseParsingException;
import ServiceObjects.Account.BalanceForAccount;
import ServiceObjects.Other.DBUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;
import utils.FarsiConvertor;
import utils.PropertiesUtils;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.*;

public class XML2MessageConvertor {
    static Log log = LogFactory.getLog(XML2MessageConvertor.class);
    String Action_Codes_For_XMLError_Response = "6003,1840,9126,6002,6001,1839,6004";
    //String Action_Codes_For_XMLError_Response = "6003,1840,9126,6002,6001,1839";

    public XML2MessageConvertor() {
    }

    private boolean checkCase(String key, String value) {
        return key == null?false:value.indexOf(key) > -1;
    }

    public StatementListMessage getResponseStatementList(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        StatementListMessage retval = null;
        String resType = "STATEMENTLIST";
        Vector transes = new Vector();
        int transCount = 0;

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (JDOMException var43) {
            log.error("Error in Reading recived Data: " + var43.getMessage());
            throw new ResponseParsingException(var43);
        } catch (Exception var44) {
            log.error("Error in Reading recived Data: " + var44.getMessage());
            throw new ResponseParsingException(var44);
        }
        DBUtils dbUtils=new DBUtils();
        Map<String,String> branchCodes=new HashMap<String,String>();
        Map<String,String> descriptions=new HashMap<String,String>();
        try {
            branchCodes=dbUtils.getBranchNames();
            descriptions=dbUtils.getTransactionDescriptions();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new StatementListMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc("description");
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String accountNumber = e.getChild("ACCNO").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String transCount2 = e.getChild("TRANSCOUNT").getText();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String fromDate = e.getChild("FDATE").getText();
                String toDate = e.getChild("TDATE").getText();
                String balance = deletePointSignOfBalance(e.getChild("BALANCE").getText());
                List tranlist = e.getChild("TRANSACTIONS").getChildren();
                Iterator traniter = tranlist.iterator();

                String resDateTime;
                while(traniter.hasNext()) {
                    Element mac = (Element)traniter.next();
                    ++transCount;
                    resDateTime = mac.getChild("OPCODE").getText();
                    String amount = deletePointSignOfBalance(mac.getChild("AMNT").getText());
                    String lastAmount =deletePointSignOfBalance(mac.getChild("LAMNT").getText());
                    String creditDebit = mac.getChild("CRDB").getText();
                    String transDate = mac.getChild("DT").getText();
                    String transTime = mac.getChild("TM").getText();
                    String transEffectiveDate = mac.getChild("EFDT").getText();
                    String transCheckNo = mac.getChild("DOCNO").getText();
                    String branchCode = mac.getChild("BRCODE").getText();
                    String transNo = mac.getChild("ROW").getText();
                    String trckId = mac.getChild("TRCKID").getText();
                    String termType = mac.getChild("TRML").getText();
                    String[] payIds = mac.getChild("PAYIDS").getText().split(",");
                    String transDesc="";
                    String branchName="";

//                         transDesc=dbUtils.getTransactionDescription(resDateTime);
                         transDesc=descriptions.get(resDateTime);
                        if (transDesc==null){
                            transDesc=resDateTime;
                        }
//                         branchName=dbUtils.getBranchName(branchCode);
                     try{

                         branchCode=String.valueOf(Integer.valueOf(branchCode));
                     }catch (Exception e1){
                         branchCode="";
                     }

                    branchName=branchCodes.get(branchCode);
                    if (branchName==null){
                        branchName=branchCode;
                    }else{
                        branchName=branchCode+"-"+branchCodes.get(branchCode);

                    }

                    String shpInf ="";
                    if (PropertiesUtils.getUseExtraInfo()) shpInf=mac.getChild("EXTRAINFO").getText();
                    else shpInf=transDesc;



                    String payId1 = "";
                    String payIs2 = "";
                    try{ payId1 = payIds[0];}catch (Exception ee){}
                    try{ payIs2 = payIds[1];}catch (Exception ee){}
                    String rrn = mac.getChild("RRN").getText();
                    StatementMessage trans = new StatementMessage(creditDebit, resDateTime, amount, lastAmount, transDate, transEffectiveDate, transCheckNo, "", branchCode, transNo, transTime);
                    trans.setPayId1(payId1);
                    trans.setPayId2(payIs2);
                    trans.setTrckId(trckId);
                    trans.setTermType(termType);
                    trans.setShpInf(shpInf);
                    trans.setBranchName(branchName);
                    trans.setRrn(rrn);
                    transes.add(trans);
                }

                String var45 = e.getChild("MAC").getText();
                resDateTime = e.getChild("RESPDATETIME").getText();
                if(requestType.equals(resType)) {
                    retval = new StatementListMessage(accountNumber, balance, fromDate, toDate, transCount2, transes);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc("description");
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(var45);
                    retval.setRespDateTime(resDateTime);
                }
            }
             branchCodes=null;
            descriptions=null;
            return retval;
        }
    }


    public FundTransferMessage getResponseFundTransfer(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        FundTransferMessage retval = null;
        String resType = "FUNDTRANSFER";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var24) {
            log.error("Error in Reading recived Data: " + var24.getMessage());
            throw new ResponseParsingException(var24);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new FundTransferMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String srcAccountNumber = e.getChild("SRCACCNO").getText();
                String dstAccountNumber = e.getChild("DSTACCNO").getText();
                String transAmount = e.getChild("TRANSAMOUNT").getText();
                String opCode = e.getChild("OPCODE").getText();
                String ftDesc = e.getChild("TRANSDESC").getText();
                String refNo = e.getChild("CMINTERNALREFNO").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                if(requestType.equals(resType)) {
                    retval = new FundTransferMessage(srcAccountNumber, dstAccountNumber, transAmount, "", ftDesc, opCode, refNo);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                }
            }

            return retval;
        }
    }
    public FundTransferNoCostMessage getResponseFundTransferNoCost(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        FundTransferNoCostMessage retval = null;
        String resType = "NOCHRGFT";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var24) {
            log.error("Error in Reading recived Data: " + var24.getMessage());
            throw new ResponseParsingException(var24);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new FundTransferNoCostMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String srcAccountNumber = e.getChild("SRCACCNO").getText();
                String dstAccountNumber = e.getChild("DSTACCNO").getText();
                String transAmount = e.getChild("TRANSAMOUNT").getText();
                String opCode = e.getChild("OPCODE").getText();
                String ftDesc = e.getChild("TRANSDESC").getText();
                String refNo = e.getChild("CMINTERNALREFNO").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                if(requestType.equals(resType)) {
                    retval = new FundTransferNoCostMessage(srcAccountNumber, dstAccountNumber, transAmount, "", ftDesc, opCode, refNo);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                }
            }

            return retval;
        }
    }


    public BillPaymentMessage getResponseBillPayment(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        BillPaymentMessage retval = null;
        String resType = "BILLPAYMENT";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var25) {
            log.error("Error in Reading recived Data: " + var25.getMessage());
            throw new ResponseParsingException(var25);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new BillPaymentMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String srcAccount = e.getChild("SRCACC").getText();
                String amount = e.getChild("AMOUNT").getText();
                String billID = e.getChild("BILLID").getText();
                String paymentID = e.getChild("PAYMENTID").getText();
                String serviceCode = e.getChild("SERVICECODE").getText();
                String company = e.getChild("COMPANYCODE").getText();
                String refNo = e.getChild("CMINTERNALREFNO").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                if(requestType.equals(resType)) {
                    retval = new BillPaymentMessage(srcAccount, amount, company, billID, paymentID, refNo, serviceCode);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                }
            }

            return retval;
        }
    }

    public BillPaymentValidation getResponseBillPaymentValidation(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        BillPaymentValidation retval = null;
        String resType = "BILLPAYMENTVALIDATION";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var24) {
            log.error("Error in Reading recived Data: " + var24.getMessage());
            throw new ResponseParsingException(var24);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new BillPaymentValidation();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String srcAccount = e.getChild("SRCACC").getText();
                String amount = e.getChild("AMOUNT").getText();
                String billID = e.getChild("BILLID").getText();
                String paymentID = e.getChild("PAYMENTID").getText();
                String serviceCode = e.getChild("SERVICECODE").getText();
                String company = e.getChild("COMPANYCODE").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                if(requestType.equals(resType)) {
                    retval = new BillPaymentValidation(srcAccount, amount, company, billID, paymentID, serviceCode);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                }
            }

            return retval;
        }
    }

    public AccountListMessage getResponseAccountList(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();


        Document doc = null;
        AccountListMessage retval = null;
        String resType = "ACCOUNTLIST";
        FarsiConvertor farsiConvertor=new FarsiConvertor();

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var21) {
            log.error("Error in Reading recived Data: " + var21.getMessage());
            throw new ResponseParsingException(var21);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new AccountListMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String accountNumber = e.getChild("ACCNO").getText();
                String accountType = e.getChild("ACCGROUP").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                String prsnType = e.getChild("PRSNTYPE").getText();
                String FARSINAME="";
                String FARSIFAMILY="";
                String FATHERNAME="";
                String ACCBRNID="";
                try{
                    FARSINAME=farsiConvertor.getUTFFromCMFarsi(e.getChild("FARSINAME").getText());
                    FARSIFAMILY=farsiConvertor.getUTFFromCMFarsi(e.getChild("FARSIFAMILY").getText());
                    FATHERNAME=farsiConvertor.getUTFFromCMFarsi(e.getChild("FATHERNAME").getText());
                    ACCBRNID=e.getChild("ACCBRNID").getText();
                }catch (Exception e2){

                }

                if(requestType.equals("ACCOUNTINFORMATION")) {
                    retval = new AccountListMessage(accountNumber, accountType, e.getChild("ACCCURRENCY").getText(), e.getChild("ACCSTATUS").getText(), e.getChild("ACCHOST").getText(), e.getChild("ACCBRNID").getText(), e.getChild("LATINNAME").getText(), e.getChild("LATINFAMILY").getText(), "FARSINAME", "FARSIFAMILY", e.getChild("NATIONALCODE").getText(), e.getChild("BIRTHDATE").getText(), "BIRTHPLACE", "ADDRESS1", "ADDRESS2", e.getChild("HOMEPHONE").getText(), e.getChild("OFFICEPHONE").getText(), e.getChild("CELPHONE").getText(), "FATHERNAME", e.getChild("CREATIONDATETIME").getText());
                    retval.setPrsnType(prsnType);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                    retval.setFarsiName(FARSINAME);
                    retval.setFarsiFamily(FARSIFAMILY);
                    retval.setFatherName(FATHERNAME);
                    retval.setAccountBranchID(ACCBRNID);
                }
            }

            return retval;
        }
    }


    public ChequeStatusMessage getResponseChequeStatus(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        ChequeStatusMessage retval = null;
        String resType = "CHEQUESTATUS";

        Document doc;
        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var24) {
            log.error("Error in Reading recived Data: " + var24.getMessage());
            throw new ResponseParsingException(var24);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new ChequeStatusMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                String accountNumber = e.getChild("ACCNO").getText();
                String chequeNumber = e.getChild("CQNO").getText();
                String chequeAmount = e.getChild("CQAMNT").getText();
                String chequeOpDate = e.getChild("CQOPDATE").getText();
                String chequeStatus = e.getChild("CQST").getText();
                String chequeStatusDesc = e.getChild("CQSTDSC").getText();
                if(requestType.equals(resType)) {
                    retval = new ChequeStatusMessage(accountNumber, chequeNumber, chequeAmount, chequeOpDate);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                    retval.setChequeStatusDesc(chequeStatusDesc);
                    retval.setChequeStatus(chequeStatus);
                }
            }

            return retval;
        }
    }

    private String deletePointSignOfBalance(String balance) {

       if (balance.contains(".")){
            for (int i = 0; i < balance.length(); i++) {
                if (balance.charAt(i)=='.'){
                    balance=balance.substring(0,i);
                    return balance;
                }
            }
        }
        return balance;

    }


    public BalanceMessage getResponseBalance(String returnedDATA) throws ResponseParsingException {
        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        BalanceMessage retval = null;
        String resType = "BALANCE";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
            Element e = doc.getRootElement();
            String actionCode = e.getChild("ACTIONCODE").getText();
            String description = e.getChild("DESC").getText();
            if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
                retval = new BalanceMessage();
                retval.setAction_code(actionCode);
                retval.setAction_desc(description);
                return retval;
            } else {
                String requestType = e.getChild("RESPTYPE").getText();
                List list = e.getChildren(resType);
                Iterator iter = list.iterator();

                while(iter.hasNext()) {
                    Element e1 = (Element)iter.next();
                    String msgSeq = e1.getChild("MSGSEQ").getText();
                    String channelType = e1.getChild("CHNTYPE").getText();
                    String channelID = e1.getChild("CHNID").getText();
                    String mac = e1.getChild("MAC").getText();
                    String resDateTime = e1.getChild("RESPDATETIME").getText();
                    String accountNumber = e1.getChild("ACCNO").getText();
                    String accountGroup = e1.getChild("ACCGROUP").getText();
                    String availableBalance = deletePointSignOfBalance(e1.getChild("AVAILABLEBALANCE").getText());
                    String actualBalance = deletePointSignOfBalance(e1.getChild("ACTUALBALANCE").getText());
                    String creditDebit = e1.getChild("CRDB").getText();
                    String currency = e1.getChild("CURRENCY").getText();
                    String lastTransactionDate = e1.getChild("LASTTRANSDATE").getText();
                    String hostId = e1.getChild("HOSTID").getText();
                    String branchId = e1.getChild("BRNCODE").getText();
                    if(requestType.equals(resType)) {
                        retval = new BalanceMessage(accountNumber, accountGroup, availableBalance, actualBalance, currency, lastTransactionDate, creditDebit, hostId, branchId);
                        retval.setAction_code(actionCode);
                        retval.setAction_desc(description);
                        retval.setChannelId(channelID);
                        retval.setChannelType(channelType);
                        retval.setMsgSequence(msgSeq);
                        retval.setMAC(mac);
                        retval.setRespDateTime(resDateTime);
                    }
                }

                return retval;
            }
        } catch (Exception var27) {
            log.error("Error in Reading recived Data: " + var27.getMessage());
            throw new ResponseParsingException(var27);
        }
    }

    public RegistrationMessage getResponseRegistration(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        RegistrationMessage retval = null;
        String resType = "REGISTRATION";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var19) {
            log.error("Error in Reading recived Data: " + var19.getMessage());
            throw new ResponseParsingException(var19);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new RegistrationMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                String accountNumber = e.getChild("ACCNO").getText();
                if(requestType.equals(resType)) {
                    retval = new RegistrationMessage(accountNumber);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                }
            }

            return retval;
        }
    }


    public FollowUpMessage getResponseFollowUp(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        FollowUpMessage retval = null;
        String resType = "FOLLOWUP";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var25) {
            log.error("Error in Reading recived Data: " + var25.getMessage());
            throw new ResponseParsingException(var25);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new FollowUpMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                String srcaccountNumber = e.getChild("SRCACCNO").getText();
                String dstaccountNumber = e.getChild("DSTACCNO").getText();
                String followUpCode = e.getChild("FOLLOWUPCODE").getText();
                String transamount = e.getChild("TRANSAMOUNT").getText();
                String transDate = e.getChild("TRANSDATE").getText();
                String transTime = e.getChild("TRANSTIME").getText();
                String transStatus = e.getChild("TRANSSTATUS").getText();
                if(requestType.equals(resType)) {
                    retval = new FollowUpMessage(srcaccountNumber, dstaccountNumber, transamount, followUpCode, transDate, transTime, transStatus);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                }
            }

            return retval;
        }
    }



    public AccountMessage getBlockAccountResponse(String returnedData) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        AccountMessage retval = null;
        String resType = "ACCBLCK";

        try {
            Document doc = builder.build(new InputSource(new StringReader(returnedData)));
            Element e = doc.getRootElement();
            String actionCode = e.getChild("ACTIONCODE").getText();
            String description = e.getChild("DESC").getText();
            if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
                retval = new AccountMessage();
                retval.setAction_code(actionCode);
                retval.setAction_desc(description);
                return retval;
            } else {
                String requestType = e.getChild("RESPTYPE").getText();
                List list = e.getChildren(resType);
                Iterator iter = list.iterator();

                while(iter.hasNext()) {
                    Element e1 = (Element)iter.next();
                    String msgSeq = e1.getChild("MSGSEQ").getText();
                    String channelType = e1.getChild("CHNTYPE").getText();
                    String channelID = e1.getChild("CHNID").getText();
                    String mac = e1.getChild("MAC").getText();
                    String respDateTime = e1.getChild("RESPDATETIME").getText();
                    String accountNumber = e1.getChild("ACCNO").getText();
                    if(requestType.equals(resType)) {
                        retval = new AccountMessage();
                        retval.setAction_code(actionCode);
                        retval.setAction_desc(description);
                        retval.setMsgSequence(msgSeq);
                        retval.setChannelType(channelType);
                        retval.setChannelId(channelID);
                        retval.setAccountNumber(accountNumber);
                        retval.setRespDateTime(respDateTime);
                        retval.setMAC(mac);
                    }
                }

                return retval;
            }
        } catch (Exception var19) {
            log.error("Error in Reading Recieved Data : " + var19.getMessage());
            throw new ResponseParsingException(var19);
        }
    }


    public CardAccountMessage getPanGetterResponse(String returnedData) throws ResponseParsingException {
        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        CardAccountMessage retval = null;
        String resType = "GETPAN";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedData)));
            Element e = doc.getRootElement();
            String actionCode = e.getChild("ACTIONCODE").getText();
            String description = e.getChild("DESC").getText();
            if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
                retval = new CardAccountMessage();
                retval.setAction_code(actionCode);
                retval.setAction_desc(description);
                return retval;
            } else {
                String requestType = e.getChild("RESPTYPE").getText();
                List list = e.getChildren(resType);
                Iterator iter = list.iterator();

                while(iter.hasNext()) {
                    Element e1 = (Element)iter.next();
                    String msgSeq = e1.getChild("MSGSEQ").getText();
                    String channelType = e1.getChild("CHNTYPE").getText();
                    String channelID = e1.getChild("CHNID").getText();
                    String pan = e1.getChild("PAN").getText();
                    String resDateTime = e1.getChild("RESPDATETIME").getText();
                    String mac = e1.getChild("MAC").getText();
                    if(requestType.equals(resType)) {
                        retval = new CardAccountMessage();
                        retval.setPan(pan);
                        retval.setAction_code(actionCode);
                        retval.setAction_desc(description);
                        retval.setChannelId(channelID);
                        retval.setChannelType(channelType);
                        retval.setMsgSequence(msgSeq);
                        retval.setMAC(mac);
                        retval.setRespDateTime(resDateTime);
                    }
                }

                return retval;
            }
        } catch (Exception var19) {
            log.error("Error in Reading recived Data: " + var19.getMessage());
            throw new ResponseParsingException(var19);
        }
    }

    public CardAccountMessage getCardAccNoResponse(String returnedData) throws ResponseParsingException {
        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        CardAccountMessage retval = null;
        String resType = "GETCARDACCNO";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedData)));
            Element e = doc.getRootElement();
            String actionCode = e.getChild("ACTIONCODE").getText();
            String description = e.getChild("DESC").getText();
            if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
                retval = new CardAccountMessage();
                retval.setAction_code(actionCode);
                retval.setAction_desc(description);
                return retval;
            } else {
                String requestType = e.getChild("RESPTYPE").getText();
                List list = e.getChildren(resType);
                Iterator iter = list.iterator();

                while(iter.hasNext()) {
                    Element e1 = (Element)iter.next();
                    String msgSeq = e1.getChild("MSGSEQ").getText();
                    String channelType = e1.getChild("CHNTYPE").getText();
                    String channelID = e1.getChild("CHNID").getText();
                    String cfsAccNo = e1.getChild("CFSACCNO").getText();
                    String farAccNo = e1.getChild("FARACCNO").getText();
                    String resDateTime = e1.getChild("RESPDATETIME").getText();
                    String mac = e1.getChild("MAC").getText();
                    if(requestType.equals(resType)) {
                        retval = new CardAccountMessage();
                        retval.setCfsAccNo(cfsAccNo);
                        retval.setFarAccNo(farAccNo);
                        retval.setAction_code(actionCode);
                        retval.setAction_desc(description);
                        retval.setChannelId(channelID);
                        retval.setChannelType(channelType);
                        retval.setMsgSequence(msgSeq);
                        retval.setMAC(mac);
                        retval.setRespDateTime(resDateTime);
                    }
                }

                return retval;
            }
        } catch (Exception var20) {
            log.error("Error in Reading recived Data: " + var20.getMessage());
            throw new ResponseParsingException(var20);
        }
    }

    public RTGSFollowUpMessage getRTGSFollowUpResponse(String returnedDATA) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        RTGSFollowUpMessage retval = null;
        String resType = "RTGSFOLLOWUP";

        try {
            doc = builder.build(new InputSource(new StringReader(returnedDATA)));
        } catch (Exception var27) {
            log.error("Error in Reading recived Data: " + var27.getMessage());
            throw new ResponseParsingException(var27);
        }

        Element root = doc.getRootElement();
        String actionCode = root.getChild("ACTIONCODE").getText();
        String description = root.getChild("DESC").getText();
        if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
            retval = new RTGSFollowUpMessage();
            retval.setAction_code(actionCode);
            retval.setAction_desc(description);
            return retval;
        } else {
            String requestType = root.getChild("RESPTYPE").getText();
            List list = root.getChildren(resType);
            Iterator iter = list.iterator();

            while(iter.hasNext()) {
                Element e = (Element)iter.next();
                String msgSeq = e.getChild("MSGSEQ").getText();
                String channelType = e.getChild("CHNTYPE").getText();
                String channelID = e.getChild("CHNID").getText();
                String mac = e.getChild("MAC").getText();
                String resDateTime = e.getChild("RESPDATETIME").getText();
                String ibanSrcAcc = e.getChild("SRCIBAN").getText();
                String srcBnkId = e.getChild("SRCBNK").getText();
                String ibanDstAcc = e.getChild("DSTIBAN").getText();
                String dstBnkId = e.getChild("DSTBNK").getText();
                String followUpCode = e.getChild("FOLLOWUPCODE").getText();
                String transAmount = e.getChild("TRANSAMOUNT").getText();
                String transDate = e.getChild("TRANSDATE").getText();
                String transTime = e.getChild("TRANSTIME").getText();
                String transStatus = e.getChild("TRANSSTATUS").getText();
                if(requestType.equals(resType)) {
                    retval = new RTGSFollowUpMessage(srcBnkId, ibanSrcAcc, dstBnkId, ibanDstAcc, followUpCode, transAmount, transDate, transTime, transStatus);
                    retval.setAction_code(actionCode);
                    retval.setAction_desc(description);
                    retval.setChannelId(channelID);
                    retval.setChannelType(channelType);
                    retval.setMsgSequence(msgSeq);
                    retval.setMAC(mac);
                    retval.setRespDateTime(resDateTime);
                }
            }

            return retval;
        }
    }

    public ACHFundTransferMessage getACHFollowupResponse(String returnedData) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        ACHFundTransferMessage retval = null;
        String resType = "ACHFOLLOWUP";

        try {
            Document doc = builder.build(new InputSource(new StringReader(returnedData)));
            Element e = doc.getRootElement();
            String actionCode = e.getChild("ACTIONCODE").getText();
            String description = e.getChild("DESC").getText();
            if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
                retval = new ACHFundTransferMessage();
                retval.setAction_code(actionCode);
                retval.setAction_desc(description);
                return retval;
            } else {
                String requestType = e.getChild("RESPTYPE").getText();
                List list = e.getChildren(resType);
                Iterator iter = list.iterator();

                while(iter.hasNext()) {
                    Element e1 = (Element)iter.next();
                    String msgSeq = e1.getChild("MSGSEQ").getText();
                    String channelType = e1.getChild("CHNTYPE").getText();
                    String channelID = e1.getChild("CHNID").getText();
                    String srcIBAN = e1.getChild("SRCIBAN").getText();
                    String flwupCode = e1.getChild("FOLLOWUPCODE").getText();
                    String transAmnt = e1.getChild("TRANSAMOUNT").getText();
                    String transDate = e1.getChild("TRANSDATE").getText();
                    String transTime = e1.getChild("TRANSTIME").getText();
                    String transSt = e1.getChild("TRANSSTATUS").getText();
                    String trckCd = e1.getChild("TRACKCODE").getText();
                    String respDateTime = e1.getChild("RESPDATETIME").getText();
                    String mac = e1.getChild("MAC").getText();
                    if(requestType.equals(resType)) {
                        retval = new ACHFundTransferMessage();
                        retval.setAction_code(actionCode);
                        retval.setAction_desc(description);
                        retval.setMsgSequence(msgSeq);
                        retval.setChannelType(channelType);
                        retval.setChannelId(channelID);
                        retval.setSrcAccountNumber(srcIBAN);
                        retval.setFollowupCode(flwupCode);
                        retval.setTransAmount(transAmnt);
                        retval.setTransDate(transDate);
                        retval.setTransTime(transTime);
                        retval.setTransStatus(transSt);
                        retval.setTrackCode(trckCd);
                        retval.setRespDateTime(respDateTime);
                        retval.setMAC(mac);
                    }
                }

                return retval;
            }
        } catch (Exception var25) {
            log.error(var25);
            throw new ResponseParsingException(var25);
        }
    }

    public ACHFundTransferMessage getACHFlwupByTrackIdResponse(String returnedData) throws ResponseParsingException {
        SAXBuilder builder = new SAXBuilder();
        ACHFundTransferMessage retval = null;
        String resType = "ACHFLWUPBYTRACKID";

        try {
            Document doc = builder.build(new InputSource(new StringReader(returnedData)));
            Element e = doc.getRootElement();
            String actionCode = e.getChild("ACTIONCODE").getText();
            String description = e.getChild("DESC").getText();
            if(this.checkCase(actionCode, this.Action_Codes_For_XMLError_Response)) {
                retval = new ACHFundTransferMessage();
                retval.setAction_code(actionCode);
                retval.setAction_desc(description);
                return retval;
            } else {
                String requestType = e.getChild("RESPTYPE").getText();
                List list = e.getChildren(resType);
                Iterator iter = list.iterator();

                while(iter.hasNext()) {
                    Element e1 = (Element)iter.next();
                    String msgSeq = e1.getChild("MSGSEQ").getText();
                    String channelType = e1.getChild("CHNTYPE").getText();
                    String channelID = e1.getChild("CHNID").getText();
                    String trckId = e1.getChild("TRCKID").getText();
                    String srcIban = e1.getChild("SRCIBAN").getText();
                    String dstIban = e1.getChild("DSTIBAN").getText();
                    String reqDate = e1.getChild("REQDATE").getText();
                    String effctvDate = e1.getChild("EFFCTVDATE").getText();
                    String transAmnt = e1.getChild("TRANSAMNT").getText();
                    String transSt = e1.getChild("TRANSST").getText();
                    String respDateTime = e1.getChild("RESPDATETIME").getText();
                    String mac = e1.getChild("MAC").getText();
                    if(requestType.equals(resType)) {
                        retval = new ACHFundTransferMessage();
                        retval.setAction_code(actionCode);
                        retval.setAction_desc(description);
                        retval.setMsgSequence(msgSeq);
                        retval.setChannelType(channelType);
                        retval.setChannelId(channelID);
                        retval.setFileId(trckId);
                        retval.setFileSt(srcIban);
                        retval.setFollowupCode(dstIban);
                        retval.setFollowupCode(reqDate);
                        retval.setFollowupCode(effctvDate);
                        retval.setFollowupCode(transAmnt);
                        retval.setFollowupCode(transSt);
                        retval.setRespDateTime(respDateTime);
                        retval.setMAC(mac);
                    }
                }

                return retval;
            }
        } catch (Exception var25) {
            log.error(var25);
            throw new ResponseParsingException(var25);
        }
    }


    public String GenerateErrorXML(String actionCodeStr, String desc_str) {
        System.out.println("actioncode str is:" + actionCodeStr);
        Element root = new Element("root");
        Document doc = new Document(root);
        doc.getContent().add(0, new Comment(" Generated: 1385/12/28 "));
        Element actionCode = new Element("ACTIONCODE");
        actionCode.setText(actionCodeStr);
        root.addContent(actionCode);
        Element desc = new Element("DESC");
        desc.setText(desc_str);
        root.addContent(desc);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }
}
