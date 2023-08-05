package com.example.ssloc.models;

import android.content.SharedPreferences;
import android.net.Uri;

import java.io.Serializable;

public class UsuarioModel{
    String login, email, telefone, validadeCNH, senha = "";
    CepModel cep;
    Uri fotoCNH, fotoComprovante;

    int numeroCasa;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsuarioModel(String login) {
        this.login = login;
    }

    public UsuarioModel() {
    }

    public UsuarioModel(String login, String email, String telefone, String validadeCNH, String senha, CepModel cep, Uri fotoCNH, Uri fotoComprovante, int numeroCasa) {
        this.login = login;
        this.email = email;
        this.telefone = telefone;
        this.validadeCNH = validadeCNH;
        this.senha = senha;
        this.cep = cep;
        this.fotoCNH = fotoCNH;
        this.fotoComprovante = fotoComprovante;
        this.numeroCasa = numeroCasa;
    }

    public String getLogin() {
        return login;
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int numeroCasa) {
        this.numeroCasa = numeroCasa;
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
