package com.example.playlistmanager.common;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private String arg;
    private Object data;

    public Request(RequestType type, String arg, Object data) {
        this.type = type;
        this.arg = arg;
        this.data = data;
    }

    public Request(RequestType type, String arg) {
        this(type, arg, null);
    }

    public Request(RequestType type) {
        this(type, null);
    }

    public RequestType getType() {
        return type;
    }

    public String getArg() {
        return arg;
    }

    public Object getData() {
        return data;
    }
}
