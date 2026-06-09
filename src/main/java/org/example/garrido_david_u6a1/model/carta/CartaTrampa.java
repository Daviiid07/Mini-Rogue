package org.example.garrido_david_u6a1.model.carta;

import org.example.garrido_david_u6a1.model.Dado;
import org.example.garrido_david_u6a1.model.Personaje;

public class CartaTrampa extends Carta {
    private int oro;
    private int comida;
    private int vida;
    private int armadura;

    public CartaTrampa(int oro, int comida, int vida, int armadura) {
        setOro(oro);
        setComida(comida);
        setVida(vida);
        setArmadura(armadura);
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
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

    public int getArmadura() {
        return armadura;
    }

    public void setArmadura(int armadura) {
        this.armadura = armadura;
    }

    @Override
    public String getTipoString() {
        return "TRAMPA";
    }

    @Override
    public String aplicarEfecto(Personaje personaje, int accion, int opcion) {
        boolean pruebaSuperada = false;
        int tiradaHabilidad = 0;

        for (Dado d : personaje.getDadosAtaque()) {
            if (d.getTipo().equalsIgnoreCase("personaje")) {
                tiradaHabilidad = d.lanzar();

                if (tiradaHabilidad >= 5) {
                    pruebaSuperada = true;
                    break;
                }
            }
        }

        int tiradaMazmorra = -1;
        for (Dado d : personaje.getDadosAtaque()) {
            if (d.getTipo().equalsIgnoreCase("mazmorra")) {
                tiradaMazmorra = d.lanzar();
                break;
            }
        }

        String recurso;
        if (tiradaMazmorra == 1 || tiradaMazmorra == 2) {
            if (pruebaSuperada) {
                personaje.ganarComida(comida);
                recurso = "+" + comida + " Comida";
            } else {
                personaje.perderComida(comida);
                recurso = "-" + comida + " Comida";
            }
        } else if (tiradaMazmorra == 3 || tiradaMazmorra == 4) {
            if (pruebaSuperada) {
                personaje.aumentarArmadura(armadura);
                recurso = "+" + armadura + " Armadura";
            } else {
                personaje.reducirArmadura(armadura);
                recurso = "-" + armadura + " Armadura";
            }
        } else {
            if (pruebaSuperada) {
                personaje.recuperarVida(vida);
                recurso = "+" + vida + " Vida";
            } else {
                personaje.recibirDanio(vida);
                recurso = "-" + vida + " Vida";
            }
        }

        return "TRAMPA: Habilidad " + tiradaHabilidad + (pruebaSuperada ? " ✓" : " ✗")
                + " | Mazmorra " + tiradaMazmorra + " → " + recurso + " | PV: " + personaje.getVida()
                + " | PX: " + personaje.getExperiencia() + " | Oro: " + personaje.getOro()
                + " | Armadura: " + personaje.getArmadura();
    }
}
