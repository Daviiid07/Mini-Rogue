package org.example.garrido_david_u6a1.model;

import org.example.garrido_david_u6a1.model.carta.Carta;

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

    public Zona(int numeroZona, String nombreZona, Carta[][] salas, int nivel) {
        setNumZona(numeroZona);
        setNombreZona(nombreZona);
        setSalas(salas);
        setNivel(nivel);

        if (getNumZona() == 2) {
            setJefe(true);
        }
    }

    public Carta[][] getSalas() {
        return salas;
    }

    public void setSalas(Carta[][] salas) {
        this.salas = salas;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
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

    // MOVIMIENTO
    public void moverAbajo() {
        if (puedeMoverAbajo()) {
            filaActual++;
        }
    }

    public void moverDerecha() {
        if (puedeMoverDerecha()) {
            columnaActual++;
        }
    }

    public boolean puedeMoverAbajo() {
        return filaActual < salas.length - 1;
    }

    public boolean puedeMoverDerecha() {
        return columnaActual < salas[0].length - 1;
    }

    // MAPA
    public Carta cartaActual() {
        return salas[filaActual][columnaActual];
    }

    public boolean cartaYaVisitada() {
        return cartaActual() != null && cartaActual().estaOcupada();
    }

    public void marcarCartaVisitada() {
        Carta actual = cartaActual();
        if (actual != null && !actual.estaOcupada()) {
            actual.marcarOcupada();
            cartasVisitadas++;
        }
    }

    // FINAL ZONA
    public void comprobarFinalZona() {
        Carta ultima = salas[2][2];
        if (ultima == null || ultima.estaOcupada()) {
            finalizada = true;
        }
    }
}