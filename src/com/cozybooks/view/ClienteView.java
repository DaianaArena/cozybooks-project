package com.cozybooks.view;

import com.cozybooks.controller.ClienteController;
import com.cozybooks.model.Cliente;
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
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Título
        Text title = new Text("Gestión de Clientes");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-fill: #2c3e50;");
        
        // Crear el layout principal con split pane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.6);
        
        // Panel izquierdo - Tabla de clientes
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
        
        // Columnas de la tabla
        TableColumn<Cliente, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        idColumn.setPrefWidth(60);
        
        TableColumn<Cliente, String> nombreColumn = new TableColumn<>("Nombre");
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreColumn.setPrefWidth(200);
        
        TableColumn<Cliente, String> documentoColumn = new TableColumn<>("Documento");
        documentoColumn.setCellValueFactory(new PropertyValueFactory<>("documento"));
        documentoColumn.setPrefWidth(100);
        
        TableColumn<Cliente, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setPrefWidth(200);
        
        TableColumn<Cliente, String> telefonoColumn = new TableColumn<>("Teléfono");
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        telefonoColumn.setPrefWidth(120);
        
        clienteTable.getColumns().addAll(idColumn, nombreColumn, documentoColumn, emailColumn, telefonoColumn);
        
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
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text formTitle = new Text("Registrar/Editar Cliente");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setStyle("-fx-fill: #2c3e50;");
        
        // Campos del formulario
        VBox formContainer = new VBox(10);
        
        // Nombre
        Label nombreLabel = new Label("Nombre *");
        nombreField = new TextField();
        nombreField.setPromptText("Ingrese el nombre completo del cliente");
        
        // Documento
        Label documentoLabel = new Label("Documento (DNI) *");
        documentoField = new TextField();
        documentoField.setPromptText("Ingrese DNI de 8 dígitos");
        
        // Email
        Label emailLabel = new Label("Email");
        emailField = new TextField();
        emailField.setPromptText("ejemplo@correo.com (opcional)");
        
        // Teléfono
        Label telefonoLabel = new Label("Teléfono");
        telefonoField = new TextField();
        telefonoField.setPromptText("Ingrese número de teléfono (opcional)");
        
        formContainer.getChildren().addAll(
            nombreLabel, nombreField,
            documentoLabel, documentoField,
            emailLabel, emailField,
            telefonoLabel, telefonoField
        );
        
        // Botones
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        saveButton = new Button("💾 Guardar");
        saveButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> saveCliente());
        
        cancelButton = new Button("❌ Cancelar");
        cancelButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        cancelButton.setOnAction(e -> clearForm());
        
        buttonContainer.getChildren().addAll(saveButton, cancelButton);
        
        panel.getChildren().addAll(formTitle, formContainer, buttonContainer);
        
        return panel;
    }
    
    private void loadClientes() {
        try {
            // Simular la carga de datos desde el controlador
            clienteData.clear();
            
            // Aquí deberías llamar al método del controlador
            // List<Cliente> clientes = clienteController.listarClientes();
            // clienteData.addAll(clientes);
            
            showAlert("Información", "Datos cargados correctamente", Alert.AlertType.INFORMATION);
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
                // clienteController.actualizarCliente(clienteEditando);
                
                showAlert("Éxito", "Cliente actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                // Crear nuevo cliente
                Cliente nuevoCliente = new Cliente(
                    nombreField.getText().trim(),
                    documentoField.getText().trim(),
                    emailField.getText().trim().isEmpty() ? null : emailField.getText().trim(),
                    telefonoField.getText().trim().isEmpty() ? null : telefonoField.getText().trim()
                );
                
                // Llamar al controlador para registrar
                // clienteController.registrarCliente(nuevoCliente);
                
                showAlert("Éxito", "Cliente registrado correctamente", Alert.AlertType.INFORMATION);
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
                    // clienteController.eliminarCliente(cliente.getIdCliente());
                    
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
                // Cliente cliente = clienteController.buscarCliente(criterio);
                
                showAlert("Información", "Búsqueda completada", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", "Error al buscar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
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
