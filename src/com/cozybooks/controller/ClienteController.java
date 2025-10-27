package com.cozybooks.controller;

import com.cozybooks.model.Cliente;
import com.cozybooks.repository.ClienteRepository;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClienteController {
    private ClienteRepository clienteRepository;
    private Scanner scanner;

    public ClienteController() {
        this.clienteRepository = new ClienteRepository();
        this.scanner = new Scanner(System.in);
    }

    // Método para consola (original)
    public void registrarCliente() {
        try {
            System.out.println("\n=== REGISTRAR CLIENTE ===");
            
            System.out.print("Nombre del cliente: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre es obligatorio.");
                return;
            }
            
            System.out.print("Documento (DNI de 8 dígitos): ");
            String documento = scanner.nextLine().trim();
            if (!validarDocumento(documento)) {
                System.out.println("Error: El documento debe ser un DNI de 8 dígitos.");
                return;
            }
            
            System.out.print("Email (opcional): ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty() && !validarEmail(email)) {
                System.out.println("Error: Formato de email inválido.");
                return;
            }
            if (email.isEmpty()) email = null;
            
            System.out.print("Teléfono (opcional): ");
            String telefono = scanner.nextLine().trim();
            if (telefono.isEmpty()) telefono = null;
            
            Cliente cliente = new Cliente(nombre, documento, email, telefono);
            cliente = clienteRepository.registrar(cliente);
            
            System.out.println("Cliente registrado exitosamente con ID: " + cliente.getIdCliente());
            
        } catch (Exception e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - registra cliente con parámetros
    public Cliente registrarCliente(String nombre, String documento, String email, String telefono) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio.");
            }
            
            if (!validarDocumento(documento)) {
                throw new IllegalArgumentException("El documento debe ser un DNI de 8 dígitos.");
            }
            
            if (email != null && !email.trim().isEmpty() && !validarEmail(email)) {
                throw new IllegalArgumentException("Formato de email inválido.");
            }
            
            Cliente cliente = new Cliente(
                nombre.trim(), 
                documento.trim(),
                (email != null && !email.trim().isEmpty()) ? email.trim() : null,
                (telefono != null && !telefono.trim().isEmpty()) ? telefono.trim() : null
            );
            
            return clienteRepository.registrar(cliente);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar cliente: " + e.getMessage());
        }
    }

    // Método para consola (original)
    public void actualizarCliente() {
        try {
            System.out.println("\n=== ACTUALIZAR CLIENTE ===");
            
            System.out.print("ID del cliente a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Cliente cliente = clienteRepository.obtenerPorId(id);
            if (cliente == null) {
                System.out.println("Error: No se encontró el cliente con ID: " + id);
                return;
            }
            
            System.out.println("Cliente encontrado: " + cliente.getNombre());
            System.out.println("Ingrese los nuevos datos (presione Enter para mantener el valor actual):");
            
            System.out.print("Nombre [" + cliente.getNombre() + "]: ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                cliente.setNombre(nombre);
            }
            
            System.out.print("Documento [" + cliente.getDocumento() + "]: ");
            String documento = scanner.nextLine().trim();
            if (!documento.isEmpty()) {
                if (!validarDocumento(documento)) {
                    System.out.println("Error: El documento debe ser un DNI de 8 dígitos.");
                    return;
                }
                cliente.setDocumento(documento);
            }
            
            System.out.print("Email [" + (cliente.getEmail() != null ? cliente.getEmail() : "sin especificar") + "]: ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                if (!validarEmail(email)) {
                    System.out.println("Error: Formato de email inválido.");
                    return;
                }
                cliente.setEmail(email);
            }
            
            System.out.print("Teléfono [" + (cliente.getTelefono() != null ? cliente.getTelefono() : "sin especificar") + "]: ");
            String telefono = scanner.nextLine().trim();
            if (!telefono.isEmpty()) {
                cliente.setTelefono(telefono);
            }
            
            clienteRepository.actualizar(cliente);
            System.out.println("Cliente actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.out.println("Error: ID inválido.");
        } catch (Exception e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - actualiza cliente con objeto
    public void actualizarCliente(Cliente cliente) {
        try {
            if (cliente == null) {
                throw new IllegalArgumentException("El cliente no puede ser nulo.");
            }
            
            if (cliente.getIdCliente() <= 0) {
                throw new IllegalArgumentException("ID de cliente inválido.");
            }
            
            clienteRepository.actualizar(cliente);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage());
        }
    }

    public void eliminarCliente() {
        try {
            System.out.println("\n=== ELIMINAR CLIENTE ===");
            
            System.out.print("ID del cliente a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Cliente cliente = clienteRepository.obtenerPorId(id);
            if (cliente == null) {
                System.out.println("Error: No se encontró el cliente con ID: " + id);
                return;
            }
            
            System.out.println("Cliente encontrado: " + cliente.getNombre());
            System.out.print("¿Está seguro de que desea eliminar este cliente? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();
            
            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                clienteRepository.eliminar(id);
                System.out.println("Cliente eliminado exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: ID inválido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    public void listarClientes() {
        try {
            System.out.println("\n=== LISTAR CLIENTES ===");
            
            List<Cliente> clientes = clienteRepository.listar();
            
            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
                return;
            }
            
            System.out.printf("%-5s %-30s %-12s %-30s %-20s %-20s%n", 
                "ID", "Nombre", "Documento", "Email", "Teléfono", "Fecha Registro");
            System.out.println("=".repeat(120));
            
            for (Cliente cliente : clientes) {
                System.out.printf("%-5d %-30s %-12s %-30s %-20s %-20s%n",
                    cliente.getIdCliente(),
                    cliente.getNombre(),
                    cliente.getDocumento(),
                    cliente.getEmail() != null ? cliente.getEmail() : "N/A",
                    cliente.getTelefono() != null ? cliente.getTelefono() : "N/A",
                    cliente.getFechaRegistro() != null ? 
                        cliente.getFechaRegistro().toLocalDate().toString() : "N/A"
                );
            }
            
        } catch (Exception e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
    }

    public void buscarCliente() {
        try {
            System.out.println("\n=== BUSCAR CLIENTE ===");
            
            System.out.print("Ingrese criterio de búsqueda (nombre o documento): ");
            String criterio = scanner.nextLine().trim();
            
            if (criterio.isEmpty()) {
                System.out.println("Error: Debe ingresar un criterio de búsqueda.");
                return;
            }
            
            Cliente cliente = clienteRepository.buscar(criterio);
            
            if (cliente == null) {
                System.out.println("No se encontró ningún cliente con el criterio: " + criterio);
                return;
            }
            
            System.out.println("\nCliente encontrado:");
            System.out.println("ID: " + cliente.getIdCliente());
            System.out.println("Nombre: " + cliente.getNombre());
            System.out.println("Documento: " + cliente.getDocumento());
            System.out.println("Email: " + (cliente.getEmail() != null ? cliente.getEmail() : "No especificado"));
            System.out.println("Teléfono: " + (cliente.getTelefono() != null ? cliente.getTelefono() : "No especificado"));
            System.out.println("Fecha de registro: " + (cliente.getFechaRegistro() != null ? 
                cliente.getFechaRegistro().toLocalDate().toString() : "No especificada"));
            
        } catch (Exception e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
    }

    public boolean validarDocumento(String documento) {
        if (documento == null || documento.isEmpty()) {
            return false;
        }
        
        Pattern pattern = Pattern.compile("^\\d{8}$");
        return pattern.matcher(documento).matches();
    }

    private boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
        return pattern.matcher(email).matches();
    }

    public Cliente obtenerCliente(int id) {
        try {
            return clienteRepository.obtenerPorId(id);
        } catch (Exception e) {
            System.out.println("Error al obtener cliente: " + e.getMessage());
            return null;
        }
    }

    public Cliente obtenerClientePorDocumento(String documento) {
        try {
            return clienteRepository.obtenerPorDocumento(documento);
        } catch (Exception e) {
            System.out.println("Error al obtener cliente por documento: " + e.getMessage());
            return null;
        }
    }
    
    // ========== MÉTODOS PARA JAVAFX ==========
    
    // Método para JavaFX - elimina cliente por ID
    public void eliminarCliente(int idCliente) {
        try {
            if (idCliente <= 0) {
                throw new IllegalArgumentException("ID de cliente inválido.");
            }
            
            clienteRepository.eliminar(idCliente);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - retorna lista de clientes
    public List<Cliente> obtenerListaClientes() {
        try {
            return clienteRepository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar clientes: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - busca cliente por criterio
    public Cliente buscarCliente(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar un criterio de búsqueda.");
            }
            
            return clienteRepository.buscar(criterio.trim());
            
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - obtiene el total de clientes
    public int obtenerTotalClientes() {
        try {
            return clienteRepository.contarTotal();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener total de clientes: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - busca clientes
    public List<Cliente> buscarClientes(String criterio) {
        try {
            return clienteRepository.buscarClientes(criterio);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar clientes: " + e.getMessage());
        }
    }
}
