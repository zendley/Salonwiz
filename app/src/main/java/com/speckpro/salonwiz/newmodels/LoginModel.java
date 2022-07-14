package com.speckpro.salonwiz.newmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginModel implements Serializable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("original")
    @Expose
    private Original original;
    @SerializedName("exception")
    @Expose
    private Object exception;
    private final static long serialVersionUID = -7481294511825708881L;

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }


    public class Data implements Serializable {

        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("user")
        @Expose
        private User user;
        private final static long serialVersionUID = -4832428197679938103L;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

    public class Headers implements Serializable {

        private final static long serialVersionUID = 4897281336715949450L;

    }

    public class Original implements Serializable {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private Object message;
        @SerializedName("data")
        @Expose
        private Data data;
        private final static long serialVersionUID = -1506806009565061623L;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

    }

    public class User implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("LastName")
        @Expose
        private String lastName;
        @SerializedName("DOB")
        @Expose
        private Object dob;
        @SerializedName("City")
        @Expose
        private Object city;
        @SerializedName("ContactNumber")
        @Expose
        private String contactNumber;
        @SerializedName("BussinessName")
        @Expose
        private String bussinessName;
        @SerializedName("BussinessAddress")
        @Expose
        private String bussinessAddress;
        @SerializedName("PostCode")
        @Expose
        private String postCode;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("email_verified_at")
        @Expose
        private Object emailVerifiedAt;
        @SerializedName("Image")
        @Expose
        private Object image;
        @SerializedName("SocialMedia")
        @Expose
        private Object socialMedia;
        @SerializedName("service")
        @Expose
        private Integer service;
        @SerializedName("package")
        @Expose
        private String _package;
        @SerializedName("serviceStatus")
        @Expose
        private Integer serviceStatus;
        @SerializedName("pdate")
        @Expose
        private String pdate;
        @SerializedName("created_at")
        @Expose
        private Object createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        private final static long serialVersionUID = -7137174174147079065L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getBussinessName() {
            return bussinessName;
        }

        public void setBussinessName(String bussinessName) {
            this.bussinessName = bussinessName;
        }

        public String getBussinessAddress() {
            return bussinessAddress;
        }

        public void setBussinessAddress(String bussinessAddress) {
            this.bussinessAddress = bussinessAddress;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(Object emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }

        public Object getSocialMedia() {
            return socialMedia;
        }

        public void setSocialMedia(Object socialMedia) {
            this.socialMedia = socialMedia;
        }

        public Integer getService() {
            return service;
        }

        public void setService(Integer service) {
            this.service = service;
        }

        public String getPackage() {
            return _package;
        }

        public void setPackage(String _package) {
            this._package = _package;
        }

        public Integer getServiceStatus() {
            return serviceStatus;
        }

        public void setServiceStatus(Integer serviceStatus) {
            this.serviceStatus = serviceStatus;
        }

        public String getPdate() {
            return pdate;
        }

        public void setPdate(String pdate) {
            this.pdate = pdate;
        }

        public Object getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Object createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}