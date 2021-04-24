package com.example.controledecorrepondencias;

public class DatasetUsuario {
    String id;
    String nome;
    String imagem;
    String Email;
    String funcao;
    String telefone;
    String condominios;
    String Senha;

    public DatasetUsuario() {
    }

    public DatasetUsuario(String id, String nome, String imagem, String email, String funcao, String telefone,String condominios,String Senha) {
        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
        this.Email = email;
        this.funcao = funcao;
        this.telefone = telefone;
        this.condominios=condominios;
        this.Senha=Senha;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public String getCondominios() {
        return condominios;
    }

    public void setCondominios(String condominios) {
        this.condominios = condominios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
