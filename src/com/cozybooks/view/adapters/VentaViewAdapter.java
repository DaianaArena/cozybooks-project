package com.cozybooks.view.adapters;

import com.cozybooks.controller.VentaController;
import com.cozybooks.model.*;

import java.util.List;

/**
 * Adaptador para conectar VentaView con VentaController
 * Maneja la comunicación entre la interfaz JavaFX y la lógica de negocio
 */
public class VentaViewAdapter {
    
    private VentaController ventaController;
    
    public VentaViewAdapter(VentaController ventaController) {
        this.ventaController = ventaController;
    }
    
    /**
     * Registra una nueva venta
     */
    public Venta registrarVenta(int idCliente, List<DetalleVenta> detalles, Venta.MetodoPago metodoPago) {
        try {
            // En una implementación real, modificarías el controlador para aceptar estos parámetros
            return ventaController.obtenerVenta(1); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar venta: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza una venta existente
     */
    public void actualizarVenta(Venta venta) {
        try {
            // Llamar al método del controlador
            // En una implementación real, modificarías el controlador para aceptar objetos Venta
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar venta: " + e.getMessage());
        }
    }
    
    /**
     * Elimina una venta
     */
    public void eliminarVenta(int idVenta) {
        try {
            // Llamar al método del controlador
            // En una implementación real, modificarías el controlador para aceptar ID
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar venta: " + e.getMessage());
        }
    }
    
    /**
     * Lista todas las ventas
     */
    public List<Venta> listarVentas() {
        try {
            // En una implementación real, modificarías el controlador para retornar List<Venta>
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al listar ventas: " + e.getMessage());
        }
    }
    
    /**
     * Busca ventas por criterio
     */
    public List<Venta> buscarVentas(String criterio) {
        try {
            // En una implementación real, modificarías el controlador para retornar List<Venta>
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar ventas: " + e.getMessage());
        }
    }
    
    /**
     * Genera un ticket para una venta
     */
    public void generarTicket(int idVenta) {
        try {
            ventaController.generarTicket(idVenta);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar ticket: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene una venta por ID
     */
    public Venta obtenerVenta(int id) {
        try {
            return ventaController.obtenerVenta(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener venta: " + e.getMessage());
        }
    }
}
