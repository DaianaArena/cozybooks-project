package com.cozybooks.repository;

import com.cozybooks.model.DetalleVenta;
import com.cozybooks.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaRepository {

    public DetalleVenta registrar(DetalleVenta detalle) throws SQLException {
        String sql = "INSERT INTO DETALLE_VENTA (cantidad, precio_unitario, subtotal, id_venta, id_libro) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, detalle.getCantidad());
            stmt.setBigDecimal(2, detalle.getPrecioUnitario());
            stmt.setBigDecimal(3, detalle.getSubtotal());
            stmt.setInt(4, detalle.getIdVenta());
            stmt.setInt(5, detalle.getIdLibro());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo registrar el detalle de venta.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    detalle.setIdDetalle(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID del detalle registrado.");
                }
            }
        }
        
        return detalle;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM DETALLE_VENTA WHERE id_detalle = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró el detalle con ID: " + id);
            }
        }
    }

    public List<DetalleVenta> listar() throws SQLException {
        String sql = "SELECT * FROM DETALLE_VENTA ORDER BY id_venta, id_detalle";
        List<DetalleVenta> detalles = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                detalles.add(mapearResultSetADetalle(rs));
            }
        }
        
        return detalles;
    }

    public List<DetalleVenta> buscarPorVenta(int idVenta) throws SQLException {
        String sql = "SELECT * FROM DETALLE_VENTA WHERE id_venta = ? ORDER BY id_detalle";
        List<DetalleVenta> detalles = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapearResultSetADetalle(rs));
                }
            }
        }
        
        return detalles;
    }

    public List<DetalleVenta> buscarPorLibro(int idLibro) throws SQLException {
        String sql = "SELECT * FROM DETALLE_VENTA WHERE id_libro = ? ORDER BY id_venta DESC";
        List<DetalleVenta> detalles = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLibro);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapearResultSetADetalle(rs));
                }
            }
        }
        
        return detalles;
    }

    public DetalleVenta obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM DETALLE_VENTA WHERE id_detalle = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetADetalle(rs);
                }
            }
        }
        
        return null;
    }

    public void actualizar(DetalleVenta detalle) throws SQLException {
        String sql = "UPDATE DETALLE_VENTA SET cantidad = ?, precio_unitario = ?, subtotal = ?, id_venta = ?, id_libro = ? WHERE id_detalle = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detalle.getCantidad());
            stmt.setBigDecimal(2, detalle.getPrecioUnitario());
            stmt.setBigDecimal(3, detalle.getSubtotal());
            stmt.setInt(4, detalle.getIdVenta());
            stmt.setInt(5, detalle.getIdLibro());
            stmt.setInt(6, detalle.getIdDetalle());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró el detalle con ID: " + detalle.getIdDetalle());
            }
        }
    }

    public void eliminarPorVenta(int idVenta) throws SQLException {
        String sql = "DELETE FROM DETALLE_VENTA WHERE id_venta = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            stmt.executeUpdate();
        }
    }

    public BigDecimal calcularTotalVenta(int idVenta) throws SQLException {
        String sql = "SELECT SUM(subtotal) FROM DETALLE_VENTA WHERE id_venta = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal(1);
                    return total != null ? total : BigDecimal.ZERO;
                }
            }
        }
        
        return BigDecimal.ZERO;
    }

    private DetalleVenta mapearResultSetADetalle(ResultSet rs) throws SQLException {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setIdDetalle(rs.getInt("id_detalle"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        detalle.setIdVenta(rs.getInt("id_venta"));
        detalle.setIdLibro(rs.getInt("id_libro"));
        
        return detalle;
    }
}
