package com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput;

public class popupinput {
    private String UtilitiesTitle;
    private String UtilitiesSupplier;
    private String User;
    private String ContractExpiryDate;
    private String IsPaid;

    public popupinput(String utilitiesTitle, String utilitiesSupplier,String user,String contractExpiryDate,String isPaid) {
        User = user;
        UtilitiesTitle = utilitiesTitle;
        UtilitiesSupplier = utilitiesSupplier;
        ContractExpiryDate = contractExpiryDate;
        IsPaid = isPaid;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getUtilitiesTitle() {
        return UtilitiesTitle;
    }

    public void setUtilitiesTitle(String utilitiesTitle) {
        UtilitiesTitle = utilitiesTitle;
    }

    public String getUtilitiesSupplier() {
        return UtilitiesSupplier;
    }

    public void setUtilitiesSupplier(String utilitiesSupplier) {
        UtilitiesSupplier = utilitiesSupplier;
    }

    public String getContractExpiryDate() {
        return ContractExpiryDate;
    }

    public void setContractExpiryDate(String contractExpiryDate) {
        ContractExpiryDate = contractExpiryDate;
    }

    public String getIsPaid() {
        return IsPaid;
    }

    public void setIsPaid(String isPaid) {
        IsPaid = isPaid;
    }
}
