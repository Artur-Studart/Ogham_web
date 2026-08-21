package model;

public class Document {
    private int id;
    private String titulo;
    private String autor;
    private String descricao;
    private String tipo;
    private String data;
    private String arquivoPath;
    private String tags;

    public Document() {}

    public Document(int id, String titulo, String autor, String descricao, String tipo, String data, String arquivoPath, String tags) {
        this.id = id; this.titulo = titulo; this.autor = autor; this.descricao = descricao;
        this.tipo = tipo; this.data = data; this.arquivoPath = arquivoPath; this.tags = tags;
    }

    // getters and setters
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; } public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; } public void setAutor(String autor) { this.autor = autor; }
    public String getDescricao() { return descricao; } public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getTipo() { return tipo; } public void setTipo(String tipo) { this.tipo = tipo; }
    public String getData() { return data; } public void setData(String data) { this.data = data; }
    public String getArquivoPath() { return arquivoPath; } public void setArquivoPath(String arquivoPath) { this.arquivoPath = arquivoPath; }
    public String getTags() { return tags; } public void setTags(String tags) { this.tags = tags; }

    @Override
    public String toString() {
        return id + " | " + titulo + " | " + autor + " | " + tipo + " | " + data;
    }
}
