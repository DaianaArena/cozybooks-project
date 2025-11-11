package com.cozybooks.util;

import com.cozybooks.model.Venta;
import com.cozybooks.model.DetalleVenta;
import com.cozybooks.model.Cliente;
import com.cozybooks.model.Libro;

import java.io.IOException;
import java.util.List;

/**
 * Interfaz para servicios de generación de tickets de venta.
 * Define el contrato que deben cumplir todas las implementaciones
 * de generadores de tickets (TXT, PDF, XML, etc.)
 */
public interface GeneradorTicket {
    
    /**
     * Genera un ticket de venta con la información proporcionada.
     * 
     * @param venta la venta para la cual generar el ticket
     * @param cliente el cliente asociado a la venta
     * @param detalles los detalles de la venta (productos y cantidades)
     * @param libros los libros vendidos (correspondientes a los detalles)
     * @throws IOException si hay error al escribir el archivo
     */
    void generarTicket(Venta venta, Cliente cliente, 
                      List<DetalleVenta> detalles, 
                      List<Libro> libros) throws IOException;
    
    /**
     * Verifica si el directorio de tickets existe.
     * 
     * @return true si el directorio existe, false en caso contrario
     */
    boolean directorioTicketsExiste();
    
    /**
     * Crea el directorio de tickets si no existe.
     * 
     * @throws IOException si hay error al crear el directorio
     */
    void crearDirectorioTickets() throws IOException;
}

