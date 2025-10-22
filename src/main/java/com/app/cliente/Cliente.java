package main.java.com.app.cliente;

import java.util.ArrayList;

//import main.java.com.app.libro.Libro;
import main.java.com.app.libro.audiolibro.Audiolibro;
import main.java.com.app.libro.digital.Digital;
import main.java.com.app.libro.fisico.Fisico;

public class Cliente {
    /*
     * agregar los atributos privados nombre, documento y un ArrayList lista_De_Libros_Comprados
     * agregar los respectivos get, set y el constructor
     * agregar un metodo mostrarCliente
     */
    private String nombre;
    private String documento;

    //private ArrayList < Libro > librosComprados;

    private ArrayList < Audiolibro > listaDeAudiolibros;
    private ArrayList < Digital > listaDeDigital;
    private ArrayList < Fisico > listaDeFisico;

    public Cliente(String nombre, String documento) {
        this.nombre = nombre;
        this.documento = documento;
        //this.librosComprados = new ArrayList<   Libro   >();

        this.listaDeAudiolibros = new ArrayList < Audiolibro > ();
        this.listaDeDigital = new ArrayList < Digital > ();
        this.listaDeFisico = new ArrayList < Fisico > ();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }

    /*public void addLibrosComprados (Libro libro){
        this.librosComprados.add(libro);

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
    
    public void mostrarCliente() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Documento: " + documento);
        
        // validar si la lista de libros comprados no esta vacia
        System.out.println("Audiolibros");
        if (this.listaDeAudiolibros.isEmpty()) {
            System.out.println("No hay libros comprados");
        } else {
            System.out.println("Lista de libros comprados:");
            this.listaDeAudiolibros.forEach((audiolibro) -> {
                audiolibro.mostrarLibro();
                System.out.println("///////");
            });
        }

        System.out.println("---------------------------------------");
        System.out.println("Libros Digitales");
        if (this.listaDeDigital.isEmpty()) {
            System.out.println("No hay libros comprados");
        } else {
            System.out.println("Lista de libros comprados:");
            this.listaDeDigital.forEach((digital) -> {
                digital.mostrarLibro();
                System.out.println("///////");
            });
        }

        System.out.println("---------------------------------------");
        System.out.println("Libros Fisicos");
        if (this.listaDeFisico.isEmpty()) {
            System.out.println("No hay libros comprados");
        } else {
            System.out.println("Lista de libros comprados:");
            this.listaDeFisico.forEach((fisico) -> {
                fisico.mostrarLibro();
                System.out.println("///////");
            });
        }
    

    }
}
