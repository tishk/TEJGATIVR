package Mainchannel.util.JMSConnectionPool;

import Pin1.currentMethod.Avacas.AvaCas;
import Pin1.currentMethod.util.CodecUtil;
import Pin1.currentMethod.util.PropertiesUtil;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Account.BaseAccountRequest;
import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * Created by Administrator on 8/17/2015.
 */
public class MQServerToServerThread {

    private byte response[];
    private String idSeq;
    private String  messageIn ;
    public void setResponse(byte response[]) {
        this.response = response;
    }


    public AuthenticatePin1 getResult(){
        return this.AuthPin1;
    }

    private AuthenticatePin1 AuthPin1=null;
    private void setAuthPin1(AuthenticatePin1 authPin1){
        this.AuthPin1=authPin1;
    }
    public  AuthenticatePin1 getAuthPin1(){
        return this.AuthPin1;
    }

    public String getIdSeq() {
        return this.idSeq;
    }



    public  MQServerToServerThread(AuthenticatePin1 authenticatePin1) {
        super();
        //setAuthPin1(authenticatePin1);
        setAuthPin1(SendToAvaCast(authenticatePin1));


    }
    private AuthenticatePin1 SendToAvaCast( AuthenticatePin1 authenticatePin1){
        if (authenticatePin1.getDoChangePin()) return Pin1_Change(authenticatePin1);
        else return Pin1_Authenticate(authenticatePin1);
    }
    private AuthenticatePin1 Pin1_Authenticate(AuthenticatePin1 authenticatePin1){
        BaseAccountRequest Result=new BaseAccountRequest();
        AuthenticatePin1 a=authenticatePin1;
        try {

            byte[] S=PreparedAuthenticateMSG(a);
            //String  messageIn = PrepareAuthenticatePin1MSG(a);
            int messageLen = messageIn.length();
            String St2[] = messageIn.split("#");
            idSeq = St2[0];
            AvaCas.sendBytes(S);

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
        String S=new String(St3.getBytes());
        authenticatePin1.setActionCode(getActionCode(S));
        authenticatePin1.setIsSuccessfulConnectToGateway(true);


        return authenticatePin1;
    }
    private AuthenticatePin1 Pin1_Change(AuthenticatePin1 authenticatePin1){
        BaseAccountRequest Result=new BaseAccountRequest();
        AuthenticatePin1 a=authenticatePin1;
        try {

            byte[] S=PrepareChangePin1MSG(a);
            // String  messageIn = PrepareChangePin1MSG(a);
            int messageLen = messageIn.length();
            String St2[] = messageIn.split("#");
            idSeq = St2[0];
            AvaCas.sendBytes(S);

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
        String S=new String(St3.getBytes());
        authenticatePin1.setActionCode(getActionCode(S));
        authenticatePin1.setIsSuccessfulConnectToGateway(true);


        return authenticatePin1;
    }



    private byte[] PreparedAuthenticateMSG(AuthenticatePin1 authenticatePin1OBJ) throws Exception {
        Random random = new Random();

        String MessageStr=
                String.valueOf(random.nextInt(9999 - 1 + 1) + 1)+
                        String.valueOf(random.nextInt(9999 - 1 + 1) + 1)+
                        "#2#"+
                        authenticatePin1OBJ.getPin()+
                        "#1#"+
                        authenticatePin1OBJ.getAccountNumber()+
                        "#";
        // String P1="000"+String.valueOf(MessageStr.length());
        // String P2=P1.substring(P1.length()-3);
        //  MessageStr=P2+MessageStr;
        //  P1=null;
        //  P2=null;
        messageIn=MessageStr;
        try{
            return CodecUtil.encrypt(MessageStr, "2#", PropertiesUtil.getKeyOrm());
        }catch (Exception e){
            return null;
        }



    }
    private byte[] PrepareChangePin1MSG(AuthenticatePin1 authenticatePin1OBJ) throws Exception {
        Random random = new Random();
        String RandNO=String.valueOf(random.nextInt(9999 - 1 + 1) + 1)+
                String.valueOf(random.nextInt(9999 - 1 + 1) + 1);
        String MessageStr=  RandNO+
                "#1#"+
                authenticatePin1OBJ.getPin()+
                "#"+
                authenticatePin1OBJ.getPin_New()+
                "#1#"+
                authenticatePin1OBJ.getAccountNumber()+
                "#";
       /* String P1="000"+String.valueOf(MessageStr.length());
        String P2=P1.substring(P1.length()-3);
        MessageStr=P2+MessageStr;
        P1=null;
        P2=null;*/
        messageIn=MessageStr;
        try{
            return CodecUtil.encrypt(MessageStr, "2#", PropertiesUtil.getKeyOrm());
        }catch (Exception e){
            return null;
        }



    }


    private String PrepareAuthenticatePin1MSG(AuthenticatePin1 authenticatePin1OBJ) throws Exception {
        Random random = new Random();
        String RandNO=String.valueOf(random.nextInt(9999 - 1 + 1) + 1)+
                String.valueOf(random.nextInt(9999 - 1 + 1) + 1);
        String MessageStr=  RandNO+
                "#2#"+
                authenticatePin1OBJ.getPin()+
                "#1#"+
                authenticatePin1OBJ.getAccountNumber()+
                "#";
       /* String P1="000"+String.valueOf(MessageStr.length());
        String P2=P1.substring(P1.length()-3);
        MessageStr=P2+MessageStr;
        P1=null;
        P2=null;*/
        try{
            return MessageStr;
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


}
