package br.senac.ogham.model;

public record Document(Integer id, String titulo, String autor, String descricao, String tipo,
                       String data, String arquivoPath, String tags) {}
