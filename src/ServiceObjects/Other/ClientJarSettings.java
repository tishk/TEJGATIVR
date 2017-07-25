package ServiceObjects.Other;

import java.io.Serializable;

/**
 * Created by Administrator on 11/14/2016.
 */
public class ClientJarSettings implements Serializable {
    private static final long serialVersionUID = 7973779757872787170L;

    private boolean useOldGateway=false;
    private boolean useOldAuthenticate=false;
    private boolean useOldPayment=false;
    private boolean useOldTelSwitch=false;
    private String  actionCode;


    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public boolean isUseOldGateway() {
        return useOldGateway;
    }

    public void setUseOldGateway(boolean useOldGateway) {
        this.useOldGateway = useOldGateway;
    }

    public boolean isUseOldAuthenticate() {
        return useOldAuthenticate;
    }

    public void setUseOldAuthenticate(boolean useOldAuthenticate) {
        this.useOldAuthenticate = useOldAuthenticate;
    }

    public boolean isUseOldPayment() {
        return useOldPayment;
    }

    public void setUseOldPayment(boolean useOldPayment) {
        this.useOldPayment = useOldPayment;
    }

    public boolean isUseOldTelSwitch() {
        return useOldTelSwitch;
    }

    public void setUseOldTelSwitch(boolean useOldTelSwitch) {
        this.useOldTelSwitch = useOldTelSwitch;
    }
}
