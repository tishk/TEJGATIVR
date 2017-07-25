import javax.swing.*;

/**
 * Created by Administrator on 1/5/2016.
 */
public class Main {
    private   JPanel panel1;
    static GatewayRMI gatewayRMI=null;
    public  static void main(String args[])throws Exception {

        gatewayRMI=new GatewayRMI(args);
    }

}
