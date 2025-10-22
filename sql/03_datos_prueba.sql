-- ============================================
-- SISTEMA DE GESTIÓN DE VENTAS - COZY BOOKS
-- Paso 3: Insertar Datos de Prueba
-- Base de Datos: MySQL 8.0
-- ============================================

USE cozy_books;

-- ============================================
-- INSERTAR AUTORES
-- ============================================
INSERT INTO AUTOR (nombre, fecha_nacimiento, nacionalidad, biografia) VALUES
('Gabriel García Márquez', '1927-03-06', 'Colombia', 'Premio Nobel de Literatura 1982'),
('Isabel Allende', '1942-08-02', 'Chile', 'Escritora chilena de renombre internacional'),
('Jorge Luis Borges', '1899-08-24', 'Argentina', 'Uno de los autores más destacados de la literatura del siglo XX'),
('Julio Cortázar', '1914-08-26', 'Argentina', 'Escritor, traductor e intelectual argentino'),
('Mario Vargas Llosa', '1936-03-28', 'Perú', 'Premio Nobel de Literatura 2010');

-- ============================================
-- INSERTAR CLIENTES
-- ============================================
INSERT INTO CLIENTE (nombre, documento, email, telefono) VALUES
('Juan Pérez', '12345678', 'juan.perez@email.com', '011-1234-5678'),
('María González', '23456789', 'maria.gonzalez@email.com', '011-2345-6789'),
('Carlos Rodríguez', '34567890', 'carlos.rodriguez@email.com', '011-3456-7890'),
('Ana Martínez', '45678901', 'ana.martinez@email.com', '011-4567-8901'),
('Luis Fernández', '56789012', 'luis.fernandez@email.com', '011-5678-9012');

-- ============================================
-- INSERTAR LIBROS FÍSICOS
-- ============================================
INSERT INTO LIBRO (titulo, isbn, editorial, año, precio, genero, tipo_libro, stock, id_autor, encuadernado, num_edicion) VALUES
('Cien Años de Soledad', '978-0307474728', 'Sudamericana', 1967, 15500.00, 'Realismo Mágico', 'FISICO', 25, 1, 'Tapa dura', 1),
('El Amor en los Tiempos del Cólera', '978-0307387738', 'Sudamericana', 1985, 14200.00, 'Romance', 'FISICO', 15, 1, 'Tapa blanda', 1),
('Rayuela', '978-8420471891', 'Alfaguara', 1963, 13800.00, 'Novela', 'FISICO', 20, 4, 'Tapa blanda', 2),
('Ficciones', '978-0307950925', 'Debolsillo', 1944, 11500.00, 'Cuentos', 'FISICO', 30, 3, 'Tapa blanda', 1);

-- ============================================
-- INSERTAR LIBROS DIGITALES
-- ============================================
INSERT INTO LIBRO (titulo, isbn, editorial, año, precio, genero, tipo_libro, stock, id_autor, extension, permisos_impresion) VALUES
('La Casa de los Espíritus (Digital)', '978-1501117015', 'Plaza & Janés', 1982, 8900.00, 'Ficción', 'DIGITAL', 0, 2, 'PDF', true),
('La Ciudad y los Perros (Digital)', '978-8420471518', 'Alfaguara', 1963, 9200.00, 'Novela', 'DIGITAL', 0, 5, 'EPUB', true),
('El Coronel No Tiene Quien Le Escriba (Digital)', '978-0307475466', 'Sudamericana', 1961, 7500.00, 'Novela corta', 'DIGITAL', 0, 1, 'PDF', false);

-- ============================================
-- INSERTAR AUDIOLIBROS
-- ============================================
INSERT INTO LIBRO (titulo, isbn, editorial, año, precio, genero, tipo_libro, stock, id_autor, duracion, plataforma, narrador) VALUES
('El Aleph (Audiolibro)', '978-0525564461', 'Debolsillo', 1949, 9500.00, 'Cuentos', 'AUDIOLIBRO', 0, 3, 720, 'Audible', 'Héctor Bonilla'),
('Paula (Audiolibro)', '978-0061122859', 'Plaza & Janés', 1994, 12000.00, 'Autobiografía', 'AUDIOLIBRO', 0, 2, 840, 'Audible', 'Isabel Allende'),
('Bestiario (Audiolibro)', '978-8420471778', 'Alfaguara', 1951, 8800.00, 'Cuentos', 'AUDIOLIBRO', 0, 4, 600, 'Spotify Audiobooks', 'Daniel Hendler');

-- ============================================
-- VERIFICAR INSERCIONES
-- ============================================
SELECT 'AUTORES INSERTADOS:' AS '';
SELECT * FROM AUTOR;

SELECT 'CLIENTES INSERTADOS:' AS '';
SELECT * FROM CLIENTE;

SELECT 'LIBROS INSERTADOS:' AS '';
SELECT id_libro, titulo, tipo_libro, precio, stock, id_autor FROM LIBRO;

SELECT 'RESUMEN POR TIPO:' AS '';
SELECT tipo_libro, COUNT(*) as cantidad, AVG(precio) as precio_promedio 
FROM LIBRO 
GROUP BY tipo_libro;

