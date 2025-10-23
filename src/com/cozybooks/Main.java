package com.cozybooks;

import com.cozybooks.view.MenuView;
import com.cozybooks.util.DBConnection;

/**
 * Clase principal de la aplicación Cozy Books
 * Punto de entrada del sistema
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Cozy Books System...");
        
        try {
            // Verificar conexión a la base de datos
            System.out.println("Verificando conexión a la base de datos...");
            DBConnection.getConnection();
            
            if (DBConnection.isConnectionActive()) {
                System.out.println("✓ Conexión a la base de datos establecida correctamente.");
                
                // Iniciar la aplicación
                MenuView menuView = new MenuView();
                menuView.iniciar();
                
            } else {
                System.out.println("✗ Error: No se pudo establecer conexión con la base de datos.");
                System.out.println("Verifique que:");
                System.out.println("1. MySQL esté ejecutándose");
                System.out.println("2. La base de datos 'cozy_books' exista");
                System.out.println("3. El usuario 'root' tenga los permisos correctos");
                System.out.println("4. El driver de MySQL esté en el classpath");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error al iniciar la aplicación: " + e.getMessage());
            System.out.println("\nVerifique la configuración de la base de datos:");
            System.out.println("- URL: jdbc:mysql://localhost:3306/cozy_books");
            System.out.println("- Usuario: root");
            System.out.println("- Contraseña: 1234");
            System.out.println("\nAsegúrese de haber ejecutado los scripts SQL de configuración.");
        } finally {
            // Cerrar conexión
            DBConnection.closeConnection();
        }
    }
}
