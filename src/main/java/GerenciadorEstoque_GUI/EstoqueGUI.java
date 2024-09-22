package GerenciadorEstoque_GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import DAO_Produto.ProdutoDAO;
import Model_Produto.Produto;

public class EstoqueGUI extends JFrame {

    private ProdutoDAO produtoDAO;
    private DefaultTableModel tableModel;
    private JTable table;

    public EstoqueGUI() {
        produtoDAO = new ProdutoDAO();

        setTitle("Neia Peças Íntimas (Gerenciador de Estoque)");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Quantidade", "Preço(R$)"}, 0);
        table = new JTable(tableModel);
        carregarDadosNaTabela();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAdicionar = new JButton("Adicionar Produto");
        add(btnAdicionar, BorderLayout.SOUTH);

        JButton btnAlterar = new JButton("Alterar Produto");
        add(btnAlterar, BorderLayout.NORTH);

        JButton btnRemover = new JButton("Remover Produto");
        add(btnRemover, BorderLayout.EAST);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioAdicionarProduto();
            }
        });

        btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioAlterarProduto();
            }
        });

        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerProduto();
            }
        });
    }

    private void carregarDadosNaTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = produtoDAO.listar();
        for (Produto produto : produtos) {
            tableModel.addRow(new Object[]{produto.getId(), produto.getNome(), produto.getQuantidade(), produto.getPreco()});
        }
    }

    private void abrirFormularioAdicionarProduto() {
        JDialog dialog = new JDialog(this, "Adicionar Produto", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(4, 2));

        JTextField txtNome = new JTextField();
        JTextField txtQuantidade = new JTextField();
        JTextField txtPreco = new JTextField();

        dialog.add(new JLabel("Nome:"));
        dialog.add(txtNome);

        dialog.add(new JLabel("Quantidade:"));
        dialog.add(txtQuantidade);

        dialog.add(new JLabel("Preço(R$):"));
        dialog.add(txtPreco);

        JButton btnSubmeter = new JButton("Submeter");
        dialog.add(btnSubmeter);

        btnSubmeter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                int quantidade;
                double preco;

                try {
                    quantidade = Integer.parseInt(txtQuantidade.getText());
                    String precoTexto = txtPreco.getText().replace(',', '.');
                    preco = Double.parseDouble(precoTexto);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantidade e Preço devem ser números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Produto novoProduto = new Produto();
                novoProduto.setNome(nome);
                novoProduto.setQuantidade(quantidade);
                novoProduto.setPreco(preco);

                boolean sucesso = produtoDAO.inserir(novoProduto);
                if (sucesso) {
                    JOptionPane.showMessageDialog(dialog, "Produto adicionado com sucesso!");
                    carregarDadosNaTabela();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Falha ao adicionar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void abrirFormularioAlterarProduto() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            Produto produto = produtoDAO.listar().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

            if (produto != null) {
                JDialog dialog = new JDialog(this, "Alterar Produto", true);
                dialog.setSize(300, 200);
                dialog.setLayout(new GridLayout(4, 2));

                JTextField txtNome = new JTextField(produto.getNome());
                JTextField txtQuantidade = new JTextField(String.valueOf(produto.getQuantidade()));
                JTextField txtPreco = new JTextField(String.valueOf(produto.getPreco()));

                dialog.add(new JLabel("Nome:"));
                dialog.add(txtNome);

                dialog.add(new JLabel("Quantidade:"));
                dialog.add(txtQuantidade);

                dialog.add(new JLabel("Preço:"));
                dialog.add(txtPreco);

                JButton btnSubmeter = new JButton("Submeter");
                dialog.add(btnSubmeter);

                btnSubmeter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String nome = txtNome.getText();
                        int quantidade;
                        double preco;

                        try {
                            quantidade = Integer.parseInt(txtQuantidade.getText());
                            String precoTexto = txtPreco.getText().replace(',', '.');
                            preco = Double.parseDouble(precoTexto);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(dialog, "Quantidade e Preço devem ser números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        produto.setNome(nome);
                        produto.setQuantidade(quantidade);
                        produto.setPreco(preco);

                        boolean sucesso = produtoDAO.alterar(produto);
                        if (sucesso) {
                            JOptionPane.showMessageDialog(dialog, "Produto alterado com sucesso!");
                            carregarDadosNaTabela();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Falha ao alterar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                        dialog.dispose();
                    }
                });

                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void removerProduto() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            produtoDAO.remover(id);
            carregarDadosNaTabela();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EstoqueGUI().setVisible(true);
            }
        });
    }
}
