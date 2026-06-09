package org.example.garrido_david_u6a1.model.carta;

import org.example.garrido_david_u6a1.model.Personaje;

public class CartaDescanso extends Carta {
    private int px;
    private int comida;
    private int vida;

    public CartaDescanso(int px, int comida, int vida) {
        setPx(px);
        setComida(comida);
        setVida(vida);
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getComida() {
        return comida;
    }

    public void setComida(int comida) {
        this.comida = comida;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    @Override
    public String getTipoString() {
        return "DESCANSO";
    }

     // Aplica el efecto del descanso según la elección del jugador.
    @Override
    public String aplicarEfecto(Personaje personaje, int accion, int opcion) {
        String recurso = "";

        switch (opcion) {
            case 1:
                personaje.ganarExperiencia(px);
                recurso = "+" + px + " PX";
                break;
            case 2:
                personaje.ganarComida(comida);
                recurso = "+" + comida + " Comida";
                break;
            case 3:
                personaje.recuperarVida(vida);
                recurso = "+" + vida + " Vida";
        }

        return "DESCANSO: " + recurso + " | PV: " + personaje.getVida() + " | PX: " + personaje.getExperiencia()
                + " | Oro: " + personaje.getOro() + " | Armadura: " + personaje.getArmadura();
    }
}
