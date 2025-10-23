# Cozy Books System - Proyecto Java Vanilla


## 📋 Requisitos

- **Java 8 o superior**
- **MySQL 8.0 o superior**
- **Driver de MySQL Connector/J**

## 🚀 Instalación y Configuración

### Configurar Base de Datos

Ejecute los scripts SQL en el orden de su numeración.


## 📁 Estructura del Proyecto

```
cozybooks-project/
├── src/
│   └── com/
│       └── cozybooks/
│           ├── model/          # Entidades del dominio
│           ├── repository/     # Acceso a datos
│           ├── controller/     # Lógica de negocio
│           ├── util/          # Utilidades
│           └── Main.java      # Punto de entrada
├── lib/                       # Librerías externas
├── bin/                       # Clases compiladas
├── tickets/                   # Tickets generados
├── sql/                       # Scripts de base de datos
├── documentacion/             # Documentación del proyecto
├── compilar.ps1              # Script de compilación
├── ejecutar.ps1              # Script de ejecución
└── README_JAVA.md            # Este archivo
```

## 🎯 Casos de Uso Implementados

### Gestión de Autores
- **CU01**: Registrar Autor
- **CU02**: Actualizar Autor  
- **CU03**: Eliminar Autor
- **CU04**: Listar Autores
- **CU05**: Buscar Autor
- **CU22**: Reporte de Libros por Autor

### Gestión de Clientes
- **CU06**: Registrar Cliente
- **CU07**: Actualizar Cliente
- **CU08**: Eliminar Cliente
- **CU09**: Listar Clientes
- **CU10**: Buscar Cliente

### Gestión de Libros
- **CU11**: Registrar Libro
- **CU12**: Actualizar Libro
- **CU13**: Eliminar Libro
- **CU14**: Listar Libros
- **CU15**: Buscar Libro

### Gestión de Ventas
- **CU16**: Registrar Venta
- **CU17**: Actualizar Venta
- **CU18**: Eliminar Venta
- **CU19**: Listar Ventas
- **CU20**: Buscar Venta
- **CU21**: Generar Ticket

## 🏗️ Arquitectura

El proyecto sigue la arquitectura **MVC (Model-View-Controller)**:

- **Model**: Clases que representan las entidades del dominio
- **Repository**: Capa de acceso a datos con JDBC
- **Controller**: Lógica de negocio y validaciones
- **Util**: Servicios auxiliares (conexión DB, archivos)

## 🔧 Configuración de Base de Datos

La aplicación se conecta a MySQL en local con las siguientes credenciales:

- **URL**: `jdbc:mysql://localhost:3306/cozy_books`
- **Usuario**: `root`
- **Contraseña**: `1234`

Nota: De ser necesario, pueden modificarse en el util DBConnection.java

## 📊 Tipos de Libros Soportados

1. **Libro Físico**
   - Requiere: encuadernado, número de edición, stock
   - Validación de stock en ventas

2. **Libro Digital**
   - Requiere: extensión de archivo, permisos de impresión
   - No requiere stock

3. **Audiolibro**
   - Requiere: duración, plataforma, narrador
   - No requiere stock

## 🎫 Generación de Tickets

Los tickets de venta se generan automáticamente en formato `.txt` en la carpeta `tickets/` con la siguiente información:

- Datos de la venta (ID, fecha, cliente)
- Detalles de cada libro vendido
- Total de la venta
- Método de pago

## ⚠️ Validaciones Implementadas

- **Documentos únicos**: DNI de 8 dígitos para clientes
- **ISBN únicos**: Para libros (opcional)
- **Precios positivos**: Todos los precios > 0
- **Stock válido**: Stock >= 0 para libros físicos
- **Referencias válidas**: Autor debe existir para libros
- **Transacciones**: Operaciones atómicas para ventas



## 📝 Notas de Desarrollo

- El proyecto respeta completamente la arquitectura definida en la documentación
- Todas las validaciones de negocio están implementadas
- Se manejan transacciones para operaciones complejas
