package com.example.ssloc.models;

import android.net.Uri;

public class ManuModel {
    String data, login;

    public ManuModel(String data, String login, int status, Uri imageTroca, Uri imageRecibo) {
        this.data = data;
        this.login = login;
        this.status = status;
        this.imageTroca = imageTroca;
        this.imageRecibo = imageRecibo;
    }

    int status;
    Uri imageTroca, imageRecibo;

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

    public Uri getImageTroca() {
        return imageTroca;
    }

    public void setImageTroca(Uri imageTroca) {
        this.imageTroca = imageTroca;
    }

    public Uri getImageRecibo() {
        return imageRecibo;
    }

    public void setImageRecibo(Uri imageRecibo) {
        this.imageRecibo = imageRecibo;
    }
}
