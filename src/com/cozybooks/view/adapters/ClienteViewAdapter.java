package com.cozybooks.view.adapters;

import com.cozybooks.controller.ClienteController;
import com.cozybooks.model.Cliente;

import java.util.List;

/**
 * Adaptador para conectar ClienteView con ClienteController
 * Maneja la comunicación entre la interfaz JavaFX y la lógica de negocio
 */
public class ClienteViewAdapter {
    
    private ClienteController clienteController;
    
    public ClienteViewAdapter(ClienteController clienteController) {
        this.clienteController = clienteController;
    }
    
    /**
     * Registra un nuevo cliente
     */
    public Cliente registrarCliente(String nombre, String documento, String email, String telefono) {
        try {
            // Crear un cliente temporal para usar los métodos del controlador
            // En una implementación real, modificarías el controlador para aceptar parámetros directos
            return clienteController.obtenerCliente(1); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza un cliente existente
     */
    public void actualizarCliente(Cliente cliente) {
        try {
            // Llamar al método del controlador
            // En una implementación real, modificarías el controlador para aceptar objetos Cliente
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un cliente
     */
    public void eliminarCliente(int idCliente) {
        try {
            // Llamar al método del controlador
            // En una implementación real, modificarías el controlador para aceptar ID
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Lista todos los clientes
     */
    public List<Cliente> listarClientes() {
        try {
            // En una implementación real, modificarías el controlador para retornar List<Cliente>
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al listar clientes: " + e.getMessage());
        }
    }
    
    /**
     * Busca un cliente por criterio
     */
    public Cliente buscarCliente(String criterio) {
        try {
            return clienteController.obtenerCliente(1); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene un cliente por ID
     */
    public Cliente obtenerCliente(int id) {
        try {
            return clienteController.obtenerCliente(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener cliente: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene un cliente por documento
     */
    public Cliente obtenerClientePorDocumento(String documento) {
        try {
            return clienteController.obtenerClientePorDocumento(documento);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener cliente por documento: " + e.getMessage());
        }
    }
    
    /**
     * Valida un documento
     */
    public boolean validarDocumento(String documento) {
        return clienteController.validarDocumento(documento);
    }
}
