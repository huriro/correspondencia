package com.example.controledecorrepondencias;

public class Controleleituraaptos {

    String data;
    String consumo;
    String leitura;
    String apto;
    String leiturista;
    String control;

    public Controleleituraaptos() {
    }

    public Controleleituraaptos(String control,String data, String consumo, String leitura, String apto, String leiturista) {
        this.data = data;
        this.consumo = consumo;
        this.leitura = leitura;
        this.apto = apto;
        this.leiturista = leiturista;
        this.control=control;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getConsumo() {
        return consumo;
    }

    public void setConsumo(String consumo) {
        this.consumo = consumo;
    }

    public String getLeitura() {
        return leitura;
    }

    public void setLeitura(String leitura) {
        this.leitura = leitura;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getLeiturista() {
        return leiturista;
    }

    public void setLeiturista(String leiturista) {
        this.leiturista = leiturista;
    }
}

