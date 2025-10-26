package com.cozybooks.view;

import com.cozybooks.controller.VentaController;
import com.cozybooks.controller.ClienteController;
import com.cozybooks.model.*;
import com.cozybooks.repository.LibroRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista para la gestión de Ventas
 */
public class VentaView {
    
    private VentaController ventaController;
    private ClienteController clienteController;
    private LibroRepository libroRepository;
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
        this.clienteController = new ClienteController();
        this.libroRepository = new LibroRepository();
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
            String criterio = clienteIdField.getText().trim();
            if (criterio.isEmpty()) {
                showAlert("Error", "Debe ingresar un criterio de búsqueda", Alert.AlertType.ERROR);
                return;
            }
            
            // Llamar al controlador para buscar cliente
            Cliente cliente = clienteController.buscarCliente(criterio);
            
            if (cliente != null) {
                // Mostrar mensaje como en consola
                showAlert("Cliente Encontrado", 
                    "Cliente seleccionado: " + cliente.getNombre(), 
                    Alert.AlertType.INFORMATION);
                
                showClienteEncontrado(cliente);
            } else {
                showAlert("Información", "No se encontró ningún cliente con el criterio: " + criterio, Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Error al buscar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void showClienteEncontrado(Cliente cliente) {
        // Crear ventana de cliente encontrado
        Stage clienteStage = new Stage();
        clienteStage.setTitle("Cliente Encontrado");
        clienteStage.setWidth(500);
        clienteStage.setHeight(400);
        
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Información del cliente
        VBox clienteInfo = new VBox(15);
        clienteInfo.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text clienteTitle = new Text("Cliente Encontrado");
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
        
        infoContainer.getChildren().addAll(idText, nombreText, documentoText, emailText, telefonoText);
        
        // Botones de acción
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        Button usarButton = new Button("✅ Usar este Cliente");
        usarButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        usarButton.setOnAction(e -> {
            clienteIdField.setText(String.valueOf(cliente.getIdCliente()));
            clienteStage.close();
        });
        
        Button closeButton = new Button("❌ Cerrar");
        closeButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        closeButton.setOnAction(e -> clienteStage.close());
        
        buttonContainer.getChildren().addAll(usarButton, closeButton);
        
        clienteInfo.getChildren().addAll(clienteTitle, infoContainer, buttonContainer);
        mainContainer.getChildren().add(clienteInfo);
        
        Scene scene = new Scene(mainContainer);
        clienteStage.setScene(scene);
        clienteStage.show();
    }
    
    private void agregarLibro() {
        try {
            int idLibro = Integer.parseInt(libroIdField.getText().trim());
            int cantidad = Integer.parseInt(cantidadField.getText().trim());
            
            if (cantidad <= 0) {
                showAlert("Error", "La cantidad debe ser mayor a 0", Alert.AlertType.ERROR);
                return;
            }
            
            // Obtener libro de la base de datos usando el repositorio
            Libro libro = libroRepository.obtenerPorId(idLibro);
            if (libro == null) {
                showAlert("Error", "No se encontró el libro con ID: " + idLibro, Alert.AlertType.ERROR);
                return;
            }
            
            // Validar stock para libros físicos
            if (libro.getTipoLibro() == Libro.TipoLibro.FISICO && libro.getStock() < cantidad) {
                showAlert("Error", "Stock insuficiente. Disponible: " + libro.getStock(), Alert.AlertType.ERROR);
                return;
            }
            
            // Crear detalle de venta con el precio real del libro
            DetalleVenta detalle = new DetalleVenta(cantidad, libro.getPrecio(), 0, idLibro);
            detalleData.add(detalle);
            librosVenta.add(libro);
            
            // Mostrar mensaje informativo como en consola
            showAlert("Libro Agregado", 
                "Libro agregado: " + libro.getTitulo() + " x" + cantidad + " = $" + detalle.getSubtotal(), 
                Alert.AlertType.INFORMATION);
            
            // Limpiar campos
            libroIdField.clear();
            cantidadField.clear();
            
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
                    // Mostrar resumen de la venta como en consola
                    mostrarResumenVenta(idCliente, metodo);
                    
                    // Llamar al controlador para registrar la venta
                    Venta venta = ventaController.registrarVenta(idCliente, new ArrayList<>(detalleData), metodo);
                    
                    showAlert("Venta Registrada", 
                        "Venta registrada exitosamente con ID: " + venta.getIdVenta() + "\n" +
                        "Total: $" + venta.getMonto(), 
                        Alert.AlertType.INFORMATION);
                    
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
    
    private void mostrarResumenVenta(int idCliente, Venta.MetodoPago metodoPago) {
        // Crear ventana de resumen de venta
        Stage resumenStage = new Stage();
        resumenStage.setTitle("Resumen de Venta");
        resumenStage.setWidth(600);
        resumenStage.setHeight(500);
        
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Título
        VBox titleContainer = new VBox(10);
        titleContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text titleText = new Text("Resumen de la Venta");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setStyle("-fx-fill: #2c3e50;");
        
        titleContainer.getChildren().add(titleText);
        
        // Información del cliente
        VBox clienteContainer = new VBox(10);
        clienteContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text clienteTitle = new Text("Información del Cliente");
        clienteTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        clienteTitle.setStyle("-fx-fill: #2c3e50;");
        
        Text clienteIdText = new Text("ID Cliente: " + idCliente);
        clienteIdText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        clienteContainer.getChildren().addAll(clienteTitle, clienteIdText);
        
        // Detalles de la venta
        VBox detallesContainer = new VBox(10);
        detallesContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text detallesTitle = new Text("Detalles de la Venta");
        detallesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        detallesTitle.setStyle("-fx-fill: #2c3e50;");
        
        VBox detallesList = new VBox(5);
        BigDecimal total = BigDecimal.ZERO;
        
        for (int i = 0; i < detalleData.size(); i++) {
            DetalleVenta detalle = detalleData.get(i);
            Libro libro = librosVenta.get(i);
            
            Text detalleText = new Text("- " + libro.getTitulo() + " x" + detalle.getCantidad() + " = $" + detalle.getSubtotal());
            detalleText.setFont(Font.font("Arial", 12));
            detallesList.getChildren().add(detalleText);
            
            total = total.add(detalle.getSubtotal());
        }
        
        Text totalText = new Text("Total: $" + total);
        totalText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalText.setStyle("-fx-fill: #27ae60;");
        
        Text metodoText = new Text("Método de pago: " + metodoPago);
        metodoText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        metodoText.setStyle("-fx-fill: #3498db;");
        
        detallesContainer.getChildren().addAll(detallesTitle, detallesList, totalText, metodoText);
        
        // Botón cerrar
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        
        Button closeButton = new Button("❌ Cerrar");
        closeButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        closeButton.setOnAction(e -> resumenStage.close());
        
        buttonContainer.getChildren().add(closeButton);
        
        mainContainer.getChildren().addAll(titleContainer, clienteContainer, detallesContainer, buttonContainer);
        
        Scene scene = new Scene(mainContainer);
        resumenStage.setScene(scene);
        resumenStage.show();
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
        // Mostrar información de la venta como en consola
        String ventaInfo = "Venta encontrada:\n" +
            "Cliente ID: " + venta.getIdCliente() + "\n" +
            "Monto: $" + venta.getMonto() + "\n" +
            "Fecha: " + venta.getFecha().toLocalDate().toString();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de que desea eliminar esta venta?");
        alert.setContentText(ventaInfo + "\n\nEsta acción no se puede deshacer.");
        
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
            ventaController.generarTicket(idVenta);
            
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
                showVentasEncontradas(ventas, criterio);
            }
        } catch (Exception e) {
            showAlert("Error", "Error al buscar ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void showVentasEncontradas(List<Venta> ventas, String criterio) {
        // Crear ventana de resultados de búsqueda
        Stage resultadosStage = new Stage();
        resultadosStage.setTitle("Resultados de Búsqueda de Ventas");
        resultadosStage.setWidth(1000);
        resultadosStage.setHeight(700);
        
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");
        
        // Título y resumen
        VBox headerContainer = new VBox(10);
        headerContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Text titleText = new Text("Resultados de Búsqueda de Ventas");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setStyle("-fx-fill: #2c3e50;");
        
        Text criterioText = new Text("Criterio: " + criterio);
        criterioText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        criterioText.setStyle("-fx-fill: #7f8c8d;");
        
        Text cantidadText = new Text("Ventas encontradas: " + ventas.size());
        cantidadText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cantidadText.setStyle("-fx-fill: #27ae60;");
        
        headerContainer.getChildren().addAll(titleText, criterioText, cantidadText);
        
        // Tabla de ventas encontradas
        VBox tableContainer = new VBox(10);
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        TableView<Venta> resultadosTable = new TableView<>();
        ObservableList<Venta> resultadosData = FXCollections.observableArrayList(ventas);
        resultadosTable.setItems(resultadosData);
        
        // Columnas de la tabla
        TableColumn<Venta, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        idColumn.setPrefWidth(60);
        
        TableColumn<Venta, Integer> clienteColumn = new TableColumn<>("ID Cliente");
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        clienteColumn.setPrefWidth(100);
        
        TableColumn<Venta, LocalDateTime> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        fechaColumn.setPrefWidth(120);
        
        TableColumn<Venta, BigDecimal> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("monto"));
        totalColumn.setPrefWidth(100);
        
        TableColumn<Venta, Venta.MetodoPago> metodoColumn = new TableColumn<>("Método Pago");
        metodoColumn.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        metodoColumn.setPrefWidth(120);
        
        TableColumn<Venta, Venta.EstadoVenta> estadoColumn = new TableColumn<>("Estado");
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        estadoColumn.setPrefWidth(100);
        
        resultadosTable.getColumns().addAll(idColumn, clienteColumn, fechaColumn, totalColumn, metodoColumn, estadoColumn);
        
        // Resumen por método de pago
        VBox resumenContainer = new VBox(10);
        resumenContainer.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10; -fx-padding: 15;");
        
        Text resumenTitle = new Text("Resumen por Método de Pago");
        resumenTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resumenTitle.setStyle("-fx-fill: white;");
        
        long efectivo = ventas.stream().filter(v -> v.getMetodoPago() == Venta.MetodoPago.EFECTIVO).count();
        long tarjeta = ventas.stream().filter(v -> v.getMetodoPago() == Venta.MetodoPago.TARJETA).count();
        long transferencia = ventas.stream().filter(v -> v.getMetodoPago() == Venta.MetodoPago.TRANSFERENCIA).count();
        
        Text efectivoText = new Text("Efectivo: " + efectivo);
        efectivoText.setStyle("-fx-fill: white;");
        
        Text tarjetaText = new Text("Tarjeta: " + tarjeta);
        tarjetaText.setStyle("-fx-fill: white;");
        
        Text transferenciaText = new Text("Transferencia: " + transferencia);
        transferenciaText.setStyle("-fx-fill: white;");
        
        // Calcular total de ventas
        BigDecimal totalVentas = ventas.stream()
            .map(Venta::getMonto)
            .filter(monto -> monto != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Text totalVentasText = new Text("Total de ventas: $" + totalVentas);
        totalVentasText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        totalVentasText.setStyle("-fx-fill: white;");
        
        resumenContainer.getChildren().addAll(resumenTitle, efectivoText, tarjetaText, transferenciaText, totalVentasText);
        
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
