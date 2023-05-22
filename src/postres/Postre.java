package postres;

class Postre extends Producto {

    private String sabor;

    public Postre(int id, String nombre, String sabor) {
        super(id, nombre);
        this.sabor = sabor;
    }

    public String getSabor() {
        return sabor;
    }
}
