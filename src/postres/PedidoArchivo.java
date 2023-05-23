/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package postres;

public abstract class PedidoArchivo {

    public abstract String getNombreArchivo();
}

class PedidoCancelado extends PedidoArchivo {

    @Override
    public String getNombreArchivo() {
        return "pedidos_cancelados.txt";
    }
}

class PedidoRecibido extends PedidoArchivo {

    @Override
    public String getNombreArchivo() {
        return "pedido_recibido.txt";
    }
}
