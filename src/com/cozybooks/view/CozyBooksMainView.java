package com.cozybooks.view;

import com.cozybooks.controller.AutorController;
import com.cozybooks.controller.ClienteController;
import com.cozybooks.controller.LibroController;
import com.cozybooks.controller.VentaController;
import com.cozybooks.model.Autor;
import com.cozybooks.model.Libro;
import com.cozybooks.util.SvgMapper;
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
import java.math.BigDecimal;
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
        Button settingsBtn = createIconButton(SvgMapper.CONFIG_ICON, "#181818");
        
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
        
        // Obtener total real de autores desde la base de datos
        int totalAutores = 0;
        try {
            totalAutores = autorController.obtenerTotalAutores();
        } catch (Exception e) {
            System.out.println("Error al obtener total de autores: " + e.getMessage());
            totalAutores = 0;
        }
        
        // Tarjeta 2: Autores Registrados (real desde BD)
        VBox card2 = createSummaryCard("👤", String.format("%,d", totalAutores), "Autores Registrados");
        
        // Obtener total real de clientes desde la base de datos
        int totalClientes = 0;
        try {
            totalClientes = clienteController.obtenerTotalClientes();
        } catch (Exception e) {
            System.out.println("Error al obtener total de clientes: " + e.getMessage());
            totalClientes = 0;
        }
        
        // Tarjeta 3: Clientes Activos (real desde BD)
        VBox card3 = createSummaryCard("👥", String.format("%,d", totalClientes), "Clientes Activos");
        
        // Obtener total real de ventas del mes desde la base de datos
        BigDecimal totalVentasMes = BigDecimal.ZERO;
        try {
            totalVentasMes = ventaController.obtenerTotalVentasDelMes();
        } catch (Exception e) {
            System.out.println("Error al obtener total de ventas del mes: " + e.getMessage());
            totalVentasMes = BigDecimal.ZERO;
        }
        
        // Tarjeta 4: Ventas del Mes (real desde BD)
        VBox card4 = createSummaryCard("🛒", String.format("$%,.2f", totalVentasMes), "Ventas del Mes");
        
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
        
        // Eventos - abrir formularios de creación directamente
        btn1.setOnAction(e -> showCreateLibroForm());
        btn2.setOnAction(e -> showCreateClienteForm());
        btn3.setOnAction(e -> showCreateVentaForm());
        btn4.setOnAction(e -> showCreateAutorForm());
        
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
    
    // Métodos para mostrar formularios de creación desde acciones rápidas
    private void showCreateLibroForm() {
        // Crear un diálogo modal para el formulario de creación de libro
        Stage formStage = new Stage();
        formStage.setTitle("Nuevo Libro");
        formStage.setResizable(true);
        formStage.setMinWidth(600);
        formStage.setMinHeight(700);
        
        // Crear el formulario
        VBox formPanel = createLibroFormPanel();
        
        Scene scene = new Scene(formPanel, 600, 700);
        formStage.setScene(scene);
        formStage.show();
    }
    
    private void showCreateClienteForm() {
        // Crear un diálogo modal para el formulario de creación de cliente
        Stage formStage = new Stage();
        formStage.setTitle("Nuevo Cliente");
        formStage.setResizable(true);
        formStage.setMinWidth(500);
        formStage.setMinHeight(500);
        
        // Crear el formulario
        VBox formPanel = createClienteFormPanel();
        
        Scene scene = new Scene(formPanel, 500, 500);
        formStage.setScene(scene);
        formStage.show();
    }
    
    private void showCreateVentaForm() {
        // Crear un diálogo modal para el formulario de creación de venta
        Stage formStage = new Stage();
        formStage.setTitle("Nueva Venta");
        formStage.setResizable(true);
        formStage.setMinWidth(800);
        formStage.setMinHeight(600);
        
        // Crear el formulario
        VBox formPanel = createVentaFormPanel();
        
        Scene scene = new Scene(formPanel, 800, 600);
        formStage.setScene(scene);
        formStage.show();
    }
    
    private void showCreateAutorForm() {
        // Crear un diálogo modal para el formulario de creación de autor
        Stage formStage = new Stage();
        formStage.setTitle("Nuevo Autor");
        formStage.setResizable(true);
        formStage.setMinWidth(500);
        formStage.setMinHeight(500);
        
        // Crear el formulario
        VBox formPanel = createAutorFormPanel();
        
        Scene scene = new Scene(formPanel, 500, 500);
        formStage.setScene(scene);
        formStage.show();
    }
    
    // Métodos helper para crear los formularios
    private VBox createLibroFormPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setStyle("-fx-background-color: #faf8d4; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        
        // Header del formulario
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        
        Text formTitle = new Text("Registrar Nuevo Libro");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setStyle("-fx-fill: #181818;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Botón cerrar
        Button closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 12; -fx-font-size: 14; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        
        headerContainer.getChildren().addAll(formTitle, spacer, closeButton);
        
        // Contenido del formulario
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);");
        
        // Campos básicos del formulario
        Label tituloLabel = createFormLabel("Título *");
        TextField tituloField = createFormTextField("Ingrese el título del libro");
        
        Label autorLabel = createFormLabel("Autor *");
        TextField autorField = createFormTextField("Ingrese el nombre del autor");
        
        Label precioLabel = createFormLabel("Precio *");
        TextField precioField = createFormTextField("Ingrese el precio");
        
        Label tipoLabel = createFormLabel("Tipo de Libro *");
        ComboBox<String> tipoCombo = new ComboBox<>();
        tipoCombo.getItems().addAll("FISICO", "DIGITAL", "AUDIOLIBRO");
        tipoCombo.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 12; -fx-font-size: 14;");
        tipoCombo.setPrefHeight(40);
        
        // Botones
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        Button guardarButton = new Button("💾 GUARDAR");
        guardarButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        
        Button cancelarButton = new Button("❌ CANCELAR");
        cancelarButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        cancelarButton.setOnAction(e -> {
            Stage stage = (Stage) cancelarButton.getScene().getWindow();
            stage.close();
        });
        
        buttonContainer.getChildren().addAll(guardarButton, cancelarButton);
        
        formContainer.getChildren().addAll(
            tituloLabel, tituloField,
            autorLabel, autorField,
            precioLabel, precioField,
            tipoLabel, tipoCombo
        );
        
        panel.getChildren().addAll(headerContainer, formContainer, buttonContainer);
        
        return panel;
    }
    
    private VBox createClienteFormPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setStyle("-fx-background-color: #faf8d4; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        
        // Header del formulario
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        
        Text formTitle = new Text("Registrar Nuevo Cliente");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setStyle("-fx-fill: #181818;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Botón cerrar
        Button closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 12; -fx-font-size: 14; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        
        headerContainer.getChildren().addAll(formTitle, spacer, closeButton);
        
        // Contenido del formulario
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);");
        
        // Campos del formulario
        Label nombreLabel = createFormLabel("Nombre *");
        TextField nombreField = createFormTextField("Ingrese el nombre completo del cliente");
        
        Label documentoLabel = createFormLabel("Documento (DNI) *");
        TextField documentoField = createFormTextField("Ingrese DNI de 8 dígitos");
        
        Label emailLabel = createFormLabel("Email");
        TextField emailField = createFormTextField("ejemplo@correo.com (opcional)");
        
        Label telefonoLabel = createFormLabel("Teléfono");
        TextField telefonoField = createFormTextField("Ingrese número de teléfono (opcional)");
        
        // Botones
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        Button guardarButton = new Button("💾 GUARDAR");
        guardarButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        
        Button cancelarButton = new Button("❌ CANCELAR");
        cancelarButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        cancelarButton.setOnAction(e -> {
            Stage stage = (Stage) cancelarButton.getScene().getWindow();
            stage.close();
        });
        
        buttonContainer.getChildren().addAll(guardarButton, cancelarButton);
        
        formContainer.getChildren().addAll(
            nombreLabel, nombreField,
            documentoLabel, documentoField,
            emailLabel, emailField,
            telefonoLabel, telefonoField
        );
        
        panel.getChildren().addAll(headerContainer, formContainer, buttonContainer);
        
        return panel;
    }
    
    private VBox createVentaFormPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setStyle("-fx-background-color: #faf8d4; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        
        // Header del formulario
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        
        Text formTitle = new Text("Nueva Venta");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setStyle("-fx-fill: #181818;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Botón cerrar
        Button closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 12; -fx-font-size: 14; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        
        headerContainer.getChildren().addAll(formTitle, spacer, closeButton);
        
        // Contenido del formulario
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);");
        
        // Información del cliente
        HBox clienteBox = new HBox(10);
        clienteBox.setAlignment(Pos.CENTER_LEFT);
        
        Label clienteLabel = createFormLabel("ID Cliente:");
        TextField clienteField = createFormTextField("Ingrese ID del cliente");
        clienteField.setPrefWidth(150);
        
        Button buscarClienteButton = new Button("🔍 Buscar");
        buscarClienteButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        
        clienteBox.getChildren().addAll(clienteLabel, clienteField, buscarClienteButton);
        
        // Agregar libros a la venta
        HBox libroBox = new HBox(10);
        libroBox.setAlignment(Pos.CENTER_LEFT);
        
        Label libroLabel = createFormLabel("ID Libro:");
        TextField libroField = createFormTextField("Ingrese ID del libro");
        libroField.setPrefWidth(150);
        
        Label cantidadLabel = createFormLabel("Cantidad:");
        TextField cantidadField = createFormTextField("Cantidad");
        cantidadField.setPrefWidth(100);
        
        Button agregarButton = new Button("➕ Agregar");
        agregarButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        
        libroBox.getChildren().addAll(libroLabel, libroField, cantidadLabel, cantidadField, agregarButton);
        
        // Tabla de detalles de la venta
        Text detalleTitle = new Text("Detalles de la Venta");
        detalleTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        detalleTitle.setStyle("-fx-fill: #181818;");
        
        TableView<String> detalleTable = new TableView<>();
        detalleTable.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8;");
        detalleTable.setPrefHeight(150);
        
        // Botones finales
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        Button finalizarButton = new Button("✅ FINALIZAR VENTA");
        finalizarButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        
        Button cancelarButton = new Button("❌ CANCELAR VENTA");
        cancelarButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        cancelarButton.setOnAction(e -> {
            Stage stage = (Stage) cancelarButton.getScene().getWindow();
            stage.close();
        });
        
        buttonContainer.getChildren().addAll(finalizarButton, cancelarButton);
        
        formContainer.getChildren().addAll(
            clienteBox,
            libroBox,
            detalleTitle,
            detalleTable,
            buttonContainer
        );
        
        panel.getChildren().addAll(headerContainer, formContainer);
        
        return panel;
    }
    
    private VBox createAutorFormPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setStyle("-fx-background-color: #faf8d4; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        
        // Header del formulario
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        
        Text formTitle = new Text("Registrar Nuevo Autor");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setStyle("-fx-fill: #181818;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Botón cerrar
        Button closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 12; -fx-font-size: 14; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        
        headerContainer.getChildren().addAll(formTitle, spacer, closeButton);
        
        // Contenido del formulario
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);");
        
        // Campos del formulario
        Label nombreLabel = createFormLabel("Nombre *");
        TextField nombreField = createFormTextField("Ingrese el nombre completo del autor");
        
        Label fechaLabel = createFormLabel("Fecha de Nacimiento");
        DatePicker fechaField = new DatePicker();
        fechaField.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 12; -fx-font-size: 14;");
        fechaField.setPrefHeight(40);
        
        Label nacionalidadLabel = createFormLabel("Nacionalidad");
        TextField nacionalidadField = createFormTextField("Ingrese la nacionalidad");
        
        Label biografiaLabel = createFormLabel("Biografía");
        TextArea biografiaField = new TextArea();
        biografiaField.setPromptText("Ingrese una breve biografía del autor");
        biografiaField.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 12; -fx-font-size: 14;");
        biografiaField.setPrefHeight(80);
        
        // Botones
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        Button guardarButton = new Button("💾 GUARDAR");
        guardarButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        
        Button cancelarButton = new Button("❌ CANCELAR");
        cancelarButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        cancelarButton.setOnAction(e -> {
            Stage stage = (Stage) cancelarButton.getScene().getWindow();
            stage.close();
        });
        
        buttonContainer.getChildren().addAll(guardarButton, cancelarButton);
        
        formContainer.getChildren().addAll(
            nombreLabel, nombreField,
            fechaLabel, fechaField,
            nacionalidadLabel, nacionalidadField,
            biografiaLabel, biografiaField
        );
        
        panel.getChildren().addAll(headerContainer, formContainer, buttonContainer);
        
        return panel;
    }
    
    // Métodos helper para crear elementos del formulario
    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setStyle("-fx-text-fill: #181818; -fx-padding: 0 0 5 0;");
        return label;
    }
    
    private TextField createFormTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 12; -fx-font-size: 14;");
        field.setPrefHeight(40);
        return field;
    }
}