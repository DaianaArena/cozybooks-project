package com.cozybooks.view;

import com.cozybooks.controller.AutorController;
import com.cozybooks.controller.ClienteController;
import com.cozybooks.controller.LibroController;
import com.cozybooks.controller.VentaController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Vista principal de la aplicación Cozy Books con interfaz JavaFX
 * Reemplaza MenuView.java manteniendo todas las funcionalidades
 */
public class CozyBooksMainView extends Application {
    
    private Stage primaryStage;
    private BorderPane root;
    private VBox menuContainer;
    private StackPane contentArea;
    
    // Controladores
    private AutorController autorController;
    private ClienteController clienteController;
    private LibroController libroController;
    private VentaController ventaController;
    
    // Vistas específicas
    private AutorView autorView;
    private ClienteView clienteView;
    private LibroView libroView;
    private VentaView ventaView;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Cozy Books System - Sistema de Gestión de Librería");
        this.primaryStage.setMinWidth(1000);
        this.primaryStage.setMinHeight(700);
        
        // Inicializar controladores
        autorController = new AutorController();
        clienteController = new ClienteController();
        libroController = new LibroController();
        ventaController = new VentaController();
        
        // Inicializar vistas
        autorView = new AutorView(autorController);
        clienteView = new ClienteView(clienteController);
        libroView = new LibroView(libroController);
        ventaView = new VentaView(ventaController);
        
        setupUI();
        
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Mostrar vista de bienvenida inicialmente
        showWelcomeView();
    }
    
    private void setupUI() {
        root = new BorderPane();
        
        // Crear el menú lateral
        createMenuBar();
        
        // Área de contenido
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        root.setCenter(contentArea);
        
        // Configurar el layout
        root.setLeft(menuContainer);
        BorderPane.setMargin(menuContainer, new Insets(10));
    }
    
    private void createMenuBar() {
        menuContainer = new VBox(10);
        menuContainer.setPadding(new Insets(20));
        menuContainer.setPrefWidth(250);
        menuContainer.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 10;");
        
        // Título
        Text title = new Text("COZY BOOKS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);");
        
        VBox titleContainer = new VBox(title);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(0, 0, 30, 0));
        
        // Botones del menú
        Button autoresBtn = createMenuButton("📚 Autores", this::showAutorView);
        Button clientesBtn = createMenuButton("👥 Clientes", this::showClienteView);
        Button librosBtn = createMenuButton("📖 Libros", this::showLibroView);
        Button ventasBtn = createMenuButton("💰 Ventas", this::showVentaView);
        Button salirBtn = createMenuButton("🚪 Salir", this::exitApplication);
        
        // Estilo especial para el botón de salir
        salirBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;");
        salirBtn.setOnMouseEntered(e -> salirBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;"));
        salirBtn.setOnMouseExited(e -> salirBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;"));
        
        menuContainer.getChildren().addAll(
            titleContainer,
            autoresBtn,
            clientesBtn,
            librosBtn,
            ventasBtn,
            new Separator(),
            salirBtn
        );
    }
    
    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;");
        
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;"));
        
        button.setOnAction(e -> action.run());
        
        return button;
    }
    
    private void showWelcomeView() {
        VBox welcomeContainer = new VBox(20);
        welcomeContainer.setAlignment(Pos.CENTER);
        welcomeContainer.setPadding(new Insets(50));
        
        Text welcomeTitle = new Text("¡Bienvenido a Cozy Books System!");
        welcomeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        welcomeTitle.setFill(Color.web("#2c3e50"));
        
        Text subtitle = new Text("Sistema de Gestión de Librería");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        subtitle.setFill(Color.web("#7f8c8d"));
        
        Text description = new Text("Seleccione una opción del menú lateral para comenzar a gestionar su librería");
        description.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        description.setFill(Color.web("#95a5a6"));
        description.setWrappingWidth(400);
        
        VBox textContainer = new VBox(10, welcomeTitle, subtitle, description);
        textContainer.setAlignment(Pos.CENTER);
        
        welcomeContainer.getChildren().add(textContainer);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(welcomeContainer);
    }
    
    private void showAutorView() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(autorView.getView());
    }
    
    private void showClienteView() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(clienteView.getView());
    }
    
    private void showLibroView() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(libroView.getView());
    }
    
    private void showVentaView() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(ventaView.getView());
    }
    
    private void exitApplication() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Salida");
        alert.setHeaderText("¿Está seguro de que desea salir?");
        alert.setContentText("Se cerrará la aplicación Cozy Books System.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                primaryStage.close();
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
