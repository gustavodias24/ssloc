package com.example.ssloc.models;

public class MsgModel {
    String msg;
    Boolean error;

    public MsgModel(String msg, Boolean error) {
        this.msg = msg;
        this.error = error;
    }

    public MsgModel() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
