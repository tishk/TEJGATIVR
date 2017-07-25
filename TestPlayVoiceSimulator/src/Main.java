public class Main {
    static int ZeroResult =0;
    public static void main(String[] args) {
	  SayPersianDigit("10000145");
    }
    public static void playFile(String s){
        System.out.println(s);
    }
    public  static  boolean SayPersianDigit(String Digit){
        try{

            if (Long.valueOf(Digit) ==0) {

                playFile("NUM/"+Digit);
                return true;
            }
            int len=Digit.length();
            int LeftIndex=len %3;
            if ((LeftIndex==0)&&(len>=3)) LeftIndex=3;
            if (len>2){
                String SayNOW=Digit.substring(0,LeftIndex);
                Say3Digits(SayNOW);
                String DigitTemp=Digit.substring(LeftIndex);
                if (0==Long.valueOf(DigitTemp)){

                    playConfFile(SetConnectionFile(Digit.length(), 0));
                }else{
                    playConfFile(SetConnectionFile(Digit.length(), 1));
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
    private static String  SetConnectionFile(int Len, int Kind){
        String Part1="NUM/";
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
    private static  boolean Say3Digits(String SayNow) {

        if (SayNow.length()==1) SayNow="00"+SayNow;
        if (SayNow.length()==2) SayNow="0"+SayNow;
        if (HaveZero(SayNow)){

            if (ZeroResult ==1){
                playFile("NUM/" + SayNow.substring(0,1)+"00o");
                playFile("NUM/" + SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==10){
                playFile("NUM/" + SayNow.substring(0,1)+"00o");
                playFile("NUM/" + SayNow.substring(2,3));
            }else if (ZeroResult ==11){
                playFile("NUM/" + SayNow.substring(0,1)+"00");
            }else if (ZeroResult ==100){
                //Util.printMessage("in say less than 20:" + SayNow.substring(1, 3), false);
                if (Integer.parseInt(SayNow)<=20){
                    playFile("NUM/" + SayNow.substring(1,3));
                    //Util.printMessage("in say less than 20:"+SayNow.substring(1,3),false);
                }else{
                    playFile("NUM/" + SayNow.substring(1,2)+"0o");
                    playFile("NUM/" + SayNow.substring(2,3));
                }
            }else if (ZeroResult ==101){
                playFile("NUM/" + SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==110){
                //Util.printMessage("110:"+SayNow.substring(1,3),false);
                playFile("NUM/" + SayNow.substring(2,3));
            }else if (ZeroResult ==111){

            }
        }else{
            if (Integer.parseInt(SayNow.substring(1,3))<=20 ){
                playFile("NUM/" + SayNow.substring(0,1)+"00o");
                playFile("NUM/" + SayNow.substring(1,3));
            }else{
                playFile("NUM/" + SayNow.substring(0,1)+"00o");
                playFile("NUM/" + SayNow.substring(1,2)+"0o");
                playFile("NUM/" + SayNow.substring(2,3));
            }


        }
        return true;
    }
    private static boolean  HaveZero(String SayNow){
        ZeroResult =0;
        if ("0".equals(SayNow.substring(2,3))) ZeroResult =1;
        if ("0".equals(SayNow.substring(1,2))) ZeroResult = ZeroResult +10;
        if ("0".equals(SayNow.substring(0,1))) ZeroResult = ZeroResult +100;
        if (ZeroResult !=0) return true;
        else return false;
    }
    public static void    playConfFile(String S) {
        playFile(S);
    }
}
