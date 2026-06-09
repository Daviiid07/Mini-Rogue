package org.example.garrido_david_u6a1.model.carta;

import org.example.garrido_david_u6a1.model.Personaje;

public class CartaMercader extends Carta {
    private int oro;
    private int comida;
    private int vida;
    private int armadura;

    public CartaMercader(int oro, int comida, int vida, int armadura) {
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
        return "MERCADER";
    }

    @Override
    public String aplicarEfecto(Personaje personaje, int accion, int opcion) {
        String resultado = "";
        String intercambio = "";

        switch (accion) {
            case 1: // COMPRAR
                switch (opcion) {
                    case 1:
                        if (personaje.getOro() >= 1) {
                            personaje.perderOro(1);
                            personaje.recuperarVida(vida);
                            resultado = "Compra";
                            intercambio = "1 oro → +" + vida + " vida";
                        } else {
                            resultado = "Compra fallida (oro insuficiente)";
                        }

                        break;
                    case 2:
                        if (personaje.getOro() >= 5) {
                            personaje.perderOro(5);
                            personaje.aumentarArmadura(armadura);
                            resultado = "Compra";
                            intercambio = "5 oro → +" + armadura + " armadura";
                        } else {
                            resultado = "Compra fallida (oro insuficiente)";
                        }

                        break;
                    case 3:
                        if (personaje.getOro() >= 3) {
                            personaje.perderOro(3);
                            personaje.ganarComida(comida);
                            resultado = "Compra";
                            intercambio = "3 oro → +" + comida + " comida";
                        } else {
                            resultado = "Compra fallida (oro insuficiente)";
                        }

                        break;
                    case 4:
                        if (personaje.getOro() >= 6) {
                            personaje.perderOro(6);
                            personaje.ganarComida(comida * 2);
                            resultado = "Compra";
                            intercambio = "6 oro → +" + (comida * 2) + " comida";
                        } else {
                            resultado = "Compra fallida (oro insuficiente)";
                        }

                        break;
                }
                break;

            case 2: // VENDER
                switch (opcion) {
                    case 1:
                        if (personaje.getArmadura() >= 1) {
                            personaje.reducirArmadura(1);
                            personaje.ganarOro(oro);
                            resultado = "Venta";
                            intercambio = "1 armadura → +" + oro + " oro";
                        } else {
                            resultado = "Venta fallida (sin armadura)";
                        }

                        break;
                    case 2:
                        if (personaje.getComida() >= 1) {
                            personaje.perderComida(1);
                            personaje.ganarOro(oro);
                            resultado = "Venta";
                            intercambio = "1 comida → +" + oro + " oro";
                        } else {
                            resultado = "Venta fallida (sin comida)";
                        }

                        break;
                }
                break;

            case 3: // NADA
                resultado = "Sin transacción";
                break;
        }

        String textoFinal = "MERCADER: " + resultado;

        if (!intercambio.isEmpty()) {
            textoFinal += " (" + intercambio + ")";
        }

        textoFinal += " | PX: " + personaje.getExperiencia() + " | Oro: " + personaje.getOro()
                + " | Armadura: " + personaje.getArmadura();

        return textoFinal;
    }
}
