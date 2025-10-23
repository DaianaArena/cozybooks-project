package com.cozybooks.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase modelo para la entidad Venta
 * Representa una transacción de venta
 */
public class Venta {
    private int idVenta;
    private LocalDateTime fecha;
    private BigDecimal monto;
    private MetodoPago metodoPago;
    private EstadoVenta estado;
    private int idCliente;
    private List<DetalleVenta> detalles;

    // Enum para métodos de pago
    public enum MetodoPago {
        EFECTIVO, TARJETA, TRANSFERENCIA
    }

    // Enum para estados de venta
    public enum EstadoVenta {
        COMPLETADA, PENDIENTE, CANCELADA
    }

    // Constructores
    public Venta() {}

    public Venta(int idCliente, MetodoPago metodoPago) {
        this.idCliente = idCliente;
        this.metodoPago = metodoPago;
        this.estado = EstadoVenta.COMPLETADA;
        this.fecha = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", fecha=" + fecha +
                ", monto=" + monto +
                ", metodoPago=" + metodoPago +
                ", estado=" + estado +
                ", idCliente=" + idCliente +
                '}';
    }
}
