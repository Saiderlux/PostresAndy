/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package postres;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class PedidoManager {

    private String postresFile;
    private String pedidosFile;
    private List<Producto> postres;
    private List<Pedido> pedidos;

    public PedidoManager(String postresFile, String pedidosFile) {
        this.postresFile = postresFile;
        this.pedidosFile = pedidosFile;
        this.postres = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        cargarPostres();
        cargarPedidos();
    }

    private void cargarPostres() {
        try (Scanner scanner = new Scanner(new File(postresFile))) {
            while (scanner.hasNextLine()) {
                String[] postreData = scanner.nextLine().split(",");
                int id = Integer.parseInt(postreData[0]);
                String nombre = postreData[1];
                postres.add(new Postre(id, nombre));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void cargarPedidos() {
        try (Scanner scanner = new Scanner(new File(pedidosFile))) {
            while (scanner.hasNextLine()) {
                String[] pedidoData = scanner.nextLine().split(",");
                int numeroOrden = Integer.parseInt(pedidoData[0]);
                String nombreCliente = pedidoData[1];
                String direccionCliente = pedidoData[2];
                String[] productosData = pedidoData[3].split("\\|");
                List<Producto> productos = new ArrayList<>();
                for (String productoData : productosData) {
                    int id = Integer.parseInt(productoData);
                    Producto producto = buscarProducto(id);
                    if (producto != null) {
                        productos.add(producto);
                    }
                }
                Pedido pedido = new Pedido(nombreCliente, direccionCliente, productos);
                pedido.setNumeroOrden(numeroOrden);
                pedidos.add(pedido);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Producto buscarProducto(int id) {
        for (Producto producto : postres) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    private void guardarPedidos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(pedidosFile))) {
            for (Pedido pedido : pedidos) {
                writer.println(pedido.getNumeroOrden() + "," + pedido.getNombreCliente() + ","
                        + pedido.getDireccionCliente() + "," + obtenerProductosString(pedido.getProductos()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String obtenerProductosString(List<Producto> productos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < productos.size(); i++) {
            sb.append(productos.get(i).getId());
            if (i < productos.size() - 1) {
                sb.append("|");
            }
        }
        return sb.toString();
    }

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
        guardarPedidos();
    }

    public void modificarPedido(int numeroOrden, String nombreCliente, String campoMod, String nuevoValorMod) {
        Pedido pedido = buscarPedido(numeroOrden, nombreCliente);
        if (pedido != null) {
            switch (campoMod) {
                case "direccion":
                    pedido.setDireccionCliente(nuevoValorMod);
                    break;
                case "productos":
                    List<Producto> nuevosProductos = obtenerProductosDesdeString(nuevoValorMod);
                    if (nuevosProductos != null) {
                        pedido.setProductos(nuevosProductos);
                    }
                    break;
                case "nombre":
                    pedido.setNombreCliente(nuevoValorMod);
                    break;
                default:
                    System.out.println("Campo de modificación inválido.");
                    return;
            }
            guardarPedidos();
            System.out.println("Pedido modificado correctamente.");
        } else {
            System.out.println("Pedido no encontrado.");
        }
    }

    private List<Producto> obtenerProductosDesdeString(String productosInput) {
        String[] productosData = productosInput.split("\\|");
        List<Producto> nuevosProductos = new ArrayList<>();
        for (String productoData : productosData) {
            int id = Integer.parseInt(productoData);
            Producto producto = buscarProducto(id);
            if (producto != null) {
                nuevosProductos.add(producto);
            } else {
                System.out.println("El ID del producto no existe.");
                return null;
            }
        }
        return nuevosProductos;
    }

    private Pedido buscarPedido(int numeroOrden, String nombreCliente) {
        for (Pedido pedido : pedidos) {
            if (pedido.getNumeroOrden() == numeroOrden && pedido.getNombreCliente().equals(nombreCliente)) {
                return pedido;
            }
        }
        return null;
    }

    public void cancelarPedido(String nombreCliente, int numeroOrden) {
        Pedido pedido = buscarPedido(numeroOrden, nombreCliente);
        if (pedido != null) {
            pedidos.remove(pedido);
            guardarPedidos();
            guardarPedidoCancelado(pedido);
            System.out.println("Pedido cancelado correctamente.");
        } else {
            System.out.println("Pedido no encontrado.");
        }
    }

    public void recibirPedido(String nombreCliente, int numeroOrden) {
        Pedido pedido = buscarPedido(numeroOrden, nombreCliente);
        if (pedido != null) {
            pedidos.remove(pedido);
            guardarPedidos();
            guardarPedidoRecibido(pedido);
            System.out.println("Pedido recibido, archivado correctamente.");
        } else {
            System.out.println("Pedido no encontrado.");
        }
    }

    private void guardarPedidoCancelado(Pedido pedido) {
        PedidoCancelado pedidoCancelado = new PedidoCancelado();
        try (PrintWriter writer = new PrintWriter(new FileWriter(pedidoCancelado.getNombreArchivo(), true))) {
            writer.println(pedido.getNumeroOrden() + "," + pedido.getNombreCliente() + ","
                    + pedido.getDireccionCliente() + "," + obtenerProductosString(pedido.getProductos()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarPedidoRecibido(Pedido pedido) {
        PedidoRecibido pedidoRecibido = new PedidoRecibido();
        try (PrintWriter writer = new PrintWriter(new FileWriter(pedidoRecibido.getNombreArchivo(), true))) {
            writer.println(pedido.getNumeroOrden() + "," + pedido.getNombreCliente() + ","
                    + pedido.getDireccionCliente() + "," + obtenerProductosString(pedido.getProductos()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void visualizarPedido(int numeroOrden, String nombreCliente) {
        Pedido pedido = buscarPedido(numeroOrden, nombreCliente);
        if (pedido != null) {
            mostrarTicketEnConsola(pedido);
            generarTicket(pedido);
        } else {
            System.out.println("Pedido no encontrado.");
        }
    }

    private void generarTicket(Pedido pedido) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("ticket.pdf"))) {
            writer.println("***************************");
            writer.println("Tienda de Postres - Andy");
            writer.println("***************************");
            writer.println("Número de Orden: " + pedido.getNumeroOrden());
            writer.println("Cliente: " + pedido.getNombreCliente());
            writer.println("Dirección: " + pedido.getDireccionCliente());
            writer.println("Productos:");
            for (Producto producto : pedido.getProductos()) {
                writer.println("- " + producto.getNombre());
            }
            writer.println("***************************");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarTicketEnConsola(Pedido pedido) {

        System.out.println("\n***************************");
        System.out.println("Tienda de Postres - Andy");
        System.out.println("***************************");
        System.out.println("Número de Orden: " + pedido.getNumeroOrden());
        System.out.println("Cliente: " + pedido.getNombreCliente());
        System.out.println("Dirección: " + pedido.getDireccionCliente());
        System.out.println("Productos:");
        for (Producto producto : pedido.getProductos()) {
            System.out.println("- " + producto.getNombre());
        }
        System.out.println("***************************\n");

    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        do {
            System.out.println("************ MENÚ ************");
            System.out.println("1. Agregar Pedido");
            System.out.println("2. Modificar Pedido");
            System.out.println("3. Cancelar Pedido");
            System.out.println("4. Visualizar Pedido");
            System.out.println("5. Confirmar recepción de pedido");
            System.out.println("6. Salir del programa");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer de entrada

            switch (opcion) {
                case 1:
                    agregarPedidoDesdeConsola(scanner);
                    break;
                case 2:
                    modificarPedidoDesdeConsola(scanner);
                    break;
                case 3:
                    cancelarPedidoDesdeConsola(scanner);
                    break;
                case 4:
                    visualizarPedidoDesdeConsola(scanner);
                    break;
                case 5:
                    confirmarPedidoDesdeConsola(scanner);
                    break;
                case 6:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
                    break;
            }
        } while (opcion != 6);
        scanner.close();
    }

    private void agregarPedidoDesdeConsola(Scanner scanner) {
        System.out.println("Ingrese los datos del pedido:");
        System.out.print("Nombre del Cliente: ");
        String nombreCliente = scanner.nextLine();
        System.out.print("Dirección del Cliente: ");
        String direccionCliente = scanner.nextLine();

        List<Producto> productos = seleccionarProductosDesdeConsola(scanner);

        Pedido pedido = new Pedido(nombreCliente, direccionCliente, productos);
        agregarPedido(pedido);

        System.out.println("Pedido agregado correctamente con número de orden: " + pedido.getNumeroOrden());
    }

    private List<Producto> seleccionarProductosDesdeConsola(Scanner scanner) {
        System.out.println("Productos disponibles:");
        mostrarProductos();

        List<Producto> productosSeleccionados = new ArrayList<>();
        char opcion;
        do {
            System.out.print("Seleccione un producto (ID): ");
            int idProducto = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer de entrada

            Producto producto = buscarProducto(idProducto);
            if (producto != null) {
                productosSeleccionados.add(producto);
                System.out.print("¿Desea agregar otro producto? (S/N): ");
                opcion = scanner.nextLine().charAt(0);
            } else {
                System.out.println("ID de producto inválido. Intente de nuevo.");
                opcion = 'S'; // Volver a pedir la selección de productos
            }
        } while (opcion == 'S' || opcion == 's');

        return productosSeleccionados;
    }

    private void modificarPedidoDesdeConsola(Scanner scanner) {
        System.out.print("Ingrese el número de orden del pedido: ");
        int numeroOrden = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        System.out.print("Ingrese el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();

        System.out.println("Seleccione el campo a modificar:");
        System.out.println("1. Dirección");
        System.out.println("2. Productos");
        System.out.println("3. Nombre");
        System.out.print("Seleccione una opción: ");
        int opcionMod = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        String campoMod;
        switch (opcionMod) {
            case 1:
                campoMod = "direccion";
                System.out.print("Ingrese la nueva dirección: ");
                break;
            case 2:
                campoMod = "productos";
                System.out.print("Ingrese los nuevos productos (IDs separados por '|'): ");
                break;
            case 3:
                campoMod = "nombre";
                System.out.print("Ingrese el nuevo nombre: ");
                break;
            default:
                System.out.println("Opción inválida. Intente de nuevo.");
                return;
        }

        String nuevoValorMod = scanner.nextLine();

        modificarPedido(numeroOrden, nombreCliente, campoMod, nuevoValorMod);
    }

    private void cancelarPedidoDesdeConsola(Scanner scanner) {
        System.out.print("Ingrese el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();
        System.out.print("Ingrese el número de orden del pedido: ");
        int numeroOrden = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        cancelarPedido(nombreCliente, numeroOrden);
    }

    private void confirmarPedidoDesdeConsola(Scanner scanner) {
        System.out.print("Ingrese el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();
        System.out.print("Ingrese el número de orden del pedido: ");
        int numeroOrden = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        recibirPedido(nombreCliente, numeroOrden);
    }

    private void visualizarPedidoDesdeConsola(Scanner scanner) {
        System.out.print("Ingrese el número de orden del pedido: ");
        int numeroOrden = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        System.out.print("Ingrese el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();
        System.out.println("\n==Consulte los archivos para visualizar su ticket en pdf==");
        visualizarPedido(numeroOrden, nombreCliente);
    }

    private void mostrarProductos() {
        for (Producto producto : postres) {
            System.out.println("ID: " + producto.getId() + " - Nombre: " + producto.getNombre());
        }
    }
}
