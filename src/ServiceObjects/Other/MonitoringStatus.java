package ServiceObjects.Other;

import ServiceObjects.Account.BaseAccountRequest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 7/21/17.
 */
public class MonitoringStatus extends BaseAccountRequest {

    private Timestamp reportTime;
    private String lastFundTransferTime ;
    private String lastBillPaymentTime;
    Map<String,String> BranchesStatus=new HashMap<String,String>();

    public String getLastFundTransferTime() {
        return lastFundTransferTime;
    }

    public void setLastFundTransferTime(String lastFundTransferTime) {
        this.lastFundTransferTime = lastFundTransferTime;
    }

    public String getLastBillPaymentTime() {
        return lastBillPaymentTime;
    }

    public void setLastBillPaymentTime(String lastBillPaymentTime) {
        this.lastBillPaymentTime = lastBillPaymentTime;
    }

    public Map<String, String> getBranchesStatus() {
        return BranchesStatus;
    }

    public void setBranchesStatus(Map<String, String> branchesStatus) {
        BranchesStatus = branchesStatus;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }

    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }
}
