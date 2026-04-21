package UD5.U5a1;

import java.util.Scanner;

public class Carta {
    public enum Tipo {
        MERCADER, TESORO, TRAMPA, ENEMIGO, EVENTO, DESCANSO
    }

    private Tipo tipo;
    private boolean ocupada;
    private String descripcion;
    private int oro;
    private int comida;
    private int armadura;
    private int vida;
    private boolean selloRojo;
    private boolean esJefe;
    private int px;
    private Dado dadoAtaque;
    private int ataque;
    private int nivel;
    private Carta enemigo;

    // Tesoro
    public Carta(String tipo, int oro, boolean selloRojo,
                 int vida, int armadura, int px) {
        setTipo(tipo);
        setOro(oro);
        setSelloRojo(selloRojo);
        setArmadura(armadura);
        setVida(vida);
        setPx(px);
    }


    //Mercader y Trampa
    public Carta(String tipo, int oro, int comida,
                 int vida, int armadura) {
        setTipo(tipo);
        setOro(oro);
        setComida(comida);
        setArmadura(armadura);
        setVida(vida);
    }

    //Enemigo Jefe
    public Carta(String tipo, boolean esJefe, int nivel, Dado dadoAtaque){
        setNivel(nivel);
        setTipo(tipo);
        setEsJefe(esJefe);
        setDadoAtaque(dadoAtaque);
        switch (nivel){
            case 1:
                setDescripcion("Lord Esqueleto");
                setVida(10);
                setAtaque(2);
                setOro(2);
                setPx(2);
                break;
            case 2:
                setDescripcion("Señor Espectral");
                setVida(15);
                setAtaque(4);
                setOro(2);
                setPx(3);
                break;
            case 3:
                setDescripcion("Demonio del Vacio");
                setVida(20);
                setAtaque(6);
                setOro(3);
                setPx(4);
                break;
            case 4:
                setDescripcion("Amo de Tinieblas");
                setVida(30);
                setAtaque(10);
                setOro(3);
                setPx(5);
        }
    }

    //Enemigo normal
    public Carta(String tipo, int nivel, Dado dadoAtaque){
        setNivel(nivel);
        setTipo(tipo);
        setDadoAtaque(dadoAtaque);
        switch (nivel){
            case 1:
                setDescripcion("Soldado Esqueleto");
                setAtaque(2);
                setPx(1);
                break;
            case 2:
                setDescripcion("Espectro Oscuro");
                setAtaque(4);
                setPx(1);
                break;
            case 3:
                setDescripcion("Acolito del Vacio");
                setAtaque(6);
                setPx(2);
                break;
            case 4:
                setDescripcion("Caballero de la Muerte");
                setAtaque(8);
                setPx(2);
                break;
            case 5:
                setDescripcion("Guardián de Sombras");
                setAtaque(10);
                setPx(3);
        }
    }

    //Descanso
    public Carta(String tipo, int px, int comida, int vida){
        setTipo(tipo);
        setPx(px);
        setVida(vida);
        setComida(comida);
    }

    //Evento
    public Carta(String tipo, int vida, int comida, int px, int armadura, int oro, Carta enemigo){
        setTipo(tipo);
        setPx(px);
        setVida(vida);
        setComida(comida);
        setEnemigo(enemigo);
        setArmadura(armadura);
        setOro(oro);
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Carta getEnemigo() {
        return enemigo;
    }

    public void setEnemigo(Carta enemigo) {
        this.enemigo = enemigo;
    }

    public boolean isEsJefe() {
        return esJefe;
    }

    public void setEsJefe(boolean esJefe) {
        this.esJefe = esJefe;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public Dado getDadoAtaque() {
        return dadoAtaque;
    }

    public void setDadoAtaque(Dado dadoAtaque) {
        this.dadoAtaque = dadoAtaque;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getArmadura() {
        return armadura;
    }

    public void setArmadura(int armadura) {
        this.armadura = armadura;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        if (vida < 0){
            this.vida = 0;
        } else {
            this.vida = vida;
        }
    }

    public boolean isSelloRojo() {
        return selloRojo;
    }

    public void setSelloRojo(boolean selloRojo) {
        this.selloRojo = selloRojo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        try {
            this.tipo = Tipo.valueOf(tipo.toUpperCase());
        } catch (Exception e) {
            System.out.println("No es un tipo valido. Por defecto se pone Trampa");
            System.out.println();

            this.tipo = Tipo.TRAMPA;
        }
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public void marcarOcupada() {
        setOcupada(true);
    }

    public boolean estaOcupada() {
        return isOcupada();
    }

    public String aplicarCarta(Personaje p, Scanner sc) {
        String resultado = "";

        switch (getTipo()) {
            case MERCADER:
                resultado = aplicarMercader(p, sc);
                break;
            case TESORO:
                resultado = aplicarTesoro(p, sc);
                break;
            case TRAMPA:
                resultado = aplicarTrampa(p);
                break;
            case ENEMIGO:
                resultado = aplicarEnemigo(p, sc);
                break;
            case DESCANSO:
                resultado = aplicarDescanso(p, sc);
                break;
            case EVENTO:
                resultado = aplicarEvento(p, sc);
        }

        marcarOcupada();

        return resultado;
    }

    public String aplicarEvento(Personaje p, Scanner sc){
        Carta enemigo = getEnemigo();
        enemigo.setAtaque(enemigo.getNivel() * 2);
        enemigo.setPx(2);

        System.out.println();
        System.out.println("=== Has encontrado una carta de evento ===");
        System.out.println();

        boolean pruebaSuperada = false;
        System.out.println("Primero pasamos a la prueba de habilidad...");
        for (Dado aux : p.getDadosAtaque()) {
            if (aux.getTipo().equalsIgnoreCase("personaje") && aux.lanzar() >= 5) {
                System.out.println("Ha salido " + aux.getValorActual());
                System.out.println("¡Has superado la prueba de habilidad!");
                pruebaSuperada = true;
                break;
            }
        }

        if (!pruebaSuperada) {
            System.out.println("No has superado la prueba de habilidad. No se realiza nada");
            System.out.println();
            return getTipo() + ": Prueba fallida | PV: " + p.getVida() + " | PX:" + p.getExperiencia()
                    + " | Oro: " + p.getOro() + " | Armadura: " + p.getArmadura();
        }
        System.out.println();

        String recurso = "";
        for (Dado aux : p.getDadosAtaque()) {
            if (aux.getTipo().equalsIgnoreCase("mazmorra")) {
                int valor = aux.lanzar();
                System.out.println("=== Dado de mazmorra lanzado ===");
                System.out.println("Resultado: " + valor);
                System.out.println();

                System.out.println("Opciones de recompensa:");
                System.out.println("1. Ración encotrada: +" + getComida() + " Comida");
                System.out.println("2. Poción de salud: +" + getVida() + " Heridas");
                System.out.println("3. Botín encontrado: +" + getOro() + " Oro");
                System.out.println("4. Yunque: +" + getPx() + "PX");
                System.out.println("5. Escudo encontrado: +" + getArmadura() + " Armadura");
                System.out.println("6. Esbirro");
                System.out.println("\tH: " + enemigo.getVida());
                System.out.println("\tD: " + enemigo.getAtaque());
                System.out.println("\tBotín: " + enemigo.getPx());
                System.out.println();

                switch (valor) {
                    case 1:
                        System.out.println("Has ganado " + getComida() + " de comida");
                        p.ganarComida(getComida());
                        recurso = "+" + getComida() + " Comida";
                        break;
                    case 2:
                        System.out.println("Has ganado " + getVida() + " de vida");
                        p.recuperarVida(getVida());
                        recurso = "+" + getVida() + " Vida";
                        break;
                    case 3:
                        System.out.println("Has ganado " + getOro() + " de oro");
                        p.ganarOro(getOro());
                        recurso = "+" + getOro() + " Oro";
                        break;
                    case 4:
                        System.out.println("Has ganado " + getPx() + "PX");
                        p.ganarExperiencia(getPx());
                        recurso = "+" + getPx() + " PX";
                        break;
                    case 5:
                        System.out.println("Has ganado " + getArmadura() + " de armadura");
                        p.aumentarArmadura(getArmadura());
                        recurso = "+" + getArmadura() + " Armadura";
                        break;
                    case 6:
                        String resultadoCombate = enemigo.aplicarEnemigo(p, sc);
                        recurso = "Combate contra " + enemigo.getDescripcion() + ":\n\t" + resultadoCombate;
                        break;

                }
                break;
            }
        }

        System.out.println();
        System.out.println("=== Fin del evento ===");
        System.out.println();

        return  "EVENTO: " + recurso + " | PX:" + p.getExperiencia() +
                " | Oro: " + p.getOro() + " | Armadura: " + p.getArmadura();
    }

    public String  aplicarEnemigo(Personaje personaje, Scanner sc){
        int ataque;
        int danioTotal = 0;

        System.out.println();
        System.out.println("==== Batalla contra " + getDescripcion() + " ====");
        System.out.println();

        while (personaje.estaVivo() && estaVivo()){
            System.out.println("=== Turno del personaje ===");
            ataque = personaje.atacar(sc);
            recibirDanio(ataque);
            danioTotal += ataque;

            System.out.println("El personaje ataca al enemigo con " + ataque + " puntos de daño");
            System.out.println("Vida actual del enemigo: " + getVida());
            System.out.println();

            if (estaVivo()){
                System.out.println("=== Contrataque del enemigo ===");
                ataque = atacar();

                if (ataque == 1){
                    System.out.println("El enemigo falla el ataque");
                    System.out.println();
                } else if (ataque == 6){
                    System.out.println("El enemigo ha hecho un crítico!!!");
                    System.out.println("Este critico afecta direcctamente al personaje sin importar su armadura " +
                            "con " + getAtaque() + " puntos de daño");
                    personaje.recibirDanio(getAtaque());

                    System.out.println("Vida actual del personaje: " + personaje.getVida());
                    System.out.println();
                } else {
                    System.out.println("El enemigo ataca con " + getAtaque() + " puntos de daño");
                    int danio = getAtaque();

                    if (personaje.getArmadura() > 0) {
                        if (personaje.getArmadura() >= danio) {
                            System.out.println("La armadura del personaje absorbe todo el daño: -" +
                                    danio + " de armadura");
                            danio = 0;
                        } else {
                            System.out.println("La armadura reduce " + personaje.getArmadura() + " puntos del daño");
                            danio -= personaje.getArmadura();
                        }
                    } else {
                        System.out.println("El personaje no tiene armadura, todo el daño afectará a la vida");
                    }

                    if (danio > 0) {
                        System.out.println("El personaje recibe " + danio + " puntos de daño a su vida");
                        personaje.recibirDanio(danio);
                    }
                    System.out.println("Vida actual del personaje: " + personaje.getVida());
                    System.out.println();

                    if (!personaje.estaVivo()){
                        System.out.println("El personaje ha muerto");
                    }
                }
            } else {
                System.out.println("Enemigo muerto");
                System.out.println();
                System.out.println("El personaje gana las siguientes recompensas:");
                if (isEsJefe()){
                    System.out.println("\t" + getOro() + " de oro");
                    System.out.println("\t" + getPx() + " de PX");

                    personaje.ganarExperiencia(getPx());
                    personaje.ganarOro(getOro());
                } else {
                    System.out.println("\t" + getPx() + " de PX");
                    personaje.ganarExperiencia(getPx());
                }
            }
        }

        if (personaje.estaVivo()){
            return getTipo() + " - " + getDescripcion() + ": Victoria (daño inflingido: " + danioTotal +
                    ") | PV: " + personaje.getVida() + " | PX:" + personaje.getExperiencia() + " | Oro: " +
                    personaje.getOro() + " | Armadura: " + personaje.getArmadura();
        }

        return getTipo() + " - " + getDescripcion() + ": Derrota (daño inflingido: " + danioTotal +
                ") | PV: " + personaje.getVida() + " | PX:" + personaje.getExperiencia() + " | Oro: " +
                personaje.getOro() + " | Armadura: " + personaje.getArmadura();
    }

    public String aplicarTesoro(Personaje p, Scanner sc) {
        String resultado = "";
        int recompensaFija;

        System.out.println();
        System.out.println("=== Has encontrado una carta de tesoro ===");

        if (isSelloRojo()) {
            System.out.println("La carta tiene sello rojo!!");
            System.out.println("Se duplica la recompensa. Ganas " + (getOro() * 2) + " de oro");
            p.ganarOro(getOro() * 2);

            recompensaFija = getOro() * 2;
        } else {
            System.out.println("Ganas " + getOro() + " de oro");
            p.ganarOro(getOro());

            recompensaFija = getOro();
        }
        System.out.println();

        boolean pruebaSuperada = false;
        System.out.println("Ahora pasamos a la prueba de habilidad...");
        for (Dado aux : p.getDadosAtaque()) {
            if (aux.getTipo().equalsIgnoreCase("personaje") && aux.lanzar() >= 5) {
                System.out.println("Ha salido " + aux.getValorActual());
                System.out.println("¡Has superado la prueba de habilidad!");
                pruebaSuperada = true;
                break;
            }
        }

        if (!pruebaSuperada) {
            System.out.println("No has superado la prueba de habilidad");
            System.out.println();

            return getTipo() + ": +" + recompensaFija + " oro | PV: " + p.getVida() + "| PX:" +
                    p.getExperiencia() + " | Oro: " + p.getOro() + " | Armadura: " + p.getArmadura();
        }
        System.out.println();


        for (Dado aux : p.getDadosAtaque()) {
            if (aux.getTipo().equalsIgnoreCase("mazmorra")) {
                int valor = aux.lanzar();
                System.out.println("=== Dado de mazmorra lanzado ===");
                System.out.println("Resultado: " + valor);
                System.out.println();

                System.out.println("Opciones de recompensa:");
                System.out.println("1. " + getOro() + " de oro / " + getArmadura() + " de armadura");
                System.out.println("2. " + (getOro() * 2) + " de oro");
                System.out.println("3. " + (getArmadura() * 2) + " de armadura");
                System.out.println("4. " + getVida() + " de vida");
                System.out.println("5. " + getArmadura() + " de armadura / " + getPx() + "PX");
                System.out.println("6. " + (getPx() * 2) + "Px");
                System.out.println();

                int eleccion = 0;
                switch (valor) {
                    case 1:
                        do {
                            System.out.println("1) " + getOro() + " de oro");
                            System.out.println("2) " + getArmadura() + " de armadura");
                            System.out.print("Elige tu recompensa: ");
                            try{
                                eleccion = sc.nextInt();
                                sc.nextLine();
                            }catch (Exception e){
                                System.out.println("Valor no válido. " + e);
                                System.out.println();
                                sc.nextLine();
                                continue;
                            }

                            if (eleccion < 1 || eleccion > 2){
                                System.out.println("Valor fuera de rango. Intentalo de nuevo");
                                System.out.println();
                            }
                        } while (eleccion < 1 || eleccion > 2);

                        if (eleccion == 1) {
                            p.ganarOro(getOro());
                            System.out.println("Has obtenido " + getOro() + " de oro");
                            resultado = "+" + getOro() + " oro";
                        } else {
                            p.aumentarArmadura(getArmadura());
                            System.out.println("Has obtenido " + getArmadura() + " de armadura");
                            resultado = "+" + getArmadura() + " armadura";
                        }
                        break;

                    case 2:
                        p.ganarOro(getOro() * 2);
                        System.out.println("Has obtenido " + (getOro() * 2) + " de oro");
                        resultado = "+" + (getOro() * 2) + " oro";
                        break;

                    case 3:
                        p.aumentarArmadura(getArmadura() * 2);
                        System.out.println("Has obtenido " + (getArmadura() * 2) + " de armadura");
                        resultado = "+" + (getArmadura() * 2) + " armadura";
                        break;

                    case 4:
                        p.recuperarVida(getVida());
                        System.out.println("Has recuperado " + getVida() + " de vida");
                        resultado = "+" + getVida() + " vida";
                        break;

                    case 5:
                        do {
                            System.out.println("1) " + getArmadura() + " de armadura");
                            System.out.println("2) " + getPx() + "PX");
                            System.out.print("Elige tu recompensa: ");
                            try {
                                eleccion = sc.nextInt();
                                sc.nextLine();
                            }catch (Exception e){
                                System.out.println("Valor no valido. " + e);
                                System.out.println();
                                sc.nextLine();
                                continue;
                            }

                            if (eleccion < 1 || eleccion > 2){
                                System.out.println("Valor fuera de rango. Intentalo de nuevo");
                                System.out.println();
                            }

                        } while (eleccion < 1 || eleccion > 2);

                        if (eleccion == 1) {
                            p.aumentarArmadura(getArmadura());
                            System.out.println("Has obtenido " + getArmadura() + " de armadura");
                            resultado = "+" + getArmadura() + " armadura";
                        } else {
                            p.ganarExperiencia(getPx());
                            System.out.println("Has obtenido " + getPx() + "PX");
                            resultado = "+" + getPx() + " PX";
                        }

                        break;

                    case 6:
                        p.ganarExperiencia(getPx() * 2);
                        System.out.println("Has obtenido " + (getPx() * 2) + "PX");
                        resultado = "+" + (getPx() * 2) + " PX";
                }

                System.out.println();
                break;
            }
        }

        System.out.println("=== Fin del tesoro ===");
        System.out.println();

        return getTipo() + ": +" + recompensaFija + " oro, " + resultado + " | PV: " + p.getVida() + "| PX:" +
                p.getExperiencia() + " | Oro: " + p.getOro() + " | Armadura: " + p.getArmadura();
    }

    public String aplicarMercader(Personaje p, Scanner sc) {
        int opcion = 0;
        String resultado;
        String accion = "";
        String intercambio = "";

        System.out.println();
        System.out.println("=== Te encuentras con un mercader ===");

        do {
            System.out.println();
            System.out.println("1. Comprar");
            System.out.println("\t1 oro --> " + getVida() + " vida");
            System.out.println("\t5 oro --> " + getArmadura() + " armadura");
            System.out.println("\t3 oro --> " + getComida() + " comida");
            System.out.println("\t6 oro --> " + (getComida() * 2) + " comida");
            System.out.println();
            System.out.println("2. Vender");
            System.out.println("\t1 armadura --> " + getOro() + " oro");
            System.out.println("\t1 comida --> " + getOro() + " oro");
            System.out.println();
            System.out.println("3. Nada");
            System.out.print("¿Qué quieres hacer? ");
            try {
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Valor no válido: " + e);
                System.out.println();
                sc.nextLine();
                continue;
            }

            if (opcion < 1 || opcion > 3) {
                System.out.println("Valor fuera de rango. Intenta de nuevo");
            }
        } while (opcion < 1 || opcion > 3);

        switch (opcion) {
            case 1:
                do {
                    opcion = 0;
                    System.out.println();
                    System.out.println("=== Comprar ===");
                    System.out.println("1. 1 oro --> " + getVida() + " vida");
                    System.out.println("2. 5 oro --> " + getArmadura() + " armadura");
                    System.out.println("3. 3 oro --> " + getComida() + " comida");
                    System.out.println("4. 6 oro --> " + (getComida() * 2) + " comida");
                    System.out.print("Selecciona lo que quieres comprar: ");
                    try {
                        opcion = sc.nextInt();
                        sc.nextLine();
                    } catch (Exception e) {
                        System.out.println("Valor no válido: " + e);
                        System.out.println();
                        sc.nextLine();
                        continue;
                    }

                    if (opcion < 1 || opcion > 4) {
                        System.out.println("Valor fuera de rango. Intenta de nuevo");
                        System.out.println();
                    }
                } while (opcion < 1 || opcion > 4);

                switch (opcion) {
                    case 1:
                        if (p.getOro() >= 1) {
                            p.perderOro(1);
                            p.recuperarVida(getVida());
                            System.out.println("Has comprado " + getVida() + " de vida por 1 oro");
                            accion = "Compra";
                            intercambio = "1 oro → " + getVida() + " vida";
                        } else {
                            System.out.println("No tienes suficiente oro");
                            accion = "Intento de compra fallido";
                        }
                        break;
                    case 2:
                        if (p.getOro() >= 5) {
                            p.perderOro(5);
                            p.aumentarArmadura(getArmadura());
                            System.out.println("Has comprado " + getArmadura() + " de armadura por 5 oro");
                            accion = "Compra";
                            intercambio = "5 oro → " + getArmadura() + " armadura";
                        } else {
                            System.out.println("No tienes suficiente oro");
                            accion = "Intento de compra fallido";
                        }
                        break;
                    case 3:
                        if (p.getOro() >= 3) {
                            p.perderOro(3);
                            p.ganarComida(getComida());
                            System.out.println("Has comprado " + getComida() + " de comida por 3 oro");
                            accion = "Compra";
                            intercambio = "3 oro → " + getComida() + " comida";
                        } else {
                            System.out.println("No tienes suficiente oro");
                            accion = "Intento de compra fallido";
                        }
                        break;
                    case 4:
                        if (p.getOro() >= 6) {
                            p.perderOro(6);
                            p.ganarComida(getComida() * 2);
                            System.out.println("Has comprado " + (getComida() * 2) + " de comida por 6 oro");
                            accion = "Compra";
                            intercambio = "6 oro → " + (getComida() * 2) + " comida";
                        } else {
                            System.out.println("No tienes suficiente oro");
                            accion = "Intento de compra fallido";
                        }
                        break;
                }
                break;

            case 2:
                do {
                    System.out.println();
                    System.out.println("=== Vender ===");
                    System.out.println("1. 1 armadura --> " + getOro() + " oro");
                    System.out.println("2. 1 comida --> " + getOro() + " oro");
                    System.out.print("Selecciona lo que quieres vender: ");

                    try {
                        opcion = sc.nextInt();
                        sc.nextLine();
                    } catch (Exception e) {
                        System.out.println("Valor no válido: " + e);
                        System.out.println();
                        sc.nextLine();
                        continue;
                    }

                    if (opcion < 1 || opcion > 2) {
                        System.out.println("Valor fuera de rango. Intenta de nuevo");
                        System.out.println();
                    }
                } while (opcion < 1 || opcion > 2);

                switch (opcion) {
                    case 1:
                        if (p.getArmadura() >= 1) {
                            p.reducirArmadura(1);
                            p.ganarOro(getOro());
                            System.out.println("Has vendido 1 armadura y ganado " + getOro() + " oro");
                            accion = "Venta";
                            intercambio = "1 armadura → " + getOro() + " oro";
                        } else {
                            System.out.println("No tienes armadura para vender");
                            accion = "Intento de venta fallido";
                        }
                        break;
                    case 2:
                        if (p.getComida() >= 1) {
                            p.perderComida(1);
                            p.ganarOro(getOro());
                            System.out.println("Has vendido 1 comida y ganado " + getOro() + " oro");
                            accion = "Venta";
                            intercambio = "1 comida → " + getOro() + " oro";
                        } else {
                            System.out.println("No tienes comida para vender");
                            accion = "Intento de venta fallido";
                        }
                        break;
                }
                break;

            case 3:
                System.out.println("No se realiza nada con el mercader");
                accion = "Nada";
                break;
        }

        resultado = "MERCADER: " + accion;
        if (!intercambio.equals("")) {
            resultado += " (" + intercambio + ")";
        }
        resultado += " | PX:" + p.getExperiencia() + " | Oro: " + p.getOro() + " | Armadura: " + p.getArmadura();

        return resultado;
    }

    public String aplicarDescanso(Personaje p, Scanner sc) {
        System.out.println();
        System.out.println("=== Has encontrado un Descanso ===");

        String resultado = "";
        String recurso = "";
        int opcion = 0;
        do {
            System.out.println("1. Reforzar Arma: +" + getPx() + "PX");
            System.out.println("2. Buscar una ración: +" + getComida() + " Comida");
            System.out.println("3. Curación: +" + getVida() + " Heridas");
            System.out.print("Elige la opción a elegir: ");
            try {
                opcion = sc.nextInt();
                sc.nextLine();
            }catch (Exception e){
                System.out.println("Valor no válido. " + e);
                System.out.println();
                sc.nextLine();

                continue;
            }

            if (opcion < 1 || opcion > 3){
                System.out.println("Valor fuera de rango. Por favor introduzca un valor dentro del rango");
                System.out.println();
            }
        }while (opcion < 1 || opcion > 3);

        switch (opcion) {
            case 1:
                System.out.println("Ganas " + getPx() + "PX de experiencia");
                p.ganarExperiencia(getPx());
                recurso = getPx() + " PX";
                break;
            case 2:
                System.out.println("Ganas " + getComida() + " de comida");
                p.ganarComida(getComida());
                recurso = getComida() + " Comida";
                break;
            case 3:
                System.out.println("Ganas " + getVida() + " de vida");
                p.recuperarVida(getVida());
                recurso = getVida() + " Vida";
        }

        System.out.println();
        System.out.println("=== Fin del descanso ===");
        System.out.println();

        resultado = "DESCANSO: +" + recurso + " | PV: " + p.getVida() + " | PX:" + p.getExperiencia() +
                " | Oro: " + p.getOro() + " | Armadura: " + p.getArmadura();

        return resultado;
    }

    public String aplicarTrampa(Personaje p) {
        System.out.println();
        System.out.println("=== Has encontrado una trampa ===");
        System.out.println("1 - 2");
        System.out.println("\tFallo --> -" + getComida() + " de comida");
        System.out.println("\tExito --> +" + getComida() + " de comida");
        System.out.println("3 - 4");
        System.out.println("\tFallo --> -" + getArmadura() + " de armadura");
        System.out.println("\tExito --> +" + getArmadura() + " de armadura");
        System.out.println("5 - 6");
        System.out.println("\tFallo --> -" + getVida() + " de vida");
        System.out.println("\tExito --> +" + getVida() + " de vida");

        System.out.println();

        boolean pruebaSuperada = false;
        for (Dado aux : p.getDadosAtaque()) {
            if (aux.getTipo().equalsIgnoreCase("personaje")) {
                int tirada = aux.lanzar();
                System.out.println("Resultado del dado de habilidad: " + tirada);
                if (tirada >= 5) {
                    System.out.println("¡Prueba superada!");
                    pruebaSuperada = true;
                } else {
                    System.out.println("No lograste superar la prueba");
                }
                break;
            }
        }

        System.out.println();

        int dadoMazmorra = -1;
        for (Dado aux : p.getDadosAtaque()) {
            if (aux.getTipo().equalsIgnoreCase("mazmorra")) {
                dadoMazmorra = aux.lanzar();
                break;
            }
        }
        System.out.println("Se ha lanzado el dado de mazmorra. El resultado es " + dadoMazmorra);
        System.out.println();

        String recurso = "";

        if (dadoMazmorra == 1 || dadoMazmorra == 2) {
            if (pruebaSuperada) {
                System.out.println("¡Éxito! Ganas " + getComida() + " de comida por haber superado la prueba de habilidad.");
                p.ganarComida(getComida());
                recurso = "+" + getComida() + " Comida";
            } else {
                System.out.println("Fallo. Pierdes " + getComida() + " de comida.");
                p.perderComida(getComida());
                recurso = "-" + getComida() + " Comida";
            }
        } else if (dadoMazmorra == 3 || dadoMazmorra == 4) {
            if (pruebaSuperada) {
                System.out.println("¡Éxito! Ganas " + getArmadura() + " de armadura por haber superado la prueba de habilidad.");
                p.aumentarArmadura(getArmadura());
                recurso = "+" + getArmadura() + " Armadura";
            } else {
                System.out.println("Fallo. Pierdes " + getArmadura() + " de armadura.");
                p.reducirArmadura(getArmadura());
                recurso = "-" + getArmadura() + " Armadura";
            }
        } else if (dadoMazmorra == 5 || dadoMazmorra == 6) {
            if (pruebaSuperada) {
                System.out.println("¡Éxito! Ganas " + getVida() + " de vida por haber superado la prueba de habilidad.");
                p.recuperarVida(getVida());
                recurso = "+" + getVida() + " Vida";
            } else {
                System.out.println("Fallo. Pierdes " + getVida() + " de vida.");
                p.recibirDanio(getVida());
                recurso = "-" + getVida() + " Vida";
            }
        }

        System.out.println();
        System.out.println("=== Fin de la trampa ===");
        System.out.println();

        return "TRAMPA: " + recurso +" | PX:" + p.getExperiencia() + " | Oro: " + p.getOro() +
                " | Armadura: " + p.getArmadura();
    }

    public void recibirDanio(int cantidad){
        setVida(getVida() - cantidad);
    }

    public boolean estaVivo(){
        return getVida() > 0;
    }

    public int atacar(){
        return getDadoAtaque().lanzar();
    }

    public void mostrarInformacion(){
        System.out.println("====================================");
        System.out.println("         INFORMACIÓN DE CARTA       ");
        System.out.println("====================================");

        switch (getTipo()) {

            case MERCADER:
                System.out.println("\n--- MERCADER ---");
                System.out.println("\nOfrece:");
                System.out.println(" - Vida: +" + getVida());
                System.out.println(" - Armadura: +" + getArmadura());
                System.out.println(" - Comida: +" + getComida());
                System.out.println(" - Oro base intercambio: " + getOro());
                break;

            case TESORO:
                System.out.println("\n--- TESORO ---");
                System.out.println("\nRecompensas:");
                System.out.println(" - Oro: " + getOro());
                System.out.println(" - Vida: +" + getVida());
                System.out.println(" - Armadura: +" + getArmadura());
                System.out.println(" - Sello rojo: " + isSelloRojo());
                break;

            case TRAMPA:
                System.out.println("\n--- TRAMPA ---");
                System.out.println("\nPosibles efectos:");
                System.out.println(" - Comida: ±" + getComida());
                System.out.println(" - Armadura: ±" + getArmadura());
                System.out.println(" - Vida: ±" + getVida());
                break;

            case ENEMIGO:
                System.out.println("\n--- ENEMIGO ---");
                System.out.println("Descripcion: " + getDescripcion());
                System.out.println("\nEstadísticas:");
                System.out.println(" - Vida: " + getVida());
                System.out.println(" - Ataque: " + getAtaque());
                System.out.println(" - Nivel: " + getNivel());
                System.out.println(" - Jefe: " + isEsJefe());
                System.out.println("\nRecompensas:");
                if (isEsJefe()){
                    System.out.println(" - Oro: " + getOro());
                    System.out.println(" - PX: " + getPx());
                } else {
                    System.out.println(" - PX: " + getPx());
                }

                break;

            case EVENTO:
                System.out.println("\n--- EVENTO ---");
                System.out.println("\nPosibles recompensas:");
                System.out.println(" - Comida: +" + getComida());
                System.out.println(" - Vida: +" + getVida());
                System.out.println(" - Oro: +" + getOro());
                System.out.println(" - Armadura: +" + getArmadura());
                System.out.println(" - PX: +" + getPx());
                System.out.println("\nPosible enemigo:");
                System.out.println(" - " + getEnemigo().getDescripcion());
                System.out.println("   Vida: " + getEnemigo().getVida());
                System.out.println("   Ataque: " + getEnemigo().getAtaque());
                break;

            case DESCANSO:
                System.out.println("\n--- DESCANSO ---");
                System.out.println("\nOpciones disponibles:");
                System.out.println(" - Recuperar vida: +" + getVida());
                System.out.println(" - Obtener comida: +" + getComida());
                System.out.println(" - Ganar PX: +" + getPx());
        }

        System.out.println("====================================\n");
    }
}