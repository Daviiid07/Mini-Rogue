package org.example.garrido_david_u6a1.model.carta;

import org.example.garrido_david_u6a1.model.Personaje;

public abstract class Carta {
    private String nombre;
    private boolean ocupada;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public void marcarOcupada() {
        setOcupada(true);
    }

    public boolean estaOcupada() {
        return isOcupada();
    }

    public abstract String getTipoString();

    public abstract String aplicarEfecto(Personaje personaje, int accion, int opcion);

    @Override
    public String toString() {
        return "[" + getTipoString() + "]" + (nombre != null && !nombre.isEmpty() ? " " + nombre : "")
                + (ocupada ? " ✔" : "");
    }
}
