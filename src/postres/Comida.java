/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package postres;

class Comida extends Producto {

    private String tipo;

    public Comida(int id, String nombre, String tipo) {
        super(id, nombre);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
