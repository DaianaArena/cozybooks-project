package main.java.com.app.venta;

import java.util.ArrayList;

import main.java.com.app.cliente.Cliente;
//import main.java.com.app.libro.Libro;
import main.java.com.app.libro.audiolibro.Audiolibro;
import main.java.com.app.libro.digital.Digital;
import main.java.com.app.libro.fisico.Fisico;

public class Venta {
   

     private String fecha;
     private Cliente cliente;
     private double monto;
     //private ArrayList < Libro > librosVendidos;
 
    private ArrayList < Audiolibro > listaDeAudiolibros;
    private ArrayList < Digital > listaDeDigital;
    private ArrayList < Fisico > listaDeFisico;
    


    public static int gananciaTotal;

     
     public Venta(String fecha, Cliente cliente, ArrayList < Audiolibro > listaDeAudiolibros, ArrayList < Digital > listaDeDigital, ArrayList < Fisico > listaDeFisico ) {
         this.fecha = fecha;
         this.cliente = cliente;
         //this.librosVendidos = librosVendidos;
         
         this.listaDeAudiolibros = listaDeAudiolibros;
         this.listaDeDigital = listaDeDigital;
         this.listaDeFisico = listaDeFisico;

         double total = 0;
         for (Audiolibro audiolibro : listaDeAudiolibros) {
            total += audiolibro.getPrecio();
         }
         for (Fisico fisico : listaDeFisico) {
            total += fisico.getPrecio();
         }
         for (Digital digital : listaDeDigital) {
            total += digital.getPrecio();
         }


         this.monto = total;

         gananciaTotal += total;
     }

     public Cliente getCliente() {
         return cliente;
     }

     public void setFecha(String fecha) {
         this.fecha = fecha;
     }

     public String getFecha() {
         return fecha;
     }

     public void setMonto(double monto) {
         this.monto = monto;
     }

     public double getMonto() {
         return monto;
     }

    public ArrayList<Audiolibro> getListaDeAudiolibros() {
        return listaDeAudiolibros;
    }

    public void setListaDeAudiolibros(ArrayList<Audiolibro> listaDeAudiolibros) {
        this.listaDeAudiolibros = listaDeAudiolibros;
    }

    public ArrayList<Digital> getListaDeDigital() {
        return listaDeDigital;
    }

    public void setListaDeDigital(ArrayList<Digital> listaDeDigital) {
        this.listaDeDigital = listaDeDigital;
    }
    
    public ArrayList<Fisico> getListaDeFisico() {
        return listaDeFisico;
    }

    public void setListaDeFisico(ArrayList<Fisico> listaDeFisico) {
        this.listaDeFisico = listaDeFisico;
    }
     
     public void mostrarVenta() {{
        System.out.println("Fecha: " + fecha);
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Monto: " + monto);
        System.out.println("Libros vendidos: ");
        
        System.out.println("Audiolibros");
        if (this.listaDeAudiolibros.isEmpty()) {
            System.out.println("No hay libros vendidos");
        } else {
            System.out.println("Lista de libros vendidos:");
            this.listaDeAudiolibros.forEach((audiolibro) -> {
                audiolibro.mostrarLibro();
                System.out.println("///////");
            });
        }

        System.out.println("---------------------------------------");
        System.out.println("Libros Digitales");
        if (this.listaDeDigital.isEmpty()) {
            System.out.println("No hay libros vendidos");
        } else {
            System.out.println("Lista de libros vendidos:");
            this.listaDeDigital.forEach((digital) -> {
                digital.mostrarLibro();
                System.out.println("///////");
            });
        }

        System.out.println("---------------------------------------");
        System.out.println("Libros Fisicos");
        if (this.listaDeFisico.isEmpty()) {
            System.out.println("No hay libros vendidos");
        } else {
            System.out.println("Lista de libros vendidos:");
            this.listaDeFisico.forEach((fisico) -> {
                fisico.mostrarLibro();
                System.out.println("///////");
            });
        }
    
     }}
}
