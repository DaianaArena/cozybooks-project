-- ============================================
-- SISTEMA DE GESTIÓN DE VENTAS - COZY BOOKS
-- Paso 2: Crear Tablas
-- Base de Datos: MySQL 8.0
-- ============================================

USE cozy_books;

-- ============================================
-- TABLA: AUTOR
-- ============================================
CREATE TABLE AUTOR (
    id_autor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    nacionalidad VARCHAR(50),
    biografia TEXT,
    INDEX idx_autor_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT = 'Almacena información de los autores de libros';

-- ============================================
-- TABLA: CLIENTE
-- ============================================
CREATE TABLE CLIENTE (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(8) NOT NULL UNIQUE,
    email VARCHAR(100),
    telefono VARCHAR(20),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_cliente_nombre (nombre),
    INDEX idx_cliente_documento (documento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT = 'Almacena información de los clientes de la librería';

-- ============================================
-- TABLA: LIBRO
-- ============================================
CREATE TABLE LIBRO (
    id_libro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    editorial VARCHAR(100) NOT NULL,
    año INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL CHECK (precio > 0),
    genero VARCHAR(50),
    tipo_libro VARCHAR(20) NOT NULL CHECK (tipo_libro IN ('FISICO', 'DIGITAL', 'AUDIOLIBRO')),
    stock INT DEFAULT 0 CHECK (stock >= 0),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Campos para LIBRO FISICO
    encuadernado VARCHAR(50),
    num_edicion INT,
    
    -- Campos para LIBRO DIGITAL
    extension VARCHAR(10),
    permisos_impresion BOOLEAN,
    
    -- Campos para AUDIOLIBRO
    duracion INT,
    plataforma VARCHAR(50),
    narrador VARCHAR(100),
    
    -- Clave foránea
    id_autor INT NOT NULL,
    
    INDEX idx_libro_titulo (titulo),
    INDEX idx_libro_isbn (isbn),
    INDEX idx_libro_tipo (tipo_libro),
    INDEX idx_libro_autor (id_autor),
    
    CONSTRAINT fk_libro_autor 
        FOREIGN KEY (id_autor) 
        REFERENCES AUTOR(id_autor)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT = 'Tabla única que almacena todos los tipos de libros (físicos, digitales y audiolibros)';

-- ============================================
-- TABLA: VENTA
-- ============================================
CREATE TABLE VENTA (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(10,2) NOT NULL CHECK (monto >= 0),
    metodo_pago VARCHAR(50) CHECK (metodo_pago IN ('EFECTIVO', 'TARJETA', 'TRANSFERENCIA')),
    estado VARCHAR(20) DEFAULT 'COMPLETADA' CHECK (estado IN ('COMPLETADA', 'PENDIENTE', 'CANCELADA')),
    
    -- Clave foránea
    id_cliente INT NOT NULL,
    
    INDEX idx_venta_fecha (fecha),
    INDEX idx_venta_cliente (id_cliente),
    INDEX idx_venta_estado (estado),
    
    CONSTRAINT fk_venta_cliente 
        FOREIGN KEY (id_cliente) 
        REFERENCES CLIENTE(id_cliente)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT = 'Registra las transacciones de venta realizadas';

-- ============================================
-- TABLA: DETALLE_VENTA
-- ============================================
CREATE TABLE DETALLE_VENTA (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario > 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    
    -- Claves foráneas
    id_venta INT NOT NULL,
    id_libro INT NOT NULL,
    
    INDEX idx_detalle_venta (id_venta),
    INDEX idx_detalle_libro (id_libro),
    
    CONSTRAINT fk_detalle_venta 
        FOREIGN KEY (id_venta) 
        REFERENCES VENTA(id_venta)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_detalle_libro 
        FOREIGN KEY (id_libro) 
        REFERENCES LIBRO(id_libro)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT = 'Tabla intermedia que relaciona ventas con libros (N:M) y guarda precio histórico';

