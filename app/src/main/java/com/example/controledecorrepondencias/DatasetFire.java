package com.example.controledecorrepondencias;

import java.security.Key;

public class DatasetFire {

    String apto;
    String tipo;
    String codigo;
    String discriminacao;
    String destinatario;
    String status;
    String dataentrega;
    String quemrecebeu;
    String dataregistro;
    String usuarioregistro;
    String usuarioBaixou;
    String imageAsiinatura;


    public DatasetFire(String apto, String tipo, String codigo, String discriminacao, String destinatario,String status,String dataentrega,String quemrecebeu ,String dataregistro,String usuarioregistro,String usuarioBaixou,    String imageAsiinatura
    ) {
        this.apto = apto;
        this.tipo = tipo;
        this.codigo = codigo;
        this.discriminacao = discriminacao;
        this.destinatario = destinatario;
        this.status=status;
        this.dataentrega=dataentrega;
        this.quemrecebeu=quemrecebeu;
        this.dataregistro=dataregistro;
        this.usuarioBaixou=usuarioBaixou;
        this.usuarioregistro=usuarioregistro;
        this.imageAsiinatura=imageAsiinatura;
    }



    public String getImageAsiinatura() {
        return imageAsiinatura;
    }

    public void setImageAsiinatura(String imageAsiinatura) {
        this.imageAsiinatura = imageAsiinatura;
    }

    public String getUsuarioregistro() {
        return usuarioregistro;
    }

    public void setUsuarioregistro(String usuarioregistro) {
        this.usuarioregistro = usuarioregistro;
    }

    public String getUsuarioBaixou() {
        return usuarioBaixou;
    }

    public void setUsuarioBaixou(String usuarioBaixou) {
        this.usuarioBaixou = usuarioBaixou;
    }

    public String getDataregistro() {
        return dataregistro;
    }

    public void setDataregistro(String dataregistro) {
        this.dataregistro = dataregistro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataentrega() {
        return dataentrega;
    }

    public void setDataentrega(String dataentrega) {
        this.dataentrega = dataentrega;
    }

    public String getQuemrecebeu() {
        return quemrecebeu;
    }

    public void setQuemrecebeu(String quemrecebeu) {
        this.quemrecebeu = quemrecebeu;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDiscriminacao() {
        return discriminacao;
    }

    public void setDiscriminacao(String discriminacao) {
        this.discriminacao = discriminacao;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public DatasetFire() {
    }
}
