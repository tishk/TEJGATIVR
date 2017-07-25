import Mainchannel.messages.StatementListMessage;
import Mainchannel.messages.StatementMessage;
import ServiceObjects.Account.Transaction;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import utils.PersianDateTime;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 11/23/2015.
 */
public class Service_Account_Fax extends BaseAgiScript {
    public  static Util util = new Util();
    private Call call=new Call();
    private int  faxMenuCount=0;
    private Set  faxMenu = new HashSet();

    private String startDate="";
    private String endDate="";
    private String maxCount="";
    private String maxAmount="";
    private String minAmount="";
    Voices Say=new Voices();
    private Transaction transaction=new Transaction();
    private StatementListMessage statementListMessage=new StatementListMessage();
    private StatementMessage[]     statementMessage;
    private PersianDateTime persianDateTime=new PersianDateTime();
    private ManagerConnection managerConnection;
    private ManagerConnectionFactory factory =null;
    boolean is30Transaction=false;

    public Service_Account_Fax(Call c) throws Exception {
        this.call=c;
        // String faxF="";
        transaction.setAccountNumber(call.getAccount());
       // startSendFax();
       // Util.printMessage("before send fax:",false);
      //  if (FaxListener.faxExtension % 2==0){
      //     faxF="/ivr/Fax/Fax2188803601";
      //  }else faxF="/ivr/Fax/Fax2188803602";
     //   Util.printMessage("faxExtensionnnnnnnnnnnnnnnnnnnnnnn:"+String.valueOf(FaxListener.faxExtension % 2),false);
      //  Util.printMessage("fax File is:"+faxF,false);
     // SendFax(faxF);
      //  Util.printMessage("after send fax:",false);
       //
       // last30Transaction();
        createMainMenu();
        sayMainMenu();
    }

    private  void createMainMenu(){
        /*
        1:last 30 transaction
        2:1 month
        3:date to date
        4:tedade delkhah
        5:sorat Hesabe delkhah
        9:exit
         */
        faxMenu.add("0");
        faxMenu.add("1");
        faxMenu.add("2");
        faxMenu.add("3");
        faxMenu.add("4");
        faxMenu.add("5");
        faxMenu.add("9");

    }
    private  void sayMainMenu() throws Exception {

        String Choice=null;

        boolean EntranceIsOK=false;
        while ((faxMenuCount<2)) {
           // Choice = call.getParentStart().Say.sayMenu(faxMenu,"007_");
            //Util.printMessage("faxExtensionnnnnnnnnnnnnnnnnnnnnnn:"+String.valueOf(FaxListener.faxExtension % 2),false);
            Choice = Say.sayMenu(faxMenu,"007_");
           // Choice="1";
            if (!Choice.equals("-1")){
                SelectSubMenu(Choice);
            }
            else call.getParentStart().Say.namafhomAst();
            faxMenuCount++;
        }
        call.getParentStart().byAndHangUp();
    }
    private  void SelectSubMenu(String Choice) throws Exception {
      //  SendFax("/ivr/Fax/"+Choice);
        switch (Choice){
            case "1":last30Transaction();
                break;
            case "2":oneMonth();
                break;
            case "3":dateToDate();
                break;
            case "4":optionalBycount();
                break;
            case "5":optional();
                break;
            case "9":exit();
                break;
            default:
                call.getParentStart().Say.namafhomAst();
                break;

        }
    }
    private  void SelectSubMenutest(String Choice) throws Exception {
        SendFax("/ivr/Fax/"+Choice);
    }

    private  void last30Transaction() throws Exception {
        transaction.setStatementType("8");
        transaction.setTransactionCount("30");
        is30Transaction=true;
        startSendFax();
    }

    private  void oneMonth() throws Exception {
        transaction.setStatementType("9");
        transaction.setTransactionCount("300");
        endDate=persianDateTime.getRequestDate();
        startDate=persianDateTime.getRequestDate(30);
        transaction.setStartDate(startDate);
        transaction.setEndDate(endDate);
        try{
            endDate=persianDateTime.getShamsi_Date_ForFax(endDate);
            startDate=persianDateTime.getShamsi_Date_ForFax(startDate);
        }catch (Exception e){

        }
        startSendFax();
    }

    private  void dateToDate() throws Exception {
      if (getDateISOK()){
          //Util.printMessage("date is ok",false);

          transaction.setStatementType("2");
          transaction.setTransactionCount("300");
          transaction.setStartDate(persianDateTime.getShamsi_Date_ForGateway(startDate));
          transaction.setEndDate(persianDateTime.getShamsi_Date_ForGateway(endDate));
          startDate=persianDateTime.getShamsi_Date_ForFax(persianDateTime.getShamsi_Date_ForGateway(startDate));
          endDate=persianDateTime.getShamsi_Date_ForFax(persianDateTime.getShamsi_Date_ForGateway(endDate));

          startSendFax();
      }
    }

    private  boolean getDateISOK() throws AgiException, IOException {
        int getDateCount=0;
        boolean getStartDateIsOK=false;
        boolean getEndDateIsOK=false;
        while ((!getStartDateIsOK) && (getDateCount<2)){
            startDate=call.getParentStart().Say.enterStartDate().trim();
             if (isNumber(startDate)){
             //    Util.printMessage("is number",false);
                 if (startDate.length()!=0) {
                   //  Util.printMessage("startDate len",false);
                     if (startDate.length()==6) {
                        // Util.printMessage("len is 6",false);
                        if (entranceDateIsOK(startDate)) {
                          //  Util.printMessage("entranceDateIsOK",false);
                            getStartDateIsOK=true;
                        //    startDate="13"+persianDateTime. startDate;
                        }
                     }
                 }
             }else {
                call.getParentStart().Say.dateNotValid();
                getDateCount++;
             }
        }
        if (getStartDateIsOK){
            getDateCount=0;
            while ((!getEndDateIsOK) && (getDateCount<2)){
                endDate=call.getParentStart().Say.enterEndDate().trim();
                //if  (isNumber(endDate)&&(endDate.length()!=0)&&(endDate.length()==6)&&(entranceDateIsOK(endDate))){
                  //  getEndDateIsOK=true;
                   // endDate="13"+endDate;
                //}
                if (isNumber(endDate)) {
                    Util.printMessage("is number",false);
                    if (endDate.length() != 0) {
                        Util.printMessage("endDate.length",false);
                        if (endDate.length() == 6) {
                            Util.printMessage("trueDate",false);
                            if (entranceDateIsOK(endDate)) {
                                Util.printMessage("entranceok",false);
                                getEndDateIsOK = true;
                                //endDate = "13" + endDate;
                            }
                        }
                    }
                }else {
                    call.getParentStart().Say.dateNotValid();
                    getDateCount++;
                }
            }
        }

        return getStartDateIsOK&&getEndDateIsOK;
    }
    private  boolean isNumber(String number){
        try{
            Long n=new Long(number);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private  boolean entranceDateIsOK(String date){

        int yy=0;int mm=0;int dd=0;
        boolean res=false;
        try{
         dd=Integer.valueOf(date.substring(0, 2));
         mm=Integer.valueOf(date.substring(2, 4));
         yy=Integer.valueOf(date.substring(4, 6));
         if (yy<77 && yy>persianDateTime.getYearOfNow()) return false;
         if (mm<1  && mm>persianDateTime.getMonthOfNow()) return false;
         if (dd<1  && mm>persianDateTime.getDayOfNow())   return false;
         return true;
        }catch (Exception e){
         return false;
        }
    }
    private  String   fixLenNumber(String no){
        try{
            return String.valueOf(Long.valueOf(no));
        }catch (Exception e){
            return "-";
        }
    }

    private  void optionalBycount() throws Exception {
        if (getMaxCountIsOK()){
            transaction.setStatementType("9");
            transaction.setTransactionCount(maxCount);
            startSendFax();
        }
    }
    private  boolean getMaxCountIsOK() throws AgiException, IOException {
        int getmaxCountC=0;
        boolean getmaxCountIsOK=false;
        while ((!getmaxCountIsOK) && (getmaxCountC<2)){
            maxCount=call.getParentStart().Say.enterMaxTrans().trim();
            if (isNumber(maxCount)&&(maxCount.length()!=0)&&(Integer.valueOf(maxCount)<=300)){
                getmaxCountIsOK=true;
            }else{
                call.getParentStart().Say.noNotValid();
                getmaxCountC++;
            }
        }
        return getmaxCountIsOK;
    }

    private  void optional() throws Exception {
        if (getAmountRangeISOK())
            if (getMaxCountIsOK()) {
                transaction.setStatementType("9");
                transaction.setTransactionCount(maxCount);
                transaction.setTransactionMaxAmount(maxAmount);
                transaction.setTransactionMinAmount(minAmount);
                startSendFax();
            }
    }
    private  boolean getAmountRangeISOK() throws AgiException, IOException {
        int getAmountRangeCount=0;
        boolean getAmountRangeMaxIsOK=false;
        boolean getAmountRangeMinIsOK=false;
        while ((!getAmountRangeMaxIsOK) && (getAmountRangeCount<2)){
            maxAmount=call.getParentStart().Say.enterMaxAmountReport().trim();
            if (isNumber(maxAmount)&&(maxAmount.length()!=0)){
                getAmountRangeMaxIsOK=true;
            }
            else {
                call.getParentStart().Say.dateNotValid();
                getAmountRangeCount++;
            }
        }

        if (getAmountRangeMaxIsOK){
           // Util.printMessage("MAX Amount is:"+maxAmount,false);
            getAmountRangeCount=0;
            while ((!getAmountRangeMinIsOK) && (getAmountRangeCount<2)){
                minAmount=call.getParentStart().Say.enterMinAmountReport().trim();
                if (isNumber(minAmount)&&(minAmount.length()!=0)){
                    getAmountRangeMinIsOK=true;
                }
                else {
                    call.getParentStart().Say.dateNotValid();
                    getAmountRangeCount++;
                }
            }
           // Util.printMessage("MIN Amount is:"+minAmount,false);
        }

        return getAmountRangeMinIsOK && getAmountRangeMaxIsOK;
    }

    private  void exit(){

    }

    public  String   getMainPathOfFaxFile(){

        String path=Util.FaxFile+getTodayDate();
        File file=new File(path);
        if (file.exists()) return path+"/";
        else {

            file.mkdir();
            return path+"/";
        }

    }
    public  String   getTodayDate(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =new SimpleDateFormat ("yyyy.MM.dd");
        String Now=DateFormat.format(Time);
        return Now;

    }
    public  String   getYesterdayDate(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =new SimpleDateFormat ("yyyy");
        SimpleDateFormat DateFormat1 =new SimpleDateFormat ("MM");
        SimpleDateFormat DateFormat2 =new SimpleDateFormat ("dd");
        String Now=DateFormat.format(Time);
        return Now;

    }
    private  void    startSendFax() throws Exception {
       // call.getParentStart().Say.pleaseWait();
        transaction.setCallUniqID(call.getCallUniqID());
        transaction=(Transaction)call.submitRequestToGateway(transaction);
       // transaction.setMAC(call.getMACAddress());
        String actionCode=transaction.getResultFromCM().getAction_code();
        if (actionCode.equals("0000")){

            //String FaxFile=CreatePDFFile(CreateJPGFile(createHTMLFaxFile()));
            String FaxFile=CreatePDFFile(createHTMLFaxFile());
         //  Util.printMessage("FaxFile Creted is :"+FaxFile,false);
            // String FaxFile=CreateTiffFile(convertJPGToPDF(CreateJPGFile(createHTMLFaxFile())));
            if (FaxFile!=null){
                //call.getParentStart().Say.startFax();
                boolean resultOfSendFax=SendFax(FaxFile);
                //ClearFootPrint(FaxFile);
            }
        }else{
            call.getParentStart().Say.errorCode(actionCode);
        }
    }
    public  String   createHTMLFaxFile() throws IOException {

        ArrayList<String> Temp = new ArrayList<String>();
        statementListMessage=transaction.getResultFromCM();
        int countOfTrans=Integer.valueOf(statementListMessage.getTransCount());
        statementMessage=new StatementMessage[countOfTrans];
        Writer out = null;
        for (int i=countOfTrans-1;i>=0;i--){
       //for (int i=0;i<countOfTrans;i++){

            statementMessage[i]=statementListMessage.getStatementMessage(i);
            if (is30Transaction){
                if (i==0) endDate=persianDateTime.getShamsi_Date_ForFax(statementMessage[i].getTransDate());
                if (i==countOfTrans-1) startDate=persianDateTime.getShamsi_Date_ForFax(statementMessage[i].getTransDate());
            }
        }
        String line;
        try {
            InputStream FileName = new FileInputStream(Util.FaxFile_Base + ".html");
            InputStreamReader InFile = new InputStreamReader(FileName, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(InFile);
            String T=null;
            File fileDir = null;

            try
            {

                 fileDir = new File(Util.FaxFile_Base+call.getCallerID()+"-"+call.getUniQID()+".html");
                 out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));

                 while ((line = br.readLine()) != null) {
                         if (line.contains("a1"))  {T=line.replace("a1","تاریخ");}
                    else if (line.contains("a2"))  {T=line.replace("a2","شماره حساب");}
                    else if (line.contains("a3"))  {T=line.replace("a3","شعبه");}
                    else if (line.contains("a4"))  {T=line.replace("a4","نام و نام خانوادگی");}
                    else if (line.contains("a5"))  {T=line.replace("a5","تا تاریخ");}
                    else if (line.contains("a6"))  {T=line.replace("a6","از تاریخ");}
                    else if (line.contains("a7"))  {T=line.replace("a7","مانده حساب");}
                    else if (line.contains("a8"))  {T=line.replace("a8","بدهکار");}
                    else if (line.contains("a9"))  {T=line.replace("a9","بستانکار");}
                    else if (line.contains("b1")) {T=line.replace("b1","شعبه عامل");}
                    else if (line.contains("b2")) {T=line.replace("b2","شرح سند");}
                    else if (line.contains("b3")) {T=line.replace("b3","شماره سند");}
                    else if (line.contains("b4")) {T=line.replace("b4","تاریخ سند");}
                    else if (line.contains("b5")) {T=line.replace("b5","ردیف");}
                    else if (line.contains("d1")) {T=line.replace("d1",persianDateTime.getShamsiDateForFax());}
                    else if (line.contains("acc")) {T=line.replace("acc",call.getAccount());}
                    else if (line.contains("c1")) {T=line.replace("c1",call.getBranch());}
                    else if (line.contains("c2")) {T=line.replace("c2",call.getNameAndFamily());}
                    else if (line.contains("c3")) {T=line.replace("c3",endDate);}
                    else if (line.contains("c4")) {T=line.replace("c4",startDate);}
                    else T=line;
                    out.append(T);
                }
               }catch (Exception e){
                Util.printMessage("e2:"+e.toString(),false);
                }
                int j=countOfTrans-1;
                String credit="";
                String debit="";
                String tblHTmlCmdPart1="<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;" ;
                String tblHTmlCmdPart2="</div></td>";
                int rowCount=1;
                while (j>=0)
                {
                    credit="";
                    debit="";
                    if (statementMessage[j].getCreditDebit().equals("C")) credit=statementMessage[j].getAmount();
                    else if (statementMessage[j].getCreditDebit().equals("D")) debit=statementMessage[j].getAmount();

                    out.append("<tr>");
                    out.append(tblHTmlCmdPart1+ fixLenNumber(statementMessage[j].getLastAmount()) + tblHTmlCmdPart2);
                    out.append(tblHTmlCmdPart1+ fixLenNumber(debit) +tblHTmlCmdPart2 );
                    out.append(tblHTmlCmdPart1+ fixLenNumber(credit) + tblHTmlCmdPart2);
                    out.append(tblHTmlCmdPart1+ statementMessage[j].getBranchName() + tblHTmlCmdPart2);
                    out.append(tblHTmlCmdPart1+ statementMessage[j].getShpInf() + tblHTmlCmdPart2);
                    out.append(tblHTmlCmdPart1+ statementMessage[j].getTransDocNo()+ tblHTmlCmdPart2);
                    out.append(tblHTmlCmdPart1+ persianDateTime.getShamsi_Date_ForFax(statementMessage[j].getTransDate()) +tblHTmlCmdPart2);
                    out.append(tblHTmlCmdPart1+ String.valueOf(rowCount) +tblHTmlCmdPart2);
                    out.append("<tr>"+"\n");
                    j--;
                    rowCount++;

                }

                out.append("<tr>");
                out.append(" <td class=\"style6\"><div align=\"right\" >  </div></td>");
                out.append("<td class=\"style6\"><div align=\"right\" >" + call.getBalance() + "</div></td>");
                out.append("<td class=\"style6\"><div align=\"right\" >" + "موجودی" + "</div></td>");
                out.append("<tr>"+"\n");


                out.append("</table>"+"\n");
                out.append("</td>"+"\n");
                out.append("</tr>"+"\n");
                out.append("</table>"+"\n");
                out.append("</font>"+"\n");
                out.append("</bodsy>"+"\n");
                out.append("</html>"+"\n");
                out.flush();
                out.close();


        } catch (FileNotFoundException e) {
            Util.printMessage("e3"+e.toString(),false);
        } catch (IOException e) {
            Util.printMessage("e4"+e.toString(),false);
        } catch (Exception e){
            Util.printMessage("e5"+e.toString(),false);
        }

        try
        {
          /* Util.printMessage(Util.FaxFile+call.getCallerID()+"-"+call.getUniQID()+".html",false);
            File fileDir = new File(Util.FaxFile+call.getCallerID()+"-"+call.getUniQID()+".html");
             out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
            int j=0;
            while (j<Temp.size()){
                out.append(Temp.get(j)).append("\r\n");
                j++;
            }
            out.flush();
            out.close();
            Util.printMessage("file creted...",false);*/

            return Util.FaxFile_Base+call.getCallerID()+"-"+call.getUniQID();
        }catch (Exception e){
            Util.printMessage("e1"+e.toString(),false);
            return null;
        }
    }

    public  String   CreateJPGFile_(String FaxFile) throws IOException {
        String faxFile_JPG=getMainPathOfFaxFile()+call.getCallerID()+"-"+call.getUniQID()+".JPG";
        String faxFile_HTML=FaxFile+".html";
        if (FaxFile!=null){
            String command = "wkhtmltoimage  "+faxFile_HTML+" "+faxFile_JPG;
            InputStreamReader isr =null;
            try
            {
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
            }catch (Exception e){
                Util.printMessage(command,false);
                Util.printMessage("errorrrrr"+e.toString(),false);
                return null;
            }
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null)
                if (line!="Done"){
                    return null;
                }
            return faxFile_JPG;
        }
        else return null;
    }
    public  String   CreatePDFFile_(String FaxFile) throws IOException {

        String faxFile_PDF=getMainPathOfFaxFile()+call.getCallerID()+"-"+call.getUniQID()+".pdf";
        String faxFile_JPG=CreateJPGFile(FaxFile);

        if (FaxFile!=null){
            //String command = "convert "+FaxFile+".jpg "+FaxFile+".pdf";

            //String command = "/usr/local/bin/wkhtmltopdf --page-size A4 --dpi 2000 "+faxFile_HTML+" "+faxFile_PDF;

            String command= "convert -density 300 -page A4 "+faxFile_JPG+" "+faxFile_PDF;
            // Util.printMessage(command,false);
            InputStreamReader isr =null;
            try
            {
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
            }catch (Exception e){
                Util.printMessage(command,false);
                Util.printMessage("errorrrrr"+e.toString(),false);
                return null;
            }

            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null)
                if (line!="Done"){
                    //return null;
                }


           //  clearFootPrint(faxFile_HTML);
            //  clearFootPrint(faxFile_HTML);
            return faxFile_PDF;
        }
        else return null;
    }

    public  String   CreateJPGFile(String FaxFile) throws IOException {
        String faxFile_JPG=getMainPathOfFaxFile()+call.getCallerID()+"-"+call.getUniQID()+".JPG";
        String faxFile_HTML=FaxFile+".html";
        if (FaxFile!=null){
            String command = "wkhtmltoimage  "+faxFile_HTML+" "+faxFile_JPG;
            InputStreamReader isr =null;
            try
            {
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
            }catch (Exception e){
                Util.printMessage(command,false);
                Util.printMessage("errorrrrr"+e.toString(),false);
                return null;
            }
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null)
                if (line!="Done"){
                    return null;
                }
            return faxFile_JPG;
        }
        else return null;
    }
    public  String   CreatePDFFile(String FaxFile) throws IOException {

        String faxFile_PDF=getMainPathOfFaxFile()+call.getCallerID()+"-"+call.getUniQID()+".pdf";
        String faxFile_HTML=FaxFile+".html";

        if (FaxFile!=null){

            String command = "/usr/local/bin/wkhtmltopdf --page-size A4 --dpi 2000 "+faxFile_HTML+" "+faxFile_PDF;

            InputStreamReader isr =null;
            try
            {
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
            }catch (Exception e){
                Util.printMessage(command,false);
                Util.printMessage("errorrrrr"+e.toString(),false);
                return null;
            }

            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null)
                if (line!="Done"){
                    return null;
                }


              clearFootPrint(faxFile_HTML);

            return faxFile_PDF;
        }
        else return null;
    }

    public  boolean  clearFootPrint(String faxfile){
        File file=new File(faxfile);
        return file.delete();

    }
    public  boolean  SendFax(String FaxFile) throws AgiException, InterruptedException, SQLException, IOException {

        exec("Park");
        Util.sendFax(FaxFile);

        return true;

    }





    @Override
    public void service(AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {

    }
}
