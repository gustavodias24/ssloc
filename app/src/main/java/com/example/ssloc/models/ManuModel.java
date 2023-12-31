package com.example.ssloc.models;

import android.net.Uri;

import java.io.Serializable;

public class ManuModel{
    String data, login;

    public ManuModel() {
    }

    public ManuModel(String data, String login, int status, String imageTroca, String imageRecibo) {
        this.data = data;
        this.login = login;
        this.status = status;
        this.imageTroca = imageTroca;
        this.imageRecibo = imageRecibo;
    }

    int status;
    String imageTroca, imageRecibo;

    public ManuModel(String login) {
        this.login = login;
    }

    public String getData() {
        return data;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImageTroca() {
        return imageTroca;
    }

    public void setImageTroca(String imageTroca) {
        this.imageTroca = imageTroca;
    }

    public String getImageRecibo() {
        return imageRecibo;
    }

    public void setImageRecibo(String imageRecibo) {
        this.imageRecibo = imageRecibo;
    }
}
