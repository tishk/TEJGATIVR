package Pin2.currentMethod.ORM;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ORMListener
        implements MessageListener
{

    public ORMListener()
    {
    }

    public void onMessage(Message message)
    {
        try
        {
        //  222-hoghoghi  legal customer  223-moshtrak
            BytesMessage byteMessage = (BytesMessage)message;
            byte receivedBytes[] = new byte[830];
            int len = byteMessage.readBytes(receivedBytes);
            byte encmsg[] = new byte[len];
            System.arraycopy(receivedBytes, 0, encmsg, 0, len);
            ORMEncrypter encryptThread = new ORMEncrypter();
            encryptThread.setMsgBytes(encmsg);
            encryptThread.start();

        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("sss : ").append(e).toString());
        }
    }
}