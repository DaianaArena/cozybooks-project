package com.cozybooks.util;

import com.cozybooks.model.Venta;
import com.cozybooks.model.DetalleVenta;
import com.cozybooks.model.Cliente;
import com.cozybooks.model.Libro;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para manejo de archivos, especialmente para generar tickets de venta
 */
public class ArchivoService {
    private static final String TICKETS_DIR = "tickets";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /**
     * Genera un ticket de venta en formato .txt
     * @param venta la venta para la cual generar el ticket
     * @param cliente el cliente de la venta
     * @param detalles los detalles de la venta
     * @param libros los libros vendidos
     * @return la ruta absoluta del archivo generado
     * @throws IOException si hay error al escribir el archivo
     */
    public static String generarTicket(Venta venta, Cliente cliente, List<DetalleVenta> detalles, List<Libro> libros) throws IOException {
        // Crear directorio si no existe
        Path ticketsPath = Paths.get(TICKETS_DIR);
        if (!Files.exists(ticketsPath)) {
            Files.createDirectories(ticketsPath);
        }

        // Generar nombre del archivo
        String fileName = String.format("venta_%d_%s.txt", 
            venta.getIdVenta(), 
            venta.getFecha().format(DATE_FORMATTER));
        
        Path filePath = ticketsPath.resolve(fileName);

        // Escribir el ticket
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write("========================================\n");
            writer.write("        COZY BOOKS - TICKET DE VENTA\n");
            writer.write("========================================\n\n");
            
            writer.write("ID Venta: " + venta.getIdVenta() + "\n");
            writer.write("Fecha: " + venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
            writer.write("Cliente: " + cliente.getNombre() + "\n");
            writer.write("Documento: " + cliente.getDocumento() + "\n");
            if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
                writer.write("Email: " + cliente.getEmail() + "\n");
            }
            writer.write("Método de Pago: " + venta.getMetodoPago() + "\n");
            writer.write("Estado: " + venta.getEstado() + "\n\n");
            
            writer.write("----------------------------------------\n");
            writer.write("DETALLES DE LA VENTA:\n");
            writer.write("----------------------------------------\n");
            
            for (int i = 0; i < detalles.size(); i++) {
                DetalleVenta detalle = detalles.get(i);
                Libro libro = libros.get(i);
                
                writer.write(String.format("%d. %s\n", i + 1, libro.getTitulo()));
                writer.write(String.format("   Autor ID: %d\n", libro.getIdAutor()));
                writer.write(String.format("   Tipo: %s\n", libro.getTipoLibro()));
                writer.write(String.format("   Cantidad: %d\n", detalle.getCantidad()));
                writer.write(String.format("   Precio Unitario: $%.2f\n", detalle.getPrecioUnitario()));
                writer.write(String.format("   Subtotal: $%.2f\n\n", detalle.getSubtotal()));
            }
            
            writer.write("----------------------------------------\n");
            writer.write(String.format("TOTAL: $%.2f\n", venta.getMonto()));
            writer.write("========================================\n");
            writer.write("¡Gracias por su compra!\n");
            writer.write("========================================\n");
        }

        System.out.println("Ticket generado exitosamente: " + filePath.toString());
        return filePath.toAbsolutePath().toString();
    }

    /**
     * Verifica si el directorio de tickets existe
     * @return true si existe, false en caso contrario
     */
    public static boolean directorioTicketsExiste() {
        return Files.exists(Paths.get(TICKETS_DIR));
    }

    /**
     * Crea el directorio de tickets si no existe
     * @throws IOException si hay error al crear el directorio
     */
    public static void crearDirectorioTickets() throws IOException {
        Path ticketsPath = Paths.get(TICKETS_DIR);
        if (!Files.exists(ticketsPath)) {
            Files.createDirectories(ticketsPath);
            System.out.println("Directorio de tickets creado: " + ticketsPath.toString());
        }
    }
}
