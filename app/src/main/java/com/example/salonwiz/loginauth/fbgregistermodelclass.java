package com.example.salonwiz.loginauth;

public class fbgregistermodelclass {

    // string variables for our name and job
    private String FirstName;
    private String LastName;
    private String Email;
    private String ContactNumber;
    private String BusinessName;
    private String BusinessAddress;
    private String PostCode;

    public fbgregistermodelclass(String FirstName, String LastName,String Email,String ContactNumber,String BusinessName,String BusinessAddress,String PostCode) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.ContactNumber = ContactNumber;
        this.BusinessName = BusinessName;
        this.BusinessAddress = BusinessAddress;
        this.PostCode = PostCode;
    }

    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }
    public String getEmail() {
        return Email;
    }
    public String getContactNumber() {
        return ContactNumber;
    }
    public String getBusinessName() {
        return BusinessName;
    }
    public String getBusinessAddress() {
        return BusinessAddress;
    }
    public String getPostCode() {
        return PostCode;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }
    public void setEmail(String Email) {
        this.Email = Email;
    }
    public void setContactNumber(String ContactNumber) {
        this.ContactNumber =ContactNumber;
    }
    public void setBusinessName(String BusinessName) {
        this.BusinessName = BusinessName;
    }
    public void setBusinessAddress(String BusinessAddress) {
        this.BusinessAddress = BusinessAddress;
    }
    public void setPostCode(String PostCode) {
        this.PostCode = PostCode;
    }
}
