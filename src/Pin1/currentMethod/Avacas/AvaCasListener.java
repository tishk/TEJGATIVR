package Pin1.currentMethod.Avacas;

import Pin1.currentMethod.util.CodecUtil;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by Administrator on 28/05/2015.
 */
public class AvaCasListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            BytesMessage byteMessage = (BytesMessage) message;
            byte receivedBytes[] = new byte[830];
            int len = byteMessage.readBytes(receivedBytes);
            byte encmsg[] = new byte[len];
            System.arraycopy(receivedBytes, 0, encmsg, 0, len);
            String receivedStr = CodecUtil.decrypt(encmsg, Pin1.currentMethod.Avacas.AvaCas.getKEY_PAIR_PATH());
            System.out.println("Recive String from sabaPardazesh : "+receivedStr);
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
