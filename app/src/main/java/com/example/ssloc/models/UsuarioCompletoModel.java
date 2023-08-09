package com.example.ssloc.models;


public class UsuarioCompletoModel{
    Boolean templano,temmanu;

    public Boolean getTemplano() {
        return templano;
    }

    public Boolean getTemmanu() {
        return temmanu;
    }

    public UsuarioModel usuario;
    public PlanoModel plano = new PlanoModel();
    public ManuModel manu = new ManuModel();

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
