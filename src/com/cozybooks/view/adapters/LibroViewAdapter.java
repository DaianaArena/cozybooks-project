package com.cozybooks.view.adapters;

import com.cozybooks.controller.LibroController;
import com.cozybooks.model.Libro;

import java.util.List;

/**
 * Adaptador para conectar LibroView con LibroController
 * Maneja la comunicación entre la interfaz JavaFX y la lógica de negocio
 */
public class LibroViewAdapter {
    
    private LibroController libroController;
    
    public LibroViewAdapter(LibroController libroController) {
        this.libroController = libroController;
    }
    
    /**
     * Registra un nuevo libro
     */
    public Libro registrarLibro(Libro libro) {
        try {
            // En una implementación real, modificarías el controlador para aceptar objetos Libro
            return libroController.obtenerLibro(1); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar libro: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza un libro existente
     */
    public void actualizarLibro(Libro libro) {
        try {
            // Llamar al método del controlador
            // En una implementación real, modificarías el controlador para aceptar objetos Libro
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar libro: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un libro
     */
    public void eliminarLibro(int idLibro) {
        try {
            // Llamar al método del controlador
            // En una implementación real, modificarías el controlador para aceptar ID
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar libro: " + e.getMessage());
        }
    }
    
    /**
     * Lista todos los libros
     */
    public List<Libro> listarLibros() {
        try {
            // En una implementación real, modificarías el controlador para retornar List<Libro>
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al listar libros: " + e.getMessage());
        }
    }
    
    /**
     * Busca libros por criterio
     */
    public List<Libro> buscarLibros(String criterio) {
        try {
            // En una implementación real, modificarías el controlador para retornar List<Libro>
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza el stock de un libro
     */
    public void actualizarStock(int idLibro, int cantidad) {
        try {
            libroController.actualizarStock(idLibro, cantidad);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar stock: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene un libro por ID
     */
    public Libro obtenerLibro(int id) {
        try {
            return libroController.obtenerLibro(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener libro: " + e.getMessage());
        }
    }
    
    /**
     * Busca libros por autor
     */
    public List<Libro> buscarPorAutor(int idAutor) {
        try {
            return libroController.buscarPorAutor(idAutor);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros por autor: " + e.getMessage());
        }
    }
}
