package org.example.garrido_david_u6a1.model;

import java.util.ArrayList;

public class Personaje {
    private String nombre;
    private int vida;
    private int experiencia;
    private int nivel = 1;
    private int armadura;
    private int comida;
    private int oro;
    private ArrayList<Dado> dadosAtaque = new ArrayList<>();

    public Personaje(String nombre, ArrayList<Dado> dadosAtaque, int vida,
                     int armadura, int oro, int comida) {
        setNombre(nombre);
        setVida(vida);
        setArmadura(armadura);
        setOro(oro);
        setComida(comida);
        setDadosAtaque(dadosAtaque);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        if (vida < 0) {
            this.vida = 0;
        } else if (vida > 20) {
            this.vida = 20;
        } else {
            this.vida = vida;
        }
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getArmadura() {
        return armadura;
    }

    public void setArmadura(int armadura) {
        if (armadura < 0) {
            this.armadura = 0;
        } else if (armadura > 4) {
            this.armadura = 4;
        } else {
            this.armadura = armadura;
        }
    }

    public int getComida() {
        return comida;
    }

    public void setComida(int comida) {
        if (comida < 0) {
            this.comida = 0;
        } else if (comida > 4) {
            this.comida = 4;
        } else {
            this.comida = comida;
        }
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        if (oro < 0) {
            this.oro = 0;
        } else if (oro > 10) {
            this.oro = 10;
        } else {
            this.oro = oro;
        }
    }

    public ArrayList<Dado> getDadosAtaque() {
        return dadosAtaque;
    }

    public void setDadosAtaque(ArrayList<Dado> dadosAtaque) {
        this.dadosAtaque = dadosAtaque;
    }

    public void recibirDanio(int danio) {
        setVida(getVida() - danio);
    }

    public void recuperarVida(int vida) {
        setVida(getVida() + vida);
    }

    public void ganarComida(int comida) {
        setComida(getComida() + comida);
    }

    public void perderComida(int comida) {
        setComida(getComida() - comida);
    }

    public String comer() {
        String resultado;

        if (getComida() == 0) {
            setVida(getVida() - 3);
            resultado = "--- FASE DE AVANCE: -3 vida ---";
        } else {
            setComida(getComida() - 1);
            resultado = "--- FASE DE AVANCE: -1 comida ---";
        }

        return resultado;
    }

    public String ganarExperiencia(int px) {
        String resultado = "";

        setExperiencia(getExperiencia() + px);
        if (getNivel() == 1){
            if (getExperiencia() >= 18) {
                setNivel(4);
                getDadosAtaque().add(new Dado("personaje"));
                getDadosAtaque().add(new Dado("personaje"));
                getDadosAtaque().add(new Dado("personaje"));

                resultado = "--- Has subido al nivel 4!! ---\n";
                resultado += "--- Ganas tres nuevos dados de personaje";

            } else if (getExperiencia() >= 12) {
                setNivel(3);
                getDadosAtaque().add(new Dado("personaje"));
                getDadosAtaque().add(new Dado("personaje"));

                resultado = "--- Has subido al nivel 3!! ---\n";
                resultado += "--- Ganas dos nuevos dados de personaje";
            } else if (getExperiencia() >= 6) {
                setNivel(2);
                getDadosAtaque().add(new Dado("personaje"));

                resultado = "--- Has subido al nivel 2!! ---\n";
                resultado += "--- Ganas un nuevo dado de personaje";
            }
        } else if (getNivel() == 2){
            if (getExperiencia() >= 18) {
                setNivel(4);
                getDadosAtaque().add(new Dado("personaje"));
                getDadosAtaque().add(new Dado("personaje"));

                resultado = "--- Has subido al nivel 4!! ---\n";
                resultado += "--- Ganas dos nuevos dados de personaje";
            } else if (getExperiencia() >= 12) {
                setNivel(3);
                getDadosAtaque().add(new Dado("personaje"));

                resultado = "--- Has subido al nivel 3!! ---\n";
                resultado += "--- Ganas un nuevo dado de personaje";
            }
        } else if (getNivel() == 3) {
            if (getExperiencia() >= 18) {
                setNivel(4);
                getDadosAtaque().add(new Dado("personaje"));

                resultado = "--- Has subido al nivel 4!! ---\n";
                resultado += "--- Ganas un nuevo dado de personaje";
            }
        }

        return resultado;
    }

    public void perderExperiencia(int px) {
        setExperiencia(getExperiencia() - px);
    }

    public void ganarOro(int oro) {
        setOro(getOro() + oro);
    }

    public void perderOro(int oro) {
        setOro(getOro() - oro);
    }

    public void aumentarArmadura(int armadura) {
        setArmadura(getArmadura() + armadura);
    }

    public void reducirArmadura(int armadura) {
        setArmadura(getArmadura() - armadura);
    }

    public boolean estaVivo() {
        return getVida() > 0;
    }
}
