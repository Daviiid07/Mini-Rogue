package org.example.garrido_david_u6a1.service;

import org.example.garrido_david_u6a1.model.Dado;
import org.example.garrido_david_u6a1.model.Personaje;
import org.example.garrido_david_u6a1.model.carta.*;

public class GameService {
    private final GameManager gameManager;

    public GameService(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public String resolverCarta(Carta carta, Personaje p, int accion, int opcion) {
        return carta.aplicarEfecto(p, accion, opcion);
    }

    // Lanza el dado de habilidad del personaje y devuelve si supera
    public int lanzarHabilidad(Personaje personaje) {
        for (Dado d : personaje.getDadosAtaque()) {
            if (d.getTipo().equalsIgnoreCase("personaje")) {
                int lanzada = d.lanzar();

                if(lanzada >= 5){
                    return lanzada;
                }
            }
        }
        return 0;
    }


     // Lanza el dado de mazmorra del personaje.
    public int lanzarMazmorra(Personaje personaje) {
        for (Dado d : personaje.getDadosAtaque()) {
            if (d.getTipo().equalsIgnoreCase("mazmorra")) {
                return d.lanzar();
            }
        }
        return -1;
    }
}
