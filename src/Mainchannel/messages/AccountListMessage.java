package Mainchannel.messages;

public class AccountListMessage extends BaseMessage {
    protected String accountNumber;
    protected String accountType;
    protected String accountcurrency;
    protected String accountStatus;
    protected String accountHost;
    protected String accountBranchID;
    protected String accountBranchName;
    protected String latinName;
    protected String latinFamily;
    protected String farsiName;
    protected String farsiFamily;
    protected String nationalCode;
    protected String birthDate;
    protected String birthPlace;
    protected String address1;
    protected String address2;
    protected String homePhone;
    protected String officePhone;
    protected String celPhone;
    protected String fatherName;
    protected String creationDateTime;
    protected String prsnType;

    public AccountListMessage(String accountNumber, String accountType, String accountcurrency, String accountStatus, String accountHost, String accountBranchID, String latinName, String latinFamily, String farsiName, String farsiFamily, String nationalCode, String birthDate, String birthPlace, String address1, String address2, String homePhone, String officePhone, String celPhone, String fatherName, String creationDateTime) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountcurrency = accountcurrency;
        this.accountStatus = accountStatus;
        this.accountHost = accountHost;
        this.accountBranchID = accountBranchID;
        this.latinName = latinName;
        this.latinFamily = latinFamily;
        this.farsiName = farsiName;
        this.farsiFamily = farsiFamily;
        this.nationalCode = nationalCode;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.address1 = address1;
        this.address2 = address2;
        this.homePhone = homePhone;
        this.officePhone = officePhone;
        this.celPhone = celPhone;
        this.fatherName = fatherName;
        this.creationDateTime = creationDateTime;
    }

    public AccountListMessage(String accountNumber, String accountType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    public AccountListMessage(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountListMessage() {
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public String getAccountcurrency() {
        return this.accountcurrency;
    }

    public void setAccountcurrency(String accountcurrency) {
        this.accountcurrency = accountcurrency;
    }

    public String getAccountStatus() {
        return this.accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountHost() {
        return this.accountHost;
    }

    public void setAccountHost(String accountHost) {
        this.accountHost = accountHost;
    }

    public String getAccountBranchID() {
        return this.accountBranchID;
    }

    public void setAccountBranchID(String accountBranchID) {
        this.accountBranchID = accountBranchID;
    }

    public String getLatinName() {
        return this.latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getLatinFamily() {
        return this.latinFamily;
    }

    public void setLatinFamily(String latinFamily) {
        this.latinFamily = latinFamily;
    }

    public String getFarsiName() {
        return this.farsiName;
    }

    public void setFarsiName(String farsiName) {
        this.farsiName = farsiName;
    }

    public String getFarsiFamily() {
        return this.farsiFamily;
    }

    public void setFarsiFamily(String farsiFamily) {
        this.farsiFamily = farsiFamily;
    }

    public String getNationalCode() {
        return this.nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getHomePhone() {
        return this.homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getOfficePhone() {
        return this.officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getCelPhone() {
        return this.celPhone;
    }

    public void setCelPhone(String celPhone) {
        this.celPhone = celPhone;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getCreationDateTime() {
        return this.creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getPrsnType() {
        return this.prsnType;
    }



    public void setPrsnType(String prsnType) {
        this.prsnType = prsnType;
    }
}

