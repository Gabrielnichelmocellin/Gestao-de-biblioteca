package biblioteca.model;

public class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String tipo;
    private String email;

    public Usuario() {} 

    public Usuario(String nome, String cpf, String tipo, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.tipo = tipo;
        this.email = email;
    }

  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}