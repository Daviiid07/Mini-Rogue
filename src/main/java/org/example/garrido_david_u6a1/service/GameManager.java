package org.example.garrido_david_u6a1.service;

import org.example.garrido_david_u6a1.model.Dado;
import org.example.garrido_david_u6a1.model.Personaje;
import org.example.garrido_david_u6a1.model.Zona;
import org.example.garrido_david_u6a1.model.carta.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GameManager {
    private Personaje personaje;
    private ArrayList<Zona[]> niveles = new ArrayList<>();

    public GameManager() {
        inicializarJuego();
    }

    public Personaje getPersonaje() {
        return personaje;
    }

    public ArrayList<Zona[]> getNiveles() {
        return niveles;
    }

    public void setNiveles(ArrayList<Zona[]> niveles) {
        this.niveles = niveles;
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;
    }

    private void inicializarJuego() {
        ArrayList<Dado> dadosPersonaje = new ArrayList<>();
        dadosPersonaje.add(new Dado("personaje"));
        dadosPersonaje.add(new Dado("mazmorra"));

        personaje = new Personaje("Heroe", dadosPersonaje, 10, 1, 3, 3);

        niveles.add(crearNivel(1));
        niveles.add(crearNivel(2));
    }

    private Zona[] crearNivel(int nivel) {
        ArrayList<Carta> cartasSalas = new ArrayList<>();

        leerCartas(cartasSalas);

        Collections.shuffle(cartasSalas);

        Carta[][] salasZ1 = new Carta[3][3];
        Carta[][] salasZ2 = new Carta[3][3];

        rellenaZonas(cartasSalas, salasZ1, salasZ2);

        Zona[] zonas = {
                new Zona(1, "Zona 1", salasZ1, nivel),
                new Zona(2, "Zona 2", salasZ2, nivel)
        };

        actualizaVidaEnemigos(zonas);

        return zonas;
    }

    private void leerCartas(ArrayList<Carta> cartasSalas) {
        try {
            FileReader file = new FileReader("cartas.txt");
            BufferedReader br = new BufferedReader(file);
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                String tipo = d[0];
                Dado dadoMazmorra = new Dado("mazmorra");

                switch (tipo) {
                    case "TESORO":
                        cartasSalas.add(new CartaTesoro(
                                Integer.parseInt(d[1]),
                                Boolean.parseBoolean(d[2]),
                                Integer.parseInt(d[3]),
                                Integer.parseInt(d[4]),
                                Integer.parseInt(d[5])
                        ));
                        break;

                    case "MERCADER":
                        cartasSalas.add(new CartaMercader(
                                Integer.parseInt(d[1]),
                                Integer.parseInt(d[2]),
                                Integer.parseInt(d[3]),
                                Integer.parseInt(d[4])
                        ));
                        break;

                    case "TRAMPA":
                        cartasSalas.add(new CartaTrampa(
                                Integer.parseInt(d[1]),
                                Integer.parseInt(d[2]),
                                Integer.parseInt(d[3]),
                                Integer.parseInt(d[4])
                        ));
                        break;

                    case "ENEMIGO":
                        boolean esJefe = Boolean.parseBoolean(d[1]);
                        if (esJefe) {
                            cartasSalas.add(new CartaEnemigo(
                                    true,
                                    Integer.parseInt(d[2]),
                                    dadoMazmorra,
                                    d[3],
                                    Integer.parseInt(d[4]),
                                    Integer.parseInt(d[5]),
                                    Integer.parseInt(d[6]),
                                    Integer.parseInt(d[7])
                            ));
                        } else {
                            cartasSalas.add(new CartaEnemigo(
                                    Integer.parseInt(d[2]),
                                    dadoMazmorra,
                                    d[3],
                                    Integer.parseInt(d[4]),
                                    Integer.parseInt(d[5])
                            ));
                        }
                        break;

                    case "DESCANSO":
                        cartasSalas.add(new CartaDescanso(
                                Integer.parseInt(d[1]),
                                Integer.parseInt(d[2]),
                                Integer.parseInt(d[3])
                        ));
                        break;

                    case "EVENTO":
                        CartaEnemigo enemigoEvento = new CartaEnemigo(
                                Integer.parseInt(d[6]),
                                dadoMazmorra,
                                d[7],
                                Integer.parseInt(d[8]),
                                Integer.parseInt(d[9])
                        );
                        cartasSalas.add(new CartaEvento(
                                Integer.parseInt(d[1]),
                                Integer.parseInt(d[2]),
                                Integer.parseInt(d[3]),
                                Integer.parseInt(d[4]),
                                Integer.parseInt(d[5]),
                                enemigoEvento
                        ));
                        break;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el archivo");
        } catch (IOException e) {
            System.out.println("Error de I/O");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void rellenaZonas(ArrayList<Carta> cartasSalas,
                               Carta[][] salasZ1, Carta[][] salasZ2) {
        ArrayList<Carta> cartasZ1 = new ArrayList<>();
        ArrayList<Carta> cartasZ2 = new ArrayList<>();
        CartaEnemigo cartaJefe = null;

        for (Carta carta : cartasSalas) {
            // instanceof para identificar el jefe
            if (carta instanceof CartaEnemigo && ((CartaEnemigo) carta).isEsJefe()) {
                cartaJefe = (CartaEnemigo) carta;
            } else {
                if (cartasZ1.size() < 8) {
                    cartasZ1.add(carta);
                } else {
                    cartasZ2.add(carta);
                }
            }
        }

        cartasZ2.add(cartaJefe);

        int contadorCartas = 0;
        for (int i = 0; i < salasZ1.length; i++) {
            for (int j = 0; j < salasZ1[0].length; j++) {
                if (i == 2 && j == 2) {
                    salasZ1[i][j] = null;
                    salasZ2[i][j] = cartasZ2.getLast();
                } else {
                    salasZ1[i][j] = cartasZ1.get(contadorCartas);
                    salasZ2[i][j] = cartasZ2.get(contadorCartas);
                    contadorCartas++;
                }
            }
        }
    }

    private void actualizaVidaEnemigos(Zona[] nivel) {
        for (Zona zona : nivel) {
            int numZona = zona.getNumZona();

            for (Carta[] fila : zona.getSalas()) {
                for (Carta carta : fila) {
                    if (carta == null) {
                        continue;
                    }

                    // instanceof para identificar los enemigos no jefe
                    if (carta instanceof CartaEnemigo enemigo && !enemigo.isEsJefe()) {
                        int nuevaVida = numZona + ((int) (Math.random() * 6 + 1));
                        enemigo.setVida(nuevaVida);
                    }

                    if (carta instanceof CartaEvento evento && evento.getEnemigo() != null) {
                        int nuevaVida = numZona + ((int) (Math.random() * 6 + 1));
                        evento.getEnemigo().setVida(nuevaVida);
                    }
                }
            }
        }
    }
}
