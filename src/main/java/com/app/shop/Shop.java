package main.java.com.app.shop;

import java.util.ArrayList;
import java.util.Scanner;

import main.java.com.app.autor.Autor;
import main.java.com.app.cliente.Cliente;
import main.java.com.app.interfaceArchivo.Archivo;
//import main.java.com.app.libro.Libro;
import main.java.com.app.libro.audiolibro.Audiolibro;
import main.java.com.app.libro.digital.Digital;
import main.java.com.app.libro.fisico.Fisico;
import main.java.com.app.venta.Venta;

public class Shop {
    
    private String nombre;
    private String descripcion;
    private ArrayList < Cliente > listaDeClientes;

    //private ArrayList < Libro > listaDeLibros;
    private ArrayList < Audiolibro > listaDeAudiolibros;
    private ArrayList < Digital > listaDeDigital;
    private ArrayList < Fisico > listaDeFisico;


    private ArrayList < Autor > ListaDeAutores;
    private ArrayList < Venta > listaDeVentas;
    private Scanner lector;
    

    //Constructor de la clase Shop
    public Shop(){
        this.nombre = "Cozy Books";
        this.descripcion = "Cozy Books es una tienda en línea de libros fundada en 2013 por una familia apasionada por la lectura. La filosofía de la marca se centra en la promoción de la lectura y la literatura como una forma de enriquecer la mente y el alma de las personas. Desde su creación, Cozy Books ha estado comprometida en ofrecer una amplia variedad de títulos y géneros a precios asequibles, y en proporcionar una experiencia de compra en línea fácil y conveniente para sus clientes. La familia detrás de Cozy Books está formada por lectores ávidos y apasionados por la literatura, y esto se refleja en la selección cuidadosa de los títulos que ofrecen en su tienda en línea. Desde los clásicos hasta las últimas novedades, Cozy Books tiene algo para todos los gustos y edades.";

        this.listaDeClientes = new ArrayList < Cliente > ();
        //this.listaDeLibros = new ArrayList < Libro > ();
        this.listaDeAudiolibros = new ArrayList < Audiolibro > ();
        this.listaDeDigital = new ArrayList < Digital > ();
        this.listaDeFisico = new ArrayList < Fisico > ();

        // listaDeAudiolibros listaDeDigital listaDeFisico

        this.listaDeVentas = new ArrayList < Venta > ();
        this.ListaDeAutores = new ArrayList < Autor > ();

        this.lector = new Scanner(System.in);
    
        

    }

    public void menu (){
        String op;
        System.out.println("-----------------------------------------------------");
        System.out.println("  Bienvenido al Sistema de gestión de ventas de ");
        
        
        System.out.println("  _____                 ____              _        ");
        System.out.println(" / ____|               |  _ \\            | |       ");
        System.out.println("| |     ___ _____   _  | |_) | ___   ___ | | _____ ");
        System.out.println("| |    / _ \\_  / | | | |  _ < / _ \\ / _ \\| |/ / __|");
        System.out.println("| |___| (_) / /| |_| | | |_) | (_) | (_) |   <\\__ \\");
        System.out.println(" \\_____\\___/___|\\__, | |____/ \\___/ \\___/|_|\\_\\___/");
        System.out.println("                __/  |                             ");
        System.out.println("               |____/                              ");
 
        System.out.println("-----------------------------------------------------");
                                                                                                                                   
    
        do {
            System.out.println("-----------------------------------------------------");
            System.out.println("Menu principal");
            System.out.println("-----------------------------------------------------");
            System.out.println("Elija una de las siguientes opciones: ");
            System.out.println("");
            System.out.println("1. Registrar Autor");
            System.out.println("2. Registrar Cliente");
            System.out.println("3. Registrar Libro");
            System.out.println("4. Registrar Venta");
            System.out.println("5. Mostrar Autores");
            System.out.println("6. Mostrar Clientes");
            System.out.println("7. Mostrar Libros");
            System.out.println("8. Mostrar libros por autor");
            System.out.println("9. Mostrar Ventas");
            System.out.println("0. Salir");
            System.out.println("");
            System.out.println("Ingrese su opción:");
            op = lector.nextLine();
            switch (op) {
                case "1":
                this.registrarAutor();
                break;
                case "2":
                    this.registrarCliente();
                    break;
                case "3":
                this.registrarLibro();
                    break;
                case "4":
                this.registrarVenta();
                    break;
                    case "5":
                    this.mostrarAutores();
                    break;
                case "6":
                this.mostrarClientes();
                    break;
                case "7":
                this.mostrarLibros();
                    break;
                case "8":
                this.mostrarLibrosPorAutor();
                break;
                case "9":
                this.mostrarVentas();
                    break;
                case "10":
                this.mostrarInfo();
                break;
                case "0":
                System.out.println("Hasta luego!");
                    break;
    
                default:
                System.out.println("-----------------------");
                System.out.println("| ! |  Opción inválida");
                System.out.println("-----------------------");
                    break;
            }
         
        } while (!op.equals("0"));
        
    }


    private void mostrarInfo() {
        System.out.println("-----------------------");
        System.out.println("| :) | Nosotros:");
        System.out.println("-----------------------");
        System.out.println("Nombre de la tienda: " + this.nombre);
        System.out.println("Sobre nosotros: " + this.descripcion);
    }

    private void mostrarVentas() {
        //double montoTotal = 0;
        if (this.listaDeVentas.isEmpty()) {
            System.out.println("No hay ventas registradas");
        } else {
            System.out.println("------------------------");
            System.out.println("| $ | Ventas registradas");
            System.out.println("------------------------");
            for (Venta venta : listaDeVentas) {
                venta.mostrarVenta();
                //montoTotal += venta.getMonto();
            }


            System.out.println("------------------------------------------------");
            System.out.println("| $ | Total de ventas: " + Venta.gananciaTotal);
            System.out.println("------------------------------------------------");
        }
    }

    private void registrarVenta() {
        String fecha, opcion;

        //ArrayList < Libro > librosVendidos = new ArrayList<   Libro   >();
        ArrayList < Audiolibro > AudiolibrosVendidos = new ArrayList<   Audiolibro   >();
        ArrayList < Fisico > fisicoVendidos = new ArrayList<   Fisico   >();
        ArrayList < Digital > digitalVendidos = new ArrayList<   Digital   >();
 
        System.out.println("Ingresa la fecha actual: ");
        fecha = lector.nextLine();

        System.out.println("Selecciona el cliente:");
            // Mostrar la lista de clientes para elegir uno
            for (int i = 1; i-1< listaDeClientes.size(); i++){
                System.out.println("ID: " + i);
                listaDeClientes.get(i-1).mostrarCliente();
                
            }
            System.out.println("Ingresa el ID del cliente elegido: ");
            int idCliente = lector.nextInt();
            Cliente cliente = listaDeClientes.get(idCliente-1);

        do {

            /* 
            System.out.println("Selecciona el libro vendido:");
             // Mostrar la lista de clientes para elegir uno
             for (int i = 1; i-1< listaDeLibros.size(); i++){
                System.out.println("ID: " + i);
                listaDeLibros.get(i-1).mostrarLibro();
                
            }
            System.out.println("Ingresa el ID del libro elegido: ");
            int idLibro= lector.nextInt();
            Libro libro = listaDeLibros.get(idLibro-1);
            librosVendidos.add(libro);

            System.out.println("El libro fue agregado con éxito.");
            lector.nextLine();
            */
            int tipoLibroVendido;

            do {
                System.out.println("Ingrese la opcion para elegir el tipo de libro vendido:");
                System.out.println("1. Libro Fisico");
                System.out.println("2. Libro Digital");
                System.out.println("3. Libro Audiolibro");

                tipoLibroVendido = lector.nextInt();
                
                // listaDeAudiolibros listaDeDigital listaDeFisico

                switch (tipoLibroVendido) {
                    case 1:
                        System.out.println("Selecciona el libro vendido:");
                        // Mostrar la lista de clientes para elegir uno
                        for (int i = 1; i-1< listaDeFisico.size(); i++){
                            System.out.println("ID: " + i);
                            listaDeFisico.get(i-1).mostrarLibro();
                            
                        }
                        System.out.println("Ingresa el ID del libro elegido: ");
                        int idLibro= lector.nextInt();
                        Fisico fisico = listaDeFisico.get(idLibro-1);
                        fisicoVendidos.add(fisico);
            
                        System.out.println("El libro fue agregado con éxito.");
                        lector.nextLine();

                        break;    
                    case 2:
                        System.out.println("Selecciona el libro vendido:");
                        // Mostrar la lista de clientes para elegir uno
                        for (int i = 1; i-1< listaDeDigital.size(); i++){
                            System.out.println("ID: " + i);
                            listaDeDigital.get(i-1).mostrarLibro();
                            
                        }
                        System.out.println("Ingresa el ID del libro elegido: ");
                        int idLibrodigital= lector.nextInt();
                        Digital digital = listaDeDigital.get(idLibrodigital-1);
                        digitalVendidos.add(digital);
            
                        System.out.println("El libro fue agregado con éxito.");
                        lector.nextLine();
                        break;    
                    case 3:
                        System.out.println("Selecciona el libro vendido:");
                        // Mostrar la lista de clientes para elegir uno
                        for (int i = 1; i-1< listaDeAudiolibros.size(); i++){
                            System.out.println("ID: " + i);
                            listaDeAudiolibros.get(i-1).mostrarLibro();
                            
                        }
                        System.out.println("Ingresa el ID del libro elegido: ");
                        int idaudiolibro= lector.nextInt();
                        Audiolibro audiolibro = listaDeAudiolibros.get(idaudiolibro-1);
                        AudiolibrosVendidos.add(audiolibro);
            
                        System.out.println("El libro fue agregado con éxito.");
                        lector.nextLine();
                        break;    
                
                    default:
                    System.out.println("Opcion no valida");
                    break;
                }
            } while (tipoLibroVendido != 1 && tipoLibroVendido != 2 && tipoLibroVendido != 3);


            System.out.println("¿Desea agregar más libros a esta venta? S/N");   
            opcion = lector.nextLine();
            
        } while (opcion.equalsIgnoreCase("S"));

        
        Venta venta = new Venta( fecha, cliente, AudiolibrosVendidos,  digitalVendidos , fisicoVendidos);
        listaDeVentas.add(venta);
        
        

        /*for (Libro libro : librosVendidos) {
            cliente.addLibrosComprados(libro);
        }*/

        for (Audiolibro audiolibro : AudiolibrosVendidos) {
            cliente.addAudiolibro(audiolibro);
        }

        for (Digital digital : digitalVendidos) {
            cliente.addDigital(digital);
        }

        for ( Fisico fisico : fisicoVendidos) {
            cliente.addFisico(fisico);
        }

        //Imprimir factura a un txt
        Archivo factura = new Archivo();
        factura.escribirArchivo(venta);

        System.out.println("Venta agregada con éxito.");
    }

    private void mostrarLibrosPorAutor() {
        if (this.ListaDeAutores.isEmpty()) {
            System.out.println("No hay autores registrados");
        } else {
            System.out.println("Selecciona el autor del cual deseas conocer sus libros:");
            // Mostrar la lista de autores para elegir uno
            for (int i = 1; i-1< ListaDeAutores.size(); i++){
                System.out.println("ID: " + i);
                ListaDeAutores.get(i-1).mostrarAutor();
                
            }
            System.out.println("Ingresa el ID: ");
            int idBuscado = lector.nextInt();
            Autor autor = ListaDeAutores.get(idBuscado-1);
            //mostrar los libros del autor encontrado
            autor.mostrarLibros();

            lector.nextLine();
        }

    }

    public void registrarCliente() {
        System.out.println("Ingrese el nombre completo del nuevo cliente:");
        String nombre = lector.nextLine();
        String documento="";

        //Valida que el DNI sea un numero entero de 8 cifras
        boolean documentoValido = false;
        while (!documentoValido) {
            System.out.println("Ingrese el documento del nuevo cliente (8 dígitos):");
            documento = lector.nextLine();
            try {
                
                //Chequea que los dni tengan 8 digitos alfanumericos
                if (documento.length() == 8) {
                    documentoValido = true;
                } else {
                    System.out.println("El documento debe tener exactamente 8 dígitos.");

                    // Genera una excepcion 
                    throw new NumberFormatException();                    
                }
            } catch (NumberFormatException e) {
                System.out.println("El documento es inválido.");
            }
        }



        Cliente nuevoCliente = new Cliente(nombre, documento);
        this.listaDeClientes.add(nuevoCliente);
        System.out.println("Cliente registrado con éxito");

    }

    public void mostrarClientes() {
        if (this.listaDeClientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
        } else {
            System.out.println("Lista de clientes registrados:");
            this.listaDeClientes.forEach((cliente) -> {
                cliente.mostrarCliente();
            });
        }
    }

    public void registrarLibro() {
        if (this.ListaDeAutores.isEmpty()) {
            System.out.println("No hay autores registrados para cargar libros");
        } else {

            int tipoDeLibro;
                        

            System.out.println("Ingrese el titulo del nuevo libro:");
            String titulo = lector.nextLine();
            // Mostrar la lista de autores para elegir uno
            System.out.println("-----------------------------------------------------");
            System.out.println("Lista de autores disponibles");
            System.out.println("-----------------------------------------------------");
            for (int i = 1; i-1< ListaDeAutores.size(); i++){
                System.out.println("ID: " + i);
                ListaDeAutores.get(i-1).mostrarAutor();
                
            }
            System.out.println("Ingresa el ID del autor del libro: ");
            int idBuscado = lector.nextInt();
            Autor autor = ListaDeAutores.get(idBuscado-1);

            
            System.out.println("Ingrese el año del libro:");
            int año = lector.nextInt();
            
        System.out.println("Ingrese el precio del libro:");
            double precio = lector.nextDouble();

            lector.nextLine();
            System.out.println("Ingrese la editorial del libro:");
            String editorial = lector.nextLine();

            do {
                System.out.println("Ingrese la opcion para elegir el tipo de libro:");
                System.out.println("1. Libro Fisico");
                System.out.println("2. Libro Digital");
                System.out.println("3. Libro Audiolibro");

                tipoDeLibro = lector.nextInt();
                
                switch (tipoDeLibro) {
                    case 1:
                        System.out.println("Ingrese el encuadernado del libro:");
                        lector.nextLine();
                        String encuadernado = lector.nextLine();
                        System.out.println("Ingrese el num De Edicion del libro:");
                        int numDeEdicion = lector.nextInt();
                        Fisico nuevoFisico = new Fisico(titulo, autor, editorial, año, precio, encuadernado, numDeEdicion);
                        this.listaDeFisico.add(nuevoFisico);

                        autor.addFisico(nuevoFisico);

                        System.out.println("termino el proceso");
                        break;
                    case 2:
                        System.out.println("Ingrese la extension del libro:");
                        lector.nextLine();
                        String extension = lector.nextLine();
                        System.out.println("¿Tiene permisos de impresion del libro? TRUE/FALSE");
                        boolean permisosDeImpresion = lector.nextBoolean();
                        Digital nuevoDigital = new Digital(titulo, autor, editorial, año, precio, permisosDeImpresion, extension);
                        this.listaDeDigital.add(nuevoDigital);

                        autor.addDigital(nuevoDigital);
                        break;

                    case 3:
                        System.out.println("Ingrese la duracion del audio en minutos:");

                        int duracion = lector.nextInt() ;
                        System.out.println("Ingrese la plataforma del audiolibro:");
                        lector.nextLine();
                        String plataforma = lector.nextLine();
                        System.out.println("Ingrese el narrador del audiolibro:");
                        String narrador = lector.nextLine();
                        Audiolibro nuevoAudiolibro = new Audiolibro(titulo, autor, editorial, año, precio, duracion, plataforma, narrador);
                        this.listaDeAudiolibros.add(nuevoAudiolibro);
                        
                        autor.addAudiolibro(nuevoAudiolibro);
                        break;    
                
                    default:
                    System.out.println("Opcion no valida");
                    break;
                }

            } while (tipoDeLibro != 1 && tipoDeLibro != 2 && tipoDeLibro != 3);
            
            


            System.out.println("Libro agregado exitosamente >>TOCAR ENTER PARA VOLVER AL MENU PRINCIPAL<<");
            lector.nextLine();
        }


    }

    public void registrarAutor() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Ingrese el nombre completo del nuevo autor:");
        String nombre = lector.nextLine();
        System.out.println("Ingrese fecha de nacimiento del nuevo autor:");
        String fechaNacimiento = lector.nextLine();
        Autor nuevoAutor = new Autor(nombre, fechaNacimiento);
    
        //agregar autor a la lista
        this.ListaDeAutores.add(nuevoAutor);
        System.out.println("El autor se agregó correctamente.");
    }

    public void mostrarAutores() {
        if (this.ListaDeAutores.isEmpty()) {
            System.out.println("No hay autores registrados");
        } else {
            System.out.println("Lista de autores registrados:");
            this.ListaDeAutores.forEach((autor) -> {
                autor.mostrarAutor();
            });
        }
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
