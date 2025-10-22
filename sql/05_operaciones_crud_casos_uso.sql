-- ============================================
-- SISTEMA DE GESTIÓN DE VENTAS - COZY BOOKS
-- Paso 6: Operaciones CRUD - Casos de Uso
-- Base de Datos: MySQL 8.0
-- ============================================
-- Este archivo está organizado por TIPO DE OPERACIÓN:
-- 1. INSERCIÓN (INSERT) - Registrar nuevos datos
-- 2. CONSULTA (SELECT) - Listar y buscar datos
-- 3. ACTUALIZACIÓN (UPDATE) - Modificar datos existentes
-- 4. BORRADO (DELETE) - Eliminar datos
-- ============================================

USE cozy_books;

-- ============================================
-- SECCIÓN 1: INSERCIÓN (INSERT)
-- Operaciones para registrar nuevos datos
-- ============================================

-- --------------------------------------------
-- CU01: REGISTRAR AUTOR
-- --------------------------------------------
-- Insertar un nuevo autor en el sistema con todos los datos
INSERT INTO AUTOR (nombre, fecha_nacimiento, nacionalidad, biografia) 
VALUES ('Gabriel García Márquez', '1927-03-06', 'Colombia', 'Premio Nobel de Literatura 1982');

-- Insertar autor con información mínima (solo campos obligatorios)
INSERT INTO AUTOR (nombre, fecha_nacimiento) 
VALUES ('Roberto Bolaño', '1953-04-28');

-- Verificar que el autor fue insertado
SELECT * FROM AUTOR WHERE nombre = 'Gabriel García Márquez';
SELECT * FROM AUTOR WHERE nombre = 'Roberto Bolaño';


-- --------------------------------------------
-- CU06: REGISTRAR CLIENTE
-- --------------------------------------------
-- Insertar un nuevo cliente con todos los datos
INSERT INTO CLIENTE (nombre, documento, email, telefono) 
VALUES ('Juan Pérez', '12345678', 'juan.perez@email.com', '011-1234-5678');

-- Insertar cliente con información mínima (solo campos obligatorios)
INSERT INTO CLIENTE (nombre, documento) 
VALUES ('María González', '23456789');

-- IMPORTANTE: Verificar que el documento sea único ANTES de insertar
SELECT COUNT(*) as existe 
FROM CLIENTE 
WHERE documento = '12345678';

-- Verificar que los clientes fueron insertados
SELECT * FROM CLIENTE WHERE documento = '12345678';
SELECT * FROM CLIENTE WHERE documento = '23456789';


-- --------------------------------------------
-- CU11: REGISTRAR LIBRO
-- --------------------------------------------
-- Registrar libro FÍSICO
INSERT INTO LIBRO (
    titulo, isbn, editorial, año, precio, genero, 
    tipo_libro, stock, id_autor, 
    encuadernado, num_edicion
) VALUES (
    'Cien Años de Soledad', 
    '978-0307474728', 
    'Sudamericana', 
    1967, 
    15500.00, 
    'Realismo Mágico',
    'FISICO', 
    25, 
    1,
    'Tapa dura', 
    1
);

-- Registrar libro DIGITAL
INSERT INTO LIBRO (
    titulo, isbn, editorial, año, precio, genero,
    tipo_libro, stock, id_autor,
    extension, permisos_impresion
) VALUES (
    'Clean Code',
    '978-0132350884',
    'Prentice Hall',
    2008,
    8900.00,
    'Programación',
    'DIGITAL',
    0,
    1,
    'PDF',
    true
);

-- Registrar AUDIOLIBRO
INSERT INTO LIBRO (
    titulo, isbn, editorial, año, precio, genero,
    tipo_libro, stock, id_autor,
    duracion, plataforma, narrador
) VALUES (
    'Sapiens: De animales a dioses',
    '978-6073149327',
    'Debate',
    2014,
    12500.00,
    'Historia',
    'AUDIOLIBRO',
    0,
    1,
    945,
    'Audible',
    'Carlos Manuel Vesga'
);

-- IMPORTANTE: Verificar que el ISBN sea único ANTES de insertar
SELECT COUNT(*) as existe 
FROM LIBRO 
WHERE isbn = '978-0307474728';

-- Verificar que los libros fueron insertados
SELECT * FROM LIBRO WHERE isbn = '978-0307474728';
SELECT * FROM LIBRO WHERE isbn = '978-0132350884';
SELECT * FROM LIBRO WHERE isbn = '978-6073149327';


-- --------------------------------------------
-- CU16: REGISTRAR VENTA (Proceso complejo)
-- --------------------------------------------
-- Este es un proceso que involucra múltiples tablas y requiere transacción

-- Paso 1: Verificar que el cliente existe
SELECT id_cliente, nombre 
FROM CLIENTE 
WHERE id_cliente = 1;

-- Paso 2: Verificar que los libros existen y hay stock (para físicos)
SELECT id_libro, titulo, tipo_libro, stock, precio
FROM LIBRO
WHERE id_libro IN (1, 2, 3);

-- Paso 3: Validar stock para libros físicos
SELECT 
    id_libro,
    titulo,
    stock,
    CASE 
        WHEN tipo_libro = 'FISICO' AND stock >= 2 THEN 'OK'
        WHEN tipo_libro IN ('DIGITAL', 'AUDIOLIBRO') THEN 'OK'
        ELSE 'Stock insuficiente'
    END as validacion
FROM LIBRO
WHERE id_libro = 1;

-- Paso 4: Insertar la venta (CON TRANSACCIÓN)
START TRANSACTION;

-- Insertar registro de venta
INSERT INTO VENTA (fecha, monto, metodo_pago, estado, id_cliente)
VALUES (NOW(), 52400.00, 'TARJETA', 'COMPLETADA', 1);

-- Obtener el ID de la venta recién creada
SET @id_venta = LAST_INSERT_ID();

-- Paso 5: Insertar detalles de venta
INSERT INTO DETALLE_VENTA (cantidad, precio_unitario, subtotal, id_venta, id_libro)
VALUES 
    (2, 15500.00, 31000.00, @id_venta, 1),  -- Libro físico
    (1, 8900.00, 8900.00, @id_venta, 2),    -- Libro digital
    (1, 12500.00, 12500.00, @id_venta, 3);  -- Audiolibro

-- Paso 6: Actualizar stock (solo para libros físicos)
UPDATE LIBRO 
SET stock = stock - 2
WHERE id_libro = 1 AND tipo_libro = 'FISICO';

-- Confirmar transacción
COMMIT;

-- Verificar la venta creada
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    v.monto,
    v.metodo_pago,
    v.estado
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
WHERE v.id_venta = @id_venta;

-- Verificar detalles de la venta
SELECT 
    dv.id_detalle,
    l.titulo as libro,
    dv.cantidad,
    dv.precio_unitario,
    dv.subtotal
FROM DETALLE_VENTA dv
INNER JOIN LIBRO l ON dv.id_libro = l.id_libro
WHERE dv.id_venta = @id_venta;


-- ============================================
-- SECCIÓN 2: CONSULTA (SELECT)
-- Operaciones para listar y buscar datos
-- ============================================

-- --------------------------------------------
-- CU04: LISTAR AUTORES
-- --------------------------------------------
-- Listar todos los autores
SELECT * FROM AUTOR 
ORDER BY nombre;

-- Listar autores con cantidad de libros
SELECT 
    a.id_autor,
    a.nombre,
    a.nacionalidad,
    COUNT(l.id_libro) as cantidad_libros
FROM AUTOR a
LEFT JOIN LIBRO l ON a.id_autor = l.id_autor
GROUP BY a.id_autor, a.nombre, a.nacionalidad
ORDER BY cantidad_libros DESC, a.nombre;


-- --------------------------------------------
-- CU05: BUSCAR AUTOR
-- --------------------------------------------
-- Buscar autor por nombre (búsqueda parcial)
SELECT * FROM AUTOR 
WHERE nombre LIKE '%García%'
ORDER BY nombre;

-- Buscar autor por ID
SELECT * FROM AUTOR 
WHERE id_autor = 1;

-- Buscar autor por nacionalidad
SELECT * FROM AUTOR 
WHERE nacionalidad = 'Colombia'
ORDER BY nombre;


-- --------------------------------------------
-- CU09: LISTAR CLIENTES
-- --------------------------------------------
-- Listar todos los clientes
SELECT * FROM CLIENTE 
ORDER BY nombre;

-- Listar clientes con su historial de compras
SELECT 
    c.id_cliente,
    c.nombre,
    c.documento,
    c.email,
    COUNT(v.id_venta) as total_compras,
    COALESCE(SUM(v.monto), 0) as monto_total_gastado
FROM CLIENTE c
LEFT JOIN VENTA v ON c.id_cliente = v.id_cliente
GROUP BY c.id_cliente, c.nombre, c.documento, c.email
ORDER BY monto_total_gastado DESC;


-- --------------------------------------------
-- CU10: BUSCAR CLIENTE
-- --------------------------------------------
-- Buscar cliente por nombre (búsqueda parcial)
SELECT * FROM CLIENTE 
WHERE nombre LIKE '%Pérez%'
ORDER BY nombre;

-- Buscar cliente por documento (DNI)
SELECT * FROM CLIENTE 
WHERE documento = '12345678';

-- Buscar cliente por ID
SELECT * FROM CLIENTE 
WHERE id_cliente = 1;

-- Buscar cliente por email
SELECT * FROM CLIENTE 
WHERE email LIKE '%@email.com'
ORDER BY nombre;


-- --------------------------------------------
-- CU14: LISTAR LIBROS
-- --------------------------------------------
-- Listar todos los libros
SELECT * FROM LIBRO 
ORDER BY tipo_libro, titulo;

-- Listar libros con información del autor
SELECT 
    l.id_libro,
    l.titulo,
    a.nombre as autor,
    l.editorial,
    l.año,
    l.precio,
    l.tipo_libro,
    l.stock
FROM LIBRO l
INNER JOIN AUTOR a ON l.id_autor = a.id_autor
ORDER BY l.tipo_libro, l.titulo;

-- Listar libros agrupados por tipo
SELECT 
    tipo_libro,
    COUNT(*) as cantidad,
    AVG(precio) as precio_promedio,
    SUM(CASE WHEN tipo_libro = 'FISICO' THEN stock ELSE 0 END) as stock_total
FROM LIBRO
GROUP BY tipo_libro;


-- --------------------------------------------
-- CU15: BUSCAR LIBRO
-- --------------------------------------------
-- Buscar libro por título (búsqueda parcial)
SELECT 
    l.*,
    a.nombre as nombre_autor
FROM LIBRO l
INNER JOIN AUTOR a ON l.id_autor = a.id_autor
WHERE l.titulo LIKE '%Soledad%'
ORDER BY l.titulo;

-- Buscar libro por ISBN
SELECT * FROM LIBRO 
WHERE isbn = '978-0307474728';

-- Buscar libro por género
SELECT * FROM LIBRO 
WHERE genero LIKE '%Realismo%'
ORDER BY titulo;

-- Buscar libro por tipo
SELECT * FROM LIBRO 
WHERE tipo_libro = 'FISICO'
ORDER BY titulo;

-- Buscar libros con stock bajo (menos de 10 unidades)
SELECT 
    l.titulo,
    l.stock,
    a.nombre as autor,
    l.precio
FROM LIBRO l
INNER JOIN AUTOR a ON l.id_autor = a.id_autor
WHERE l.tipo_libro = 'FISICO' AND l.stock < 10
ORDER BY l.stock ASC;


-- --------------------------------------------
-- CU19: LISTAR VENTAS
-- --------------------------------------------
-- Listar todas las ventas
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    v.monto,
    v.metodo_pago,
    v.estado
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
ORDER BY v.fecha DESC;

-- Listar ventas con detalles completos
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    c.documento,
    l.titulo as libro,
    dv.cantidad,
    dv.precio_unitario,
    dv.subtotal,
    v.monto as total_venta,
    v.metodo_pago,
    v.estado
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
INNER JOIN DETALLE_VENTA dv ON v.id_venta = dv.id_venta
INNER JOIN LIBRO l ON dv.id_libro = l.id_libro
ORDER BY v.fecha DESC, v.id_venta, dv.id_detalle;

-- Resumen de ventas por día
SELECT 
    DATE(fecha) as dia,
    COUNT(*) as cantidad_ventas,
    SUM(monto) as total_vendido
FROM VENTA
WHERE estado = 'COMPLETADA'
GROUP BY DATE(fecha)
ORDER BY dia DESC;


-- --------------------------------------------
-- CU20: BUSCAR VENTA
-- --------------------------------------------
-- Buscar ventas por cliente (nombre parcial)
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    v.monto,
    v.metodo_pago,
    v.estado
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
WHERE c.nombre LIKE '%Pérez%'
ORDER BY v.fecha DESC;

-- Buscar venta por ID
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    c.documento,
    c.email,
    v.monto,
    v.metodo_pago,
    v.estado
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
WHERE v.id_venta = 1;

-- Buscar ventas por rango de fechas
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    v.monto,
    v.estado
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
WHERE v.fecha BETWEEN '2024-01-01' AND '2024-12-31'
ORDER BY v.fecha DESC;

-- Buscar ventas por monto (mayor a cierto valor)
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    v.monto,
    v.metodo_pago
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
WHERE v.monto > 50000.00
ORDER BY v.monto DESC;

-- Buscar ventas por método de pago
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    v.monto,
    v.metodo_pago,
    v.estado
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
WHERE v.metodo_pago = 'EFECTIVO'
ORDER BY v.fecha DESC;

-- Buscar ventas por estado
SELECT 
    v.id_venta,
    v.fecha,
    c.nombre as cliente,
    v.monto,
    v.estado
FROM VENTA v
INNER JOIN CLIENTE c ON v.id_cliente = c.id_cliente
WHERE v.estado = 'COMPLETADA'
ORDER BY v.fecha DESC;


-- --------------------------------------------
-- CU22: REPORTE DE LIBROS POR AUTOR
-- --------------------------------------------
-- Consultar todos los libros de un autor específico con detalles
SELECT 
    l.id_libro,
    l.titulo,
    l.editorial,
    l.año,
    l.precio,
    l.tipo_libro,
    l.stock,
    l.genero,
    -- Campos específicos según tipo
    CASE 
        WHEN l.tipo_libro = 'FISICO' THEN CONCAT('Encuadernado: ', l.encuadernado, ', Edición: ', l.num_edicion)
        WHEN l.tipo_libro = 'DIGITAL' THEN CONCAT('Formato: ', l.extension, ', Impresión: ', IF(l.permisos_impresion, 'Sí', 'No'))
        WHEN l.tipo_libro = 'AUDIOLIBRO' THEN CONCAT('Duración: ', l.duracion, ' min, Plataforma: ', l.plataforma, ', Narrador: ', l.narrador)
    END as detalles_especificos
FROM LIBRO l
WHERE l.id_autor = 1
ORDER BY l.tipo_libro, l.titulo;

-- Reporte resumen por autor
SELECT 
    a.nombre as autor,
    COUNT(l.id_libro) as total_libros,
    SUM(CASE WHEN l.tipo_libro = 'FISICO' THEN 1 ELSE 0 END) as fisicos,
    SUM(CASE WHEN l.tipo_libro = 'DIGITAL' THEN 1 ELSE 0 END) as digitales,
    SUM(CASE WHEN l.tipo_libro = 'AUDIOLIBRO' THEN 1 ELSE 0 END) as audiolibros,
    AVG(l.precio) as precio_promedio
FROM AUTOR a
LEFT JOIN LIBRO l ON a.id_autor = l.id_autor
WHERE a.id_autor = 1
GROUP BY a.id_autor, a.nombre;


-- --------------------------------------------
-- CONSULTAS ADICIONALES ÚTILES
-- --------------------------------------------

-- Obtener historial completo de un cliente (CU09 con detalle)
SELECT 
    c.nombre as cliente,
    c.documento,
    v.id_venta,
    v.fecha,
    l.titulo as libro,
    dv.cantidad,
    dv.precio_unitario,
    dv.subtotal,
    v.monto as total_venta,
    v.metodo_pago
FROM CLIENTE c
INNER JOIN VENTA v ON c.id_cliente = v.id_cliente
INNER JOIN DETALLE_VENTA dv ON v.id_venta = dv.id_venta
INNER JOIN LIBRO l ON dv.id_libro = l.id_libro
WHERE c.id_cliente = 1
ORDER BY v.fecha DESC, dv.id_detalle;

-- Calcular total de ventas (para reportes)
SELECT 
    COUNT(*) as total_ventas,
    SUM(monto) as ganancia_total,
    AVG(monto) as venta_promedio
FROM VENTA
WHERE estado = 'COMPLETADA';

-- Libros más vendidos
SELECT 
    l.id_libro,
    l.titulo,
    a.nombre as autor,
    l.tipo_libro,
    SUM(dv.cantidad) as unidades_vendidas,
    SUM(dv.subtotal) as ingresos_generados
FROM LIBRO l
INNER JOIN AUTOR a ON l.id_autor = a.id_autor
LEFT JOIN DETALLE_VENTA dv ON l.id_libro = dv.id_libro
GROUP BY l.id_libro, l.titulo, a.nombre, l.tipo_libro
HAVING unidades_vendidas > 0
ORDER BY unidades_vendidas DESC
LIMIT 10;

-- Clientes más activos (mayor cantidad de compras)
SELECT 
    c.id_cliente,
    c.nombre,
    c.documento,
    COUNT(v.id_venta) as total_compras,
    SUM(v.monto) as total_gastado,
    AVG(v.monto) as promedio_por_compra
FROM CLIENTE c
LEFT JOIN VENTA v ON c.id_cliente = v.id_cliente
GROUP BY c.id_cliente, c.nombre, c.documento
HAVING total_compras > 0
ORDER BY total_gastado DESC
LIMIT 10;

-- Stock crítico (libros físicos con menos de 5 unidades)
SELECT 
    l.id_libro,
    l.titulo,
    a.nombre as autor,
    l.stock,
    l.precio
FROM LIBRO l
INNER JOIN AUTOR a ON l.id_autor = a.id_autor
WHERE l.tipo_libro = 'FISICO' 
  AND l.stock < 5
ORDER BY l.stock ASC;


-- ============================================
-- SECCIÓN 3: ACTUALIZACIÓN (UPDATE)
-- Operaciones para modificar datos existentes
-- ============================================

-- --------------------------------------------
-- CU02: ACTUALIZAR AUTOR
-- --------------------------------------------
-- Actualizar información completa de un autor
UPDATE AUTOR 
SET nombre = 'Gabriel José de la Concordia García Márquez',
    nacionalidad = 'Colombia',
    biografia = 'Escritor, periodista y premio Nobel de Literatura 1982. Máximo exponente del realismo mágico.'
WHERE id_autor = 1;

-- Actualizar solo algunos campos
UPDATE AUTOR 
SET biografia = 'Escritor y poeta chileno, considerado uno de los más importantes de la literatura en español.'
WHERE nombre = 'Roberto Bolaño';

-- Verificar actualización
SELECT * FROM AUTOR WHERE id_autor = 1;
SELECT * FROM AUTOR WHERE nombre = 'Roberto Bolaño';


-- --------------------------------------------
-- CU07: ACTUALIZAR CLIENTE
-- --------------------------------------------
-- Actualizar información completa del cliente
UPDATE CLIENTE 
SET nombre = 'Juan Carlos Pérez',
    email = 'juancarlos.perez@email.com',
    telefono = '011-9999-8888'
WHERE id_cliente = 1;

-- Actualizar solo email
UPDATE CLIENTE 
SET email = 'maria.gonzalez@newemail.com'
WHERE documento = '23456789';

-- Verificar actualización
SELECT * FROM CLIENTE WHERE id_cliente = 1;
SELECT * FROM CLIENTE WHERE documento = '23456789';


-- --------------------------------------------
-- CU12: ACTUALIZAR LIBRO
-- --------------------------------------------
-- Actualizar información general del libro
UPDATE LIBRO 
SET precio = 16000.00,
    stock = 30
WHERE id_libro = 1;

-- Actualizar campos específicos de libro físico
UPDATE LIBRO 
SET encuadernado = 'Tapa blanda',
    num_edicion = 2
WHERE id_libro = 1 AND tipo_libro = 'FISICO';

-- Actualizar precio de todos los libros de un autor (aumento del 10%)
UPDATE LIBRO 
SET precio = precio * 1.10
WHERE id_autor = 1;

-- Verificar actualización
SELECT * FROM LIBRO WHERE id_libro = 1;
-- Verificar actualización de precio para libros del autor
SELECT id_libro, titulo, precio FROM LIBRO WHERE id_autor = 1;


-- --------------------------------------------
-- CU17: ACTUALIZAR VENTA
-- --------------------------------------------
-- Actualizar estado de la venta
UPDATE VENTA 
SET estado = 'CANCELADA'
WHERE id_venta = 1;

-- Actualizar método de pago
UPDATE VENTA 
SET metodo_pago = 'EFECTIVO'
WHERE id_venta = 1;

-- Verificar actualización simple
SELECT * FROM VENTA WHERE id_venta = 1;

-- Ejemplo COMPLETO: Actualizar detalles de venta (proceso complejo)
-- Supongamos que queremos cambiar la cantidad del libro 1 en la venta 1 de 2 a 3 unidades
START TRANSACTION;

-- 1. Obtener datos actuales del detalle
SELECT @cantidad_actual := cantidad, @id_libro_actual := id_libro
FROM DETALLE_VENTA
WHERE id_venta = 1 AND id_detalle = 1;

-- 2. Revertir stock del libro físico (sumar la cantidad que se había restado)
UPDATE LIBRO 
SET stock = stock + @cantidad_actual
WHERE id_libro = @id_libro_actual AND tipo_libro = 'FISICO';

-- 3. Actualizar el detalle con la nueva cantidad y recalcular subtotal
UPDATE DETALLE_VENTA dv
INNER JOIN LIBRO l ON dv.id_libro = l.id_libro
SET dv.cantidad = 3,
    dv.subtotal = 3 * dv.precio_unitario
WHERE dv.id_detalle = 1 AND dv.id_venta = 1;

-- 4. Descontar el nuevo stock
UPDATE LIBRO 
SET stock = stock - 3
WHERE id_libro = @id_libro_actual AND tipo_libro = 'FISICO';

-- 5. Recalcular el monto total de la venta
UPDATE VENTA v
SET v.monto = (
    SELECT SUM(dv.subtotal)
    FROM DETALLE_VENTA dv
    WHERE dv.id_venta = v.id_venta
)
WHERE v.id_venta = 1;

COMMIT;

-- Verificar actualización del detalle
SELECT * FROM DETALLE_VENTA WHERE id_venta = 1;
SELECT * FROM VENTA WHERE id_venta = 1;


-- ============================================
-- SECCIÓN 4: BORRADO (DELETE)
-- Operaciones para eliminar datos
-- ============================================

-- --------------------------------------------
-- CU03: ELIMINAR AUTOR
-- --------------------------------------------
-- IMPORTANTE: Primero verificar si tiene libros asociados
SELECT COUNT(*) as libros_asociados
FROM LIBRO 
WHERE id_autor = 1;

-- Eliminar un autor (solo si NO tiene libros asociados)
DELETE FROM AUTOR 
WHERE id_autor = 99;

-- Verificar eliminación
SELECT COUNT(*) as existe 
FROM AUTOR 
WHERE id_autor = 99;

-- Nota: Si el autor tiene libros, la operación fallará por la restricción FK


-- --------------------------------------------
-- CU08: ELIMINAR CLIENTE
-- --------------------------------------------
-- IMPORTANTE: Primero verificar si tiene ventas
SELECT COUNT(*) as ventas_registradas
FROM VENTA 
WHERE id_cliente = 1;

-- Eliminar un cliente (solo si NO tiene ventas registradas)
DELETE FROM CLIENTE 
WHERE id_cliente = 99;

-- Verificar eliminación
SELECT COUNT(*) as existe 
FROM CLIENTE 
WHERE id_cliente = 99;

-- Nota: Si el cliente tiene ventas, la operación fallará por la restricción FK


-- --------------------------------------------
-- CU13: ELIMINAR LIBRO
-- --------------------------------------------
-- IMPORTANTE: Primero verificar si está en ventas
SELECT COUNT(*) as veces_vendido
FROM DETALLE_VENTA 
WHERE id_libro = 1;

-- Eliminar un libro (solo si NO está en ninguna venta)
DELETE FROM LIBRO 
WHERE id_libro = 99;

-- Verificar eliminación
SELECT COUNT(*) as existe 
FROM LIBRO 
WHERE id_libro = 99;

-- Nota: Si el libro está en DETALLE_VENTA, la operación fallará por FK


-- --------------------------------------------
-- CU18: ELIMINAR VENTA
-- --------------------------------------------
-- Eliminar una venta con reversión de stock (CON TRANSACCIÓN)
START TRANSACTION;

-- 1. OPCIONAL: Revertir stock antes de eliminar (solo para físicos)
UPDATE LIBRO l
INNER JOIN DETALLE_VENTA dv ON l.id_libro = dv.id_libro
SET l.stock = l.stock + dv.cantidad
WHERE dv.id_venta = 99 
  AND l.tipo_libro = 'FISICO';

-- 2. Eliminar la venta (los detalles se eliminan automáticamente por CASCADE)
DELETE FROM VENTA 
WHERE id_venta = 99;

COMMIT;

-- Verificar eliminación
SELECT COUNT(*) as existe 
FROM VENTA 
WHERE id_venta = 99;

-- Verificar que los detalles también se eliminaron
SELECT COUNT(*) as existe 
FROM DETALLE_VENTA 
WHERE id_venta = 99;


-- ============================================
-- NOTAS IMPORTANTES
-- ============================================

/*

ORDEN DE OPERACIONES RECOMENDADO:
1. INSERCIÓN: Primero registrar autores, luego clientes, luego libros, finalmente ventas
2. CONSULTA: Puede hacerse en cualquier momento sin afectar datos
3. ACTUALIZACIÓN: Requiere que los datos existan previamente
4. BORRADO: Debe hacerse en orden inverso (ventas, libros, clientes, autores)

CONSIDERACIONES DE TRANSACCIONES:
- Siempre usar START TRANSACTION y COMMIT para operaciones que involucren
  múltiples tablas (especialmente en ventas).
- Usar ROLLBACK si algo falla durante la transacción.

VALIDACIONES IMPORTANTES ANTES DE INSERTAR:
- Verificar existencia de FK (autor, cliente) antes de insertar libros/ventas
- Validar stock antes de procesar ventas de libros físicos
- Verificar unicidad de ISBN (libros) y documento (clientes)

VALIDACIONES IMPORTANTES ANTES DE ELIMINAR:
- No permitir eliminaciones si existen referencias (FK constraints)
- Verificar dependencias antes de intentar eliminar
- Para ventas, considerar si se debe revertir el stock

ÍNDICES:
- Todos los campos comúnmente usados en búsquedas tienen índices definidos
- Los índices mejoran el rendimiento de SELECT, WHERE, JOIN y ORDER BY

PERMISOS:
- El usuario 'cozybooks_app' tiene permisos SELECT, INSERT, UPDATE, DELETE
- No tiene permisos para DROP o ALTER (seguridad)
*/
