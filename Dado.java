package UD5.U5a1;

public class Dado {
    private int caras = 6;
    private int valorActual;
    private String tipo;

    public Dado(String tipo){
        setTipo(tipo);
    }

    public int getCaras() {
        return caras;
    }

    public void setCaras(int caras) {
        this.caras = caras;
    }

    public int getValorActual() {
        return valorActual;
    }

    public void setValorActual(int valorActual) {
        this.valorActual = valorActual;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int lanzar(){
        setValorActual((int) (Math.random() * getCaras() + 1));

        return getValorActual();
    }

    public boolean esCritico(){
        return getValorActual() == 6;
    }

    public boolean esFallo(){
        return getValorActual() == 1;
    }
}
