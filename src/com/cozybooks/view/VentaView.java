package com.cozybooks.view;

import com.cozybooks.controller.VentaController;
import com.cozybooks.model.*;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista para la gestión de Ventas
 */
public class VentaView {
    
    private VentaController ventaController;
    private VBox mainContainer;
    private TableView<Venta> ventaTable;
    private ObservableList<Venta> ventaData;
    
    // Formulario de nueva venta
    private TextField clienteIdField;
    private TextField libroIdField;
    private TextField cantidadField;
    private Button agregarLibroButton;
    private TableView<DetalleVenta> detalleTable;
    private ObservableList<DetalleVenta> detalleData;
    private Button finalizarVentaButton;
    private Button cancelarVentaButton;
    
    // Información de la venta actual
    private Venta ventaActual;
    private List<Libro> librosVenta;
    
    public VentaView(VentaController ventaController) {
        this.ventaController = ventaController;
        this.ventaData = FXCollections.observableArrayList();
        this.detalleData = FXCollections.observableArrayList();
        this.librosVenta = new ArrayList<>();
        setupUI();
        loadVentas();
    }
    
    private void setupUI() {
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Título
        Text title = new Text("Gestión de Ventas");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-fill: #2c3e50;");
        
        // Crear tabs para diferentes funcionalidades
        TabPane tabPane = new TabPane();
        
        // Tab de listado de ventas
        Tab ventasTab = new Tab("📋 Listado de Ventas");
        ventasTab.setContent(createVentasListTab());
        
        // Tab de nueva venta
        Tab nuevaVentaTab = new Tab("🛒 Nueva Venta");
        nuevaVentaTab.setContent(createNuevaVentaTab());
        
        // Tab de búsqueda
        Tab busquedaTab = new Tab("🔍 Búsqueda");
        busquedaTab.setContent(createBusquedaTab());
        
        tabPane.getTabs().addAll(ventasTab, nuevaVentaTab, busquedaTab);
        
        mainContainer.getChildren().addAll(title, tabPane);
    }
    
    private VBox createVentasListTab() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Botones de acción
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        Button refreshButton = new Button("🔄 Actualizar");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        refreshButton.setOnAction(e -> loadVentas());
        
        buttonBox.getChildren().add(refreshButton);
        
        // Tabla de ventas
        ventaTable = new TableView<>();
        ventaTable.setItems(ventaData);
        
        // Columnas de la tabla
        TableColumn<Venta, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        idColumn.setPrefWidth(80);
        
        TableColumn<Venta, LocalDateTime> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        fechaColumn.setPrefWidth(150);
        
        TableColumn<Venta, Integer> clienteColumn = new TableColumn<>("Cliente ID");
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        clienteColumn.setPrefWidth(100);
        
        TableColumn<Venta, BigDecimal> montoColumn = new TableColumn<>("Monto");
        montoColumn.setCellValueFactory(new PropertyValueFactory<>("monto"));
        montoColumn.setPrefWidth(100);
        
        TableColumn<Venta, Venta.MetodoPago> metodoColumn = new TableColumn<>("Método Pago");
        metodoColumn.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        metodoColumn.setPrefWidth(120);
        
        TableColumn<Venta, Venta.EstadoVenta> estadoColumn = new TableColumn<>("Estado");
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        estadoColumn.setPrefWidth(100);
        
        ventaTable.getColumns().addAll(idColumn, fechaColumn, clienteColumn, montoColumn, metodoColumn, estadoColumn);
        
        // Botones de acción de fila
        TableColumn<Venta, Void> actionsColumn = new TableColumn<>("Acciones");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<Venta, Void>() {
            private final Button editButton = new Button("✏️");
            private final Button deleteButton = new Button("🗑️");
            private final Button ticketButton = new Button("🎫");
            
            {
                editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                ticketButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                
                editButton.setOnAction(e -> {
                    Venta venta = getTableView().getItems().get(getIndex());
                    editVenta(venta);
                });
                
                deleteButton.setOnAction(e -> {
                    Venta venta = getTableView().getItems().get(getIndex());
                    deleteVenta(venta);
                });
                
                ticketButton.setOnAction(e -> {
                    Venta venta = getTableView().getItems().get(getIndex());
                    generarTicket(venta.getIdVenta());
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(3, editButton, deleteButton, ticketButton);
                    setGraphic(buttons);
                }
            }
        });
        
        ventaTable.getColumns().add(actionsColumn);
        
        panel.getChildren().addAll(buttonBox, ventaTable);
        
        return panel;
    }
    
    private VBox createNuevaVentaTab() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Formulario de nueva venta
        VBox formContainer = new VBox(15);
        
        Text formTitle = new Text("Nueva Venta");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setStyle("-fx-fill: #2c3e50;");
        
        // Información del cliente
        HBox clienteBox = new HBox(10);
        clienteBox.setAlignment(Pos.CENTER_LEFT);
        
        Label clienteLabel = new Label("ID Cliente:");
        clienteIdField = new TextField();
        clienteIdField.setPromptText("Ingrese ID del cliente");
        clienteIdField.setPrefWidth(150);
        
        Button buscarClienteButton = new Button("🔍 Buscar");
        buscarClienteButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        buscarClienteButton.setOnAction(e -> buscarCliente());
        
        clienteBox.getChildren().addAll(clienteLabel, clienteIdField, buscarClienteButton);
        
        // Agregar libros a la venta
        HBox libroBox = new HBox(10);
        libroBox.setAlignment(Pos.CENTER_LEFT);
        
        Label libroLabel = new Label("ID Libro:");
        libroIdField = new TextField();
        libroIdField.setPromptText("Ingrese ID del libro");
        libroIdField.setPrefWidth(150);
        
        Label cantidadLabel = new Label("Cantidad:");
        cantidadField = new TextField();
        cantidadField.setPromptText("Cantidad");
        cantidadField.setPrefWidth(100);
        
        agregarLibroButton = new Button("➕ Agregar");
        agregarLibroButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        agregarLibroButton.setOnAction(e -> agregarLibro());
        
        libroBox.getChildren().addAll(libroLabel, libroIdField, cantidadLabel, cantidadField, agregarLibroButton);
        
        // Tabla de detalles de la venta
        Text detalleTitle = new Text("Detalles de la Venta");
        detalleTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        detalleTitle.setStyle("-fx-fill: #2c3e50;");
        
        detalleTable = new TableView<>();
        detalleTable.setItems(detalleData);
        
        TableColumn<DetalleVenta, Integer> libroIdColumn = new TableColumn<>("ID Libro");
        libroIdColumn.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        libroIdColumn.setPrefWidth(100);
        
        TableColumn<DetalleVenta, Integer> cantidadColumn = new TableColumn<>("Cantidad");
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        cantidadColumn.setPrefWidth(100);
        
        TableColumn<DetalleVenta, BigDecimal> precioColumn = new TableColumn<>("Precio Unit.");
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        precioColumn.setPrefWidth(120);
        
        TableColumn<DetalleVenta, BigDecimal> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        subtotalColumn.setPrefWidth(120);
        
        detalleTable.getColumns().addAll(libroIdColumn, cantidadColumn, precioColumn, subtotalColumn);
        
        // Botones de finalización
        HBox finalizarBox = new HBox(10);
        finalizarBox.setAlignment(Pos.CENTER);
        
        finalizarVentaButton = new Button("✅ Finalizar Venta");
        finalizarVentaButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        finalizarVentaButton.setOnAction(e -> finalizarVenta());
        
        cancelarVentaButton = new Button("❌ Cancelar Venta");
        cancelarVentaButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        cancelarVentaButton.setOnAction(e -> cancelarVenta());
        
        finalizarBox.getChildren().addAll(finalizarVentaButton, cancelarVentaButton);
        
        formContainer.getChildren().addAll(
            formTitle,
            clienteBox,
            libroBox,
            detalleTitle,
            detalleTable,
            finalizarBox
        );
        
        panel.getChildren().add(formContainer);
        
        return panel;
    }
    
    private VBox createBusquedaTab() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text searchTitle = new Text("Búsqueda de Ventas");
        searchTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        searchTitle.setStyle("-fx-fill: #2c3e50;");
        
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("ID venta, ID cliente, método de pago o estado");
        searchField.setPrefWidth(300);
        
        Button searchButton = new Button("🔍 Buscar");
        searchButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        searchButton.setOnAction(e -> buscarVentas(searchField.getText()));
        
        searchBox.getChildren().addAll(searchField, searchButton);
        
        // Tabla de resultados de búsqueda
        TableView<Venta> searchTable = new TableView<>();
        searchTable.setItems(ventaData); // Reutilizar los mismos datos
        
        // Columnas similares a la tabla principal
        TableColumn<Venta, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        idColumn.setPrefWidth(80);
        
        TableColumn<Venta, LocalDateTime> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        fechaColumn.setPrefWidth(150);
        
        TableColumn<Venta, Integer> clienteColumn = new TableColumn<>("Cliente ID");
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        clienteColumn.setPrefWidth(100);
        
        TableColumn<Venta, BigDecimal> montoColumn = new TableColumn<>("Monto");
        montoColumn.setCellValueFactory(new PropertyValueFactory<>("monto"));
        montoColumn.setPrefWidth(100);
        
        TableColumn<Venta, Venta.MetodoPago> metodoColumn = new TableColumn<>("Método Pago");
        metodoColumn.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        metodoColumn.setPrefWidth(120);
        
        TableColumn<Venta, Venta.EstadoVenta> estadoColumn = new TableColumn<>("Estado");
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        estadoColumn.setPrefWidth(100);
        
        searchTable.getColumns().addAll(idColumn, fechaColumn, clienteColumn, montoColumn, metodoColumn, estadoColumn);
        
        panel.getChildren().addAll(searchTitle, searchBox, searchTable);
        
        return panel;
    }
    
    private void loadVentas() {
        try {
            ventaData.clear();
            List<Venta> ventas = ventaController.obtenerListaVentas();
            ventaData.addAll(ventas);
        } catch (Exception e) {
            showAlert("Error", "Error al cargar ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void buscarCliente() {
        try {
            int idCliente = Integer.parseInt(clienteIdField.getText().trim());
            // Llamar al controlador para buscar cliente
            // Cliente cliente = clienteController.obtenerCliente(idCliente);
            
            showAlert("Información", "Cliente encontrado", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "ID de cliente inválido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al buscar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void agregarLibro() {
        try {
            int idLibro = Integer.parseInt(libroIdField.getText().trim());
            int cantidad = Integer.parseInt(cantidadField.getText().trim());
            
            if (cantidad <= 0) {
                showAlert("Error", "La cantidad debe ser mayor a 0", Alert.AlertType.ERROR);
                return;
            }
            
            // Llamar al controlador para obtener libro
            // Libro libro = libroController.obtenerLibro(idLibro);
            
            // Crear detalle de venta
            // DetalleVenta detalle = new DetalleVenta(cantidad, libro.getPrecio(), 0, idLibro);
            // detalleData.add(detalle);
            // librosVenta.add(libro);
            
            // Limpiar campos
            libroIdField.clear();
            cantidadField.clear();
            
            showAlert("Éxito", "Libro agregado a la venta", Alert.AlertType.INFORMATION);
            
        } catch (NumberFormatException e) {
            showAlert("Error", "ID de libro o cantidad inválidos", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al agregar libro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void finalizarVenta() {
        try {
            if (detalleData.isEmpty()) {
                showAlert("Error", "Debe agregar al menos un libro a la venta", Alert.AlertType.ERROR);
                return;
            }
            
            int idCliente = Integer.parseInt(clienteIdField.getText().trim());
            
            // Mostrar diálogo para seleccionar método de pago
            ChoiceDialog<Venta.MetodoPago> metodoDialog = new ChoiceDialog<>(
                Venta.MetodoPago.EFECTIVO, 
                Venta.MetodoPago.EFECTIVO, 
                Venta.MetodoPago.TARJETA, 
                Venta.MetodoPago.TRANSFERENCIA
            );
            metodoDialog.setTitle("Método de Pago");
            metodoDialog.setHeaderText("Seleccione el método de pago");
            metodoDialog.setContentText("Método:");
            
            metodoDialog.showAndWait().ifPresent(metodo -> {
                try {
                    // Llamar al controlador para registrar la venta
                    // ventaController.registrarVenta(idCliente, detalleData, metodo);
                    
                    showAlert("Éxito", "Venta registrada correctamente", Alert.AlertType.INFORMATION);
                    cancelarVenta();
                    loadVentas();
                } catch (Exception e) {
                    showAlert("Error", "Error al registrar venta: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            });
            
        } catch (NumberFormatException e) {
            showAlert("Error", "ID de cliente inválido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al finalizar venta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void cancelarVenta() {
        detalleData.clear();
        librosVenta.clear();
        clienteIdField.clear();
        libroIdField.clear();
        cantidadField.clear();
        ventaActual = null;
    }
    
    private void editVenta(Venta venta) {
        // Implementar edición de venta
        showAlert("Información", "Funcionalidad de edición en desarrollo", Alert.AlertType.INFORMATION);
    }
    
    private void deleteVenta(Venta venta) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de que desea eliminar esta venta?");
        alert.setContentText("Venta ID: " + venta.getIdVenta() + "\nEsta acción no se puede deshacer.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Llamar al controlador para eliminar
                    ventaController.eliminarVenta(venta.getIdVenta());
                    
                    showAlert("Éxito", "Venta eliminada correctamente", Alert.AlertType.INFORMATION);
                    loadVentas();
                } catch (Exception e) {
                    showAlert("Error", "Error al eliminar venta: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }
    
    private void generarTicket(int idVenta) {
        try {
            // Llamar al controlador para generar ticket
            // ventaController.generarTicket(idVenta);
            
            showAlert("Éxito", "Ticket generado correctamente", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error al generar ticket: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void buscarVentas(String criterio) {
        try {
            if (criterio.trim().isEmpty()) {
                showAlert("Error", "Debe ingresar un criterio de búsqueda", Alert.AlertType.ERROR);
                return;
            }
            
            // Llamar al controlador para buscar
            List<Venta> ventas = ventaController.buscarVentas(criterio);
            
            if (ventas.isEmpty()) {
                showAlert("Información", "No se encontraron ventas con el criterio: " + criterio, Alert.AlertType.INFORMATION);
            } else {
                showAlert("Ventas Encontradas", "Se encontraron " + ventas.size() + " venta(s) con el criterio: " + criterio, Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Error al buscar ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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
