package com.cozybooks.view;

import com.cozybooks.controller.AutorController;
import com.cozybooks.controller.ClienteController;
import com.cozybooks.controller.LibroController;
import com.cozybooks.controller.VentaController;
import com.cozybooks.model.Autor;
import com.cozybooks.model.Libro;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.List;

/**
 * Vista principal de la aplicación Cozy Books con interfaz JavaFX moderna
 * Dashboard con diseño responsivo y paleta de colores suave
 */
public class CozyBooksMainView extends Application {
    
    private Stage primaryStage;
    private BorderPane root;
    private VBox sidebar;
    private VBox contentArea;
    
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
    
    // Botones del menú para controlar el estado activo
    private List<Button> menuButtons;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // No establecer título ni dimensiones aquí, se configuran en Main.java
        
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
        
        // Mostrar dashboard por defecto
        showDashboard();
    }
    
    private void setupUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #faf8d4;");
        
        // Crear header
        HBox header = createHeader();
        root.setTop(header);
        
        // Crear sidebar
        sidebar = createSidebar();
        root.setLeft(sidebar);
        
        // Crear área de contenido
        contentArea = new VBox();
        contentArea.setStyle("-fx-background-color: #faf8d4;");
        contentArea.setPadding(new Insets(20));
        root.setCenter(contentArea);
        
        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("⚠ Advertencia: No se pudo cargar el archivo CSS: " + e.getMessage());
        }
        primaryStage.setScene(scene);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #ede3e9; -fx-padding: 15 20;");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);
        
        // Logo con icono
        HBox logoContainer = new HBox(10);
        logoContainer.setAlignment(Pos.CENTER_LEFT);
        
        try {
            Image bookIcon = new Image(getClass().getResourceAsStream("assets/book-icon.png"));
            ImageView bookIconView = new ImageView(bookIcon);
            bookIconView.setFitWidth(32);
            bookIconView.setFitHeight(32);
            bookIconView.setPreserveRatio(true);
            logoContainer.getChildren().add(bookIconView);
        } catch (Exception e) {
            System.out.println("⚠ Advertencia: No se pudo cargar el icono del libro: " + e.getMessage());
        }
        
        Text logo = new Text("CozyBooks");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logo.setStyle("-fx-fill: #181818;");
        
        logoContainer.getChildren().add(logo);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Iconos del header
        HBox headerIcons = new HBox(15);
        headerIcons.setAlignment(Pos.CENTER_RIGHT);
        
        // Icono de notificaciones
        Button notificationBtn = createIconButton("🔔", "#181818");
        
        // Icono de configuración
        Button settingsBtn = createIconButton("⚙️", "#181818");
        
        // Usuario
        HBox userInfo = new HBox(8);
        userInfo.setAlignment(Pos.CENTER);
        Button userBtn = createIconButton("👤", "#181818");
        Text userText = new Text("Admin");
        userText.setFont(Font.font("Arial", 14));
        userText.setStyle("-fx-fill: #181818;");
        userInfo.getChildren().addAll(userBtn, userText);
        
        headerIcons.getChildren().addAll(notificationBtn, settingsBtn, userInfo);
        header.getChildren().addAll(logoContainer, spacer, headerIcons);
        
        return header;
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #ede3e9; -fx-padding: 20;");
        sidebar.setSpacing(10);
        sidebar.setPrefWidth(250);
        
        // Elementos del menú
        Button dashboardBtn = createMenuButton("📊 Dashboard", true);
        Button librosBtn = createMenuButton("📚 Gestión de Libros", false);
        Button autoresBtn = createMenuButton("👤 Gestión de Autores", false);
        Button clientesBtn = createMenuButton("👥 Gestión de Clientes", false);
        Button ventasBtn = createMenuButton("🛒 Gestión de Ventas", false);
        Button reportesBtn = createMenuButton("📈 Reportes", false);
        
        // Lista de botones para manejar la selección
        this.menuButtons = List.of(dashboardBtn, librosBtn, autoresBtn, clientesBtn, ventasBtn, reportesBtn);
        
        // Eventos de los botones
        dashboardBtn.setOnAction(e -> {
            setActiveButton(menuButtons, dashboardBtn);
            showDashboard();
        });
        librosBtn.setOnAction(e -> {
            setActiveButton(menuButtons, librosBtn);
            showLibros();
        });
        autoresBtn.setOnAction(e -> {
            setActiveButton(menuButtons, autoresBtn);
            showAutores();
        });
        clientesBtn.setOnAction(e -> {
            setActiveButton(menuButtons, clientesBtn);
            showClientes();
        });
        ventasBtn.setOnAction(e -> {
            setActiveButton(menuButtons, ventasBtn);
            showVentas();
        });
        reportesBtn.setOnAction(e -> {
            setActiveButton(menuButtons, reportesBtn);
            showReportes();
        });
        
        sidebar.getChildren().addAll(dashboardBtn, librosBtn, autoresBtn, clientesBtn, ventasBtn, reportesBtn);
        
        return sidebar;
    }
    
    private Button createMenuButton(String text, boolean isActive) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(45);
        button.setFont(Font.font("Arial", 14));
        button.setAlignment(Pos.CENTER_LEFT);
        
        // Agregar una propiedad para rastrear si el botón está activo
        button.getProperties().put("isActive", isActive);
        
        if (isActive) {
            button.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: #fafafa; -fx-background-radius: 10; -fx-border-radius: 10;");
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: #181818; -fx-background-radius: 10; -fx-border-radius: 10;");
        }
        
        button.setOnMouseEntered(e -> {
            Boolean active = (Boolean) button.getProperties().get("isActive");
            if (!active) {
                button.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: #fafafa; -fx-background-radius: 10; -fx-border-radius: 10;");
            }
        });
        
        button.setOnMouseExited(e -> {
            Boolean active = (Boolean) button.getProperties().get("isActive");
            if (!active) {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: #181818; -fx-background-radius: 10; -fx-border-radius: 10;");
            }
        });
        
        return button;
    }
    
    private void setActiveButton(List<Button> menuButtons, Button activeButton) {
        for (Button button : menuButtons) {
            if (button == activeButton) {
                // Botón activo
                button.getProperties().put("isActive", true);
                button.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: #fafafa; -fx-background-radius: 10; -fx-border-radius: 10;");
            } else {
                // Botones inactivos
                button.getProperties().put("isActive", false);
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: #181818; -fx-background-radius: 10; -fx-border-radius: 10;");
            }
        }
    }
    
    private Button createIconButton(String icon, String color) {
        Button button = new Button(icon);
        button.setStyle("-fx-background-color: transparent; -fx-font-size: 16; -fx-text-fill: " + color + ";");
        button.setPrefSize(40, 40);
        return button;
    }
    
    private void showDashboard() {
        contentArea.getChildren().clear();
        
        // Título del dashboard
        VBox titleSection = new VBox(5);
        Text title = new Text("Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-fill: #181818;");
        
        Text subtitle = new Text("Bienvenido al sistema de gestión de CozyBooks");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setStyle("-fx-fill: #181818;");
        
        titleSection.getChildren().addAll(title, subtitle);
        contentArea.getChildren().add(titleSection);
        
        // Tarjetas de resumen
        HBox summaryCards = createSummaryCards();
        contentArea.getChildren().add(summaryCards);
        
        // Sección inferior
        HBox bottomSection = new HBox(20);
        bottomSection.setSpacing(20);
        bottomSection.getStyleClass().add("bottom-section-container");
        
        // Libros recientes
        VBox recentBooks = createRecentBooksSection();
        recentBooks.getStyleClass().add("libros-recientes");
        
        // Acciones rápidas
        VBox quickActions = createQuickActionsSection();
        quickActions.getStyleClass().add("acciones-rapidas");
        
        // Hacer que ambas secciones ocupen el mismo espacio (50% cada una)
        HBox.setHgrow(recentBooks, Priority.ALWAYS);
        HBox.setHgrow(quickActions, Priority.ALWAYS);
        
        bottomSection.getChildren().addAll(recentBooks, quickActions);
        contentArea.getChildren().add(bottomSection);
    }
    
    private HBox createSummaryCards() {
        HBox cardsContainer = new HBox(20);
        cardsContainer.setPadding(new Insets(20, 0, 40, 0)); // Aumentado margen inferior de 0 a 40
        
        // Obtener total real de libros desde la base de datos
        int totalLibros = 0;
        try {
            totalLibros = libroController.obtenerTotalLibros();
        } catch (Exception e) {
            System.out.println("Error al obtener total de libros: " + e.getMessage());
            totalLibros = 0;
        }
        
        // Tarjeta 1: Total de Libros (real desde BD)
        VBox card1 = createSummaryCard("📚", String.format("%,d", totalLibros), "Total de Libros");
        
        // Tarjeta 2: Autores Registrados
        VBox card2 = createSummaryCard("👤", "156", "Autores Registrados");
        
        // Tarjeta 3: Clientes Activos
        VBox card3 = createSummaryCard("👥", "1,234", "Clientes Activos");
        
        // Tarjeta 4: Ventas del Mes
        VBox card4 = createSummaryCard("🛒", "$12,450", "Ventas del Mes");
        
        // Hacer que las tarjetas ocupen todo el ancho disponible
        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);
        
        cardsContainer.getChildren().addAll(card1, card2, card3, card4);
        return cardsContainer;
    }
    
    private VBox createSummaryCard(String icon, String number, String label) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f3f6f4; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        card.setAlignment(Pos.CENTER);
        
        Text iconText = new Text(icon);
        iconText.setFont(Font.font("Arial", 32));
        iconText.setStyle("-fx-fill: #9f84bd;");
        
        Text numberText = new Text(number);
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        numberText.setStyle("-fx-fill: #181818;");
        
        Text labelText = new Text(label);
        labelText.setFont(Font.font("Arial", 12));
        labelText.setStyle("-fx-fill: #9f84bd;");
        labelText.setTextAlignment(TextAlignment.CENTER);
        
        card.getChildren().addAll(iconText, numberText, labelText);
        return card;
    }
    
    private VBox createRecentBooksSection() {
        VBox section = new VBox(15);
        
        // Título de la sección
        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setSpacing(10);
        
        Text title = new Text("Libros Recientes");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setStyle("-fx-fill: #181818;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Text seeAll = new Text("Ver todos");
        seeAll.setFont(Font.font("Arial", 12));
        seeAll.setStyle("-fx-fill: #181818; -fx-underline: true; -fx-cursor: hand;");
        
        // Hacer que "Ver todos" sea clickeable
        seeAll.setOnMouseClicked(e -> showLibros());
        
        titleRow.getChildren().addAll(title, spacer, seeAll);
        
        // Lista de libros (obtenidos de la base de datos)
        VBox booksList = new VBox(10);
        
        try {
            // Obtener los últimos 3 libros de la base de datos
            List<Libro> ultimosLibros = libroController.obtenerUltimosLibros(3);
            
            if (ultimosLibros.isEmpty()) {
                // Si no hay libros, mostrar mensaje
                Text noBooksText = new Text("No hay libros registrados");
                noBooksText.setFont(Font.font("Arial", 14));
                noBooksText.setStyle("-fx-fill: #9f84bd;");
                booksList.getChildren().add(noBooksText);
            } else {
                // Mostrar los libros obtenidos
                for (Libro libro : ultimosLibros) {
                    // Obtener información del autor
                    String nombreAutor = "Autor desconocido";
                    try {
                        Autor autor = autorController.obtenerAutor(libro.getIdAutor());
                        if (autor != null) {
                            nombreAutor = autor.getNombre();
                        }
                    } catch (Exception e) {
                        System.out.println("Error al obtener autor: " + e.getMessage());
                    }
                    
                    // Determinar el tipo de libro
                    String tipoLibro = libro.getTipoLibro().toString();
                    if (tipoLibro.equals("FISICO")) tipoLibro = "Físico";
                    else if (tipoLibro.equals("DIGITAL")) tipoLibro = "Digital";
                    else if (tipoLibro.equals("AUDIOLIBRO")) tipoLibro = "Audiolibro";
                    
                    // Determinar el estado de disponibilidad
                    String estado = "Disponible";
                    if (libro.getTipoLibro() == Libro.TipoLibro.FISICO && libro.getStock() <= 0) {
                        estado = "Agotado";
                    }
                    
                    HBox bookItem = createBookItem(libro.getTitulo(), nombreAutor, tipoLibro, estado);
                    booksList.getChildren().add(bookItem);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener libros recientes: " + e.getMessage());
            // Mostrar mensaje de error
            Text errorText = new Text("Error al cargar libros");
            errorText.setFont(Font.font("Arial", 14));
            errorText.setStyle("-fx-fill: #e74c3c;");
            booksList.getChildren().add(errorText);
        }
        
        section.getChildren().addAll(titleRow, booksList);
        return section;
    }
    
    private HBox createBookItem(String title, String author, String format, String status) {
        HBox item = new HBox(15);
        item.setStyle("-fx-background-color: #f3f6f4; -fx-background-radius: 10; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");
        item.setAlignment(Pos.CENTER_LEFT);
        
        VBox bookInfo = new VBox(5);
        
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleText.setStyle("-fx-fill: #181818;");
        
        Text authorText = new Text(author);
        authorText.setFont(Font.font("Arial", 12));
        authorText.setStyle("-fx-fill: #181818;");
        
        bookInfo.getChildren().addAll(titleText, authorText);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Tags
        HBox tags = new HBox(8);
        
        Label formatTag = new Label(format);
        formatTag.setStyle("-fx-background-color: #ebdccb; -fx-text-fill: #181818; -fx-background-radius: 5; -fx-padding: 2 8; -fx-font-size: 10;");
        
        Label statusTag = new Label(status);
        if (status.equals("Disponible")) {
            statusTag.setStyle("-fx-background-color: #ebc3db; -fx-text-fill: #181818; -fx-background-radius: 5; -fx-padding: 2 8; -fx-font-size: 10;");
        } else {
            statusTag.setStyle("-fx-background-color: #ebc3db; -fx-text-fill: #181818; -fx-background-radius: 5; -fx-padding: 2 8; -fx-font-size: 10;");
        }
        
        tags.getChildren().addAll(formatTag, statusTag);
        
        item.getChildren().addAll(bookInfo, spacer, tags);
        return item;
    }
    
    private VBox createQuickActionsSection() {
        VBox section = new VBox(15);
        
        Text title = new Text("Acciones Rápidas");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setStyle("-fx-fill: #181818;");
        
        // Grid de botones 2x2
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(15);
        buttonGrid.setVgap(15);
        
        Button btn1 = createQuickActionButton("📚", "Registrar Libro");
        Button btn2 = createQuickActionButton("👥", "Nuevo Cliente");
        Button btn3 = createQuickActionButton("🛒", "Nueva Venta");
        Button btn4 = createQuickActionButton("👤", "Crear Autor");
        
        // Eventos
        btn1.setOnAction(e -> showLibros());
        btn2.setOnAction(e -> showClientes());
        btn3.setOnAction(e -> showVentas());
        btn4.setOnAction(e -> showAutores());
        
        buttonGrid.add(btn1, 0, 0);
        buttonGrid.add(btn2, 1, 0);
        buttonGrid.add(btn3, 0, 1);
        buttonGrid.add(btn4, 1, 1);
        
        section.getChildren().addAll(title, buttonGrid);
        return section;
    }
    
    private Button createQuickActionButton(String icon, String label) {
        VBox buttonContent = new VBox(8);
        buttonContent.setAlignment(Pos.CENTER);
        
        Text iconText = new Text(icon);
        iconText.setFont(Font.font("Arial", 24));
        iconText.setStyle("-fx-fill: #9f84bd;");
        
        Text labelText = new Text(label);
        labelText.setFont(Font.font("Arial", 12));
        labelText.setStyle("-fx-fill: #181818;");
        labelText.setTextAlignment(TextAlignment.CENTER);
        
        buttonContent.getChildren().addAll(iconText, labelText);
        
        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setStyle("-fx-background-color: #f3f6f4; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");
        button.setPrefSize(120, 100);
        
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #ebdccb; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #f3f6f4; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");
        });
        
        return button;
    }
    
    private void showLibros() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(libroView.getView());
        
        // Actualizar el estado del botón del menú (librosBtn es el segundo botón, índice 1)
        if (menuButtons != null && menuButtons.size() > 1) {
            setActiveButton(menuButtons, menuButtons.get(1));
        }
    }
    
    private void showAutores() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(autorView.getView());
        
        // Actualizar el estado del botón del menú (autoresBtn es el tercer botón, índice 2)
        if (menuButtons != null && menuButtons.size() > 2) {
            setActiveButton(menuButtons, menuButtons.get(2));
        }
    }
    
    private void showClientes() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(clienteView.getView());
        
        // Actualizar el estado del botón del menú (clientesBtn es el cuarto botón, índice 3)
        if (menuButtons != null && menuButtons.size() > 3) {
            setActiveButton(menuButtons, menuButtons.get(3));
        }
    }
    
    private void showVentas() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(ventaView.getView());
        
        // Actualizar el estado del botón del menú (ventasBtn es el quinto botón, índice 4)
        if (menuButtons != null && menuButtons.size() > 4) {
            setActiveButton(menuButtons, menuButtons.get(4));
        }
    }
    
    private void showReportes() {
        contentArea.getChildren().clear();
        Text reportesText = new Text("Módulo de Reportes - En desarrollo");
        reportesText.setFont(Font.font("Arial", 24));
        reportesText.setStyle("-fx-fill: #91818a;");
        contentArea.getChildren().add(reportesText);
    }
}