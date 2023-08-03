package com.example.ssloc.models;

import android.net.Uri;

public class UsuarioModel {
    String login, email, telefone, validadeCNH, senha = "";
    CepModel cep;
    Uri fotoCNH, fotoComprovante;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsuarioModel() {
    }

    public UsuarioModel(String login, String email, String telefone, String validadeCNH, String senha, CepModel cep, Uri fotoCNH, Uri fotoComprovante) {
        this.login = login;
        this.email = email;
        this.telefone = telefone;
        this.validadeCNH = validadeCNH;
        this.senha = senha;
        this.cep = cep;
        this.fotoCNH = fotoCNH;
        this.fotoComprovante = fotoComprovante;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getValidadeCNH() {
        return validadeCNH;
    }

    public void setValidadeCNH(String validadeCNH) {
        this.validadeCNH = validadeCNH;
    }

    public CepModel getCep() {
        return cep;
    }

    public void setCep(CepModel cep) {
        this.cep = cep;
    }

    public Uri getFotoCNH() {
        return fotoCNH;
    }

    public void setFotoCNH(Uri fotoCNH) {
        this.fotoCNH = fotoCNH;
    }

    public Uri getFotoComprovante() {
        return fotoComprovante;
    }

    public void setFotoComprovante(Uri fotoComprovante) {
        this.fotoComprovante = fotoComprovante;
    }
}
