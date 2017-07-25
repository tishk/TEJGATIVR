package ServiceObjects.ISO;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Administrator on 9/1/2016.
 */
public class BitMap implements Serializable {

    private char P1='0'; private char P2='0'; private char P3='0'; private char P4='0';
    private char P5='0'; private char P6='0'; private char P7='0'; private char P8='0';
    private char P9='0'; private char P10='0';private char P11='0';private char P12='0';
    private char P13='0';private char P14='0';private char P15='0';private char P16='0';
    private char P17='0';private char P18='0';private char P19='0';private char P20='0';
    private char P21='0';private char P22='0';private char P23='0';private char P24='0';
    private char P25='0';private char P26='0';private char P27='0';private char P28='0';
    private char P29='0';private char P30='0';private char P31='0';private char P32='0';
    private char P33='0';private char P34='0';private char P35='0';private char P36='0';
    private char P37='0';private char P38='0';private char P39='0';private char P40='0';
    private char P41='0';private char P42='0';private char P43='0';private char P44='0';
    private char P45='0';private char P46='0';private char P47='0';private char P48='0';
    private char P49='0';private char P50='0';private char P51='0';private char P52='0';
    private char P53='0';private char P54='0';private char P55='0';private char P56='0';
    private char P57='0';private char P58='0';private char P59='0';private char P60='0';
    private char P61='0';private char P62='0';private char P63='0';private char P64='0';

    private String binaryBitMap="";
    private String hexBitMap="";


    public BitMap(String bitmap){

        if (bitmap.length()==16){
            this.hexBitMap=bitmap;
            this.binaryBitMap=hexToBin(hexBitMap);
            isCorrectBitMap=true;
            processBitMap();
        }else
            try {throw new Exception();} catch (Exception e) {System.out.print("Error on Entry Len");}
    }

    private String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
    }
    private boolean convertCharToBoolean(char ch) {
        return (ch=='1' || ch!='0');
    }
    private void processBitMap(){
        int i=0;
        setP1(binaryBitMap.charAt(i++));
        setP2(binaryBitMap.charAt(i++));
        setP3(binaryBitMap.charAt(i++));
        setP4(binaryBitMap.charAt(i++));
        setP5(binaryBitMap.charAt(i++));
        setP6(binaryBitMap.charAt(i++));
        setP7(binaryBitMap.charAt(i++));
        setP8(binaryBitMap.charAt(i++));
        setP9(binaryBitMap.charAt(i++));
        setP10(binaryBitMap.charAt(i++));
        setP11(binaryBitMap.charAt(i++));
        setP12(binaryBitMap.charAt(i++));
        setP13(binaryBitMap.charAt(i++));
        setP14(binaryBitMap.charAt(i++));
        setP15(binaryBitMap.charAt(i++));
        setP16(binaryBitMap.charAt(i++));
        setP17(binaryBitMap.charAt(i++));
        setP18(binaryBitMap.charAt(i++));
        setP19(binaryBitMap.charAt(i++));
        setP20(binaryBitMap.charAt(i++));
        setP21(binaryBitMap.charAt(i++));
        setP22(binaryBitMap.charAt(i++));
        setP23(binaryBitMap.charAt(i++));
        setP24(binaryBitMap.charAt(i++));
        setP25(binaryBitMap.charAt(i++));
        setP26(binaryBitMap.charAt(i++));
        setP27(binaryBitMap.charAt(i++));
        setP28(binaryBitMap.charAt(i++));
        setP29(binaryBitMap.charAt(i++));
        setP30(binaryBitMap.charAt(i++));
        setP31(binaryBitMap.charAt(i++));
        setP32(binaryBitMap.charAt(i++));
        setP33(binaryBitMap.charAt(i++));
        setP34(binaryBitMap.charAt(i++));
        setP35(binaryBitMap.charAt(i++));
        setP36(binaryBitMap.charAt(i++));
        setP37(binaryBitMap.charAt(i++));
        setP38(binaryBitMap.charAt(i++));
        setP39(binaryBitMap.charAt(i++));
        setP40(binaryBitMap.charAt(i++));
        setP41(binaryBitMap.charAt(i++));
        setP42(binaryBitMap.charAt(i++));
        setP43(binaryBitMap.charAt(i++));
        setP44(binaryBitMap.charAt(i++));
        setP45(binaryBitMap.charAt(i++));
        setP46(binaryBitMap.charAt(i++));
        setP47(binaryBitMap.charAt(i++));
        setP48(binaryBitMap.charAt(i++));
        setP49(binaryBitMap.charAt(i++));
        setP50(binaryBitMap.charAt(i++));
        setP51(binaryBitMap.charAt(i++));
        setP52(binaryBitMap.charAt(i++));
        setP53(binaryBitMap.charAt(i++));
        setP54(binaryBitMap.charAt(i++));
        setP55(binaryBitMap.charAt(i++));
        setP56(binaryBitMap.charAt(i++));
        setP57(binaryBitMap.charAt(i++));
        setP58(binaryBitMap.charAt(i++));
        setP59(binaryBitMap.charAt(i++));
        setP60(binaryBitMap.charAt(i++));
        setP61(binaryBitMap.charAt(i++));
        setP62(binaryBitMap.charAt(i++));
        setP63(binaryBitMap.charAt(i++));
        setP64(binaryBitMap.charAt(i++));
    }


    public  boolean isCorrectBitMap=false;

    private void   setBinaryBitMap(String binaryBitMap){
        this.binaryBitMap=binaryBitMap;
    }
    public  String getBinaryBitMap(){
        return this.binaryBitMap;
    }

    private void   sethexBitMap(String hexBitMap){
        this.hexBitMap=hexBitMap;
    }
    public  String gethexBitMap(){
        return this.hexBitMap;
    }

    public void    setP1(char ch) {
        P1 = ch;
    }
    public boolean existP1() {
        return convertCharToBoolean(P1);
    }

    public void    setP2(char ch) {
        P2 = ch;
    }
    public boolean existP2() {
        return convertCharToBoolean(P2);
    }

    public void    setP3(char ch) {
        P3 = ch;
    }
    public boolean existP3() {
        return convertCharToBoolean(P3);
    }

    public void    setP4(char ch) {
        P4 = ch;
    }
    public boolean existP4() {
        return convertCharToBoolean(P4);
    }

    public void    setP5(char ch) {
        P5 = ch;
    }
    public boolean existP5() {
        return convertCharToBoolean(P5);
    }

    public void    setP6(char ch) {
        P6 = ch;
    }
    public boolean existP6() {
        return convertCharToBoolean(P6);
    }

    public void    setP7(char ch) {
        P7= ch;
    }
    public boolean existP7() {
        return convertCharToBoolean(P7);
    }

    public void    setP8(char ch) {
        P8 = ch;
    }
    public boolean existP8() {
        return convertCharToBoolean(P8);
    }

    public void    setP9(char ch) {
        P9 = ch;
    }
    public boolean existP9() {
        return convertCharToBoolean(P9);
    }

    public void    setP10(char ch) {
        P10 = ch;
    }
    public boolean existP10() {
        return convertCharToBoolean(P10);
    }

    public void    setP11(char ch) {
        P11 = ch;
    }
    public boolean existP11() {
        return convertCharToBoolean(P11);
    }

    public void    setP12(char ch) {
        P12 = ch;
    }
    public boolean existP12() {
        return convertCharToBoolean(P12);
    }

    public void    setP13(char ch) {
        P13 = ch;
    }
    public boolean existP13() {
        return convertCharToBoolean(P13);
    }

    public void    setP14(char ch) {
        P14 = ch;
    }
    public boolean existP14() {
        return convertCharToBoolean(P14);
    }

    public void    setP15(char ch) {
        P15 = ch;
    }
    public boolean existP15() {
        return convertCharToBoolean(P15);
    }

    public void    setP16(char ch) {
        P16 = ch;
    }
    public boolean existP16() {
        return convertCharToBoolean(P16);
    }

    public void    setP17(char ch) {
        P17= ch;
    }
    public boolean existP17() {
        return convertCharToBoolean(P17);
    }

    public void    setP18(char ch) {
        P18 = ch;
    }
    public boolean existP18() {
        return convertCharToBoolean(P18);
    }

    public void    setP19(char ch) {
        P19 = ch;
    }
    public boolean existP19() {
        return convertCharToBoolean(P19);
    }

    public void    setP20(char ch) {
        P20 = ch;
    }
    public boolean existP20() {
        return convertCharToBoolean(P20);
    }

    public void    setP21(char ch) {
        P21 = ch;
    }
    public boolean existP21() {
        return convertCharToBoolean(P21);
    }

    public void    setP22(char ch) {
        P22 = ch;
    }
    public boolean existP22() {
        return convertCharToBoolean(P22);
    }

    public void    setP23(char ch) {
        P23 = ch;
    }
    public boolean existP23() {
        return convertCharToBoolean(P23);
    }

    public void    setP24(char ch) {
        P24 = ch;
    }
    public boolean existP24() {
        return convertCharToBoolean(P24);
    }

    public void    setP25(char ch) {
        P25 = ch;
    }
    public boolean existP25() {
        return convertCharToBoolean(P25);
    }

    public void    setP26(char ch) {
        P26 = ch;
    }
    public boolean existP26() {
        return convertCharToBoolean(P26);
    }

    public void    setP27(char ch) {
        P27= ch;
    }
    public boolean existP27() {
        return convertCharToBoolean(P27);
    }

    public void    setP28(char ch) {
        P28 = ch;
    }
    public boolean existP28() {
        return convertCharToBoolean(P28);
    }

    public void    setP29(char ch) {
        P29 = ch;
    }
    public boolean existP29() {
        return convertCharToBoolean(P29);
    }

    public void    setP30(char ch) {
        P30 = ch;
    }
    public boolean existP30() {
        return convertCharToBoolean(P30);
    }

    public void    setP31(char ch) {
        P31 = ch;
    }
    public boolean existP31() {
        return convertCharToBoolean(P31);
    }

    public void    setP32(char ch) {
        P32 = ch;
    }
    public boolean existP32() {
        return convertCharToBoolean(P32);
    }

    public void    setP33(char ch) {
        P33 = ch;
    }
    public boolean existP33() {
        return convertCharToBoolean(P33);
    }

    public void    setP34(char ch) {
        P34 = ch;
    }
    public boolean existP34() {
        return convertCharToBoolean(P34);
    }

    public void    setP35(char ch) {
        P35 = ch;
    }
    public boolean existP35() {
        return convertCharToBoolean(P35);
    }

    public void    setP36(char ch) {
        P36 = ch;
    }
    public boolean existP36() {
        return convertCharToBoolean(P36);
    }

    public void    setP37(char ch) {
        P37= ch;
    }
    public boolean existP37() {
        return convertCharToBoolean(P37);
    }

    public void    setP38(char ch) {
        P38 = ch;
    }
    public boolean existP38() {
        return convertCharToBoolean(P38);
    }

    public void    setP39(char ch) {
        P39 = ch;
    }
    public boolean existP39() {
        return convertCharToBoolean(P39);
    }

    public void    setP40(char ch) {
        P40 = ch;
    }
    public boolean existP40() {
        return convertCharToBoolean(P40);
    }

    public void    setP41(char ch) {
        P41 = ch;
    }
    public boolean existP41() {
        return convertCharToBoolean(P41);
    }

    public void    setP42(char ch) {
        P42 = ch;
    }
    public boolean existP42() {
        return convertCharToBoolean(P42);
    }

    public void    setP43(char ch) {
        P43 = ch;
    }
    public boolean existP43() {
        return convertCharToBoolean(P43);
    }

    public void    setP44(char ch) {
        P44 = ch;
    }
    public boolean existP44() {
        return convertCharToBoolean(P44);
    }

    public void    setP45(char ch) {
        P45 = ch;
    }
    public boolean existP45() {
        return convertCharToBoolean(P45);
    }

    public void    setP46(char ch) {
        P46 = ch;
    }
    public boolean existP46() {
        return convertCharToBoolean(P46);
    }

    public void    setP47(char ch) {
        P47= ch;
    }
    public boolean existP47() {
        return convertCharToBoolean(P47);
    }

    public void    setP48(char ch) {
        P48 = ch;
    }
    public boolean existP48() {
        return convertCharToBoolean(P48);
    }

    public void    setP49(char ch) {
        P49 = ch;
    }
    public boolean existP49() {
        return convertCharToBoolean(P49);
    }

    public void    setP50(char ch) {
        P50 = ch;
    }
    public boolean existP50() {
        return convertCharToBoolean(P50);
    }

    public void    setP51(char ch) {
        P51 = ch;
    }
    public boolean existP51() {
        return convertCharToBoolean(P51);
    }

    public void    setP52(char ch) {
        P52 = ch;
    }
    public boolean existP52() {
        return convertCharToBoolean(P52);
    }

    public void    setP53(char ch) {
        P53 = ch;
    }
    public boolean existP53() {
        return convertCharToBoolean(P53);
    }

    public void    setP54(char ch) {
        P54 = ch;
    }
    public boolean existP54() {
        return convertCharToBoolean(P54);
    }

    public void    setP55(char ch) {
        P5 = ch;
    }
    public boolean existP55() {
        return convertCharToBoolean(P55);
    }

    public void    setP56(char ch) {
        P56 = ch;
    }
    public boolean existP56() {
        return convertCharToBoolean(P56);
    }

    public void    setP57(char ch) {
        P57= ch;
    }
    public boolean existP57() {
        return convertCharToBoolean(P57);
    }

    public void    setP58(char ch) {
        P58 = ch;
    }
    public boolean existP58() {
        return convertCharToBoolean(P58);
    }

    public void    setP59(char ch) {
        P59 = ch;
    }
    public boolean existP59() {
        return convertCharToBoolean(P59);
    }

    public void    setP60(char ch) {
        P60 = ch;
    }
    public boolean existP60() {
        return convertCharToBoolean(P60);
    }

    public void    setP61(char ch) {
        P61 = ch;
    }
    public boolean existP61() {
        return convertCharToBoolean(P61);
    }

    public void    setP62(char ch) {
        P62 = ch;
    }
    public boolean existP62() {
        return convertCharToBoolean(P62);
    }

    public void    setP63(char ch) {
        P63 = ch;
    }
    public boolean existP63() {
        return convertCharToBoolean(P63);
    }

    public void    setP64(char ch) {
        P64 = ch;
    }
    public boolean existP64() {
        return convertCharToBoolean(P64);
    }



}
