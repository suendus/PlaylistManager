package com.example.playlistmanager.common;
import java.io.Serializable;


public class Response<T> implements Serializable {
    private ResponseType type;
    private String message;
    private T data;

    public Response(ResponseType type, String message, T data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }
    public String toString() {
        return "Type: " + type + ", Message: " + message;
    }

    public ResponseType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
