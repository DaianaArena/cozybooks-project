package com.cozybooks;

import com.cozybooks.view.CozyBooksMainView;
import com.cozybooks.util.DBConnection;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación Cozy Books
 * Punto de entrada del sistema con interfaz JavaFX
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Iniciando Cozy Books System...");
        
        try {
            // Verificar conexión a la base de datos
            System.out.println("Verificando conexión a la base de datos...");
            DBConnection.getConnection();
            
            if (DBConnection.isConnectionActive()) {
                System.out.println("✓ Conexión a la base de datos establecida correctamente.");
                
                // Iniciar la aplicación JavaFX
                CozyBooksMainView mainView = new CozyBooksMainView();
                mainView.start(primaryStage);
                
            } else {
                System.out.println("✗ Error: No se pudo establecer conexión con la base de datos.");
                showDatabaseError();
                Platform.exit();
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error al iniciar la aplicación: " + e.getMessage());
            showDatabaseError();
            Platform.exit();
        }
    }
    
    private void showDatabaseError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Conexión");
        alert.setHeaderText("No se pudo conectar a la base de datos");
        alert.setContentText("Verifique que:\n" +
                "1. MySQL esté ejecutándose\n" +
                "2. La base de datos 'cozy_books' exista\n" +
                "3. El usuario 'root' tenga los permisos correctos\n" +
                "4. El driver de MySQL esté en el classpath\n\n" +
                "Configuración actual:\n" +
                "- URL: jdbc:mysql://localhost:3306/cozy_books\n" +
                "- Usuario: root\n" +
                "- Contraseña: 1234\n\n" +
                "Asegúrese de haber ejecutado los scripts SQL de configuración.");
        alert.showAndWait();
    }
    
    @Override
    public void stop() throws Exception {
        // Cerrar conexión cuando se cierre la aplicación
        DBConnection.closeConnection();
        System.out.println("Aplicación cerrada. Conexión a la base de datos cerrada.");
        super.stop();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
