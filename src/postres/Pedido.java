/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package postres;

import java.util.List;

class Pedido {

    private final int numeroOrden;
    private final String nombreCliente;
    private final String direccionCliente;
    private final List<Producto> productos;

    public Pedido(int numeroOrden, String nombreCliente, String direccionCliente, List<Producto> productos) {
        this.numeroOrden = numeroOrden;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.productos = productos;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }
}
