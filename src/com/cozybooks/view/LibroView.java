package com.cozybooks.view;

import com.cozybooks.controller.LibroController;
import com.cozybooks.model.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

/**
 * Vista para la gestión de Libros
 */
public class LibroView {
    
    private LibroController libroController;
    private VBox mainContainer;
    private TableView<Libro> libroTable;
    private ObservableList<Libro> libroData;
    
    // Formulario de registro/actualización
    private TextField tituloField;
    private TextField isbnField;
    private TextField editorialField;
    private TextField añoField;
    private TextField precioField;
    private TextField generoField;
    private ComboBox<Libro.TipoLibro> tipoCombo;
    private TextField stockField;
    private TextField idAutorField;
    
    // Campos específicos por tipo
    private TextField encuadernadoField;
    private TextField numEdicionField;
    private TextField extensionField;
    private CheckBox permiteImpresionCheck;
    private TextField duracionField;
    private TextField plataformaField;
    private TextField narradorField;
    
    private Button saveButton;
    private Button cancelButton;
    private boolean isEditing = false;
    private Libro libroEditando;
    
    public LibroView(LibroController libroController) {
        this.libroController = libroController;
        this.libroData = FXCollections.observableArrayList();
        setupUI();
        loadLibros();
    }
    
    private void setupUI() {
        mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #faf8d4;");
        
        // Header con título, buscador y botón crear
        HBox header = createHeader();
        
        // Tabla principal que ocupa todo el ancho
        VBox tableContainer = createTableContainer();
        
        mainContainer.getChildren().addAll(header, tableContainer);
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #ede3e9; -fx-background-radius: 0 0 15 15;");
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Título
        Text title = new Text("Gestión de Libros");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: #181818;");
        
        // Spacer para empujar elementos a la derecha
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Buscador
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar por título, ISBN o género...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12;");
        
        // Botón de búsqueda
        Button searchButton = new Button("🔍");
        searchButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8 12;");
        searchButton.setOnAction(e -> searchLibros(searchField.getText()));
        
        // Botón crear libro
        Button createButton = new Button("➕ NUEVO LIBRO");
        createButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        createButton.setOnAction(e -> showCreateForm());
        
        HBox searchContainer = new HBox(8);
        searchContainer.getChildren().addAll(searchField, searchButton);
        
        header.getChildren().addAll(title, spacer, searchContainer, createButton);
        
        return header;
    }
    
    private VBox createTableContainer() {
        VBox container = new VBox(0);
        container.setPadding(new Insets(20));
        
        // Tabla de libros
        libroTable = new TableView<>();
        libroTable.setItems(libroData);
        libroTable.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        libroTable.setRowFactory(tv -> {
            TableRow<Libro> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Libro libro = row.getItem();
                    editLibro(libro);
                }
            });
            return row;
        });
        
        // Configurar columnas
        setupTableColumns();
        
        // Hacer que la tabla ocupe todo el ancho disponible
        VBox.setVgrow(libroTable, Priority.ALWAYS);
        
        container.getChildren().add(libroTable);
        
        return container;
    }
    
    private void setupTableColumns() {
        // Limpiar columnas existentes
        libroTable.getColumns().clear();
        
        // ID
        TableColumn<Libro, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        idColumn.setStyle("-fx-alignment: CENTER;");
        
        // Título
        TableColumn<Libro, String> tituloColumn = new TableColumn<>("TÍTULO");
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        
        // Editorial
        TableColumn<Libro, String> editorialColumn = new TableColumn<>("EDITORIAL");
        editorialColumn.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        
        // Año
        TableColumn<Libro, Integer> añoColumn = new TableColumn<>("AÑO");
        añoColumn.setCellValueFactory(new PropertyValueFactory<>("año"));
        añoColumn.setStyle("-fx-alignment: CENTER;");
        
        // Precio
        TableColumn<Libro, BigDecimal> precioColumn = new TableColumn<>("PRECIO");
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        precioColumn.setStyle("-fx-alignment: CENTER_RIGHT;");
        precioColumn.setCellFactory(column -> new TableCell<Libro, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%,.2f", item));
                }
            }
        });
        
        // Tipo
        TableColumn<Libro, Libro.TipoLibro> tipoColumn = new TableColumn<>("TIPO");
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoLibro"));
        tipoColumn.setStyle("-fx-alignment: CENTER;");
        tipoColumn.setCellFactory(column -> new TableCell<Libro, Libro.TipoLibro>() {
            @Override
            protected void updateItem(Libro.TipoLibro item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    String tipo = item.toString();
                    if (tipo.equals("FISICO")) tipo = "Físico";
                    else if (tipo.equals("DIGITAL")) tipo = "Digital";
                    else if (tipo.equals("AUDIOLIBRO")) tipo = "Audiolibro";
                    
                    setText(tipo);
                    // Usar colores del dashboard
                    if (tipo.equals("Físico")) {
                        setStyle("-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32; -fx-background-radius: 4; -fx-padding: 4 8; -fx-alignment: CENTER;");
                    } else if (tipo.equals("Digital")) {
                        setStyle("-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2; -fx-background-radius: 4; -fx-padding: 4 8; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-background-color: #fce4ec; -fx-text-fill: #c2185b; -fx-background-radius: 4; -fx-padding: 4 8; -fx-alignment: CENTER;");
                    }
                }
            }
        });
        
        // Stock
        TableColumn<Libro, Integer> stockColumn = new TableColumn<>("STOCK");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockColumn.setStyle("-fx-alignment: CENTER;");
        stockColumn.setCellFactory(column -> new TableCell<Libro, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    if (item <= 0) {
                        setStyle("-fx-background-color: #ebc3db; -fx-background-radius: 5; -fx-padding: 2 8; -fx-font-size: 10;");
                    } else {
                        setStyle("-fx-background-color: #ebc3db; -fx-background-radius: 5; -fx-padding: 2 8; -fx-font-size: 10;");
                    }
                }
            }
        });
        
        // Acciones
        TableColumn<Libro, Void> actionsColumn = new TableColumn<>("ACCIONES");
        actionsColumn.setStyle("-fx-alignment: CENTER;");
        actionsColumn.setCellFactory(param -> new TableCell<Libro, Void>() {
            private final Button editButton = new Button("✏️");
            private final Button deleteButton = new Button("🗑️");
            
            {
                editButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 12; -fx-font-size: 12;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 12; -fx-font-size: 12;");
                
                editButton.setOnAction(e -> {
                    Libro libro = getTableView().getItems().get(getIndex());
                    editLibro(libro);
                });
                
                deleteButton.setOnAction(e -> {
                    Libro libro = getTableView().getItems().get(getIndex());
                    deleteLibro(libro);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(8, editButton, deleteButton);
                    buttons.setAlignment(Pos.CENTER);
                    setGraphic(buttons);
                }
            }
        });
        
        libroTable.getColumns().addAll(idColumn, tituloColumn, editorialColumn, añoColumn, precioColumn, tipoColumn, stockColumn, actionsColumn);
        
        // Configurar ancho de columnas para que ocupen el 100%
        idColumn.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.08));
        tituloColumn.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.25));
        editorialColumn.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.15));
        añoColumn.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.08));
        precioColumn.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.12));
        tipoColumn.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.12));
        stockColumn.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.10));
        actionsColumn.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.10));
    }
    
    private VBox createTablePanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Botones de acción
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        Button refreshButton = new Button("🔄 Actualizar");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        refreshButton.setOnAction(e -> loadLibros());
        
        Button searchButton = new Button("🔍 Buscar");
        searchButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        searchButton.setOnAction(e -> showSearchDialog());
        
        buttonBox.getChildren().addAll(refreshButton, searchButton);
        
        // Tabla de libros
        libroTable = new TableView<>();
        libroTable.setItems(libroData);
        libroTable.setRowFactory(tv -> {
            TableRow<Libro> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Libro libro = row.getItem();
                    editLibro(libro);
                }
            });
            return row;
        });
        
        // Columnas de la tabla
        TableColumn<Libro, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        idColumn.setPrefWidth(60);
        
        TableColumn<Libro, String> tituloColumn = new TableColumn<>("Título");
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tituloColumn.setPrefWidth(200);
        
        TableColumn<Libro, String> editorialColumn = new TableColumn<>("Editorial");
        editorialColumn.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        editorialColumn.setPrefWidth(120);
        
        TableColumn<Libro, Integer> añoColumn = new TableColumn<>("Año");
        añoColumn.setCellValueFactory(new PropertyValueFactory<>("año"));
        añoColumn.setPrefWidth(80);
        
        TableColumn<Libro, BigDecimal> precioColumn = new TableColumn<>("Precio");
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        precioColumn.setPrefWidth(80);
        
        TableColumn<Libro, Libro.TipoLibro> tipoColumn = new TableColumn<>("Tipo");
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoLibro"));
        tipoColumn.setPrefWidth(100);
        
        TableColumn<Libro, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockColumn.setPrefWidth(80);
        
        libroTable.getColumns().addAll(idColumn, tituloColumn, editorialColumn, añoColumn, precioColumn, tipoColumn, stockColumn);
        
        // Botones de acción de fila
        TableColumn<Libro, Void> actionsColumn = new TableColumn<>("Acciones");
        actionsColumn.setPrefWidth(120);
        actionsColumn.setCellFactory(param -> new TableCell<Libro, Void>() {
            private final Button editButton = new Button("✏️");
            private final Button deleteButton = new Button("🗑️");
            
            {
                editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                
                editButton.setOnAction(e -> {
                    Libro libro = getTableView().getItems().get(getIndex());
                    editLibro(libro);
                });
                
                deleteButton.setOnAction(e -> {
                    Libro libro = getTableView().getItems().get(getIndex());
                    deleteLibro(libro);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
        
        libroTable.getColumns().add(actionsColumn);
        
        panel.getChildren().addAll(buttonBox, libroTable);
        
        return panel;
    }
    
    private VBox createFormPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text formTitle = new Text("Registrar/Editar Libro");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setStyle("-fx-fill: #2c3e50;");
        
        // Scroll pane para el formulario
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        
        // Campos del formulario
        VBox formContainer = new VBox(10);
        
        // Título
        Label tituloLabel = new Label("Título *");
        tituloField = new TextField();
        tituloField.setPromptText("Ingrese el título del libro");
        
        // ISBN
        Label isbnLabel = new Label("ISBN");
        isbnField = new TextField();
        isbnField.setPromptText("Ingrese ISBN (opcional)");
        
        // Editorial
        Label editorialLabel = new Label("Editorial *");
        editorialField = new TextField();
        editorialField.setPromptText("Ingrese la editorial");
        
        // Año
        Label añoLabel = new Label("Año de Publicación *");
        añoField = new TextField();
        añoField.setPromptText("Ingrese el año");
        
        // Precio
        Label precioLabel = new Label("Precio *");
        precioField = new TextField();
        precioField.setPromptText("Ingrese el precio");
        
        // Género
        Label generoLabel = new Label("Género");
        generoField = new TextField();
        generoField.setPromptText("Ingrese el género (opcional)");
        
        // Tipo de libro
        Label tipoLabel = new Label("Tipo de Libro *");
        tipoCombo = new ComboBox<>();
        tipoCombo.getItems().addAll(Libro.TipoLibro.values());
        tipoCombo.setPromptText("Seleccione el tipo");
        tipoCombo.setOnAction(e -> toggleSpecificFields());
        
        // Stock (solo para libros físicos)
        Label stockLabel = new Label("Stock");
        stockField = new TextField();
        stockField.setPromptText("Ingrese stock inicial");
        
        // ID Autor
        Label idAutorLabel = new Label("ID del Autor *");
        idAutorField = new TextField();
        idAutorField.setPromptText("Ingrese el ID del autor");
        
        // Campos específicos para libros físicos
        Label encuadernadoLabel = new Label("Tipo de Encuadernado");
        encuadernadoField = new TextField();
        encuadernadoField.setPromptText("Tapa dura, tapa blanda, etc.");
        
        Label numEdicionLabel = new Label("Número de Edición");
        numEdicionField = new TextField();
        numEdicionField.setPromptText("Ingrese número de edición");
        
        // Campos específicos para libros digitales
        Label extensionLabel = new Label("Extensión del Archivo");
        extensionField = new TextField();
        extensionField.setPromptText("PDF, EPUB, etc.");
        
        permiteImpresionCheck = new CheckBox("Permite Impresión");
        
        // Campos específicos para audiolibros
        Label duracionLabel = new Label("Duración (minutos)");
        duracionField = new TextField();
        duracionField.setPromptText("Ingrese duración en minutos");
        
        Label plataformaLabel = new Label("Plataforma");
        plataformaField = new TextField();
        plataformaField.setPromptText("Audible, Spotify, etc.");
        
        Label narradorLabel = new Label("Narrador");
        narradorField = new TextField();
        narradorField.setPromptText("Ingrese nombre del narrador");
        
        formContainer.getChildren().addAll(
            tituloLabel, tituloField,
            isbnLabel, isbnField,
            editorialLabel, editorialField,
            añoLabel, añoField,
            precioLabel, precioField,
            generoLabel, generoField,
            tipoLabel, tipoCombo,
            stockLabel, stockField,
            idAutorLabel, idAutorField,
            encuadernadoLabel, encuadernadoField,
            numEdicionLabel, numEdicionField,
            extensionLabel, extensionField,
            permiteImpresionCheck,
            duracionLabel, duracionField,
            plataformaLabel, plataformaField,
            narradorLabel, narradorField
        );
        
        scrollPane.setContent(formContainer);
        
        // Botones
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        saveButton = new Button("💾 Guardar");
        saveButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> saveLibro());
        
        cancelButton = new Button("❌ Cancelar");
        cancelButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        cancelButton.setOnAction(e -> clearForm());
        
        buttonContainer.getChildren().addAll(saveButton, cancelButton);
        
        panel.getChildren().addAll(formTitle, scrollPane, buttonContainer);
        
        return panel;
    }
    
    private void toggleSpecificFields() {
        Libro.TipoLibro tipo = tipoCombo.getValue();
        
        // Ocultar todos los campos específicos primero
        encuadernadoField.setVisible(false);
        numEdicionField.setVisible(false);
        extensionField.setVisible(false);
        permiteImpresionCheck.setVisible(false);
        duracionField.setVisible(false);
        plataformaField.setVisible(false);
        narradorField.setVisible(false);
        
        if (tipo == Libro.TipoLibro.FISICO) {
            encuadernadoField.setVisible(true);
            numEdicionField.setVisible(true);
            stockField.setVisible(true);
        } else if (tipo == Libro.TipoLibro.DIGITAL) {
            extensionField.setVisible(true);
            permiteImpresionCheck.setVisible(true);
            stockField.setVisible(false);
        } else if (tipo == Libro.TipoLibro.AUDIOLIBRO) {
            duracionField.setVisible(true);
            plataformaField.setVisible(true);
            narradorField.setVisible(true);
            stockField.setVisible(false);
        }
    }
    
    private void loadLibros() {
        try {
            libroData.clear();
            List<Libro> libros = libroController.obtenerListaLibros();
            libroData.addAll(libros);
        } catch (Exception e) {
            showAlert("Error", "Error al cargar libros: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void saveLibro() {
        try {
            // Validaciones básicas
            if (tituloField.getText().trim().isEmpty()) {
                showAlert("Error", "El título es obligatorio", Alert.AlertType.ERROR);
                return;
            }
            
            if (editorialField.getText().trim().isEmpty()) {
                showAlert("Error", "La editorial es obligatoria", Alert.AlertType.ERROR);
                return;
            }
            
            if (tipoCombo.getValue() == null) {
                showAlert("Error", "Debe seleccionar un tipo de libro", Alert.AlertType.ERROR);
                return;
            }
            
            // Validar año
            int año;
            try {
                año = Integer.parseInt(añoField.getText().trim());
                if (año < 1000 || año > 2024) {
                    showAlert("Error", "Año inválido", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Año debe ser un número válido", Alert.AlertType.ERROR);
                return;
            }
            
            // Validar precio
            BigDecimal precio;
            try {
                precio = new BigDecimal(precioField.getText().trim());
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    showAlert("Error", "El precio debe ser mayor a 0", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Precio debe ser un número válido", Alert.AlertType.ERROR);
                return;
            }
            
            // Validar ID autor
            int idAutor;
            try {
                idAutor = Integer.parseInt(idAutorField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Error", "ID del autor debe ser un número válido", Alert.AlertType.ERROR);
                return;
            }
            
            // Validaciones específicas por tipo
            Libro.TipoLibro tipo = tipoCombo.getValue();
            if (tipo == Libro.TipoLibro.FISICO) {
                if (encuadernadoField.getText().trim().isEmpty()) {
                    showAlert("Error", "El tipo de encuadernado es obligatorio para libros físicos", Alert.AlertType.ERROR);
                    return;
                }
                
                int numEdicion;
                try {
                    numEdicion = Integer.parseInt(numEdicionField.getText().trim());
                    if (numEdicion <= 0) {
                        showAlert("Error", "El número de edición debe ser mayor a 0", Alert.AlertType.ERROR);
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Error", "Número de edición debe ser un número válido", Alert.AlertType.ERROR);
                    return;
                }
            } else if (tipo == Libro.TipoLibro.DIGITAL) {
                if (extensionField.getText().trim().isEmpty()) {
                    showAlert("Error", "La extensión es obligatoria para libros digitales", Alert.AlertType.ERROR);
                    return;
                }
            } else if (tipo == Libro.TipoLibro.AUDIOLIBRO) {
                if (duracionField.getText().trim().isEmpty()) {
                    showAlert("Error", "La duración es obligatoria para audiolibros", Alert.AlertType.ERROR);
                    return;
                }
                
                if (plataformaField.getText().trim().isEmpty()) {
                    showAlert("Error", "La plataforma es obligatoria para audiolibros", Alert.AlertType.ERROR);
                    return;
                }
                
                if (narradorField.getText().trim().isEmpty()) {
                    showAlert("Error", "El narrador es obligatorio para audiolibros", Alert.AlertType.ERROR);
                    return;
                }
            }
            
            // Crear o actualizar libro
            if (isEditing && libroEditando != null) {
                // Actualizar libro existente
                libroEditando.setTitulo(tituloField.getText().trim());
                libroEditando.setEditorial(editorialField.getText().trim());
                libroEditando.setAño(año);
                libroEditando.setPrecio(precio);
                libroEditando.setTipoLibro(tipo);
                
                if (tipo == Libro.TipoLibro.FISICO) {
                    int stock = Integer.parseInt(stockField.getText().trim());
                    libroEditando.setStock(stock);
                }
                
                // Llamar al controlador para actualizar
                libroController.actualizarLibro(libroEditando);
                
                showAlert("Éxito", "Libro actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                // Crear nuevo libro
                Libro nuevoLibro = new Libro(
                    tituloField.getText().trim(),
                    editorialField.getText().trim(),
                    año,
                    precio,
                    tipo,
                    idAutor
                );
                
                nuevoLibro.setIsbn(isbnField.getText().trim().isEmpty() ? null : isbnField.getText().trim());
                nuevoLibro.setGenero(generoField.getText().trim().isEmpty() ? null : generoField.getText().trim());
                
                if (tipo == Libro.TipoLibro.FISICO) {
                    int stock = Integer.parseInt(stockField.getText().trim());
                    nuevoLibro.setStock(stock);
                    nuevoLibro.setEncuadernado(encuadernadoField.getText().trim());
                    nuevoLibro.setNumEdicion(Integer.parseInt(numEdicionField.getText().trim()));
                } else if (tipo == Libro.TipoLibro.DIGITAL) {
                    nuevoLibro.setExtension(extensionField.getText().trim());
                    nuevoLibro.setPermisosImpresion(permiteImpresionCheck.isSelected());
                } else if (tipo == Libro.TipoLibro.AUDIOLIBRO) {
                    nuevoLibro.setDuracion(Integer.parseInt(duracionField.getText().trim()));
                    nuevoLibro.setPlataforma(plataformaField.getText().trim());
                    nuevoLibro.setNarrador(narradorField.getText().trim());
                }
                
                // Llamar al controlador para registrar
                Libro libroRegistrado = libroController.registrarLibro(nuevoLibro);
                
                showAlert("Éxito", "Libro registrado correctamente con ID: " + libroRegistrado.getIdLibro(), Alert.AlertType.INFORMATION);
            }
            
            clearForm();
            loadLibros();
            
        } catch (Exception e) {
            showAlert("Error", "Error al guardar libro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void editLibro(Libro libro) {
        isEditing = true;
        libroEditando = libro;
        
        tituloField.setText(libro.getTitulo());
        isbnField.setText(libro.getIsbn() != null ? libro.getIsbn() : "");
        editorialField.setText(libro.getEditorial());
        añoField.setText(String.valueOf(libro.getAño()));
        precioField.setText(libro.getPrecio().toString());
        generoField.setText(libro.getGenero() != null ? libro.getGenero() : "");
        tipoCombo.setValue(libro.getTipoLibro());
        stockField.setText(String.valueOf(libro.getStock()));
        idAutorField.setText(String.valueOf(libro.getIdAutor()));
        
        // Llenar campos específicos según el tipo
        if (libro.getTipoLibro() == Libro.TipoLibro.FISICO) {
            encuadernadoField.setText(libro.getEncuadernado() != null ? libro.getEncuadernado() : "");
            numEdicionField.setText(String.valueOf(libro.getNumEdicion()));
        } else if (libro.getTipoLibro() == Libro.TipoLibro.DIGITAL) {
            extensionField.setText(libro.getExtension() != null ? libro.getExtension() : "");
            permiteImpresionCheck.setSelected(libro.getPermisosImpresion());
        } else if (libro.getTipoLibro() == Libro.TipoLibro.AUDIOLIBRO) {
            duracionField.setText(String.valueOf(libro.getDuracion()));
            plataformaField.setText(libro.getPlataforma() != null ? libro.getPlataforma() : "");
            narradorField.setText(libro.getNarrador() != null ? libro.getNarrador() : "");
        }
        
        toggleSpecificFields();
        saveButton.setText("💾 Actualizar");
    }
    
    private void deleteLibro(Libro libro) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de que desea eliminar este libro?");
        alert.setContentText("Libro: " + libro.getTitulo() + "\nEsta acción no se puede deshacer.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Llamar al controlador para eliminar
                    libroController.eliminarLibro(libro.getIdLibro());
                    
                    showAlert("Éxito", "Libro eliminado correctamente", Alert.AlertType.INFORMATION);
                    loadLibros();
                } catch (Exception e) {
                    showAlert("Error", "Error al eliminar libro: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }
    
    private void clearForm() {
        isEditing = false;
        libroEditando = null;
        
        tituloField.clear();
        isbnField.clear();
        editorialField.clear();
        añoField.clear();
        precioField.clear();
        generoField.clear();
        tipoCombo.setValue(null);
        stockField.clear();
        idAutorField.clear();
        encuadernadoField.clear();
        numEdicionField.clear();
        extensionField.clear();
        permiteImpresionCheck.setSelected(false);
        duracionField.clear();
        plataformaField.clear();
        narradorField.clear();
        
        // Ocultar campos específicos
        toggleSpecificFields();
        
        saveButton.setText("💾 Guardar");
    }
    
    private void showSearchDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Libro");
        dialog.setHeaderText("Ingrese criterio de búsqueda");
        dialog.setContentText("Título, ISBN o género:");
        
        dialog.showAndWait().ifPresent(criterio -> {
            try {
                // Llamar al controlador para buscar
                List<Libro> libros = libroController.buscarLibros(criterio);
                
                if (libros.isEmpty()) {
                    showAlert("Información", "No se encontraron libros con el criterio: " + criterio, Alert.AlertType.INFORMATION);
                } else {
                    showLibrosEncontrados(libros, criterio);
                }
            } catch (Exception e) {
                showAlert("Error", "Error al buscar libros: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }
    
    private void showLibrosEncontrados(List<Libro> libros, String criterio) {
        // Crear ventana de resultados de búsqueda
        Stage resultadosStage = new Stage();
        resultadosStage.setTitle("Resultados de Búsqueda");
        resultadosStage.setWidth(900);
        resultadosStage.setHeight(600);
        
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Título y resumen
        VBox headerContainer = new VBox(10);
        headerContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text titleText = new Text("Resultados de Búsqueda");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setStyle("-fx-fill: #2c3e50;");
        
        Text criterioText = new Text("Criterio: " + criterio);
        criterioText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        criterioText.setStyle("-fx-fill: #7f8c8d;");
        
        Text cantidadText = new Text("Libros encontrados: " + libros.size());
        cantidadText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cantidadText.setStyle("-fx-fill: #27ae60;");
        
        headerContainer.getChildren().addAll(titleText, criterioText, cantidadText);
        
        // Tabla de libros encontrados
        VBox tableContainer = new VBox(10);
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        TableView<Libro> resultadosTable = new TableView<>();
        ObservableList<Libro> resultadosData = FXCollections.observableArrayList(libros);
        resultadosTable.setItems(resultadosData);
        
        // Columnas de la tabla
        TableColumn<Libro, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        idColumn.setPrefWidth(60);
        
        TableColumn<Libro, String> tituloColumn = new TableColumn<>("Título");
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tituloColumn.setPrefWidth(200);
        
        TableColumn<Libro, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setPrefWidth(120);
        
        TableColumn<Libro, Integer> autorColumn = new TableColumn<>("ID Autor");
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
        autorColumn.setPrefWidth(100);
        
        TableColumn<Libro, String> editorialColumn = new TableColumn<>("Editorial");
        editorialColumn.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        editorialColumn.setPrefWidth(120);
        
        TableColumn<Libro, Integer> añoColumn = new TableColumn<>("Año");
        añoColumn.setCellValueFactory(new PropertyValueFactory<>("año"));
        añoColumn.setPrefWidth(80);
        
        TableColumn<Libro, BigDecimal> precioColumn = new TableColumn<>("Precio");
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        precioColumn.setPrefWidth(80);
        
        TableColumn<Libro, Libro.TipoLibro> tipoColumn = new TableColumn<>("Tipo");
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoLibro"));
        tipoColumn.setPrefWidth(100);
        
        TableColumn<Libro, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockColumn.setPrefWidth(80);
        
        resultadosTable.getColumns().addAll(idColumn, tituloColumn, isbnColumn, autorColumn, editorialColumn, añoColumn, precioColumn, tipoColumn, stockColumn);
        
        // Resumen por tipo
        VBox resumenContainer = new VBox(10);
        resumenContainer.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10; -fx-padding: 15;");
        
        Text resumenTitle = new Text("Resumen por Tipo");
        resumenTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resumenTitle.setStyle("-fx-fill: white;");
        
        long librosFisicos = libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.FISICO).count();
        long librosDigitales = libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.DIGITAL).count();
        long audiolibros = libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.AUDIOLIBRO).count();
        
        Text fisicos = new Text("Libros físicos: " + librosFisicos);
        fisicos.setStyle("-fx-fill: white;");
        
        Text digitales = new Text("Libros digitales: " + librosDigitales);
        digitales.setStyle("-fx-fill: white;");
        
        Text audio = new Text("Audiolibros: " + audiolibros);
        audio.setStyle("-fx-fill: white;");
        
        resumenContainer.getChildren().addAll(resumenTitle, fisicos, digitales, audio);
        
        // Botón cerrar
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        
        Button closeButton = new Button("❌ Cerrar");
        closeButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        closeButton.setOnAction(e -> resultadosStage.close());
        
        buttonContainer.getChildren().add(closeButton);
        
        tableContainer.getChildren().addAll(resultadosTable, resumenContainer, buttonContainer);
        mainContainer.getChildren().addAll(headerContainer, tableContainer);
        
        Scene scene = new Scene(mainContainer);
        resultadosStage.setScene(scene);
        resultadosStage.show();
    }
    
    private void searchLibros(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                loadLibros(); // Si no hay criterio, cargar todos
                return;
            }
            
            List<Libro> resultados = libroController.buscarLibros(criterio.trim());
            libroData.clear();
            libroData.addAll(resultados);
            
            if (resultados.isEmpty()) {
                showAlert("Búsqueda", "No se encontraron libros con el criterio: " + criterio, Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Error al buscar libros: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void showCreateForm() {
        // Crear un diálogo modal para el formulario de creación
        Stage formStage = new Stage();
        formStage.setTitle("Nuevo Libro");
        formStage.setResizable(false);
        
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: #faf8d4;");
        
        // Título
        Text title = new Text("Crear Nuevo Libro");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-fill: #181818;");
        
        // Formulario (reutilizar el panel de formulario existente)
        VBox formPanel = createFormPanel();
        
        formContainer.getChildren().addAll(title, formPanel);
        
        Scene scene = new Scene(formContainer, 500, 600);
        formStage.setScene(scene);
        formStage.show();
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public VBox getView() {
        return mainContainer;
    }
}
