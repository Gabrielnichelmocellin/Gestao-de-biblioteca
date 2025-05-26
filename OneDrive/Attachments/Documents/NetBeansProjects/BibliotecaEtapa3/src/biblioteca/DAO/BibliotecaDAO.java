package biblioteca.DAO;


import biblioteca.model.Usuario;
import biblioteca.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import biblioteca.DAO.Conexao;

public class BibliotecaDAO {


    public static void salvarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, cpf, tipo, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getTipo());
            stmt.setString(4, usuario.getEmail());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("tipo"),
                    rs.getString("email")
                );
                usuarios.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public static Usuario buscarUsuarioPorCPF(String cpf) {
        String sql = "SELECT * FROM usuario WHERE cpf = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("tipo"),
                        rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void salvarLivro(Livro livro) {
        String sql = "INSERT INTO livro (titulo, autor) VALUES (?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Livro> listarLivros() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livro";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Livro l = new Livro(
                    rs.getString("titulo"),
                    rs.getString("autor")
                );
                l.setDisponivel(rs.getBoolean("disponivel"));
                livros.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }

    public static Livro buscarLivroPorTitulo(String titulo) {
        String sql = "SELECT * FROM livro WHERE titulo = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, titulo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("autor")
                    );
                    livro.setDisponivel(rs.getBoolean("disponivel"));
                    return livro;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

  
    public static void registrarEmprestimo(Usuario usuario, Livro livro) {
        String sqlEmprestimo = "INSERT INTO emprestimo (id_usuario, id_livro, data_emprestimo) VALUES (?, ?, ?)";
        String sqlAtualizaLivro = "UPDATE livro SET disponivel = false WHERE titulo = ?";
        
        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false); 
            
            
            try (PreparedStatement stmtEmprestimo = conn.prepareStatement(sqlEmprestimo)) {
                stmtEmprestimo.setInt(1, buscarIdUsuario(usuario.getCpf()));
                stmtEmprestimo.setInt(2, buscarIdLivro(livro.getTitulo()));
                stmtEmprestimo.setDate(3, new Date(System.currentTimeMillis()));
                stmtEmprestimo.executeUpdate();
            }
            
           
            try (PreparedStatement stmtLivro = conn.prepareStatement(sqlAtualizaLivro)) {
                stmtLivro.setString(1, livro.getTitulo());
                stmtLivro.executeUpdate();
            }
            
            conn.commit(); 
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Métodos auxiliares para buscar IDs
    private static int buscarIdUsuario(String cpf) throws SQLException {
        String sql = "SELECT id FROM usuario WHERE cpf = ?";
        try (PreparedStatement stmt = Conexao.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    private static int buscarIdLivro(String titulo) throws SQLException {
        String sql = "SELECT id FROM livro WHERE titulo = ?";
        try (PreparedStatement stmt = Conexao.getConnection().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }
}

