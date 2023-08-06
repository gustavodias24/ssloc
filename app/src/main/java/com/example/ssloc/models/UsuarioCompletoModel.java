package com.example.ssloc.models;


public class UsuarioCompletoModel{
    UsuarioModel usuario;
    PlanoModel plano;
    ManuModel manu;

    public UsuarioModel getUsuarioModel() {
        return usuario;
    }

    public void setUsuarioModel(UsuarioModel usuarioModel) {
        this.usuario = usuarioModel;
    }

    public PlanoModel getPlano() {
        return plano;
    }

    public void setPlano(PlanoModel plano) {
        this.plano = plano;
    }

    public ManuModel getManu() {
        return manu;
    }

    public void setManu(ManuModel manu) {
        this.manu = manu;
    }

}
