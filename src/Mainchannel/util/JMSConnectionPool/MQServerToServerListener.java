package Mainchannel.util.JMSConnectionPool;

import Pin1.currentMethod.Avacas.AvaCas;
import Pin1.currentMethod.Avacas.AvaCasThread;
import Pin1.currentMethod.util.CodecUtil;
import com.ibm.disthub2.client.Message;

import javax.jms.BytesMessage;
import javax.jms.MessageListener;

/**
 * Created by Administrator on 8/17/2015.
 */
public class MQServerToServerListener implements MessageListener{
    @Override
    public void onMessage(javax.jms.Message message) {
        try {
            BytesMessage byteMessage = (BytesMessage) message;
            byte receivedBytes[] = new byte[830];
            int len = byteMessage.readBytes(receivedBytes);
            byte encmsg[] = new byte[len];
            System.arraycopy(receivedBytes, 0, encmsg, 0, len);
            String receivedStr = CodecUtil.decrypt(encmsg, Pin1.currentMethod.Avacas.AvaCas.getKEY_PAIR_PATH());
            String receivedArray[] = receivedStr.split("#");
            String id = receivedArray[0];
            AvaCasThread avaCasThread = AvaCas.getAvaCasThread(id);
            if (avaCasThread != null) {
                avaCasThread.setResponse(receivedStr.getBytes());
                String idSeq = avaCasThread.getIdSeq();
                synchronized (idSeq) {
                    idSeq.notify();
                }
            }
        } catch (Exception e) {

        }

    }
}
