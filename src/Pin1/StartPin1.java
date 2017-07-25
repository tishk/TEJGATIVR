package Pin1;

/**
 * Created by Administrator on 15/05/2015.
 */
import Pin1.currentMethod.*;
import com.ibm.mq.jms.MQQueueConnectionFactory;

import javax.jms.*;

public class StartPin1 {
public StartPin1(){
      try {
         new StartCurrentMethod();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
