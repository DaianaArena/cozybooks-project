package com.cozybooks.controller;

import java.util.Scanner;

/**
 * Controlador principal de la aplicación Cozy Books
 * Maneja el menú principal y coordina las operaciones
 */
public class ShopController {
    private AutorController autorController;
    private ClienteController clienteController;
    private LibroController libroController;
    private VentaController ventaController;
    private Scanner scanner;
    private boolean ejecutando;

    public ShopController() {
        this.autorController = new AutorController();
        this.clienteController = new ClienteController();
        this.libroController = new LibroController();
        this.ventaController = new VentaController();
        this.scanner = new Scanner(System.in);
        this.ejecutando = true;
    }

    /**
     * Inicia la aplicación y muestra el menú principal
     */
    public void iniciar() {
        System.out.println("========================================");
        System.out.println("    BIENVENIDO A COZY BOOKS SYSTEM");
        System.out.println("========================================");
        
        while (ejecutando) {
            mostrarMenuPrincipal();
            procesarOpcion();
        }
        
        System.out.println("¡Gracias por usar Cozy Books System!");
    }

    /**
     * Muestra el menú principal de la aplicación
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("GESTIÓN DE AUTORES:");
        System.out.println("1.  Registrar Autor");
        System.out.println("2.  Actualizar Autor");
        System.out.println("3.  Eliminar Autor");
        System.out.println("4.  Listar Autores");
        System.out.println("5.  Buscar Autor");
        System.out.println("22. Reporte de Libros por Autor");
        
        System.out.println("\nGESTIÓN DE CLIENTES:");
        System.out.println("6.  Registrar Cliente");
        System.out.println("7.  Actualizar Cliente");
        System.out.println("8.  Eliminar Cliente");
        System.out.println("9.  Listar Clientes");
        System.out.println("10. Buscar Cliente");
        
        System.out.println("\nGESTIÓN DE LIBROS:");
        System.out.println("11. Registrar Libro");
        System.out.println("12. Actualizar Libro");
        System.out.println("13. Eliminar Libro");
        System.out.println("14. Listar Libros");
        System.out.println("15. Buscar Libro");
        
        System.out.println("\nGESTIÓN DE VENTAS:");
        System.out.println("16. Registrar Venta");
        System.out.println("17. Actualizar Venta");
        System.out.println("18. Eliminar Venta");
        System.out.println("19. Listar Ventas");
        System.out.println("20. Buscar Venta");
        System.out.println("21. Generar Ticket");
        
        System.out.println("\n0.  Salir");
        System.out.print("\nSeleccione una opción: ");
    }

    /**
     * Procesa la opción seleccionada por el usuario
     */
    private void procesarOpcion() {
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            
            switch (opcion) {
                // Gestión de Autores
                case 1:
                    autorController.registrarAutor();
                    break;
                case 2:
                    autorController.actualizarAutor();
                    break;
                case 3:
                    autorController.eliminarAutor();
                    break;
                case 4:
                    autorController.listarAutores();
                    break;
                case 5:
                    autorController.buscarAutor();
                    break;
                case 22:
                    autorController.reporteLibrosPorAutor();
                    break;
                
                // Gestión de Clientes
                case 6:
                    clienteController.registrarCliente();
                    break;
                case 7:
                    clienteController.actualizarCliente();
                    break;
                case 8:
                    clienteController.eliminarCliente();
                    break;
                case 9:
                    clienteController.listarClientes();
                    break;
                case 10:
                    clienteController.buscarCliente();
                    break;
                
                // Gestión de Libros
                case 11:
                    libroController.registrarLibro();
                    break;
                case 12:
                    libroController.actualizarLibro();
                    break;
                case 13:
                    libroController.eliminarLibro();
                    break;
                case 14:
                    libroController.listarLibros();
                    break;
                case 15:
                    libroController.buscarLibro();
                    break;
                
                // Gestión de Ventas
                case 16:
                    ventaController.registrarVenta();
                    break;
                case 17:
                    ventaController.actualizarVenta();
                    break;
                case 18:
                    ventaController.eliminarVenta();
                    break;
                case 19:
                    ventaController.listarVentas();
                    break;
                case 20:
                    ventaController.buscarVenta();
                    break;
                case 21:
                    System.out.print("ID de la venta para generar ticket: ");
                    int idVenta = Integer.parseInt(scanner.nextLine().trim());
                    ventaController.generarTicket(idVenta);
                    break;
                
                // Salir
                case 0:
                    ejecutando = false;
                    break;
                
                default:
                    System.out.println("Error: Opción inválida. Por favor, seleccione una opción válida.");
                    break;
            }
            
            if (ejecutando && opcion != 0) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }
}
