package ServiceObjects.Other;

import java.sql.Timestamp;

/**
 * Created by root on 4/20/16.
 */
public class MasterTableObject {
    private  String ACCNO="";
    public  void setACCNO(String accNo){
        ACCNO=accNo;
    }
    public  String getACCNO(){
        return ACCNO;
    }

    private  String BRANCHNO="";
    public  void setBRANCHNO(String bRANCHNO){
        BRANCHNO=BRANCHNO;
    }
    public  String getBRANCHNO(){
        return BRANCHNO;
    }

    private  String NAME="";
    public  void setNAME(String nAME){
        NAME=nAME;
    }
    public  String getNAME(){
        return NAME;
    }

    private  String PRIMARYPASSWORD="";
    public  void setPRIMARYPASSWORD(String pRIMARYPASSWORD){
        PRIMARYPASSWORD=pRIMARYPASSWORD;
    }
    public  String getPRIMARYPASSWORD(){
        return PRIMARYPASSWORD;
    }

    private  String PASSWORD="";
    public  void setPASSWORD(String accNo){
        ACCNO=accNo;
    }
    public  String getPASSWORD(){
        return ACCNO;
    }

    private  String SUFFIX="";
    public  void setSUFFIX(String sUFFIX){
        SUFFIX=sUFFIX;
    }
    public  String getSUFFIX(){
        return SUFFIX;
    }

    private  String HESABGROUP="";
    public  void setHESABGROUP(String hESABGROUP){
        HESABGROUP=hESABGROUP;
    }
    public  String getHESABGROUP(){
        return HESABGROUP;
    }

    private  int REGDATE=0;
    public  void setREGDATE(int rEGDATE){
        REGDATE=rEGDATE;
    }
    public  int getREGDATE(){
        return REGDATE;
    }

    private  int CENTERCODE=0;
    public  void setCENTERCODE(int cENTERCODE){
        CENTERCODE=cENTERCODE;
    }
    public  int getCENTERCODE(){
        return CENTERCODE;
    }

    private  int CHANGEDATE=0;
    public  void setCHANGEDATE(int CHANGEDATE){
        CHANGEDATE=CHANGEDATE;
    }
    public  int getCHANGEDATE(){
        return CHANGEDATE;
    }

    private  String FAMILY="";
    public  void setFAMILY(String fAMILY){
        FAMILY=fAMILY;
    }
    public  String getFAMILY(){
        return FAMILY;
    }

    private  String BITCHANGE="";
    public  void setBITCHANGE(String bITCHANGE){
        BITCHANGE=bITCHANGE;
    }
    public  String getBITCHANGE(){
        return BITCHANGE;
    }

    private  String AVACASACTIVE="";
    public  void setAVACASACTIVE(String aVACASACTIVE){
        AVACASACTIVE=aVACASACTIVE;
    }
    public  String getAVACASACTIVE(){
        return AVACASACTIVE;
    }

    private  String SENDTOAVACAS="";
    public  void setSENDTOAVACAS(String sENDTOAVACAS){
        SENDTOAVACAS=sENDTOAVACAS;
    }
    public  String getSENDTOAVACAS(){
        return SENDTOAVACAS;
    }


    private  String ORMMSGID="";
    public  void setORMMSGID(String oRMMSGID){
        ORMMSGID=oRMMSGID;
    }
    public  String getORMMSGID(){
        return ORMMSGID;
    }

    private Timestamp CREATEDATE;
    public  void setCREATEDATE(Timestamp cREATEDATE){
        CREATEDATE=cREATEDATE;
    }
    public  Timestamp getCREATEDATE(){
        return CREATEDATE;
    }

    private Timestamp UPDATEDATE;
    public  void setUPDATEDATE(Timestamp uPDATEDATE){
        UPDATEDATE=uPDATEDATE;
    }
    public  Timestamp getUPDATEDATE(){
        return UPDATEDATE;
    }



}
