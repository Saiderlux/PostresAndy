/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package postres;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private static int nextId = 1;

    private int id;
    private String direccion;
    private String nombre;
    private List<Postre> postres;

    public Pedido(String direccion, String nombre) {
        this.id = nextId++;
        this.direccion = direccion;
        this.nombre = nombre;
        this.postres = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Postre> getPostres() {
        return postres;
    }

    public void agregarPostre(Postre postre) {
        postres.add(postre);
    }

    public void eliminarPostre(Postre postre) {
        postres.remove(postre);
    }

    public double calcularTotal() {
        double total = 0.0;
        for (Postre postre : postres) {
            total += postre.getPrecio();
        }
        return total;
    }
}

}
