/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package postres;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDataAccess {
    private static final String PEDIDOS_FILE_PATH = "pedidos.txt";

    public List<Pedido> obtenerPedidos() throws IOException {
        List<Pedido> pedidos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PEDIDOS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Pedido pedido = parsePedido(line);
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    public void guardarPedido(Pedido pedido) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PEDIDOS_FILE_PATH, true))) {
            pw.println(formatPedido(pedido));
        }
    }

    public void modificarPedido(Pedido pedido) throws IOException {
        List<Pedido> pedidos = obtenerPedidos();
        int index = -1;
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getId() == pedido.getId()) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            pedidos.set(index, pedido);
            guardarPedidos(pedidos);
        }
    }

    public void eliminarPedido(Pedido pedido) throws IOException {
        List<Pedido> pedidos = obtenerPedidos();
        pedidos.removeIf(p -> p.getId() == pedido.getId());
        guardarPedidos(pedidos);
    }

    public Pedido buscarPedido(int idPedido) throws IOException {
        List<Pedido> pedidos = obtenerPedidos();
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == idPedido) {
                return pedido;
            }
        }
        return null;
    }

    private void guardarPedidos(List<Pedido> pedidos) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PEDIDOS_FILE_PATH))) {
            for (Pedido pedido : pedidos) {
                pw.println(formatPedido(pedido));
            }
        }
    }

    private Pedido parsePedido(String line) {
        String[] fields = line.split(",");
        int id = Integer.parseInt(fields[0]);
        String direccion = fields[1];
        String nombre = fields[2];
        Pedido pedido = new Pedido(direccion, nombre);
        for (int i = 3; i < fields.length; i += 2) {
            String nombrePostre = fields[i];
            double precioPostre = Double.parseDouble(fields[i + 1]);
            Postre postre = new Postre(nombrePostre, precioPostre);
            pedido.agregarPostre(postre);
        }
        return pedido;
    }

    private String formatPedido(Pedido pedido) {
        StringBuilder sb = new StringBuilder();
        sb.append(pedido.getId()).append(",");
        sb.append(pedido.getDireccion()).append(",");
        sb.append(pedido.getNombre());
        for (Postre postre : pedido.getPostres()) {
            sb.append(",").append(postre.getNombre()).append(",").append(postre.getPrecio());
        }
        return sb.toString();
    }
}
