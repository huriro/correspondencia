package com.example.controledecorrepondencias;

public class Controlereservas {
    String tiporeserva;
    String Dtareserva;
    String DtaEvento;
    String ResponsReserva;
    String usuarioQReservou;
    String usuarioQbaixou;
    String idReserva;
    String status;
    String aptoReserva;

    public Controlereservas() {
    }

    public Controlereservas(String tiporeserva, String dtareserva, String dtaEvento, String responsReserva, String usuarioQReservou, String usuarioQbaixou, String idReserva,String aptoReserva,String status) {
        this.tiporeserva = tiporeserva;
        this.Dtareserva = dtareserva;
        this.DtaEvento = dtaEvento;
        this.ResponsReserva = responsReserva;
        this.usuarioQReservou = usuarioQReservou;
        this.usuarioQbaixou = usuarioQbaixou;
        this.idReserva = idReserva;
        this.aptoReserva=aptoReserva;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAptoReserva() {
        return aptoReserva;
    }

    public void setAptoReserva(String aptoReserva) {
        this.aptoReserva = aptoReserva;
    }

    public String getTiporeserva() {
        return tiporeserva;
    }

    public void setTiporeserva(String tiporeserva) {
        this.tiporeserva = tiporeserva;
    }

    public String getDtareserva() {
        return Dtareserva;
    }

    public void setDtareserva(String dtareserva) {
        Dtareserva = dtareserva;
    }

    public String getDtaEvento() {
        return DtaEvento;
    }

    public void setDtaEvento(String dtaEvento) {
        DtaEvento = dtaEvento;
    }

    public String getResponsReserva() {
        return ResponsReserva;
    }

    public void setResponsReserva(String responsReserva) {
        ResponsReserva = responsReserva;
    }

    public String getUsuarioQReservou() {
        return usuarioQReservou;
    }

    public void setUsuarioQReservou(String usuarioQReservou) {
        this.usuarioQReservou = usuarioQReservou;
    }

    public String getUsuarioQbaixou() {
        return usuarioQbaixou;
    }

    public void setUsuarioQbaixou(String usuarioQbaixou) {
        this.usuarioQbaixou = usuarioQbaixou;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }
}
