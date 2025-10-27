package com.cozybooks.view;

import com.cozybooks.controller.ClienteController;
import com.cozybooks.model.Cliente;
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

import java.util.List;
import java.util.regex.Pattern;

/**
 * Vista para la gestión de Clientes
 */
public class ClienteView {
    
    private ClienteController clienteController;
    private VBox mainContainer;
    private TableView<Cliente> clienteTable;
    private ObservableList<Cliente> clienteData;
    
    // Formulario de registro/actualización
    private TextField nombreField;
    private TextField documentoField;
    private TextField emailField;
    private TextField telefonoField;
    private Button saveButton;
    private Button cancelButton;
    private boolean isEditing = false;
    private Cliente clienteEditando;
    
    public ClienteView(ClienteController clienteController) {
        this.clienteController = clienteController;
        this.clienteData = FXCollections.observableArrayList();
        setupUI();
        loadClientes();
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
        Text title = new Text("Gestión de Clientes");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: #181818;");
        
        // Espaciador
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Buscador
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar por nombre, documento o email...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ebdccb; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12;");
        
        // Botón de búsqueda
        Button searchButton = new Button(SvgMapper.SEARCH_ICON);
        searchButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8 12;");
        searchButton.setOnAction(e -> searchClientes(searchField.getText()));
        
        // Botón crear cliente
        Button createButton = new Button("➕ NUEVO CLIENTE");
        createButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20; -fx-font-size: 14;");
        createButton.setOnAction(e -> showCreateForm());
        
        HBox searchContainer = new HBox(8);
        searchContainer.getChildren().addAll(searchField, searchButton);
        
        header.getChildren().addAll(title, spacer, searchContainer, createButton);
        
        return header;
    }
    
    private VBox createTableContainer() {
        VBox container = new VBox(0);
        container.setPadding(new Insets(20));
        
        // Tabla de clientes
        clienteTable = new TableView<>();
        clienteTable.setItems(clienteData);
        clienteTable.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        clienteTable.setRowFactory(tv -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Cliente cliente = row.getItem();
                    editCliente(cliente);
                }
            });
            return row;
        });
        
        // Configurar columnas
        setupTableColumns();
        
        // Hacer que la tabla ocupe todo el ancho disponible
        VBox.setVgrow(clienteTable, Priority.ALWAYS);
        
        container.getChildren().add(clienteTable);
        
        return container;
    }
    
    private void setupTableColumns() {
        // Limpiar columnas existentes
        clienteTable.getColumns().clear();
        
        // ID
        TableColumn<Cliente, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        idColumn.setStyle("-fx-alignment: CENTER;");
        
        // Nombre
        TableColumn<Cliente, String> nombreColumn = new TableColumn<>("NOMBRE");
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        // Documento
        TableColumn<Cliente, String> documentoColumn = new TableColumn<>("DOCUMENTO");
        documentoColumn.setCellValueFactory(new PropertyValueFactory<>("documento"));
        documentoColumn.setStyle("-fx-alignment: CENTER;");
        
        // Email
        TableColumn<Cliente, String> emailColumn = new TableColumn<>("EMAIL");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Teléfono
        TableColumn<Cliente, String> telefonoColumn = new TableColumn<>("TELÉFONO");
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        telefonoColumn.setStyle("-fx-alignment: CENTER;");
        
        // Acciones
        TableColumn<Cliente, Void> actionsColumn = new TableColumn<>("ACCIONES");
        actionsColumn.setStyle("-fx-alignment: CENTER;");
        actionsColumn.setCellFactory(param -> new TableCell<Cliente, Void>() {
            private final Button editButton = new Button(SvgMapper.EDIT_ICON);
            private final Button deleteButton = new Button(SvgMapper.DELETE_ICON);
            
            {
                editButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 12; -fx-font-size: 12;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 12; -fx-font-size: 12;");
                
                editButton.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    editCliente(cliente);
                });
                
                deleteButton.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    deleteCliente(cliente);
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
        
        clienteTable.getColumns().addAll(idColumn, nombreColumn, documentoColumn, emailColumn, telefonoColumn, actionsColumn);
        
        // Configurar ancho de columnas para que ocupen el 100%
        idColumn.prefWidthProperty().bind(clienteTable.widthProperty().multiply(0.08));
        nombreColumn.prefWidthProperty().bind(clienteTable.widthProperty().multiply(0.25));
        documentoColumn.prefWidthProperty().bind(clienteTable.widthProperty().multiply(0.15));
        emailColumn.prefWidthProperty().bind(clienteTable.widthProperty().multiply(0.30));
        telefonoColumn.prefWidthProperty().bind(clienteTable.widthProperty().multiply(0.12));
        actionsColumn.prefWidthProperty().bind(clienteTable.widthProperty().multiply(0.10));
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
        refreshButton.setOnAction(e -> loadClientes());
        
        Button searchButton = new Button("🔍 Buscar");
        searchButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        searchButton.setOnAction(e -> showSearchDialog());
        
        buttonBox.getChildren().addAll(refreshButton, searchButton);
        
        // Tabla de clientes
        clienteTable = new TableView<>();
        clienteTable.setItems(clienteData);
        clienteTable.setRowFactory(tv -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Cliente cliente = row.getItem();
                    editCliente(cliente);
                }
            });
            return row;
        });
        
        // Configurar columnas
        setupTableColumns();
        
        // Botones de acción de fila
        TableColumn<Cliente, Void> actionsColumn = new TableColumn<>("Acciones");
        actionsColumn.setPrefWidth(120);
        actionsColumn.setCellFactory(param -> new TableCell<Cliente, Void>() {
            private final Button editButton = new Button("✏️");
            private final Button deleteButton = new Button("🗑️");
            
            {
                editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                
                editButton.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    editCliente(cliente);
                });
                
                deleteButton.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    deleteCliente(cliente);
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
        
        clienteTable.getColumns().add(actionsColumn);
        
        panel.getChildren().addAll(buttonBox, clienteTable);
        
        return panel;
    }
    
    private VBox createFormPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setStyle("-fx-background-color: #faf8d4; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        
        // Header del formulario
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        
        Text formTitle = new Text("Registrar/Editar Cliente");
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
        nombreField = createFormTextField("Ingrese el nombre completo del cliente");
        
        // Documento
        Label documentoLabel = createFormLabel("Documento (DNI) *");
        documentoField = createFormTextField("Ingrese DNI de 8 dígitos");
        
        // Email
        Label emailLabel = createFormLabel("Email");
        emailField = createFormTextField("ejemplo@correo.com (opcional)");
        
        // Teléfono
        Label telefonoLabel = createFormLabel("Teléfono");
        telefonoField = createFormTextField("Ingrese número de teléfono (opcional)");
        
        formContainer.getChildren().addAll(
            nombreLabel, nombreField,
            documentoLabel, documentoField,
            emailLabel, emailField,
            telefonoLabel, telefonoField
        );
        
        // Botones
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        saveButton = new Button("💾 GUARDAR");
        saveButton.setStyle("-fx-background-color: #9f84bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        saveButton.setOnAction(e -> saveCliente());
        
        cancelButton = new Button("❌ CANCELAR");
        cancelButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 24; -fx-font-size: 14;");
        cancelButton.setOnAction(e -> clearForm());
        
        buttonContainer.getChildren().addAll(saveButton, cancelButton);
        
        panel.getChildren().addAll(headerContainer, formContainer, buttonContainer);
        
        return panel;
    }
    
    private void loadClientes() {
        try {
            clienteData.clear();
            List<Cliente> clientes = clienteController.obtenerListaClientes();
            clienteData.addAll(clientes);
        } catch (Exception e) {
            showAlert("Error", "Error al cargar clientes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void saveCliente() {
        try {
            // Validaciones
            if (nombreField.getText().trim().isEmpty()) {
                showAlert("Error", "El nombre es obligatorio", Alert.AlertType.ERROR);
                return;
            }
            
            if (!validarDocumento(documentoField.getText().trim())) {
                showAlert("Error", "El documento debe ser un DNI de 8 dígitos", Alert.AlertType.ERROR);
                return;
            }
            
            if (!emailField.getText().trim().isEmpty() && !validarEmail(emailField.getText().trim())) {
                showAlert("Error", "Formato de email inválido", Alert.AlertType.ERROR);
                return;
            }
            
            // Crear o actualizar cliente
            if (isEditing && clienteEditando != null) {
                // Actualizar cliente existente
                clienteEditando.setNombre(nombreField.getText().trim());
                clienteEditando.setDocumento(documentoField.getText().trim());
                clienteEditando.setEmail(emailField.getText().trim().isEmpty() ? null : emailField.getText().trim());
                clienteEditando.setTelefono(telefonoField.getText().trim().isEmpty() ? null : telefonoField.getText().trim());
                
                // Llamar al controlador para actualizar
                clienteController.actualizarCliente(clienteEditando);
                
                showAlert("Éxito", "Cliente actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                // Crear nuevo cliente usando el método con parámetros
                Cliente nuevoCliente = clienteController.registrarCliente(
                    nombreField.getText().trim(),
                    documentoField.getText().trim(),
                    emailField.getText().trim().isEmpty() ? null : emailField.getText().trim(),
                    telefonoField.getText().trim().isEmpty() ? null : telefonoField.getText().trim()
                );
                
                showAlert("Éxito", "Cliente registrado correctamente con ID: " + nuevoCliente.getIdCliente(), Alert.AlertType.INFORMATION);
            }
            
            clearForm();
            loadClientes();
            
        } catch (Exception e) {
            showAlert("Error", "Error al guardar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void editCliente(Cliente cliente) {
        isEditing = true;
        clienteEditando = cliente;
        
        nombreField.setText(cliente.getNombre());
        documentoField.setText(cliente.getDocumento());
        emailField.setText(cliente.getEmail() != null ? cliente.getEmail() : "");
        telefonoField.setText(cliente.getTelefono() != null ? cliente.getTelefono() : "");
        
        saveButton.setText("💾 Actualizar");
    }
    
    private void deleteCliente(Cliente cliente) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de que desea eliminar este cliente?");
        alert.setContentText("Cliente: " + cliente.getNombre() + "\nEsta acción no se puede deshacer.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Llamar al controlador para eliminar
                    clienteController.eliminarCliente(cliente.getIdCliente());
                    
                    showAlert("Éxito", "Cliente eliminado correctamente", Alert.AlertType.INFORMATION);
                    loadClientes();
                } catch (Exception e) {
                    showAlert("Error", "Error al eliminar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }
    
    private void clearForm() {
        isEditing = false;
        clienteEditando = null;
        
        nombreField.clear();
        documentoField.clear();
        emailField.clear();
        telefonoField.clear();
        
        saveButton.setText("💾 Guardar");
    }
    
    private void showSearchDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Cliente");
        dialog.setHeaderText("Ingrese criterio de búsqueda");
        dialog.setContentText("Nombre o documento:");
        
        dialog.showAndWait().ifPresent(criterio -> {
            try {
                // Llamar al controlador para buscar
                Cliente cliente = clienteController.buscarCliente(criterio);
                
                if (cliente != null) {
                    showClienteDetallado(cliente);
                } else {
                    showAlert("Información", "No se encontró ningún cliente con el criterio: " + criterio, Alert.AlertType.INFORMATION);
                }
            } catch (Exception e) {
                showAlert("Error", "Error al buscar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }
    
    private void showClienteDetallado(Cliente cliente) {
        // Crear ventana de detalles del cliente
        Stage clienteStage = new Stage();
        clienteStage.setTitle("Detalles del Cliente");
        clienteStage.setWidth(500);
        clienteStage.setHeight(400);
        
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Información del cliente
        VBox clienteInfo = new VBox(15);
        clienteInfo.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text clienteTitle = new Text("Información del Cliente");
        clienteTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        clienteTitle.setStyle("-fx-fill: #2c3e50;");
        
        VBox infoContainer = new VBox(10);
        
        Text idText = new Text("ID: " + cliente.getIdCliente());
        idText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Text nombreText = new Text("Nombre: " + cliente.getNombre());
        nombreText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Text documentoText = new Text("Documento: " + cliente.getDocumento());
        Text emailText = new Text("Email: " + (cliente.getEmail() != null ? cliente.getEmail() : "No especificado"));
        Text telefonoText = new Text("Teléfono: " + (cliente.getTelefono() != null ? cliente.getTelefono() : "No especificado"));
        
        String fechaRegistro = cliente.getFechaRegistro() != null ? 
            cliente.getFechaRegistro().toLocalDate().toString() : "No especificada";
        Text fechaText = new Text("Fecha de registro: " + fechaRegistro);
        
        infoContainer.getChildren().addAll(idText, nombreText, documentoText, emailText, telefonoText, fechaText);
        
        // Botones de acción
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        Button editButton = new Button("✏️ Editar");
        editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        editButton.setOnAction(e -> {
            clienteStage.close();
            editCliente(cliente);
        });
        
        Button closeButton = new Button("❌ Cerrar");
        closeButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        closeButton.setOnAction(e -> clienteStage.close());
        
        buttonContainer.getChildren().addAll(editButton, closeButton);
        
        clienteInfo.getChildren().addAll(clienteTitle, infoContainer, buttonContainer);
        mainContainer.getChildren().add(clienteInfo);
        
        Scene scene = new Scene(mainContainer);
        clienteStage.setScene(scene);
        clienteStage.show();
    }
    
    private boolean validarDocumento(String documento) {
        if (documento == null || documento.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d{8}$");
        return pattern.matcher(documento).matches();
    }
    
    private boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
        return pattern.matcher(email).matches();
    }
    
    
    private void searchClientes(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                loadClientes(); // Si no hay criterio, cargar todos
                return;
            }
            
            List<Cliente> resultados = clienteController.buscarClientes(criterio.trim());
            clienteData.clear();
            clienteData.addAll(resultados);
            
            if (resultados.isEmpty()) {
                showAlert("Búsqueda", "No se encontraron clientes con el criterio: " + criterio, Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Error al buscar clientes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void showCreateForm() {
        // Crear un diálogo modal para el formulario de creación
        Stage formStage = new Stage();
        formStage.setTitle("Nuevo Cliente");
        formStage.setResizable(true);
        formStage.setMinWidth(500);
        formStage.setMinHeight(500);
        
        // Formulario con el nuevo diseño
        VBox formPanel = createFormPanel();
        
        Scene scene = new Scene(formPanel, 500, 500);
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
    
    public VBox getView() {
        return mainContainer;
    }
}
