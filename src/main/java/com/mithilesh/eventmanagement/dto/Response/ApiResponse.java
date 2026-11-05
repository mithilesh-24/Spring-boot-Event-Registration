package com.mithilesh.eventmanagement.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    /**
     * Used when we want to send the data
     *
     * @param status code
     * @param message to say what happend
     * @param data contain body of response data
     */
    public ApiResponse(int status,String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Used when we don't want to send the data
     *
     *  @param status code
     *  @param message to say what happend
     */
    public ApiResponse(int status,String message){
        this.status = status;
        this.message = message;
    }
}
