package com.cozybooks.view;

import com.cozybooks.controller.AutorController;
import com.cozybooks.model.Autor;
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

import java.time.LocalDate;

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
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Título
        Text title = new Text("Gestión de Autores");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-fill: #2c3e50;");
        
        // Crear el layout principal con split pane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.6);
        
        // Panel izquierdo - Tabla de autores
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
        
        // Columnas de la tabla
        TableColumn<Autor, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
        idColumn.setPrefWidth(60);
        
        TableColumn<Autor, String> nombreColumn = new TableColumn<>("Nombre");
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreColumn.setPrefWidth(200);
        
        TableColumn<Autor, LocalDate> fechaColumn = new TableColumn<>("Fecha Nacimiento");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        fechaColumn.setPrefWidth(120);
        
        TableColumn<Autor, String> nacionalidadColumn = new TableColumn<>("Nacionalidad");
        nacionalidadColumn.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        nacionalidadColumn.setPrefWidth(120);
        
        autorTable.getColumns().addAll(idColumn, nombreColumn, fechaColumn, nacionalidadColumn);
        
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
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text formTitle = new Text("Registrar/Editar Autor");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setStyle("-fx-fill: #2c3e50;");
        
        // Campos del formulario
        VBox formContainer = new VBox(10);
        
        // Nombre
        Label nombreLabel = new Label("Nombre *");
        nombreField = new TextField();
        nombreField.setPromptText("Ingrese el nombre del autor");
        
        // Fecha de nacimiento
        Label fechaLabel = new Label("Fecha de Nacimiento *");
        fechaNacimientoPicker = new DatePicker();
        fechaNacimientoPicker.setPromptText("Seleccione la fecha");
        
        // Nacionalidad
        Label nacionalidadLabel = new Label("Nacionalidad");
        nacionalidadField = new TextField();
        nacionalidadField.setPromptText("Ingrese la nacionalidad (opcional)");
        
        // Biografía
        Label biografiaLabel = new Label("Biografía");
        biografiaArea = new TextArea();
        biografiaArea.setPromptText("Ingrese la biografía del autor (opcional)");
        biografiaArea.setPrefRowCount(4);
        
        formContainer.getChildren().addAll(
            nombreLabel, nombreField,
            fechaLabel, fechaNacimientoPicker,
            nacionalidadLabel, nacionalidadField,
            biografiaLabel, biografiaArea
        );
        
        // Botones
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        saveButton = new Button("💾 Guardar");
        saveButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> saveAutor());
        
        cancelButton = new Button("❌ Cancelar");
        cancelButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        cancelButton.setOnAction(e -> clearForm());
        
        buttonContainer.getChildren().addAll(saveButton, cancelButton);
        
        panel.getChildren().addAll(formTitle, formContainer, buttonContainer);
        
        return panel;
    }
    
    private void loadAutores() {
        try {
            // Simular la carga de datos desde el controlador
            // En una implementación real, esto llamaría a autorController.listar()
            autorData.clear();
            
            // Aquí deberías llamar al método del controlador
            // List<Autor> autores = autorController.listarAutores();
            // autorData.addAll(autores);
            
            showAlert("Información", "Datos cargados correctamente", Alert.AlertType.INFORMATION);
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
                // autorController.actualizarAutor(autorEditando);
                
                showAlert("Éxito", "Autor actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                // Crear nuevo autor
                Autor nuevoAutor = new Autor(
                    nombreField.getText().trim(),
                    fechaNacimientoPicker.getValue(),
                    nacionalidadField.getText().trim().isEmpty() ? null : nacionalidadField.getText().trim(),
                    biografiaArea.getText().trim().isEmpty() ? null : biografiaArea.getText().trim()
                );
                
                // Llamar al controlador para registrar
                // autorController.registrarAutor(nuevoAutor);
                
                showAlert("Éxito", "Autor registrado correctamente", Alert.AlertType.INFORMATION);
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
                    // autorController.eliminarAutor(autor.getIdAutor());
                    
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
                // Llamar al controlador para generar reporte
                // autorController.reporteLibrosPorAutor(idAutor);
                
                showAlert("Información", "Reporte generado correctamente", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                showAlert("Error", "ID inválido", Alert.AlertType.ERROR);
            } catch (Exception e) {
                showAlert("Error", "Error al generar reporte: " + e.getMessage(), Alert.AlertType.ERROR);
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
