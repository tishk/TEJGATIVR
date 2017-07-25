package Pin2.currentMethod.Avacas;

import Pin2.currentMethod.util.CodecUtil;
import Pin2.currentMethod.util.PropertiesUtil;
import ServiceObjects.Pin.AuthenticatePin2;
import ServiceObjects.Account.BaseAccountRequest;
import org.apache.commons.lang.StringUtils;


import java.util.Random;


public class AvaCasThread implements Runnable {

    private byte response[];
    private String idSeq;
    private String messageIn;
    private String MsgId;
    private String MsgType;

    public void setResponse(byte response[]) {
        this.response = response;
    }

    private AuthenticatePin2 Result=null;
    private void setResult(AuthenticatePin2 result){
        this.Result=result;
    }
    public AuthenticatePin2 getResult(){
        return this.Result;
    }

    private AuthenticatePin2 AuthPin2=null;
    private void setAuthPin2(AuthenticatePin2 authPin2){
        this.AuthPin2=authPin2;
    }
    public  AuthenticatePin2 getAuthPin2(){
        return this.AuthPin2;
    }

    public String getIdSeq() {
        return this.idSeq;
    }



    public  AvaCasThread(AuthenticatePin2 authenticatePin2) {
        super();
        //setAuthPin2(authenticatePin2);
        setAuthPin2(SendToAvaCast(authenticatePin2));
    }
    private AuthenticatePin2 SendToAvaCast(AuthenticatePin2 authenticatePin2 ){
     if (authenticatePin2.getDoChangePin()) return Pin2_Change(authenticatePin2);
        else return Pin2_authenticate(authenticatePin2);
    }
    private AuthenticatePin2 Pin2_authenticate(AuthenticatePin2 authenticatePin2){
        BaseAccountRequest Result=new BaseAccountRequest();
        AuthPin2=authenticatePin2;
        try {
            AvaCas.sendBytes(PrepareAuthenticatePin2MSG(AuthPin2));
            try {

                AvaCas.idHolderHashMap.put(idSeq, this);
                long t1 = System.currentTimeMillis();
                synchronized (idSeq) {
                    idSeq.wait(15000L);
                }
                long totalTime = System.currentTimeMillis() - t1;
                if (totalTime >= 15000L) {
                    Result.setGatewayMessage((new StringBuilder())
                            .append("Queue not answered, Timeout occured : ")
                            .append(totalTime).append(" Thread # ")
                            .append(Thread.currentThread().getId()).toString());
                }
            } catch (InterruptedException e) {
                response = (new StringBuilder())
                        .append(idSeq)
                        .append("#2#3#").toString().getBytes();
            }
        } catch (Exception e) {
            response = (new StringBuilder())
                    .append(idSeq)
                    .append("#2#3#").toString().getBytes();
        }
        String St3=null;
        if (response != null) {
            St3 = (new  StringBuilder())
                    .append(StringUtils.leftPad(Integer.toString(response.length), 3, "0"))
                    .append(new String(response)).toString();

        } else {
            Result.setGatewayMessage("Queue not answered, Response is null ");
            St3 = (new StringBuilder()).append(idSeq).append("#2#3#").toString();

        }
        AuthPin2.setActionCode(getActionCode(new String(St3.getBytes())));
        AuthPin2.setIsSuccessfulConnectToGateway(true);


        setResult(AuthPin2);
        return AuthPin2;
    }
    private AuthenticatePin2 Pin2_Change(AuthenticatePin2 authenticatePin2){
        BaseAccountRequest Result=new BaseAccountRequest();
        AuthPin2=authenticatePin2;
        try {
            Pin2.currentMethod.Avacas.AvaCas.sendBytes(PrepareChangePin2MSG(AuthPin2));
            try {

                AvaCas.idHolderHashMap.put(idSeq, this);
                long t1 = System.currentTimeMillis();
                synchronized (idSeq) {
                    idSeq.wait(15000L);
                }
                long totalTime = System.currentTimeMillis() - t1;
                if (totalTime >= 15000L) {
                    Result.setGatewayMessage((new StringBuilder())
                            .append("Queue not answered, Timeout occured : ")
                            .append(totalTime).append(" Thread # ")
                            .append(Thread.currentThread().getId()).toString());
                }
            } catch (InterruptedException e) {
                response = (new StringBuilder())
                        .append(idSeq)
                        .append("#2#3#").toString().getBytes();
            }
        } catch (Exception e) {
            response = (new StringBuilder())
                    .append(idSeq)
                    .append("#2#3#").toString().getBytes();
        }
        String St3=null;
        if (response != null) {
            St3 = (new  StringBuilder())
                    .append(StringUtils.leftPad(Integer.toString(response.length), 3, "0"))
                    .append(new String(response)).toString();

        } else {
            Result.setGatewayMessage("Queue not answered, Response is null ");
            St3 = (new StringBuilder()).append(idSeq).append("#2#3#").toString();

        }
        AuthPin2.setActionCode(getActionCode(new String(St3.getBytes())));
        AuthPin2.setIsSuccessfulConnectToGateway(true);


        setResult(AuthPin2);
        return AuthPin2;
    }


    private byte[] PrepareAuthenticatePin2MSG(AuthenticatePin2 authenticatePin2OBJ) throws Exception {
        Random random = new Random();
        String RandNO=String.valueOf(random.nextInt(9999 - 1 + 1) + 1)+
                String.valueOf(random.nextInt(9999 - 1 + 1) + 1);
        String MessageStr=  RandNO+
                "#2#"+
                authenticatePin2OBJ.getPin()+
                "#2#"+
                authenticatePin2OBJ.getAccountNumber()+
                "#";
       // String P1="000"+String.valueOf(MessageStr.length());
       // String P2=P1.substring(P1.length()-3);
      //  MessageStr=P2+MessageStr;
        String St2[] = MessageStr.split("#");
        idSeq = St2[0];
     //   P1=null;
      //  P2=null;
        try{
            return CodecUtil.encrypt(MessageStr, "2#", PropertiesUtil.getKeyOrm());
        }catch (Exception e){
            return null;
        }



    }
    private byte[] PrepareChangePin2MSG(AuthenticatePin2 authenticatePin2OBJ) throws Exception {
        Random random = new Random();
        String RandNO=String.valueOf(random.nextInt(9999 - 1 + 1) + 1)+
                String.valueOf(random.nextInt(9999 - 1 + 1) + 1);
        String MessageStr=  RandNO+
                "#1#"+
                authenticatePin2OBJ.getPin()+
                "#"+
                authenticatePin2OBJ.getPin_New()+
                "#2#"+
                authenticatePin2OBJ.getAccountNumber()+
                "#";
      //  String P1="000"+String.valueOf(MessageStr.length());
      //  String P2=P1.substring(P1.length()-3);
     //   MessageStr=P2+MessageStr;
        String St2[] = MessageStr.split("#");
        idSeq = St2[0];
      //  P1=null;
      //  P2=null;
        try{
            return CodecUtil.encrypt(MessageStr, "2#", PropertiesUtil.getKeyAvaCas());
        }catch (Exception e){
            return null;
        }




    }

    private String getActionCode(String res){
        int ResponseResult=0;
        try{
            ResponseResult=Integer.valueOf(res.substring(res.length() - 2, res.length() - 1));
        }catch (Exception e){
            ResponseResult=3;
        }

        switch (ResponseResult){
            case 0:return "0000";

            case 1:return "9902";

            case 2:return "9903";

            case 3:return "9904";

            default:return "9904";

        }
    }


    @Override
    public void run() {
     //  setResult(SendToAvaCast());
    }
}
