package Model_Produto;

public class Produto {

    private Integer id;
    private String nome;
    private Integer quantidade;
    private Double preco;

    public Produto() {
    }

    public Produto(String nome, Integer quantidade, Double preco) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public Produto(Integer id, String nome, Integer quantidade, Double preco) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "id=" + id + ", nome=" + nome + ", quantidade=" + quantidade + ", preco=" + preco;
    }

}