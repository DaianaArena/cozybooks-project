package com.cozybooks.controller;

import com.cozybooks.model.*;
import com.cozybooks.repository.*;
import com.cozybooks.util.ArchivoService;
import com.cozybooks.util.DBConnection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VentaController {
    private VentaRepository ventaRepository;
    private DetalleVentaRepository detalleVentaRepository;
    private ClienteRepository clienteRepository;
    private LibroRepository libroRepository;
    private Scanner scanner;

    public VentaController() {
        this.ventaRepository = new VentaRepository();
        this.detalleVentaRepository = new DetalleVentaRepository();
        this.clienteRepository = new ClienteRepository();
        this.libroRepository = new LibroRepository();
        this.scanner = new Scanner(System.in);
    }

    public void registrarVenta() {
        try {
            System.out.println("\n=== REGISTRAR VENTA ===");
            
            System.out.print("ID del cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine().trim());
            
            Cliente cliente = clienteRepository.obtenerPorId(idCliente);
            if (cliente == null) {
                System.out.println("Error: No se encontró el cliente con ID: " + idCliente);
                return;
            }
            
            System.out.println("Cliente seleccionado: " + cliente.getNombre());
            
            Venta venta = iniciarVenta(idCliente);
            List<DetalleVenta> detalles = new ArrayList<>();
            List<Libro> libros = new ArrayList<>();
            
            boolean continuar = true;
            while (continuar) {
                System.out.print("ID del libro a agregar (0 para finalizar): ");
                int idLibro = Integer.parseInt(scanner.nextLine().trim());
                
                if (idLibro == 0) {
                    continuar = false;
                    break;
                }
                
                Libro libro = libroRepository.obtenerPorId(idLibro);
                if (libro == null) {
                    System.out.println("Error: No se encontró el libro con ID: " + idLibro);
                    continue;
                }
                
                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine().trim());
                
                if (cantidad <= 0) {
                    System.out.println("Error: La cantidad debe ser mayor a 0.");
                    continue;
                }
                
                if (libro.getTipoLibro() == Libro.TipoLibro.FISICO && libro.getStock() < cantidad) {
                    System.out.println("Error: Stock insuficiente. Disponible: " + libro.getStock());
                    continue;
                }
                
                DetalleVenta detalle = new DetalleVenta(cantidad, libro.getPrecio(), venta.getIdVenta(), idLibro);
                detalles.add(detalle);
                libros.add(libro);
                
                System.out.println("Libro agregado: " + libro.getTitulo() + " x" + cantidad + " = $" + detalle.getSubtotal());
            }
            
            if (detalles.isEmpty()) {
                System.out.println("Error: Debe agregar al menos un libro a la venta.");
                return;
            }
            
            System.out.println("\nResumen de la venta:");
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < detalles.size(); i++) {
                DetalleVenta detalle = detalles.get(i);
                Libro libro = libros.get(i);
                System.out.println("- " + libro.getTitulo() + " x" + detalle.getCantidad() + " = $" + detalle.getSubtotal());
                total = total.add(detalle.getSubtotal());
            }
            System.out.println("Total: $" + total);
            
            System.out.print("¿Confirmar venta? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();
            
            if (!confirmacion.equals("s") && !confirmacion.equals("si")) {
                System.out.println("Venta cancelada.");
                return;
            }
            
            System.out.println("Método de pago:");
            System.out.println("1. Efectivo");
            System.out.println("2. Tarjeta");
            System.out.println("3. Transferencia");
            System.out.print("Seleccione (1-3): ");
            int metodoOpcion = Integer.parseInt(scanner.nextLine().trim());
            
            Venta.MetodoPago metodoPago;
            switch (metodoOpcion) {
                case 1: metodoPago = Venta.MetodoPago.EFECTIVO; break;
                case 2: metodoPago = Venta.MetodoPago.TARJETA; break;
                case 3: metodoPago = Venta.MetodoPago.TRANSFERENCIA; break;
                default:
                    System.out.println("Error: Opción inválida.");
                    return;
            }
            
            venta = confirmarVenta(venta, detalles, libros, metodoPago);
            
            System.out.println("Venta registrada exitosamente con ID: " + venta.getIdVenta());
            System.out.println("Total: $" + venta.getMonto());
            
            generarTicket(venta.getIdVenta());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico inválido.");
        } catch (Exception e) {
            System.out.println("Error al registrar venta: " + e.getMessage());
        }
    }

    private Venta iniciarVenta(int idCliente) {
        Venta venta = new Venta(idCliente, Venta.MetodoPago.EFECTIVO);
        venta.setFecha(LocalDateTime.now());
        venta.setMonto(BigDecimal.ZERO);
        venta.setEstado(Venta.EstadoVenta.PENDIENTE);
        
        try {
            venta = ventaRepository.registrar(venta);
        } catch (Exception e) {
            System.out.println("Error al iniciar venta: " + e.getMessage());
        }
        
        return venta;
    }

    private Venta confirmarVenta(Venta venta, List<DetalleVenta> detalles, List<Libro> libros, Venta.MetodoPago metodoPago) {
        try {
            DBConnection.beginTransaction();
            
            
            
            for (int i = 0; i < detalles.size(); i++) {
                DetalleVenta detalle = detalles.get(i);
                Libro libro = libros.get(i);
                
                detalleVentaRepository.registrar(detalle);
                
                if (libro.getTipoLibro() == Libro.TipoLibro.FISICO) {
                    libroRepository.actualizarStock(libro.getIdLibro(), -detalle.getCantidad());
                }
            }

            BigDecimal total = calcularTotal(detalles);
            venta.setMonto(total);
            venta.setMetodoPago(metodoPago);
            venta.setEstado(Venta.EstadoVenta.COMPLETADA);
            
            ventaRepository.actualizar(venta);
            
            DBConnection.commitTransaction();
            
            return venta;
            
        } catch (Exception e) {
            try {
                DBConnection.rollbackTransaction();
            } catch (Exception rollbackEx) {
                System.out.println("Error al revertir transacción: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Error al confirmar venta: " + e.getMessage());
        }
    }

    public void actualizarVenta() {
        try {
            System.out.println("\n=== ACTUALIZAR VENTA ===");
            
            System.out.print("ID de la venta a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Venta venta = ventaRepository.obtenerPorId(id);
            if (venta == null) {
                System.out.println("Error: No se encontró la venta con ID: " + id);
                return;
            }
            
            System.out.println("Venta encontrada:");
            System.out.println("Cliente ID: " + venta.getIdCliente());
            System.out.println("Monto: $" + venta.getMonto());
            System.out.println("Método de pago: " + venta.getMetodoPago());
            System.out.println("Estado: " + venta.getEstado());
            
            System.out.println("¿Qué desea actualizar?");
            System.out.println("1. Método de pago");
            System.out.println("2. Estado");
            System.out.print("Seleccione (1-2): ");
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            
            switch (opcion) {
                case 1:
                    System.out.println("Método de pago:");
                    System.out.println("1. Efectivo");
                    System.out.println("2. Tarjeta");
                    System.out.println("3. Transferencia");
                    System.out.print("Seleccione (1-3): ");
                    int metodoOpcion = Integer.parseInt(scanner.nextLine().trim());
                    
                    switch (metodoOpcion) {
                        case 1: venta.setMetodoPago(Venta.MetodoPago.EFECTIVO); break;
                        case 2: venta.setMetodoPago(Venta.MetodoPago.TARJETA); break;
                        case 3: venta.setMetodoPago(Venta.MetodoPago.TRANSFERENCIA); break;
                        default:
                            System.out.println("Error: Opción inválida.");
                            return;
                    }
                    break;
                    
                case 2:
                    System.out.println("Estado:");
                    System.out.println("1. Completada");
                    System.out.println("2. Pendiente");
                    System.out.println("3. Cancelada");
                    System.out.print("Seleccione (1-3): ");
                    int estadoOpcion = Integer.parseInt(scanner.nextLine().trim());
                    
                    switch (estadoOpcion) {
                        case 1: venta.setEstado(Venta.EstadoVenta.COMPLETADA); break;
                        case 2: venta.setEstado(Venta.EstadoVenta.PENDIENTE); break;
                        case 3: venta.setEstado(Venta.EstadoVenta.CANCELADA); break;
                        default:
                            System.out.println("Error: Opción inválida.");
                            return;
                    }
                    break;
                    
                default:
                    System.out.println("Error: Opción inválida.");
                    return;
            }
            
            ventaRepository.actualizar(venta);
            System.out.println("Venta actualizada exitosamente.");
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico inválido.");
        } catch (Exception e) {
            System.out.println("Error al actualizar venta: " + e.getMessage());
        }
    }

    public void eliminarVenta() {
        try {
            System.out.println("\n=== ELIMINAR VENTA ===");
            
            System.out.print("ID de la venta a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Venta venta = ventaRepository.obtenerPorId(id);
            if (venta == null) {
                System.out.println("Error: No se encontró la venta con ID: " + id);
                return;
            }
            
            System.out.println("Venta encontrada:");
            System.out.println("Cliente ID: " + venta.getIdCliente());
            System.out.println("Monto: $" + venta.getMonto());
            System.out.println("Fecha: " + venta.getFecha());
            
            System.out.print("¿Está seguro de que desea eliminar esta venta? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();
            
            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                DBConnection.beginTransaction();
                
                try {
                    List<DetalleVenta> detalles = detalleVentaRepository.buscarPorVenta(id);
                    
                    for (DetalleVenta detalle : detalles) {
                        Libro libro = libroRepository.obtenerPorId(detalle.getIdLibro());
                        if (libro != null && libro.getTipoLibro() == Libro.TipoLibro.FISICO) {
                            libroRepository.actualizarStock(libro.getIdLibro(), detalle.getCantidad());
                        }
                    }
                    
                    ventaRepository.eliminar(id);
                    
                    DBConnection.commitTransaction();
                    System.out.println("Venta eliminada exitosamente.");
                    
                } catch (Exception e) {
                    DBConnection.rollbackTransaction();
                    throw e;
                }
            } else {
                System.out.println("Operación cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: ID inválido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar venta: " + e.getMessage());
        }
    }

    public void listarVentas() {
        try {
            System.out.println("\n=== LISTAR VENTAS ===");
            
            List<Venta> ventas = ventaRepository.listar();
            
            if (ventas.isEmpty()) {
                System.out.println("No hay ventas registradas.");
                return;
            }
            
            System.out.printf("%-8s %-20s %-8s %-12s %-15s %-15s%n", 
                "ID", "Fecha", "Cliente", "Monto", "Método Pago", "Estado");
            System.out.println("=".repeat(80));
            
            for (Venta venta : ventas) {
                BigDecimal monto = venta.getMonto();
                String montoStr = (monto != null) ? String.format("%.2f", monto) : "0.00";
                
                System.out.printf("%-8d %-20s %-8d %-12s %-15s %-15s%n",
                    venta.getIdVenta(),
                    venta.getFecha().toLocalDate().toString(),
                    venta.getIdCliente(),
                    montoStr,
                    venta.getMetodoPago(),
                    venta.getEstado()
                );
            }
            
        } catch (Exception e) {
            System.out.println("Error al listar ventas: " + e.getMessage());
        }
    }

    public void buscarVenta() {
        try {
            System.out.println("\n=== BUSCAR VENTA ===");
            
            System.out.print("Ingrese criterio de búsqueda (ID venta, ID cliente, método de pago o estado): ");
            String criterio = scanner.nextLine().trim();
            
            if (criterio.isEmpty()) {
                System.out.println("Error: Debe ingresar un criterio de búsqueda.");
                return;
            }
            
            List<Venta> ventas = ventaRepository.buscar(criterio);
            
            if (ventas.isEmpty()) {
                System.out.println("No se encontraron ventas con el criterio: " + criterio);
                return;
            }
            
            System.out.println("\nVentas encontradas:");
            System.out.printf("%-8s %-20s %-8s %-12s %-15s %-15s%n", 
                "ID", "Fecha", "Cliente", "Monto", "Método Pago", "Estado");
            System.out.println("=".repeat(80));
            
            for (Venta venta : ventas) {
                BigDecimal monto = venta.getMonto();
                String montoStr = (monto != null) ? String.format("%.2f", monto) : "0.00";
                
                System.out.printf("%-8d %-20s %-8d %-12s %-15s %-15s%n",
                    venta.getIdVenta(),
                    venta.getFecha().toLocalDate().toString(),
                    venta.getIdCliente(),
                    montoStr,
                    venta.getMetodoPago(),
                    venta.getEstado()
                );
            }
            
        } catch (Exception e) {
            System.out.println("Error al buscar ventas: " + e.getMessage());
        }
    }

    public String generarTicket(int idVenta) {
        try {
            Venta venta = ventaRepository.obtenerPorId(idVenta);
            if (venta == null) {
                System.out.println("Error: No se encontró la venta con ID: " + idVenta);
                return null;
            }
            
            Cliente cliente = clienteRepository.obtenerPorId(venta.getIdCliente());
            if (cliente == null) {
                System.out.println("Error: No se encontró el cliente de la venta.");
                return null;
            }
            
            List<DetalleVenta> detalles = detalleVentaRepository.buscarPorVenta(idVenta);
            List<Libro> libros = new ArrayList<>();
            
            for (DetalleVenta detalle : detalles) {
                Libro libro = libroRepository.obtenerPorId(detalle.getIdLibro());
                if (libro != null) {
                    libros.add(libro);
                }
            }
            
            return ArchivoService.generarTicket(venta, cliente, detalles, libros);
            
        } catch (Exception e) {
            System.out.println("Error al generar ticket: " + e.getMessage());
            return null;
        }
    }

    private BigDecimal calcularTotal(List<DetalleVenta> detalles) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVenta detalle : detalles) {
            total = total.add(detalle.getSubtotal());
        }
        return total;
    }

    public Venta obtenerVenta(int id) {
        try {
            return ventaRepository.obtenerPorId(id);
        } catch (Exception e) {
            System.out.println("Error al obtener venta: " + e.getMessage());
            return null;
        }
    }
    
    // ========== MÉTODOS PARA JAVAFX ==========
    
    // Método para JavaFX - registra venta con parámetros
    public Venta registrarVenta(int idCliente, List<DetalleVenta> detalles, Venta.MetodoPago metodoPago) {
        try {
            if (idCliente <= 0) {
                throw new IllegalArgumentException("ID de cliente inválido.");
            }
            
            if (detalles == null || detalles.isEmpty()) {
                throw new IllegalArgumentException("Debe agregar al menos un libro a la venta.");
            }
            
            if (metodoPago == null) {
                throw new IllegalArgumentException("Debe seleccionar un método de pago.");
            }
            
            // Crear la venta
            Venta venta = iniciarVenta(idCliente);
            
            // Confirmar la venta con los detalles
            return confirmarVenta(venta, detalles, metodoPago);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar venta: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - actualiza venta con objeto
    public void actualizarVenta(Venta venta) {
        try {
            if (venta == null) {
                throw new IllegalArgumentException("La venta no puede ser nula.");
            }
            
            if (venta.getIdVenta() <= 0) {
                throw new IllegalArgumentException("ID de venta inválido.");
            }
            
            ventaRepository.actualizar(venta);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar venta: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - elimina venta por ID
    public void eliminarVenta(int idVenta) {
        try {
            if (idVenta <= 0) {
                throw new IllegalArgumentException("ID de venta inválido.");
            }
            
            DBConnection.beginTransaction();
            
            try {
                List<DetalleVenta> detalles = detalleVentaRepository.buscarPorVenta(idVenta);
                
                for (DetalleVenta detalle : detalles) {
                    Libro libro = libroRepository.obtenerPorId(detalle.getIdLibro());
                    if (libro != null && libro.getTipoLibro() == Libro.TipoLibro.FISICO) {
                        libroRepository.actualizarStock(libro.getIdLibro(), detalle.getCantidad());
                    }
                }
                
                ventaRepository.eliminar(idVenta);
                
                DBConnection.commitTransaction();
                
            } catch (Exception e) {
                DBConnection.rollbackTransaction();
                throw e;
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar venta: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - retorna lista de ventas
    public List<Venta> obtenerListaVentas() {
        try {
            return ventaRepository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar ventas: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - busca ventas por criterio
    public List<Venta> buscarVentas(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar un criterio de búsqueda.");
            }
            
            return ventaRepository.buscar(criterio.trim());
            
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar ventas: " + e.getMessage());
        }
    }
    
    // Método privado para confirmar venta (usado por registrarVenta)
    private Venta confirmarVenta(Venta venta, List<DetalleVenta> detalles, Venta.MetodoPago metodoPago) {
        try {
            DBConnection.beginTransaction();
            
            for (DetalleVenta detalle : detalles) {
                detalleVentaRepository.registrar(detalle);
                
                Libro libro = libroRepository.obtenerPorId(detalle.getIdLibro());
                if (libro != null && libro.getTipoLibro() == Libro.TipoLibro.FISICO) {
                    libroRepository.actualizarStock(libro.getIdLibro(), -detalle.getCantidad());
                }
            }

            BigDecimal total = calcularTotal(detalles);
            venta.setMonto(total);
            venta.setMetodoPago(metodoPago);
            venta.setEstado(Venta.EstadoVenta.COMPLETADA);
            
            ventaRepository.actualizar(venta);
            
            DBConnection.commitTransaction();
            
            return venta;
            
        } catch (Exception e) {
            try {
                DBConnection.rollbackTransaction();
            } catch (Exception rollbackEx) {
                System.out.println("Error al revertir transacción: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Error al confirmar venta: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - obtiene el total de ventas del mes actual
    public BigDecimal obtenerTotalVentasDelMes() {
        try {
            return ventaRepository.obtenerTotalVentasDelMes();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener total de ventas del mes: " + e.getMessage());
        }
    }
}
