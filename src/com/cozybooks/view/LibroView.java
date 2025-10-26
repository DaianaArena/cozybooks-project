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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Título
        Text title = new Text("Gestión de Libros");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-fill: #2c3e50;");
        
        // Crear el layout principal con split pane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.6);
        
        // Panel izquierdo - Tabla de libros
        VBox leftPanel = createTablePanel();
        
        // Panel derecho - Formulario
        VBox rightPanel = createFormPanel();
        
        splitPane.getItems().addAll(leftPanel, rightPanel);
        
        mainContainer.getChildren().addAll(title, splitPane);
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
                    showAlert("Libros Encontrados", "Se encontraron " + libros.size() + " libro(s) con el criterio: " + criterio, Alert.AlertType.INFORMATION);
                }
            } catch (Exception e) {
                showAlert("Error", "Error al buscar libros: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
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
