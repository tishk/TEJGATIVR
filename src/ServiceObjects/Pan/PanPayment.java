package ServiceObjects.Pan;

/**
 * Created by Administrator on 2/2/2017.
 */
public class PanPayment {

    boolean isRegistered=false;
    boolean hasAlreadyBeenPaid;
    boolean subServiceNotDefined;
    boolean dataFromDbChashed;


    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isHasAlreadyBeenPaid() {
        return hasAlreadyBeenPaid;
    }

    public void setHasAlreadyBeenPaid(boolean hasAlreadyBeenPaid) {
        this.hasAlreadyBeenPaid = hasAlreadyBeenPaid;
    }

    public boolean isSubServiceNotDefined() {
        return subServiceNotDefined;
    }

    public void setSubServiceNotDefined(boolean subServiceNotDefined) {
        this.subServiceNotDefined = subServiceNotDefined;
    }

    public boolean isDataFromDbChashed() {
        return dataFromDbChashed;
    }

    public void setDataFromDbChashed(boolean dataFromDbChashed) {
        this.dataFromDbChashed = dataFromDbChashed;
    }
}
