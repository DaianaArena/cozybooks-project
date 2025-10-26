package com.cozybooks.controller;

import com.cozybooks.model.Autor;
import com.cozybooks.model.Libro;
import com.cozybooks.repository.AutorRepository;
import com.cozybooks.repository.LibroRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class LibroController {
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private Scanner scanner;

    public LibroController() {
        this.libroRepository = new LibroRepository();
        this.autorRepository = new AutorRepository();
        this.scanner = new Scanner(System.in);
    }

    public void registrarLibro() {
        try {
            System.out.println("\n=== REGISTRAR LIBRO ===");
            
            System.out.print("Título del libro: ");
            String titulo = scanner.nextLine().trim();
            if (titulo.isEmpty()) {
                System.out.println("Error: El título es obligatorio.");
                return;
            }
            
            System.out.print("ISBN (opcional): ");
            String isbn = scanner.nextLine().trim();
            if (isbn.isEmpty()) isbn = null;
            
            System.out.print("Editorial: ");
            String editorial = scanner.nextLine().trim();
            if (editorial.isEmpty()) {
                System.out.println("Error: La editorial es obligatoria.");
                return;
            }
            
            System.out.print("Año de publicación: ");
            int año = Integer.parseInt(scanner.nextLine().trim());
            if (año < 1000 || año > 2024) {
                System.out.println("Error: Año inválido.");
                return;
            }
            
            System.out.print("Precio: ");
            BigDecimal precio = new BigDecimal(scanner.nextLine().trim());
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Error: El precio debe ser mayor a 0.");
                return;
            }
            
            System.out.print("Género (opcional): ");
            String genero = scanner.nextLine().trim();
            if (genero.isEmpty()) genero = null;
            
            System.out.println("Tipo de libro:");
            System.out.println("1. Físico");
            System.out.println("2. Digital");
            System.out.println("3. Audiolibro");
            System.out.print("Seleccione (1-3): ");
            int tipoOpcion = Integer.parseInt(scanner.nextLine().trim());
            
            Libro.TipoLibro tipoLibro;
            int stock = 0;
            
            switch (tipoOpcion) {
                case 1:
                    tipoLibro = Libro.TipoLibro.FISICO;
                    System.out.print("Stock inicial: ");
                    stock = Integer.parseInt(scanner.nextLine().trim());
                    if (stock < 0) {
                        System.out.println("Error: El stock no puede ser negativo.");
                        return;
                    }
                    break;
                case 2:
                    tipoLibro = Libro.TipoLibro.DIGITAL;
                    break;
                case 3:
                    tipoLibro = Libro.TipoLibro.AUDIOLIBRO;
                    break;
                default:
                    System.out.println("Error: Opción inválida.");
                    return;
            }
            
            System.out.print("ID del autor: ");
            int idAutor = Integer.parseInt(scanner.nextLine().trim());
            Autor autor = autorRepository.obtenerPorId(idAutor);
            if (autor == null) {
                System.out.println("Error: No se encontró el autor con ID: " + idAutor);
                return;
            }
            
            Libro libro = new Libro(titulo, editorial, año, precio, tipoLibro, idAutor);
            libro.setIsbn(isbn);
            libro.setGenero(genero);
            libro.setStock(stock);
            
            if (tipoLibro == Libro.TipoLibro.FISICO) {
                System.out.print("Tipo de encuadernado: ");
                String encuadernado = scanner.nextLine().trim();
                if (encuadernado.isEmpty()) {
                    System.out.println("Error: El tipo de encuadernado es obligatorio para libros físicos.");
                    return;
                }
                libro.setEncuadernado(encuadernado);
                
                System.out.print("Número de edición: ");
                int numEdicion = Integer.parseInt(scanner.nextLine().trim());
                if (numEdicion <= 0) {
                    System.out.println("Error: El número de edición debe ser mayor a 0.");
                    return;
                }
                libro.setNumEdicion(numEdicion);
                
            } else if (tipoLibro == Libro.TipoLibro.DIGITAL) {
                System.out.print("Extensión del archivo (PDF, EPUB, etc.): ");
                String extension = scanner.nextLine().trim();
                if (extension.isEmpty()) {
                    System.out.println("Error: La extensión es obligatoria para libros digitales.");
                    return;
                }
                libro.setExtension(extension);
                
                System.out.print("¿Permite impresión? (s/n): ");
                String permiteImpresion = scanner.nextLine().trim().toLowerCase();
                libro.setPermisosImpresion(permiteImpresion.equals("s") || permiteImpresion.equals("si"));
                
            } else if (tipoLibro == Libro.TipoLibro.AUDIOLIBRO) {
                System.out.print("Duración en minutos: ");
                int duracion = Integer.parseInt(scanner.nextLine().trim());
                if (duracion <= 0) {
                    System.out.println("Error: La duración debe ser mayor a 0.");
                    return;
                }
                libro.setDuracion(duracion);
                
                System.out.print("Plataforma (Audible, Spotify, etc.): ");
                String plataforma = scanner.nextLine().trim();
                if (plataforma.isEmpty()) {
                    System.out.println("Error: La plataforma es obligatoria para audiolibros.");
                    return;
                }
                libro.setPlataforma(plataforma);
                
                System.out.print("Narrador: ");
                String narrador = scanner.nextLine().trim();
                if (narrador.isEmpty()) {
                    System.out.println("Error: El narrador es obligatorio para audiolibros.");
                    return;
                }
                libro.setNarrador(narrador);
            }
            
            libro = libroRepository.registrar(libro);
            System.out.println("Libro registrado exitosamente con ID: " + libro.getIdLibro());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico inválido.");
        } catch (Exception e) {
            System.out.println("Error al registrar libro: " + e.getMessage());
        }
    }

    public void actualizarLibro() {
        try {
            System.out.println("\n=== ACTUALIZAR LIBRO ===");
            
            System.out.print("ID del libro a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Libro libro = libroRepository.obtenerPorId(id);
            if (libro == null) {
                System.out.println("Error: No se encontró el libro con ID: " + id);
                return;
            }
            
            System.out.println("Libro encontrado: " + libro.getTitulo());
            System.out.println("Ingrese los nuevos datos (presione Enter para mantener el valor actual):");
            
            System.out.print("Título [" + libro.getTitulo() + "]: ");
            String titulo = scanner.nextLine().trim();
            if (!titulo.isEmpty()) {
                libro.setTitulo(titulo);
            }
            
            System.out.print("Precio [" + libro.getPrecio() + "]: ");
            String precioStr = scanner.nextLine().trim();
            if (!precioStr.isEmpty()) {
                BigDecimal precio = new BigDecimal(precioStr);
                if (precio.compareTo(BigDecimal.ZERO) > 0) {
                    libro.setPrecio(precio);
                } else {
                    System.out.println("Error: El precio debe ser mayor a 0. Se mantendrá el precio actual.");
                }
            }
            
            if (libro.getTipoLibro() == Libro.TipoLibro.FISICO) {
                System.out.print("Stock [" + libro.getStock() + "]: ");
                String stockStr = scanner.nextLine().trim();
                if (!stockStr.isEmpty()) {
                    int stock = Integer.parseInt(stockStr);
                    if (stock >= 0) {
                        libro.setStock(stock);
                    } else {
                        System.out.println("Error: El stock no puede ser negativo. Se mantendrá el stock actual.");
                    }
                }
            }
            
            libroRepository.actualizar(libro);
            System.out.println("Libro actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico inválido.");
        } catch (Exception e) {
            System.out.println("Error al actualizar libro: " + e.getMessage());
        }
    }

    public void eliminarLibro() {
        try {
            System.out.println("\n=== ELIMINAR LIBRO ===");
            
            System.out.print("ID del libro a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Libro libro = libroRepository.obtenerPorId(id);
            if (libro == null) {
                System.out.println("Error: No se encontró el libro con ID: " + id);
                return;
            }
            
            System.out.println("Libro encontrado: " + libro.getTitulo());
            System.out.print("¿Está seguro de que desea eliminar este libro? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();
            
            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                libroRepository.eliminar(id);
                System.out.println("Libro eliminado exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: ID inválido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());
        }
    }

    public void listarLibros() {
        try {
            System.out.println("\n=== LISTAR LIBROS ===");
            
            List<Libro> libros = libroRepository.listar();
            
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados.");
                return;
            }
            
            System.out.printf("%-5s %-40s %-20s %-15s %-8s %-10s %-15s %-8s%n", 
                "ID", "Título", "Autor ID", "Editorial", "Año", "Precio", "Tipo", "Stock");
            System.out.println("=".repeat(130));
            
            for (Libro libro : libros) {
                System.out.printf("%-5d %-40s %-20d %-15s %-8d %-10.2f %-15s %-8d%n",
                    libro.getIdLibro(),
                    libro.getTitulo().length() > 40 ? libro.getTitulo().substring(0, 37) + "..." : libro.getTitulo(),
                    libro.getIdAutor(),
                    libro.getEditorial(),
                    libro.getAño(),
                    libro.getPrecio(),
                    libro.getTipoLibro(),
                    libro.getStock()
                );
            }
            
        } catch (Exception e) {
            System.out.println("Error al listar libros: " + e.getMessage());
        }
    }

    public void buscarLibro() {
        try {
            System.out.println("\n=== BUSCAR LIBRO ===");
            
            System.out.print("Ingrese criterio de búsqueda (título, ISBN o género): ");
            String criterio = scanner.nextLine().trim();
            
            if (criterio.isEmpty()) {
                System.out.println("Error: Debe ingresar un criterio de búsqueda.");
                return;
            }
            
            List<Libro> libros = libroRepository.buscar(criterio);
            
            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros con el criterio: " + criterio);
                return;
            }
            
            System.out.println("\nLibros encontrados:");
            System.out.printf("%-5s %-40s %-20s %-15s %-8s %-10s %-15s%n", 
                "ID", "Título", "Autor ID", "Editorial", "Año", "Precio", "Tipo");
            System.out.println("=".repeat(120));
            
            for (Libro libro : libros) {
                System.out.printf("%-5d %-40s %-20d %-15s %-8d %-10.2f %-15s%n",
                    libro.getIdLibro(),
                    libro.getTitulo().length() > 40 ? libro.getTitulo().substring(0, 37) + "..." : libro.getTitulo(),
                    libro.getIdAutor(),
                    libro.getEditorial(),
                    libro.getAño(),
                    libro.getPrecio(),
                    libro.getTipoLibro()
                );
            }
            
        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }
    }

    public void actualizarStock(int idLibro, int cantidad) {
        try {
            libroRepository.actualizarStock(idLibro, cantidad);
        } catch (Exception e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
        }
    }

    public Libro obtenerLibro(int id) {
        try {
            return libroRepository.obtenerPorId(id);
        } catch (Exception e) {
            System.out.println("Error al obtener libro: " + e.getMessage());
            return null;
        }
    }

    public List<Libro> buscarPorAutor(int idAutor) {
        try {
            return libroRepository.buscarPorAutor(idAutor);
        } catch (Exception e) {
            System.out.println("Error al buscar libros por autor: " + e.getMessage());
            return null;
        }
    }
    
    // ========== MÉTODOS PARA JAVAFX ==========
    
    // Método para JavaFX - registra libro con objeto
    public Libro registrarLibro(Libro libro) {
        try {
            if (libro == null) {
                throw new IllegalArgumentException("El libro no puede ser nulo.");
            }
            
            return libroRepository.registrar(libro);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar libro: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - actualiza libro con objeto
    public void actualizarLibro(Libro libro) {
        try {
            if (libro == null) {
                throw new IllegalArgumentException("El libro no puede ser nulo.");
            }
            
            if (libro.getIdLibro() <= 0) {
                throw new IllegalArgumentException("ID de libro inválido.");
            }
            
            libroRepository.actualizar(libro);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar libro: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - elimina libro por ID
    public void eliminarLibro(int idLibro) {
        try {
            if (idLibro <= 0) {
                throw new IllegalArgumentException("ID de libro inválido.");
            }
            
            libroRepository.eliminar(idLibro);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar libro: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - retorna lista de libros
    public List<Libro> obtenerListaLibros() {
        try {
            return libroRepository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar libros: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - busca libros por criterio
    public List<Libro> buscarLibros(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar un criterio de búsqueda.");
            }
            
            return libroRepository.buscar(criterio.trim());
            
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros: " + e.getMessage());
        }
    }
    
    // Método para JavaFX - obtiene el total de libros
    public int obtenerTotalLibros() {
        try {
            return libroRepository.contarTotal();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener total de libros: " + e.getMessage());
        }
    }
}
