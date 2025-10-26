package com.cozybooks.repository;

import com.cozybooks.model.Autor;
import com.cozybooks.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para manejar operaciones CRUD de la entidad Autor
 */
public class AutorRepository {

    public Autor registrar(Autor autor) throws SQLException {
        String sql = "INSERT INTO AUTOR (nombre, fecha_nacimiento, nacionalidad, biografia) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, autor.getNombre());
            stmt.setDate(2, Date.valueOf(autor.getFechaNacimiento()));
            stmt.setString(3, autor.getNacionalidad());
            stmt.setString(4, autor.getBiografia());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo registrar el autor.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    autor.setIdAutor(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID del autor registrado.");
                }
            }
        }
        
        return autor;
    }

    public void actualizar(Autor autor) throws SQLException {
        String sql = "UPDATE AUTOR SET nombre = ?, fecha_nacimiento = ?, nacionalidad = ?, biografia = ? WHERE id_autor = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, autor.getNombre());
            stmt.setDate(2, Date.valueOf(autor.getFechaNacimiento()));
            stmt.setString(3, autor.getNacionalidad());
            stmt.setString(4, autor.getBiografia());
            stmt.setInt(5, autor.getIdAutor());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró el autor con ID: " + autor.getIdAutor());
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        if (tieneLibrosAsociados(id)) {
            throw new SQLException("No se puede eliminar el autor porque tiene libros asociados.");
        }
        
        String sql = "DELETE FROM AUTOR WHERE id_autor = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró el autor con ID: " + id);
            }
        }
    }

    public List<Autor> listar() throws SQLException {
        String sql = "SELECT * FROM AUTOR ORDER BY nombre";
        List<Autor> autores = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                autores.add(mapearResultSetAAutor(rs));
            }
        }
        
        return autores;
    }

    public Autor buscar(String criterio) throws SQLException {
        String sql = "SELECT * FROM AUTOR WHERE nombre LIKE ? ORDER BY nombre LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + criterio + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAAutor(rs);
                }
            }
        }
        
        return null;
    }

    public Autor obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM AUTOR WHERE id_autor = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAAutor(rs);
                }
            }
        }
        
        return null;
    }

    private boolean tieneLibrosAsociados(int idAutor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LIBRO WHERE id_autor = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idAutor);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM AUTOR";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }

    private Autor mapearResultSetAAutor(ResultSet rs) throws SQLException {
        Autor autor = new Autor();
        autor.setIdAutor(rs.getInt("id_autor"));
        autor.setNombre(rs.getString("nombre"));
        
        Date fechaNacimiento = rs.getDate("fecha_nacimiento");
        if (fechaNacimiento != null) {
            autor.setFechaNacimiento(fechaNacimiento.toLocalDate());
        }
        
        autor.setNacionalidad(rs.getString("nacionalidad"));
        autor.setBiografia(rs.getString("biografia"));
        
        return autor;
    }
}
