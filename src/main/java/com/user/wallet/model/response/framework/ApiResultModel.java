package com.user.wallet.model.response.framework;

import lombok.Data;

@Data
public class ApiResultModel<T> {

    public String message;
    public boolean error;
    public T response;
    public String responseCode;


    public ApiResultModel(boolean error, String message)
    {
        this.error = error;
        this.message = message;
    }

    public ApiResultModel(boolean error, String message, T response)
    {
        this.error = error;
        this.message = message;
        this.response = response;
    }

    public ApiResultModel(boolean error, String message, T response, String responseCode)
    {
        this.error = error;
        this.message = message;
        this.response = response;
        this.responseCode = responseCode;
    }
}

