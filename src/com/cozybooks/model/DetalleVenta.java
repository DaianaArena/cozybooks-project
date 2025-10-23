package com.cozybooks.model;

import java.math.BigDecimal;

/**
 * Clase modelo para la entidad DetalleVenta
 * Representa un detalle de una venta (qué libro se vendió, cantidad, precio)
 */
public class DetalleVenta {
    private int idDetalle;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private int idVenta;
    private int idLibro;

    // Constructores
    public DetalleVenta() {}

    public DetalleVenta(int cantidad, BigDecimal precioUnitario, int idVenta, int idLibro) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.idVenta = idVenta;
        this.idLibro = idLibro;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        // Recalcular subtotal cuando cambia la cantidad
        if (precioUnitario != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        // Recalcular subtotal cuando cambia el precio
        if (cantidad > 0) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" +
                "idDetalle=" + idDetalle +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                ", idVenta=" + idVenta +
                ", idLibro=" + idLibro +
                '}';
    }
}
