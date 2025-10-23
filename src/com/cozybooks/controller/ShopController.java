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
        System.out.println("1. Autores");
        System.out.println("2. Clientes");
        System.out.println("3. Libros");
        System.out.println("4. Ventas");
        System.out.println("0. Salir");
        System.out.print("\nSeleccione una opción: ");
    }

    /**
     * Procesa la opción seleccionada por el usuario
     */
    private void procesarOpcion() {
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            
            switch (opcion) {
                case 1:
                    mostrarSubmenuAutores();
                    break;
                case 2:
                    mostrarSubmenuClientes();
                    break;
                case 3:
                    mostrarSubmenuLibros();
                    break;
                case 4:
                    mostrarSubmenuVentas();
                    break;
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

    /**
     * Muestra el submenú de Autores
     */
    private void mostrarSubmenuAutores() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== GESTIÓN DE AUTORES ===");
            System.out.println("1. Registrar Autor");
            System.out.println("2. Actualizar Autor");
            System.out.println("3. Eliminar Autor");
            System.out.println("4. Listar Autores");
            System.out.println("5. Buscar Autor");
            System.out.println("6. Reporte de Libros por Autor");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nSeleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
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
                    case 6:
                        autorController.reporteLibrosPorAutor();
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Error: Opción inválida.");
                        break;
                }
                
                if (continuar && opcion != 0) {
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

    /**
     * Muestra el submenú de Clientes
     */
    private void mostrarSubmenuClientes() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== GESTIÓN DE CLIENTES ===");
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Actualizar Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("4. Listar Clientes");
            System.out.println("5. Buscar Cliente");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nSeleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
                    case 1:
                        clienteController.registrarCliente();
                        break;
                    case 2:
                        clienteController.actualizarCliente();
                        break;
                    case 3:
                        clienteController.eliminarCliente();
                        break;
                    case 4:
                        clienteController.listarClientes();
                        break;
                    case 5:
                        clienteController.buscarCliente();
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Error: Opción inválida.");
                        break;
                }
                
                if (continuar && opcion != 0) {
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

    /**
     * Muestra el submenú de Libros
     */
    private void mostrarSubmenuLibros() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== GESTIÓN DE LIBROS ===");
            System.out.println("1. Registrar Libro");
            System.out.println("2. Actualizar Libro");
            System.out.println("3. Eliminar Libro");
            System.out.println("4. Listar Libros");
            System.out.println("5. Buscar Libro");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nSeleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
                    case 1:
                        libroController.registrarLibro();
                        break;
                    case 2:
                        libroController.actualizarLibro();
                        break;
                    case 3:
                        libroController.eliminarLibro();
                        break;
                    case 4:
                        libroController.listarLibros();
                        break;
                    case 5:
                        libroController.buscarLibro();
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Error: Opción inválida.");
                        break;
                }
                
                if (continuar && opcion != 0) {
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

    /**
     * Muestra el submenú de Ventas
     */
    private void mostrarSubmenuVentas() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== GESTIÓN DE VENTAS ===");
            System.out.println("1. Registrar Venta");
            System.out.println("2. Actualizar Venta");
            System.out.println("3. Eliminar Venta");
            System.out.println("4. Listar Ventas");
            System.out.println("5. Buscar Venta");
            System.out.println("6. Generar Ticket");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nSeleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
                    case 1:
                        ventaController.registrarVenta();
                        break;
                    case 2:
                        ventaController.actualizarVenta();
                        break;
                    case 3:
                        ventaController.eliminarVenta();
                        break;
                    case 4:
                        ventaController.listarVentas();
                        break;
                    case 5:
                        ventaController.buscarVenta();
                        break;
                    case 6:
                        System.out.print("ID de la venta para generar ticket: ");
                        int idVenta = Integer.parseInt(scanner.nextLine().trim());
                        ventaController.generarTicket(idVenta);
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Error: Opción inválida.");
                        break;
                }
                
                if (continuar && opcion != 0) {
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
}
