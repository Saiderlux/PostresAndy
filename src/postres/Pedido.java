/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package postres;

import java.util.List;

class Pedido {

    private static int contadorPedidos = 0;

    private int numeroOrden;
    private String nombreCliente;
    private String direccionCliente;
    private List<Producto> productos;

    public Pedido(String nombreCliente, String direccionCliente, List<Producto> productos) {
        this.numeroOrden = ++contadorPedidos;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.productos = productos;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
