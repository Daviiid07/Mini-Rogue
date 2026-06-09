package org.example.garrido_david_u6a1.model.carta;

import org.example.garrido_david_u6a1.model.Personaje;

public class CartaEvento extends Carta {
    private int vida;
    private int comida;
    private int px;
    private int armadura;
    private int oro;
    private CartaEnemigo enemigo;

    public CartaEvento(int vida, int comida, int px, int armadura, int oro, CartaEnemigo enemigo) {
        setVida(vida);
        setComida(comida);
        setPx(px);
        setArmadura(armadura);
        setOro(oro);
        setEnemigo(enemigo);
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getComida() {
        return comida;
    }

    public void setComida(int comida) {
        this.comida = comida;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getArmadura() {
        return armadura;
    }

    public void setArmadura(int armadura) {
        this.armadura = armadura;
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }

    public CartaEnemigo getEnemigo() {
        return enemigo;
    }

    public void setEnemigo(CartaEnemigo enemigo) {
        this.enemigo = enemigo;
    }

    @Override
    public String getTipoString() {
        return "EVENTO";
    }

    @Override
    public String aplicarEfecto(Personaje personaje, int accion, int opcion) {
        if (accion <= 0) {
            return "EVENTO: Prueba de habilidad fallida. Sin efecto." + statsStr(personaje);
        }

        String recurso = "";
        switch (accion) {
            case 1:
                personaje.ganarComida(comida);
                recurso = "+" + comida + " Comida";
                break;
            case 2:
                personaje.recuperarVida(vida);
                recurso = "+" + vida + " Vida";
                break;
            case 3:
                personaje.ganarOro(oro);
                recurso = "+" + oro + " Oro";
                break;
            case 4:
                personaje.ganarExperiencia(px);
                recurso = "+" + px + " PX";
                break;
            case 5:
                personaje.aumentarArmadura(armadura);
                recurso = "+" + armadura + " Armadura";
                break;
            case 6:
                // El controlador debe detectar esto y lanzar el combate
                return "EVENTO: ¡Aparece un enemigo! Combate iniciado.";
        }

        return "EVENTO: " + recurso + statsStr(personaje);
    }

    public boolean necesitaCombate(int tiradaMazmorra) {
        return tiradaMazmorra == 6 && enemigo != null;
    }

    private String statsStr(Personaje p) {
        return " | PV: " + p.getVida() + " | PX: " + p.getExperiencia() + " | Oro: " + p.getOro()
                + " | Armadura: " + p.getArmadura();
    }

}
