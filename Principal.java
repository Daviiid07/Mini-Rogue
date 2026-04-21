package UD5.U5a1;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Creación Personaje
        ArrayList<Dado> dadosPersonaje = new ArrayList<>();
        dadosPersonaje.add(new Dado("personaje"));
        dadosPersonaje.add(new Dado("mazmorra"));
        Personaje personaje = new Personaje("Heroe", dadosPersonaje, 10, 1, 3);

        // Creación del nivel 1
        ArrayList<Carta> cartasSalasN1 = new ArrayList<>();
        leerCartas(cartasSalasN1);

        Dado dadoEnemigo = new Dado("mazmorra");

        Carta cartaJefe1 = new Carta("ENEMIGO", true, 1, dadoEnemigo);

        Collections.shuffle(cartasSalasN1);

        Carta[][] salasZ1N1 = new Carta[3][3];
        Carta[][] salasZ2N1 = new Carta[3][3];

        rellenaZonas(cartasSalasN1, cartaJefe1, salasZ1N1, salasZ2N1);

        Zona[] nivel1 = {
                new Zona(1, "Zona 1", salasZ1N1, 1),
                new Zona(2, "Zona 2", salasZ2N1, 1)
        };

        actualizaVidaEnemigos(nivel1);

        // Creación del nivel 2
        ArrayList<Carta> cartasSalasN2 = new ArrayList<>();
        leerCartas(cartasSalasN2);

        Carta cartaJefe2 = new Carta("ENEMIGO", true, 1, dadoEnemigo);

        Collections.shuffle(cartasSalasN2);

        Carta[][] salasZ1N2 = new Carta[3][3];
        Carta[][] salasZ2N2 = new Carta[3][3];

        rellenaZonas(cartasSalasN2, cartaJefe2, salasZ1N2, salasZ2N2);

        Zona[] nivel2 = {
                new Zona(1, "Zona 1", salasZ1N2, 2),
                new Zona(2, "Zona 2", salasZ2N2, 2)
        };

        actualizaVidaEnemigos(nivel2);

        ArrayList<Zona[]> niveles = new ArrayList<>();
        niveles.add(nivel1);
        niveles.add(nivel2);

        // Comienzo del juego
        System.out.print("Pulse cualquier tecla para empezar la partida...");
        sc.nextLine();
        System.out.println();

        int nivelActual = 0;

        try {
            FileWriter fileWriter = new FileWriter("registro.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String resultado;

            for (Zona[] nivel : niveles){
                nivelActual++;

                System.out.print("Pulse cualquier tecla para comenzar el nivel... ");
                sc.nextLine();
                System.out.println();

                for (Zona zona : nivel) {
                    System.out.println("====================================");
                    System.out.println("=== NIVEL " + zona.getNivel() + " ===");
                    System.out.println("ZONA: " + zona.getNombreZona());
                    System.out.println("====================================");
                    System.out.println();

                    bufferedWriter.write("=== NIVEL " + zona.getNivel() + " ===\n");
                    bufferedWriter.write("=== ZONA: " + zona.getNombreZona() + " ===\n");

                    System.out.println("Datos actuales del personaje");
                    System.out.println("====================================");
                    personaje.mostrarEstado();
                    System.out.println();

                    while (!zona.isFinalizada()) {
                        Carta actual = zona.cartaActual();

                        if (actual == null) {
                            System.out.println();
                            System.out.println("¡Has completado la zona: " + zona.getNombreZona() + "!");
                            System.out.println();

                            zona.setFinalizada(true);

                            bufferedWriter.write("[" + nivelActual + ", " + zona.getFilaActual() + ", " +
                                    zona.getColumnaActual() + "] Salida de zona\n");

                            continue;
                        }

                        System.out.println("Te encuentras en la sala [" + zona.getFilaActual() + "]" +
                                "[" + zona.getColumnaActual() + "]");
                        actual.mostrarInformacion();
                        System.out.print("Introduce cualquier cosa para entrar en la sala:");
                        sc.nextLine();
                        resultado = zona.visitarCarta(personaje, sc);

                        bufferedWriter.write("[" + nivelActual + ", " + zona.getFilaActual() + ", " +
                                zona.getColumnaActual() + "] " + resultado + "\n");

                        if (zona.isFinalizada()){
                            continue;
                        }

                        if (!personaje.estaVivo()) {
                            System.out.println("¡Has muerto! Fin del juego.");
                            bufferedWriter.write("--- DERROTA ---");

                            bufferedWriter.close();
                            fileWriter.close();

                            return;
                        }

                        int opcion = 0;
                        int salasDispo;
                        do {
                            salasDispo = 0;
                            System.out.println("\nElige a la posición a la que moverte:");
                            if (zona.puedeMoverAbajo()){
                                salasDispo++;
                                System.out.println(salasDispo + ". ABAJO");
                                if (zona.getSalas()[zona.getFilaActual() + 1][zona.getColumnaActual()] == null){
                                    System.out.println("==== FIN DE LA SALA ====");
                                } else {
                                    zona.getSalas()[zona.getFilaActual() + 1][zona.getColumnaActual()].mostrarInformacion();
                                }
                            }

                            if (zona.puedeMoverDerecha()){
                                salasDispo++;
                                System.out.println(salasDispo + ". DERECHA");
                                if (zona.getSalas()[zona.getFilaActual()][zona.getColumnaActual() + 1] == null){
                                    System.out.println("==== FIN DE LA SALA ====");
                                } else {
                                    zona.getSalas()[zona.getFilaActual()][zona.getColumnaActual() + 1].mostrarInformacion();
                                }
                            }

                            System.out.print("Introduce tu elección: ");
                            try {
                                opcion = sc.nextInt();
                                sc.nextLine();
                            }catch (Exception e){
                                System.out.println("Valor no válido. " + e);
                                System.out.println();
                                sc.nextLine();
                                continue;
                            }

                            if (opcion < 1 || opcion > salasDispo){
                                System.out.println("Valor fuera de rango. Por favor introduce uno dentro del rango");
                                System.out.println();
                            }
                        } while (opcion < 1 || opcion > salasDispo);

                        if (salasDispo == 2){
                            if (opcion == 1){
                                zona.moverAbajo();
                            } else {
                                zona.moverDerecha();
                            }
                        } else if (zona.puedeMoverAbajo()) {
                            zona.moverAbajo();
                        } else {
                            zona.moverDerecha();
                        }
                    }

                    if (nivelActual == 1 || !zona.isJefe()){
                        resultado = personaje.comer();
                        bufferedWriter.write(resultado + "\n");

                        if (!personaje.estaVivo()){
                            System.out.println("¡Has muerto por falta de comida! Fin del juego.");
                            bufferedWriter.write("--- DERROTA ---");

                            bufferedWriter.close();
                            fileWriter.close();

                            return;
                        }
                    }
                }
            }

            bufferedWriter.write("--- VICTORIA ---");

            System.out.println();
            System.out.println("==== VICTORIA!!! ====");
            System.out.println();
            System.out.println(" - Estado final del personaje");
            personaje.mostrarEstado();

            bufferedWriter.close();
            fileWriter.close();
        } catch (FileNotFoundException e1) {
            System.out.println("FNF: " + e1);
        } catch (IOException e2) {
            System.out.println("Error E/S: " + e2);
        } catch (Exception e3) {
            System.out.println("Error: " + e3);
        }
    }

    public static void leerCartas(ArrayList<Carta> cartasSalas){
        try {
            FileReader file = new FileReader("cartas.txt");
            BufferedReader br = new BufferedReader(file);
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datosCarta = linea.split(";");
                String tipo = datosCarta[0];
                int oro;
                int comida;
                int armadura;
                int vida;
                boolean selloRojo;
                int px;
                int nivel;
                Dado dadoMazmorra = new Dado("mazmorra");

                switch (tipo){
                    case "TESORO":
                        oro = Integer.parseInt(datosCarta[1]);
                        selloRojo = Boolean.parseBoolean(datosCarta[2]);
                        vida = Integer.parseInt(datosCarta[3]);
                        armadura = Integer.parseInt(datosCarta[4]);
                        px = Integer.parseInt(datosCarta[5]);

                        cartasSalas.add(new Carta(tipo, oro, selloRojo, vida, armadura, px));
                        break;
                    case "MERCADER":
                        oro = Integer.parseInt(datosCarta[1]);
                        comida = Integer.parseInt(datosCarta[2]);
                        vida = Integer.parseInt(datosCarta[3]);
                        armadura = Integer.parseInt(datosCarta[4]);

                        cartasSalas.add(new Carta(tipo, oro, comida, vida, armadura));
                        break;
                    case "TRAMPA":
                        oro = Integer.parseInt(datosCarta[1]);
                        comida = Integer.parseInt(datosCarta[2]);
                        vida = Integer.parseInt(datosCarta[3]);
                        armadura = Integer.parseInt(datosCarta[4]);

                        cartasSalas.add(new Carta(tipo, oro, comida, vida, armadura));
                        break;
                    case "ENEMIGO":
                        nivel = Integer.parseInt(datosCarta[1]);

                        cartasSalas.add(new Carta(tipo, nivel, dadoMazmorra));
                        break;

                    case "DESCANSO":
                        px = Integer.parseInt(datosCarta[1]);
                        comida = Integer.parseInt(datosCarta[2]);
                        vida = Integer.parseInt(datosCarta[3]);

                        cartasSalas.add(new Carta(tipo, px, comida, vida));
                        break;
                    case "EVENTO":
                        vida = Integer.parseInt(datosCarta[1]);
                        comida = Integer.parseInt(datosCarta[2]);
                        px = Integer.parseInt(datosCarta[3]);
                        armadura = Integer.parseInt(datosCarta[4]);
                        oro = Integer.parseInt(datosCarta[5]);
                        nivel = Integer.parseInt(datosCarta[6]);

                        Carta enemigo = new Carta("ENEMIGO", nivel, dadoMazmorra);

                        cartasSalas.add(new Carta(tipo, vida, comida, px, armadura, oro, enemigo));
                        break;
                }
            }
            br.close();
        } catch(FileNotFoundException e){
            System.out.println("No se ha encontrado el archivo");
        }catch(IOException e){
            System.out.println("Error de I/O");
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void rellenaZonas(ArrayList<Carta> cartasSalas, Carta cartaJefe,
                                    Carta[][] salasZ1, Carta[][] salasZ2){
        ArrayList<Carta> cartasZ1 = new ArrayList<>();
        ArrayList<Carta> cartasZ2 = new ArrayList<>();

        for (int i = 0; i < cartasSalas.size(); i++){
            if (i < 8){
                cartasZ1.add(cartasSalas.get(i));
            } else {
                cartasZ2.add(cartasSalas.get(i));
            }
        }
        cartasZ2.add(cartaJefe);

        int contadorCartas = 0;
        for (int i = 0; i < salasZ1.length; i++){
            for (int j = 0; j < salasZ1[0].length; j++){
                if (i == 2 && j == 2){
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

    public static void actualizaVidaEnemigos(Zona[] nivel){
        for (Zona zona : nivel){
            int numZona = zona.getNumZona();
            Carta[][] salas = zona.getSalas();
            int nuevaVida;

            for (int i = 0; i < salas.length; i++){
                for (int j = 0; j < salas[0].length; j++){
                    Carta carta = salas[i][j];

                    if (carta == null) {
                        continue;
                    }

                    if (carta.getTipo() == Carta.Tipo.ENEMIGO && !carta.isEsJefe()){
                        nuevaVida = numZona + ((int)(Math.random() * 6 + 1));
                        carta.setVida(nuevaVida);
                    }

                    if (carta.getTipo() == Carta.Tipo.EVENTO && carta.getEnemigo() != null){
                        Carta enemigo = carta.getEnemigo();
                        nuevaVida = numZona + ((int)(Math.random() * 6 + 1));
                        enemigo.setVida(nuevaVida);
                    }
                }
            }
        }
    }
}
