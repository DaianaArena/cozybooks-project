package main.java.com.app.libro.fisico;

import main.java.com.app.autor.Autor;
import main.java.com.app.libro.Libro;

public class Fisico extends Libro{
    
    
    private String encuadernado;
    private int numDeEdicion;

    

    public String getEncuadernado() {
        return encuadernado;
    }

    public void setEncuadernado(String encuadernado) {
        this.encuadernado = encuadernado;
    }

    public int getNumDeEdicion() {
        return numDeEdicion;
    }

    public void setNumDeEdicion(int numDeEdicion) {
        this.numDeEdicion = numDeEdicion;
    }

    public Fisico(String titulo, Autor autor, String editorial, int año, double precio, String encuadernado,
    int numDeEdicion) {
    super(titulo, autor, editorial, año, precio);
    this.encuadernado = encuadernado;
    this.numDeEdicion = numDeEdicion;
    }

    public void mostrarLibro() {
        
        System.out.println("Titulo: " + this.getTitulo() );
        System.out.println("Autor: " + this.getAutor().getNombre() );
        System.out.println("Editorial: " + this.getEditorial());
        System.out.println("Año: " + this.getAño());
        System.out.println("Precio: " + this.getPrecio());
        System.out.println("Encuadernado: " + this.getEncuadernado());
        System.out.println("Num de edicion: " + this.getNumDeEdicion());

        
    }

    
}
