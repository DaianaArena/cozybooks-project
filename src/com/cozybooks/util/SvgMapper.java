package com.cozybooks.util;

/**
 * Mapeador de iconos SVG para la aplicación CozyBooks
 * Contiene constantes con los SVG paths para reutilización
 */
public class SvgMapper {
    
    // Icono de configuración (cog/gear) - SVG: lucide-cog
    public static final String CONFIG_ICON = "⚙";
    
    // Icono de rayo/bolt
    public static final String BOLT_ICON = "⚡";
    
    // Icono de notificaciones
    public static final String NOTIFICATION_ICON = "🔔";
    
    // Icono de usuario
    public static final String USER_ICON = "👤";
    
    // Iconos de libros
    public static final String BOOK_ICON = "📚";
    public static final String NEW_BOOK_ICON = "➕";
    
    // Iconos de acciones
    public static final String EDIT_ICON = "✏️";
    public static final String DELETE_ICON = "🗑️";
    public static final String SEARCH_ICON = "🔍";
    
    // Iconos de tipos de libro
    public static final String PHYSICAL_BOOK_ICON = "📖";
    public static final String DIGITAL_BOOK_ICON = "💻";
    public static final String AUDIO_BOOK_ICON = "🎧";
    
    // Iconos de estadísticas
    public static final String SALES_ICON = "🛒";
    public static final String CLIENTS_ICON = "👥";
    public static final String AUTHORS_ICON = "👤";
    
    // Iconos de navegación
    public static final String DASHBOARD_ICON = "🏠";
    public static final String REPORTS_ICON = "📊";
    
    // Constructor privado para evitar instanciación
    private SvgMapper() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada");
    }
}
