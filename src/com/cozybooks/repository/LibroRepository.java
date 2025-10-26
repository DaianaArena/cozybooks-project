package com.cozybooks.repository;

import com.cozybooks.model.Libro;
import com.cozybooks.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LibroRepository {

    public Libro registrar(Libro libro) throws SQLException {
        if (libro.getIsbn() != null && !libro.getIsbn().isEmpty() && existeIsbn(libro.getIsbn())) {
            throw new SQLException("Ya existe un libro con el ISBN: " + libro.getIsbn());
        }
        
        String sql = "INSERT INTO LIBRO (titulo, isbn, editorial, año, precio, genero, tipo_libro, stock, id_autor, " +
                    "encuadernado, num_edicion, extension, permisos_impresion, duracion, plataforma, narrador) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getIsbn());
            stmt.setString(3, libro.getEditorial());
            stmt.setInt(4, libro.getAño());
            stmt.setBigDecimal(5, libro.getPrecio());
            stmt.setString(6, libro.getGenero());
            stmt.setString(7, libro.getTipoLibro().toString());
            stmt.setInt(8, libro.getStock());
            stmt.setInt(9, libro.getIdAutor());
            
            // Campos específicos según el tipo de libro
            if (libro.getTipoLibro() == Libro.TipoLibro.FISICO) {
                stmt.setString(10, libro.getEncuadernado());
                stmt.setInt(11, libro.getNumEdicion());
                stmt.setNull(12, Types.VARCHAR); // extension
                stmt.setNull(13, Types.BOOLEAN); // permisos_impresion
                stmt.setNull(14, Types.INTEGER); // duracion
                stmt.setNull(15, Types.VARCHAR); // plataforma
                stmt.setNull(16, Types.VARCHAR); // narrador
            } else if (libro.getTipoLibro() == Libro.TipoLibro.DIGITAL) {
                stmt.setNull(10, Types.VARCHAR); // encuadernado
                stmt.setNull(11, Types.INTEGER); // num_edicion
                stmt.setString(12, libro.getExtension());
                stmt.setBoolean(13, libro.getPermisosImpresion());
                stmt.setNull(14, Types.INTEGER); // duracion
                stmt.setNull(15, Types.VARCHAR); // plataforma
                stmt.setNull(16, Types.VARCHAR); // narrador
            } else if (libro.getTipoLibro() == Libro.TipoLibro.AUDIOLIBRO) {
                stmt.setNull(10, Types.VARCHAR); // encuadernado
                stmt.setNull(11, Types.INTEGER); // num_edicion
                stmt.setNull(12, Types.VARCHAR); // extension
                stmt.setNull(13, Types.BOOLEAN); // permisos_impresion
                stmt.setInt(14, libro.getDuracion());
                stmt.setString(15, libro.getPlataforma());
                stmt.setString(16, libro.getNarrador());
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo registrar el libro.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    libro.setIdLibro(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID del libro registrado.");
                }
            }
        }
        
        return libro;
    }

    public void actualizar(Libro libro) throws SQLException {
        if (libro.getIsbn() != null && !libro.getIsbn().isEmpty() && 
            existeIsbnExcluyendoId(libro.getIsbn(), libro.getIdLibro())) {
            throw new SQLException("Ya existe otro libro con el ISBN: " + libro.getIsbn());
        }
        
        String sql = "UPDATE LIBRO SET titulo = ?, isbn = ?, editorial = ?, año = ?, precio = ?, genero = ?, " +
                    "tipo_libro = ?, stock = ?, id_autor = ?, encuadernado = ?, num_edicion = ?, " +
                    "extension = ?, permisos_impresion = ?, duracion = ?, plataforma = ?, narrador = ? " +
                    "WHERE id_libro = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getIsbn());
            stmt.setString(3, libro.getEditorial());
            stmt.setInt(4, libro.getAño());
            stmt.setBigDecimal(5, libro.getPrecio());
            stmt.setString(6, libro.getGenero());
            stmt.setString(7, libro.getTipoLibro().toString());
            stmt.setInt(8, libro.getStock());
            stmt.setInt(9, libro.getIdAutor());
            
            // Campos específicos según el tipo de libro
            if (libro.getTipoLibro() == Libro.TipoLibro.FISICO) {
                stmt.setString(10, libro.getEncuadernado());
                stmt.setInt(11, libro.getNumEdicion());
                stmt.setNull(12, Types.VARCHAR); // extension
                stmt.setNull(13, Types.BOOLEAN); // permisos_impresion
                stmt.setNull(14, Types.INTEGER); // duracion
                stmt.setNull(15, Types.VARCHAR); // plataforma
                stmt.setNull(16, Types.VARCHAR); // narrador
            } else if (libro.getTipoLibro() == Libro.TipoLibro.DIGITAL) {
                stmt.setNull(10, Types.VARCHAR); // encuadernado
                stmt.setNull(11, Types.INTEGER); // num_edicion
                stmt.setString(12, libro.getExtension());
                stmt.setBoolean(13, libro.getPermisosImpresion());
                stmt.setNull(14, Types.INTEGER); // duracion
                stmt.setNull(15, Types.VARCHAR); // plataforma
                stmt.setNull(16, Types.VARCHAR); // narrador
            } else if (libro.getTipoLibro() == Libro.TipoLibro.AUDIOLIBRO) {
                stmt.setNull(10, Types.VARCHAR); // encuadernado
                stmt.setNull(11, Types.INTEGER); // num_edicion
                stmt.setNull(12, Types.VARCHAR); // extension
                stmt.setNull(13, Types.BOOLEAN); // permisos_impresion
                stmt.setInt(14, libro.getDuracion());
                stmt.setString(15, libro.getPlataforma());
                stmt.setString(16, libro.getNarrador());
            }
            
            stmt.setInt(17, libro.getIdLibro());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró el libro con ID: " + libro.getIdLibro());
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        if (estaEnVentas(id)) {
            throw new SQLException("No se puede eliminar el libro porque está en ventas registradas.");
        }
        
        String sql = "DELETE FROM LIBRO WHERE id_libro = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró el libro con ID: " + id);
            }
        }
    }

    public List<Libro> listar() throws SQLException {
        String sql = "SELECT * FROM LIBRO ORDER BY tipo_libro, titulo";
        List<Libro> libros = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                libros.add(mapearResultSetALibro(rs));
            }
        }
        
        return libros;
    }

    public List<Libro> buscar(String criterio) throws SQLException {
        String sql = "SELECT * FROM LIBRO WHERE titulo LIKE ? OR isbn LIKE ? OR genero LIKE ? ORDER BY titulo";
        List<Libro> libros = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String criterioBusqueda = "%" + criterio + "%";
            stmt.setString(1, criterioBusqueda);
            stmt.setString(2, criterioBusqueda);
            stmt.setString(3, criterioBusqueda);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapearResultSetALibro(rs));
                }
            }
        }
        
        return libros;
    }

    public List<Libro> buscarPorAutor(int idAutor) throws SQLException {
        String sql = "SELECT * FROM LIBRO WHERE id_autor = ? ORDER BY titulo";
        List<Libro> libros = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idAutor);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapearResultSetALibro(rs));
                }
            }
        }
        
        return libros;
    }

    public Libro obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM LIBRO WHERE id_libro = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetALibro(rs);
                }
            }
        }
        
        return null;
    }

    public void actualizarStock(int idLibro, int cantidad) throws SQLException {
        String sql = "UPDATE LIBRO SET stock = stock + ? WHERE id_libro = ? AND tipo_libro = 'FISICO'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idLibro);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo actualizar el stock del libro con ID: " + idLibro);
            }
        }
    }
    
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM LIBRO";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    public List<Libro> obtenerUltimosLibros(int cantidad) throws SQLException {
        String sql = "SELECT * FROM LIBRO ORDER BY fecha_registro DESC LIMIT ?";
        List<Libro> libros = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidad);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapearResultSetALibro(rs));
                }
            }
        }
        
        return libros;
    }

    private boolean existeIsbn(String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LIBRO WHERE isbn = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }

    private boolean existeIsbnExcluyendoId(String isbn, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LIBRO WHERE isbn = ? AND id_libro != ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            stmt.setInt(2, idExcluir);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }

    private boolean estaEnVentas(int idLibro) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DETALLE_VENTA WHERE id_libro = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLibro);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }

    private Libro mapearResultSetALibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setIdLibro(rs.getInt("id_libro"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setIsbn(rs.getString("isbn"));
        libro.setEditorial(rs.getString("editorial"));
        libro.setAño(rs.getInt("año"));
        libro.setPrecio(rs.getBigDecimal("precio"));
        libro.setGenero(rs.getString("genero"));
        libro.setTipoLibro(Libro.TipoLibro.valueOf(rs.getString("tipo_libro")));
        libro.setStock(rs.getInt("stock"));
        libro.setIdAutor(rs.getInt("id_autor"));
        
        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            libro.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }
        
        // Campos específicos según el tipo
        libro.setEncuadernado(rs.getString("encuadernado"));
        libro.setNumEdicion(rs.getInt("num_edicion"));
        if (rs.wasNull()) libro.setNumEdicion(null);
        
        libro.setExtension(rs.getString("extension"));
        libro.setPermisosImpresion(rs.getBoolean("permisos_impresion"));
        if (rs.wasNull()) libro.setPermisosImpresion(null);
        
        libro.setDuracion(rs.getInt("duracion"));
        if (rs.wasNull()) libro.setDuracion(null);
        
        libro.setPlataforma(rs.getString("plataforma"));
        libro.setNarrador(rs.getString("narrador"));
        
        return libro;
    }
}
