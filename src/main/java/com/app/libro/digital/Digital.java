package main.java.com.app.libro.digital;

import main.java.com.app.autor.Autor;
import main.java.com.app.libro.Libro;

public class Digital extends Libro {
    
    
    private String extension;
    private Boolean permisosDeImpresion;


    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    

    public Boolean getPermisosDeImpresion() {
        return permisosDeImpresion;
    }

    public void setPermisosDeImpresion(Boolean permisosDeImpresion) {
        this.permisosDeImpresion = permisosDeImpresion;
    }

    public Digital(String titulo, Autor autor, String editorial, int año, double precio, Boolean permisosDeImpresion,String extension ) {
        super(titulo, autor, editorial, año, precio);
        
        this.extension = extension;
        this.permisosDeImpresion = permisosDeImpresion;
    }

    public void mostrarLibro() {
        
        System.out.println("Titulo: " + this.getTitulo() );
        System.out.println("Autor: " + this.getAutor().getNombre() );
        System.out.println("Editorial: " + this.getEditorial());
        System.out.println("Año: " + this.getAño());
        System.out.println("Precio: " + this.getPrecio());
        System.out.println("Permisos de impresion: " + this.getPermisosDeImpresion());
        System.out.println("Extension: " + this.getExtension());

        
    }

} 