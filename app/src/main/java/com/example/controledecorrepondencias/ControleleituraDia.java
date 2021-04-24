package com.example.controledecorrepondencias;

public class ControleleituraDia {
    String dataAgua;
    String dataenergia;
    String leituraAgua;
    String leituraEnergia;
    String consumoAgua;
    String consumoEnergia;
    String status;
    String usuario;
    String datames;
   public ControleleituraDia(){}
    public ControleleituraDia(String datames,String usuario,String dataAgua, String dataenergia, String leituraAgua, String leituraEnergia, String consumoAgua, String consumoEnergia,String status) {
        this.dataAgua = dataAgua;
        this.dataenergia = dataenergia;
        this.leituraAgua = leituraAgua;
        this.leituraEnergia = leituraEnergia;
        this.consumoAgua = consumoAgua;
        this.consumoEnergia = consumoEnergia;
        this.status=status;
        this.usuario=usuario;
        this.datames=datames;
    }

    public String getDatames() {
        return datames;
    }

    public void setDatames(String datames) {
        this.datames = datames;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataAgua() {
        return dataAgua;
    }

    public void setDataAgua(String dataAgua) {
        this.dataAgua = dataAgua;
    }

    public String getDataenergia() {
        return dataenergia;
    }

    public void setDataenergia(String dataenergia) {
        this.dataenergia = dataenergia;
    }

    public String getLeituraAgua() {
        return leituraAgua;
    }

    public void setLeituraAgua(String leituraAgua) {
        this.leituraAgua = leituraAgua;
    }

    public String getLeituraEnergia() {
        return leituraEnergia;
    }

    public void setLeituraEnergia(String leituraEnergia) {
        this.leituraEnergia = leituraEnergia;
    }

    public String getConsumoAgua() {
        return consumoAgua;
    }

    public void setConsumoAgua(String consumoAgua) {
        this.consumoAgua = consumoAgua;
    }

    public String getConsumoEnergia() {
        return consumoEnergia;
    }

    public void setConsumoEnergia(String consumoEnergia) {
        this.consumoEnergia = consumoEnergia;
    }
}



