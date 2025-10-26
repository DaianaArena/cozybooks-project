package com.cozybooks.repository;

import com.cozybooks.model.Venta;
import com.cozybooks.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaRepository {

    public Venta registrar(Venta venta) throws SQLException {
        String sql = "INSERT INTO VENTA (fecha, monto, metodo_pago, estado, id_cliente) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            stmt.setBigDecimal(2, venta.getMonto());
            stmt.setString(3, venta.getMetodoPago().toString());
            stmt.setString(4, venta.getEstado().toString());
            stmt.setInt(5, venta.getIdCliente());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo registrar la venta.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    venta.setIdVenta(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID de la venta registrada.");
                }
            }
        }
        
        return venta;
    }

    public void actualizar(Venta venta) throws SQLException {
        String sql = "UPDATE VENTA SET fecha = ?, monto = ?, metodo_pago = ?, estado = ?, id_cliente = ? WHERE id_venta = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            stmt.setBigDecimal(2, venta.getMonto());
            stmt.setString(3, venta.getMetodoPago().toString());
            stmt.setString(4, venta.getEstado().toString());
            stmt.setInt(5, venta.getIdCliente());
            stmt.setInt(6, venta.getIdVenta());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró la venta con ID: " + venta.getIdVenta());
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM VENTA WHERE id_venta = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró la venta con ID: " + id);
            }
        }
    }

    public List<Venta> listar() throws SQLException {
        String sql = "SELECT * FROM VENTA ORDER BY fecha DESC";
        List<Venta> ventas = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ventas.add(mapearResultSetAVenta(rs));
            }
        }
        
        return ventas;
    }

    public List<Venta> buscar(String criterio) throws SQLException {
        String sql = "SELECT * FROM VENTA WHERE CAST(id_venta AS CHAR) LIKE ? OR CAST(id_cliente AS CHAR) LIKE ? " +
                    "OR metodo_pago LIKE ? OR estado LIKE ? ORDER BY fecha DESC";
        List<Venta> ventas = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String criterioBusqueda = "%" + criterio + "%";
            stmt.setString(1, criterioBusqueda);
            stmt.setString(2, criterioBusqueda);
            stmt.setString(3, criterioBusqueda);
            stmt.setString(4, criterioBusqueda);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ventas.add(mapearResultSetAVenta(rs));
                }
            }
        }
        
        return ventas;
    }

    public Venta obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM VENTA WHERE id_venta = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAVenta(rs);
                }
            }
        }
        
        return null;
    }

    public BigDecimal obtenerTotalVentasDelMes() throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM VENTA WHERE " +
                    "YEAR(fecha) = YEAR(CURDATE()) AND MONTH(fecha) = MONTH(CURDATE())";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }
        
        return BigDecimal.ZERO;
    }

    private Venta mapearResultSetAVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setIdVenta(rs.getInt("id_venta"));
        
        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) {
            venta.setFecha(fecha.toLocalDateTime());
        }
        
        venta.setMonto(rs.getBigDecimal("monto"));
        venta.setMetodoPago(Venta.MetodoPago.valueOf(rs.getString("metodo_pago")));
        venta.setEstado(Venta.EstadoVenta.valueOf(rs.getString("estado")));
        venta.setIdCliente(rs.getInt("id_cliente"));
        
        return venta;
    }
}
