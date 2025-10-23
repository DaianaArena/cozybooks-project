package com.cozybooks.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase modelo para la entidad Libro
 * Representa un libro que puede ser FÍSICO, DIGITAL o AUDIOLIBRO
 */
public class Libro {
    private int idLibro;
    private String titulo;
    private String isbn; // Único
    private String editorial;
    private int año;
    private BigDecimal precio;
    private String genero;
    private TipoLibro tipoLibro;
    private int stock;
    private LocalDateTime fechaRegistro;
    private int idAutor;

    // Campos específicos para LIBRO FÍSICO
    private String encuadernado;
    private Integer numEdicion;

    // Campos específicos para LIBRO DIGITAL
    private String extension;
    private Boolean permisosImpresion;

    // Campos específicos para AUDIOLIBRO
    private Integer duracion; // en minutos
    private String plataforma;
    private String narrador;

    // Enum para tipos de libro
    public enum TipoLibro {
        FISICO, DIGITAL, AUDIOLIBRO
    }

    // Constructores
    public Libro() {}

    public Libro(String titulo, String editorial, int año, BigDecimal precio, TipoLibro tipoLibro, int idAutor) {
        this.titulo = titulo;
        this.editorial = editorial;
        this.año = año;
        this.precio = precio;
        this.tipoLibro = tipoLibro;
        this.idAutor = idAutor;
    }

    // Getters y Setters
    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public TipoLibro getTipoLibro() {
        return tipoLibro;
    }

    public void setTipoLibro(TipoLibro tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    // Getters y Setters para campos específicos de FÍSICO
    public String getEncuadernado() {
        return encuadernado;
    }

    public void setEncuadernado(String encuadernado) {
        this.encuadernado = encuadernado;
    }

    public Integer getNumEdicion() {
        return numEdicion;
    }

    public void setNumEdicion(Integer numEdicion) {
        this.numEdicion = numEdicion;
    }

    // Getters y Setters para campos específicos de DIGITAL
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Boolean getPermisosImpresion() {
        return permisosImpresion;
    }

    public void setPermisosImpresion(Boolean permisosImpresion) {
        this.permisosImpresion = permisosImpresion;
    }

    // Getters y Setters para campos específicos de AUDIOLIBRO
    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getNarrador() {
        return narrador;
    }

    public void setNarrador(String narrador) {
        this.narrador = narrador;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "idLibro=" + idLibro +
                ", titulo='" + titulo + '\'' +
                ", isbn='" + isbn + '\'' +
                ", editorial='" + editorial + '\'' +
                ", año=" + año +
                ", precio=" + precio +
                ", genero='" + genero + '\'' +
                ", tipoLibro=" + tipoLibro +
                ", stock=" + stock +
                ", fechaRegistro=" + fechaRegistro +
                ", idAutor=" + idAutor +
                '}';
    }
}
