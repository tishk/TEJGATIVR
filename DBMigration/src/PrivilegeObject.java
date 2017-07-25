/**
 * Created by Administrator on 7/12/2016.
 */
public class PrivilegeObject {

    public String fldRegisterationDate=""; // [char](8);
    public String fldCenterCode="";      // [varchar](2);
    public String fldBranchCode="";      // [char](6) NOT NULL,
    public String fldMainAccountNo="";   // [char](10) NOT NULL,
    public String fldNationalCode="";    // [char](10) NULL,
    public String fldPersianName="";     // [varchar](100) NULL,
    public String fldServiceType="";     // [char](10) NULL,
    public String fldFirstAccountNo="";  // [char](10) NULL,
    public String fldSecondAccountNo=""; // [char](10) NULL,
    public String fldThirdAccountNo="";  // [char](10) NULL,
    public String fldFourthAccountNo=""; // [char](10) NUL
    public String fldFifthAccountNo="";  // [char](10) NULL
    public String fldAccountGroup="";    // [char](3) NULL
    public String fldPassword="";    // [char](15) NULL,
    public String fldPrintFlag="";     // [bit] NULL,
    public String IsFreeTransfer="";   // [bit] NOT NULL,
    public String LastChangePin="";  // [char](8) NULL,
    public String RegisteredInCM=""; // [bit] NOT NULL,
    public int Priority=0;          // NOT NULL,
    public String IsActive="";         // [bit] NOT NULL,
    public String OnlineState="";      // [bit] NOT NULL,
    public long OrmMsgID=0; // [bigint] NOT NULL,
    public String MyPriority="";     // [char](5) NOT NULL,
    public String AvaCasActive="";
    public String SendToAvaCas="";
    public String SendToOracle="";

}
