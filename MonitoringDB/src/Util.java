import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by root on 5/7/16.
 */
public class Util {
    static public class strUtils {

        public  String   getDecodedTime(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =

                    new SimpleDateFormat ("HHmmss");
            String Now=DateFormat.format(Time);
            return Now;

        }
        public  String   getDecodedDate() {

            Calendar cal = Calendar.getInstance();
            int Day = cal.get(Calendar.DAY_OF_MONTH);
            int Month = cal.get(Calendar.MONTH)+1;
            int Year = cal.get(Calendar.YEAR);

            if(Month < 3 && Day < 21)
                Year -= 622;
            else
                Year -= 621;

            switch (Month) {
                case 1:
                    if(Day < 21)
                    {
                        Month=10;
                        Day+=10;
                    }
                    else
                    {
                        Month=11;
                        Day-=20;
                    }
                    break;

                case 2:
                    if(Day < 20)
                    {
                        Month=11;
                        Day+=11;
                    }
                    else
                    {
                        Month=12;
                        Day-=19;
                    }
                    break;

                case 3:
                    if(Day < 21)
                    {
                        Month=12;
                        Day+=9;
                    }
                    else
                    {
                        Month=1;
                        Day-=20;
                    }
                    break;

                case 4:
                    if(Day < 21)
                    {
                        Month=1;
                        Day+=11;
                    }
                    else
                    {
                        Month=2;
                        Day-=20;
                    }
                    break;

                case 5:
                case 6:
                    if(Day < 22)
                    {
                        Month-=3;
                        Day+=10;
                    }
                    else
                    {
                        Month-=2;
                        Day-=21;
                    }
                    break;

                case 7:
                case 8:
                case 9:
                    if(Day < 23)
                    {
                        Month-=3;
                        Day+=9;
                    }
                    else
                    {
                        Month-=2;
                        Day-=22;
                    }
                    break;

                case 10 :
                    if(Day < 23)
                    {
                        Month=7;
                        Day+=8;
                    }
                    else
                    {
                        Month=8;
                        Day-=22;
                    }
                    break;

                case 11:
                case 12:
                    if(Day < 22)
                    {
                        Month-=3;
                        Day+=9;
                    }
                    else
                    {
                        Month-=2;
                        Day-=21;
                    }
                    break;
            }
            String mm=null;
            String dd=null;
            if (Month<10) mm="0"+String.valueOf(Month); else mm=String.valueOf(Month);
            if (Day<10) dd="0"+String.valueOf(Day); else dd=String.valueOf(Day);
            return String.valueOf(Year)+ mm+dd;

        }
        public  String   getFixedDateToFax() {

            Calendar cal = Calendar.getInstance();
            int Day = cal.get(Calendar.DAY_OF_MONTH);
            int Month = cal.get(Calendar.MONTH)+1;
            int Year = cal.get(Calendar.YEAR);

            if(Month < 3 && Day < 21)
                Year -= 622;
            else
                Year -= 621;

            switch (Month) {
                case 1:
                    if(Day < 21){Month=10;Day+=10;}
                    else{Month=11;Day-=20;}
                    break;
                case 2:
                    if(Day < 20)
                    {
                        Month=11;
                        Day+=11;
                    }
                    else
                    {
                        Month=12;
                        Day-=19;
                    }
                    break;

                case 3:
                    if(Day < 21)
                    {
                        Month=12;
                        Day+=9;
                    }
                    else
                    {
                        Month=1;
                        Day-=20;
                    }
                    break;

                case 4:
                    if(Day < 21)
                    {
                        Month=1;
                        Day+=11;
                    }
                    else
                    {
                        Month=2;
                        Day-=20;
                    }
                    break;

                case 5:
                case 6:
                    if(Day < 22)
                    {
                        Month-=3;
                        Day+=10;
                    }
                    else
                    {
                        Month-=2;
                        Day-=21;
                    }
                    break;

                case 7:
                case 8:
                case 9:
                    if(Day < 23)
                    {
                        Month-=3;
                        Day+=9;
                    }
                    else
                    {
                        Month-=2;
                        Day-=22;
                    }
                    break;

                case 10 :
                    if(Day < 23)
                    {
                        Month=7;
                        Day+=8;
                    }
                    else
                    {
                        Month=8;
                        Day-=22;
                    }
                    break;

                case 11:
                case 12:
                    if(Day < 22)
                    {
                        Month-=3;
                        Day+=9;
                    }
                    else
                    {
                        Month-=2;
                        Day-=21;
                    }
                    break;
            }
            String mm=null;
            String dd=null;
            if (Month<10) mm="0"+String.valueOf(Month); else mm=String.valueOf(Month);
            if (Day<10) dd="0"+String.valueOf(Day); else dd=String.valueOf(Day);
            return String.valueOf(Year)+"/"+ mm+"/"+dd;

        }
        public  String   getFixedTimeToFax() {

            Date Time=new Date();
            SimpleDateFormat DateFormat =

                    new SimpleDateFormat ("HH:mm:ss");
            String Now=DateFormat.format(Time);
            return Now;
        }
        public  String   createZeroString(int Count){
            String res="";
            for (int i=0;i<Count;i++) res=res+"0";
            return res;
        }
        public  String   fixLengthWithZero(String Str,int length){
            String Temp=Str;
            for (int i=0;i<length-Str.length();i++) Temp="0"+Temp;
            return Temp;
        }
        public  String   fixLengthWithStar(String Str,int length){
            String Temp=Str;
            for (int i=0;i<length-Str.length();i++) Temp="*"+Temp;
            return Temp;
        }
        public  String   midString(String str,int start,int count){
            try{
                return str.substring(start-1,count+start-1);
            }catch (Exception e){
                return "!";
            }

        }
        public  String   DecToBin32(long n){

            char ST[]=new char[32];
            byte i;
            for ( i=0;i<32;i++) ST[i]='0';
            i=31;
            while (n!=0){

                ST[i]=(char)(n % 2 +(int)('0'));
                n=n / 2;
                i--;
            }
            return String.valueOf(ST);

        }
        public  long     HexToDec(String s) {

            long p=0;
            int  c,i;
            for(i=0;i<s.length();i++){
                switch (s.charAt(i)) {
                    case '0':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '1':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '2':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '3':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '4':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '5':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '6':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '7':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '8':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case '9':c=(int)s.charAt(i)-(int)'0';
                        break;
                    case 'a':c=(int)s.charAt(i)-(int)'a'+10;
                        break;
                    case 'b':c=(int)s.charAt(i)-(int)'a'+10;
                        break;
                    case 'c':c=(int)s.charAt(i)-(int)'a'+10;
                        break;
                    case 'd':c=(int)s.charAt(i)-(int)'a'+10;
                        break;
                    case 'e':c=(int)s.charAt(i)-(int)'a'+10;
                        break;
                    case 'f':c=(int)s.charAt(i)-(int)'a'+10;
                        break;
                    case 'A':c=(int)s.charAt(i)-(int)'A'+10;
                        break;
                    case 'B':c=(int)s.charAt(i)-(int)'A'+10;
                        break;
                    case 'C':c=(int)s.charAt(i)-(int)'A'+10;
                        break;
                    case 'D':c=(int)s.charAt(i)-(int)'A'+10;
                        break;
                    case 'E':c=(int)s.charAt(i)-(int)'A'+10;
                        break;
                    case 'F':c=(int)s.charAt(i)-(int)'A'+10;
                        break;
                    default:return -1;
                }
                p=p*16+c;
            }
            return p;
        }
        public  String   rightString(String str,int index){
            try{
                return str.substring(str.length()-index,str.length());
            }catch (Exception e){
                return "!";
            }

        }
        public  String   leftString(String str,int index){
            try{
                return str.substring(0,index);
            }catch (Exception e){
                return "!";
            }

        }
        public  String   deleteFromString(String str,int start,int count){

            try{

                String before=str.substring(0,start-1);
                String after=str.substring(start+count-1,str.length());
                String Result=before+after;
                return Result;
            }catch (Exception e){
                return "!";
            }

        }
        public  String   Tokenize(String str ,char id){
            int len=0;
            String Result=null;
            try{
                if (id=='1'){
                    len= Integer.valueOf(midString(str,1,3));
                    str=deleteFromString(str,1,3);
                    Result=midString(str,1,len);
                    str=deleteFromString(str,1,len);

                }else Result="";
                return Result;

            }catch (Exception e){
                return "";
            }
        }
        public  String   SplitAndCutString(String str,char SplitParameter,int position){
            int positionCount=1;
            int perposition=0;
            try{
                for (int i=0;i<=str.length()-1;i++){
                    if (str.charAt(i)==SplitParameter) {
                        if (position==positionCount){
                            return str.substring(perposition,i);
                        }
                        else{
                            positionCount++;
                            perposition=i+1;
                        }
                    }
                }
            }catch (Exception e){

            }
            return null;
        }
        public  String   insertSeparator(String str){
            String Temp="";
            try{
                int len=str.length();
                while (len>0){
                    Temp=","+right3DigitString(str)+Temp;
                    str=deleteRight3DigitFromString(str);
                    len=str.length();
                }
            }catch (Exception e){
                return "!";
            }


            return Temp.substring(1);
        }
        public  String   right3DigitString(String str){
            try{
                if (str.length()<3) return str;
                else return str.substring(str.length()-3,str.length());
            }catch (Exception e){
                return "!";
            }

        }
        public  String   deleteRight3DigitFromString(String str){

            try{
                if (str.length()<3) return "";
                else {
                    return str.substring(0, str.length() - 3);
                }
            }catch (Exception e){
                return "!";
            }

        }

        public  String[]     extractText(String aText,String aSeparator){
            String[] Result= aText.split(aSeparator);
            return Result;

        }
        public List<String> extractChequeData(String aText, String identifier){

            List<String> result=new ArrayList<String>();
            if (aText.trim().equals("")) return result;
            String s="";
            String param="";
            try {
                aText=deleteFromString(aText,1,16);
                int sizeOfIdentifier=identifier.length();
                for(int i=0;i<sizeOfIdentifier;i++){
                    if (identifier.charAt(i)=='1'){
                        int lenOfParam=Integer.valueOf(midString(aText,1,3));
                        aText=deleteFromString(aText,1,3);
                        param=midString(aText,1,lenOfParam);
                        aText=deleteFromString(aText,1,lenOfParam);
                    }else param="";
                    result.add(param);
                    param="";
                }

            }catch (Exception var1){

            }
            return result;
        }
        public  String[]     extractTextPos(String aText,int[] aPos){

            List<String> result=new ArrayList<>();
            try{
                if ((aText.trim()=="")||(aPos.length==0)){
                    result.add(aText);
                    return  result.toArray(new String[result.size()]);
                }
                int s=1;
                String tempString="";
                for (int i=0;i<aPos.length;i++){
                    tempString=midString(aText,s,aPos[i]);
                    result.add(tempString);
                    aText=deleteFromString(aText,1,aPos[i]);
                }
                return result.toArray(new String[result.size()]);
            }catch (Exception e){

            }
            return result.toArray(new String[result.size()]);
        }

    }
    public static strUtils strutils=new strUtils();

    public static class PersianDateTime {
        private int irYear; // Year part of a Iranian date
        private int irMonth; // Month part of a Iranian date
        private int irDay; // Day part of a Iranian date
        private int gYear; // Year part of a Gregorian date
        private int gMonth; // Month part of a Gregorian date
        private int gDay; // Day part of a Gregorian date
        private int juYear; // Year part of a Julian date
        private int juMonth; // Month part of a Julian date
        private int juDay; // Day part of a Julian date
        private int leap; // Number of years since the last leap year (0 to 4)
        private int JDN; // Julian Day Number
        private int march; // The march day of Farvardin the first (First day of jaYear)
        public PersianDateTime(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        public  String  Shamsi_Date(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            return getIranianDate();
        }
        public  String  GetNowTime(){

            Date  Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss");
            String Now=DateFormat.format(Time);
            return Now;

        }
        public  String  GetNowDate(){
            Date  Date=new Date();
            SimpleDateFormat DateFormat =
                    new SimpleDateFormat ("yyyy.MM.dd");
            String Now=DateFormat.format(Date);
            return Now;
        }
        public  int     GetTimeInDate(){

            Date  Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH");
            String Now=DateFormat.format(Time);
            return Integer.valueOf(Now);

        }
        public  int     GetMonthInYear(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            return irMonth;
        }
        public  String  getShamsiDateForFileName(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            return getIranianDateForFileName();
        }
        public  String  Shamsi_Date_WithoutSeperator(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            return getIranianDateWithoutSeperator();
        }
        public  int     GetNowTimeInteger(){
            Date  Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm");

            String Now=DateFormat.format(Time);

            String HH=Now.substring(0,2);
            String MM=Now.substring(3,5);

            int R=Integer.valueOf(HH+MM);
            Now=null;
            HH=null;
            MM=null;
            return R;
        }
        public PersianDateTime(int year, int month, int day)
        {
            setGregorianDate(year,month,day);
        }
        public int getIranianYear() {
            return irYear;
        }
        public int getIranianMonth() {
            return irMonth;
        }
        public int getIranianDay() {
            return irDay;
        }
        public int getGregorianYear() {
            return gYear;
        }
        public int getGregorianMonth() {
            return gMonth;
        }
        public int getGregorianDay() {
            return gDay;
        }
        public int getJulianYear() {
            return juYear;
        }
        public int getJulianMonth() {
            return juMonth;
        }
        public int getJulianDay() {
            return juDay;
        }
        public String getIranianDate(){
            if ((irMonth<10)&&(irDay<10)){
                return (irYear+"/0"+irMonth+"/0"+irDay);
            }else if (irMonth<10){
                return (irYear+"/0"+irMonth+"/"+irDay);
            }else if (irDay<10){
                return (irYear+"/"+irMonth+"/0"+irDay);
            }else return (irYear+"/"+irMonth+"/"+irDay);

        }
        public String getIranianDateForFileName(){
            if ((irMonth<10)&&(irDay<10)){
                return (irYear+".0"+irMonth+".0"+irDay);
            }else if (irMonth<10){
                return (irYear+".0"+irMonth+"."+irDay);
            }else if (irDay<10){
                return (irYear+"."+irMonth+".0"+irDay);
            }else return (irYear+"."+irMonth+"."+irDay);
        }
        public String getIranianDateWithoutSeperator(){
            if ((irMonth<10)&&(irDay<10)){
                return (irYear+"0"+irMonth+"0"+irDay);
            }else if (irMonth<10){
                return (irYear+"0"+irMonth+""+irDay);
            }else if (irDay<10){
                return (irYear+""+irMonth+"0"+irDay);
            }else return (irYear+""+irMonth+""+irDay);
        }
        public String getGregorianDate()
        {
            return (gYear+"/"+gMonth+"/"+gDay);
        }
        public String getJulianDate()
        {
            return (juYear+"/"+juMonth+"/"+juDay);
        }
        public String getWeekDayStr() {
            String weekDayStr[]={
                    "Monday",
                    "Tuesday",
                    "Wednesday",
                    "Thursday",
                    "Friday",
                    "Saturday",
                    "Sunday"};
            return (weekDayStr[getDayOfWeek()]);
        }
        public String toString(){
            return (getWeekDayStr()+
                    ", Gregorian:["+getGregorianDate()+
                    "], Julian:["+getJulianDate()+
                    "], Iranian:["+getIranianDate()+"]");
        }
        public int getDayOfWeek()
        {
            return (JDN % 7);
        }
        public void nextDay(){
            JDN++;
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void nextDay(int days) {
            JDN+=days;
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void previousDay(){
            JDN--;
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void previousDay(int days){
            JDN-=days;
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void setIranianDate(int year, int month, int day){
            irYear =year;
            irMonth = month;
            irDay = day;
            JDN = IranianDateToJDN();
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void setGregorianDate(int year, int month, int day){
            gYear = year;
            gMonth = month;
            gDay = day;
            JDN = gregorianDateToJDN(year,month,day);
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void setJulianDate(int year, int month, int day){
            juYear = year;
            juMonth = month;
            juDay = day;
            JDN = julianDateToJDN(year,month,day);
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        private void IranianCalendar(){
// Iranian years starting the 33-year rule
            int Breaks[]=
                    {-61, 9, 38, 199, 426, 686, 756, 818,1111,1181,
                            1210,1635,2060,2097,2192,2262,2324,2394,2456,3178} ;
            int jm,N,leapJ,leapG,jp,j,jump;
            gYear = irYear + 621;
            leapJ = -14;
            jp = Breaks[0];
// Find the limiting years for the Iranian year 'irYear'
            j=1;
            do{
                jm=Breaks[j];
                jump = jm-jp;
                if (irYear >= jm)
                {
                    leapJ += (jump / 33 * 8 + (jump % 33) / 4);
                    jp = jm;
                }
                j++;
            } while ((j<20) && (irYear >= jm));
            N = irYear - jp;
// Find the number of leap years from AD 621 to the begining of the current
// Iranian year in the Iranian (Jalali) calendar
            leapJ += (N/33 * 8 + ((N % 33) +3)/4);
            if ( ((jump % 33) == 4 ) && ((jump-N)==4))
                leapJ++;
// And the same in the Gregorian date of Farvardin the first
            leapG = gYear/4 - ((gYear /100 + 1) * 3 / 4) - 150;
            march = 20 + leapJ - leapG;
// Find how many years have passed since the last leap year
            if ( (jump - N) < 6 )
                N = N - jump + ((jump + 4)/33 * 33);
            leap = (((N+1) % 33)-1) % 4;
            if (leap == -1)
                leap = 4;
        }
        public boolean IsLeap(int irYear1){
// Iranian years starting the 33-year rule
            int Breaks[]=
                    {-61, 9, 38, 199, 426, 686, 756, 818,1111,1181,
                            1210,1635,2060,2097,2192,2262,2324,2394,2456,3178} ;
            int jm,N,leapJ,leapG,jp,j,jump;
            gYear = irYear1 + 621;
            leapJ = -14;
            jp = Breaks[0];
// Find the limiting years for the Iranian year 'irYear'
            j=1;
            do{
                jm=Breaks[j];
                jump = jm-jp;
                if (irYear1 >= jm)
                {
                    leapJ += (jump / 33 * 8 + (jump % 33) / 4);
                    jp = jm;
                }
                j++;
            } while ((j<20) && (irYear1 >= jm));
            N = irYear1 - jp;
// Find the number of leap years from AD 621 to the begining of the current
// Iranian year in the Iranian (Jalali) calendar
            leapJ += (N/33 * 8 + ((N % 33) +3)/4);
            if ( ((jump % 33) == 4 ) && ((jump-N)==4))
                leapJ++;
// And the same in the Gregorian date of Farvardin the first
            leapG = gYear/4 - ((gYear /100 + 1) * 3 / 4) - 150;
            march = 20 + leapJ - leapG;
// Find how many years have passed since the last leap year
            if ( (jump - N) < 6 )
                N = N - jump + ((jump + 4)/33 * 33);
            leap = (((N+1) % 33)-1) % 4;
            if (leap == -1)
                leap = 4;
            if (leap==4 || leap==0)
                return true;
            else
                return false;

        }
        private int IranianDateToJDN(){
            IranianCalendar();
            return (gregorianDateToJDN(gYear,3,march)+ (irMonth-1) * 31 - irMonth/7 * (irMonth-7) + irDay -1);
        }
        private void JDNToIranian(){
            JDNToGregorian();
            irYear = gYear - 621;
            IranianCalendar(); // This invocation will update 'leap' and 'march'
            int JDN1F = gregorianDateToJDN(gYear,3,march);
            int k = JDN - JDN1F;
            if (k >= 0)
            {
                if (k <= 185)
                {
                    irMonth = 1 + k/31;
                    irDay = (k % 31) + 1;
                    return;
                }
                else
                    k -= 186;
            }
            else
            {
                irYear--;
                k += 179;
                if (leap == 1)
                    k++;
            }
            irMonth = 7 + k/30;
            irDay = (k % 30) + 1;
        }
        private int julianDateToJDN(int year, int month, int day) {
            return (year + (month - 8) / 6 + 100100) * 1461/4 + (153 * ((month+9) % 12) + 2)/5 + day - 34840408;
        }
        private void JDNToJulian(){
            int j= 4 * JDN + 139361631;
            int i= ((j % 1461)/4) * 5 + 308;
            juDay = (i % 153) / 5 + 1;
            juMonth = ((i/153) % 12) + 1;
            juYear = j/1461 - 100100 + (8-juMonth)/6;
        }
        private int gregorianDateToJDN(int year, int month, int day){
            int jdn = (year + (month - 8) / 6 + 100100) * 1461/4 + (153 * ((month+9) % 12) + 2)/5 + day - 34840408;
            jdn = jdn - (year + 100100+(month-8)/6)/100*3/4+752;
            return (jdn);
        }
        private void JDNToGregorian(){
            int j= 4 * JDN + 139361631;
            j = j + (((((4* JDN +183187720)/146097)*3)/4)*4-3908);
            int i= ((j % 1461)/4) * 5 + 308;
            gDay = (i % 153) / 5 + 1;
            gMonth = ((i/153) % 12) + 1;
            gYear = j/1461 - 100100 + (8-gMonth)/6;
        }
    }
    public static final PersianDateTime persianDateTime= new PersianDateTime();

    public static class PropertiesUtil {

        PropertiesUtil(){
            try
            {
                readConfig();;
            }catch (Exception e){

            }
        }

        public  void readConfig() throws Exception {

            File FileOfSettings = new File(Util.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String filePath=FileOfSettings.toString();
          //  System.out.println("filePath:"+filePath);
            String part1Path=strutils.leftString(filePath,filePath.length()-16 );
         //   System.out.println("part1Path:"+part1Path);
            String Path =part1Path +"/monitoringDB.properties";
         //   System.out.println("Path:"+Path);
            setPath(part1Path);
            Properties props = new Properties();

            //----------------------------------



                try{
                    props.load(new FileInputStream(Path));
                }catch (Exception var1){
                   // System.out.println(var1.toString());
                    try{
                      // String Path1= filePath+"/MonitoringDB";

                        props.load(new FileInputStream(filePath+"/monitoringDB.properties"));// if class in running

                    }catch (Exception ee){
                        //System.out.println(ee.toString());

                    }
                }
            setHostIP(props.getProperty("HostIP"));
            setMonitoringServicePort(props.getProperty("MonitoringServicePort"));
            setMonitoringServerIP(props.getProperty("MonitoringServerIP"));
            setMonitoringServerPort(props.getProperty("MonitoringServerPort"));


        }

        private String Path=null;
        public  void   setPath(String path){
            Path=path;
        }
        public  String getPath(){
            return Path;
        }

        private  String HostIP=null;
        public  void   setHostIP(String hostIP){
            HostIP=hostIP;
        }
        public   String getHostIP(){
            return HostIP;
        }


        private  String MonitoringServicePort=null;
        public  void   setMonitoringServicePort(String monitoringServicePort){
            MonitoringServicePort=monitoringServicePort;
        }
        public   String getMonitoringServicePort(){
            return MonitoringServicePort;
        }


        public  void    setMonitoringServerPort(String MonitoringServerPort){
            this.MonitoringServerPort=MonitoringServerPort;
        }
        public   String getMonitoringServerPort(){
            return MonitoringServerPort;
        }
        private  String MonitoringServerPort=null;

        private  String MonitoringServerIP=null;
        public  void    setMonitoringServerIP(String MonitoringServerIP){
            this.MonitoringServerIP=MonitoringServerIP;
        }
        public   String getMonitoringServerIP(){
            return MonitoringServerIP;
        }
    }
    private static final PropertiesUtil prob= new PropertiesUtil();

    public static final String IP= prob.getHostIP();
    public static final String MonitoringPort= prob.getMonitoringServicePort();
    public static final String MonitoringServerIP= prob.getMonitoringServerIP();
    public static final String MonitoringServerPort= prob.getMonitoringServerPort();




}
