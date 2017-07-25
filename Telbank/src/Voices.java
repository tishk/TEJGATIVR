import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Set;

public class Voices extends BaseAgiScript {

    //you should copy the persian folder that contain Sound files and folder,into /var/lib/asterisk/sounds/en

    private String  CheckStar(String S){
        int Len=S.length();
        if (Len!=0){

            if ("*".equals(S.substring(Len-1))) S=S.substring(0,Len-1);
        }
        return S;
    }
    private String  RepairDigit(String S){
        String Temp="";
        for (int i=0;i<S.length();i++){
            Temp=Temp+S.charAt(i);
            i++;
        }
        return Temp;
    }
    public  void    PlayFile(String S) throws AgiException{
        streamFile(Util.VoicePath+"Pmsgs/"+S);

    }
    public  void    PlayConfFile(String S) throws AgiException, IOException {
        streamFile(S);
    }
    public  void    Beep() throws AgiException{
        streamFile(Util.VoicePath+"Pmsgs/BEEP");
    }
    public  boolean SayDate(String D) throws AgiException{
        D=D.trim();
        String Year="";
        String Month="";
        String Day="";
        if (D.length()==6){
             Year=D.substring(0,2);
             Month=D.substring(2,4);
             Day=D.substring(4,6);
        }
        else if (D.length()==8){
             Year=D.substring(2,4);
             Month=D.substring(4,6);
             Day=D.substring(6,8);
        }
        //  Util.printMessage("Year:"+Year,false);
        //  Util.printMessage("Month:"+Month,false);
        // Util.printMessage("Day:"+Day,false);
        char temp='!';
        temp=streamFile(Util.VoicePath+"DATE/A"+Day, "0123456789*#");
        if (temp!='!'){
            temp=streamFile(Util.VoicePath+"DATE/B"+Month, "0123456789*#");
            if (temp!='!'){

                streamFile(Util.VoicePath+"DATE/C"+Year, "0123456789*#");
            }
        }
        return false;
    }
    private  boolean isNumber(String number){
        try{
            BigInteger n=new BigInteger(number);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private int     ZeroResult;
    private boolean HaveZero(String SayNow){
        ZeroResult =0;
        if ("0".equals(SayNow.substring(2,3))) ZeroResult =1;
        if ("0".equals(SayNow.substring(1,2))) ZeroResult = ZeroResult +10;
        if ("0".equals(SayNow.substring(0,1))) ZeroResult = ZeroResult +100;
        if (ZeroResult !=0) return true;
        else return false;
    }

    public  boolean SayPersianDigitsSeparate(String Digit) throws AgiException{
        try{
            int len=Digit.length();
            //Util.printMessage("len is:"+String.valueOf(len),false);
            int LeftIndex=len %3;
            if (len>2){
                String SayNOW=Digit.substring(0,3);
                Digit=Digit.substring(3);
                //Util.printMessage("Say now:"+SayNOW,false);
                //Util.printMessage("Say later:"+Digit,false);
                Say3DigitSeparate(SayNOW);
                SayPersianDigitsSeparate(Digit);
            }else{
                if (len==0) return true;
                else if (len>=1){
                    //Util.printMessage("in here more then 1:"+Digit,false);
                    Say3DigitSeparate(Digit);
                    return true;
                }

            }
        }catch(NumberFormatException ex){
            return true;
        }
        return false;
    }
    public  boolean SayMobileNo(String Digit) throws AgiException{
        try{
            int len=Digit.length();
            if (Digit.length()==11){
               Digit=Digit.substring(1);
                Say3DigitSeparate("0");
                SayPersianDigitsSeparate(Digit);
            }else{
                int LeftIndex=len %3;
                if (len>2){
                    String SayNOW=Digit.substring(0,3);
                    Digit=Digit.substring(3);
                    Say3DigitSeparate(SayNOW);
                    SayPersianDigitsSeparate(Digit);
                }else{
                    if (len==0) return true;
                    else if (len>=1){
                        Say3DigitSeparate(Digit);
                        return true;
                    }

                }

            }
        }catch(NumberFormatException ex){
            return true;
        }

        return false;
    }
    private boolean Say3DigitSeparate(String SayNow) throws AgiException {
        boolean IsZero=false;
        //   Util.printMessage("Say 3 digit:"+SayNow,false);

        if ((SayNow.length()==1)&& Integer.valueOf(SayNow)!=0){
            streamFile(Util.VoicePath+"NUM/"+SayNow);
            return true;
        }
        if (SayNow.length()==1) {
            IsZero=true;
            SayNow="00"+SayNow;
        }
        if (SayNow.length()==2){
           try
           {

               if (Integer.valueOf(SayNow)<10){
                   streamFile(Util.VoicePath+"NUM/0");
                   streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2));
               }
               else if ((Integer.valueOf(SayNow)>=10) &&(Integer.valueOf(SayNow)<=20)){

                   streamFile(Util.VoicePath+"NUM/"+SayNow);
               }
               else{

                   streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"0o");
                   streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2));
               }
           }catch (Exception e){

           }

        }else if (HaveZero(SayNow)){
            //   Util.printMessage("Say 3 digit:"+SayNow+" have zero result is:"+String.valueOf(ZeroResult),false);
            if (ZeroResult ==1){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==10){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }else if (ZeroResult ==11){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00");
            }else if (ZeroResult ==100){
                if (Integer.valueOf(SayNow)<20 ){
                    //streamFile(Util.VoicePath+"NUM/0");
                    //Util.printMessage("in say less than 20:",false);
                    streamFile(Util.VoicePath+"NUM/0");
                    //streamFile(Util.VoicePath+"NUM/0");
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
               // }else{
                   // streamFile(Util.VoicePath+"NUM/0");
                   // streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                  //  streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
                }else{
                    streamFile(Util.VoicePath+"NUM/0");
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
                }

            }else if (ZeroResult ==101){
                streamFile(Util.VoicePath+"NUM/0");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==110){
                streamFile(Util.VoicePath+"NUM/0");
                streamFile(Util.VoicePath+"NUM/0");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }else if (ZeroResult ==111){
                if (IsZero){
                    streamFile(Util.VoicePath+"NUM/0");
                }else{
                    streamFile(Util.VoicePath+"NUM/0");
                    streamFile(Util.VoicePath+"NUM/0");
                    streamFile(Util.VoicePath+"NUM/0");
                }

            }
        }else{
            if (Integer.parseInt(SayNow.substring(1,3))<20 ){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,3));
            }else{
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }


        }
        return true;

    }
    public  boolean SayPersianDigit(String Digit) throws AgiException{
        try{
            Util.printMessage("in say persina digit",false);
            if (Long.valueOf(Digit) ==0) {

                PlayFile(Util.VoicePath+"NUM/"+Digit);
                return true;
            }
            int len=Digit.length();
            int LeftIndex=len %3;
            if ((LeftIndex==0)&&(len>=3)) LeftIndex=3;
            if (len>2){
                String SayNOW=Digit.substring(0,LeftIndex);
                Say3Digits(SayNOW);
                String DigitTemp=Digit.substring(LeftIndex);
                if (Integer.valueOf(SayNOW)!=0){
                    if (0==Long.valueOf(DigitTemp)){

                        PlayConfFile(SetConnectionFile(Digit.length(), 0));
                    }else{
                        PlayConfFile(SetConnectionFile(Digit.length(), 1));
                    }
                }
                Digit=Digit.substring(LeftIndex);
                SayPersianDigit(Digit);

            }else{
                if (len==0) return true;
                else if (len>=1){
                    Say3Digits(Digit);
                    return true;
                }

            }
        }catch(Exception ex){

        }
        return false;
    }
    private boolean Say3Digits(String SayNow) throws AgiException{
        if (Integer.valueOf(SayNow)==0) return true;
        if (SayNow.length()==1) SayNow="00"+SayNow;
        if (SayNow.length()==2) SayNow="0"+SayNow;
        if (HaveZero(SayNow)){

            if (ZeroResult ==1){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==10){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }else if (ZeroResult ==11){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00");
            }else if (ZeroResult ==100){
                //Util.printMessage("in say less than 20:" + SayNow.substring(1, 3), false);
                if (Integer.parseInt(SayNow)<=20){
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,3));
                    //Util.printMessage("in say less than 20:"+SayNow.substring(1,3),false);
                }else{
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
                }
            }else if (ZeroResult ==101){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==110){
                //Util.printMessage("110:"+SayNow.substring(1,3),false);
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }else if (ZeroResult ==111){

            }
        }else{
            if (Integer.parseInt(SayNow.substring(1,3))<=20 ){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,3));
            }else{
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }


        }
        return true;
    }

    private String  SetConnectionFile(int Len,int Kind){
        String Part1=Util.VoicePath+"NUM/";
        if (Kind==0){
            switch (Len){
                case 1:return "";
                case 2:return "";
                case 3:return "";
                case 4:return Part1+"1000";
                case 5:return Part1+"1000";
                case 6:return Part1+"1000";
                case 7:return Part1+"1000000";
                case 8:return Part1+"1000000";
                case 9:return Part1+"1000000";
                case 10:return Part1+"1000000000";
                case 11:return Part1+"1000000000";
                case 12:return Part1+"1000000000";
                case 13:return Part1+"1000000000000";
                case 14:return Part1+"1000000000000";
                case 15:return Part1+"1000000000000";
                default:return "";
            }
        }else{
            switch (Len){
                case 1:return "";
                case 2:return "";
                case 3:return "";
                case 4:return Part1+"1000o";
                case 5:return Part1+"1000o";
                case 6:return Part1+"1000o";
                case 7:return Part1+"1000000o";
                case 8:return Part1+"1000000o";
                case 9:return Part1+"1000000o";
                case 10:return Part1+"1000000000o";
                case 11:return Part1+"1000000000o";
                case 12:return Part1+"1000000000o";
                case 13:return Part1+"1000000000000o";
                case 14:return Part1+"1000000000000o";
                case 15:return Part1+"1000000000000o";
                default:return "";
            }

        }
    }
    public  String  GetStringFromAsterisk(String File,int TimeOut) throws AgiException{
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while ((digit = waitForDigit(TimeOut)) != 0)
            {

                if (digit == '#' || digit == '*')
                {
                    break;
                }
                Result=Result+String.valueOf(digit);
            }
        }

        return  Result;
    }
    public  String  GetStringFormASK(String File,int timeOUT) throws AgiException, IOException {
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");


        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+Integer.toString(timeOUT), "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0)
                {
                    break;
                }else Result=Result+String.valueOf(digit);

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  GetStringFormASKWithLen(String File,int timeOUT,int Len) throws AgiException{
        char digit= '@';
        int counter=1;
        String Result="";
        digit=streamFile(File, "0123456789*#");
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#' )
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+Integer.toString(timeOUT), "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0||counter<Len)
                {
                    break;
                }else{
                    Result=Result+String.valueOf(digit);
                    counter++;
                }

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  GetStringFormASKSilent(int timeOUT) throws AgiException{
        char digit= '@';
        String Result="";
        digit=streamFile("", "0123456789*#");
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+Integer.toString(timeOUT), "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0)
                {
                    break;
                }else Result=Result+String.valueOf(digit);

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  GetStringFormASKTinyTimeOut_(String File,String timeOUT) throws AgiException, IOException {
        String Result="";
        Result=String.valueOf(streamFile(File, "0123456789*#"));
        Util.printMessage(Result,false);
        Result=Result.trim();
        return  Result;
    }
    public  String  GetStringFormASKTinyTimeOut(String File,String timeOUT) throws AgiException, IOException {
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");
       // Util.printMessage("Stream file is :"+File,false);
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+timeOUT, "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0)
                {
                    break;
                }else Result=Result+String.valueOf(digit);

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  GetCharacterFormASKTinyTimeOut(String File,String timeOUT) throws AgiException{
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+timeOUT, "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0)
                {
                    break;
                }else Result=Result+String.valueOf(digit);

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  GetStringFormASKseperateMenu(String File,int timeOUT) throws AgiException{
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");
        Result=Result+String.valueOf(digit);
        Result=Result.trim();
        return  Result;
    }
    public  String  ConvertEntranceToDigit(String e){
        int v=-1;
        try {
            v=Integer.valueOf(e);
        }catch (Exception var1){
            v=-1;
        }
        return String.valueOf(v);
    }
   //--------------------------------------------------
    public  String  sayMenu(Set MenuSet, String PrefixMessages) throws AgiException, IOException {
        //Util.printMessage("in say for bill",false);

        String element=null;
        String element2=null;
        String LastElement=null;
        String Choice="-1";
        Set tempSet=MenuSet;
        String tempElement=null;
        boolean IsInMenu=false;
        for(Object object : MenuSet)
            LastElement = (String) object;
        Choice=GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/" + "P006", "0.25");
        Choice=ConvertEntranceToDigit(Choice);
        if (Choice.equals("-1")){
            for(Object object : MenuSet) {

                element = (String) object;
               // Util.printMessage(Util.VoicePath +"Pmsgs/"  + PrefixMessages + element,false);
                if (element.equals(LastElement))
                {
                    Choice=GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + PrefixMessages + element, "3");
                    Choice=ConvertEntranceToDigit(Choice);
                }
                else Choice=GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + PrefixMessages + element, "0.25");
                Choice=ConvertEntranceToDigit(Choice);
                if (!Choice.equals("-1")){
                    for(Object object2 : tempSet) {
                        tempElement = (String) object2;
                        if (Choice.equals(tempElement)) return Choice;
                    }
                    break;
                }
            }

        }else{
            for(Object object3 : MenuSet) {
                element2 = (String) object3;
                if (Choice.equals(element2)) return Choice;
            }
        }
        return "-1";

    }
    public  String  sayBalance(String balance,String currency) throws IOException, AgiException {
        String res=mojodieHesabeShoma();
        if (res.equals("")){
            SayPersianDigit(balance);
            res=currency(currency);
            if (res.equals("")){
                res=mibashad();
            }
        }
        return res;
    }

    //---------------Tejarat---------------------------------
    public  String  BenameKhoda() throws AgiException, IOException {
        return GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + "P035" , "0.25");

    }
    public  String  playHint() throws AgiException {
        String hint="";
        try{
            hint=GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + "PIVR" , "0.25");
        }catch (Exception e){}
        return hint;
    }
    public  String  vaghtBekheyrAndEnter(int Vaght) throws AgiException, IOException {

        switch (Vaght){
         case 0:return sobh_Bekheyr();
         case 1:return zohr_Bekheyr();
         case 2:return badazohr_Bekheyr();
         case 3:return shab_Bekheyr();
         default:return "";
     }
    }
    private String  sobh_Bekheyr() throws AgiException, IOException {
       //return GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + "sob" , "0.25").trim();
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "sob" , 5).trim();
    }
    private String  zohr_Bekheyr() throws AgiException, IOException {
       // return GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + "zohr" , "0.25").trim();
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "zohr" , 5).trim();
    }
    private String  badazohr_Bekheyr() throws AgiException, IOException {
       // return  GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + "badazohr" , "0.25").trim();
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "badazohr" , 5).trim();
    }
    private String  shab_Bekheyr() throws AgiException, IOException {
       // return GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + "shab" , "0.25").trim();
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "shab" , 5).trim();
    }
    public  String  hi() throws AgiException, IOException {
        return GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P001", "0.25").trim();
    }
    public  String  advertizment() throws AgiException {
        String hint="";
        try{
            hint=GetStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + "sh170" , "0.25");
        }catch (Exception e){}
        return hint;
    }
    public  String  enterAccountNumber() throws AgiException, IOException {

        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "sh107t" , 5).trim();
    }
    public  String  enterPinForAccount() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "002T" , 5).trim();
    }
    public  void    pinNotOK() throws AgiException {
       PlayFile("E003");
    }
    public  String  enterPinForPan() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "009T" , 5).trim();
    }
    public  String  accountNotValidreenter() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath + "Pmsgs/" + "notvalidreenter", 5).trim();
    }
    public  void    accountNotValid() throws AgiException, IOException {
         PlayFile("E002");
    }
    public  void    numberNotValid() throws AgiException, IOException {
        PlayFile("Sh2");
    }
    public  String  noNotValid() throws AgiException, IOException {
        return GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh2", "0.25").trim();
    }
    public  void    namafhomAst() throws AgiException {
        PlayFile("E006");
    }
    public  void    by() throws AgiException {
        PlayFile("P020");
        hangup();
    }
    public  void    goBranch() throws AgiException {
        PlayFile("sh19");
    }
    public  void    baArzePozesh() throws AgiException {
        PlayFile("E011");
    }
    public  void    codeMelliNadarad() throws AgiException {
        PlayFile("sh1940");
    }
    public  void    errorCode(String Code) throws AgiException {
        PlayFile(Code);
    }
    public  boolean gardesheHesaBeShoma() throws AgiException, IOException {

       if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P008", "0.25").trim().equals("")) return true;
       else return false;

    }
    public  String mojodieHesabeShoma() throws AgiException, IOException {
        return GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P003", "0.25").trim();
    }
    public  boolean varize() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P010", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean bardashte() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P009", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean mablaghe() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P011", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  String currency(String currency ) throws AgiException, IOException {
        try{currency=String.valueOf(Integer.valueOf(currency));}catch (Exception e){currency="0";}
        return  GetStringFormASKTinyTimeOut(Util.VoicePath + "PCUR/" + currency, "0.25").trim();
    }
    public  boolean beTarikhe() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P014", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean teyeSanadeSHomare() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P013", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean babate() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P038", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean sanadType(String sanad) throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "psanad/" + sanad, "0.25").trim().equals("")) return true;
        else return false;

    }
    public  String  mibashad() throws AgiException, IOException {

         return  GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P017", "0.25").trim();

    }
    public  boolean gardesheDigariNist() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E004", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean gardeshiMojodNist() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E014", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean ertebatBaServerNist() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E011", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  String  enterNewPin() throws AgiException, IOException {

        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "P023T" , 5).trim();

    }
    public  String  enterNewPinReEnter() throws AgiException, IOException {

        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "P024T" , 5).trim();

    }
    public  boolean pinInvalidEntry() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E007", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean pinChanged() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P022", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean pinNotChanged() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P021", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean pinMotabeghatNadarad() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E009", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  String  enterNewMob() throws AgiException, IOException {

        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sms01T" , 5).trim();

    }
    public  boolean activatedSMSService() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sms02", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean inActivatedSMSService() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sms03", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean notActivatedSMSService() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "ESms02", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean reEnterMob() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "ESms01", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean mobNotOK() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "ESms03", "0.25").trim().equals("")) return true;
        else return false;

    }

    public  boolean shomareMobVaredShode() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "ConfirmMobile", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean shomareVaredShode() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "enteredNumber", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  String  ifMobCorrect5() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "ConfirmMobile2T" , 2).trim();
    }
    public  boolean shebaUseFor() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh501", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean shebaieShoma() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh502", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean shebaIR() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh503", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean shomareCateMotasel() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "MsgCart", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  String  enterPin2() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "SH20T" , 5).trim();
    }
    public  String  enterDestinationAccount() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh10t" , 5).trim();
    }
    public  void    FundTransferError(String errorCode) throws AgiException {
        PlayFile("SH"+errorCode);
    }
    public  boolean transferOK() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh61", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean shomarePeygireiShma() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH21", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean bardashVaVarizKhahadShode() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH64", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean mablaghBardashMishavad() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh64", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean enteghalAnjamKhahadShod() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh63", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean sorryDoNotPay() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH33", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean paySuccess() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH32", "0.25").trim().equals("")) return true;
        else return false;

    }public  boolean payInProgress() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh34", "0.25").trim().equals("")) return true;
        else return false;

    }

    public  boolean saghfPorAst() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH35", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean mojodiKafiNist() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh71", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean noeHesabBaNoeDarkhastYeksanNist() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh207", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean accountNotRegistered() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH23", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean farmaniDaryaftNashod() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh13", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  String  lotfanMablaghRaVarehNamaeid() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh11t" , 5).trim();
    }
    public  String   rial() throws AgiException, IOException {
        return GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P027", "0.25").trim();
    }
    public  void    bardashVaBeHesabe() throws AgiException {
        PlayFile("SH44");
    }
    public  void    varizKhahadShod() throws AgiException {
        PlayFile("SH45");
    }
    public  String  ifCorrectPress5() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh46T" , 3).trim();
    }
    public  String  enterTelNumber() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "GetTelNumberT" , 5).trim();
    }
    public  void    shomareTelephoneVaredShode() throws AgiException {
        PlayFile("ConfirmTelNumber");
    }
    public  void    rialBabateGhabze() throws AgiException {
        PlayFile("Sh37");
    }
    public  void    telephone() throws AgiException {
        PlayFile("Sh41");
    }
    public  void    bardashtKhahadshod() throws AgiException {
        PlayFile("Sh43");
    }
    public  boolean balanceNotEnaugh() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh105", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean shenaseMotabarNist() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh56", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean billPayedEarly() throws AgiException, IOException {
        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh55", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  String  enterBillID() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh1t" , 5).trim();
    }
    public  String  enterPaymentID() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh3t" , 5).trim();
    }
    public  void    sayBillKind(int id) throws AgiException {

        switch (id){
            case 1: PlayFile("Sh38");break;
            case 2: PlayFile("Sh39");break;
            case 3: PlayFile("Sh40");break;
            case 4: PlayFile("Sh41");break;
            case 5: PlayFile("Sh42");break;
            case 6: PlayFile("Sh57");break;
            case 7: PlayFile("Sh57");break;
            case 8: PlayFile("Sh58");break;
            case 9: PlayFile("Sh59");break;
            default:
                break;
        }
    }
    public  boolean vaShenaseGhabze() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh67", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  String  enterAccountOfinstallmentPayment() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "sh51t" , 5).trim();
    }

    public  String  enterFollowCode() throws AgiException, IOException {

            return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "SH31T" , 5).trim();

    }
    public  void    sayFollowStatus(int id) throws AgiException, IOException {

        switch (id){
            case 0: PlayFile("SH33");break;
            case 1: PlayFile("SH32");break;
            case 2: PlayFile("SH34");break;
            case 3: PlayFile("SH64");break;
            case 4: PlayFile("SH58");break;
            case 5: PlayFile("SH59");break;
            default:
                break;
        }
    }
    public  String  enterIdentCode() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "CSH01T" , 5).trim();
    }
    public  String  enterSerialOfChequq() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh209T" , 5).trim();
    }
    public  void    sayChequeStatus(int status) throws AgiException {

        switch (status){

            case    0: PlayFile("Sh200");break;
            case    1: PlayFile("Sh201");break;
            case    2: PlayFile("Sh202");break;
            case    3: PlayFile("Sh203");break;
            case    4: PlayFile("Sh210");break;
            case    5: PlayFile("Sh204");break;
            case 3007: PlayFile("Sh200");break;
            case 1014: PlayFile("Sh207");break;
            case 1912: PlayFile("Sh207");break;
            case 1817: PlayFile("Sh208");break;
            case 9126: PlayFile("E011") ;break;
            case 9000: PlayFile("E002") ;break;

            default: PlayFile(String.valueOf(status)) ;break;
        }
    }
    public  String  inactive_TB_MB_IB_AreYouSure() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "AccountBlock2" , 3).trim();
    }
    public  String  inactive_All_AreYouSure() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath +"Pmsgs/"  + "AccountBlock" , 3).trim();
    }
    public  boolean accountHasBeenblock() throws AgiException, IOException {

        GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "AccBlock", "0.25");
        return true;
    }
    public  String  enterStartDate() throws AgiException, IOException {
       // return GetStringFormASKWithLen(Util.VoicePath + "Pmsgs/" + "P032", 3, 6).trim();

            return GetStringFormASK(Util.VoicePath + "Pmsgs/" + "P032", 3).trim();

    }
    public  String  enterEndDate() throws AgiException, IOException {
        //return GetStringFormASKWithLen(Util.VoicePath + "Pmsgs/" + "P033", 3, 6).trim();

            return GetStringFormASK(Util.VoicePath + "Pmsgs/" + "P033", 3).trim();

    }
    public  boolean dateNotValid() throws AgiException, IOException {

        GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E016", "0.25");
        return true;

    }
    public  boolean startFax() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P026", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  String  enterMaxTrans() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath + "Pmsgs/" + "CountFaxT", 3).trim();
    }
    public  String  enterMaxAmountReport() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath + "Pmsgs/" + "F1T", 3).trim();
    }
    public  String  enterMinAmountReport() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath + "Pmsgs/" + "F2T", 3).trim();
    }
    public  boolean pleaseWait() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P019", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  String  enterPanPass() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath + "Pmsgs/" + "009T", 5).trim();
    }
    public  String  cardBlockedIfAreYouSurePress5() throws AgiException, IOException {
        return GetStringFormASK(Util.VoicePath + "Pmsgs/" + "sh111", 3).trim();
    }
    public  void    panBlocked() throws AgiException, IOException {

        GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh112", "0.25").trim().equals("");

    }
    public  boolean accountOfPanIs() throws AgiException, IOException {

        if (!GetStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "MagAccount", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  void    sayAuthenticationResponse(String actionCode) throws AgiException {
        int intActionCode=-1;
        try{intActionCode=Integer.valueOf(actionCode);}catch (Exception e){intActionCode=-1;}
        switch (intActionCode){
            case   -1:  baArzePozesh();
                break;
            case    0:
                break;
            case 9902: pinNotOK();
                break;
            case 9904: errorCode("9126");//time out
                break;
            case 9300: goBranch();
                break;
            case 9999: baArzePozesh();
                break;
            case 7777: baArzePozesh();
                break;
            default: errorCode(actionCode);
                break;
        }
    }
    public  void    sayChangePinResponse(String actionCode) throws AgiException, IOException {
        int intActionCode=-1;
        try{intActionCode=Integer.valueOf(actionCode);}catch (Exception e){intActionCode=-1;}
        switch (intActionCode){
            case   -1:  baArzePozesh();
                break;
            case    0:  pinChanged();
                break;
            case 9001: accountNotValid();
                break;
            case 9010: pinNotChanged();
                break;
            case 1912: noeHesabBaNoeDarkhastYeksanNist();
                break;
            case 9999: baArzePozesh();
                break;
            case 7777: baArzePozesh();
                break;
            default: errorCode(actionCode);
                break;
        }
    }

    //MagAccount

    //---------------Tejarat---------------------------------
    @Override
    public  void    service(AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {

    }
}
