package main.java.com.app.autor;

import java.util.ArrayList;

//import main.java.com.app.libro.Libro;
import main.java.com.app.libro.audiolibro.Audiolibro;
import main.java.com.app.libro.digital.Digital;
import main.java.com.app.libro.fisico.Fisico;

public class Autor {
    
    private String nombre;
    private String fechaNacimiento;
    //private ArrayList < Libro > librosEscritos;

    private ArrayList < Audiolibro > listaDeAudiolibros;
    private ArrayList < Digital > listaDeDigital;
    private ArrayList < Fisico > listaDeFisico;
  

    public Autor(String nombre, String fechaNacimiento ) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
       // this.librosEscritos = new ArrayList<    Libro   >();

        this.listaDeAudiolibros = new ArrayList < Audiolibro > ();
        this.listaDeDigital = new ArrayList < Digital > ();
        this.listaDeFisico = new ArrayList < Fisico > ();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /*public void addLibro (Libro libro){
        this.librosEscritos.add(libro);

    }*/

    public void addAudiolibro (Audiolibro audiolibro){
        this.listaDeAudiolibros.add(audiolibro);

    }

    public void addDigital (Digital digital){
        this.listaDeDigital.add(digital);

    }

    public void addFisico (Fisico fisico){
        this.listaDeFisico.add(fisico);

    }

   

    public void mostrarAutor() {
        System.out.println("Nombre: " + nombre + "\nFecha de nacimiento: " + fechaNacimiento );

        /*
        // validar si la lista de libros comprados no esta vacia
        if (this.librosEscritos.size() > 0) {
            System.out.println("Libros Escritos: ");
            for (Libro libro : librosEscritos) {
                libro.mostrarLibro();
            }
        } else {
            System.out.println("Este Autor no tiene agregado ningun libro en el sistema.");
        }
        */
    }

    public void mostrarLibros() {
        System.out.println("Audiolibros");
        if (this.listaDeAudiolibros.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            System.out.println("Lista de libros registrados:");
            this.listaDeAudiolibros.forEach((audiolibro) -> {
                audiolibro.mostrarLibro();
                System.out.println("///////");
            });
        }

        System.out.println("---------------------------------------");
        System.out.println("Libros Digitales");
        if (this.listaDeDigital.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            System.out.println("Lista de libros registrados:");
            this.listaDeDigital.forEach((digital) -> {
                digital.mostrarLibro();
                System.out.println("///////");
            });
        }

        System.out.println("---------------------------------------");
        System.out.println("Libros Fisicos");
        if (this.listaDeFisico.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            System.out.println("Lista de libros registrados:");
            this.listaDeFisico.forEach((fisico) -> {
                fisico.mostrarLibro();
                System.out.println("///////");
            });
        }
    
    }

}
