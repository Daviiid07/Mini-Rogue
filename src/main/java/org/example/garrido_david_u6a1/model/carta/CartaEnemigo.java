package org.example.garrido_david_u6a1.model.carta;

import org.example.garrido_david_u6a1.model.Dado;
import org.example.garrido_david_u6a1.model.Personaje;

public class CartaEnemigo extends Carta {
    private boolean esJefe;
    private int nivel;
    private Dado dadoAtaque;
    private String descripcion;
    private int vida;
    private int ataque;
    private int oro;
    private int px;

    // Constructor enemigo normal.
    public CartaEnemigo(int nivel, Dado dadoAtaque, String descripcion, int ataque, int px) {
        setNivel(nivel);
        setDadoAtaque(dadoAtaque);
        setDescripcion(descripcion);
        setAtaque(ataque);
        setPx(px);
        setEsJefe(false);
    }

    // Constructor enemigo jefe de zona.
    public CartaEnemigo(boolean esJefe, int nivel, Dado dadoAtaque, String descripcion,
                        int vida, int ataque, int oro, int px) {
        setEsJefe(esJefe);
        setNivel(nivel);
        setDadoAtaque(dadoAtaque);
        setDescripcion(descripcion);
        setVida(vida);
        setAtaque(ataque);
        setOro(oro);
        setPx(px);
    }

    public boolean isEsJefe() {
        return esJefe;
    }

    public void setEsJefe(boolean esJefe) {
        this.esJefe = esJefe;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Dado getDadoAtaque() {
        return dadoAtaque;
    }

    public void setDadoAtaque(Dado dadoAtaque) {
        this.dadoAtaque = dadoAtaque;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = Math.max(0, vida);
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public void recibirDanio(int cantidad) {
        setVida(getVida() - cantidad);
    }

    public boolean estaVivo() {
        return getVida() > 0;
    }

    public int atacar() {
        return dadoAtaque.lanzar();
    }

    @Override
    public String getTipoString() {
        return esJefe ? "JEFE" : "ESBIRRO";
    }


    @Override
    public String aplicarEfecto(Personaje personaje, int accion, int opcion) {
        String subida = personaje.ganarExperiencia(px);
        String recompensa = getTipoString() + " (" + descripcion + ") derrotado: +" + px + " PX";

        if (esJefe) {
            personaje.ganarOro(oro);
            recompensa += ", +" + oro + " oro (jefe)";
        }

        if (subida != null && !subida.isEmpty()) {
            recompensa += " | " + subida;
        }

        return recompensa + " | PV: " + personaje.getVida() + " | PX: " + personaje.getExperiencia()
                + " | Oro: " + personaje.getOro() + " | Armadura: " + personaje.getArmadura();
    }
}
