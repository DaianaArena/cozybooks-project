-- ============================================
-- SISTEMA DE GESTIÓN DE VENTAS - COZY BOOKS
-- Paso 4: Crear Usuario para la Aplicación
-- Base de Datos: MySQL 8.0
-- ============================================

-- IMPORTANTE: Ajusta la contraseña según tus necesidades de seguridad

-- Crear usuario para la aplicación
CREATE USER IF NOT EXISTS 'cozybooks_app'@'localhost' IDENTIFIED BY 'CozyBooks2024!';

-- Otorgar permisos solo en la base de datos cozy_books
GRANT SELECT, INSERT, UPDATE, DELETE ON cozy_books.* TO 'cozybooks_app'@'localhost';

-- Aplicar cambios
FLUSH PRIVILEGES;

-- Verificar permisos otorgados
SHOW GRANTS FOR 'cozybooks_app'@'localhost';

-- ============================================
-- CONFIGURACIÓN PARA JAVA
-- ============================================
-- Usa estos datos en tu clase DBConnection.java:
--
-- URL: jdbc:mysql://localhost:3306/cozy_books?useSSL=false&serverTimezone=UTC
-- USER: cozybooks_app
-- PASSWORD: CozyBooks2024!
-- ============================================

