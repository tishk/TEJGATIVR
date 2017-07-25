package Pin2;

import Pin2.currentMethod.StartCurrentMethod;


public class StartPin2 {
    public StartPin2(){
        try {
            new StartCurrentMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
