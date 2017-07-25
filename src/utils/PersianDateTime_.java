package utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class PersianDateTime_ {
    public  String  getShamsiDate() {

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
        return String.valueOf(Year) + "/" + String.valueOf(Month) + "/" + String.valueOf(Day);

    }
    public  String  getShamsiDateForFileName() {

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
        String M="";
        String D="";
        if (Month<10) M="0"+String.valueOf(Month);else M=String.valueOf(Month);
        if (Day<10)   D="0"+String.valueOf(Day);else D=String.valueOf(Day);
        return String.valueOf(Year) + "." +M + "." + D;

    }
    public  String  getShamsi_Date_WithoutSeperator() {

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
        String M=null;
        String D=null;
        if (Month<10) M="0"; else M="";
        if (Day<10) D="0"; else D="";
        return String.valueOf(Year) +  M+String.valueOf(Month) + D+String.valueOf(Day);

    }
    public  String  getRequestTime(){
        Date  Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("HHmmss");
        String Now=DateFormat.format(Time);
        return Now;
    }
    public  String  getRequestDate(){
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
        String m="";
        String d="";
        if (Month<10) m= "0"+String.valueOf(Month); else m= String.valueOf(Month);
        if (Day<10) d= "0"+String.valueOf(Day); else d= String.valueOf(Day);
        return String.valueOf(Year) +  m  + d;
    }
    public  String  getRequestDate(int BeforeDayCount){

        LocalDate BeforeDate = LocalDate.now().minusDays(BeforeDayCount);
        int Day = BeforeDate.getDayOfMonth();
        int Month =BeforeDate.getMonthValue() ;
        int Year = BeforeDate.getYear();

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
        String m="";
        String d="";


        if (Month<10) m= "0"+String.valueOf(Month); else m= String.valueOf(Month);
        if (Day<10) d= "0"+String.valueOf(Day); else d= String.valueOf(Day);
        return String.valueOf(Year) +  m  + d;
    }


    public  String  getRequestDateTime(){
        return getRequestDate()+getRequestTime();
    }
    public  String  GetNowDateTime(){
        Date DateTime=new Date();
        SimpleDateFormat DateFormat =
                new SimpleDateFormat ("yyyy.MM.dd '@' hh:mm:ss a");
        String Now=DateFormat.format(DateTime);
        return getShamsiDate()+"  "+Now.substring(13);
    }
    public  String  GetNowDate(){
        Date  Date=new Date();
        SimpleDateFormat DateFormat =
                new SimpleDateFormat ("yyyy.MM.dd");
        String Now=DateFormat.format(Date);
        return Now;
    }
    public  String  GetNowTime(){

        Date  Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("HHmmss");
        String Now=DateFormat.format(Time);
        return Now;

    }
    public  int     getYearOfNow(){

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
        return Integer.valueOf(String.valueOf(Year).substring(0, 2)) ;

    }
    public  int     getMonthOfNow(){

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
        return Integer.valueOf(String.valueOf(Month)) ;

    }
    public  int     getDayOfNow(){

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
        return Integer.valueOf(String.valueOf(Day)) ;

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
    public  String  ConvertDateForFax(String date) {
        String Result=null;
        try
        {
            Result=date.substring(0,2)+"/"+date.substring(2,4)+"/"+date.substring(4,6);

        }catch (Exception e){
            Result=null;
        }
        return Result;
    }
    public  String  ConvertDBTime(String time){
        String Result=null;
        try
        {
            Result=time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);

        }catch (Exception e){
            Result=null;
        }
        return Result;
    }
}
