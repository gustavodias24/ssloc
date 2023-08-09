package com.example.ssloc.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

public class PlanoModel{
    String msg,login;
    Boolean ativo, error;
    int tipo;
    String data;
    String comprovantePagamento;

    public PlanoModel(String login, int tipo) {
        this.login = login;
        this.tipo = tipo;
    }

    public PlanoModel(String msg, String login, Boolean ativo, Boolean error, int tipo, String data, String comprovantePagamento) {
        this.msg = msg;
        this.login = login;
        this.ativo = ativo;
        this.error = error;
        this.tipo = tipo;
        this.data = data;
        this.comprovantePagamento = comprovantePagamento;
    }

    public PlanoModel() {
    }

    public String getMsg() {
        return msg;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getComprovantePagamento() {
        return comprovantePagamento;
    }

    public void setComprovantePagamento(String comprovantePagamento) {
        this.comprovantePagamento = comprovantePagamento;
    }
}
