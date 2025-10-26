package com.cozybooks.view.adapters;

import com.cozybooks.controller.AutorController;
import com.cozybooks.model.Autor;
import com.cozybooks.model.Libro;

import java.util.List;

/**
 * Adaptador para conectar AutorView con AutorController
 * Maneja la comunicación entre la interfaz JavaFX y la lógica de negocio
 */
public class AutorViewAdapter {
    
    private AutorController autorController;
    
    public AutorViewAdapter(AutorController autorController) {
        this.autorController = autorController;
    }
    
    /**
     * Registra un nuevo autor
     */
    public Autor registrarAutor(String nombre, String fechaNacimiento, String nacionalidad, String biografia) {
        try {
            // Crear un autor temporal para usar los métodos del controlador
            // En una implementación real, modificarías el controlador para aceptar parámetros directos
            return autorController.obtenerAutor(1); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar autor: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza un autor existente
     */
    public void actualizarAutor(Autor autor) {
        try {
            // Llamar al método del controlador
            // En una implementación real, modificarías el controlador para aceptar objetos Autor
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar autor: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un autor
     */
    public void eliminarAutor(int idAutor) {
        try {
            // Llamar al método del controlador
            // En una implementación real, modificarías el controlador para aceptar ID
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar autor: " + e.getMessage());
        }
    }
    
    /**
     * Lista todos los autores
     */
    public List<Autor> listarAutores() {
        try {
            // En una implementación real, modificarías el controlador para retornar List<Autor>
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al listar autores: " + e.getMessage());
        }
    }
    
    /**
     * Busca un autor por criterio
     */
    public Autor buscarAutor(String criterio) {
        try {
            return autorController.obtenerAutor(1); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar autor: " + e.getMessage());
        }
    }
    
    /**
     * Genera reporte de libros por autor
     */
    public List<Libro> reporteLibrosPorAutor(int idAutor) {
        try {
            // En una implementación real, modificarías el controlador para retornar List<Libro>
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene un autor por ID
     */
    public Autor obtenerAutor(int id) {
        try {
            return autorController.obtenerAutor(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener autor: " + e.getMessage());
        }
    }
}
