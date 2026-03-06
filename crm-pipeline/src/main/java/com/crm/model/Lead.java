package com.crm.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Lead {
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String empresa;
    private double valor;
    private String prioridade;
    private String estagio;
    private String notas;
    private String dataCriacao;

    public Lead() {
        this.id = UUID.randomUUID().toString();
        this.dataCriacao = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.prioridade = "MEDIA";
        this.valor = 0;
    }

    public Lead(String nome, String email, String telefone, String empresa,
                double valor, String prioridade, String estagio, String notas) {
        this();
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.empresa = empresa;
        this.valor = valor;
        this.prioridade = prioridade;
        this.estagio = estagio;
        this.notas = notas;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    public String getEstagio() { return estagio; }
    public void setEstagio(String estagio) { this.estagio = estagio; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    @Override
    public String toString() { return nome != null ? nome : ""; }
}