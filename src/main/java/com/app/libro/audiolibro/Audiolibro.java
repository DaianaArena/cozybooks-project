package main.java.com.app.libro.audiolibro;

import main.java.com.app.autor.Autor;
import main.java.com.app.libro.Libro;

public class Audiolibro extends Libro{
    
    


    private int duracion; //en minutos
    private String plataforma;
    private String narrador;


    public int getDuracion() {
        return duracion;
    }




    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }




    public String getPlataforma() {
        return plataforma;
    }




    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }




    public String getNarrador() {
        return narrador;
    }




    public void setNarrador(String narrador) {
        this.narrador = narrador;
    }
    
    public Audiolibro(String titulo, Autor autor, String editorial, int año, double precio, int duracion, String plataforma, String narrador) {
        super(titulo, autor, editorial, año, precio);
        
        
        this.duracion = duracion;
        this.plataforma = plataforma;
        this.narrador = narrador;
    }

   
    
    
    public void mostrarLibro() {
        
        System.out.println("Titulo: " + this.getTitulo() );
        System.out.println("Autor: " + this.getAutor().getNombre() );
        System.out.println("Editorial: " + this.getEditorial());
        System.out.println("Año: " + this.getAño());
        System.out.println("Precio: " + this.getPrecio());
        System.out.println("Duracion: " + this.getDuracion() + " minutos");
        System.out.println("Plataforma: " + this.getPlataforma());
        System.out.println("Narrador: " + this.getNarrador());

        
    }




    


}
