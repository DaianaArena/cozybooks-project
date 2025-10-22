package main.java.com.app.interfaceArchivo;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter; 

import main.java.com.app.venta.Venta;

public class Archivo implements IArchivo {
    
    

    public void escribirArchivo(Venta factura) {

      

        
        //factura.mostrarVenta();
        try {
            int num = 1;
            String numTicket = "ticket"+ num + ".txt";
            File ticket = new File(numTicket);
            while(ticket.exists()) {
                numTicket = "ticket"+ (num++) + ".txt";
                ticket = new File(numTicket);
            }


            if (ticket.createNewFile()) {
                FileWriter myWriter = new FileWriter(ticket);
               

                myWriter.write("-----------------------------------------------------" + "\n\n");                
                
                myWriter.write("  _____                 ____              _        " + "\n\n");
                myWriter.write(" / ____|               |  _ \\            | |       " + "\n\n");
                myWriter.write("| |     ___ _____   _  | |_) | ___   ___ | | _____ "+ "\n\n");
                myWriter.write("| |    / _ \\_  / | | | |  _ < / _ \\ / _ \\| |/ / __|"+ "\n\n");
                myWriter.write("| |___| (_) / /| |_| | | |_) | (_) | (_) |   <\\__ \\"+ "\n\n");
                myWriter.write(" \\_____\\___/___|\\__, | |____/ \\___/ \\___/|_|\\_\\___/"+ "\n\n");
                myWriter.write("                __/  |                             "+ "\n\n");
                myWriter.write("               |____/                              "+ "\n\n");
        
                myWriter.write("-----------------------------------------------------"+ "\n\n");
                
                myWriter.write("Fecha: " + factura.getFecha() + "\n\n");
                
                myWriter.write("Cliente: " + factura.getCliente().getNombre() + "\n\n");
                
                 myWriter.write("////////////////////////////////////////////// \n\n");
                myWriter.write("Artículos: " + "\n\n");
                
                
                myWriter.write("Audiolibros" + "\n\n");
                if (!factura.getListaDeAudiolibros().isEmpty()) {
                    
                    factura.getListaDeAudiolibros().forEach((audiolibro) -> {
                        
                        try {
                            myWriter.write("Titulo: " + audiolibro.getTitulo() + " ");
                            myWriter.write("Precio: " + audiolibro.getPrecio() + "\n\n");
                            
                        } catch (IOException e) {
                            System.out.println("No pudieron facturarse los audiolibros");
                            e.printStackTrace();
                        }
                         
                    });
                }
                
                myWriter.write("-----------------------------------------------------"+ "\n\n");
                myWriter.write("Libros Digitales"+ "\n\n");
                if (!factura.getListaDeDigital().isEmpty()) {
                    
                    factura.getListaDeDigital().forEach((digital) -> {
                        try {
                            myWriter.write("Titulo: " + digital.getTitulo()+ " " );
                            myWriter.write("Precio: " + digital.getPrecio()+ "\n\n");

                        } catch (IOException e) {
                            System.out.println("No pudieron facturarse los digital");
                            e.printStackTrace();
                        }
                        
                    });
                }

                myWriter.write("-----------------------------------------------------"+ "\n\n");
                myWriter.write("Libros Fisicos"+ "\n\n");
                
                if (!factura.getListaDeFisico().isEmpty()) {
                    
                    factura.getListaDeFisico().forEach((fisico) -> {
                        try {
                            myWriter.write("Titulo: " + fisico.getTitulo()+ " " );
                            myWriter.write("Precio: " + fisico.getPrecio()+ "\n\n");

                        } catch (IOException e) {
                            System.out.println("No pudieron facturarse los fisico");
                            e.printStackTrace();
                        }
                        
                    });
                }
                
               myWriter.write("-----------------------------------------------------"+ "\n\n");
                myWriter.write("TOTAL A PAGAR:      $"+ factura.getMonto() + "\n\n");
               myWriter.write("-----------------------------------------------------"+ "\n\n");
               
                              
                
                myWriter.close();

                System.out.println("File created: " + ticket.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

   
    
}
