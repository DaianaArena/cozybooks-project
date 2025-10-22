package main.java.com.app.libro;

import main.java.com.app.autor.Autor;

public abstract class Libro {

    private String titulo;
    private Autor autor;
    private String editorial;
    private int año;
    private double precio;

    public Libro(String titulo, Autor autor, String editorial, int año, double precio) {
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.año = año;
        this.precio = precio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public abstract void mostrarLibro();
        /*{System.out.println("Titulo: " + titulo);
        System.out.println("Autor: " + autor.getNombre());
        System.out.println("Editorial: " + editorial);
        System.out.println("Año: " + año);
        System.out.println("Precio: " + precio);}*/
    

}
