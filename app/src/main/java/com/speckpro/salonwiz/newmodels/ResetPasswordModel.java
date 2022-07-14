package com.speckpro.salonwiz.newmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResetPasswordModel implements Serializable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("original")
    @Expose
    private Original original;
    @SerializedName("exception")
    @Expose
    private Object exception;
    private final static long serialVersionUID = -1574441368158350909L;

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


    public class Headers implements Serializable {

        private final static long serialVersionUID = 4897281336715949450L;

    }

    public class Original implements Serializable {

        @SerializedName("message")
        @Expose
        private String message;
        private final static long serialVersionUID = -3636461205973118537L;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

}