package com.cozybooks.repository;

import com.cozybooks.model.Cliente;
import com.cozybooks.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    public Cliente registrar(Cliente cliente) throws SQLException {
        if (existeDocumento(cliente.getDocumento())) {
            throw new SQLException("Ya existe un cliente con el documento: " + cliente.getDocumento());
        }
        
        String sql = "INSERT INTO CLIENTE (nombre, documento, email, telefono) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDocumento());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo registrar el cliente.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID del cliente registrado.");
                }
            }
        }
        
        return cliente;
    }

    public void actualizar(Cliente cliente) throws SQLException {
        if (existeDocumentoExcluyendoId(cliente.getDocumento(), cliente.getIdCliente())) {
            throw new SQLException("Ya existe otro cliente con el documento: " + cliente.getDocumento());
        }
        
        String sql = "UPDATE CLIENTE SET nombre = ?, documento = ?, email = ?, telefono = ? WHERE id_cliente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDocumento());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            stmt.setInt(5, cliente.getIdCliente());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró el cliente con ID: " + cliente.getIdCliente());
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        if (tieneVentasAsociadas(id)) {
            throw new SQLException("No se puede eliminar el cliente porque tiene ventas asociadas.");
        }
        
        String sql = "DELETE FROM CLIENTE WHERE id_cliente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró el cliente con ID: " + id);
            }
        }
    }

    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT * FROM CLIENTE ORDER BY nombre";
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(mapearResultSetACliente(rs));
            }
        }
        
        return clientes;
    }

    public Cliente buscar(String criterio) throws SQLException {
        String sql = "SELECT * FROM CLIENTE WHERE nombre LIKE ? OR documento LIKE ? ORDER BY nombre LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String criterioBusqueda = "%" + criterio + "%";
            stmt.setString(1, criterioBusqueda);
            stmt.setString(2, criterioBusqueda);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetACliente(rs);
                }
            }
        }
        
        return null;
    }

    public Cliente obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM CLIENTE WHERE id_cliente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetACliente(rs);
                }
            }
        }
        
        return null;
    }

    public Cliente obtenerPorDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM CLIENTE WHERE documento = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, documento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetACliente(rs);
                }
            }
        }
        
        return null;
    }

    private boolean existeDocumento(String documento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CLIENTE WHERE documento = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, documento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }

    private boolean existeDocumentoExcluyendoId(String documento, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CLIENTE WHERE documento = ? AND id_cliente != ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, documento);
            stmt.setInt(2, idExcluir);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }

    private boolean tieneVentasAsociadas(int idCliente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM VENTA WHERE id_cliente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM CLIENTE";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    public List<Cliente> buscarClientes(String criterio) throws SQLException {
        String sql = "SELECT * FROM CLIENTE WHERE nombre LIKE ? OR documento LIKE ? OR email LIKE ?";
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + criterio + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDocumento(rs.getString("documento"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setTelefono(rs.getString("telefono"));
                    clientes.add(cliente);
                }
            }
        }
        
        return clientes;
    }

    private Cliente mapearResultSetACliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setDocumento(rs.getString("documento"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefono(rs.getString("telefono"));
        
        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            cliente.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }
        
        return cliente;
    }
}
