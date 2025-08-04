package org.example.barber.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.barber.Application;
import org.example.barber.DAO.UsuarioDAO;
import org.example.barber.entities.Usuario;

import java.util.List;

public class UsuariosController {

    @FXML private TextField novoUsuario;
    @FXML private PasswordField novaSenha;
    @FXML private PasswordField confirmarSenha;
    @FXML private Label msgErro;
    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, String> colNome;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        carregarUsuarios();
    }

    private void carregarUsuarios() {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        ObservableList<Usuario> dados = FXCollections.observableArrayList(usuarios);
        tabelaUsuarios.setItems(dados);
    }

    @FXML
    private void cadastrarUsuario() {
        String nome = novoUsuario.getText().trim();
        String senha = novaSenha.getText();
        String confirmar = confirmarSenha.getText();

        if (nome.isEmpty() || senha.isEmpty() || confirmar.isEmpty()) {
            msgErro.setText("Preencha todos os campos.");
            return;
        }

        if (!senha.equals(confirmar)) {
            msgErro.setText("As senhas não coincidem.");
            return;
        }

        if (usuarioDAO.buscarPorNome(nome) != null) {
            msgErro.setText("Este nome de usuário já está em uso.");
            return;
        }

        Usuario novo = new Usuario(nome, senha);
        usuarioDAO.inserir(nome,senha,false);
        msgErro.setText("Usuário cadastrado com sucesso!");
        limparCampos();
        carregarUsuarios();
    }

    @FXML
    private void excluirUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            msgErro.setText("Selecione um usuário para excluir.");
            return;
        }

        if (selecionado.isAdmin()) {
            msgErro.setStyle("-fx-text-fill: red;");
            msgErro.setText("Impossivel excluir um administrador.");
            return;
        }

        usuarioDAO.excluir(selecionado.getId());
        msgErro.setText("Usuário excluído.");
        carregarUsuarios();
    }

    @FXML
    private void atualizarUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            msgErro.setText("Selecione um usuário para atualizar.");
            return;
        }

        String nome = novoUsuario.getText().trim();
        String senha = novaSenha.getText();
        String confirmar = confirmarSenha.getText();

        if (nome.isEmpty() || senha.isEmpty() || confirmar.isEmpty()) {
            msgErro.setText("Preencha todos os campos.");
            return;
        }

        if (!senha.equals(confirmar)) {
            msgErro.setText("As senhas não coincidem.");
            return;
        }

        // Se o nome for diferente do nome original, verificar duplicidade
        if (!nome.equals(selecionado.getNome()) && usuarioDAO.buscarPorNome(nome) != null) {
            msgErro.setText("Este nome de usuário já está em uso.");
            return;
        }

        // Atualiza o objeto usuário
        selecionado.setNome(nome);
        selecionado.setSenha(senha);  // Ajuste conforme como você armazena a senha (hash etc)

        boolean atualizado = usuarioDAO.atualizar(selecionado);
        if (atualizado) {
            msgErro.setText("Usuário atualizado com sucesso!");
            limparCampos();
            carregarUsuarios();
        } else {
            msgErro.setText("Erro ao atualizar usuário.");
        }
    }



    @FXML
    private void voltarMenu() {
        Application.trocarTela("menuAdmin.fxml","Barber Shop - Menu Administrativo");
    }

    private void limparCampos() {
        novoUsuario.clear();
        novaSenha.clear();
        confirmarSenha.clear();
    }
}
