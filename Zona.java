package UD5.U5a1;

import java.util.Scanner;

public class Zona {
    private Carta[][] salas = new Carta[3][3];
    private int nivel;
    private int numZona;
    private String nombreZona;
    private int filaActual;
    private int columnaActual;
    private boolean finalizada;
    private boolean jefe;
    private int cartasVisitadas;

    public Zona(int numeroZona, String nombreZona, Carta[][] salas, int nivel){
        setNumZona(numeroZona);
        setNombreZona(nombreZona);
        setSalas(salas);
        setNivel(nivel);
        if (getNumZona() == 2){
            setJefe(true);
        }
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Carta[][] getSalas() {
        return salas;
    }

    public void setSalas(Carta[][] salas) {
        this.salas = salas;
    }

    public int getNumZona() {
        return numZona;
    }

    public void setNumZona(int numZona) {
        this.numZona = numZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public int getFilaActual() {
        return filaActual;
    }

    public void setFilaActual(int filaActual) {
        this.filaActual = filaActual;
    }

    public int getColumnaActual() {
        return columnaActual;
    }

    public void setColumnaActual(int columnaActual) {
        this.columnaActual = columnaActual;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public boolean isJefe() {
        return jefe;
    }

    public void setJefe(boolean jefe) {
        this.jefe = jefe;
    }

    public int getCartasVisitadas() {
        return cartasVisitadas;
    }

    public void setCartasVisitadas(int cartasVisitadas) {
        this.cartasVisitadas = cartasVisitadas;
    }

    public void moverAbajo() {
        if (getFilaActual() < getSalas().length - 1) {
            setFilaActual(getFilaActual() + 1);
        } else {
            System.out.println("No puedes moverte más hacia abajo.");
        }
    }

    public void moverDerecha() {
        if (getColumnaActual() < getSalas()[0].length - 1) {
            setColumnaActual(getColumnaActual() + 1);
        } else {
            System.out.println("No puedes moverte más hacia la derecha.");
        }
    }

    public boolean puedeMoverAbajo() {
        return getFilaActual() < getSalas().length - 1;
    }

    public boolean puedeMoverDerecha() {
        return getColumnaActual() < getSalas()[0].length - 1;
    }

    public Carta cartaActual() {
        return getSalas()[getFilaActual()][getColumnaActual()];
    }

    public String visitarCarta(Personaje p, Scanner sc) {
        Carta actual = cartaActual();
        String resultado = "";

        if (!actual.estaOcupada()) {
            setCartasVisitadas(getCartasVisitadas() + 1);
            resultado = actual.aplicarCarta(p, sc);
            comprobarFinalZona();
        } else {
            System.out.println("Ya has visitado esta carta.");
        }

        return resultado;
    }

    public void comprobarFinalZona() {
        if (getSalas()[2][2] != null && getSalas()[2][2].estaOcupada()) {
            setFinalizada(true);
            System.out.println();
            System.out.println("¡Has completado la zona: " + getNombreZona() + "!");
        }
    }

}
