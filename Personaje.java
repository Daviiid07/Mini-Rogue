package UD5.U5a1;

import java.util.ArrayList;
import java.util.Scanner;

public class Personaje {
    private String nombre;
    private int vida;
    private int experiencia;
    private int nivel = 1;
    private int armadura;
    private int comida;
    private int oro;
    private ArrayList<Dado> dadosAtaque = new ArrayList<>();

    public Personaje(String nombre, ArrayList<Dado> dadosAtaque, int vida, int armadura, int oro) {
        setNombre(nombre);
        setVida(vida);
        setArmadura(armadura);
        setOro(oro);
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
            System.out.println("Has alcanzado la vida máxima (20)");
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
            System.out.println("Has alcanzado la armadura máxima (4)");
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
            System.out.println("Has alcanzado la comida máxima (4)");
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
            System.out.println("Has alcanzado el oro máximo (10)");
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

    public int atacar(Scanner sc) {
        int total = 0;

        System.out.println();
        System.out.println("=== Comienza el ataque ===");

        for (Dado aux : getDadosAtaque()){
            if (aux.getTipo().equalsIgnoreCase("personaje")){
                System.out.println();
                System.out.println("Se lanza el dado...");
                int tirada = aux.lanzar();
                System.out.println("Resultado del dado: " + tirada);

                if (getExperiencia() > 0 || getVida() > 2) {
                    System.out.print("¿Quieres realizar una proeza? (Si/No): ");
                    String decision = sc.nextLine();
                    if (decision.equalsIgnoreCase("si")) {
                        int opcion = 0;
                        int numOpciones;
                        boolean puedePX = getExperiencia() > 0;
                        boolean puedePV = getVida() > 2;

                        do {
                            numOpciones = 0;
                            System.out.println("Opciones para la proeza:");

                            if (puedePX) {
                                numOpciones++;
                                System.out.println("\t" + numOpciones + ". Restar 1PX");
                            }
                            if (puedePV) {
                                numOpciones++;
                                System.out.println("\t" + numOpciones + ". Restar 2PV");
                            }

                            System.out.print("Selecciona tu opción: ");
                            try {
                                opcion = sc.nextInt();
                                sc.nextLine();
                            } catch (Exception e) {
                                System.out.println("Valor no válido: " + e);
                                sc.nextLine();
                                continue;
                            }

                            if (opcion < 1 || opcion > numOpciones) {
                                System.out.println("Opción fuera de rango. Intenta de nuevo.");
                            }
                        } while (opcion < 1 || opcion > numOpciones);

                        if (numOpciones == 1) {
                            if (puedePX) {
                                System.out.println("Se resta 1PX");
                                perderExperiencia(1);
                            } else {
                                System.out.println("Se resta 2PV");
                                recibirDanio(2);
                            }
                        } else {
                            if (opcion == 1) {
                                System.out.println("Se resta 1PX");
                                perderExperiencia(1);
                            } else {
                                System.out.println("Se resta 2PV");
                                recibirDanio(2);
                            }
                        }

                        System.out.println();
                        System.out.println("Se lanza el dado de proeza...");
                        tirada = aux.lanzar();
                        System.out.println("Resultado del dado de proeza: " + tirada);
                    } else {
                        System.out.println("No se realiza la proeza");
                    }
                } else {
                    System.out.println("No puedes realizar una proeza");
                }

                System.out.println();

                boolean falloRelanzada = false;

                while (aux.esCritico()) {
                    System.out.print("¡Crítico! ¿Quieres relanzar el dado? (Si/No): ");
                    String decision = sc.nextLine();
                    if (decision.equalsIgnoreCase("si")) {
                        int relanzada = aux.lanzar();
                        System.out.println("Resultado de la relanzada: " + relanzada);
                        if (!aux.esFallo()) {
                            System.out.println("No ha fallado, se suma al valor del crítico");
                            tirada += relanzada;
                        } else {
                            System.out.println("Ha fallado, se pierde todo el valor del dado");
                            falloRelanzada = true;
                            tirada = 0;
                        }
                    } else {
                        System.out.println("No se relanza el dado");
                        break;
                    }
                }

                if (aux.esFallo() || falloRelanzada) {
                    System.out.println("El dado ha fallado, se pierde el valor");
                    tirada = 0;
                }

                total += tirada;
                System.out.println("Valor acumulado hasta ahora: " + total);
            }
        }

        System.out.println();
        System.out.println("=== Fin del ataque. Total: " + total + " ===");
        System.out.println();

        return total;
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
            System.out.println("No tienes comida suficiente, pierdes 3 puntos de vida");
            System.out.println();

            resultado = "--- FASE DE AVANCE: -3 vida ---";
        } else {
            setComida(getComida() - 1);
            System.out.println("Al superar la zona consumes 1 muslo de comida");
            System.out.println();

            resultado = "--- FASE DE AVANCE: -1 comida ---";
        }

        return resultado;
    }

    public void ganarExperiencia(int px) {
        setExperiencia(getExperiencia() + px);
        if (getNivel() == 1){
            if (getExperiencia() >= 18) {
                setNivel(4);
                getDadosAtaque().add(new Dado("personaje"));
                getDadosAtaque().add(new Dado("personaje"));
                getDadosAtaque().add(new Dado("personaje"));

                System.out.println();
                System.out.println("Has subido al nivel 4!!");
                System.out.println("Ganas tres nuevos dados de personaje");
                System.out.println();

            } else if (getExperiencia() >= 12) {
                setNivel(3);
                getDadosAtaque().add(new Dado("personaje"));
                getDadosAtaque().add(new Dado("personaje"));

                System.out.println();
                System.out.println("Has subido al nivel 3!!");
                System.out.println("Ganas dos nuevos dados de personaje");
                System.out.println();
            } else if (getExperiencia() >= 6) {
                setNivel(2);
                getDadosAtaque().add(new Dado("personaje"));

                System.out.println();
                System.out.println("Has subido al nivel 2!!");
                System.out.println("Ganas un nuevo dado de personaje");
                System.out.println();
            }
        } else if (getNivel() == 2){
            if (getExperiencia() >= 18) {
                setNivel(4);
                getDadosAtaque().add(new Dado("personaje"));
                getDadosAtaque().add(new Dado("personaje"));

                System.out.println();
                System.out.println("Has subido al nivel 4!!");
                System.out.println("Ganas dos nuevos dados de personaje");
                System.out.println();

            } else if (getExperiencia() >= 12) {
                setNivel(3);
                getDadosAtaque().add(new Dado("personaje"));

                System.out.println();
                System.out.println("Has subido al nivel 3!!");
                System.out.println("Ganas un nuevo dado de personaje");
                System.out.println();
            }
        } else if (getNivel() == 3) {
            if (getExperiencia() >= 18) {
                setNivel(4);
                getDadosAtaque().add(new Dado("personaje"));

                System.out.println();
                System.out.println("Has subido al nivel 4!!");
                System.out.println("Ganas un nuevo dado de personaje");
                System.out.println();

            }
        }
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

    public void mostrarEstado() {
        System.out.println("Estado actual del personaje: " + getNombre());
        System.out.println("\tVida: " + getVida());
        System.out.println("\tOro: " + getOro());
        System.out.println("\tExperiencia: " + getExperiencia());
        System.out.println("\tNivel: " + getNivel());
        System.out.println("\tComida: " + getComida());
        System.out.println("\tArmadura: " + getArmadura());
    }
}
