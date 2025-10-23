package com.cozybooks.controller;

import com.cozybooks.model.Autor;
import com.cozybooks.model.Libro;
import com.cozybooks.repository.AutorRepository;
import com.cozybooks.repository.LibroRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class AutorController {
    private AutorRepository autorRepository;
    private LibroRepository libroRepository;
    private Scanner scanner;

    public AutorController() {
        this.autorRepository = new AutorRepository();
        this.libroRepository = new LibroRepository();
        this.scanner = new Scanner(System.in);
    }

    public void registrarAutor() {
        try {
            System.out.println("\n=== REGISTRAR AUTOR ===");
            
            System.out.print("Nombre del autor: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre es obligatorio.");
                return;
            }
            
            System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
            String fechaStr = scanner.nextLine().trim();
            LocalDate fechaNacimiento;
            try {
                fechaNacimiento = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha inválido. Use YYYY-MM-DD");
                return;
            }
            
            System.out.print("Nacionalidad (opcional): ");
            String nacionalidad = scanner.nextLine().trim();
            if (nacionalidad.isEmpty()) nacionalidad = null;
            
            System.out.print("Biografía (opcional): ");
            String biografia = scanner.nextLine().trim();
            if (biografia.isEmpty()) biografia = null;
            
            Autor autor = new Autor(nombre, fechaNacimiento, nacionalidad, biografia);
            autor = autorRepository.registrar(autor);
            
            System.out.println("Autor registrado exitosamente con ID: " + autor.getIdAutor());
            
        } catch (Exception e) {
            System.out.println("Error al registrar autor: " + e.getMessage());
        }
    }

    public void actualizarAutor() {
        try {
            System.out.println("\n=== ACTUALIZAR AUTOR ===");
            
            System.out.print("ID del autor a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Autor autor = autorRepository.obtenerPorId(id);
            if (autor == null) {
                System.out.println("Error: No se encontró el autor con ID: " + id);
                return;
            }
            
            System.out.println("Autor encontrado: " + autor.getNombre());
            System.out.println("Ingrese los nuevos datos (presione Enter para mantener el valor actual):");
            
            System.out.print("Nombre [" + autor.getNombre() + "]: ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                autor.setNombre(nombre);
            }
            
            System.out.print("Fecha de nacimiento [" + autor.getFechaNacimiento() + "] (YYYY-MM-DD): ");
            String fechaStr = scanner.nextLine().trim();
            if (!fechaStr.isEmpty()) {
                try {
                    autor.setFechaNacimiento(LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE));
                } catch (DateTimeParseException e) {
                    System.out.println("Error: Formato de fecha inválido. Se mantendrá la fecha actual.");
                }
            }
            
            System.out.print("Nacionalidad [" + (autor.getNacionalidad() != null ? autor.getNacionalidad() : "sin especificar") + "]: ");
            String nacionalidad = scanner.nextLine().trim();
            if (!nacionalidad.isEmpty()) {
                autor.setNacionalidad(nacionalidad);
            }
            
            System.out.print("Biografía [" + (autor.getBiografia() != null ? autor.getBiografia() : "sin especificar") + "]: ");
            String biografia = scanner.nextLine().trim();
            if (!biografia.isEmpty()) {
                autor.setBiografia(biografia);
            }
            
            autorRepository.actualizar(autor);
            System.out.println("Autor actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.out.println("Error: ID inválido.");
        } catch (Exception e) {
            System.out.println("Error al actualizar autor: " + e.getMessage());
        }
    }

    public void eliminarAutor() {
        try {
            System.out.println("\n=== ELIMINAR AUTOR ===");
            
            System.out.print("ID del autor a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Autor autor = autorRepository.obtenerPorId(id);
            if (autor == null) {
                System.out.println("Error: No se encontró el autor con ID: " + id);
                return;
            }
            
            System.out.println("Autor encontrado: " + autor.getNombre());
            System.out.print("¿Está seguro de que desea eliminar este autor? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();
            
            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                autorRepository.eliminar(id);
                System.out.println("Autor eliminado exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: ID inválido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar autor: " + e.getMessage());
        }
    }

    public void listarAutores() {
        try {
            System.out.println("\n=== LISTAR AUTORES ===");
            
            List<Autor> autores = autorRepository.listar();
            
            if (autores.isEmpty()) {
                System.out.println("No hay autores registrados.");
                return;
            }
            
            System.out.printf("%-5s %-30s %-12s %-20s %-50s%n", 
                "ID", "Nombre", "Nacimiento", "Nacionalidad", "Biografía");
            System.out.println("=".repeat(120));
            
            for (Autor autor : autores) {
                System.out.printf("%-5d %-30s %-12s %-20s %-50s%n",
                    autor.getIdAutor(),
                    autor.getNombre(),
                    autor.getFechaNacimiento(),
                    autor.getNacionalidad() != null ? autor.getNacionalidad() : "N/A",
                    autor.getBiografia() != null ? 
                        (autor.getBiografia().length() > 50 ? 
                            autor.getBiografia().substring(0, 47) + "..." : 
                            autor.getBiografia()) : "N/A"
                );
            }
            
        } catch (Exception e) {
            System.out.println("Error al listar autores: " + e.getMessage());
        }
    }

    public void buscarAutor() {
        try {
            System.out.println("\n=== BUSCAR AUTOR ===");
            
            System.out.print("Ingrese criterio de búsqueda (nombre): ");
            String criterio = scanner.nextLine().trim();
            
            if (criterio.isEmpty()) {
                System.out.println("Error: Debe ingresar un criterio de búsqueda.");
                return;
            }
            
            Autor autor = autorRepository.buscar(criterio);
            
            if (autor == null) {
                System.out.println("No se encontró ningún autor con el criterio: " + criterio);
                return;
            }
            
            System.out.println("\nAutor encontrado:");
            System.out.println("ID: " + autor.getIdAutor());
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Fecha de nacimiento: " + autor.getFechaNacimiento());
            System.out.println("Nacionalidad: " + (autor.getNacionalidad() != null ? autor.getNacionalidad() : "No especificada"));
            System.out.println("Biografía: " + (autor.getBiografia() != null ? autor.getBiografia() : "No especificada"));
            
        } catch (Exception e) {
            System.out.println("Error al buscar autor: " + e.getMessage());
        }
    }

    public void reporteLibrosPorAutor() {
        try {
            System.out.println("\n=== REPORTE DE LIBROS POR AUTOR ===");
            
            System.out.print("ID del autor: ");
            int idAutor = Integer.parseInt(scanner.nextLine().trim());
            
            Autor autor = autorRepository.obtenerPorId(idAutor);
            if (autor == null) {
                System.out.println("Error: No se encontró el autor con ID: " + idAutor);
                return;
            }
            
            List<Libro> libros = libroRepository.buscarPorAutor(idAutor);
            
            System.out.println("\nAutor: " + autor.getNombre());
            System.out.println("Nacionalidad: " + (autor.getNacionalidad() != null ? autor.getNacionalidad() : "No especificada"));
            System.out.println("Fecha de nacimiento: " + autor.getFechaNacimiento());
            
            if (libros.isEmpty()) {
                System.out.println("\nEste autor no tiene libros registrados.");
                return;
            }
            
            System.out.println("\nLibros del autor:");
            System.out.printf("%-5s %-40s %-15s %-8s %-10s %-15s %-8s%n", 
                "ID", "Título", "Editorial", "Año", "Precio", "Tipo", "Stock");
            System.out.println("=".repeat(110));
            
            for (Libro libro : libros) {
                System.out.printf("%-5d %-40s %-15s %-8d %-10.2f %-15s %-8d%n",
                    libro.getIdLibro(),
                    libro.getTitulo().length() > 40 ? libro.getTitulo().substring(0, 37) + "..." : libro.getTitulo(),
                    libro.getEditorial(),
                    libro.getAño(),
                    libro.getPrecio(),
                    libro.getTipoLibro(),
                    libro.getStock()
                );
            }
            
            System.out.println("\nResumen:");
            System.out.println("Total de libros: " + libros.size());
            System.out.println("Libros físicos: " + libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.FISICO).count());
            System.out.println("Libros digitales: " + libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.DIGITAL).count());
            System.out.println("Audiolibros: " + libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.AUDIOLIBRO).count());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: ID inválido.");
        } catch (Exception e) {
            System.out.println("Error al generar reporte: " + e.getMessage());
        }
    }

    public Autor obtenerAutor(int id) {
        try {
            return autorRepository.obtenerPorId(id);
        } catch (Exception e) {
            System.out.println("Error al obtener autor: " + e.getMessage());
            return null;
        }
    }
}
