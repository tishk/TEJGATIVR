package Pin1.currentMethod.ORM;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ORMListener
        implements MessageListener
{

    public ORMListener(){
    }

    public void onMessage(Message message){
        try
        {
        //  222-hoghoghi  legal customer  223-moshtrak
            BytesMessage byteMessage = (BytesMessage)message;
            byte receivedBytes[] = new byte[830];
            int len = byteMessage.readBytes(receivedBytes);
            byte temp[] = new byte[len];
            int l=receivedBytes.length;

            System.arraycopy(receivedBytes, 0, temp, 0, len);


            ORMEncrypter encryptThread = new ORMEncrypter(temp);
           // encryptThread.setMsgBytes(encmsg);
            encryptThread.start();

        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("sss : ").append(e).toString());
        }
    }
}