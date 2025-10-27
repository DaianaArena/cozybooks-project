package com.cozybooks.view;

import com.cozybooks.controller.AutorController;
import com.cozybooks.model.Autor;
import com.cozybooks.model.Libro;
import com.cozybooks.util.SvgMapper;

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
import java.time.LocalDate;
import java.util.List;

/**
 * Vista para la gestión de Autores
 */
public class AutorView {
    
    private AutorController autorController;
    private VBox mainContainer;
    private TableView<Autor> autorTable;
    private ObservableList<Autor> autorData;
    
    // Formulario de registro/actualización
    private TextField nombreField;
    private DatePicker fechaNacimientoPicker;
    private TextField nacionalidadField;
    private TextArea biografiaArea;
    private Button saveButton;
    private Button cancelButton;
    private boolean isEditing = false;
    private Autor autorEditando;
    
    public AutorView(AutorController autorController) {
        this.autorController = autorController;
        this.autorData = FXCollections.observableArrayList();
        setupUI();
        loadAutores();
    }
    
    private void setupUI() {
        mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #faf8d4;");
        
        // Header con título, buscador y botón crear
        HBox header = createHeader();
        
        // Contenedor de la tabla
        VBox tableContainer = createTableContainer();
        
        mainContainer.getChildren().addAll(header, tableContainer);
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #ede3e9; -fx-background-radius: 0 0 15 15;");
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Título
        Text title = new Text("Gestión de Autores");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: #181818;");
        
        // Espaciador
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Buscador
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar por nombre o nacionalidad...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12;");
        
        // Botón de búsqueda
        Button searchButton = new Button(SvgMapper.SEARCH_ICON);
        searchButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8 12;");
        searchButton.setOnAction(e -> searchAutores(searchField.getText()));
        
        // Botón reporte
        Button reportButton = new Button("📊");
        reportButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8 12;");
        reportButton.setOnAction(e -> showReporteDialog());
        
        // Botón crear autor
        Button createButton = new Button("➕ NUEVO AUTOR");
        createButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20; -fx-font-size: 14;");
        createButton.setOnAction(e -> showCreateForm());
        
        HBox searchContainer = new HBox(8);
        searchContainer.getChildren().addAll(searchField, searchButton);
        
        header.getChildren().addAll(title, spacer, searchContainer, reportButton, createButton);
        
        return header;
    }
    
    private VBox createTableContainer() {
        VBox container = new VBox(0);
        container.setPadding(new Insets(20));
        
        // Tabla de autores
        autorTable = new TableView<>();
        autorTable.setItems(autorData);
        autorTable.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        autorTable.setRowFactory(tv -> {
            TableRow<Autor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Autor autor = row.getItem();
                    editAutor(autor);
                }
            });
            return row;
        });
        
        // Configurar columnas
        setupTableColumns();
        
        // Hacer que la tabla ocupe todo el ancho disponible
        VBox.setVgrow(autorTable, Priority.ALWAYS);
        
        container.getChildren().add(autorTable);
        
        return container;
    }
    
    private void setupTableColumns() {
        // Limpiar columnas existentes
        autorTable.getColumns().clear();
        
        // ID
        TableColumn<Autor, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
        idColumn.setStyle("-fx-alignment: CENTER;");
        
        // Nombre
        TableColumn<Autor, String> nombreColumn = new TableColumn<>("NOMBRE");
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        // Fecha de Nacimiento
        TableColumn<Autor, LocalDate> fechaColumn = new TableColumn<>("FECHA NACIMIENTO");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        fechaColumn.setStyle("-fx-alignment: CENTER;");
        
        // Nacionalidad
        TableColumn<Autor, String> nacionalidadColumn = new TableColumn<>("NACIONALIDAD");
        nacionalidadColumn.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        nacionalidadColumn.setStyle("-fx-alignment: CENTER;");
        
        // Acciones
        TableColumn<Autor, Void> actionsColumn = new TableColumn<>("ACCIONES");
        actionsColumn.setStyle("-fx-alignment: CENTER;");
        actionsColumn.setCellFactory(param -> new TableCell<Autor, Void>() {
            private final Button editButton = new Button(SvgMapper.EDIT_ICON);
            private final Button deleteButton = new Button(SvgMapper.DELETE_ICON);
            
            {
                editButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 12; -fx-font-size: 12;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 12; -fx-font-size: 12;");
                
                editButton.setOnAction(e -> {
                    Autor autor = getTableView().getItems().get(getIndex());
                    editAutor(autor);
                });
                
                deleteButton.setOnAction(e -> {
                    Autor autor = getTableView().getItems().get(getIndex());
                    deleteAutor(autor);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5);
                    buttons.setAlignment(Pos.CENTER);
                    buttons.getChildren().addAll(editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
        
        autorTable.getColumns().addAll(idColumn, nombreColumn, fechaColumn, nacionalidadColumn, actionsColumn);
        
        // Configurar ancho de columnas para que ocupen el 100%
        idColumn.prefWidthProperty().bind(autorTable.widthProperty().multiply(0.10));
        nombreColumn.prefWidthProperty().bind(autorTable.widthProperty().multiply(0.35));
        fechaColumn.prefWidthProperty().bind(autorTable.widthProperty().multiply(0.25));
        nacionalidadColumn.prefWidthProperty().bind(autorTable.widthProperty().multiply(0.20));
        actionsColumn.prefWidthProperty().bind(autorTable.widthProperty().multiply(0.10));
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
        refreshButton.setOnAction(e -> loadAutores());
        
        Button reporteButton = new Button("📊 Reporte de Libros");
        reporteButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        reporteButton.setOnAction(e -> showReporteDialog());
        
        buttonBox.getChildren().addAll(refreshButton, reporteButton);
        
        // Tabla de autores
        autorTable = new TableView<>();
        autorTable.setItems(autorData);
        autorTable.setRowFactory(tv -> {
            TableRow<Autor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Autor autor = row.getItem();
                    editAutor(autor);
                }
            });
            return row;
        });
        
        // Configurar columnas
        setupTableColumns();
        
        // Botones de acción de fila
        TableColumn<Autor, Void> actionsColumn = new TableColumn<>("Acciones");
        actionsColumn.setPrefWidth(120);
        actionsColumn.setCellFactory(param -> new TableCell<Autor, Void>() {
            private final Button editButton = new Button("✏️");
            private final Button deleteButton = new Button("🗑️");
            
            {
                editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                
                editButton.setOnAction(e -> {
                    Autor autor = getTableView().getItems().get(getIndex());
                    editAutor(autor);
                });
                
                deleteButton.setOnAction(e -> {
                    Autor autor = getTableView().getItems().get(getIndex());
                    deleteAutor(autor);
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
        
        autorTable.getColumns().add(actionsColumn);
        
        panel.getChildren().addAll(buttonBox, autorTable);
        
        return panel;
    }
    
    private VBox createFormPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setStyle("-fx-background-color: #faf8d4; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        
        // Header del formulario
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        
        Text formTitle = new Text("Registrar/Editar Autor");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setStyle("-fx-fill: #181818;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Botón cerrar
        Button closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 12; -fx-font-size: 14; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> {
            // Cerrar el formulario
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        
        headerContainer.getChildren().addAll(formTitle, spacer, closeButton);
        
        // Campos del formulario
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);");
        
        // Nombre
        Label nombreLabel = createFormLabel("Nombre *");
        nombreField = createFormTextField("Ingrese el nombre del autor");
        
        // Fecha de nacimiento
        Label fechaLabel = createFormLabel("Fecha de Nacimiento *");
        fechaNacimientoPicker = createFormDatePicker();
        
        // Nacionalidad
        Label nacionalidadLabel = createFormLabel("Nacionalidad");
        nacionalidadField = createFormTextField("Ingrese la nacionalidad (opcional)");
        
        // Biografía
        Label biografiaLabel = createFormLabel("Biografía");
        biografiaArea = createFormTextArea("Ingrese la biografía del autor (opcional)");
        
        formContainer.getChildren().addAll(
            nombreLabel, nombreField,
            fechaLabel, fechaNacimientoPicker,
            nacionalidadLabel, nacionalidadField,
            biografiaLabel, biografiaArea
        );
        
        // Botones
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        saveButton = new Button("💾 GUARDAR");
        saveButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        saveButton.setOnAction(e -> saveAutor());
        
        cancelButton = new Button("❌ CANCELAR");
        cancelButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        cancelButton.setOnAction(e -> clearForm());
        
        buttonContainer.getChildren().addAll(saveButton, cancelButton);
        
        panel.getChildren().addAll(headerContainer, formContainer, buttonContainer);
        
        return panel;
    }
    
    private void loadAutores() {
        try {
            autorData.clear();
            List<Autor> autores = autorController.obtenerListaAutores();
            autorData.addAll(autores);
        } catch (Exception e) {
            showAlert("Error", "Error al cargar autores: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void saveAutor() {
        try {
            // Validaciones
            if (nombreField.getText().trim().isEmpty()) {
                showAlert("Error", "El nombre es obligatorio", Alert.AlertType.ERROR);
                return;
            }
            
            if (fechaNacimientoPicker.getValue() == null) {
                showAlert("Error", "La fecha de nacimiento es obligatoria", Alert.AlertType.ERROR);
                return;
            }
            
            // Crear o actualizar autor
            if (isEditing && autorEditando != null) {
                // Actualizar autor existente
                autorEditando.setNombre(nombreField.getText().trim());
                autorEditando.setFechaNacimiento(fechaNacimientoPicker.getValue());
                autorEditando.setNacionalidad(nacionalidadField.getText().trim().isEmpty() ? null : nacionalidadField.getText().trim());
                autorEditando.setBiografia(biografiaArea.getText().trim().isEmpty() ? null : biografiaArea.getText().trim());
                
                // Llamar al controlador para actualizar
                autorController.actualizarAutor(autorEditando);
                
                showAlert("Éxito", "Autor actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                // Crear nuevo autor usando el método con parámetros
                Autor nuevoAutor = autorController.registrarAutor(
                    nombreField.getText().trim(),
                    fechaNacimientoPicker.getValue(),
                    nacionalidadField.getText().trim().isEmpty() ? null : nacionalidadField.getText().trim(),
                    biografiaArea.getText().trim().isEmpty() ? null : biografiaArea.getText().trim()
                );
                
                showAlert("Éxito", "Autor registrado correctamente con ID: " + nuevoAutor.getIdAutor(), Alert.AlertType.INFORMATION);
            }
            
            clearForm();
            loadAutores();
            
        } catch (Exception e) {
            showAlert("Error", "Error al guardar autor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void editAutor(Autor autor) {
        isEditing = true;
        autorEditando = autor;
        
        nombreField.setText(autor.getNombre());
        fechaNacimientoPicker.setValue(autor.getFechaNacimiento());
        nacionalidadField.setText(autor.getNacionalidad() != null ? autor.getNacionalidad() : "");
        biografiaArea.setText(autor.getBiografia() != null ? autor.getBiografia() : "");
        
        saveButton.setText("💾 Actualizar");
    }
    
    private void deleteAutor(Autor autor) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de que desea eliminar este autor?");
        alert.setContentText("Autor: " + autor.getNombre() + "\nEsta acción no se puede deshacer.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Llamar al controlador para eliminar
                    autorController.eliminarAutor(autor.getIdAutor());
                    
                    showAlert("Éxito", "Autor eliminado correctamente", Alert.AlertType.INFORMATION);
                    loadAutores();
                } catch (Exception e) {
                    showAlert("Error", "Error al eliminar autor: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }
    
    private void clearForm() {
        isEditing = false;
        autorEditando = null;
        
        nombreField.clear();
        fechaNacimientoPicker.setValue(null);
        nacionalidadField.clear();
        biografiaArea.clear();
        
        saveButton.setText("💾 Guardar");
    }
    
    private void showReporteDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reporte de Libros por Autor");
        dialog.setHeaderText("Ingrese el ID del autor");
        dialog.setContentText("ID del autor:");
        
        dialog.showAndWait().ifPresent(idStr -> {
            try {
                int idAutor = Integer.parseInt(idStr);
                
                // Obtener información del autor
                Autor autor = autorController.obtenerAutor(idAutor);
                if (autor == null) {
                    showAlert("Error", "No se encontró el autor con ID: " + idAutor, Alert.AlertType.ERROR);
                    return;
                }
                
                // Obtener libros del autor
                List<Libro> libros = autorController.reporteLibrosPorAutor(idAutor);
                
                // Mostrar reporte detallado
                showReporteDetallado(autor, libros);
                
            } catch (NumberFormatException e) {
                showAlert("Error", "ID inválido", Alert.AlertType.ERROR);
            } catch (Exception e) {
                showAlert("Error", "Error al generar reporte: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }
    
    private void showReporteDetallado(Autor autor, List<Libro> libros) {
        // Crear ventana de reporte
        Stage reporteStage = new Stage();
        reporteStage.setTitle("Reporte de Libros por Autor");
        reporteStage.setWidth(800);
        reporteStage.setHeight(600);
        
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Información del autor
        VBox autorInfo = new VBox(10);
        autorInfo.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text autorTitle = new Text("Información del Autor");
        autorTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        autorTitle.setStyle("-fx-fill: #2c3e50;");
        
        Text autorNombre = new Text("Nombre: " + autor.getNombre());
        autorNombre.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Text autorNacionalidad = new Text("Nacionalidad: " + (autor.getNacionalidad() != null ? autor.getNacionalidad() : "No especificada"));
        Text autorFecha = new Text("Fecha de nacimiento: " + autor.getFechaNacimiento());
        
        autorInfo.getChildren().addAll(autorTitle, autorNombre, autorNacionalidad, autorFecha);
        
        // Tabla de libros
        VBox librosContainer = new VBox(10);
        librosContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text librosTitle = new Text("Libros del Autor");
        librosTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        librosTitle.setStyle("-fx-fill: #2c3e50;");
        
        TableView<Libro> librosTable = new TableView<>();
        ObservableList<Libro> librosData = FXCollections.observableArrayList(libros);
        librosTable.setItems(librosData);
        
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
        
        librosTable.getColumns().addAll(idColumn, tituloColumn, editorialColumn, añoColumn, precioColumn, tipoColumn, stockColumn);
        
        // Resumen
        VBox resumenContainer = new VBox(10);
        resumenContainer.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10; -fx-padding: 15;");
        
        Text resumenTitle = new Text("Resumen");
        resumenTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resumenTitle.setStyle("-fx-fill: white;");
        
        long librosFisicos = libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.FISICO).count();
        long librosDigitales = libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.DIGITAL).count();
        long audiolibros = libros.stream().filter(l -> l.getTipoLibro() == Libro.TipoLibro.AUDIOLIBRO).count();
        
        Text totalLibros = new Text("Total de libros: " + libros.size());
        totalLibros.setStyle("-fx-fill: white;");
        
        Text fisicos = new Text("Libros físicos: " + librosFisicos);
        fisicos.setStyle("-fx-fill: white;");
        
        Text digitales = new Text("Libros digitales: " + librosDigitales);
        digitales.setStyle("-fx-fill: white;");
        
        Text audio = new Text("Audiolibros: " + audiolibros);
        audio.setStyle("-fx-fill: white;");
        
        resumenContainer.getChildren().addAll(resumenTitle, totalLibros, fisicos, digitales, audio);
        
        librosContainer.getChildren().addAll(librosTitle, librosTable, resumenContainer);
        
        mainContainer.getChildren().addAll(autorInfo, librosContainer);
        
        Scene scene = new Scene(mainContainer);
        reporteStage.setScene(scene);
        reporteStage.show();
    }
  
    
    private void searchAutores(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                loadAutores(); // Si no hay criterio, cargar todos
                return;
            }
            
            List<Autor> resultados = autorController.buscarAutores(criterio.trim());
            autorData.clear();
            autorData.addAll(resultados);
            
            if (resultados.isEmpty()) {
                showAlert("Búsqueda", "No se encontraron autores con el criterio: " + criterio, Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Error al buscar autores: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void showCreateForm() {
        // Crear un diálogo modal para el formulario de creación
        Stage formStage = new Stage();
        formStage.setTitle("Nuevo Autor");
        formStage.setResizable(true);
        formStage.setMinWidth(500);
        formStage.setMinHeight(600);
        
        // Formulario con el nuevo diseño
        VBox formPanel = createFormPanel();
        
        Scene scene = new Scene(formPanel, 500, 600);
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
    
    // Métodos helper para crear elementos del formulario con estilo del dashboard
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
    
    private DatePicker createFormDatePicker() {
        DatePicker picker = new DatePicker();
        picker.setPromptText("Seleccione la fecha");
        picker.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 12; -fx-font-size: 14;");
        picker.setPrefHeight(40);
        return picker;
    }
    
    private TextArea createFormTextArea(String promptText) {
        TextArea area = new TextArea();
        area.setPromptText(promptText);
        area.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 12; -fx-font-size: 14;");
        area.setPrefRowCount(4);
        return area;
    }
    
    public VBox getView() {
        return mainContainer;
    }
}
