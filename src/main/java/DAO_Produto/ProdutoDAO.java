package DAO_Produto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model_Produto.Produto;

public class ProdutoDAO {

    private Connection conexao;

    public ProdutoDAO() {
        try {
            String url = "jdbc:derby:C:./NEIAESTOQUEBD;create=true";
            conexao = DriverManager.getConnection(url);
            System.out.println("Banco de dados conectado.");
            criarTabela();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void criarTabela() {
        String sql = "CREATE TABLE APP.PRODUTOS ("
                   + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                   + "NOME VARCHAR(100), "
                   + "QUANTIDADE INT, "
                   + "PRECO DECIMAL(10, 2))";
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.execute();
            System.out.println("Tabela PRODUTOS criada com sucesso.");
        } catch (SQLException e) {
        if (e.getErrorCode() == 20000) {
            System.out.println("Tabela PRODUTOS já existe.");
        } else {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        }
    }

    public List<Produto> listar() {
        String sql = "SELECT * FROM APP.PRODUTOS";
        List<Produto> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Produto produto = new Produto();
                produto.setId(resultado.getInt("ID"));
                produto.setNome(resultado.getString("NOME"));
                produto.setQuantidade(resultado.getInt("QUANTIDADE"));
                produto.setPreco(resultado.getDouble("PRECO"));
                retorno.add(produto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO APP.PRODUTOS(NOME, QUANTIDADE, PRECO) VALUES(?,?,?)";
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Produto produto) {
        String sql = "UPDATE APP.PRODUTOS SET NOME=?, QUANTIDADE=?, PRECO=? WHERE ID=?";
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Integer id) {
        String sql = "DELETE FROM APP.PRODUTOS WHERE ID=?";
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão com o banco de dados encerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}