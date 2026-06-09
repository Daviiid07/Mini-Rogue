package org.example.garrido_david_u6a1.model.carta;

import org.example.garrido_david_u6a1.model.Personaje;

public class CartaTesoro extends Carta {
    private int oro;
    private boolean selloRojo;
    private int vida;
    private int armadura;
    private int px;

    public CartaTesoro(int oro, boolean selloRojo, int vida, int armadura, int px) {
        setOro(oro);
        setSelloRojo(selloRojo);
        setVida(vida);
        setArmadura(armadura);
        setPx(px);
    }

    public int getOro() { 
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }

    public boolean isSelloRojo() {
        return selloRojo;
    }

    public void setSelloRojo(boolean selloRojo) {
        this.selloRojo = selloRojo;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getArmadura() {
        return armadura;
    }

    public void setArmadura(int armadura) {
        this.armadura = armadura;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    @Override
    public String getTipoString() {
        return "TESORO";
    }

    @Override
    public String aplicarEfecto(Personaje personaje, int accion, int opcion) {
        // Oro base (accion == -1 indica "solo aplicar oro base, sin bonus")
        int oroBase = selloRojo ? oro * 2 : oro;
        personaje.ganarOro(oroBase);
        String resultado = "TESORO: +" + oroBase + " oro (base)";

        // accion aquí es el resultado del dado de mazmorra
        if (accion <= 0) {
            return resultado + " | Sin bonificación" + statsStr(personaje);
        }

        String bonus = aplicarBonus(personaje, accion, opcion);
        return resultado + " | Bonus mazmorra " + accion + ": " + bonus + statsStr(personaje);
    }

    public String aplicarBonus(Personaje personaje, int tiradaMazmorra, int opcion) {
        String bonus = "";

        switch (tiradaMazmorra) {
            case 1:
                if (opcion == 1) {
                    personaje.ganarOro(oro);
                    bonus = "+" + oro + " oro";
                } else {
                    personaje.aumentarArmadura(armadura);
                    bonus = "+" + armadura + " armadura";
                }

                break;
            case 2:
                personaje.ganarOro(oro * 2);
                bonus = "+" + (oro * 2) + " oro";
                break;
            case 3:
                personaje.aumentarArmadura(armadura * 2);
                bonus = "+" + (armadura * 2) + " armadura";
                break;
            case 4:
                personaje.recuperarVida(vida);
                bonus = "+" + vida + " vida";
                break;
            case 5:
                if (opcion == 1) {
                    personaje.aumentarArmadura(armadura);
                    bonus = "+" + armadura + " armadura";
                } else {
                    personaje.ganarExperiencia(px);
                    bonus = "+" + px + " PX";
                }
                break;
            case 6:
                personaje.ganarExperiencia(px * 2);
                bonus = "+" + (px * 2) + " PX";
        }

        return bonus;
    }

    private String statsStr(Personaje p) {
        return " | PV: " + p.getVida() + " | PX: " + p.getExperiencia() + " | Oro: " + p.getOro()
                + " | Armadura: " + p.getArmadura();
    }
}
